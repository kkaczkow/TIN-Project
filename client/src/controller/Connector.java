package controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import org.apache.log4j.Logger;

public class Connector {

	private Socket mClient;
	private boolean isServerConnected;
	private static final Logger LOGGER = Logger.getLogger(Controller.class);

	public Connector() {
		isServerConnected = false;
	}

	public Socket getSocket() {
		return mClient;
	}

	public void connect(final String serverName, final int portNumber) {
		if (mClient != null) {
			// we're connected, disconnect before connecting again
			disconnect();
		}
		try {
			LOGGER.info("Client: Connecting to " + serverName + " on port "
					+ portNumber);
			mClient = new Socket(serverName, portNumber);
			isServerConnected = true;
			LOGGER.info("Client: Just connected to "
					+ mClient.getRemoteSocketAddress());
		} catch (IOException e) {
			LOGGER.error(e);
		}
	}

	/**
	 * Receiving data from server
	 */
	public String getData() {
		String message = null;
		try {
			InputStream inFromServer = mClient.getInputStream();
			DataInputStream in = new DataInputStream(inFromServer);
			message = in.readUTF();
			LOGGER.info("Client: Server says: " + message);
			in.close();
		} catch (NullPointerException e) {
			LOGGER.error(e);
		} catch (IOException e) {
			LOGGER.error(e);
		}
		return message;
	}

	/**
	 * Sending message to server
	 * 
	 * @param message
	 *            - command
	 */
	public void sendData(byte message) {
		try {
			OutputStream outToServer = mClient.getOutputStream();
			DataOutputStream out = new DataOutputStream(outToServer);
			out.writeByte(message);
			out.close();
		} catch (NullPointerException e) {
			LOGGER.error(e);
		} catch (IOException e) {
			LOGGER.error(e);
		}
	}

	public void disconnect() {
		try {
			LOGGER.info("Client: Close()");
			mClient.close();
			mClient = null;
		} catch (IOException e) {
			LOGGER.error(e);
		}
	}

	public boolean isServerConnected() {
		return isServerConnected;
	}

}
