package org.ksam.logic.me.configuration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.ksam.logic.analyzer.configuration.KsamAnalyzerConfig;
import org.ksam.logic.autonomicmanager.SimpleLoopManager;
import org.ksam.logic.effectors.Effector;
import org.ksam.logic.effectors.SupportedEnactors;
import org.ksam.logic.executer.configuration.KsamExecuterConfig;
import org.ksam.logic.knowledgebase.configuration.KsamKbConfig;
import org.ksam.logic.monitor.configuration.KsamMonitorConfig;
import org.ksam.logic.planner.configuration.KsamPlannerConfig;
import org.ksam.model.configuration.MeConfig;
import org.loopa.comm.message.AMMessageBodyType;
import org.loopa.recipient.Recipient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MEConfigurationManager {
    protected final Logger LOGGER = LoggerFactory.getLogger(getClass().getName());
    private final KsamMonitorConfig mConfig = new KsamMonitorConfig();
    private final KsamAnalyzerConfig aConfig = new KsamAnalyzerConfig();
    private final KsamPlannerConfig pConfig = new KsamPlannerConfig();
    private final KsamExecuterConfig eConfig = new KsamExecuterConfig();
    private final KsamKbConfig kbConfig = new KsamKbConfig();

    private final SimpleLoopManager loop;

    public MEConfigurationManager(SimpleLoopManager loop) {
	this.loop = loop;
    }

    public void setLoopMEConfig(MeConfig config) {
	LOGGER.info("Set ME config and add it to ksam loop");
	// TODO fill missing elementConfig
	Map<String, Map<String, String>> elementsCongfig = new HashMap<String, Map<String, String>>() {
	    {
		put("monitorConfig", mConfig.getElementConfig(config));
		put("analyzerConfig", aConfig.getElementConfig(config));
		put("plannerConfig", pConfig.getElementConfig(config));
		put("executerConfig", eConfig.getElementConfig(config));
		put("kbConfig", kbConfig.getElementConfig(config));
	    }
	};

	loop.getLoop().addME(elementsCongfig, new Recipient(
		"effector_" + config.getSystemUnderMonitoringConfig().getSystemId(),
		Arrays.asList(AMMessageBodyType.ADAPTATION.toString()),
		new Effector(SupportedEnactors.enactors.get(config.getSystemUnderMonitoringConfig().getSystemId()))));

    }
}
