package org.ksam.logic.monitor.components;

import java.util.Map;

import org.ksam.model.configuration.MeConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Normalizer implements IMonitorOperation {
    protected final Logger LOGGER = LoggerFactory.getLogger(getClass().getName());
    private final MeConfig config;

    private boolean isAnalysisRequired;

    public Normalizer(MeConfig config) {
	super();
	this.config = config;
	this.isAnalysisRequired = false;
    }

    @Override
    public Map<String, String> doMonitorOperation(Map<String, String> monData) {
	LOGGER.info("Normalize monitoring data" + monData.get("monData").toString());
	// TODO use config monitoring vars for normalizing them
	return monData;
    }

    @Override
    public boolean isAnalysisRequired() {
	/**
	 * bool isUltrasonicFrontCenterOk = true; bool isUltrasonicFrontRightOk = true;
	 * 
	 * if (distanceToObstacleOld < 0) { -- ask vehicles around for distance,
	 * multiply it by -1 and set it to distanceToObstacleOld std::cout << "Ask for
	 * frontal distance info" << std::endl; isUltrasonicFrontCenterOk = false; }
	 * else { isUltrasonicFrontRightOk = true; }
	 * 
	 * if ((false == isUltrasonicFrontCenterOk) && (false ==
	 * isUltrasonicFrontRightOk)) { std::cout << "Change to manual mode" <<
	 * std::endl; m_vehicleControl.setSpeed(0);
	 * m_vehicleControl.setSteeringWheelAngle(0); Container c2(m_vehicleControl);
	 * getConference().send(c2); break; }
	 * 
	 * if (distanceToObstacle > 100000000) { isUltrasonicFrontRightOk = false; }
	 * else { isUltrasonicFrontRightOk = true; }
	 */
	return this.isAnalysisRequired;
    }

}
