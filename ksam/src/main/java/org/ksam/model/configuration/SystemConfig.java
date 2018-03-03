package org.ksam.model.configuration;

import org.ksam.model.configuration.context.uc.proteus.ContextVariablesConfiguration;
import org.ksam.model.configuration.monitors.MonitorUnderMonitoringConfig;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SystemConfig {
    private ContextVariablesConfiguration contextConfig;
    private MonitorUnderMonitoringConfig monitorConfig;

    public ContextVariablesConfiguration getContextConfig() {
	return contextConfig;
    }

    public void setContextConfig(ContextVariablesConfiguration contextConfig) {
	this.contextConfig = contextConfig;
    }

    public MonitorUnderMonitoringConfig getMonitorConfig() {
	return monitorConfig;
    }

    public void setMonitorConfig(MonitorUnderMonitoringConfig monitorConfig) {
	this.monitorConfig = monitorConfig;
    }

}
