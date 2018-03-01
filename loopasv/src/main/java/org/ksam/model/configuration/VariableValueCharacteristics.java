package org.ksam.model.configuration;

import java.util.List;

import org.ksam.model.configuration.supportedvalues.VariableValueType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VariableValueCharacteristics {
    private VariableValueType valueType;
    private List<String> values;

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

}
