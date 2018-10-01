package org.ksam.logic.analyzer.components;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class PositionAnalysisManager {
    private final boolean isLearning;

    public PositionAnalysisManager(Boolean isLearning) {
	this.isLearning = isLearning;
    }

    public String getLatLon(String point) {
	String latlon = null;
	if (!isLearning) {
	    try {
		for (String line : Files.lines(Paths.get("/tmp/weka/pointsLatLon.txt")).collect(Collectors.toList())) {
		    String[] data = line.split(" ");
		    if (point.equals(data[0])) {
			latlon = data[1];
			break;
		    }
		}
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
	return latlon;
    }

}
