package org.ksam.logic.knowledgebase.components;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import org.ksam.model.configuration.MeConfig;
import org.ksam.model.configuration.monitors.Monitor;
import org.ksam.model.configuration.monitors.VariableValueCharacteristics;
import org.ksam.model.monitoringData.RuntimeMonitorData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.micrometer.core.instrument.Metrics;

public class DataPersister implements IKbOperation {
    protected final Logger LOGGER = LoggerFactory.getLogger(getClass().getName());

    private final MeConfig config;
    private Map<String, AtomicInteger> contextMetrics;
    private List<String> activeMonitors;
    private final Map<String, VariableValueCharacteristics> varsCh;

    private WekaPersistenceManager wekaM;

    public DataPersister(MeConfig config) {
	super();
	this.config = config;
	this.contextMetrics = new HashMap<>();
	this.varsCh = new HashMap<>();
	this.config.getSystemUnderMonitoringConfig().getSystemConfiguration().getMonitorConfig().getMonitoringVars()
		.forEach(var -> varsCh.put(var.getVarId(), var.getValueCharacteristics()));
	Map<String, Monitor> monitors = new HashMap<>();
	this.config.getSystemUnderMonitoringConfig().getSystemConfiguration().getMonitorConfig().getMonitors()
		.forEach(m -> {
		    monitors.put(m.getMonitorAttributes().getMonitorId(), m);
		});
	this.wekaM = new WekaPersistenceManager(this.config.getSystemUnderMonitoringConfig().getSystemId(), monitors,
		this.config.getSystemUnderMonitoringConfig().getSystemConfiguration().getMonitorConfig()
			.getPersistenceMonitors(),
		this.config.getSystemUnderMonitoringConfig().getSystemConfiguration().getMonitorConfig()
			.getMonitoringVars(),
		this.config.getSystemUnderMonitoringConfig().getSystemVariables().getContextVars().getStates());
	this.activeMonitors = this.config.getSystemUnderMonitoringConfig().getSystemConfiguration().getMonitorConfig()
		.getInitialActiveMonitors();
	this.wekaM.updateActiveMonitors(this.activeMonitors);

	// create metrics for context variables
	this.config.getSystemUnderMonitoringConfig().getSystemVariables().getContextVars().getStates()
		.forEach(state -> {
		    String contextMetricName = "ksam.me." + this.config.getSystemUnderMonitoringConfig().getSystemId()
			    + ".context.variable." + state;
		    this.contextMetrics.put(state, Metrics.gauge(contextMetricName, new AtomicInteger()));
		    this.contextMetrics.get(state).set(0);
		});
    }

    @Override
    public void updateContext(List<Entry<String, Object>> context) {
	Map<String, Integer> newVals = this.wekaM.setContextData(context);
	newVals.forEach((k, v) -> this.contextMetrics.get(k).set(v));
    }

    @Override
    public void persistMonitorData(List<RuntimeMonitorData> data) {
	// Assume one measure per iteration. Measure value is normalized.
	Map<String, Double> monVarValue = new HashMap<>();
	data.forEach(m -> {
	    m.getMeasurements().forEach(measurement -> {
		String normalizedValue = varsCh.get(measurement.getVarId()).getValueType().getNormalizedValue(
			measurement.getMeasures().get(0).getValue(), varsCh.get(measurement.getVarId()));
		Double value = Double.valueOf(normalizedValue);
		// Double value = Double.valueOf(measurement.getMeasures().get(0).getValue());
		monVarValue.put(m.getMonitorId() + "-" + measurement.getVarId(), value);
	    });

	});
	this.wekaM.setMonitoringData(monVarValue);
    }

    @Override
    public void updateActiveMonitors(List<String> activeMonitors) {
	this.activeMonitors = activeMonitors;
	this.wekaM.updateActiveMonitors(activeMonitors);
    }
}
