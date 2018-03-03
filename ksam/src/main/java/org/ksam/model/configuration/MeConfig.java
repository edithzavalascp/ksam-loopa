package org.ksam.model.configuration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MeConfig {
    private KsamConfig ksamConfig;
    private SumConfig systemUnderMonitoringConfig;

    public KsamConfig getKsamConfig() {
	return ksamConfig;
    }

    public void setKsamConfig(KsamConfig ksamConfig) {
	this.ksamConfig = ksamConfig;
    }

    public SumConfig getSystemUnderMonitoringConfig() {
	return systemUnderMonitoringConfig;
    }

    public void setSystemUnderMonitoringConfig(SumConfig systemUnderMonitoringConfig) {
	this.systemUnderMonitoringConfig = systemUnderMonitoringConfig;
    }
}
