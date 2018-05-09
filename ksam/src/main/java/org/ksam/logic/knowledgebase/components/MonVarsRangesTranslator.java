package org.ksam.logic.knowledgebase.components;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ksam.model.configuration.monitors.MonitoringVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MonVarsRangesTranslator {
    protected final Logger LOGGER = LoggerFactory.getLogger(getClass().getName());
    private Map<String, List<String>> varsRanges;

    public MonVarsRangesTranslator(List<MonitoringVariable> monVars) {
	super();
	this.varsRanges = new HashMap<>();
	monVars.forEach(var -> this.varsRanges.put(var.getVarId(), var.getValueCharacteristics().getRanges()));
	// LOGGER.info("Ranges: " + this.varsRanges.toString());
    }

    public String getValueRange(String varName, Double x) {
	// LOGGER.info("Transform: " + varName);
	if (x == 0 || x == 1) {
	    return String.valueOf(x);

	} else {
	    for (String range : this.varsRanges.get(varName)) {
		if (x > Double.valueOf(range.split("-")[0]) && x <= Double.valueOf(range.split("-")[1])) {
		    return range;
		}
	    }
	}
	return "?"; // ? represent null/missing values for weka, in ksam these values are normalized
		    // and set to -1
    }

    public String getVarRanges(String varName) {
	return "{0," + this.varsRanges.get(varName).toString().substring(1,
		this.varsRanges.get(varName).toString().length() - 1) + ",1}";
    }

}
