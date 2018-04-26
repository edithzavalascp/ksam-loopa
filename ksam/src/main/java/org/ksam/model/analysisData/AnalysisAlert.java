package org.ksam.model.analysisData;

import java.util.List;

import org.ksam.model.adaptation.AlertType;

public class AnalysisAlert {
    private String systemId;
    private AlertType alertType;
    private List<String> faultyMonitors;
    private List<String> recoveredMonitors;

    public String getSystemId() {
	return systemId;
    }

    public void setSystemId(String systemId) {
	this.systemId = systemId;
    }

    public List<String> getFaultyMonitors() {
	return faultyMonitors;
    }

    public void setFaultyMonitors(List<String> faultyMonitors) {
	this.faultyMonitors = faultyMonitors;
    }

    public AlertType getAlertType() {
	return alertType;
    }

    public void setAlertType(AlertType alertType) {
	this.alertType = alertType;
    }

    public List<String> getRecoveredMonitors() {
	return recoveredMonitors;
    }

    public void setRecoveredMonitors(List<String> recoveredMonitors) {
	this.recoveredMonitors = recoveredMonitors;
    }

}
