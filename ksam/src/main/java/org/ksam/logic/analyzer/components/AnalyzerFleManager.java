package org.ksam.logic.analyzer.components;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.ksam.api.Application;
import org.ksam.model.analysisData.AnalysisAlert;
import org.ksam.model.analysisData.AnalysisContextData;
import org.ksam.model.configuration.MeConfig;
import org.ksam.model.planData.PlanAlert;
import org.loopa.comm.message.AMMessageBodyType;
import org.loopa.comm.message.IMessage;
import org.loopa.comm.message.LoopAElementMessageBody;
import org.loopa.comm.message.LoopAElementMessageCode;
import org.loopa.comm.message.Message;
import org.loopa.comm.message.MessageType;
import org.loopa.element.functionallogic.enactor.analyzer.IAnalyzerFleManager;
import org.loopa.generic.element.component.ILoopAElementComponent;
import org.loopa.policy.IPolicy;
import org.loopa.policy.Policy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;

public class AnalyzerFleManager implements IAnalyzerFleManager {
    protected final Logger LOGGER = LoggerFactory.getLogger(getClass().getName());
    private IPolicy managerPolicy = new Policy("AnalyzerFleManager", null);
    private ILoopAElementComponent owner = null;

    private Map<String, MeConfig> config;
    private Map<String, IAnalyzerOperation> analysisOperations;

    private Counter analyzerCalls;

    public AnalyzerFleManager() {
	this.config = new HashMap<>();
	this.managerPolicy = new Policy(this.getClass().getName(), new HashMap<String, String>());
	this.analysisOperations = new HashMap<String, IAnalyzerOperation>();
	this.analyzerCalls = Metrics.counter("ksam.analyzer.calls");
    }

    @Override
    public void setConfiguration(Map<String, String> config) {
	LOGGER.info(this.getComponent().getElement().getElementId() + " | set configuration");
	if (config.containsKey("meId")) {
	    this.config.put(config.get("meId"), Application.meConfigM.getConfigs().get(config.get("meId")));
	    this.analysisOperations.put(config.get("meId"), new AlertsAnalyzer(this.config.get(config.get("meId"))));
	    this.managerPolicy.update(new Policy(this.managerPolicy.getPolicyOwner(), config));
	} else if (config.containsKey("newMinAlerts")) {
	    this.analysisOperations.get(config.get("systemId"))
		    .updateMinAlerts(Integer.valueOf(config.get("newMinAlerts")));
	}
    }

    @Override
    public void processLogicData(Map<String, String> analysisData) {
	switch (analysisData.get("contentType")) {
	case "AnalysisAlert":
	    LOGGER.info(this.getComponent().getElement().getElementId() + " | receive alert");
	    this.analyzerCalls.increment();
	    try {
		ObjectMapper mapper = new ObjectMapper();
		AnalysisAlert data = mapper.readValue(analysisData.get("content"), AnalysisAlert.class);
		this.analysisOperations.get(data.getSystemId()).doAnalysisOperation(data);
		if (this.analysisOperations.get(data.getSystemId()).isPlanRequired()) {
		    PlanAlert pa = new PlanAlert();
		    pa.setSystemId(data.getSystemId());
		    pa.setAlertType(data.getAlertType());
		    pa.setAffectedVarsAlternativeMons(
			    this.analysisOperations.get(data.getSystemId()).getVarsMonsToPlan());
		    sendAnalysisDataToPlan(pa);
		} else {
		    LOGGER.info(this.getComponent().getElement().getElementId() + " | no plan required");
		}
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	    break;
	case "AnalysisContextData":
	    LOGGER.info(this.getComponent().getElement().getElementId() + " | receive context data");
	    try {
		ObjectMapper mapper = new ObjectMapper();
		AnalysisContextData data = mapper.readValue(analysisData.get("content"), AnalysisContextData.class);
		this.analysisOperations.get(data.getSystemId()).updateContext(data.getContextData());
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	    break;
	default:
	    break;
	}

    }

    private void sendAnalysisDataToPlan(PlanAlert varsMonToPlan) {
	ObjectMapper mapper = new ObjectMapper();
	try {
	    String jsonVarsMonData = mapper.writeValueAsString(varsMonToPlan);
	    LoopAElementMessageBody messageContent = new LoopAElementMessageBody(AMMessageBodyType.PLAN.toString(),
		    jsonVarsMonData);

	    LOGGER.info(this.getComponent().getElement().getElementId() + " | send analysis data to plan");
	    messageContent.getMessageBody().put("contentType", "PlanAlert");

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
	// LOGGER.info("Set component");
	this.owner = c;
    }

    @Override
    public ILoopAElementComponent getComponent() {
	return this.owner;
    }

}
