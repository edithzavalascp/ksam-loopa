package org.ksam.logic.knowledgebase.components;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.ksam.model.configuration.monitors.Monitor;
import org.ksam.model.configuration.monitors.MonitoringVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.reactivex.subjects.PublishSubject;

public class WekaPersistenceManager {
    protected final Logger LOGGER = LoggerFactory.getLogger(getClass().getName());
    private Map<String, Monitor> monitors;
    private Map<String, String> activeMonsVarsRuntimeData;
    private List<String> persistenceMonitors;

    private final String filePath;
    private String meId;

    private PublishSubject<Entry<String, Double>> dataToPersistInArff;
    private final ExecutorService arffFilePersister = Executors.newSingleThreadExecutor();
    private final MonVarsRangesTranslator mvrT;

    public WekaPersistenceManager(String meId, Map<String, Monitor> monitors, List<String> persistenceMonitors,
	    List<MonitoringVariable> monVars) {
	super();
	this.meId = meId;
	this.mvrT = new MonVarsRangesTranslator(monVars);
	this.persistenceMonitors = persistenceMonitors;
	this.filePath = "/tmp/weka/" + this.meId + ".arff";
	this.activeMonsVarsRuntimeData = new HashMap<>();
	this.monitors = monitors;
	this.dataToPersistInArff = PublishSubject.create();
	this.dataToPersistInArff.subscribe(monVarData -> arffFilePersister.execute(() -> {
	    if (this.activeMonsVarsRuntimeData.keySet().contains(monVarData.getKey())) {
		this.activeMonsVarsRuntimeData.put(monVarData.getKey(),
			this.mvrT.getValueRange(monVarData.getKey().split("-")[1], monVarData.getValue()));
		LOGGER.info("ksamKb - WekaPersistenceManager | Persist weka data");
		// if (!this.activeMonsVarsRuntimeData.values().contains(null)) {
		this.arffFilePersister.execute(() -> {
		    setToArffFile("\n" + this.activeMonsVarsRuntimeData.values().toString().substring(1,
			    this.activeMonsVarsRuntimeData.values().toString().length() - 1), true);
		    // this.activeMonsVarsRuntimeData.forEach((k, v) ->
		    // this.activeMonsVarsRuntimeData.put(k, null));
		    // LOGGER.info(this.activeMonsVarsRuntimeData.toString());
		});
		// }
	    }
	}));
    }

    public void setMonitoringData(String monVar, Double value) {
	this.dataToPersistInArff.onNext(new AbstractMap.SimpleEntry<>(monVar, value));
    }

    public void setToArffFile(String stringToWrite, boolean append) {
	// LOGGER.info("Data to persist in arff: " + stringToWrite);
	try {
	    File file = new File(this.filePath);
	    if (!file.exists()) {
		file.createNewFile();
	    }

	    FileWriter fileWritter = new FileWriter(file, append);
	    BufferedWriter output = new BufferedWriter(fileWritter);
	    output.write(stringToWrite);
	    output.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public void updateHeader(List<String> activeMonitors) {
	this.activeMonsVarsRuntimeData.clear();
	activeMonitors.forEach(am -> {
	    if (this.persistenceMonitors.contains(am)) {
		this.monitors.get(am).getMonitorAttributes().getMonitoringVars()
			.forEach(var -> this.activeMonsVarsRuntimeData.put(am + "-" + var, "?"));
	    }
	});

	// LOGGER.info(this.activeMonitors.toString() + " " +
	// this.activeMonsVarsRuntimeData.toString());

	String header = "@relation " + this.meId + "\n\n";
	// for (String m : this.activeMonitors) {
	for (String monVar : this.activeMonsVarsRuntimeData.keySet()) {
	    header += "@attribute " + monVar + " " + this.mvrT.getVarRanges(monVar.split("-")[1]) + "\n";
	}
	// }

	header += "\n@data";
	setToArffFile(header, false);
    }

}
