package org.ksam.model.configuration.context.uc.proteus;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StateChange {
    private String nextStateId;
    private List<StateChangeRule> rules;

    public String getNextStateId() {
	return nextStateId;
    }

    public void setNextStateId(String nextStateId) {
	this.nextStateId = nextStateId;
    }

    public List<StateChangeRule> getRules() {
	return rules;
    }

    public void setRules(List<StateChangeRule> rules) {
	this.rules = rules;
    }

}
