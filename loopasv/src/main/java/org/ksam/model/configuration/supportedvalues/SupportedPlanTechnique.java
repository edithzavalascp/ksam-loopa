package org.ksam.model.configuration.supportedvalues;

public enum SupportedPlanTechnique {
    MOO("Multi-Objective Optimization");

    private final String name;

    private SupportedPlanTechnique(String name) {
	this.name = name;
    }

    @Override
    public String toString() {
	return name;
    }
}
