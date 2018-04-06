package org.ksam.logic.monitor.components;

import java.util.List;
import java.util.Map;

import org.model.monitoringData.MonitoringData;

public interface IMonitorOperation {

    MonitoringData doMonitorOperation(Map<String, String> monData);

    boolean isAnalysisRequired();

    List<String> getFaultyMonitors();

    void updateMinSymptoms(int newMinSympt);

}
