package org.ksam.model.analysisData;

import java.util.List;
import java.util.Map.Entry;

public class AnalysisContextData {
    private String systemId;
    private List<Entry<String, Object>> contextData;

    public String getSystemId() {
	return systemId;
    }

    public void setSystemId(String systemId) {
	this.systemId = systemId;
    }

    public List<Entry<String, Object>> getContextData() {
	return contextData;
    }

    public void setContextData(List<Entry<String, Object>> contextData) {
	this.contextData = contextData;
    }

}
