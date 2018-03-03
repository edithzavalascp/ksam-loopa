package org.ksam.logic.sensors;

import java.util.HashMap;
import java.util.Map;

import org.loopa.comm.message.IMessage;
import org.loopa.comm.message.LoopAElementMessageCode;
import org.loopa.comm.message.Message;
import org.loopa.comm.message.MessageType;
import org.loopa.monitor.IMonitor;
import org.loopa.monitor.sensor.ISensor;

public class Sensor implements ISensor {
    private final IMonitor m;

    public Sensor(IMonitor initializedM) {
	m = initializedM;
    }

    public Map<String, String> processMonData(String senderID, String monData) {
	Map<String, String> mapMonData = new HashMap<String, String>() {
	    {
		put("monData", monData);
		put("systemId", senderID);
	    }
	};
	IMessage mssg = new Message(senderID, m.getElementId(),
		Integer.parseInt(
			m.getElementPolicy().getPolicyContent().get(LoopAElementMessageCode.MSSGINFL.toString())),
		MessageType.RESPONSE.toString(), mapMonData);
	m.getReceiver().doOperation(mssg);
	return mssg.getMessageBody();
    }

}
