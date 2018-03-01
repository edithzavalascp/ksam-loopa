package org.ksam.model.configuration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SumConfig {
    private String systemId;
    private SystemVariables systemVariables;
    private SystemConfig systemConfiguration;

    public String getSystemId() {
	return systemId;
    }

    public void setSystemId(String systemId) {
	this.systemId = systemId;
    }

    public SystemVariables getSystemVariables() {
	return systemVariables;
    }

    public void setSystemVariables(SystemVariables systemVariables) {
	this.systemVariables = systemVariables;
    }

    public SystemConfig getSystemConfiguration() {
	return systemConfiguration;
    }

    public void setSystemConfiguration(SystemConfig systemConfiguration) {
	this.systemConfiguration = systemConfiguration;
    }

}
