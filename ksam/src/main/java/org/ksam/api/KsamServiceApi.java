package org.ksam.api;

import java.util.HashMap;

import javax.websocket.server.PathParam;

import org.ksam.logic.sensors.Sensor;
import org.ksam.model.configuration.MeConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KsamServiceApi {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass().getName());

    @PostMapping("/{param1}/monitoringData")
    public void createMonDataEntry(@PathParam("param1") String systemId) {
	// Call sensors (sensors compose a message and send it to LoopaMonitor)
	// sensors.processMonitoringData(systemId, "jsonReceived");

	// // Example of sending a message with sensor data to the loop
	// ISensor s = new Sensor(loopa.getMonitor());
	// s.processSensorData("me1", new HashMap<String, String>());
	//
	// // Example of sending a message with policy adaptation to the loop
	// loopa.AdaptLoopElement("ksam", loopa.getMonitor().getElementId(),
	// AMElementAdpaptationType.RECEIVER,
	// "var1:value1,var2:value2,var3");
	Sensor.processMonData(systemId, new HashMap<String, String>());
    }

    @PostMapping("/managedElement")
    public ResponseEntity<String> addManagedElement(@RequestBody MeConfig meConfig) {
	KsamInitializer.meConfigM.setLoopMEConfig(meConfig);
	return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
