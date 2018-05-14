package org.ksam.logic.knowledgebase.components;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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
    private IContextPersister ctxPersister;
    private Map<String, Monitor> monitors;
    private Map<String, String> pMonsVarsRuntimeData;
    private Map<String, String> contextVarsValues;
    private List<String> persistenceMonitors;
    private String meId;

    private final String filePath;
    private PublishSubject<Map<String, Double>> dataToPersistInArff;
    private final ExecutorService arffFilePersister = Executors.newSingleThreadExecutor();
    private final MonVarsRangesTranslator mvrT;

    private List<String> inactiveMons;

    public WekaPersistenceManager(String meId, Map<String, Monitor> monitors, List<String> persistenceMonitors,
	    List<MonitoringVariable> monVars, List<String> contextVars) {
	super();
	this.meId = meId;
	this.mvrT = new MonVarsRangesTranslator(monVars);
	this.persistenceMonitors = persistenceMonitors;
	this.filePath = "/tmp/weka/" + this.meId + ".arff";
	this.pMonsVarsRuntimeData = new HashMap<>();
	this.monitors = monitors;
	this.contextVarsValues = new HashMap<>();
	contextVars.forEach(var -> this.contextVarsValues.put(var, "0"));
	this.ctxPersister = new OpenDlvContextPersister(contextVars);
	createHeader();
	this.inactiveMons = new ArrayList<>();

	this.dataToPersistInArff = PublishSubject.create();
	this.dataToPersistInArff.subscribe(monVarData -> arffFilePersister.execute(() -> {
	    this.pMonsVarsRuntimeData.forEach((k, v) -> {
		if (monVarData.keySet().contains(k)) {
		    this.pMonsVarsRuntimeData.put(k, this.mvrT.getValueRange(k.split("-")[1], monVarData.get(k)));
		}
		if (this.inactiveMons.contains(k.split("-")[0])) {
		    this.pMonsVarsRuntimeData.put(k, "?");
		}
	    });

	    setToArffFile("\n"
		    + this.pMonsVarsRuntimeData.values().toString()
			    .substring(1, this.pMonsVarsRuntimeData.values().toString().length() - 1).replace(" ", "")
		    + ","
		    + this.contextVarsValues.values().toString()
			    .substring(1, this.contextVarsValues.values().toString().length() - 1).replace(" ", ""),
		    true);
	}));

    }

    public void setMonitoringData(Map<String, Double> monVarValue) {
	this.dataToPersistInArff.onNext(monVarValue);
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

    public void createHeader() {
	this.persistenceMonitors.forEach(am -> {
	    this.monitors.get(am).getMonitorAttributes().getMonitoringVars()
		    .forEach(var -> this.pMonsVarsRuntimeData.put(am + "-" + var, "?"));
	});

	String header = "@relation " + this.meId + "\n\n";

	for (String monVar : this.pMonsVarsRuntimeData.keySet()) {
	    header += "@attribute " + monVar + " " + this.mvrT.getVarRanges(monVar.split("-")[1]) + "\n";
	}

	for (String ctxVar : this.contextVarsValues.keySet()) {
	    header += "@attribute " + ctxVar + " {0,1}\n";
	}

	header += "\n@data";
	setToArffFile(header, false);
    }

    public void updateActiveMonitors(List<String> activeMonitors) {
	this.persistenceMonitors.forEach(m -> {
	    if (!activeMonitors.contains(m)) {
		this.inactiveMons.add(m);
	    }
	});
    }

    public Map<String, Integer> setContextData(List<Entry<String, Object>> context) {
	Map<String, Integer> cxtVarVal = this.ctxPersister.updateContext(context);
	cxtVarVal.forEach((k, v) -> this.contextVarsValues.put(k, String.valueOf(v)));
	return cxtVarVal;
    }

}
