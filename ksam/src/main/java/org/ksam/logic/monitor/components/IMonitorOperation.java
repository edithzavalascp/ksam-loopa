package org.ksam.logic.monitor.components;

import java.util.List;
import java.util.Map;

import org.ksam.model.monitoringData.MonitoringData;

public interface IMonitorOperation {

    MonitoringData doMonitorOperation(Map<String, String> monData);

    boolean isAnalysisRequired();

    List<String> getFaultyMonitors();

    List<String> getRecoveredMonitors();

    boolean lowBatteryLevel();

    boolean isCrash();

    void updateMinSymptoms(int newMinSympt);

}
