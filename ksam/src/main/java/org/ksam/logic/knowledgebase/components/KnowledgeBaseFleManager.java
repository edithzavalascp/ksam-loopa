package org.ksam.logic.knowledgebase.components;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.ksam.api.Application;
import org.ksam.model.analysisData.AnalysisContextData;
import org.ksam.model.configuration.MeConfig;
import org.ksam.model.monitoringData.MonitoringData;
import org.ksam.model.planData.PlanData;
import org.loopa.comm.message.AMMessageBodyType;
import org.loopa.comm.message.IMessage;
import org.loopa.comm.message.LoopAElementMessageBody;
import org.loopa.comm.message.LoopAElementMessageCode;
import org.loopa.comm.message.Message;
import org.loopa.comm.message.MessageType;
import org.loopa.element.functionallogic.enactor.knowledgebase.IKnowledgeBaseFleManager;
import org.loopa.generic.element.component.ILoopAElementComponent;
import org.loopa.policy.IPolicy;
import org.loopa.policy.Policy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;

public class KnowledgeBaseFleManager implements IKnowledgeBaseFleManager {

    protected final Logger LOGGER = LoggerFactory.getLogger(getClass().getName());
    private ILoopAElementComponent owner = null;
    private IPolicy managerPolicy = new Policy("KbFleManager", null);

    private Map<String, MeConfig> configs;
    private Map<String, IKbOperation> kbOperations;

    private Counter kbCalls;

    public KnowledgeBaseFleManager() {
	this.configs = new HashMap<>();
	this.managerPolicy = new Policy(this.getClass().getName(), new HashMap<String, String>());
	this.kbOperations = new HashMap<String, IKbOperation>();
	this.kbCalls = Metrics.counter("ksam.kb.calls");
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
	this.kbCalls.increment();
	switch (monData.get("contentType")) {
	case "MonitoringData":
	    // LOGGER.info(this.getComponent().getElement().getElementId() + " | persist
	    // monitoring data");
	    try {
		ObjectMapper mapper = new ObjectMapper();
		MonitoringData data = mapper.readValue(monData.get("content"), MonitoringData.class);
		this.kbOperations.get(data.getSystemId()).updateContext(data.getContext());
		this.kbOperations.get(data.getSystemId()).persistMonitorData(data.getMonitors());

		// AnalysisContextData ctxData = new AnalysisContextData();
		// ctxData.setSystemId(data.getSystemId());
		// ctxData.setContextData(data.getContext());
		// sendContextDataToAnalyze(ctxData);
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

    private void sendContextDataToAnalyze(AnalysisContextData planCtxData) {
	ObjectMapper mapper = new ObjectMapper();
	try {
	    String jsonVarsMonData = mapper.writeValueAsString(planCtxData);
	    LoopAElementMessageBody messageContent = new LoopAElementMessageBody(AMMessageBodyType.ANALYZE.toString(),
		    jsonVarsMonData);

	    LOGGER.info(this.getComponent().getElement().getElementId() + " | send ctx data to analysis");
	    messageContent.getMessageBody().put("contentType", "AnalysisContextData");

	    String code = this.getComponent().getElement().getElementPolicy().getPolicyContent()
		    .get(LoopAElementMessageCode.MSSGOUTFL.toString());
	    IMessage mssg = new Message(this.owner.getComponentId(), this.managerPolicy.getPolicyContent().get(code),
		    Integer.parseInt(code), MessageType.REQUEST.toString(), messageContent.getMessageBody());
	    ((ILoopAElementComponent) this.owner.getComponentRecipient(mssg.getMessageTo()).getRecipient())
		    .doOperation(mssg);
	} catch (JsonProcessingException e) {
	    e.printStackTrace();
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
