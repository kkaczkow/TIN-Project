package model;

public class IPv4 {
	private byte address[]=  new byte[4];
	private String textRepresentation;
	
	public IPv4 (byte[] addr) {
		this.setAddress(addr);
	}
	
	public IPv4 (String addr) {
		this.setTextRepresentation(addr);
	}
	
	public IPv4 (byte[] addr, String txt) {
		this.setAddress(addr);
		this.setTextRepresentation(txt);
	}
	
	public byte[] getAddress () {
		return address;
	}
	
	public void setAddress(byte address[]) {
		this.address = address;
	}

	public String getTextRepresentation() {
		return textRepresentation;
	}

	public void setTextRepresentation(String textRepresentation) {
		this.textRepresentation = textRepresentation;
	}
}
