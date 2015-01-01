package model;

import java.util.List;

public class ServerMessageParser {
	//TODO Fill all functions
	public static void chooseInputParser(byte[] packet) {
		switch (packet[0]) 
		{
			case (byte) 0x81:
				parseMyID(packet);;
				break;
			case (byte) 0x83:
				parseAgentsList(packet);
				break;
			case (byte) 0x84:
				parseServicesList(packet);
				break;
				
		}
	}
	
	public static void parseAgentsList(byte[] packet) {
		
	}
	
	public static void parseServicesList(byte[] packet) {
		
	}
	
	public static void parseMyID(byte[] packet) {
		
	}
	
	public static void start(byte[] packet, int ID, List<Agent> agentsList, List<Service> servicesList) {
		ServerMessageParser.chooseInputParser(packet);
	}
}
