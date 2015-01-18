package common.events;

import java.util.ArrayList;
import java.util.List;

import common.CommandType;

public class ListServicesEvent extends ClientEvent {

	private final List<Integer> agentFilter;
	private final List<Short> servicesFilter;
	
	public ListServicesEvent(List<Integer> agentFilter, List<Short> servicesFilter) {
		super(CommandType.LIST_SERVICES);
		this.agentFilter = agentFilter;
		this.servicesFilter = servicesFilter;
	}

	public ListServicesEvent(String agentFilter, String serviceFilter) {
		super(CommandType.LIST_SERVICES);
		this.agentFilter = new ArrayList<Integer>();
		this.servicesFilter = new ArrayList<Short>();
		if (!agentFilter.isEmpty()) {
			String[] ids = agentFilter.split(" ");
			for (String id : ids) {
				this.agentFilter.add(Integer.parseInt(id));
			}
		}
		if (!serviceFilter.isEmpty()) {
			String[]ids = serviceFilter.split(" ");
			for (String id : ids) {
				this.servicesFilter.add(Short.parseShort(id));
			}
		}
	}

	public List<Integer> getAgentFilter() {
		return agentFilter;
	}

	public List<Short> getServicesFilter() {
		return servicesFilter;
	}
}
