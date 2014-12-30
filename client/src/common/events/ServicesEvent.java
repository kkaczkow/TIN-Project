package common.events;

import common.CommandType;

public class ServicesEvent extends ClientEvent {
	
	private String ipv4;
	private String ipv6;

	public ServicesEvent(final String ipv4, final String ipv6) {
		super(CommandType.SERVICES);
		this.ipv4 = ipv4;
		this.ipv6 = ipv6;
	}

}
