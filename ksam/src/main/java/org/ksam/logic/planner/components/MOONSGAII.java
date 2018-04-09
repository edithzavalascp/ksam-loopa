package org.ksam.logic.planner.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.ksam.model.adaptation.MonitorAdaptation;
import org.ksam.model.configuration.SumConfig;
import org.model.planData.PlanAlert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MOONSGAII implements IPlanMethod {
    protected final Logger LOGGER = LoggerFactory.getLogger(getClass().getName());
    private List<Entry<String, String>> algorithmParams;
    private List<Entry<String, String>> evalParams;
    private SumConfig config;
    private Map<String, Double> monsCost;

    public MOONSGAII(SumConfig config, List<Entry<String, String>> algorithmParams,
	    List<Entry<String, String>> evalParams) {
	super();
	this.config = config;
	this.algorithmParams = algorithmParams;
	this.evalParams = evalParams;
	this.monsCost = new HashMap<>();
	// TODO Check different types of costs, how to managed that?
	this.config.getSystemConfiguration().getMonitorConfig().getMonitors().forEach(m -> {
	    this.monsCost.put(m.getMonitorAttributes().getMonitorId(), m.getMonitorAttributes().getCost().getValue());
	});
    }

    @Override
    public List<MonitorAdaptation> doPlan(PlanAlert planAlert) {
	// LOGGER.info("Do analysis using " + SupportedAnlysisTechnique.ML.getLongName()
	// + "_"
	// + SupportedAlgorithm.IMWPP.getLongName());
	List<MonitorAdaptation> adaptations = new ArrayList<>();

	switch (planAlert.getAlertType()) {
	case MONITORFAULT:
	    MonitorAdaptation adapt = new MonitorAdaptation();
	    List<String> monitorsToAdd = new ArrayList<>();
	    // List<String> monitorsToRemove = new ArrayList<>(); //Don't remove faulty
	    // monitors, once they are up again they could be re-incorporated. Particularly,
	    // if they are cheaper.
	    adapt.setAdaptId("MONITORFAULT_" + planAlert.getSystemId());

	    // Substitute this simplified algorithm for the genetic one
	    for (Map.Entry<String, List<String>> entry : planAlert.getAffectedVarsAlternativeMons().entrySet()) {
		if (!entry.getValue().isEmpty()) {
		    String minCostMonId = entry.getValue().get(0);
		    for (String m : entry.getValue()) {
			minCostMonId = this.monsCost.get(m) < this.monsCost.get(minCostMonId) ? m : minCostMonId;
		    }
		    monitorsToAdd.add(minCostMonId);
		}
	    }
	    adapt.setMonitorsToAdd(monitorsToAdd);
	    adaptations.add(adapt);
	    break;
	case LOWBATTERYLEVEL:
	    // TODO Evaluate all combinations and select which to add and remove
	    // List<String> monitorsToAdd = new ArrayList<>();
	    // List<String> monitorsToRemove = new ArrayList<>();
	    break;
	default:
	    break;
	}

	return adaptations;
    }

}
