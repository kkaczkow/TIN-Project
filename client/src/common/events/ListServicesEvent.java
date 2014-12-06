package common.events;

import common.CommandType;

public class ListServicesEvent extends ClientEvent {

	public ListServicesEvent() {
		super(CommandType.LIST_SERVICES);
	}

}
