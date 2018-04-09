package org.ksam.logic.knowledgebase.components;

import java.util.HashMap;
import java.util.Map;

import org.ksam.model.configuration.MeConfig;
import org.loopa.element.functionallogic.enactor.knowledgebase.IKnowledgeBaseFleManager;
import org.loopa.generic.element.component.ILoopAElementComponent;
import org.loopa.policy.IPolicy;
import org.loopa.policy.Policy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KnowledgeBaseFleManager implements IKnowledgeBaseFleManager {

    protected final Logger LOGGER = LoggerFactory.getLogger(getClass().getName());
    private Map<String, MeConfig> configs;
    private IPolicy managerPolicy = new Policy("PlannerrFleManager", null);
    private ILoopAElementComponent owner = null;

    // private Map<String, IAnalyzerOperation> kbOperations; //persistence
    // implementations

    @Override
    public void setConfiguration(Map<String, String> config) {
	LOGGER.info(this.getComponent().getElement().getElementId() + " | set configuration");
	this.configs = new HashMap<>();
	this.managerPolicy = new Policy(this.getClass().getName(), new HashMap<String, String>());
    }

    @Override
    public void processLogicData(Map<String, String> monData) {
	LOGGER.info(this.getComponent().getElement().getElementId() + " | persist monitoring data");
	/**
	 * Data received can be: MonitoringData, AnalysisData, PlanData or ExecuteData
	 * Note: AnalysisAlert, PlanAlert and ExecuteAlert are sent between MAPE-K
	 * Elements, not to KB for being persisted
	 * 
	 * 
	 * 
	 * Note2: private Map<String, Integer> activeMonitors; // got to specific ME
	 * persistence implementation for MonitoringData
	 */
	// try {
	// ObjectMapper mapper = new ObjectMapper();
	// MonitoringData data = mapper.readValue(monData.get("content"),
	// MonitoringData.class);
	// data.getMonitors().forEach(m -> {
	// // LOGGER.info(this.getComponent().getElement().getElementId() + " | persist
	// // monitoring data of monitor "
	// // + m.getMonitorId());
	// });
	//
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
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
