package org.ksam.logic.knowledgebase.components;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PositionPersistenceManager {
    private final int WINDOW = 5;
    private Map<String, Integer> points;
    private int pointNumber;
    private List<Integer> lastXPoints;

    public PositionPersistenceManager() {
	this.points = new HashMap<>();
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
	String lat = latitudeLongitude.get("imuodsimcvehicle-latitude");
	String lon = latitudeLongitude.get("imuodsimcvehicle-longitude");
	if (!lat.equals("?") && !lon.equals("?")) {
	    if (this.points.put(lat + "," + lon, this.pointNumber) == null) {
		this.pointNumber++;
	    }
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
	    try {
		File file = new File("/tmp/weka/position.arff");
		File fileRelation = new File("/tmp/weka/pointsToLatLon.txt");
		if (!file.exists()) {
		    file.createNewFile();
		}
		if (!fileRelation.exists()) {
		    fileRelation.createNewFile();
		}

		FileWriter fileWritter = new FileWriter(file, true);
		BufferedWriter output = new BufferedWriter(fileWritter);
		output.write(this.lastXPoints.toString().substring(1, this.lastXPoints.toString().length() - 1)
			.replace(" ", "") + "," + this.pointNumber + "\n"); // ((this.pointNumber - 1.0) / (1000.0 -
									    // 1.0))
		output.close();

		FileWriter fileWritterR = new FileWriter(fileRelation, true);
		BufferedWriter outputR = new BufferedWriter(fileWritterR);
		outputR.write(this.pointNumber + " " + lat + "," + lon + "\n");
		outputR.close();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
    }

}
