package org.model.analysisData;

import java.util.List;

import org.ksam.model.adaptation.AlertType;

public class AnalysisAlert {
    private String systemId;
    private AlertType alertType;
    private List<String> faultyMonitors;

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

}
