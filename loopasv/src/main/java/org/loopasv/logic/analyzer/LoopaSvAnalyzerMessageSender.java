package org.loopasv.logic.analyzer;

import java.util.Map;
import org.loopa.comm.message.IMessage;
import org.loopa.comm.message.Message;
import org.loopa.element.sender.messagesender.AMessageSender;
import org.loopa.generic.element.ILoopAElement;

public class LoopaSvAnalyzerMessageSender extends AMessageSender {

  @Override
  public void processMessage(IMessage t) {
    IMessage m = process(t);
    if (m != null)
      sendMessage(m);
  }

  protected IMessage process(IMessage m) {
    return new Message(this.getComponent().getComponentId(),
        getRecipientFromPolicy(m.getMessageBody()), m.getMessageCode(), m.getMessageType(),
        m.getMessageBody());
  }

  protected String getRecipientFromPolicy(Map<String, String> messageBody) {
    return this.getPolicyVariables().get(messageBody.get("type"));
  }

  protected void sendMessage(IMessage m) {
    /* decide if is for planner or KB */
    ILoopAElement r =
        (ILoopAElement) this.getComponent().getComponentRecipients().get(m.getMessageTo());
    r.getReceiver().doOperation(m);
  }

}
