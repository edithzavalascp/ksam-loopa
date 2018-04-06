package org.ksam.logic.knowledgebase.components;

import java.util.Map;

import org.loopa.element.functionallogic.enactor.knowledgebase.IKnowledgeBaseFleManager;
import org.loopa.generic.element.component.ILoopAElementComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KnowledgeBaseFleManager implements IKnowledgeBaseFleManager {

    protected final Logger LOGGER = LoggerFactory.getLogger(getClass().getName());

    private ILoopAElementComponent owner = null;

    @Override
    public void setConfiguration(Map<String, String> config) {
	LOGGER.info(this.getComponent().getElement().getElementId() + " | set configuration");
    }

    @Override
    public void processLogicData(Map<String, String> monData) {
	LOGGER.info(this.getComponent().getElement().getElementId() + " | persist monitoring data");
	// try {
	// Thread.sleep(50);
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	// LOGGER.info("Data persisted");
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
