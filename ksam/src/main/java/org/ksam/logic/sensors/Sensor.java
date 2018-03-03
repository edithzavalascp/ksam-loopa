package org.ksam.logic.sensors;

import java.util.Map;

import org.loopa.comm.message.IMessage;
import org.loopa.comm.message.LoopAElementMessageCode;
import org.loopa.comm.message.Message;
import org.loopa.comm.message.MessageType;
import org.loopa.monitor.IMonitor;
import org.loopa.monitor.sensor.ISensor;

public class Sensor implements ISensor {
    private static IMonitor m;

    public static void setMonitor(IMonitor initializedM) {
	m = initializedM;
    }

    public static void processMonData(String senderID, Map<String, String> sensorData) {
	IMessage mssg = new Message(senderID, m.getElementId(),
		Integer.parseInt(
			m.getElementPolicy().getPolicyContent().get(LoopAElementMessageCode.MSSGINFL.toString())),
		MessageType.RESPONSE.toString(), sensorData);
	m.getReceiver().doOperation(mssg);
    }

}
