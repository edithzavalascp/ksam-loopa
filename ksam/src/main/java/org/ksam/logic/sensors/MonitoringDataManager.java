package org.ksam.logic.sensors;

import java.util.HashMap;
import java.util.Map;

public class MonitoringDataManager {
    private final Map<String, Sensor> sensors;

    public MonitoringDataManager() {
	this.sensors = new HashMap<String, Sensor>();
    }

    public Map<String, Sensor> getSensors() {
	return sensors;
    }

    public void addSensor(String meId, Sensor sensor) {
	this.sensors.put(meId, sensor);
    }

}
