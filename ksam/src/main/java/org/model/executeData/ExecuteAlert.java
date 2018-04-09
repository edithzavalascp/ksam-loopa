package org.model.executeData;

import java.util.List;

import org.ksam.model.adaptation.MonitorAdaptation;

public class ExecuteAlert {
    private String systemId;
    private List<MonitorAdaptation> monAdaptations;

    public String getSystemId() {
	return systemId;
    }

    public void setSystemId(String systemId) {
	this.systemId = systemId;
    }

    public List<MonitorAdaptation> getMonAdaptations() {
	return monAdaptations;
    }

    public void setMonAdaptations(List<MonitorAdaptation> monAdaptations) {
	this.monAdaptations = monAdaptations;
    }

}
