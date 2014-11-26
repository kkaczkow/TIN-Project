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
	 * Command format: 
	 *  1 - register
  		2 - registered id
  		3 - services
		4 - list agents
  		5 - agents list
  		6 - list services
  		7 - services list
  		8 - conn request
  		9 - connect
	 * @param args
	 */
	public static void main(String[] args) {
		String command = null;
		
		for(String elem: args)
		{
			command += elem;
		}
		
		switch(CommandType.fromString(command)) {
		
		case REGISTER:
			break;
			
		case REGISTERED_ID:
			break;
			
		case SERVICES:
			break;
			
		case LIST_AGENTS:
			break;
			
		case AGENTS_LIST:
			break;
			
		case LIST_SERVICES:
			break;
			
		case SERVICES_LIST:
			break;
			
		case CONN_REQUEST:
			break;
			
		case CONNECT:
			mConnector.connect(serverName, port);
			break;
		
			default:
				throw new RuntimeException("unknow command");
		
		}
	      

	}

}
