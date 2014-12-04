package controller;

import common.CommandType;

public class Controller {
	
	private static Connector mConnector;
	private static String serverName ="localhost";
	private static int port = 666;
	
	public Controller() {
		mConnector = new Connector();
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		String command = null;
		
		for(String elem: args)
		{
			command += elem;
		}
		
		switch(CommandType.fromString(command)) {
		
		case REGISTER:SERVICES:LIST_AGENTS:LIST_SERVICES:
			mConnector.sendData(command);
			mConnector.getData();
			break;
			
		case CONNECT:
			mConnector.connect(serverName, port);
			break;
			
		case DISCONNECT:
			mConnector.disconnect();
			break;
		
			default:
				throw new RuntimeException("unknow command");
		
		}
	      

	}

}