package org.ksam.logic.monitor.components;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.DoubleAdder;

import org.ksam.model.configuration.SumConfig;
import org.ksam.model.configuration.monitors.VariableValueCharacteristics;
import org.ksam.model.configuration.supportedvalues.VariableValueType;
import org.ksam.model.monitoringData.MonitoringData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;

public class MonitorsChecker implements IMonitorOperation {
    protected final Logger LOGGER = LoggerFactory.getLogger(getClass().getName());
    private final SumConfig config;
    private final Map<String, VariableValueCharacteristics> varsCh;
    private int minSymptoms;
    private Map<String, Integer> accumMonSymptoms;
    // private Map<String, Integer> accumMonAlert;
    // private Map<String, Integer> faultyMonitor;
    private List<String> faultyMonitors;
    private List<String> faultyMonitorsIteration;
    private List<String> recoveredMonitors;
    private boolean isMonitorFaulty;
    private boolean isAnalysisRequired;
    private boolean isBatteryLevelLow;
    private boolean isCrash;
    private Map<String, Map<String, DoubleAdder>> monitorMetrics;
    private Counter symptoms;

    private BatteryLevelInspector batteryLevelI;
    private boolean lowBatteryLevelReported;

    public MonitorsChecker(SumConfig config) {
	super();
	this.monitorMetrics = new HashMap<>();
	this.lowBatteryLevelReported = false;
	this.config = config;
	this.minSymptoms = 3;
	this.varsCh = new HashMap<>();
	this.config.getSystemConfiguration().getMonitorConfig().getMonitoringVars()
		.forEach(var -> varsCh.put(var.getVarId(), var.getValueCharacteristics()));
	this.accumMonSymptoms = new HashMap<>();
	this.faultyMonitors = new ArrayList<>();
	this.faultyMonitorsIteration = new ArrayList<>();
	this.recoveredMonitors = new ArrayList<>();
	this.batteryLevelI = new BatteryLevelInspector(
		this.config.getSystemConfiguration().getMonitorConfig().getMonitors(), this.config.getSystemId());
	// this.faultyMonitor = new HashMap<>();
	// this.accumMonAlert = new HashMap<>();
	this.config.getSystemVariables().getMonitorVars().getMonitors().forEach(monitor -> {
	    // this.accumMonAlert.put(monitor, 0);
	    this.accumMonSymptoms.put(monitor, 0);
	});
	this.isBatteryLevelLow = false;
	this.isCrash = false;

	// create metrics for monitoring variables
	this.config.getSystemConfiguration().getMonitorConfig().getMonitors().forEach(m -> {
	    Map<String, DoubleAdder> metrics = new HashMap<>();
	    for (String var : m.getMonitorAttributes().getMonitoringVars()) {
		String metricName = "ksam.me." + this.config.getSystemId() + ".monitor."
			+ m.getMonitorAttributes().getMonitorId() + ".variable." + var;
		metrics.put(var, Metrics.gauge(metricName, new DoubleAdder()));

	    }
	    this.monitorMetrics.put(m.getMonitorAttributes().getMonitorId(), metrics);

	});
	this.symptoms = Metrics.counter("ksam.monitor.symptoms");
    }

