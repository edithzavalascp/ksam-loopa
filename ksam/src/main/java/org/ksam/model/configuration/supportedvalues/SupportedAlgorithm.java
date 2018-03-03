package org.ksam.model.configuration.supportedvalues;

public enum SupportedAlgorithm {
    IMWPP("Inductuve Miner with Paths Probability"), NSGAII("Non-Dominated Sorting Genetic Algorithm II");

    private final String name;

    private SupportedAlgorithm(String name) {
	this.name = name;
    }

    @Override
    public String toString() {
	return name;
    }
}
