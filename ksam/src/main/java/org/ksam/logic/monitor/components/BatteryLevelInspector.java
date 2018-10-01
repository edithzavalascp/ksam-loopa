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
    private final boolean reduceBattery;
    private final double batteryLimit;

    private Map<String, Double> monitorsCost;
    private DoubleAdder batteryLevel;

    public BatteryLevelInspector(List<Monitor> monitors, String systemId, int initBatteryLevele, boolean reduceBattery,
	    double batteryLimit) {
	this.monitorsCost = new HashMap<>();
	String metricName = "ksam.me." + systemId + ".monitor.batterylevelsensor.variable.batterylevel";
	batteryLevel = Metrics.gauge(metricName, new DoubleAdder());
	batteryLevel.add(initBatteryLevele);
	monitors.forEach(monitor -> this.monitorsCost.put(monitor.getMonitorAttributes().getMonitorId(),
		monitor.getMonitorAttributes().getCost().getValue()));
	this.reduceBattery = reduceBattery;
	this.batteryLimit = batteryLimit;
    }

    public void reduceBattery(String monitorId) {
	if (this.reduceBattery) {
	    double reduce = (this.monitorsCost.get(monitorId) / 100000) * -1;
	    batteryLevel.add(reduce);
	    LOGGER.info("Curent battery: " + batteryLevel.doubleValue());
	}
    }

    public boolean isBatteryLevelLow() {
	return batteryLevel.doubleValue() < this.batteryLimit ? true : false;
    }

}
