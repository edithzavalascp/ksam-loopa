package org.ksam.logic.analyzer.components;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.ksam.model.configuration.MeConfig;
import org.ksam.model.configuration.SumConfig;
import org.model.analysisData.AnalysisAlert;
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
	this.minAlerts = 3;
	this.isPlanRequired = false;
	this.varsMonsToPlan = new HashMap<>();
	this.faultyMonsIteration = new ArrayList<>();
	this.config.getKsamConfig().getAnalyzerConfig().getAnalysisTechniques()
		.forEach(tech -> tech.getAlgorithms().forEach(algorithm -> {

		    String a0Name = this.getClass().getPackage().getName() + "." + tech.getTechId().toString()
			    + algorithm.getAlgorithmId();
		    try {
			Class<?> clazz = Class.forName(a0Name);
			IAnalysisMethod a0 = (IAnalysisMethod) clazz
				.getConstructor(SumConfig.class, List.class, List.class)
				.newInstance(this.config.getSystemUnderMonitoringConfig(),
					algorithm.getAlgorithmParameters(), algorithm.getEvaluationParameters());
			this.techAlgorithms.add(a0);
		    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
			    | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
			    | SecurityException e) {
			e.printStackTrace();
		    }
		}));
	this.config.getSystemUnderMonitoringConfig().getSystemVariables().getMonitorVars().getMonitors()
		.forEach(m -> this.accumMonAlert.put(m, 0));
    }

    @Override
    public void doAnalysisOperation(AnalysisAlert analysisAlert) {

	switch (analysisAlert.getAlertType()) {
	case MONITORFAULT:
	    LOGGER.info("AlertsAnalyzer | Faulty monitors: " + analysisAlert.getFaultyMonitors().toString());
	    analysisAlert.getFaultyMonitors().forEach(faultym -> {
		this.accumMonAlert.put(faultym, this.accumMonAlert.get(faultym) + 1);
		if (this.accumMonAlert.get(faultym) == minAlerts) {
		    this.accumMonAlert.put(faultym, 0);
		    this.faultyMonsIteration.add(faultym);
		    // TODO Modify for supporting more than one algorithm, trigger a thread per
		    // method and merge results
		    this.varsMonsToPlan
			    .putAll(this.techAlgorithms.get(0).doAnalysis(faultym, analysisAlert.getAlertType()));
		}
	    });
	    break;
	case LOWBATTERYLEVEL:
	    LOGGER.info("Find alternatives for low battery level");
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
}
