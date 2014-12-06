package common.events;

import common.CommandType;

public class DisconnectEvent extends ClientEvent {

	public DisconnectEvent() {
		super(CommandType.DISCONNECT);
	}

}
