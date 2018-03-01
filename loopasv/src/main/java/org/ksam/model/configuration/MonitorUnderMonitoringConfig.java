package org.ksam.model.configuration;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MonitorUnderMonitoringConfig {
    private List<Monitor> monitors;
    private List<MonitoringVariable> monitoringVars;

    public List<Monitor> getMonitors() {
	return monitors;
    }

    public void setMonitors(List<Monitor> monitors) {
	this.monitors = monitors;
    }

    public List<MonitoringVariable> getMonitoringVars() {
	return monitoringVars;
    }

    public void setMonitoringVars(List<MonitoringVariable> monitoringVars) {
	this.monitoringVars = monitoringVars;
    }

}
