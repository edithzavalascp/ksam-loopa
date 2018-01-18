package org.loopasv.logic.sensors;

import org.loopa.comm.message.IMessage;
import org.loopa.monitor.IMonitor;
import org.loopa.monitor.sensors.ISensors;

public class LoopaSvSensors implements ISensors {

  IMonitor m;

  public LoopaSvSensors(IMonitor monitorOwner) {
    super();
    this.m = monitorOwner;
  }

  public void processMonitoringData(String sender, String jsonWithMonData) {
    IMessage m = null; // create a message using the sender and jsonWithMonData infor
    // send it to the monitor
    this.m.getReceiver().doOperation(m);
  }

  public void processAdaptation(String sender, String jsonWithAdaptnData) {
    IMessage m = null; // create a message using the sender and jsonWithAdaptData infor
    // send it to the monitor
    this.m.getReceiver().adapt(m);
  }
}
