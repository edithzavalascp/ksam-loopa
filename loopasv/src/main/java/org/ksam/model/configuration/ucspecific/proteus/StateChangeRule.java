package org.ksam.model.configuration.ucspecific.proteus;

import java.util.List;

import org.ksam.model.configuration.Condition;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StateChangeRule {
    private String ruleId;
    private List<String> events;
    private List<Condition> conditions;

    public String getRuleId() {
	return ruleId;
    }

    public void setRuleId(String ruleId) {
	this.ruleId = ruleId;
    }

    public List<String> getEvents() {
	return events;
    }

    public void setEvents(List<String> events) {
	this.events = events;
    }

    public List<Condition> getConditions() {
	return conditions;
    }

    public void setConditions(List<Condition> conditions) {
	this.conditions = conditions;
    }

}
