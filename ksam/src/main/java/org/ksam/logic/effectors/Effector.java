package org.ksam.logic.effectors;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.ksam.client.IEffectorEnactor;
import org.ksam.model.adaptation.MonitorAdaptation;
import org.loopa.comm.message.IMessage;
import org.loopa.executer.effector.IEffector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	MonitorAdaptation a = new MonitorAdaptation();
	a.setAdaptId(m.getMessageBody().get("adaptId"));
	a.setMonitorsToAdd(Stream.of(m.getMessageBody().get("monitorsToAdd")).collect(Collectors.toList()));
	a.setMonitorsToRemove(Stream.of(m.getMessageBody().get("monitorsToRemove")).collect(Collectors.toList()));
	meEnactor.enact(a);
    }

}
