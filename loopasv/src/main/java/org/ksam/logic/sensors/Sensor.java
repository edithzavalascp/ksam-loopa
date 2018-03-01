package org.ksam.logic.sensors;

import java.util.Map;
import org.loopa.comm.message.IMessage;
import org.loopa.comm.message.LoopAElementMessageCode;
import org.loopa.comm.message.Message;
import org.loopa.comm.message.MessageType;
import org.loopa.monitor.IMonitor;
import org.loopa.monitor.sensor.ISensor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Sensor implements ISensor {
  protected final Logger LOGGER = LoggerFactory.getLogger(getClass().getName());

  private final IMonitor m;


  public Sensor(IMonitor m) {
    super();
    this.m = m;
  }

  @Override
  public void processSensorData(String senderID, Map<String, String> sensorData) {
    LOGGER.info("Send data to LoopA");
    IMessage mssg = new Message(senderID, m.getElementId(),
        Integer.parseInt(m.getElementPolicy().getPolicyContent()
            .get(LoopAElementMessageCode.MSSGINFL.toString())),
        MessageType.RESPONSE.toString(), sensorData);
    this.m.getReceiver().doOperation(mssg);
  }

}
