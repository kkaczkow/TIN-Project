package common;

public enum CommandType {
	REGISTER("register"),
	REGISTERED_ID("registered"),
	SERVICES("services"),
	LIST_AGENTS("list agents"),
	AGENTS_LIST("agents list"),
	LIST_SERVICES("list services"),
	SERVICES_LIST("services list"),
	CONN_REQUEST("conn request"),
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
	      for (CommandType tmp: CommandType.values()) {
	        if (command.equalsIgnoreCase(tmp.command)) {
	          return tmp;
	        }
	      }
	    }
	    return null;
	  }

}