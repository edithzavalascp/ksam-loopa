package org.ksam.model.configuration.context.uc.proteus;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EventTrigger {

    private String triggerId;
    private List<Condition> conditions;

    public String getTriggerId() {
	return triggerId;
    }

    public void setTriggerId(String triggerId) {
	this.triggerId = triggerId;
    }

    public List<Condition> getConditions() {
	return conditions;
    }

    public void setConditions(List<Condition> conditions) {
	this.conditions = conditions;
    }

}
