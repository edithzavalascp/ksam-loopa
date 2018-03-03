package org.ksam.logic.monitor.components;

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
import org.loopa.element.functionallogic.enactor.monitor.IMonitorFleManager;
import org.loopa.generic.element.component.ILoopAElementComponent;
import org.loopa.policy.IPolicy;
import org.loopa.policy.Policy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;;

public class MonitorFleManager implements IMonitorFleManager {

    protected final Logger LOGGER = LoggerFactory.getLogger(getClass().getName());

    private ILoopAElementComponent owner = null;

    private final Map<String, MeConfig> configs;

    private final IPolicy managerPolicy;

    private final Map<String, IMonitorOperation> monOperations;

    public MonitorFleManager() {
	this.configs = new HashMap<>();
	this.managerPolicy = new Policy(this.getClass().getName(), new HashMap<String, String>());
	this.monOperations = new HashMap<String, IMonitorOperation>();
    }

    @Override
    public void setConfiguration(Map<String, String> config) {
	LOGGER.info("Set configuration");
	if (config.containsKey("meIds")) {
	    this.configs.put(config.get("meIds"), Application.meConfigM.getConfigs().get(config.get("meIds")));
	    this.monOperations.put(config.get("meIds"), new Normalizer(this.configs.get(config.get("meIds"))));
	}
	this.managerPolicy.update(new Policy(this.managerPolicy.getPolicyOwner(), config));
    }

    @Override
    public void processLogicData(Map<String, String> monData) {
	LOGGER.info("Receive data");
	sendMonDataToKB(this.monOperations.get(monData.get("systemId")).doMonitorOperation(monData));
	if (this.monOperations.get(monData.get("systemId")).isAnalysisRequired()) {
	    sendSymptomToAnalysis("SENSOR.FAILURE");
	}
    }

    private void sendSymptomToAnalysis(String symptom) {
	LOGGER.info("Send message to analysis");
	String code = this.getComponent().getElement().getElementPolicy().getPolicyContent()
		.get(LoopAElementMessageCode.MSSGOUTFL.toString());

	LoopAElementMessageBody messageContent = new LoopAElementMessageBody(AMMessageBodyType.ANALYZE.toString(),
		symptom);

	IMessage mssg = new Message(this.owner.getComponentId(), this.managerPolicy.getPolicyContent().get(code),
		Integer.parseInt(code), MessageType.REQUEST.toString(), messageContent.getMessageBody());

	((ILoopAElementComponent) this.owner.getComponentRecipient(mssg.getMessageTo()).getRecipient())
		.doOperation(mssg);
    }

    private void sendMonDataToKB(Map<String, String> normalizedData) {
	LOGGER.info("Send message to kb");
	String code = this.getComponent().getElement().getElementPolicy().getPolicyContent()
		.get(LoopAElementMessageCode.MSSGOUTFL.toString());

	LoopAElementMessageBody messageContent = new LoopAElementMessageBody(AMMessageBodyType.KB.toString(),
		normalizedData.toString().substring(1, normalizedData.toString().length() - 1));

	IMessage mssg = new Message(this.owner.getComponentId(), this.managerPolicy.getPolicyContent().get(code),
		Integer.parseInt(code), MessageType.REQUEST.toString(), messageContent.getMessageBody());

	((ILoopAElementComponent) this.owner.getComponentRecipient(mssg.getMessageTo()).getRecipient())
		.doOperation(mssg);
    }

    @Override
    public void setComponent(ILoopAElementComponent c) {
	LOGGER.info("Set component");
	this.owner = c;
    }

    @Override
    public ILoopAElementComponent getComponent() {
	return this.owner;
    }
}
