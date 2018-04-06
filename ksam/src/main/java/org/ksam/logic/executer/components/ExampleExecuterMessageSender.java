package org.ksam.logic.executer.components;

import org.ksam.logic.generic.sender.ExampleMessageSender;
import org.loopa.comm.message.IMessage;
import org.loopa.executer.effector.IEffector;

public class ExampleExecuterMessageSender extends ExampleMessageSender {

    @Override
    protected void sendMessage(IMessage m) {
	// TODO decide if is for effector or KB - now all messages go to effectors
	((IEffector) this.getComponent().getComponentRecipient(m.getMessageTo()).getRecipient()).effect(m);
    }
}
