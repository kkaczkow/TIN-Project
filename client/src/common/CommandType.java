package common;

public enum CommandType {
	LIST_AGENTS("list agents", (byte)0x03),
	LIST_SERVICES("list services", (byte)0x04),
	SERVICES("services", (byte)0x02),
	REGISTER("register", (byte)0x01),
	CONN_REQUEST("conn request", (byte)0x05),
	DISCONNECT("disconnect"),
	CONNECT("connect"),
	AGENT_LIST("agent list", (byte)0x83),
	REGISTERED("registered id", (byte)0x81),
	SERVICES_LIST("services list", (byte)0x84);
	
	private String command;
	private byte byteToSend;
	
	CommandType(String command, byte byteToSend) {
		this.command = command;
		this.byteToSend = byteToSend;
	}
	
	CommandType(String command) {
		this.command = command;
	}
	
	public byte getByteToSend () {
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