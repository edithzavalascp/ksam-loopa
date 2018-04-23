package org.ksam.logic.knowledgebase.components;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.DoubleAdder;

import org.ksam.model.configuration.MeConfig;
import org.model.monitoringData.RuntimeMonitorData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.micrometer.core.instrument.Metrics;

public class DataPersister implements IKbOperation {
    protected final Logger LOGGER = LoggerFactory.getLogger(getClass().getName());
    private final MeConfig config;
    private Map<String, Map<String, DoubleAdder>> monitorMetrics;

    public DataPersister(MeConfig config) {
	super();

	this.config = config;
	monitorMetrics = new HashMap<>();
	this.config.getSystemUnderMonitoringConfig().getSystemConfiguration().getMonitorConfig().getMonitors()
		.forEach(m -> {
		    Map<String, DoubleAdder> metrics = new HashMap<>();
		    for (String var : m.getMonitorAttributes().getMonitoringVars()) {
			String metricName = "ksam.me." + this.config.getSystemUnderMonitoringConfig().getSystemId()
				+ ".monitor." + m.getMonitorAttributes().getMonitorId() + ".variable." + var;
			metrics.put(var, Metrics.gauge(metricName, new DoubleAdder()));

		    }
		    monitorMetrics.put(m.getMonitorAttributes().getMonitorId(), metrics);
		});
    }

    @Override
    public void persistData(List<RuntimeMonitorData> data) {
	// Assume one measure per iteration. Measure value is normalized.
	data.forEach(m -> {
	    m.getMeasurements().forEach(measurement -> {
		monitorMetrics.get(m.getMonitorId()).get(measurement.getVarId()).reset();
		// LOGGER.info(measurement.getMeasures().get(0).getValue());
		monitorMetrics.get(m.getMonitorId()).get(measurement.getVarId())
			.add(Double.valueOf(measurement.getMeasures().get(0).getValue()));
	    });

	});

    }

}
