package org.ksam.logic.executer.configuration;

import org.ksam.logic.generic.sender.ExampleMessageSender;
import org.loopa.comm.message.IMessage;
import org.loopa.executer.effector.IEffector;

public class ExampleExecuterMessageSender extends ExampleMessageSender {

  @Override
  protected void sendMessage(IMessage m) {
    // TODO decide if is for effector or KB
    ((IEffector) this.getComponent().getComponentRecipient(m.getMessageTo()).getRecipient())
        .effect(m);;
  }
}
