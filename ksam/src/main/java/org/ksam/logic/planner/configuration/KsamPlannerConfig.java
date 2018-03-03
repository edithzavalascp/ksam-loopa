package org.ksam.logic.planner.configuration;

import java.util.Map;

import org.ksam.logic.configuration.KsamElementConfig;
import org.ksam.model.configuration.MeConfig;

public class KsamPlannerConfig implements KsamElementConfig {

    @Override
    public Map<String, String> getElementConfig(MeConfig meConfig) {
	/**
	 * meConfig.getSystemUnderMonitoringConfig().getSystemConfiguration().getMonitorConfig().getMonitors()
	 * .forEach(monitor -> {
	 * this.config.put(monitor.getMonitorAttributes().getMonitorId() + ".type",
	 * monitor.getType().toString());
	 * this.config.put(monitor.getMonitorAttributes().getMonitorId() + ".frequency",
	 * String.valueOf(monitor.getMonitorAttributes().getFrequency()));
	 * this.config.put(monitor.getMonitorAttributes().getMonitorId() + ".cost.type",
	 * String.valueOf(monitor.getMonitorAttributes().getCost().getType().toString()));
	 * this.config.put(monitor.getMonitorAttributes().getMonitorId() + ".cost.type",
	 * String.valueOf(monitor.getMonitorAttributes().getCost().getType().toString()));
	 * });
	 */
	return null;
    }

}
