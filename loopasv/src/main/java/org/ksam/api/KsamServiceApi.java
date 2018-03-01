package org.ksam.api;

import java.io.File;

import javax.websocket.server.PathParam;

import org.ksam.logic.me.configuration.MEConfigurationManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KsamServiceApi {
    // private LoopaSvSensors sensors; // connect the api with a sensors instance

    // @RequestMapping("/")
    // public String index() {
    // return "demo";
    // }

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
    }

    @PostMapping("/managedElement")
    public ResponseEntity<String> addManagedElement() {
	// TODO Read Json from payload
	if (MEConfigurationManager.addME(
		new File(getClass().getClassLoader().getResource("KsamApiJson_managedelement.json").getFile()))) {
	    return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

}
