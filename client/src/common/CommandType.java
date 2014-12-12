package common;

public enum CommandType {
	LIST_AGENTS("list agents", (char)0x03),
	LIST_SERVICES("list services", (char)0x04),
	DISCONNECT("disconnect"),
	CONNECT("connect");
	
	private String command;
	private char byteToSend;
	
	CommandType(String command, char byteToSend) {
		this.command = command;
		this.byteToSend = byteToSend;
	}
	
	CommandType(String command) {
		this.command = command;
	}
	
	public String getCommand() {
		return this.command;
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