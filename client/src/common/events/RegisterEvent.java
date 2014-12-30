package common.events;

import common.CommandType;

public class RegisterEvent extends ClientEvent {
	
	private String ipv4;
	private String ipv6;

	public RegisterEvent(final String ipv4, final String ipv6) {
		super(CommandType.REGISTER);
		this.ipv4 = ipv4;
		this.ipv6 = ipv6;
	}

}
