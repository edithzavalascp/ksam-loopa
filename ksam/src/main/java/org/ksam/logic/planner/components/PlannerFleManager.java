package org.ksam.logic.planner.components;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.ksam.api.Application;
import org.ksam.model.configuration.MeConfig;
import org.loopa.comm.message.AMMessageBodyType;
import org.loopa.comm.message.IMessage;
import org.loopa.comm.message.LoopAElementMessageBody;
import org.loopa.comm.message.LoopAElementMessageCode;
import org.loopa.comm.message.Message;
import org.loopa.comm.message.MessageType;
import org.loopa.element.functionallogic.enactor.planner.IPlannerFleManager;
import org.loopa.generic.element.component.ILoopAElementComponent;
import org.loopa.policy.IPolicy;
import org.loopa.policy.Policy;
import org.model.executeData.ExecuteAlert;
import org.model.planData.PlanAlert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PlannerFleManager implements IPlannerFleManager {

    protected final Logger LOGGER = LoggerFactory.getLogger(getClass().getName());
    private IPolicy managerPolicy = new Policy("PlannerFleManager", null);
    private ILoopAElementComponent owner = null;

    private Map<String, MeConfig> config;
    private Map<String, IPlannerOperation> plannerOperations;

    public PlannerFleManager() {
	this.config = new HashMap<>();
	this.managerPolicy = new Policy(this.getClass().getName(), new HashMap<String, String>());
	this.plannerOperations = new HashMap<>();
    }

    @Override
    public void setConfiguration(Map<String, String> config) {
	LOGGER.info(this.getComponent().getElement().getElementId() + " | set configuration");
	if (config.containsKey("meId")) {
	    this.config.put(config.get("meId"), Application.meConfigM.getConfigs().get(config.get("meId")));
	    this.plannerOperations.put(config.get("meId"), new AlertsPlanner(this.config.get(config.get("meId"))));
	    this.managerPolicy.update(new Policy(this.managerPolicy.getPolicyOwner(), config));
	}
    }

    @Override
    public void processLogicData(Map<String, String> planData) {
	LOGGER.info(this.getComponent().getElement().getElementId() + " | receive analysis data");
	try {
	    ObjectMapper mapper = new ObjectMapper();
	    PlanAlert data = mapper.readValue(planData.get("content"), PlanAlert.class);
	    this.plannerOperations.get(data.getSystemId()).doPlanOperation(data);

	    if (this.plannerOperations.get(data.getSystemId()).isAdaptationRequired()) {
		ExecuteAlert ea = new ExecuteAlert();
		ea.setSystemId(data.getSystemId());
		ea.setMonAdaptations(this.plannerOperations.get(data.getSystemId()).getAdaptationsPlanned());
		sendPlanDataToExecute(ea);
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    private void sendPlanDataToExecute(ExecuteAlert executeAlert) {
	ObjectMapper mapper = new ObjectMapper();
	try {
	    String jsonExecuteAlert = mapper.writeValueAsString(executeAlert);
	    LoopAElementMessageBody messageContent = new LoopAElementMessageBody(AMMessageBodyType.EXECUTE.toString(),
		    jsonExecuteAlert);

	    LOGGER.info(this.getComponent().getElement().getElementId() + "| send adaptation to execute");
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
