package org.ksam.logic.analyzer.components;

import java.util.List;
import java.util.Map;

import org.ksam.model.adaptation.AlertType;

public interface IAnalysisMethod {
    Map<String, List<String>> doAnalysis(String monitorId, AlertType alertType);
}
