package org.ksam.logic.monitor.components;

import java.util.Map;

public interface IMonitorOperation {

    Map<String, String> doMonitorOperation(Map<String, String> monData);

    boolean isAnalysisRequired();

}
