package org.ksam.model.configuration.monitors;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MonitorUnderMonitoringConfig {
    private List<String> initialActiveMonitors;
    private List<String> persistenceMonitors;
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

    public List<String> getInitialActiveMonitors() {
	return initialActiveMonitors;
    }

    public void setInitialActiveMonitors(List<String> initialActiveMonitors) {
	this.initialActiveMonitors = initialActiveMonitors;
    }

    public List<String> getPersistenceMonitors() {
	return persistenceMonitors;
    }

    public void setPersistenceMonitors(List<String> persistenceMonitors) {
	this.persistenceMonitors = persistenceMonitors;
    }

}
