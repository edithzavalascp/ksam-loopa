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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.ksam.model.adaptation.AlertType;
import org.ksam.model.configuration.MeConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import io.micrometer.core.instrument.Metrics;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class MLJRip implements IAnalysisMethod {
    protected final Logger LOGGER = LoggerFactory.getLogger(getClass().getName());
    private final String homePath = "/tmp/weka/";
    private List<Entry<String, String>> lfAlgorithmParams;
    private List<Entry<String, String>> lfEvalParams;
    private MeConfig config;
    private Map<String, List<String>> varsMons;
    private Map<String, List<String>> monsVars;
    private PositionAnalysisManager posMan;
    private boolean dmDoneOnFault;
    private boolean dmDoneOnLowBattery;

    private AtomicInteger adaptationNeeded;
    private IContextAnalyzer ctxAnalyzer;

    private final String URL_WEKA;
    private final boolean simulation;
    private final int WINDOW;
    private final String lfAlgorithm;
    private final String positionAlgorithm;

    public MLJRip(MeConfig config) {
	super();
	this.config = config;
	this.lfAlgorithm = this.config.getKsamConfig().getAnalyzerConfig().getAnalysisTechniques().get(0)
		.getAlgorithms().get(0).getAlgorithmId().toString();
	this.positionAlgorithm = this.config.getKsamConfig().getAnalyzerConfig().getAnalysisTechniques().get(0)
		.getAlgorithms().get(1).getAlgorithmId().toString();
	this.simulation = this.config.getKsamConfig().isSimulation();
	this.URL_WEKA = "http://localhost:" + this.config.getKsamConfig().getAnalyzerConfig().getToolPort() + "/";
	this.ctxAnalyzer = new OpenDlvContextAnalyzer(this.config.getSystemUnderMonitoringConfig());
	// TODO select correspoding parameters by algorithm and technique id
	this.lfAlgorithmParams = this.config.getKsamConfig().getAnalyzerConfig().getAnalysisTechniques().get(0)
		.getAlgorithms().get(0).getAlgorithmParameters();
	this.lfEvalParams = this.config.getKsamConfig().getAnalyzerConfig().getAnalysisTechniques().get(0)
		.getAlgorithms().get(0).getEvaluationParameters();
	this.WINDOW = Integer.valueOf(this.lfEvalParams.get(0).getValue());
	this.posMan = new PositionAnalysisManager(this.config.getKsamConfig().isLearning());
	this.varsMons = new HashMap<>();
	this.monsVars = new HashMap<>();
	this.config.getSystemUnderMonitoringConfig().getSystemConfiguration().getMonitorConfig().getMonitors()
		.forEach(m -> {
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
	this.dmDoneOnFault = false;
	this.dmDoneOnLowBattery = false;

	this.adaptationNeeded = Metrics.gauge("ksam.weka.lanefollower.prediction", new AtomicInteger());
	this.adaptationNeeded.set(-1);
    }

    @Override
    public Map<String, List<String>> doAnalysis(String monitorId, AlertType alertType) {
	// LOGGER.info("Do analysis using " + SupportedAnlysisTechnique.ML.getLongName()
	// + "_"
	// + SupportedAlgorithm.IMWPP.getLongName());
	Map<String, List<String>> alternativeMons = new HashMap<>();

	switch (alertType) {
	case MONITORFAULT:
	    if (!dmDoneOnFault) {
		try {
		    // copy header
		    Files.copy(
			    Paths.get(homePath + this.config.getSystemUnderMonitoringConfig().getSystemId()
				    + "_header.arff"),
			    Paths.get(homePath + this.config.getSystemUnderMonitoringConfig().getSystemId()
				    + "_jrip_predict_temp.arff"),
			    java.nio.file.StandardCopyOption.REPLACE_EXISTING);
		    // Copy last available runtime data
		    List<String> lines = Files
			    .lines(Paths.get(
				    homePath + this.config.getSystemUnderMonitoringConfig().getSystemId() + ".arff"))
			    .collect(Collectors.toList());
		    String toPred = "";
		    for (int w = 0; w < WINDOW; w++) {
			toPred += lines.get(lines.size() - 1) + "\n";
		    }
		    // Copy last available runtime data into file
		    try {
			File file = new File(homePath + this.config.getSystemUnderMonitoringConfig().getSystemId()
				+ "_jrip_predict_temp.arff");
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
		LOGGER.info("Analysis | send data to predict position");
		ResponseEntity<String> response = restTemplate
			.getForEntity(URL_WEKA + "/position/" + this.positionAlgorithm + "/" + WINDOW, String.class);
		LOGGER.info("Analysis | receive position prediction");
		// LOGGER.info(response.getBody());
		// Load runtime data without predicted positions
		DataSource sourcePredictJ = null;
		Instances datasetPredictJ = null;
		try {
		    sourcePredictJ = new DataSource(homePath
			    + this.config.getSystemUnderMonitoringConfig().getSystemId() + "_jrip_predict_temp.arff");
		    datasetPredictJ = sourcePredictJ.getDataSet();
		    datasetPredictJ.setClassIndex(datasetPredictJ.numAttributes() - 1);
		} catch (Exception e) {
		    e.printStackTrace();
		}
		// Substitute positions for predicted using weka library -- monitors names may
		// change from one ME to another and from one environment to other.
		// TODO Read them from config file.
		String[] positions = response.getBody().split(" ");
		for (int i = 0; i < positions.length; i++) {
		    String latlon = this.posMan.getLatLon(positions[i]);
		    if (simulation) {
			datasetPredictJ.instance(i).setValue(datasetPredictJ.attribute("imuodsimcvehicle-latitude"),
				Double.valueOf(latlon.split(",")[0]));
			datasetPredictJ.instance(i).setValue(datasetPredictJ.attribute("imuodsimcvehicle-longitude"),
				Double.valueOf(latlon.split(",")[1]));
		    } else {
			datasetPredictJ.instance(i).setValue(datasetPredictJ.attribute("applanixGps-latitude"),
				Double.valueOf(latlon.split(",")[0]));
			datasetPredictJ.instance(i).setValue(datasetPredictJ.attribute("applanixGps-longitude"),
				Double.valueOf(latlon.split(",")[1]));
		    }
		    datasetPredictJ.instance(i).setClassMissing();
		}

		// Re-write file with predicted positions
		try {
		    Files.copy(
			    Paths.get(homePath + this.config.getSystemUnderMonitoringConfig().getSystemId()
				    + "_header.arff"),
			    Paths.get(homePath + this.config.getSystemUnderMonitoringConfig().getSystemId()
				    + "_jrip_predict.arff"),
			    java.nio.file.StandardCopyOption.REPLACE_EXISTING);
		    String toWrite = "";
		    for (int j = 0; j < datasetPredictJ.numInstances(); j++) {
			toWrite += datasetPredictJ.instance(j) + "\n";
		    }
		    try {
			File file = new File(homePath + this.config.getSystemUnderMonitoringConfig().getSystemId()
				+ "_jrip_predict.arff");
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
		LOGGER.info("Analysis | send data to predict self-driving functionality usage");
		ResponseEntity<String> responseLaneFollower = restTemplate
			.getForEntity(URL_WEKA + "/" + this.config.getSystemUnderMonitoringConfig().getSystemId() + "/"
				+ this.lfAlgorithm + "/" + WINDOW, String.class);
		LOGGER.info("Analysis | receive self-driving functionality usage prediction");
		LOGGER.info("Analysis | Prediction " + responseLaneFollower.getBody().toString());
		// LOGGER.info(responseLaneFollower.getBody());
		// Check if self-driving is needed
		String[] lfUsage = responseLaneFollower.getBody().split(" ");
		boolean lfNeeded = true;
		for (String pUsage : lfUsage) {
		    if (pUsage.equals("1")) {
			lfNeeded &= true;
			this.adaptationNeeded.set(1);
		    } else {
			lfNeeded = false;
			this.adaptationNeeded.set(0);
		    }
		}

		List<String> listAlternative = new ArrayList<>();
		if (lfNeeded) {
		    for (String monVar : this.monsVars.get(monitorId)) {
			this.varsMons.get(monVar).forEach(m -> listAlternative.add(m));
			listAlternative.remove(monitorId);
			alternativeMons.put(monVar, listAlternative);
		    }
		    // Copy last available runtimedata
		    try {
			List<String> linesPost = Files.lines(Paths.get(
				homePath + this.config.getSystemUnderMonitoringConfig().getSystemId() + "_real.arff"))
				.collect(Collectors.toList());
			LOGGER.info(
				"Current data: " + linesPost.get(linesPost.size() - 1) + ", last predicted position: "
					+ this.posMan.getLatLon(positions[positions.length - 1]));
		    } catch (IOException e) {
			e.printStackTrace();
		    }
		    this.dmDoneOnFault = true;
		}

	    }
	    break;
	case LOWBATTERYLEVEL:
	    if (!dmDoneOnLowBattery) {
		try {
		    // copy header
		    Files.copy(
			    Paths.get(homePath + this.config.getSystemUnderMonitoringConfig().getSystemId()
				    + "_header.arff"),
			    Paths.get(homePath + this.config.getSystemUnderMonitoringConfig().getSystemId()
				    + "_jrip_predict_temp.arff"),
			    java.nio.file.StandardCopyOption.REPLACE_EXISTING);
		    // Copy last available runtime data
		    List<String> lines = Files.lines(Paths
			    .get(homePath + this.config.getSystemUnderMonitoringConfig().getSystemId() + "_real.arff"))
			    .collect(Collectors.toList());
		    String toPred = "";
		    for (int w = 0; w < WINDOW; w++) {
			toPred += lines.get(lines.size() - 1) + "\n";
		    }
		    // Copy last available runtime data into file
		    try {
			File file = new File(homePath + this.config.getSystemUnderMonitoringConfig().getSystemId()
				+ "_jrip_predict_temp.arff");
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
		ResponseEntity<String> response = restTemplate
			.getForEntity(URL_WEKA + "/position/" + this.positionAlgorithm + "/" + WINDOW, String.class);
		// LOGGER.info(response.getBody());
		// Load runtime data without predicted positions
		DataSource sourcePredictJ = null;
		Instances datasetPredictJ = null;
		try {
		    sourcePredictJ = new DataSource(homePath
			    + this.config.getSystemUnderMonitoringConfig().getSystemId() + "_jrip_predict_temp.arff");
		    datasetPredictJ = sourcePredictJ.getDataSet();
		    datasetPredictJ.setClassIndex(datasetPredictJ.numAttributes() - 1);
		} catch (Exception e) {
		    e.printStackTrace();
		}
		// Substitute positions for predicted using weka library -- monitors names may
		// change from one ME to another and from one environment to other.
		// TODO Read them from config file.
		String[] positions = response.getBody().split(" ");
		for (int i = 0; i < positions.length; i++) {

		    String latlon = this.posMan.getLatLon(positions[i]);
		    if (simulation) {
			datasetPredictJ.instance(i).setValue(datasetPredictJ.attribute("imuodsimcvehicle-latitude"),
				Double.valueOf(latlon.split(",")[0]));
			datasetPredictJ.instance(i).setValue(datasetPredictJ.attribute("imuodsimcvehicle-longitude"),
				Double.valueOf(latlon.split(",")[1]));
		    } else {
			datasetPredictJ.instance(i).setValue(datasetPredictJ.attribute("applanixGps-latitude"),
				Double.valueOf(latlon.split(",")[0]));
			datasetPredictJ.instance(i).setValue(datasetPredictJ.attribute("applanixGps-longitude"),
				Double.valueOf(latlon.split(",")[1]));
		    }
		    datasetPredictJ.instance(i).setClassMissing();
		}

		// Re-write file with predicted positions
		try {
		    Files.copy(
			    Paths.get(homePath + this.config.getSystemUnderMonitoringConfig().getSystemId()
				    + "_header.arff"),
			    Paths.get(homePath + this.config.getSystemUnderMonitoringConfig().getSystemId()
				    + "_jrip_predict.arff"),
			    java.nio.file.StandardCopyOption.REPLACE_EXISTING);
		    String toWrite = "";
		    for (int j = 0; j < datasetPredictJ.numInstances(); j++) {
			toWrite += datasetPredictJ.instance(j) + "\n";
		    }
		    try {
			File file = new File(homePath + this.config.getSystemUnderMonitoringConfig().getSystemId()
				+ "_jrip_predict.arff");
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
			.getForEntity(URL_WEKA + "/" + this.config.getSystemUnderMonitoringConfig().getSystemId() + "/"
				+ this.lfAlgorithm + "/" + WINDOW, String.class);
		// LOGGER.info(responseLaneFollower.getBody());
		// Check if self-driving is needed
		String[] lfUsage = responseLaneFollower.getBody().split(" ");
		boolean lfNeeded = true;
		for (String pUsage : lfUsage) {
		    if (pUsage.equals("1")) {
			lfNeeded &= true;
			this.adaptationNeeded.set(1);
		    } else {
			lfNeeded = false;
			this.adaptationNeeded.set(0);
		    }
		}

		if (lfNeeded) {
		    List<String> reqVars = this.ctxAnalyzer.getRequiredVars("laneFollower");
		    LOGGER.info("Required variables: " + reqVars);
		    this.varsMons.forEach((k, v) -> {
			if (reqVars.contains(k)) {
			    alternativeMons.put(k, v);
			}
		    });

		    // Copy last available runtimedata
		    try {
			List<String> linesPost = Files.lines(Paths.get(
				homePath + this.config.getSystemUnderMonitoringConfig().getSystemId() + "_real.arff"))
				.collect(Collectors.toList());
			LOGGER.info(
				"Current data: " + linesPost.get(linesPost.size() - 1) + ", last predicted position: "
					+ this.posMan.getLatLon(positions[positions.length - 1]));
		    } catch (IOException e) {
			e.printStackTrace();
		    }
		    this.dmDoneOnLowBattery = true;
		}
	    }
	    break;
	case MONITORECOVERED:
	    for (String monVar : this.monsVars.get(monitorId)) {
		List<String> listAlternative = new ArrayList<>();
		this.varsMons.get(monVar).forEach(m -> listAlternative.add(m));
		alternativeMons.put(monVar, listAlternative);
		// LOGGER.info(alternativeMons.toString());
	    }
	    break;
	case ROADEVENT:
	    alternativeMons.put("trafficFactor", null);
	    break;
	default:
	    break;
	}

	return alternativeMons;
    }

    @Override
    public void updateContext(List<Entry<String, Object>> context) {
	this.ctxAnalyzer.updateContext(context);
    }

}
