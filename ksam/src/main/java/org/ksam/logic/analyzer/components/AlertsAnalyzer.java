package org.ksam.logic.analyzer.components;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.ksam.model.analysisData.AnalysisAlert;
import org.ksam.model.configuration.MeConfig;
import org.ksam.model.configuration.supportedvalues.SupportedAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlertsAnalyzer implements IAnalyzerOperation {
    protected final Logger LOGGER = LoggerFactory.getLogger(getClass().getName());
    private final MeConfig config;
    private Map<String, Integer> accumMonAlert;
    private int minAlerts;
    private List<IAnalysisMethod> techAlgorithms;
    private boolean isPlanRequired;
    private Map<String, List<String>> varsMonsToPlan;
    private List<String> faultyMonsIteration;

    public AlertsAnalyzer(MeConfig config) {
	this.config = config;
	this.techAlgorithms = new ArrayList<>();
	this.accumMonAlert = new HashMap<>();
	this.minAlerts = this.config.getKsamConfig().getAnalyzerConfig().getMinSymptoms();
	this.isPlanRequired = false;
	this.varsMonsToPlan = new HashMap<>();
	this.faultyMonsIteration = new ArrayList<>();
	this.config.getKsamConfig().getAnalyzerConfig().getAnalysisTechniques()
		.forEach(tech -> tech.getAlgorithms().forEach(algorithm -> {
		    if (algorithm.getAlgorithmId().equals(SupportedAlgorithm.JRip)) {
			String a0Name = this.getClass().getPackage().getName() + "." + tech.getTechId().toString()
				+ algorithm.getAlgorithmId();
			try {
			    Class<?> clazz = Class.forName(a0Name);
			    IAnalysisMethod a0 = (IAnalysisMethod) clazz.getConstructor(MeConfig.class)
				    .newInstance(this.config);
			    this.techAlgorithms.add(a0);
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			    e.printStackTrace();
			}
		    }
		}));
	this.config.getSystemUnderMonitoringConfig().getSystemVariables().getMonitorVars().getMonitors()
		.forEach(m -> this.accumMonAlert.put(m, 0));

    }

    @Override
    public void doAnalysisOperation(AnalysisAlert analysisAlert) {

	switch (analysisAlert.getAlertType()) {
	case MONITORFAULT:
	    LOGGER.info(
		    "AlertsAnalyzer | Faulty (or unuseful) monitors: " + analysisAlert.getFaultyMonitors().toString());
	    analysisAlert.getFaultyMonitors().forEach(faultym -> {
		this.accumMonAlert.put(faultym, this.accumMonAlert.get(faultym) + 1);
		if (this.accumMonAlert.get(faultym) == minAlerts) {
		    this.accumMonAlert.put(faultym, 0);
		    // TODO Modify for supporting more than one algorithm, trigger a thread per
		    // method and merge results
		    this.varsMonsToPlan
			    .putAll(this.techAlgorithms.get(0).doAnalysis(faultym, analysisAlert.getAlertType()));
		    if (!this.varsMonsToPlan.isEmpty()) {
			this.faultyMonsIteration.add(faultym);
		    }
		}
	    });
	    break;
	case LOWBATTERYLEVEL:
	    LOGGER.info("AlertsAnalyzer | Low battery alert");
	    this.varsMonsToPlan = this.techAlgorithms.get(0).doAnalysis(null, analysisAlert.getAlertType());
	    break;
	case MONITORECOVERED:
	    LOGGER.info("AlertsAnalyzer | Recovered monitors: " + analysisAlert.getRecoveredMonitors().toString());
	    analysisAlert.getRecoveredMonitors().forEach(recoveredm -> {
		this.varsMonsToPlan
			.putAll(this.techAlgorithms.get(0).doAnalysis(recoveredm, analysisAlert.getAlertType()));
	    });
	    break;
	case ROADEVENT:
	    LOGGER.info("AlertsAnalyzer | ROADEVENT (CRASH)");
	    this.varsMonsToPlan = this.techAlgorithms.get(0).doAnalysis(null, analysisAlert.getAlertType());
	    break;
	default:
	    break;
	}

    }

    @Override
    public boolean isPlanRequired() {
	this.isPlanRequired = this.varsMonsToPlan.isEmpty() ? false : true;
	return this.isPlanRequired;
    }

    @Override
    public void updateMinAlerts(int newMinAlert) {
	this.minAlerts = newMinAlert;
    }

    @Override
    public Map<String, List<String>> getVarsMonsToPlan() {
	Map<String, List<String>> varsMonsToPlanIteration = new HashMap<>();
	this.varsMonsToPlan.forEach((k, v) -> varsMonsToPlanIteration.put(k, v));
	this.varsMonsToPlan.clear();
	// check any variables contains faulty monitors
	// LOGGER.info("FaultyMonsI: " + this.faultyMonsIteration.toString());
	varsMonsToPlanIteration.forEach((k, v) -> this.faultyMonsIteration.forEach(fmon -> v.remove(fmon)));
	this.faultyMonsIteration.clear();
	return varsMonsToPlanIteration;
    }

    @Override
    public void updateAnalysisAlgorithmParameters(List<Entry<String, String>> algorithmParams,
	    List<Entry<String, String>> evalParams) {
	// TODO Call corresponding algorithm and change parameters, we will probably
	// need systemId here

    }

    @Override
    public void updateContext(List<Entry<String, Object>> context) {
	this.techAlgorithms.get(0).updateContext(context);
    }
}
