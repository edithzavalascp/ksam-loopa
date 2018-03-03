package org.ksam.model.configuration.supportedvalues;

public enum SupportedAnlysisTechnique {
    ML("Machine Learning");

    private final String name;

    private SupportedAnlysisTechnique(String name) {
	this.name = name;
    }

    @Override
    public String toString() {
	return name;
    }
}
