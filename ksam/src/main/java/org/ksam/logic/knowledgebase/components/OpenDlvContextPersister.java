package org.ksam.logic.knowledgebase.components;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpenDlvContextPersister implements IContextPersister {
    protected final Logger LOGGER = LoggerFactory.getLogger(getClass().getName());
    private List<String> contextVars;

    public OpenDlvContextPersister(List<String> contextVars) {
	this.contextVars = contextVars;
    }

    @Override
    public Map<String, Integer> updateContext(List<Entry<String, Object>> context) {
	// LOGGER.info("Receive context: " + context.toString());
	Map<String, Integer> ctxVarsVals = new HashMap<>();
	if (context != null) {
	    context.forEach(ctx -> {
		if (ctx.getKey().equals("services")) {
		    List<String> x = (List<String>) ctx.getValue();
		    this.contextVars.forEach(var -> {
			if (x.contains(var)) {
			    ctxVarsVals.put(var, 1);
			} else {
			    ctxVarsVals.put(var, 0);
			}
		    });
		}
	    });
	} else {
	    this.contextVars.forEach(var -> {
		ctxVarsVals.put(var, 0);
	    });
	}
	return ctxVarsVals;
    }

}
