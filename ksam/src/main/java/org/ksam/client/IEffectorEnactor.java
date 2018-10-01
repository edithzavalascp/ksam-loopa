package org.ksam.client;

import org.ksam.model.adaptation.MonitorAdaptation;
import org.ksam.model.configuration.MeConfig;

public interface IEffectorEnactor {

    void enact(MonitorAdaptation a);

    void setConfig(MeConfig config);

}
