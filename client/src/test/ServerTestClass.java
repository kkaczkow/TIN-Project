package test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ServerTestClass extends Thread {

	private ServerSocket serverSocket;
	private Socket server;
	private static int port = 6066;
	private boolean isClientConnected;
	private static int serverConnectionCounter;

	public ServerTestClass() {
		try {
			isClientConnected = false;
			serverSocket = new ServerSocket(port);
			serverSocket.setSoTimeout(10000);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		while (true && serverConnectionCounter == 0) {
			++serverConnectionCounter;
			connect();
			/*
			 * read();
			 * write();
			 */
			disconnect();
		}
	}

	private void connect() {
		System.out.println("Server: Waiting for client on port "
				+ serverSocket.getLocalPort() + "...");
		try {
			server = serverSocket.accept();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Server: Just connected to "
				+ server.getRemoteSocketAddress());
		isClientConnected = true;
	}
	
	private void read() {
		DataInputStream in;
		try {
			in = new DataInputStream(server.getInputStream());
			System.out.println(in.readUTF()); 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void write() {
		try {
			DataOutputStream out = new DataOutputStream(server.getOutputStream());
			out.writeUTF("Thank you for connecting to " + server.getLocalSocketAddress() + "\nGoodbye!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void disconnect() {
		try {
			server.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Server: Close()");
	}

	public boolean isClientConnected() {
		return isClientConnected;
	}

	public void setServerConnectionCounter(int serverConnectionCounter) {
		ServerTestClass.serverConnectionCounter = serverConnectionCounter;
	}

}
