package org.ksam.logic.knowledgebase.components;

import java.util.Map;

import org.loopa.element.functionallogic.enactor.knowledgebase.IKnowledgeBaseFleManager;
import org.loopa.generic.element.component.ILoopAElementComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KnowledgeBaseFleManager implements IKnowledgeBaseFleManager {

    protected final Logger LOGGER = LoggerFactory.getLogger(getClass().getName());

    @Override
    public void setConfiguration(Map<String, String> config) {

    }

    @Override
    public void processLogicData(Map<String, String> monData) {
	LOGGER.info("Pesist data");
    }

    @Override
    public void setComponent(ILoopAElementComponent c) {
	// TODO Auto-generated method stub

    }

    @Override
    public ILoopAElementComponent getComponent() {
	// TODO Auto-generated method stub
	return null;
    }

}
