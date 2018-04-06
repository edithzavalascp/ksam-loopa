package org.ksam.logic.analyzer.components;

import java.util.List;

public interface IAnalyzerOperation {

    void doAnalysisOperation(List<String> faultyMonitors);

    boolean isPlanRequired();

    void updateMinAlerts(int newMinAlert);
}
