package model;

public class Service {
	private byte flags;
	private int id;
	private int port;
	
	/**
	 * Construct service from data
	 * @param data - format of string: flags;id;port
	 */
	public Service (String data) {
		String[] serv= data.split(";");
		flags= Byte.parseByte(serv[0]);
		id= Short.parseShort(serv[1]);
		port= Short.parseShort(serv[2]);
	}
	
	public byte getFlags() {
		return flags;
	}
	public void setFlags(byte flags) {
		this.flags = flags;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
}
