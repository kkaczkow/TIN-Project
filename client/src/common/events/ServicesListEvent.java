package common.events;

import common.CommandType;

public class ServicesListEvent extends ClientEvent {

	public ServicesListEvent(CommandType command) {
		super(command.SERVICES_LIST);
	}

}
