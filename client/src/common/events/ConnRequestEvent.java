package common.events;

import common.CommandType;

public class ConnRequestEvent extends ClientEvent {

	public ConnRequestEvent() {
		super(CommandType.CONN_REQUEST);
	}

}
