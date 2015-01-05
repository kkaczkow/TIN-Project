package common.events;

import common.CommandType;

public class RegisteredIdEvent extends ClientEvent {

	public RegisteredIdEvent(CommandType command) {
		super(command.RGISTERED_ID);
	}

}
