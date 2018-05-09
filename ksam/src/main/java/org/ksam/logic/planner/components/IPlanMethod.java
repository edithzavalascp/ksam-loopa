package org.ksam.logic.planner.components;

import java.util.List;

import org.ksam.model.adaptation.MonitorAdaptation;
import org.ksam.model.planData.PlanAlert;

public interface IPlanMethod {
    List<MonitorAdaptation> doPlan(PlanAlert planAlert);

    List<String> getUpdatedActiveMonitors();
}
