package org.ksam.logic.monitor.components;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.DoubleAdder;

import org.ksam.model.configuration.monitors.Monitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.micrometer.core.instrument.Metrics;

public class BatteryLevelInspector {
    protected final Logger LOGGER = LoggerFactory.getLogger(getClass().getName());
    private DoubleAdder batteryLevel;

    private Map<String, Double> monitorsCost;

    public BatteryLevelInspector(List<Monitor> monitors, String systemId) {
	this.monitorsCost = new HashMap<>();
	String metricName = "ksam.me." + systemId + ".monitor.batterylevelsensor.variable.batterylevel";
	batteryLevel = Metrics.gauge(metricName, new DoubleAdder());
	batteryLevel.add(1); // intial battery -> move this variable to a config file
	monitors.forEach(monitor -> this.monitorsCost.put(monitor.getMonitorAttributes().getMonitorId(),
		monitor.getMonitorAttributes().getCost().getValue()));
    }

    public void reduceBattery(String monitorId) {
	// double reduce = (this.monitorsCost.get(monitorId) / 100000) * -1;
	// batteryLevel.add(reduce);
	// LOGGER.info("Curent battery: " + batteryLevel.doubleValue());
    }

    public boolean isBatteryLevelLow() {
	return batteryLevel.doubleValue() < 0.50 ? true : false;
    }

}
