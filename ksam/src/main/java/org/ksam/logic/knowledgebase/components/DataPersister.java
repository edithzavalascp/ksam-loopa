package org.ksam.logic.knowledgebase.components;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.DoubleAdder;

import org.ksam.model.configuration.MeConfig;
import org.ksam.model.configuration.monitors.Monitor;
import org.ksam.model.monitoringData.RuntimeMonitorData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.micrometer.core.instrument.Metrics;

public class DataPersister implements IKbOperation {
    protected final Logger LOGGER = LoggerFactory.getLogger(getClass().getName());

    private final MeConfig config;
    private Map<String, Map<String, DoubleAdder>> monitorMetrics;
    private List<String> activeMonitors;
    private AtomicInteger mode;
    private WekaManager wekaM;

    public DataPersister(MeConfig config) {
	super();
	this.config = config;
	this.monitorMetrics = new HashMap<>();
	Map<String, Monitor> monitors = new HashMap<>();
	this.config.getSystemUnderMonitoringConfig().getSystemConfiguration().getMonitorConfig().getMonitors()
		.forEach(m -> {
		    Map<String, DoubleAdder> metrics = new HashMap<>();
		    for (String var : m.getMonitorAttributes().getMonitoringVars()) {
			String metricName = "ksam.me." + this.config.getSystemUnderMonitoringConfig().getSystemId()
				+ ".monitor." + m.getMonitorAttributes().getMonitorId() + ".variable." + var;
			metrics.put(var, Metrics.gauge(metricName, new DoubleAdder()));

		    }
		    this.monitorMetrics.put(m.getMonitorAttributes().getMonitorId(), metrics);
		    monitors.put(m.getMonitorAttributes().getMonitorId(), m);
		});

	String modeMetricName = "ksam.me." + this.config.getSystemUnderMonitoringConfig().getSystemId() + ".mode";
	this.mode = Metrics.gauge(modeMetricName, new AtomicInteger());
	this.mode.set(0);

	this.wekaM = new WekaManager(this.config.getSystemUnderMonitoringConfig().getSystemId(), monitors, this.config
		.getSystemUnderMonitoringConfig().getSystemConfiguration().getMonitorConfig().getPersistenceMonitors());
	this.activeMonitors = this.config.getSystemUnderMonitoringConfig().getSystemConfiguration().getMonitorConfig()
		.getInitialActiveMonitors();
	this.wekaM.updateHeader(this.activeMonitors);
    }

    @Override
    public void persistMonitorData(List<RuntimeMonitorData> data) {
	// Assume one measure per iteration. Measure value is normalized.
	data.forEach(m -> {
	    m.getMeasurements().forEach(measurement -> {
		this.monitorMetrics.get(m.getMonitorId()).get(measurement.getVarId()).reset();

		Double value = Double.valueOf(measurement.getMeasures().get(0).getValue());
		this.monitorMetrics.get(m.getMonitorId()).get(measurement.getVarId()).add(value);
		this.wekaM.setMonitoringData(m.getMonitorId() + "_" + measurement.getVarId(), value);
	    });

	});
	/** Applicable just for OpenDLV case **/
	if ((this.monitorMetrics.get("imuodsimcvehicle").get("speed").intValue() == 0)
		&& (this.monitorMetrics.get("V2VDenm_Event").get("Event").intValue() == 1)) {
	    this.mode.set(1);
	}
	/**************************************/
    }

    @Override
    public void updateActiveMonitors(List<String> activeMonitors) {
	this.activeMonitors = activeMonitors;
	this.wekaM.updateHeader(this.activeMonitors);
    }
}
