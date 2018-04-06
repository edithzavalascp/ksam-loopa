package org.ksam.logic.planner.components;

import java.util.Map;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlannerFleManager implements IPlannerFleManager {

    protected final Logger LOGGER = LoggerFactory.getLogger(getClass().getName());

    private IPolicy managerPolicy = new Policy("PlannerFleManager", null);

    private ILoopAElementComponent owner = null;

    @Override
    public void setConfiguration(Map<String, String> config) {
	LOGGER.info(this.getComponent().getElement().getElementId() + " | set configuration");
	this.managerPolicy.setPolicyContent(config);
    }

    @Override
    public void processLogicData(Map<String, String> monData) {
	LOGGER.info(this.getComponent().getElement().getElementId() + " | receive analysis data");
	try {
	    Thread.sleep(50);
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
	sendPlanDataToExecute();

    }

    private void sendPlanDataToExecute() {
	LOGGER.info(this.getComponent().getElement().getElementId() + "| send adaptation to execute");
	String code = this.getComponent().getElement().getElementPolicy().getPolicyContent()
		.get(LoopAElementMessageCode.MSSGOUTFL.toString());

	LoopAElementMessageBody messageContent = new LoopAElementMessageBody(AMMessageBodyType.EXECUTE.toString(),
		"PLanToExecuteId:1");

	IMessage mssg = new Message(this.owner.getComponentId(), this.managerPolicy.getPolicyContent().get(code),
		Integer.parseInt(code), MessageType.REQUEST.toString(), messageContent.getMessageBody());

	((ILoopAElementComponent) this.owner.getComponentRecipient(mssg.getMessageTo()).getRecipient())
		.doOperation(mssg);
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
