package org.loopasv.api;

import org.loopasv.logic.sensors.LoopaSvSensors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoopaSvServiceApi {
  private LoopaSvSensors sensors; // connect the api with a sensors instance

  @RequestMapping("/")
  public String index() {
    return "demo";
  }

  @PostMapping("/monitoringData")
  public void postMonData(@RequestParam(value = "systemId", required = true) String systemId) {
    // Call sensors (sensors compose a message and send it to LoopaMonitor)
    sensors.processMonitoringData(systemId, "jsonReceived");
  }

}
