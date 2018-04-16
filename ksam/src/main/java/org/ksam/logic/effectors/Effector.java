package org.ksam.logic.effectors;

import java.io.IOException;

import org.ksam.client.IEffectorEnactor;
import org.loopa.comm.message.IMessage;
import org.loopa.executer.effector.IEffector;
import org.model.executeData.ExecuteAlert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Effector implements IEffector {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass().getName());

    private final IEffectorEnactor meEnactor;

    public Effector(IEffectorEnactor meEnactor) {
	super();
	this.meEnactor = meEnactor;
    }

    @Override
    public void effect(IMessage m) {
	LOGGER.info("Effector | receive adaptation to effect");
	LOGGER.info("Effector | send adaptation to enact");
	try {
	    ObjectMapper mapper = new ObjectMapper();
	    ExecuteAlert data = mapper.readValue(m.getMessageBody().get("content"), ExecuteAlert.class);
	    data.getMonAdaptations().forEach(adaptation -> {
		// MonitorAdaptation a = new MonitorAdaptation();
		// a.setAdaptId(m.getMessageBody().get("adaptId"));
		// a.setMonitorsToAdd(Stream.of(m.getMessageBody().get("monitorsToAdd")).collect(Collectors.toList()));
		// a.setMonitorsToRemove(Stream.of(m.getMessageBody().get("monitorsToRemove")).collect(Collectors.toList()));
		meEnactor.enact(adaptation);
	    });
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

}
