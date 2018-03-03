package org.ksam.model.configuration.ksambasicoperation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KbConfiguration {
    private String monDatapersistenceFormat;

    public String getMonDatapersistenceFormat() {
	return monDatapersistenceFormat;
    }

    public void setMonDatapersistenceFormat(String monDatapersistenceFormat) {
	this.monDatapersistenceFormat = monDatapersistenceFormat;
    }

}
