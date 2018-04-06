package org.ksam.logic.analyzer.components;

import java.util.List;
import java.util.Map;

import org.ksam.model.configuration.MeConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MLIMWPP implements IAnalyzerOperation {
    protected final Logger LOGGER = LoggerFactory.getLogger(getClass().getName());
    private final MeConfig config;
    private Map<String, Integer> accumMonAlert;
    private int minAlerts;

    public MLIMWPP(MeConfig config) {
	this.config = config;
    }

    @Override
    public void doAnalysisOperation(List<String> analysisAlert) {
	LOGGER.info("Test to trigger: MLIMWPP apply "
		+ this.config.getKsamConfig().getAnalyzerConfig().getAnalysisTechniques().get(0).getTechId()
			.getLongName()
		+ "_" + this.config.getKsamConfig().getAnalyzerConfig().getAnalysisTechniques().get(0).getAlgorithms()
			.get(0).getAlgorithmId().getLongName()
		+ " although for faulty " + analysisAlert + " is not required");

    }

    @Override
    public boolean isPlanRequired() {
	// TODO Auto-generated method stub
	return true;
    }

    @Override
    public void updateMinAlerts(int newMinAlert) {
	this.minAlerts = newMinAlert;
    }

}
