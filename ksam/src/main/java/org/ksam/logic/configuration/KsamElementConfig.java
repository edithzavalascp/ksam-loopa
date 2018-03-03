package org.ksam.logic.configuration;

public class KsamElementConfig {

    // public static Map<String, String> getMonitoringVarsConfig(MeConfig meConfig)
    // {
    // Map<String, String> config = new HashMap<String, String>();
    // config.put("monitoringVarsIds",
    // meConfig.getSystemUnderMonitoringConfig().getSystemVariables().getMonitorVars().getMonitoringVars()
    // .toString().substring(1,
    // meConfig.getSystemUnderMonitoringConfig().getSystemVariables()
    // .getMonitorVars().getMonitoringVars().toString().length() - 1));
    //
    // meConfig.getSystemUnderMonitoringConfig().getSystemConfiguration().getMonitorConfig().getMonitoringVars()
    // .forEach(variable -> {
    // config.put(variable.getVarId() + ".type", variable.getType().toString());
    // config.put(variable.getVarId() + ".valueType",
    // variable.getValueCharacteristics().getValueType().toString());
    // if (variable.getValueCharacteristics().getValueType() !=
    // VariableValueType.BOOLEAN) {
    // config.put(variable.getVarId() + ".values",
    // variable.getValueCharacteristics().getValues().toString().substring(1,
    // variable.getValueCharacteristics().getValues().toString().length() - 1));
    // }
    // });
    // return config;
    // }
    //
    // public static Map<String, String> getMonitorsConfig(MeConfig meConfig) {
    // Map<String, String> config = new HashMap<String, String>();
    // config.put("monitorsIds",
    // meConfig.getSystemUnderMonitoringConfig().getSystemVariables().getMonitorVars().getMonitoringVars()
    // .toString().substring(1,
    // meConfig.getSystemUnderMonitoringConfig().getSystemVariables()
    // .getMonitorVars().getMonitoringVars().toString().length() - 1));
    //
    // meConfig.getSystemUnderMonitoringConfig().getSystemConfiguration().getMonitorConfig().getMonitors()
    // .forEach(monitor -> {
    // config.put(monitor.getMonitorAttributes().getMonitorId() + ".type",
    // monitor.getType().toString());
    // config.put(monitor.getMonitorAttributes().getMonitorId() + ".frequency",
    // String.valueOf(monitor.getMonitorAttributes().getFrequency()));
    // config.put(monitor.getMonitorAttributes().getMonitorId() + ".monitoringVars",
    // monitor.getMonitorAttributes().getMonitoringVars().toString().substring(1,
    // monitor.getMonitorAttributes().toString().length() - 1));
    // config.put(monitor.getMonitorAttributes().getMonitorId() + ".costType",
    // monitor.getMonitorAttributes().getCost().getType().toString());
    // config.put(monitor.getMonitorAttributes().getMonitorId() + ".costValue",
    // String.valueOf(monitor.getMonitorAttributes().getCost().getValue()));
    //
    // });
    // return config;
    // }
    //
    // public void setAnalyzer() {
    // this.config.put("problemId",
    // meConfig.getKsamConfig().getAnalyzerConfig().getProblemId());
    // meConfig.getKsamConfig().getAnalyzerConfig().getAnalysisTechniques().forEach(technique
    // -> {
    // this.config.put("techId", technique.getTechId().toString());
    // technique.getAlgorithms().forEach(algorithm -> {
    //
    // this.config.put(technique.getTechId().toString() + ".algorithmId",
    // algorithm.getAlgorithmId().toString());
    //
    // algorithm.getAlgorithmParameters().forEach(algorithmParameter -> {
    // this.config.put(technique.getTechId().toString() + "." +
    // algorithm.getAlgorithmId().toString() + "."
    // + algorithmParameter.getKey(), algorithmParameter.getValue());
    // });
    //
    // algorithm.getEvaluationParameters().forEach(evalParameter -> {
    // this.config.put(technique.getTechId().toString() + "." +
    // algorithm.getAlgorithmId().toString() + "."
    // + evalParameter.getKey(), evalParameter.getValue());
    // });
    //
    // });
    // });
    // }

}
