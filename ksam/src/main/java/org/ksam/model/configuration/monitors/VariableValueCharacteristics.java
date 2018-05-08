package org.ksam.model.configuration.monitors;

import java.util.List;

import org.ksam.model.configuration.supportedvalues.VariableValueType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VariableValueCharacteristics {
    private VariableValueType valueType;
    private List<String> values;
    private List<String> ranges;

    public VariableValueType getValueType() {
	return valueType;
    }

    public void setValueType(VariableValueType valueType) {
	this.valueType = valueType;
    }

    public List<String> getValues() {
	return values;
    }

    public void setValues(List<String> values) {
	this.values = values;
    }

    public List<String> getRanges() {
	return ranges;
    }

    public void setRanges(List<String> ranges) {
	this.ranges = ranges;
    }

}
