package model;

import java.util.ArrayList;
import java.util.List;

public class Model {
	private List<String> agentsList;
	private List<String> servicesList;
	
	public Model() {
		agentsList = new ArrayList<String>();
		servicesList = new ArrayList<String>();
	}
	
	public List<String> getAgentsList() {
		return agentsList;
	}
	
	public List<String> getServicesList() {
		return servicesList;
	}
	
	public void setAgentsList(List<String> agentsList) {
		this.agentsList = agentsList;
	}
	
	public void setServicesList(List<String> servicesList) {
		this.servicesList = servicesList;
	}

}
