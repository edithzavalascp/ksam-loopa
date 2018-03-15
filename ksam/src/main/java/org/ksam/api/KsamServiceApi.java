package org.ksam.api;

import org.ksam.logic.sensors.Sensor;
import org.ksam.model.configuration.MeConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KsamServiceApi {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass().getName());

    @PostMapping("/{systemId}/monitoringData")
    public ResponseEntity<String> createMonDataEntry(@PathVariable("systemId") String systemId,
	    @RequestBody String monData) {
	if (Application.mdM.getSensors().get(systemId).processMonData(systemId, monData) != null) {
	    return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PostMapping("/managedElement")
    public ResponseEntity<String> addManagedElement(@RequestBody MeConfig meConfig) {
	Application.meConfigM.setLoopMEConfig(meConfig);
	Application.mdM.addSensor(meConfig.getSystemUnderMonitoringConfig().getSystemId(),
		new Sensor(Application.meConfigM.getLoop().getAMLoop().getMonitor()));
	if (Application.mdM.getSensors().get(meConfig.getSystemUnderMonitoringConfig().getSystemId()) != null) {
	    return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
