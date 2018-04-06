package org.ksam.client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.ksam.model.adaptation.MonitorAdaptation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpenDlvClient implements IEffectorEnactor {
    private static final Logger LOGGER = LoggerFactory.getLogger(OpenDlvClient.class);
    private static final String HOST_NAME = "localhost";
    private static final int PORT = 8082;

    private final String vehicleId;

    public OpenDlvClient(String vehicleId) {
	super();
	this.vehicleId = vehicleId;
    }

    @Override
    public void enact(MonitorAdaptation a) {
	try {
	    Socket socket = new Socket(HOST_NAME, PORT);

	    DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
	    // TODO Get action and monitor from MonitorAdaptation object
	    String dataString = "VehicleId=" + vehicleId + ",Action=Deactivate,Monitor=i_rear\0";
	    byte[] data = dataString.getBytes();
	    dos.write(data);
	    LOGGER.info("OpenDlv enactor | send adaptation to be applied by vehicle" + vehicleId);

	    dos.close();
	    socket.close();
	} catch (IOException e) {
	    // LOGGER.error(e.getMessage());
	}
    }

    // public static void main(String[] args) {
    // // new OpenDlvClient().enact(new MonitorAdaptation());
    // OpenDlvClient odlvC = new OpenDlvClient("0");
    // odlvC.enact(new MonitorAdaptation());
    // }
}
