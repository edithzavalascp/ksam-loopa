package org.ksam.model.configuration.ksambasicoperation;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlannerConfiguration {
    private List<PlanTechnique> planTechniques;
    private boolean lonelyRoad;

    public List<PlanTechnique> getPlanTechniques() {
	return planTechniques;
    }

    public void setPlanTechniques(List<PlanTechnique> planTechniques) {
	this.planTechniques = planTechniques;
    }

    public boolean isLonelyRoad() {
	return lonelyRoad;
    }

    public void setLonelyRoad(boolean lonelyRoad) {
	this.lonelyRoad = lonelyRoad;
    }

}
