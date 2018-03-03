package org.ksam.model.configuration;

import org.ksam.model.configuration.ksambasicoperation.AnalyzerConfiguration;
import org.ksam.model.configuration.ksambasicoperation.ExecuterConfiguration;
import org.ksam.model.configuration.ksambasicoperation.KbConfiguration;
import org.ksam.model.configuration.ksambasicoperation.MonitorConfiguration;
import org.ksam.model.configuration.ksambasicoperation.PlannerConfiguration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KsamConfig {

    private MonitorConfiguration monitorConfig;
    private AnalyzerConfiguration analyzerConfig;
    private PlannerConfiguration plannerConfig;
    private ExecuterConfiguration executerConfig;
    private KbConfiguration kbConfig;

    public MonitorConfiguration getMonitorConfig() {
	return monitorConfig;
    }

    public void setMonitorConfig(MonitorConfiguration monitorConfig) {
	this.monitorConfig = monitorConfig;
    }

    public AnalyzerConfiguration getAnalyzerConfig() {
	return analyzerConfig;
    }

    public void setAnalyzerConfig(AnalyzerConfiguration analyzerConfig) {
	this.analyzerConfig = analyzerConfig;
    }

    public PlannerConfiguration getPlannerConfig() {
	return plannerConfig;
    }

    public void setPlannerConfig(PlannerConfiguration plannerConfig) {
	this.plannerConfig = plannerConfig;
    }

    public ExecuterConfiguration getExecuterConfig() {
	return executerConfig;
    }

    public void setExecuterConfig(ExecuterConfiguration executerConfig) {
	this.executerConfig = executerConfig;
    }

    public KbConfiguration getKbConfig() {
	return kbConfig;
    }

    public void setKbConfig(KbConfiguration kbConfig) {
	this.kbConfig = kbConfig;
    }

}
