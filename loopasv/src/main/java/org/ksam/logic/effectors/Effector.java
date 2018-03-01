package org.ksam.logic.effectors;

import org.loopa.comm.message.IMessage;
import org.loopa.executer.effector.IEffector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Effector implements IEffector {
  protected final Logger LOGGER = LoggerFactory.getLogger(getClass().getName());

  @Override
  public void effect(IMessage m) {
    LOGGER.info("Adaptation received to effect");
    // TODO Enact adaptation in the corresponding ME
  }

}
