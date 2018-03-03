package org.ksam.model.configuration.context.uc.proteus;

import org.ksam.model.configuration.supportedvalues.Operator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Condition {
    private String varId;
    private Operator operator;
    private String value;

    public String getVarId() {
	return varId;
    }

    public void setVarId(String varId) {
	this.varId = varId;
    }

    public Operator getOperator() {
	return operator;
    }

    public void setOperator(Operator operator) {
	this.operator = operator;
    }

    public String getValue() {
	return value;
    }

    public void setValue(String value) {
	this.value = value;
    }

}
