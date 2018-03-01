package org.ksam.model.configuration;

import org.ksam.model.configuration.supportedvalues.MonVarType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MonitoringVariable {
    private String varId;
    private MonVarType type;
    private VariableValueCharacteristics valueCharacteristics;

    public String getVarId() {
	return varId;
    }

    public void setVarId(String varId) {
	this.varId = varId;
    }

    public MonVarType getType() {
	return type;
    }

    public void setType(MonVarType type) {
	this.type = type;
    }

    public VariableValueCharacteristics getValueCharacteristics() {
	return valueCharacteristics;
    }

    public void setValueCharacteristics(VariableValueCharacteristics valueCharacteristics) {
	this.valueCharacteristics = valueCharacteristics;
    }

}
