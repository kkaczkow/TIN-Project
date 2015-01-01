package common.events;

import common.CommandType;

public class ServicesEvent extends ClientEvent {

	private String servicesList;
	
	public ServicesEvent(final String services) {
		super(CommandType.SERVICES);
		setServicesList(services);
	}

	public String getServicesList() {
		return servicesList;
	}

	public void setServicesList(String servicesList) {
		this.servicesList = servicesList;
	}

}
