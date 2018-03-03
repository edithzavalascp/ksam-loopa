package org.ksam.model.configuration.ksambasicoperation;

import java.util.List;
import java.util.Map;

import org.ksam.model.configuration.supportedvalues.SupportedAlgorithm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Algorithm {
    private SupportedAlgorithm algorithmId;
    private List<Map<String, String>> algorithmParameters;
    private List<Map<String, String>> evaluationParameters;

    public SupportedAlgorithm getAlgorithmId() {
	return algorithmId;
    }

    public void setAlgorithmId(SupportedAlgorithm algorithmId) {
	this.algorithmId = algorithmId;
    }

    public List<Map<String, String>> getAlgorithmParameters() {
	return algorithmParameters;
    }

    public void setAlgorithmParameters(List<Map<String, String>> algorithmParameters) {
	this.algorithmParameters = algorithmParameters;
    }

    public List<Map<String, String>> getEvaluationParameters() {
	return evaluationParameters;
    }

    public void setEvaluationParameters(List<Map<String, String>> evaluationParameters) {
	this.evaluationParameters = evaluationParameters;
    }

}
