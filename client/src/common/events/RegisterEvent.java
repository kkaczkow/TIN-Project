package common.events;

import common.CommandType;

public class RegisterEvent extends ClientEvent {
	
	private String ipv4;
	private String ipv6;

	public RegisterEvent(final String ipv4, final String ipv6) {
		super(CommandType.REGISTER);
		this.setIpV4(ipv4);
		this.setIpV6(ipv6);
	}

	public String getIpV4() {
		return ipv4;
	}

	public void setIpV4(String ipv4) {
		this.ipv4 = ipv4;
	}

	public String getIpV6() {
		return ipv6;
	}

	public void setIpV6(String ipv6) {
		this.ipv6 = ipv6;
	}

}
