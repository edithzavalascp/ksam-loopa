package org.loopasv.logic.effectors;

import org.loopa.comm.message.IMessage;
import org.loopa.executer.effectors.IEffectors;
import org.loopasv.client.LoopaSvClient;


public class LoopaSvEffectors implements IEffectors {

  public String dispatch(IMessage m) {
    // transform message, e.g., into a JSON if requires and call the corresponding call from client
    return new LoopaSvClient().callGet().toString();
  }
}
