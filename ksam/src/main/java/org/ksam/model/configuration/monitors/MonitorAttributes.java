package org.ksam.model.configuration.monitors;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MonitorAttributes {
    private String monitorId;
    private int frequency;
    private MonitoringCost cost;
    private List<String> monitoringVars;

    public String getMonitorId() {
	return monitorId;
    }

    public void setMonitorId(String monitorId) {
	this.monitorId = monitorId;
    }

    public int getFrequency() {
	return frequency;
    }

    public void setFrequency(int frequency) {
	this.frequency = frequency;
    }

    public MonitoringCost getCost() {
	return cost;
    }

    public void setCost(MonitoringCost cost) {
	this.cost = cost;
    }

    public List<String> getMonitoringVars() {
	return monitoringVars;
    }

    public void setMonitoringVars(List<String> monitoringVars) {
	this.monitoringVars = monitoringVars;
    }

}
