package org.ksam.model.configuration.operation;

import java.util.List;

import org.ksam.model.configuration.supportedvalues.SupportedPlanTechnique;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlanTechnique {
    private SupportedPlanTechnique techId;

    private List<Algorithm> algorithms;

    public SupportedPlanTechnique getTechId() {
	return techId;
    }

    public void setTechId(SupportedPlanTechnique techId) {
	this.techId = techId;
    }

    public List<Algorithm> getAlgorithms() {
	return algorithms;
    }

    public void setAlgorithms(List<Algorithm> algorithms) {
	this.algorithms = algorithms;
    }

}
