package org.ksam.logic.monitor.components;

import java.util.Map;

import org.ksam.model.configuration.MeConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Normalizer implements IMonitorOperation {
    protected final Logger LOGGER = LoggerFactory.getLogger(getClass().getName());
    private final MeConfig config;

    private boolean isAnalysisRequired;

    public Normalizer(MeConfig config) {
	super();
	this.config = config;
	this.isAnalysisRequired = false;
    }

    @Override
    public Map<String, String> doMonitorOperation(Map<String, String> monData) {
	LOGGER.info("Normalize monitoring data" + monData.get("monData").toString());
	// TODO use config monitoring vars for normalizing them
	return monData;
    }

    @Override
    public boolean isAnalysisRequired() {
	return this.isAnalysisRequired;
    }

}
