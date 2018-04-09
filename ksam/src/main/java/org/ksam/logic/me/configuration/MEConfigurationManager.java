package org.ksam.logic.me.configuration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.ksam.logic.autonomicmanager.SimpleLoopManager;
import org.ksam.logic.effectors.Effector;
import org.ksam.logic.effectors.SupportedEnactors;
import org.ksam.model.configuration.MeConfig;
import org.loopa.comm.message.AMMessageBodyType;
import org.loopa.recipient.Recipient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MEConfigurationManager {
    protected final Logger LOGGER = LoggerFactory.getLogger(getClass().getName());

    private final SimpleLoopManager loop;

    private final Map<String, MeConfig> configs;

    public MEConfigurationManager(SimpleLoopManager loop) {
	this.loop = loop;
	this.configs = new HashMap<>();
    }

    public void setLoopMEConfig(MeConfig config) {
	LOGGER.info("Set ME config and add it to ksam loop");
	this.configs.put(config.getSystemUnderMonitoringConfig().getSystemId(), config);

	loop.getAMLoop().addME(config.getSystemUnderMonitoringConfig().getSystemId(), new Recipient(
		"effector_" + config.getSystemUnderMonitoringConfig().getSystemId(),
		Arrays.asList(AMMessageBodyType.ADAPTATION.toString()
			+ config.getSystemUnderMonitoringConfig().getSystemId()),
		new Effector(SupportedEnactors.enactors.get(config.getSystemUnderMonitoringConfig().getSystemId()))));

    }

    public Map<String, MeConfig> getConfigs() {
	return configs;
    }

    public SimpleLoopManager getLoop() {
	return loop;
    }

}
