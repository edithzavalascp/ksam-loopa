package org.ksam.logic.autonomicmanager.configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.ksam.model.configuration.AMConfiguration;
import org.loopa.policy.IPolicy;
import org.loopa.policy.Policy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AmConfigurationManager {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass().getName());

    private AMConfiguration config;

    public AmConfigurationManager(String amConfigFile) {
	super();
	ObjectMapper mapper = new ObjectMapper();
	try {
	    InputStream is = AmConfigurationManager.class.getResourceAsStream(amConfigFile);
	    // LOGGER.info("" + is);
	    StringBuilder textBuilder = new StringBuilder();
	    try (Reader reader = new BufferedReader(
		    new InputStreamReader(is, Charset.forName(StandardCharsets.UTF_8.name())))) {
		int c = 0;
		while ((c = reader.read()) != -1) {
		    textBuilder.append((char) c);
		}
	    }
	    this.config = mapper.readValue(textBuilder.toString(), AMConfiguration.class);
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
