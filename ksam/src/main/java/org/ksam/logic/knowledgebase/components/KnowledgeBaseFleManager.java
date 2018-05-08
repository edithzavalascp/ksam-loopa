package org.ksam.logic.knowledgebase.components;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.ksam.api.Application;
import org.ksam.model.configuration.MeConfig;
import org.ksam.model.monitoringData.MonitoringData;
import org.ksam.model.planData.PlanData;
import org.loopa.element.functionallogic.enactor.knowledgebase.IKnowledgeBaseFleManager;
import org.loopa.generic.element.component.ILoopAElementComponent;
import org.loopa.policy.IPolicy;
import org.loopa.policy.Policy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class KnowledgeBaseFleManager implements IKnowledgeBaseFleManager {

    protected final Logger LOGGER = LoggerFactory.getLogger(getClass().getName());
    private ILoopAElementComponent owner = null;
    private IPolicy managerPolicy = new Policy("KbFleManager", null);

    private Map<String, MeConfig> configs;
    private Map<String, IKbOperation> kbOperations;

    public KnowledgeBaseFleManager() {
	this.configs = new HashMap<>();
	this.managerPolicy = new Policy(this.getClass().getName(), new HashMap<String, String>());
	this.kbOperations = new HashMap<String, IKbOperation>();
    }

    @Override
    public void setConfiguration(Map<String, String> config) {
	LOGGER.info(this.getComponent().getElement().getElementId() + " | set configuration");
	if (config.containsKey("meId")) {
	    this.configs.put(config.get("meId"), Application.meConfigM.getConfigs().get(config.get("meId")));
	    this.kbOperations.put(config.get("meId"), new DataPersister(this.configs.get(config.get("meId"))));
	    this.managerPolicy.update(new Policy(this.managerPolicy.getPolicyOwner(), config));
	}

    }

    @Override
    public void processLogicData(Map<String, String> monData) {
	switch (monData.get("contentType")) {
	case "MonitoringData":
	    LOGGER.info(this.getComponent().getElement().getElementId() + " | persist monitoring data");
	    try {
		ObjectMapper mapper = new ObjectMapper();
		MonitoringData data = mapper.readValue(monData.get("content"), MonitoringData.class);
		this.kbOperations.get(data.getSystemId()).persistMonitorData(data.getMonitors());
		this.kbOperations.get(data.getSystemId()).updateContext(data.getContext());
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	    break;
	case "PlanData":
	    try {
		ObjectMapper mapper = new ObjectMapper();
		PlanData data = mapper.readValue(monData.get("content"), PlanData.class);
		this.kbOperations.get(data.getSystemId()).updateActiveMonitors(data.getActiveMonitors());
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	    break;
	default:
	    break;
	}
    }

    @Override
    public void setComponent(ILoopAElementComponent c) {
	this.owner = c;
    }

    @Override
    public ILoopAElementComponent getComponent() {
	return this.owner;
    }
}
