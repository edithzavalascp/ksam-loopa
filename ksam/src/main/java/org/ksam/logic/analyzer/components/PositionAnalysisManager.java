package org.ksam.logic.analyzer.components;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class PositionAnalysisManager {
    private Map<String, String> pointsToLatLon;

    public PositionAnalysisManager() {
	this.pointsToLatLon = new HashMap<>();
	loadPoints();
    }

    public String getLatLon(String point) {
	return this.pointsToLatLon.get(point);
    }

    private void loadPoints() {
	try {
	    Files.lines(Paths.get("/tmp/weka/pointsToLatLon.txt")).forEach(line -> {
		String[] data = line.split(" ");
		this.pointsToLatLon.put(data[0], data[1]);
	    });
	    ;
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

}
