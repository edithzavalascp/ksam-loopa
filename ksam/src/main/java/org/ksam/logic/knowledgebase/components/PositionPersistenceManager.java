package org.ksam.logic.knowledgebase.components;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PositionPersistenceManager {
    protected final Logger LOGGER = LoggerFactory.getLogger(getClass().getName());
    private final int WINDOW = 5;
    private Map<String, Integer> points;
    private int pointNumber;
    private List<Integer> lastXPoints;
    private final String URL_WEKA = "http://localhost:8085/";

    public PositionPersistenceManager() {
	this.points = new HashMap<>(); // use only for training
	try {
	    Files.copy(Paths.get("/tmp/weka/points_header.arff"), Paths.get("/tmp/weka/points_real.arff"),
		    java.nio.file.StandardCopyOption.REPLACE_EXISTING); // use only for training
	    Files.copy(Paths.get("/tmp/weka/position_header.arff"), Paths.get("/tmp/weka/position_real.arff"),
		    java.nio.file.StandardCopyOption.REPLACE_EXISTING); // use only for training

	    // Files.copy(Paths.get("/tmp/weka/points_header.arff"),
	    // Paths.get("/tmp/weka/points_runtime.arff"),
	    // java.nio.file.StandardCopyOption.REPLACE_EXISTING);
	} catch (IOException e) {
	    e.printStackTrace();
	}
	this.pointNumber = 0;
	this.lastXPoints = new ArrayList<>();
    }

    public Map<String, Integer> getPoints() {
	return points;
    }

    public void setPoints(Map<String, Integer> points) {
	this.points = points;
    }

    public void setPoint(Map<String, String> latitudeLongitude) {
	int prevPoint = this.pointNumber;
	/** Simulation **/
	// String lat = latitudeLongitude.get("imuodsimcvehicle-latitude");
	// String lon = latitudeLongitude.get("imuodsimcvehicle-longitude");

	/** Real car **/
	String lat = latitudeLongitude.get("applanixGps-latitude");
	String lon = latitudeLongitude.get("applanixGps-longitude");

	if (!lat.equals("?") && !lon.equals("?")) {
	    /************************************
	     * Learning - first phase
	     ************************************/
	    Integer value = this.points.put(lat + "," + lon, this.pointNumber);
	    // Compute last points
	    if (this.lastXPoints.isEmpty()) {
		for (int i = 0; i < WINDOW; i++) {
		    // this.lastXPoints.add(((this.pointNumber - 1.0) / (1000.0 - 1.0)));
		    this.lastXPoints.add(this.pointNumber);
		}
	    } else {
		this.lastXPoints.remove(0);
		// this.lastXPoints.add(((prevPoint - 1.0) / (1000.0 - 1.0)));
		this.lastXPoints.add(prevPoint);
	    }
	    //////////////////////////////////
	    // Persist real last points + current
	    try {
		File file = new File("/tmp/weka/position_real.arff");
		if (!file.exists()) {
		    file.createNewFile();
		}

		FileWriter fileWritter = new FileWriter(file, true);
		BufferedWriter output = new BufferedWriter(fileWritter);
		output.write(this.lastXPoints.toString().substring(1, this.lastXPoints.toString().length() - 1)
			.replace(" ", "") + "," + this.pointNumber + "\n"); // ((this.pointNumber -1.0) / (1000.0 -1.0))
		output.close();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	    ////////////////////////////////
	    if (value == null) {
		// Persist real lat,lon,point relation data
		try {
		    File fileRelation = new File("/tmp/weka/points_real.arff");
		    if (!fileRelation.exists()) {
			fileRelation.createNewFile();
		    }
		    FileWriter fileWritterR = new FileWriter(fileRelation, true);
		    BufferedWriter outputR = new BufferedWriter(fileWritterR);
		    outputR.write(lat + "," + lon + "," + this.pointNumber + "\n");
		    outputR.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		this.pointNumber++;
		//////////////////////////////////
	    }
	    /**************************************************************************************************************/

	    /**************************
	     * Normal runtime operation
	     ***************************/
	    // // Write current latitude and longitude
	    // try {
	    // File fileRelation = new File("/tmp/weka/points_runtime.arff");
	    // if (!fileRelation.exists()) {
	    // fileRelation.createNewFile();
	    // }
	    // FileWriter fileWritterR = new FileWriter(fileRelation, true);
	    // BufferedWriter outputR = new BufferedWriter(fileWritterR);
	    // outputR.write(lat + "," + lon + ",?\n");
	    // outputR.close();
	    // } catch (IOException e) {
	    // e.printStackTrace();
	    // }
	    //
	    // RestTemplate restTemplate = new RestTemplate();
	    // ResponseEntity<String> response = restTemplate.getForEntity(URL_WEKA +
	    // "points/Ibk/1", String.class);
	    // try {
	    // File fileRelation = new File("/tmp/weka/pointsRun.txt");
	    // if (!fileRelation.exists()) {
	    // fileRelation.createNewFile();
	    // }
	    // FileWriter fileWritterR = new FileWriter(fileRelation, true);
	    // BufferedWriter outputR = new BufferedWriter(fileWritterR);
	    // outputR.write(response.getBody() + " " + lat + "," + lon + "\n");
	    // outputR.close();
	    // } catch (IOException e) {
	    // e.printStackTrace();
	    // }
	    //
	    // // Compute last points of this run
	    // if (this.lastXPoints.isEmpty()) {
	    // for (int i = 0; i < WINDOW; i++) {
	    // // this.lastXPoints.add(((this.pointNumber - 1.0) / (1000.0 - 1.0)));
	    // this.lastXPoints.add(Integer.valueOf(response.getBody()));
	    // }
	    // } else {
	    // this.lastXPoints.remove(0);
	    // // this.lastXPoints.add(((prevPoint - 1.0) / (1000.0 - 1.0)));
	    // this.lastXPoints.add(prevPoint);
	    // }
	    // this.pointNumber = Integer.valueOf(response.getBody());
	    //
	    // // -------- Persist last points + current -------------
	    // try {
	    // File file = new File("/tmp/weka/positionRun.txt");
	    // if (!file.exists()) {
	    // file.createNewFile();
	    // }
	    //
	    // FileWriter fileWritter = new FileWriter(file, true);
	    // BufferedWriter output = new BufferedWriter(fileWritter);
	    // output.write(this.lastXPoints.toString().substring(1,
	    // this.lastXPoints.toString().length() - 1)
	    // .replace(" ", "") + "," + this.pointNumber + "\n"); // ((this.pointNumber -
	    // 1.0) / (1000.0 - //
	    // // 1.0))
	    // output.close();
	    // } catch (IOException e) {
	    // e.printStackTrace();
	    // }
	    /*****************************************************************************************************************/
	}
    }

}
