package test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ServerTestClass extends Thread{

	private ServerSocket serverSocket;
	private static int port = 6066;
	private boolean isClientConnected;
	private static int serverConnectionCounter;
	   
	   public ServerTestClass()
	   {
	      try {
	    	  isClientConnected = false;
	    	  serverSocket = new ServerSocket(port);
	    	  serverSocket.setSoTimeout(10000);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	   }
	   
	   public void run()
	   {
	      while(true && serverConnectionCounter == 0)
	      {
	         try
	         {
	        	++serverConnectionCounter;
	            System.out.println("Server: Waiting for client on port " +
	            serverSocket.getLocalPort() + "...");
	            Socket server = serverSocket.accept();
	            System.out.println("Server: Just connected to " + server.getRemoteSocketAddress());
	            isClientConnected = true;
	            
	            /*DataInputStream in = new DataInputStream(server.getInputStream());
	            System.out.println(in.readUTF());
	            DataOutputStream out = new DataOutputStream(server.getOutputStream());
	            out.writeUTF("Thank you for connecting to " + server.getLocalSocketAddress() + "\nGoodbye!");*/
	            server.close();
	            System.out.println("Server: Close()");
	            
	         } catch(SocketTimeoutException s)
	         {
	            System.out.println("Socket timed out!");
	            break;
	            
	         } catch(IOException e)
	         {
	            e.printStackTrace();
	            break;
	         }
	      }
	   }
	   
	   public boolean isClientConnected() {
		   return isClientConnected;
	   }
	   
	   public void setServerConnectionCounter(int serverConnectionCounter) {
		   this.serverConnectionCounter = serverConnectionCounter;
	   }

}
