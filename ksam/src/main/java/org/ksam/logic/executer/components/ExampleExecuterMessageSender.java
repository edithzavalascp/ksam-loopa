package org.ksam.logic.executer.components;

import org.ksam.logic.generic.sender.ExampleMessageSender;
import org.loopa.comm.message.IMessage;
import org.loopa.executer.effector.IEffector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleExecuterMessageSender extends ExampleMessageSender {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass().getName());

    @Override
    protected void sendMessage(IMessage m) {
	// TODO decide if is for effector or KB - now all messages go to effectors
	((IEffector) this.getComponent().getComponentRecipient(m.getMessageTo()).getRecipient()).effect(m);
    }
}
