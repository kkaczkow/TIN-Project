package common;

public enum CommandType {
	REGISTER("register"),
	SERVICES("services"),
	LIST_AGENTS("list agents"),
	LIST_SERVICES("list services"),
	DISCONNECT("disconnect"),
	CONNECT("connect");
	
	private String command;
	
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