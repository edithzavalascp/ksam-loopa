package org.ksam.client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.ksam.model.adaptation.MonitorAdaptation;
import org.ksam.model.configuration.MeConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OpenDlvClient implements IEffectorEnactor {
    private static final Logger LOGGER = LoggerFactory.getLogger(OpenDlvClient.class);
    private MeConfig config;
    private final String vehicleId;

    private String meHostName;
    private int mePort;
    private String replayerUrl;
    private String cityUrl;
    private boolean replay;
    private boolean simulation;

    public OpenDlvClient(String vehicleId) {
	super();
	this.vehicleId = vehicleId;
    }

    @Override
    public void setConfig(MeConfig config) {
	this.config = config;
	this.meHostName = this.config.getKsamConfig().getExecuterConfig().getMeHost();
	this.mePort = this.config.getKsamConfig().getExecuterConfig().getMePort();
	this.replayerUrl = "http://" + this.config.getKsamConfig().getReplayerHost() + ":"
		+ this.config.getKsamConfig().getReplayerPort() + "/";
	this.cityUrl = "http://" + this.config.getKsamConfig().getExecuterConfig().getCityReporterHost() + ":"
		+ this.config.getKsamConfig().getExecuterConfig().getCityReporterPort() + "/";
	this.simulation = this.config.getKsamConfig().isSimulation();

    }

    @Override
    public void enact(MonitorAdaptation a) {

	if (!a.getParamsToAdapt().keySet().isEmpty()) {
	    // SEND ADAPTATION TO CITYREPORTER SERVICE
	    ObjectMapper mapper = new ObjectMapper();
	    String jsonAdaptation;
	    try {
		jsonAdaptation = mapper.writeValueAsString(a);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set("Content-Type", "application/json");
		HttpEntity<String> httpEntity = new HttpEntity<String>(jsonAdaptation, httpHeaders);
		RestTemplate restTemplate = new RestTemplate();
		LOGGER.info("OpenDlv enactor | send adaptation to cityreporter service - vehicleId" + vehicleId
			+ " adaptation: change" + a.getParamsToAdapt().keySet().toString() + " parameter(s) to "
			+ a.getParamsToAdapt().values().toString() + " respectively");
		restTemplate.postForObject(cityUrl + "openDlvMonitorv" + vehicleId + "/adapt", httpEntity,
			String.class);
	    } catch (JsonProcessingException e) {
		e.printStackTrace();
	    }
	} else {
	    if (a.getMonitorsToAdd().contains("heretraffic") || a.getMonitorsToRemove().contains("heretraffic")
		    || a.getMonitorsToAdd().contains("openweathermap")
		    || a.getMonitorsToRemove().contains("openweathermap")) {
		// SEND ADAPTATION TO CITYREPORTER SERVICE
		ObjectMapper mapper = new ObjectMapper();
		String jsonAdaptation;
		try {
		    jsonAdaptation = mapper.writeValueAsString(a);
		    HttpHeaders httpHeaders = new HttpHeaders();
		    httpHeaders.set("Content-Type", "application/json");
		    HttpEntity<String> httpEntity = new HttpEntity<String>(jsonAdaptation, httpHeaders);
		    RestTemplate restTemplate = new RestTemplate();
		    LOGGER.info("OpenDlv enactor | send adaptation to cityreporter service - vehicleId" + vehicleId
			    + " adaptation: add/remove service");
		    restTemplate.postForObject(cityUrl + "openDlvMonitorv" + vehicleId + "/adapt", httpEntity,
			    String.class);
		} catch (JsonProcessingException e) {
		    e.printStackTrace();
		}
		if (a.getMonitorsToAdd().contains("heretraffic")) {
		    a.getMonitorsToAdd().remove("heretraffic");
		}

		if (a.getMonitorsToRemove().contains("heretraffic")) {
		    a.getMonitorsToRemove().remove("heretraffic");
		}
		if (a.getMonitorsToAdd().contains("openweathermap")) {
		    a.getMonitorsToAdd().remove("openweathermap");
		}

		if (a.getMonitorsToRemove().contains("openweathermap")) {
		    a.getMonitorsToRemove().remove("openweathermap");
		}
	    }

	    String dataString = "VehicleId:" + this.vehicleId
		    + (!a.getMonitorsToAdd().isEmpty()
			    ? ";MonitorsToAdd:" + a.getMonitorsToAdd().toString()
				    .substring(1, a.getMonitorsToAdd().toString().length() - 1).replace(", ", ",")
			    : ";MonitorsToAdd:")
		    + (!a.getMonitorsToRemove().isEmpty()
			    ? ";MonitorsToRemove:" + a.getMonitorsToRemove().toString()
				    .substring(1, a.getMonitorsToRemove().toString().length() - 1).replace(", ", ",")
			    : ";MonitorsToRemove:" + "\0");

	    // if (!replay) {
	    try {
		Socket socket = new Socket(meHostName, mePort);
		DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
		byte[] data = dataString.getBytes();
		LOGGER.info("OpenDlv enactor | send adaptation to real vehicle - vehicleId" + vehicleId
			+ " adaptation: " + dataString);
		dos.write(data);
		dos.close();
		socket.close();
	    } catch (IOException e) {
		LOGGER.error(e.toString());
	    }
	    // } else {
	    // LOGGER.info("OpenDlv enactor | send adaptation to be applied by vehicle" +
	    // vehicleId + " adaptation: "
	    // + dataString);
	    if (replay) {
		ObjectMapper mapper = new ObjectMapper();
		String jsonAdaptation;
		try {
		    jsonAdaptation = mapper.writeValueAsString(a);
		    HttpHeaders httpHeaders = new HttpHeaders();
		    httpHeaders.set("Content-Type", "application/json");
		    HttpEntity<String> httpEntity = new HttpEntity<String>(jsonAdaptation, httpHeaders);
		    RestTemplate restTemplate = new RestTemplate();
		    LOGGER.info("OpenDlv enactor | send adaptation to replayer - vehicleId" + vehicleId
			    + " adaptation: " + dataString);
		    restTemplate.postForObject(replayerUrl + "openDlvMonitorv" + vehicleId + "/adapt", httpEntity,
			    String.class);
		} catch (JsonProcessingException e) {
		    e.printStackTrace();
		}
	    }
	    // }
	    // LOGGER.info("OpenDlv enactor | send adaptation to be applied by vehicle" +
	    // vehicleId + " adaptation: "
	    // + dataString);
	}

    }
}
