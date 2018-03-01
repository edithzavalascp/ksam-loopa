package org.ksam.model.monitor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ModelQuoteExample {
    private String type;

    public ModelQuoteExample() {
    }

    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
    }
}
