package org.ksam.model.configuration.supportedvalues;

import org.ksam.model.configuration.monitors.VariableValueCharacteristics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum VariableValueType {

    STRING {

	@Override
	public String getNormalizedValue(String value, VariableValueCharacteristics ch) {
	    if (ch.getValues().indexOf(value) == -1) {
		return "-1";
	    }
	    return String.valueOf((1.0 / ch.getValues().size()) * (ch.getValues().indexOf(value) + 1));
	}

    },
    DOUBLE {

	@Override
	public String getNormalizedValue(String value, VariableValueCharacteristics ch) {
	    // TODO For nominal values
	    if (Double.valueOf(value) < Double.valueOf(ch.getValues().get(0))) {
		return "-1";
	    }
	    return String.valueOf((Double.valueOf(value) - Double.valueOf(ch.getValues().get(0)))
		    / (Double.valueOf(ch.getValues().get(1)) - Double.valueOf(ch.getValues().get(0))));
	}

    },
    BOOLEAN {

	@Override
	public String getNormalizedValue(String value, VariableValueCharacteristics ch) {
	    if (!(value.equals("0") || value.equals("1"))) {
		return "-1";
	    }
	    return value;
	}

    },
    INT {

	@Override
	public String getNormalizedValue(String value, VariableValueCharacteristics ch) {
	    // TODO For nominal values
	    if (Integer.valueOf(value) < Integer.valueOf(ch.getValues().get(0))) {
		return "-1";
	    }
	    return String.valueOf((Integer.valueOf(value) - Integer.valueOf(ch.getValues().get(0)))
		    / (Integer.valueOf(ch.getValues().get(1)) - Integer.valueOf(ch.getValues().get(0))));
	}

    };
    protected final Logger LOGGER = LoggerFactory.getLogger(getClass().getName());

    public abstract String getNormalizedValue(String value, VariableValueCharacteristics ch);

}
