package org.model.planData;

import java.util.List;
import java.util.Map;

import org.ksam.model.adaptation.AlertType;

public class PlanAlert {
    private String systemId;
    private AlertType alertType;
    private Map<String, List<String>> affectedVarsAlternativeMons;

    public String getSystemId() {
	return systemId;
    }

    public void setSystemId(String systemId) {
	this.systemId = systemId;
    }

    public AlertType getAlertType() {
	return alertType;
    }

    public void setAlertType(AlertType alertType) {
	this.alertType = alertType;
    }

    public Map<String, List<String>> getAffectedVarsAlternativeMons() {
	return affectedVarsAlternativeMons;
    }

    public void setAffectedVarsAlternativeMons(Map<String, List<String>> affectedVarsAlternativeMons) {
	this.affectedVarsAlternativeMons = affectedVarsAlternativeMons;
    }
}
