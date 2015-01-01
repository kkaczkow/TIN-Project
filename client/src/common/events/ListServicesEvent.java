package common.events;

import java.util.List;

import model.Agent;
import common.CommandType;

public class ListServicesEvent extends ClientEvent {

	private List<Agent> agentList;
	private String servicesList;
	
	public ListServicesEvent(List<Agent> ag, String serv) {
		super(CommandType.LIST_SERVICES);
		setAgentList(ag);
		setServicesList(serv);
	}

	public String getServicesList() {
		return servicesList;
	}

	public void setServicesList(String servicesList) {
		this.servicesList = servicesList;
	}

	public List<Agent> getAgentList() {
		return agentList;
	}

	public void setAgentList(List<Agent> agentList) {
		this.agentList = agentList;
	}
}
