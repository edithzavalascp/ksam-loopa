package org.ksam.model.configuration.context.uc.proteus;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ContextVariables {
    private List<String> states;
    private List<String> events;

    public List<String> getStates() {
	return states;
    }

    public void setStates(List<String> states) {
	this.states = states;
    }

    public List<String> getEvents() {
	return events;
    }

    public void setEvents(List<String> events) {
	this.events = events;
    }

}
