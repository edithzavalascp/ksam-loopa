package org.ksam.logic.planner.configuration;

import org.ksam.logic.configuration.IKsamElementConfig;

public class KsamPlannerConfig implements IKsamElementConfig {
    //
    // private Map<String, String> config = new HashMap<>();
    //
    // @Override
    // public Map<String, String> getElementConfig(MeConfig meConfig) {
    // meConfig.getKsamConfig().getPlannerConfig().getPlanTechniques().forEach(technique
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
    //
    // return this.config;
    // }
    //
    // @Override
    // public void setNewEntry(String key, String value) {
    // this.config.put(key, value);
    //
    // }

}
