package org.ksam.logic.analyzer.components;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.ksam.api.Application;
import org.ksam.model.configuration.MeConfig;
import org.ksam.model.configuration.ksambasicoperation.AnalysisTechnique;
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
import org.model.analysisData.AlertType;
import org.model.analysisData.AnalysisAlert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AnalyzerFleManager implements IAnalyzerFleManager {

    protected final Logger LOGGER = LoggerFactory.getLogger(getClass().getName());
    private IPolicy managerPolicy = new Policy("AnalyzerFleManager", null);
    private ILoopAElementComponent owner = null;

    private Map<String, MeConfig> configs;
    private Map<String, IAnalyzerOperation> analysisOperations;

    public AnalyzerFleManager() {
	this.configs = new HashMap<>();
	this.managerPolicy = new Policy(this.getClass().getName(), new HashMap<String, String>());
	this.analysisOperations = new HashMap<String, IAnalyzerOperation>();
    }

    @Override
    public void setConfiguration(Map<String, String> config) {
	LOGGER.info(this.getComponent().getElement().getElementId() + " | set configuration");
	if (config.containsKey("meId")) {
	    this.configs.put(config.get("meId"), Application.meConfigM.getConfigs().get(config.get("meId")));

	    // TODO Make it suitable for storing more than one atech+aalgorithm per Me
	    AnalysisTechnique aTech = this.configs.get(config.get("meId")).getKsamConfig().getAnalyzerConfig()
		    .getAnalysisTechniques().get(0);
	    String a0Name = "org.ksam.logic.analyzer.components." + aTech.getTechId().toString()
		    + aTech.getAlgorithms().get(0).getAlgorithmId();
	    try {
		Class<?> clazz = Class.forName(a0Name);
		IAnalyzerOperation a0 = (IAnalyzerOperation) clazz.getConstructor(MeConfig.class)
			.newInstance(this.configs.get(config.get("meId")));
		this.analysisOperations.put(config.get("meId"), a0);
	    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException
		    | InvocationTargetException | NoSuchMethodException | SecurityException e) {
		e.printStackTrace();
	    }
	    this.managerPolicy.update(new Policy(this.managerPolicy.getPolicyOwner(), config));
	}
	// else if (config.containsKey("newMinAlerts")) {
	// this.monOperations.get(config.get("systemId"))
	// .updateMinAlerts(Integer.valueOf(config.get("newMinAlerts")));
	// }
    }

    @Override
    public void processLogicData(Map<String, String> analysisData) {
	LOGGER.info(this.getComponent().getElement().getElementId() + " | receive alert");
	try {
	    ObjectMapper mapper = new ObjectMapper();
	    AnalysisAlert data = mapper.readValue(analysisData.get("content"), AnalysisAlert.class);
	    if (data.getAlertType().equals(AlertType.MONITORFAULT)) {
		// TODO This method may return possible monitors to substitute the faulty if the
		// sensor is really necessary, check current vehicle state requirements and
		// probability of state change
		this.analysisOperations.get(data.getSystemId()).doAnalysisOperation(data.getFaultyMonitors()); // this
													       // call
													       // is a
													       // test
	    }
	    if (this.analysisOperations.get(data.getSystemId()).isPlanRequired()) { // e.g., probability of sensor to be
										    // used has significantly changed
		sendAnalysisDataToPlan();
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	}
	// try {
	// Thread.sleep(50);
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }

    }

    private void sendAnalysisDataToPlan() {
	LOGGER.info(this.getComponent().getElement().getElementId() + " | send analysis data to plan");
	String code = this.getComponent().getElement().getElementPolicy().getPolicyContent()
		.get(LoopAElementMessageCode.MSSGOUTFL.toString());

	LoopAElementMessageBody messageContent = new LoopAElementMessageBody(AMMessageBodyType.PLAN.toString(),
		"ImportantEventId:1");

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
