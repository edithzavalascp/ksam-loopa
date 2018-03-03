package org.ksam.model.configuration.context.uc.proteus;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ContextVariablesConfiguration {
    private List<State> states;
    private List<Event> events;

    public List<State> getStates() {
	return states;
    }

    public void setStates(List<State> states) {
	this.states = states;
    }

    public List<Event> getEvents() {
	return events;
    }

    public void setEvents(List<Event> events) {
	this.events = events;
    }

}
