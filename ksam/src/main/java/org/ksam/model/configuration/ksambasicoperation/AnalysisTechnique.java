package org.ksam.model.configuration.ksambasicoperation;

import java.util.List;

import org.ksam.model.configuration.supportedvalues.SupportedAnlysisTechnique;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AnalysisTechnique {
    private SupportedAnlysisTechnique techId;
    private List<Algorithm> algorithms;
    private String tool;
    private String toolHost;
    private int toolPort;
    private boolean toolOn;

    public SupportedAnlysisTechnique getTechId() {
	return techId;
    }

    public void setTechId(SupportedAnlysisTechnique techId) {
	this.techId = techId;
    }

    public List<Algorithm> getAlgorithms() {
	return algorithms;
    }

    public void setAlgorithms(List<Algorithm> algorithms) {
	this.algorithms = algorithms;
    }

    public int getToolPort() {
	return toolPort;
    }

    public void setToolPort(int toolPort) {
	this.toolPort = toolPort;
    }

    public String getTool() {
	return tool;
    }

    public boolean isToolOn() {
	return toolOn;
    }

    public void setToolOn(boolean toolOn) {
	this.toolOn = toolOn;
    }

    public void setTool(String tool) {
	this.tool = tool;
    }

    public String getToolHost() {
	return toolHost;
    }

    public void setToolHost(String toolHost) {
	this.toolHost = toolHost;
    }

}
