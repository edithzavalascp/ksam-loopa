package org.ksam.client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.ksam.model.adaptation.MonitorAdaptation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OpenDlvClient implements IEffectorEnactor {
    private static final Logger LOGGER = LoggerFactory.getLogger(OpenDlvClient.class);
    private static final String HOST_NAME = "localhost";
    private static final int PORT = 8082;
    private final boolean replay = true;
    private final String replayerUrl = "http://localhost:8088/";

    private final String vehicleId;

    public OpenDlvClient(String vehicleId) {
	super();
	this.vehicleId = vehicleId;
    }

    @Override
    public void enact(MonitorAdaptation a) {
	String dataString = "VehicleId:" + this.vehicleId
		+ (!a.getMonitorsToAdd().isEmpty()
			? ";MonitorsToAdd:" + a.getMonitorsToAdd().toString()
				.substring(1, a.getMonitorsToAdd().toString().length() - 1).replace(", ", ",")
			: ";MonitorsToAdd:")
		+ (!a.getMonitorsToRemove().isEmpty()
			? ";MonitorsToRemove:" + a.getMonitorsToRemove().toString()
				.substring(1, a.getMonitorsToRemove().toString().length() - 1).replace(", ", ",")
			: ";MonitorsToRemove:" + "\0");

	if (!replay) {
	    try {
		Socket socket = new Socket(HOST_NAME, PORT);
		DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
		byte[] data = dataString.getBytes();
		dos.write(data);
		dos.close();
		socket.close();
	    } catch (IOException e) {
		LOGGER.error(e.toString());
	    }
	} else {
	    // LOGGER.info("OpenDlv enactor | send adaptation to be applied by vehicle" +
	    // vehicleId + " adaptation: "
	    // + dataString);
	    ObjectMapper mapper = new ObjectMapper();
	    String jsonAdaptation;
	    try {
		jsonAdaptation = mapper.writeValueAsString(a);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set("Content-Type", "application/json");
		HttpEntity<String> httpEntity = new HttpEntity<String>(jsonAdaptation, httpHeaders);
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.postForObject(replayerUrl + "openDlvMonitorv" + vehicleId + "/adapt", httpEntity,
			String.class);
	    } catch (JsonProcessingException e) {
		e.printStackTrace();
	    }
	}
	LOGGER.info("OpenDlv enactor | send adaptation to be applied by vehicle" + vehicleId + " adaptation: "
		+ dataString);

    }
}
