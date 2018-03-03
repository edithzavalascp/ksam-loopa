package org.ksam.model.configuration;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AMConfiguration {
    private String autonomicManagerId;
    private Map<String, String> elementsId;
    private Map<String, String> elementsMessagesCodes;
    private Map<String, String> elementsMessageBodyTypes;

    public String getAutonomicManagerId() {
	return autonomicManagerId;
    }

    public void setAutonomicManagerId(String autonomicManagerId) {
	this.autonomicManagerId = autonomicManagerId;
    }

    public Map<String, String> getElementsId() {
	return elementsId;
    }

    public void setElementsId(HashMap<String, String> elementsId) {
	this.elementsId = elementsId;
    }

    public Map<String, String> getElementsMessagesCodes() {
	return elementsMessagesCodes;
    }

    public void setElementsMessagesCodes(HashMap<String, String> elementsMessagesCodes) {
	this.elementsMessagesCodes = elementsMessagesCodes;
    }

    public Map<String, String> getElementsMessageBodyTypes() {
	return elementsMessageBodyTypes;
    }

    public void setElementsMessageBodyTypes(HashMap<String, String> elementsMessageBodyTypes) {
	this.elementsMessageBodyTypes = elementsMessageBodyTypes;
    }
}
