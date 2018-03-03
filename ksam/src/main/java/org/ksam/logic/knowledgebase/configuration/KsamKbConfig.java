package org.ksam.logic.knowledgebase.configuration;

import java.util.HashMap;
import java.util.Map;

import org.ksam.logic.configuration.KsamElementConfig;
import org.ksam.model.configuration.MeConfig;

public class KsamKbConfig implements KsamElementConfig {

    private Map<String, String> config = new HashMap<>();

    @Override
    public Map<String, String> getElementConfig(MeConfig meConfig) {
	return null;
    }

}
