package org.ksam.model.configuration;

import org.ksam.model.configuration.context.uc.proteus.ContextVariables;
import org.ksam.model.configuration.monitors.MonitorVariables;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SystemVariables {
    private ContextVariables contextVars;
    private MonitorVariables monitorVars;

    public ContextVariables getContextVars() {
	return contextVars;
    }

    public void setContextVars(ContextVariables contextVars) {
	this.contextVars = contextVars;
    }

    public MonitorVariables getMonitorVars() {
	return monitorVars;
    }

    public void setMonitorVars(MonitorVariables monitorVars) {
	this.monitorVars = monitorVars;
    }

}
