package org.ksam.model.configuration.ksambasicoperation;

import java.util.List;
import java.util.Map.Entry;

import org.ksam.model.configuration.supportedvalues.SupportedAlgorithm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Algorithm {
    private SupportedAlgorithm algorithmId;
    private List<Entry<String, String>> algorithmParameters;
    private List<Entry<String, String>> evaluationParameters;

    public SupportedAlgorithm getAlgorithmId() {
	return algorithmId;
    }

    public void setAlgorithmId(SupportedAlgorithm algorithmId) {
	this.algorithmId = algorithmId;
    }

    public List<Entry<String, String>> getAlgorithmParameters() {
	return algorithmParameters;
    }

    public void setAlgorithmParameters(List<Entry<String, String>> algorithmParameters) {
	this.algorithmParameters = algorithmParameters;
    }

    public List<Entry<String, String>> getEvaluationParameters() {
	return evaluationParameters;
    }

    public void setEvaluationParameters(List<Entry<String, String>> evaluationParameters) {
	this.evaluationParameters = evaluationParameters;
    }

}
