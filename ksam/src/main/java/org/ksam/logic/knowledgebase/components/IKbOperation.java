package org.ksam.logic.knowledgebase.components;

import java.util.List;
import java.util.Map.Entry;

import org.ksam.model.monitoringData.RuntimeMonitorData;

public interface IKbOperation {
    void persistMonitorData(List<RuntimeMonitorData> data);

    void updateActiveMonitors(List<String> activeMonitors);

    void updateContext(List<Entry<String, Object>> context);
}
