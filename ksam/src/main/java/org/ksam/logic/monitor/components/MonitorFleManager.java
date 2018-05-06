package org.ksam.logic.monitor.components;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ksam.api.Application;
import org.ksam.model.adaptation.AlertType;
import org.ksam.model.analysisData.AnalysisAlert;
import org.ksam.model.configuration.MeConfig;
import org.ksam.model.monitoringData.MonitoringData;
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
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;;

public class MonitorFleManager implements IMonitorFleManager {

    protected final Logger LOGGER = LoggerFactory.getLogger(getClass().getName());
    private ILoopAElementComponent owner = null;
    private IPolicy managerPolicy;

    private Map<String, MeConfig> configs;
    private Map<String, IMonitorOperation> monOperations;

    public MonitorFleManager() {
	this.configs = new HashMap<>();
	this.managerPolicy = new Policy(this.getClass().getName(), new HashMap<String, String>());
	this.monOperations = new HashMap<String, IMonitorOperation>();
    }

    @Override
    public void setConfiguration(Map<String, String> config) {
	LOGGER.info(this.getComponent().getElement().getElementId() + " | set configuration");
	if (config.containsKey("meId")) {
	    this.configs.put(config.get("meId"), Application.meConfigM.getConfigs().get(config.get("meId")));
	    this.monOperations.put(config.get("meId"),
		    new Normalizer(this.configs.get(config.get("meId")).getSystemUnderMonitoringConfig()));
	    this.managerPolicy.update(new Policy(this.managerPolicy.getPolicyOwner(), config));
	} else if (config.containsKey("newMinSymptoms")) {
	    this.monOperations.get(config.get("systemId"))
		    .updateMinSymptoms(Integer.valueOf(config.get("newMinSymptoms")));
	}

    }

    @Override
    public void processLogicData(Map<String, String> monData) {
	LOGGER.info(this.getComponent().getElement().getElementId() + " | receive monitoring data");
	MonitoringData nomalizedData = this.monOperations.get(monData.get("systemId")).doMonitorOperation(monData);
	sendMonDataToKB(nomalizedData);
	if (this.monOperations.get(monData.get("systemId")).isAnalysisRequired()) {
	    LOGGER.info("Analysis is required");
	    List<String> fm = this.monOperations.get(monData.get("systemId")).getFaultyMonitors();
	    if (!fm.isEmpty()) {
		AnalysisAlert aa = new AnalysisAlert();
		aa.setFaultyMonitors(fm);
		aa.setSystemId(monData.get("systemId"));
		aa.setAlertType(AlertType.MONITORFAULT);
		sendSymptomToAnalysis(aa);
	    }
	    List<String> rm = this.monOperations.get(monData.get("systemId")).getRecoveredMonitors();
	    if (!rm.isEmpty()) {
		AnalysisAlert aa = new AnalysisAlert();
		aa.setRecoveredMonitors(rm);
		aa.setSystemId(monData.get("systemId"));
		aa.setAlertType(AlertType.MONITORECOVERED);
		sendSymptomToAnalysis(aa);
	    }
	    if (this.monOperations.get(monData.get("systemId")).lowBatteryLevel()) {
		AnalysisAlert aa = new AnalysisAlert();
		aa.setSystemId(monData.get("systemId"));
		aa.setAlertType(AlertType.LOWBATTERYLEVEL);
		sendSymptomToAnalysis(aa);
	    }
	}
    }

    private void sendSymptomToAnalysis(AnalysisAlert symptomMonitorsStateData) {
	ObjectMapper mapper = new ObjectMapper();
	try {
	    String jsonAnalysisAlert = mapper.writeValueAsString(symptomMonitorsStateData);
	    LoopAElementMessageBody messageContent = new LoopAElementMessageBody(AMMessageBodyType.ANALYZE.toString(),
		    jsonAnalysisAlert);
	    LOGGER.info(this.getComponent().getElement().getElementId() + " | send alert to analysis");

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

    private void sendMonDataToKB(MonitoringData normalizedData) {
	ObjectMapper mapper = new ObjectMapper();
	try {
	    String jsonNomalizedData = mapper.writeValueAsString(normalizedData);
	    LoopAElementMessageBody messageContent = new LoopAElementMessageBody(AMMessageBodyType.KB.toString(),
		    jsonNomalizedData);
	    messageContent.getMessageBody().put("contentType", "MonitoringData");

	    LOGGER.info(this.getComponent().getElement().getElementId() + " | send monitoring to persist to kb");

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
