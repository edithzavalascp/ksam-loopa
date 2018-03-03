package org.model.monitoringData;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Measure {
    private long mTimeStamp;
    private String values;

    public long getmTimeStamp() {
	return mTimeStamp;
    }

    public void setmTimeStamp(long mTimeStamp) {
	this.mTimeStamp = mTimeStamp;
    }

    public String getValues() {
	return values;
    }

    public void setValues(String values) {
	this.values = values;
    }

}
