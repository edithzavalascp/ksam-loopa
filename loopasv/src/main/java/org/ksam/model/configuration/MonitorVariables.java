package org.ksam.model.configuration;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MonitorVariables {
    private List<String> monitors;
    private List<String> monitoringVars;

    public List<String> getMonitors() {
	return monitors;
    }

    public void setMonitors(List<String> monitors) {
	this.monitors = monitors;
    }

    public List<String> getMonitoringVars() {
	return monitoringVars;
    }

    public void setMonitoringVars(List<String> monitoringVars) {
	this.monitoringVars = monitoringVars;
    }

}
