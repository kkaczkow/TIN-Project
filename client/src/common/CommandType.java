package common;

public enum CommandType {
	LIST_AGENTS("list agents", (byte)0x03),
	LIST_SERVICES("list services", (byte)0x04),
	DISCONNECT("disconnect"),
	CONNECT("connect");
	
	private String command;
	private byte byteToSend;
	
	CommandType(String command, byte byteToSend) {
		this.command = command;
		this.byteToSend = byteToSend;
	}
	
	CommandType(String command) {
		this.command = command;
	}
	
	public  byte getByteToSend () {
		return byteToSend;
	}
	public static CommandType fromString(String command) {
		if (command != null) {
			for (CommandType tmp : CommandType.values()) {
				if (command.equalsIgnoreCase(tmp.command)) {
					return tmp;
				}
			}
		}
		return null;
	}

}