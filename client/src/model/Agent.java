package model;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.util.ArrayList;
import java.util.List;


public class Agent {
	
	private int ID;
	List<Inet4Address> IPv4;
	List<Inet6Address> IPv6;
	
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
}
