package org.ksam.model.configuration.ksambasicoperation;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AnalyzerConfiguration {
    private String problemId;
    private List<AnalysisTechnique> analysisTechniques;
    private int minSymptoms;
    private List<String> adaptationSupported;

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

    public List<String> getAdaptationSupported() {
	return adaptationSupported;
    }

    public void setAdaptationSupported(List<String> adaptationSupported) {
	this.adaptationSupported = adaptationSupported;
    }

}
