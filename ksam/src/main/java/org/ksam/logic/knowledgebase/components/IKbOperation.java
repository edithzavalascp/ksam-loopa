package org.ksam.logic.knowledgebase.components;

import java.util.List;

import org.ksam.model.monitoringData.RuntimeMonitorData;

public interface IKbOperation {
    void persistData(List<RuntimeMonitorData> data);
}
