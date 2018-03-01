package org.ksam.model.configuration.operation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExecuterConfiguration {
    private String adaptationType;
    private String adaptationsFormat;

    public String getAdaptationType() {
	return adaptationType;
    }

    public void setAdaptationType(String adaptationType) {
	this.adaptationType = adaptationType;
    }

    public String getAdaptationsFormat() {
	return adaptationsFormat;
    }

    public void setAdaptationsFormat(String adaptationsFormat) {
	this.adaptationsFormat = adaptationsFormat;
    }

}
