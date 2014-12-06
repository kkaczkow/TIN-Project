package common.events;

import common.CommandType;

public abstract class ClientEvent {
	protected CommandType command;
	
	public ClientEvent(final CommandType command) {
		this.command = command;
	}
	
	public CommandType getCommand() {
		return command;
	}
}
