package controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class Connector {

	private Socket mClient;
	private boolean isServerConnected;
	private static final Logger LOGGER = Logger.getLogger(Controller.class);
	
	public Connector() {
		isServerConnected = false;
		BasicConfigurator.configure();
	}
	
	public Socket getSocket() {
		return mClient;
	}
	
	public void connect(final String serverName, final int portNumber) {
		if (mClient != null)
		{
			/* we're connected, disconnect before connecting again */
			disconnect();
		}
	    try {
	    	LOGGER.info("Client: Connecting to " + serverName + " on port " + portNumber);
	    	mClient = new Socket(serverName, portNumber);
	    	isServerConnected = true;
		    LOGGER.info("Client: Just connected to " + mClient.getRemoteSocketAddress());
	    }
	    catch (IOException e) {
	        LOGGER.error(e);
	    }
	}
	
	/**
	 * Receiving data from server
	 */
	public void getData() {
		try {
			InputStream inFromServer = mClient.getInputStream();
	         DataInputStream in =  new DataInputStream(inFromServer);
	         LOGGER.info("Server says " + in.readUTF());
			in.close();
		} catch(NullPointerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sending message to server
	 * @param message - command
	 */
	//public void sendData(String message) {
	public void sendData(byte message) {
        try {
        	OutputStream outToServer = mClient.getOutputStream();
        	DataOutputStream out = new DataOutputStream(outToServer);
        	out.writeByte(message);
			//out.writeUTF(message + mClient.getLocalSocketAddress());
			out.close();
		} catch(NullPointerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void disconnect() {
		try {
			LOGGER.info("Client: Close()");
	        mClient.close();
	        mClient = null;
	    } 
	    catch (IOException e) {
	    	LOGGER.error(e);
	    }
	}
	
	public boolean isServerConnected() {
		return isServerConnected;
	}
    
}
