package org.ksam.model.configuration.ksambasicoperation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExecuterConfiguration {
    private String adaptationType;
    private String adaptationsFormat;
    private String meHost;
    private int mePort;
    private String cityReporterHost;
    private int cityReporterPort;

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

    public int getMePort() {
	return mePort;
    }

    public void setMePort(int mePort) {
	this.mePort = mePort;
    }

    public String getMeHost() {
	return meHost;
    }

    public void setMeHost(String meHost) {
	this.meHost = meHost;
    }

    public String getCityReporterHost() {
	return cityReporterHost;
    }

    public void setCityReporterHost(String cityReporterHost) {
	this.cityReporterHost = cityReporterHost;
    }

    public int getCityReporterPort() {
	return cityReporterPort;
    }

    public void setCityReporterPort(int cityReporterPort) {
	this.cityReporterPort = cityReporterPort;
    }

}
