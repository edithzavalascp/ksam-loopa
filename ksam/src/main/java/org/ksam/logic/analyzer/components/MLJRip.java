package org.ksam.logic.analyzer.components;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.ksam.model.adaptation.AlertType;
import org.ksam.model.configuration.SumConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class MLJRip implements IAnalysisMethod {
    protected final Logger LOGGER = LoggerFactory.getLogger(getClass().getName());
    private final String URL_WEKA = "http://localhost:8085/";
    private List<Entry<String, String>> algorithmParams;
    private List<Entry<String, String>> evalParams;
    private SumConfig config;
    private Map<String, List<String>> varsMons;
    private Map<String, List<String>> monsVars;
    private PositionAnalysisManager posMan;
    private boolean dmDone;

    public MLJRip(SumConfig config, List<Entry<String, String>> algorithmParams,
	    List<Entry<String, String>> evalParams) {
	super();
	this.config = config;
	this.algorithmParams = algorithmParams;
	this.evalParams = evalParams;
	this.posMan = new PositionAnalysisManager();
	this.varsMons = new HashMap<>();
	this.monsVars = new HashMap<>();
	this.config.getSystemConfiguration().getMonitorConfig().getMonitors().forEach(m -> {
	    this.monsVars.put(m.getMonitorAttributes().getMonitorId(), new ArrayList<>());
	    m.getMonitorAttributes().getMonitoringVars().forEach(varMon -> {
		this.monsVars.get(m.getMonitorAttributes().getMonitorId()).add(varMon);
		if (this.varsMons.get(varMon) != null) {
		    this.varsMons.get(varMon).add(m.getMonitorAttributes().getMonitorId());
		} else {
		    List<String> monitors = new ArrayList<>();
		    monitors.add(m.getMonitorAttributes().getMonitorId());
		    this.varsMons.put(varMon, monitors);
		}

	    });
	});
	this.dmDone = false;
    }

    @Override
    public Map<String, List<String>> doAnalysis(String monitorId, AlertType alertType) {
	// LOGGER.info("Do analysis using " + SupportedAnlysisTechnique.ML.getLongName()
	// + "_"
	// + SupportedAlgorithm.IMWPP.getLongName());
	Map<String, List<String>> alternativeMons = new HashMap<>();

	switch (alertType) {
	case MONITORFAULT:
	    if (!dmDone) {
		for (String monVar : this.monsVars.get(monitorId)) {

		    try {
			// copy header
			Files.copy(Paths.get("/tmp/weka/" + this.config.getSystemId() + "_predict.arff"),
				Paths.get("/tmp/weka/" + this.config.getSystemId() + "_predict_1.arff"),
				java.nio.file.StandardCopyOption.REPLACE_EXISTING);
			// Copy last available runtimedata
			List<String> lines = Files.lines(Paths.get("/tmp/weka/" + this.config.getSystemId() + ".arff"))
				.collect(Collectors.toList());
			String toPred = lines.get(lines.size() - 1) + "\n" + lines.get(lines.size() - 1) + "\n"
				+ lines.get(lines.size() - 1) + "\n" + lines.get(lines.size() - 1) + "\n"
				+ lines.get(lines.size() - 1);

			try {
			    File file = new File("/tmp/weka/" + this.config.getSystemId() + "_predict_1.arff");
			    if (!file.exists()) {
				file.createNewFile();
			    }

			    FileWriter fileWritter = new FileWriter(file, true);
			    BufferedWriter output = new BufferedWriter(fileWritter);
			    output.write(toPred);
			    output.close();
			} catch (IOException e) {
			    e.printStackTrace();
			}
		    } catch (IOException e) {
			e.printStackTrace();
		    }
		    // Predict next vehicle position
		    RestTemplate restTemplate = new RestTemplate();
		    ResponseEntity<String> response = restTemplate.getForEntity(URL_WEKA + "/position/Regression/5",
			    String.class);
		    LOGGER.info(response.getBody());
		    // Load runtime data without predicted positions
		    DataSource sourcePredictJ = null;
		    Instances datasetPredictJ = null;
		    try {
			sourcePredictJ = new DataSource("/tmp/weka/" + this.config.getSystemId() + "_predict_1.arff");
			datasetPredictJ = sourcePredictJ.getDataSet();
			datasetPredictJ.setClassIndex(datasetPredictJ.numAttributes() - 1);
		    } catch (Exception e) {
			e.printStackTrace();
		    }
		    // Substitute positions for predicted using weka library -- monitors names may
		    // change from one ME to other and from one environment to other.
		    String[] positions = response.getBody().split(" ");
		    for (int i = 0; i < positions.length; i++) {

			String latlon = this.posMan.getLatLon(positions[i]);
			datasetPredictJ.instance(i).setValue(datasetPredictJ.attribute("imuodsimcvehicle-latitude"),
				Double.valueOf(latlon.split(",")[0]));
			datasetPredictJ.instance(i).setValue(datasetPredictJ.attribute("imuodsimcvehicle-longitude"),
				Double.valueOf(latlon.split(",")[1]));
			datasetPredictJ.instance(i).setClassMissing();
		    }

		    // Re-write file with predicted positions
		    try {
			Files.copy(Paths.get("/tmp/weka/" + this.config.getSystemId() + "_predict.arff"),
				Paths.get("/tmp/weka/" + this.config.getSystemId() + "_predict_1.arff"),
				java.nio.file.StandardCopyOption.REPLACE_EXISTING);
			String toWrite = "";
			for (int j = 0; j < datasetPredictJ.numInstances(); j++) {
			    toWrite += datasetPredictJ.instance(j) + "\n";
			}
			try {
			    File file = new File("/tmp/weka/" + this.config.getSystemId() + "_predict_1.arff");
			    if (!file.exists()) {
				file.createNewFile();
			    }

			    FileWriter fileWritter = new FileWriter(file, true);
			    BufferedWriter output = new BufferedWriter(fileWritter);
			    output.write(toWrite);
			    output.close();
			} catch (IOException e) {
			    e.printStackTrace();
			}
		    } catch (IOException e) {
			e.printStackTrace();
		    }
		    // Predict self-driving functionality usage
		    ResponseEntity<String> responseLaneFollower = restTemplate
			    .getForEntity(URL_WEKA + "/" + this.config.getSystemId() + "/JRip/5", String.class);
		    LOGGER.info(responseLaneFollower.getBody());
		    // Check if self-driving is needed
		    String[] lfUsage = responseLaneFollower.getBody().split(" ");
		    boolean lfNeeded = false;
		    for (String pUsage : lfUsage) {
			if (pUsage.equals("1")) {
			    lfNeeded = true;
			}
		    }

		    List<String> listAlternative = new ArrayList<>();
		    if (lfNeeded) {
			this.varsMons.get(monVar).forEach(m -> listAlternative.add(m));
			listAlternative.remove(monitorId);
			alternativeMons.put(monVar, listAlternative);

			// Copy last available runtimedata
			try {
			    List<String> linesPost = Files
				    .lines(Paths.get("/tmp/weka/" + this.config.getSystemId() + ".arff"))
				    .collect(Collectors.toList());
			    LOGGER.info("Current data: " + linesPost.get(linesPost.size() - 1)
				    + ", last predicted position: " + this.posMan.getLatLon(positions[4]));
			} catch (IOException e) {
			    e.printStackTrace();
			}
			this.dmDone = true;
		    }

		}
	    }
	    break;
	case LOWBATTERYLEVEL:
	    // TODO Check worth variables with models probabilities
	    // if this.monsVars changes alternativeMons could change!
	    this.varsMons.forEach((k, v) -> alternativeMons.put(k, v));
	    break;
	case MONITORECOVERED:
	    for (String monVar : this.monsVars.get(monitorId)) {
		List<String> listAlternative = new ArrayList<>();
		this.varsMons.get(monVar).forEach(m -> listAlternative.add(m));
		alternativeMons.put(monVar, listAlternative);
		// LOGGER.info(alternativeMons.toString());
	    }
	default:
	    break;
	}

	return alternativeMons;
    }

}
