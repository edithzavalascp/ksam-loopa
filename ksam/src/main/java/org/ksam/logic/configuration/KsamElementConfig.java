package org.ksam.logic.configuration;

import java.util.Map;

import org.ksam.model.configuration.MeConfig;

public interface KsamElementConfig {
    Map<String, String> getElementConfig(MeConfig meConfig);
}
