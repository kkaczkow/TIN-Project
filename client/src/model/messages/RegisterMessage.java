package model.messages;

import java.util.ArrayList;
import java.util.List;
import model.IPv4;
import model.IPv6;

public class RegisterMessage extends ClientMessage {
	List<IPv4> IPv4= new ArrayList<IPv4>();
	List<IPv6> IPv6= new ArrayList<IPv6>();
	
	public void parseInput (String IPs) {
		// Parse message type
		this.setType((byte) IPs.charAt(0));
		
		String IPtext;
		byte[] IPbuffer= new byte[4];
		int i;
		// Parse IPv4 list
		for (i= 1; i <= IPs.length(); i= i+4) {
			IPtext= IPs.substring(i, i+3);
			if (IPtext == "0000")
				break;
			for (int j= 0; j < 4; ++j)
				IPbuffer[j]= (byte) IPtext.charAt(j);
			IPv4.add(new IPv4(IPbuffer, IPtext));
		}
		
		// Parse IPv6 list
		IPbuffer= new byte[32];
		for (;i <= IPs.length(); i= i+32) {
			IPtext= IPs.substring(i, i+31);
			if (IPtext == "00000000000000000000000000000000")
				break;
			for (int j= 0; j < 32; ++j)
				IPbuffer[j]= (byte) IPtext.charAt(j);
			IPv6.add(new IPv6(IPbuffer, IPtext));
		}
	}
}
