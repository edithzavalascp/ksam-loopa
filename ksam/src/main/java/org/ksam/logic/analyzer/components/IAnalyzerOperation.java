package org.ksam.logic.analyzer.components;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.ksam.model.analysisData.AnalysisAlert;

public interface IAnalyzerOperation {

    void doAnalysisOperation(AnalysisAlert analysisAlert);

    boolean isPlanRequired();

    Map<String, List<String>> getVarsMonsToPlan();

    void updateMinAlerts(int newMinAlert);

    void updateAnalysisAlgorithmParameters(List<Entry<String, String>> algorithmParams,
	    List<Entry<String, String>> evalParams);
}
