package org.ksam.model.configuration.ksambasicoperation;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AnalyzerConfiguration {
    private String problemId;
    private List<AnalysisTechnique> analysisTechniques;
    private int minSymptoms;
    private String tool;
    private int toolPort;

    public String getProblemId() {
	return problemId;
    }

    public void setProblemId(String problemId) {
	this.problemId = problemId;
    }

    public List<AnalysisTechnique> getAnalysisTechniques() {
	return analysisTechniques;
    }

    public void setAnalysisTechniques(List<AnalysisTechnique> analysisTechniques) {
	this.analysisTechniques = analysisTechniques;
    }

    public int getMinSymptoms() {
	return minSymptoms;
    }

    public void setMinSymptoms(int minSymptoms) {
	this.minSymptoms = minSymptoms;
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

    public void setTool(String tool) {
	this.tool = tool;
    }

}
