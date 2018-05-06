package org.ksam.model.planData;

import java.util.List;

public class PlanData {
    private String systemId;
    private List<String> activeMonitors;

    public String getSystemId() {
	return systemId;
    }

    public void setSystemId(String systemId) {
	this.systemId = systemId;
    }

    public List<String> getActiveMonitors() {
	return activeMonitors;
    }

    public void setActiveMonitors(List<String> activeMonitors) {
	this.activeMonitors = activeMonitors;
    }
}