    @Override
    public MonitoringData doMonitorOperation(Map<String, String> monData) {
	MonitoringData normalizedData = new MonitoringData();
	// LOGGER.info(monData.toString());
	try {
	    ObjectMapper mapper = new ObjectMapper();
	    MonitoringData data = mapper.readValue(monData.get("monData").toString(), MonitoringData.class);
	    data.getMonitors().forEach(m -> {
		if (m.getMonitorId().equals("V2VDenm_Event")) {
		    this.isCrash = true; // report crash everytime an event is received
		}
		this.batteryLevelI.reduceBattery(m.getMonitorId());
		this.isMonitorFaulty = false;
		m.getMeasurements().forEach(measurement -> {
		    measurement.getMeasures().forEach(measure -> {
			/*********** Remove if persistence of no normalized data is required **********/
			String filePathNN = "/tmp/weka/" + m.getMonitorId() + "-" + measurement.getVarId()
				+ "NoNormalized.txt";
			try {
			    File file = new File(filePathNN);
			    if (!file.exists()) {
				file.createNewFile();
			    }

			    FileWriter fileWritter = new FileWriter(file, true);
			    BufferedWriter output = new BufferedWriter(fileWritter);
			    output.write(measure.getmTimeStamp() + " " + measure.getValue() + "\n");
			    output.close();
			} catch (IOException e) {
			    e.printStackTrace();
			}
			/*********************/

			/** First version, normalization was done in the monitor **/
			// String normalizedValue = varsCh.get(measurement.getVarId()).getValueType()
			// .getNormalizedValue(measure.getValue(), varsCh.get(measurement.getVarId()));
			// measure.setValue(normalizedValue);

			// if (normalizedValue.equals("-1")) {
			// this.isMonitorFaulty = true;
			// }
			/***********************************************************/
			this.isMonitorFaulty = this.isMonitorFaulty || VariableValueChecker
				.isValueOutOfRange(measure.getValue(), varsCh.get(measurement.getVarId()));
			// measure.setValue(measure.getValue()); // this line is not required if the
			// value passed is not normalized here.

			this.monitorMetrics.get(m.getMonitorId()).get(measurement.getVarId()).reset();

			// The variable type should be check against NOMINAL/NUMERIC, variable value
			// type has been used for
			// the sake of simplicity, i.e., varsCh are already saved for other purposes and
			// her is reused. In
			// this way we assume INT and DOUBLE are always NUMERIC
			this.monitorMetrics.get(m.getMonitorId()).get(measurement.getVarId()).add(
				!(varsCh.get(measurement.getVarId()).getValueType().equals(VariableValueType.STRING))
					? Double.valueOf(measure.getValue())
					: Double.valueOf(varsCh.get(measurement.getVarId()).getValues()
						.indexOf(measure.getValue())));
		    });
		});
		if (this.isMonitorFaulty) {
		    this.accumMonSymptoms.put(m.getMonitorId(), this.accumMonSymptoms.get(m.getMonitorId()) + 1);
		    this.symptoms.increment();
		    if (this.accumMonSymptoms.get(m.getMonitorId()) == this.minSymptoms) {
			this.accumMonSymptoms.put(m.getMonitorId(), 0);
			// this.symptoms.increment(this.symptoms.count() * -1);
			this.faultyMonitorsIteration.add(m.getMonitorId());
			if (!this.faultyMonitors.contains(m.getMonitorId())) {
			    this.faultyMonitors.add(m.getMonitorId());
			}
			// this.accumMonAlert.put(m.getMonitorId(),
			// this.accumMonAlert.get(m.getMonitorId()) + 1);
			// this.faultyMonitor.put(m.getMonitorId(),
			// this.accumMonAlert.get(m.getMonitorId()));
		    }
		} else {
		    this.accumMonSymptoms.put(m.getMonitorId(), 0);
		    this.symptoms.increment(this.symptoms.count() * -1);
		    if (this.faultyMonitors.contains(m.getMonitorId())) {
			this.faultyMonitors.remove(m.getMonitorId());
			this.recoveredMonitors.add(m.getMonitorId());
		    }

		}
	    });
	    normalizedData = data;
	} catch (IOException e) {
	    e.printStackTrace();
	}
	this.isBatteryLevelLow = !this.lowBatteryLevelReported ? this.batteryLevelI.isBatteryLevelLow() : false;
	return normalizedData;
    }

    @Override
    public boolean isAnalysisRequired() {
	this.isAnalysisRequired = this.faultyMonitorsIteration.isEmpty() && this.recoveredMonitors.isEmpty()
		&& !this.isBatteryLevelLow && !this.isCrash ? false : true;
	if (this.isBatteryLevelLow) {
	    this.symptoms.increment();
	    this.lowBatteryLevelReported = true;
	}

	return this.isAnalysisRequired;
    }

    @Override
    public List<String> getFaultyMonitors() {
	List<String> fualtyMonitorsIteration = new ArrayList<>();
	this.faultyMonitorsIteration.forEach(faultyMon -> fualtyMonitorsIteration.add(faultyMon));
	this.faultyMonitorsIteration.clear(); // instead of clear, save a historic thus a monitor recovered could be
					      // detected
	// and an alert being triggered. Particularly, when monitors are cheap.
	return fualtyMonitorsIteration;
    }

    @Override
    public List<String> getRecoveredMonitors() {
	List<String> recoveredMonitorsIteration = new ArrayList<>();
	this.recoveredMonitors.forEach(faultyMon -> recoveredMonitorsIteration.add(faultyMon));
	this.recoveredMonitors.clear(); // instead of clear, save a historic thus a monitor recovered could be
					// detected
	// and an alert being triggered. Particularly, when monitors are cheap.
	return recoveredMonitorsIteration;
    }

    @Override
    public void updateMinSymptoms(int newMinSympt) {
	this.minSymptoms = newMinSympt;
    }

    @Override
    public boolean lowBatteryLevel() {
	return this.isBatteryLevelLow ? true : false;
    }

    @Override
    public boolean isCrash() {
	boolean iCrash = this.isCrash ? true : false;
	this.isCrash = false;
	return iCrash;
    }

}
