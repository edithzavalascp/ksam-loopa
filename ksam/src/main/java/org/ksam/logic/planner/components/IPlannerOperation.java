package org.ksam.logic.planner.components;

import java.util.List;
import java.util.Map.Entry;

import org.ksam.model.adaptation.MonitorAdaptation;
import org.model.planData.PlanAlert;

public interface IPlannerOperation {
    void doPlanOperation(PlanAlert planAlert);

    boolean isAdaptationRequired();

    List<MonitorAdaptation> getAdaptationsPlanned();

    void updatePlanAlgorithmParameters(List<Entry<String, String>> algorithmParams,
	    List<Entry<String, String>> evalParams);
}
