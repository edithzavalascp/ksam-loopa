package org.ksam.logic.executer.components;

import java.io.IOException;
import java.util.Map;

import org.ksam.model.executeData.ExecuteAlert;
import org.loopa.comm.message.AMMessageBodyType;
import org.loopa.comm.message.IMessage;
import org.loopa.comm.message.LoopAElementMessageBody;
import org.loopa.comm.message.LoopAElementMessageCode;
import org.loopa.comm.message.Message;
import org.loopa.comm.message.MessageType;
import org.loopa.element.functionallogic.enactor.executer.IExecuterFleManager;
import org.loopa.generic.element.component.ILoopAElementComponent;
import org.loopa.policy.IPolicy;
import org.loopa.policy.Policy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ExecuterFleManager implements IExecuterFleManager {

    protected final Logger LOGGER = LoggerFactory.getLogger(getClass().getName());

    private IPolicy managerPolicy = new Policy("ExecuterFleManager", null);

    private ILoopAElementComponent owner = null;

    @Override
    public void setConfiguration(Map<String, String> config) {
	LOGGER.info(this.getComponent().getElement().getElementId() + " | set configuration");
	this.managerPolicy.setPolicyContent(config);
    }

    @Override
    public void processLogicData(Map<String, String> monData) {
	LOGGER.info(this.getComponent().getElement().getElementId() + " | receive adaptation to execute");
	try {
	    ObjectMapper mapper = new ObjectMapper();
	    ExecuteAlert data = mapper.readValue(monData.get("content"), ExecuteAlert.class);
	    sendExecuteDataToMEEffect(data);
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    private void sendExecuteDataToMEEffect(ExecuteAlert data) {
	ObjectMapper mapper = new ObjectMapper();
	try {
	    String jsonExecuteAlert = mapper.writeValueAsString(data);
	    LoopAElementMessageBody messageContent = new LoopAElementMessageBody(
		    AMMessageBodyType.ADAPTATION.toString() + data.getSystemId(), jsonExecuteAlert);

	    LOGGER.info(this.getComponent().getElement().getElementId() + " | send adaptation to effect");

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
