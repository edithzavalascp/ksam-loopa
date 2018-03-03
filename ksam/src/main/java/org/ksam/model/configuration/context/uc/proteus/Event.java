package org.ksam.model.configuration.context.uc.proteus;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Event {
    private String eventId;
    private List<EventTrigger> triggers;

    public String getEventId() {
	return eventId;
    }

    public void setEventId(String eventId) {
	this.eventId = eventId;
    }

    public List<EventTrigger> getTriggers() {
	return triggers;
    }

    public void setTriggers(List<EventTrigger> triggers) {
	this.triggers = triggers;
    }

}
