package model;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class ClientMessageParser {
	
	public static List<Inet4Address> parserIPv4(String ipV4) throws UnknownHostException {
		ArrayList<Inet4Address> result= new ArrayList<Inet4Address>();
		if (ipV4.isEmpty())
			return result;
		String[] ips = ipV4.split(" ");
		for (String ip : ips)
			result.add((Inet4Address)InetAddress.getByName(ip));
		return result;
	}
	
	public static List<Inet6Address> parserIPv6(String ipV6) throws UnknownHostException {
		ArrayList<Inet6Address> result= new ArrayList<Inet6Address>();
		if (ipV6.isEmpty())
			return result;
		String[] ips = ipV6.split(" ");
		for (String ip : ips)
			result.add((Inet6Address)InetAddress.getByName(ip));
		return result;
	}
	
	/**
	 * 
	 * @param serv - format of string: flag1;id1;port1 flag2;id2;port2 ...
	 * @return list of Services
	 */
	public static List<Service> parserServices (String serv) {
		ArrayList<Service> result= new ArrayList<Service>();
		if (serv.isEmpty())
			return result;
		String[] services= serv.split(" ");
		for (String s : services) {
			result.add(new Service(s));
		}
		return result;
	}
}
