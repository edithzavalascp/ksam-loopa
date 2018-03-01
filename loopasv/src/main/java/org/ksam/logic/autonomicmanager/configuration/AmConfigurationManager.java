package org.ksam.logic.autonomicmanager.configuration;

import java.io.File;
import java.io.IOException;

import org.ksam.model.configuration.operation.AMConfiguration;
import org.loopa.policy.IPolicy;
import org.loopa.policy.Policy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AmConfigurationManager {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass().getName());
    private final String aMConfigFile = "loopConfig.json";

    private AMConfiguration config;

    public AmConfigurationManager() {
	super();
	ObjectMapper mapper = new ObjectMapper();
	try {
	    this.config = mapper.readValue(new File(getClass().getClassLoader().getResource(aMConfigFile).getFile()),
		    AMConfiguration.class);
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public IPolicy getElementsMessagesCodesPolicy(String elementId) {
	return new Policy(elementId, config.getElementsMessagesCodes());
    }

    public IPolicy getElementsMessageBodyTypesPolicy() {
	return new Policy(config.getAutonomicManagerId(), config.getElementsMessageBodyTypes());
    }

    public String getElementId(String name) {
	return config.getElementsId().get(name);
    }

    public String getId() {
	return config.getAutonomicManagerId();
    }

}
