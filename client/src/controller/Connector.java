package controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Connector {

	private Socket mClient;
	
	public void connect(final String serverName, final int portNumber) {
		
	    try {
	    	mClient = new Socket(serverName, portNumber);
	    }
	    catch (IOException e) {
	        System.out.println(e);
	    }
	    System.out.println("Just connected to " + mClient.getRemoteSocketAddress());

	}
	
	/**
	 * Receiving data from server
	 */
	public void getData() {
		try {
			InputStream inFromServer = mClient.getInputStream();
	         DataInputStream in =
	                        new DataInputStream(inFromServer);
	         
	         System.out.println("Server says " + in.readUTF());
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
	public void sendData(String message) {

        try {
        	OutputStream outToServer = mClient.getOutputStream();
        	DataOutputStream out = new DataOutputStream(outToServer);
			out.writeUTF(message
			             + mClient.getLocalSocketAddress());
			out.close();
		} catch(NullPointerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void disconnect() {
		try {
	           mClient.close();
	    } 
	    catch (IOException e) {
	       System.out.println(e);
	    }
	}
    
}
