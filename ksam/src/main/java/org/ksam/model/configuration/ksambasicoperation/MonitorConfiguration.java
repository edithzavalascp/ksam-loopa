package org.ksam.model.configuration.ksambasicoperation;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MonitorConfiguration {
    private List<String> monMechanisms;
    private List<String> monOperations;
    private int frequency;
    private int minSymptoms;
    private double initBatteryLevel;
    private boolean reduceBattery;
    private double batteryLimit;

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

    public int getMinSymptoms() {
	return minSymptoms;
    }

    public void setMinSymptoms(int minSymptoms) {
	this.minSymptoms = minSymptoms;
    }

    public boolean isReduceBattery() {
	return reduceBattery;
    }

    public void setReduceBattery(boolean reduceBattery) {
	this.reduceBattery = reduceBattery;
    }

    public double getBatteryLimit() {
	return batteryLimit;
    }

    public void setBatteryLimit(double batteryLimit) {
	this.batteryLimit = batteryLimit;
    }

    public double getInitBatteryLevel() {
	return initBatteryLevel;
    }

    public void setInitBatteryLevel(double initBatteryLevel) {
	this.initBatteryLevel = initBatteryLevel;
    }

}
