package org.ksam.logic.monitor.components;

import org.ksam.model.configuration.monitors.VariableValueCharacteristics;

public class VariableValueChecker {

    public static boolean isValueOutOfRange(String value, VariableValueCharacteristics ch) {
	switch (ch.getValueType()) {
	case STRING:
	    return ch.getValues().indexOf(value) == -1 ? true : false;
	case DOUBLE:
	    // TODO For nominal values
	    return Double.valueOf(value) < Double.valueOf(ch.getValues().get(0))
		    || Double.valueOf(value) > Double.valueOf(ch.getValues().get(1)) ? true : false;
	case BOOLEAN:
	    return !(value.equals("true") || value.equals("false")) ? true : false;
	case INT:
	    // TODO For nominal values
	    return Integer.valueOf(value) < Integer.valueOf(ch.getValues().get(0))
		    || Integer.valueOf(value) > Integer.valueOf(ch.getValues().get(1)) ? true : false;
	default:
	    return true;
	}
    }
}
