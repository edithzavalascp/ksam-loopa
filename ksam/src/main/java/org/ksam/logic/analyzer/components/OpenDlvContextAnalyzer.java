package org.ksam.logic.analyzer.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.ksam.model.configuration.SumConfig;

public class OpenDlvContextAnalyzer implements IContextAnalyzer {

    private Map<String, Boolean> contextVars;
    private Map<String, List<String>> servicesVars;

    public OpenDlvContextAnalyzer(SumConfig config) {
	this.contextVars = new HashMap<>();
	config.getSystemVariables().getContextVars().getStates().forEach(var -> this.contextVars.put(var, false));
	this.servicesVars = new HashMap<>();
	config.getSystemConfiguration().getContextConfig().getStates()
		.forEach(service -> this.servicesVars.put(service.getStateId(), service.getVars()));
    }

    @Override
    public void updateContext(List<Entry<String, Object>> context) {
	if (context != null) {
	    context.forEach(ctx -> {
		if (ctx.getKey().equals("services")) {
		    List<String> x = (List<String>) ctx.getValue();
		    this.contextVars.keySet().forEach(var -> {
			if (x.contains(var)) {
			    this.contextVars.put(var, true);
			} else {
			    this.contextVars.put(var, false);
			}
		    });
		}
	    });
	} else {
	    this.contextVars.keySet().forEach(var -> {
		this.contextVars.put(var, false);
	    });
	}
    }

    @Override
    public List<String> getRequiredVars() {
	List<String> reqVars = new ArrayList<>();
	for (String k : this.contextVars.keySet()) {
	    if (this.contextVars.get(k)) {
		for (String var : this.servicesVars.get(k)) {
		    if (!reqVars.contains(var)) {
			reqVars.add(var);
		    }
		}
	    }
	}
	return reqVars;
    }
}
