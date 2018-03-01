package org.ksam.model.configuration.operation;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AnalyzerConfiguration {
    private String problemId;
    private List<AnalysisTechnique> analysisTechniques;

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

}
