package org.ksam.logic.analyzer.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.ksam.model.adaptation.AlertType;
import org.ksam.model.configuration.SumConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MLIMWPP implements IAnalysisMethod {
    protected final Logger LOGGER = LoggerFactory.getLogger(getClass().getName());
    private List<Entry<String, String>> algorithmParams;
    private List<Entry<String, String>> evalParams;
    private SumConfig config;
    private Map<String, List<String>> varsMons;
    private Map<String, List<String>> monsVars;

    public MLIMWPP(SumConfig config, List<Entry<String, String>> algorithmParams,
	    List<Entry<String, String>> evalParams) {
	super();
	this.config = config;
	this.algorithmParams = algorithmParams;
	this.evalParams = evalParams;
	this.varsMons = new HashMap<>();
	this.monsVars = new HashMap<>();
	this.config.getSystemConfiguration().getMonitorConfig().getMonitors().forEach(m -> {
	    this.monsVars.put(m.getMonitorAttributes().getMonitorId(), new ArrayList<>());
	    m.getMonitorAttributes().getMonitoringVars().forEach(varMon -> {
		this.monsVars.get(m.getMonitorAttributes().getMonitorId()).add(varMon);
		if (this.varsMons.get(varMon) != null) {
		    this.varsMons.get(varMon).add(m.getMonitorAttributes().getMonitorId());
		} else {
		    List<String> monitors = new ArrayList<>();
		    monitors.add(m.getMonitorAttributes().getMonitorId());
		    this.varsMons.put(varMon, monitors);
		}

	    });
	});
    }

    @Override
    public Map<String, List<String>> doAnalysis(String monitorId, AlertType alertType) {
	// LOGGER.info("Do analysis using " + SupportedAnlysisTechnique.ML.getLongName()
	// + "_"
	// + SupportedAlgorithm.IMWPP.getLongName());
	Map<String, List<String>> alternativeMons = new HashMap<>();

	switch (alertType) {
	case MONITORFAULT:
	    for (String monVar : this.monsVars.get(monitorId)) {
		// TODO Check if it's worth monitoring a variable with models probabilities
		List<String> listAlternative = new ArrayList<>();
		this.varsMons.get(monVar).forEach(m -> listAlternative.add(m));
		listAlternative.remove(monitorId);
		alternativeMons.put(monVar, listAlternative);
	    }
	    break;
	case LOWBATTERYLEVEL:
	    // TODO Check worth variables with models probabilities
	    // if this.monsVars changes alternativeMons could change!
	    this.varsMons.forEach((k, v) -> alternativeMons.put(k, v));
	    break;
	case MONITORECOVERED:
	    for (String monVar : this.monsVars.get(monitorId)) {
		// LOGGER.info(this.varsMons.get(monVar).toString());
		List<String> listAlternative = new ArrayList<>();
		this.varsMons.get(monVar).forEach(m -> listAlternative.add(m));
		alternativeMons.put(monVar, listAlternative);
	    }
	default:
	    break;
	}

	return alternativeMons;
    }

}
