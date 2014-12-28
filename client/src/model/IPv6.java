package model;

public class IPv6 {
	
	private byte address[]=  new byte[32];
	private String textRepresentation;
	
	public IPv6 (byte[] addr) {
		this.setAddress(addr);
	}
	
	public IPv6 (String addr) {
		this.setTextRepresentation(addr);
	}
	
	public IPv6 (byte[] addr, String txt) {
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
