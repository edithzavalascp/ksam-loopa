package org.ksam.logic.monitor.configuration;

import java.util.HashMap;
import java.util.Map;

import org.ksam.logic.configuration.KsamElementConfig;
import org.ksam.model.configuration.MeConfig;
import org.ksam.model.configuration.supportedvalues.VariableValueType;

public class KsamMonitorConfig implements KsamElementConfig {

    private Map<String, String> config = new HashMap<>();

    @Override
    public Map<String, String> getElementConfig(MeConfig meConfig) {
	this.config.put("frequency", String.valueOf(meConfig.getKsamConfig().getMonitorConfig().getFrequency()));
	this.config.put("monMechanisms", meConfig.getKsamConfig().getMonitorConfig().getMonMechanisms().toString()
		.substring(1, meConfig.getKsamConfig().getMonitorConfig().getMonMechanisms().toString().length() - 1));
	this.config.put("monOperations", meConfig.getKsamConfig().getMonitorConfig().getMonOperations().toString()
		.substring(1, meConfig.getKsamConfig().getMonitorConfig().getMonOperations().toString().length() - 1));
	this.config.put("monitoringVarsIds",
		meConfig.getSystemUnderMonitoringConfig().getSystemVariables().getMonitorVars().getMonitoringVars()
			.toString().substring(1, meConfig.getSystemUnderMonitoringConfig().getSystemVariables()
				.getMonitorVars().getMonitoringVars().toString().length() - 1));

	meConfig.getSystemUnderMonitoringConfig().getSystemConfiguration().getMonitorConfig().getMonitoringVars()
		.forEach(variable -> {
		    this.config.put(variable.getVarId() + ".type", variable.getType().toString());
		    this.config.put(variable.getVarId() + ".valueType",
			    variable.getValueCharacteristics().getValueType().toString());
		    if (!this.config.get(variable.getVarId() + ".valueType")
			    .equals(VariableValueType.BOOLEAN.toString())) {
			this.config.put(variable.getVarId() + ".values",
				variable.getValueCharacteristics().getValues().toString().substring(1,
					variable.getValueCharacteristics().getValues().toString().length() - 1));
		    }
		});

	return this.config;
    }

}
