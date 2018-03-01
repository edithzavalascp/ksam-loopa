package org.ksam.model.configuration.operation;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MonitorConfiguration {
    private List<String> monMechanisms;
    private List<String> monOperations;
    private int frequency;

    public List<String> getMonMechanisms() {
	return monMechanisms;
    }

    public void setMonMechanisms(List<String> monMechanisms) {
	this.monMechanisms = monMechanisms;
    }

    public List<String> getMonOperations() {
	return monOperations;
    }

    public void setMonOperations(List<String> monOperations) {
	this.monOperations = monOperations;
    }

    public int getFrequency() {
	return frequency;
    }

    public void setFrequency(int frequency) {
	this.frequency = frequency;
    }

}
