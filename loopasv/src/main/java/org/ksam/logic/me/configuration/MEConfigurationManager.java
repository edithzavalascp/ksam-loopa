package org.ksam.logic.me.configuration;

import java.io.File;
import java.io.IOException;

import org.ksam.logic.autonomicmanager.LoopManager;
import org.ksam.model.configuration.operation.MeConfig;

import com.fasterxml.jackson.databind.ObjectMapper;

public class MEConfigurationManager {
    // private final Logger LOGGER = LoggerFactory.getLogger(getClass().getName());
    private static LoopManager loop;

    public static boolean addME(File mEConfigFile) {
	// TODO call loop AM for adding the ME
	// Call sensors (sensors compose a message and send it to LoopaMonitor)
	// sensors.processMonitoringData(systemId, "jsonReceived");

	// loopa.addME("var1:value1",
	// new Recipient("effector",
	// Arrays.asList(AMMessageBodyType.ADAPTATION.toString()), new Effector()));
	boolean isValidConfig = true;
	ObjectMapper mapper = new ObjectMapper();
	try {
	    mapper.readValue(mEConfigFile, MeConfig.class);
	} catch (IOException e) {
	    isValidConfig = false;
	    e.printStackTrace();
	}
	return isValidConfig;
    }

    public static void setLoop(LoopManager initilizedLoop) {
	loop = initilizedLoop;
    }
}
