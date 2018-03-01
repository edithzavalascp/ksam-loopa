package org.ksam.model.configuration;

import org.ksam.model.configuration.supportedvalues.MonitorType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Monitor {
    private MonitorType type;
    private MonitorAttributes monitorAttributes;

    public MonitorType getType() {
	return type;
    }

    public void setType(MonitorType type) {
	this.type = type;
    }

    public MonitorAttributes getMonitorAttributes() {
	return monitorAttributes;
    }

    public void setMonitorAttributes(MonitorAttributes monitorAttributes) {
	this.monitorAttributes = monitorAttributes;
    }

}
