package org.ksam.model.configuration.ksambasicoperation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KbConfiguration {
    private String monDatapersistenceFormat;
    private int persisterWindow;

    public String getMonDatapersistenceFormat() {
	return monDatapersistenceFormat;
    }

    public void setMonDatapersistenceFormat(String monDatapersistenceFormat) {
	this.monDatapersistenceFormat = monDatapersistenceFormat;
    }

    public int getPersisterWindow() {
	return persisterWindow;
    }

    public void setPersisterWindow(int persisterWindow) {
	this.persisterWindow = persisterWindow;
    }

}
