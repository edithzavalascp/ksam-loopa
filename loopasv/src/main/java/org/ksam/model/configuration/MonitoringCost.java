package org.ksam.model.configuration;

import org.ksam.model.configuration.supportedvalues.MonitorCostType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MonitoringCost {
    private MonitorCostType type;
    private double value;

    public MonitorCostType getType() {
	return type;
    }

    public void setType(MonitorCostType type) {
	this.type = type;
    }

    public double getValue() {
	return value;
    }

    public void setValue(double value) {
	this.value = value;
    }

}
