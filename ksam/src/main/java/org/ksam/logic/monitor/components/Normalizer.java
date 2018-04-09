package org.ksam.logic.monitor.components;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ksam.model.configuration.SumConfig;
import org.ksam.model.configuration.monitors.VariableValueCharacteristics;
import org.model.monitoringData.MonitoringData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Normalizer implements IMonitorOperation {
    protected final Logger LOGGER = LoggerFactory.getLogger(getClass().getName());
    private final SumConfig config;
    private final Map<String, VariableValueCharacteristics> varsCh;
    private int minSymptoms;
    private Map<String, Integer> accumMonSymptoms;
    // private Map<String, Integer> accumMonAlert;
    // private Map<String, Integer> faultyMonitor;
    private List<String> faultyMonitors;
    private boolean isMonitorFaulty;
    private boolean isAnalysisRequired;

    public Normalizer(SumConfig config) {
	super();
	this.config = config;
	this.minSymptoms = 10;
	this.varsCh = new HashMap<>();
	this.config.getSystemConfiguration().getMonitorConfig().getMonitoringVars()
		.forEach(var -> varsCh.put(var.getVarId(), var.getValueCharacteristics()));
	this.accumMonSymptoms = new HashMap<>();
	this.faultyMonitors = new ArrayList<>();
	// this.faultyMonitor = new HashMap<>();
	// this.accumMonAlert = new HashMap<>();
	this.config.getSystemVariables().getMonitorVars().getMonitors().forEach(monitor -> {
	    // this.accumMonAlert.put(monitor, 0);
	    this.accumMonSymptoms.put(monitor, 0);
	});
    }

    @Override
    public MonitoringData doMonitorOperation(Map<String, String> monData) {
	MonitoringData normalizedData = new MonitoringData();

	try {
	    ObjectMapper mapper = new ObjectMapper();
	    MonitoringData data = mapper.readValue(monData.get("monData").toString(), MonitoringData.class);

	    data.getMonitors().forEach(m -> {
		this.isMonitorFaulty = false;
		m.getMeasurements().forEach(measurement -> {
		    measurement.getMeasures().forEach(measure -> {
			String normalizedValue = varsCh.get(measurement.getVarId()).getValueType()
				.getNormalizedValue(measure.getValue(), varsCh.get(measurement.getVarId()));
			measure.setValue(normalizedValue);
			// LOGGER.info("Normalizer | process monitoring data of monitor " +
			// m.getMonitorId());
			if (normalizedValue.equals("-1")) {
			    this.isMonitorFaulty = true;
			}
		    });
		});
		if (isMonitorFaulty) {
		    this.accumMonSymptoms.put(m.getMonitorId(), this.accumMonSymptoms.get(m.getMonitorId()) + 1);
		    if (this.accumMonSymptoms.get(m.getMonitorId()) == this.minSymptoms) {
			this.accumMonSymptoms.put(m.getMonitorId(), 0);
			this.faultyMonitors.add(m.getMonitorId());
			// this.accumMonAlert.put(m.getMonitorId(),
			// this.accumMonAlert.get(m.getMonitorId()) + 1);
			// this.faultyMonitor.put(m.getMonitorId(),
			// this.accumMonAlert.get(m.getMonitorId()));
		    }
		} else {
		    this.accumMonSymptoms.put(m.getMonitorId(), 0);
		    // this.accumMonAlert.put(m.getMonitorId(), 0);
		}
	    });
	    normalizedData = data;
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return normalizedData;
    }

    @Override
    public boolean isAnalysisRequired() {
	this.isAnalysisRequired = this.faultyMonitors.isEmpty() ? false : true;
	return this.isAnalysisRequired;
    }

    @Override
    public List<String> getFaultyMonitors() {
	List<String> fualtyMonitorsIteration = new ArrayList<>();
	this.faultyMonitors.forEach(faultyMon -> fualtyMonitorsIteration.add(faultyMon));
	this.faultyMonitors.clear(); // instead of clear, save a historic thus a monitor recovered could be detected
				     // and an alert being triggered. Particularly, when monitors are cheap.
	return fualtyMonitorsIteration;
    }

    @Override
    public void updateMinSymptoms(int newMinSympt) {
	this.minSymptoms = newMinSympt;
    }
}
