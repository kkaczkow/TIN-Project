package common.events;

import common.CommandType;

public class ConnectEvent extends ClientEvent {

	public ConnectEvent() {
		super(CommandType.CONNECT);
	}
	
}
