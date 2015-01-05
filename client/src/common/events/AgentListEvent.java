package common.events;

import common.CommandType;

public class AgentListEvent extends ClientEvent {

	public AgentListEvent(CommandType command) {
		super(command.AGENT_LIST);
	}

}
