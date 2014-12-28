package model.messages;

public abstract class ClientMessage {
	protected byte type;
	
	public byte getType() {
		return this.type;
	}
	
	public void setType(byte type) {
		this.type= type;
	}
}
