package model;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.util.List;


public class Agent {
	
	private final int ID;
	private final List<Inet4Address> IPv4;
	private final List<Inet6Address> IPv6;
	
	public Agent(int ID, List<Inet4Address> IPv4, List<Inet6Address> IPv6) {
		this.ID = ID;
		this.IPv4 = IPv4;
		this.IPv6 = IPv6;
	}
	
	public int getID() {
		return ID;
	}

	public List<Inet4Address> getIPv4() {
		return IPv4;
	}

	public List<Inet6Address> getIPv6() {
		return IPv6;
	}
}
