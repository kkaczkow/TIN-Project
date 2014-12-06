package common.events;

import common.CommandType;

public class ListAgentsEvent extends ClientEvent {

	public ListAgentsEvent() {
		super(CommandType.LIST_AGENTS);
	}

}
