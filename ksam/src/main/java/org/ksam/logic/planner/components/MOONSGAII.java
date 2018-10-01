package org.ksam.logic.planner.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import org.ksam.model.adaptation.MonitorAdaptation;
import org.ksam.model.configuration.MeConfig;
import org.ksam.model.planData.PlanAlert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.micrometer.core.instrument.Metrics;

public class MOONSGAII implements IPlanMethod {
    protected final Logger LOGGER = LoggerFactory.getLogger(getClass().getName());

    private final boolean lonelyRoad;
    private List<Entry<String, String>> algorithmParams;
    private List<Entry<String, String>> evalParams;
    private MeConfig config;
    private Map<String, Double> monsCost;

    private List<String> activeMonitors;
    private Map<String, AtomicInteger> monitorMetrics;
    // private List<String> requiredVars;

    // private IContextAnalyzer ctxPlanner;

    public MOONSGAII(MeConfig config) {
	super();
	this.config = config;
	this.lonelyRoad = this.config.getKsamConfig().getPlannerConfig().isLonelyRoad();
	this.algorithmParams = this.config.getKsamConfig().getPlannerConfig().getPlanTechniques().get(0).getAlgorithms()
		.get(0).getAlgorithmParameters();
	this.evalParams = this.config.getKsamConfig().getPlannerConfig().getPlanTechniques().get(0).getAlgorithms()
		.get(0).getEvaluationParameters();
	this.monsCost = new HashMap<>();
	this.monitorMetrics = new HashMap<>();
	// this.requiredVars = new ArrayList<>();
	this.activeMonitors = this.config.getSystemUnderMonitoringConfig().getSystemConfiguration().getMonitorConfig()
		.getInitialActiveMonitors();
	// TODO Check different types of costs, how to manage that?
	this.config.getSystemUnderMonitoringConfig().getSystemConfiguration().getMonitorConfig().getMonitors()
		.forEach(m -> {
		    this.monsCost.put(m.getMonitorAttributes().getMonitorId(),
			    m.getMonitorAttributes().getCost().getValue());
		    String metricName = "ksam.me." + this.config.getSystemUnderMonitoringConfig().getSystemId()
			    + ".monitor." + m.getMonitorAttributes().getMonitorId() + ".state";
		    if (this.activeMonitors.contains(m.getMonitorAttributes().getMonitorId())) {
			this.monitorMetrics.put(m.getMonitorAttributes().getMonitorId(),
				Metrics.gauge(metricName, new AtomicInteger(1)));
		    } else {
			this.monitorMetrics.put(m.getMonitorAttributes().getMonitorId(),
				Metrics.gauge(metricName, new AtomicInteger(0)));
		    }
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
		    if (lonelyRoad) {
			String minCostMonId = entry.getValue().get(0);
			for (String m : entry.getValue()) {
			    minCostMonId = this.monsCost.get(m) < this.monsCost.get(minCostMonId) ? m : minCostMonId;
			}
			if (!this.activeMonitors.contains(minCostMonId)) {
			    monitorsToAdd.add(minCostMonId);
			    this.activeMonitors.add(minCostMonId);
			    this.monitorMetrics.get(minCostMonId).set(1);
			}
		    } else {
			for (String m : entry.getValue()) {
			    if (!this.activeMonitors.contains(m)) {
				monitorsToAdd.add(m);
				this.activeMonitors.add(m);
				this.monitorMetrics.get(m).set(1);
			    }
			}
		    }
		}
	    }
	    adapt.setMonitorsToAdd(monitorsToAdd);
	    adapt.setMonitorsToRemove(new ArrayList<>());
	    adaptations.add(adapt);
	    break;
	case LOWBATTERYLEVEL:
	    // Simple algorithm: Evaluate all combinations and select which to add and
	    // remove
	    MonitorAdaptation adaptB = new MonitorAdaptation();
	    List<String> monitorsToAddB = new ArrayList<>();
	    List<String> monitorsToRemoveB = new ArrayList<>();
	    adaptB.setAdaptId("LOWBATTERYLEVEL" + planAlert.getSystemId());

	    // Substitute this simplified algorithm for the genetic one - take into account
	    // context in Analysis

	    List<String> allMonitors = this.config.getSystemUnderMonitoringConfig().getSystemVariables()
		    .getMonitorVars().getMonitors();

	    /** SELECT BEST ALTERNATIVE MONITORS FOR THE VARIABLES REQUIRED **/
	    for (Map.Entry<String, List<String>> entry : planAlert.getAffectedVarsAlternativeMons().entrySet()) {
		if (!entry.getValue().isEmpty()) {

		    String minCostMonId = entry.getValue().get(0);
		    for (String m : entry.getValue()) {
			minCostMonId = this.monsCost.get(m) < this.monsCost.get(minCostMonId) ? m : minCostMonId;
			allMonitors.remove(m);
		    }
		    //////////////////////////////////////////////////////////
		    if (!this.activeMonitors.contains(minCostMonId)) {
			monitorsToAddB.add(minCostMonId);
			this.activeMonitors.add(minCostMonId);
			this.monitorMetrics.get(minCostMonId).set(1);
		    }
		    //////////////////////////////////////////////////////////
		    entry.getValue().remove(minCostMonId);

		    for (String mId : entry.getValue()) {
			if (this.activeMonitors.contains(mId)) {
			    monitorsToRemoveB.add(mId);
			    this.activeMonitors.remove(mId);
			    this.monitorMetrics.get(mId).set(0);
			}
		    }
		}
	    }
	    /** REMOVE ALL MONITORS THAT ARE NOT ASSOCIATED TO REQUIRED VARIABLES **/
	    // LOGGER.info(allMonitors.toString());
	    allMonitors.forEach(monNoReq -> {
		if (this.activeMonitors.contains(monNoReq)) {
		    monitorsToRemoveB.add(monNoReq);
		    this.activeMonitors.remove(monNoReq);
		    this.monitorMetrics.get(monNoReq).set(0);
		}
	    });
	    adaptB.setMonitorsToAdd(monitorsToAddB);
	    adaptB.setMonitorsToRemove(monitorsToRemoveB);
	    adaptations.add(adaptB);
	    break;
	case MONITORECOVERED:
	    MonitorAdaptation adaptR = new MonitorAdaptation();
	    List<String> monitorsToRemove = new ArrayList<>();

	    adaptR.setAdaptId("MONITORECOVERED_" + planAlert.getSystemId());

	    // Substitute this simplified algorithm for the genetic one
	    for (Map.Entry<String, List<String>> entry : planAlert.getAffectedVarsAlternativeMons().entrySet()) {
		if (!entry.getValue().isEmpty()) {
		    String minCostMonId = entry.getValue().get(0);
		    for (String mId : entry.getValue()) {
			minCostMonId = this.monsCost.get(mId) < this.monsCost.get(minCostMonId) ? mId : minCostMonId;
		    }
		    entry.getValue().remove(minCostMonId);

		    for (String mId : entry.getValue()) {
			if (this.activeMonitors.contains(mId)) {
			    monitorsToRemove.add(mId);
			    this.activeMonitors.remove(mId);
			    this.monitorMetrics.get(mId).set(0);
			}
		    }
		}
	    }
	    adaptR.setMonitorsToAdd(new ArrayList<>());
	    adaptR.setMonitorsToRemove(monitorsToRemove);
	    adaptations.add(adaptR);
	    break;
	case ROADEVENT:
	    MonitorAdaptation adaptROAD = new MonitorAdaptation();
	    adaptROAD.setAdaptId("ROADEVENT");
	    adaptROAD.setMonitorsToAdd(new ArrayList<>());
	    adaptROAD.setMonitorsToRemove(new ArrayList<>());
	    // Decide how to adapt
	    Map<String, String> paramsToAdapt = new HashMap<>();
	    paramsToAdapt.put("traffic-frequency", "1000"); // E.g., reduce traffic factor monitoring frequency
	    adaptROAD.setParamsToAdapt(paramsToAdapt);
	    adaptations.add(adaptROAD);
	    break;
	default:
	    break;
	}

	return adaptations;
    }

    @Override
    public List<String> getUpdatedActiveMonitors() {
	List<String> aMonIteration = new ArrayList<>();
	this.activeMonitors.forEach(m -> aMonIteration.add(m));
	return aMonIteration;
    }

}
