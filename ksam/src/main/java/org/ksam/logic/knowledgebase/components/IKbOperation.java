package org.ksam.logic.knowledgebase.components;

import java.util.List;

import org.ksam.model.monitoringData.RuntimeMonitorData;

public interface IKbOperation {
    void persistMonitorData(List<RuntimeMonitorData> data);

    void updateActiveMonitors(List<String> activeMonitors);
}
