package org.ksam.model.configuration.operation;

import java.util.List;

import org.ksam.model.configuration.supportedvalues.SupportedAnlysisTechnique;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AnalysisTechnique {
    private SupportedAnlysisTechnique techId;

    private List<Algorithm> algorithms;

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

}
