package org.ksam.model.configuration.context.uc.proteus;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class State {
    private String stateId;
    private List<String> vars;
    private List<StateChange> nextStates;

    public String getStateId() {
	return stateId;
    }

    public void setStateId(String stateId) {
	this.stateId = stateId;
    }

    public List<String> getVars() {
	return vars;
    }

    public void setVars(List<String> vars) {
	this.vars = vars;
    }

    public List<StateChange> getNextStates() {
	return nextStates;
    }

    public void setNextStates(List<StateChange> nextStates) {
	this.nextStates = nextStates;
    }

}
