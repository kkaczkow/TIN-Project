package test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import org.apache.log4j.Logger;

/**
 * A dummy server class
 *
 */
public class ServerTestClass{

	private ServerSocket serverSocket;
	private Socket server;
	private boolean isClientConnected;
	private static int serverConnectionCounter;
	private static final Logger LOGGER = Logger.getLogger(ServerTestClass.class);

	public ServerTestClass(final int port) {
		try {
			isClientConnected = false;
			serverSocket = new ServerSocket(port);
			serverSocket.setSoTimeout(10000);

		} catch (IOException e) {
			LOGGER.error(e);
		}
	}

	public void runConnectionTest() {
		while (true && serverConnectionCounter == 0) {
			++serverConnectionCounter;
			connect();
			disconnect();
		}
	}
	
	public String runReadTest() {
		String receivedMessage = null;
		while (true && serverConnectionCounter == 0) {
			++serverConnectionCounter;
			connect();
			receivedMessage = read();
			disconnect();
			
		}
		return receivedMessage;
	}
	
	public void runWriteTest(final String message) {
		while (true && serverConnectionCounter == 0) {
			++serverConnectionCounter;
			connect();
			write(message);
			disconnect();
		}
	}

	private void connect() {
		LOGGER.info("Server: Waiting for client on port "
				+ serverSocket.getLocalPort() + "...");
		try {
			server = serverSocket.accept();
		} catch (IOException e) {
			LOGGER.error(e);
		}
		LOGGER.info("Server: Just connected to "
				+ server.getLocalSocketAddress());
		isClientConnected = true;
	}
	
	private String read() {
		String receivedMessage = null;
		DataInputStream in;
		try {
			in = new DataInputStream(server.getInputStream());
			receivedMessage = Byte.toString(in.readByte());
			LOGGER.info("Server: Read: " + receivedMessage);
		} catch (IOException e) {
			LOGGER.error(e);
			e.printStackTrace();
		}
		return receivedMessage;
	}
	
	private void write(final String message) {
		try {
			DataOutputStream out = new DataOutputStream(server.getOutputStream());
			out.writeUTF(message);
		} catch (IOException e) {
			LOGGER.error(e);
		}
	}
	
	private void disconnect() {
		try {
			server.close();
			serverSocket.close();
		} catch (IOException e) {
			LOGGER.error(e);
		}
		LOGGER.info("Server: Close()");
	}

	public boolean isClientConnected() {
		return isClientConnected;
	}

	public void setServerConnectionCounter(int serverConnectionCounter) {
		ServerTestClass.serverConnectionCounter = serverConnectionCounter;
	}

}
