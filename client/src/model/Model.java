package model;

import java.util.ArrayList;
import java.util.List;

public class Model {
	private int ID;
	private List<Agent> agentsList;
	private List<Service> servicesList;
	
	public Model() {
		agentsList = new ArrayList<Agent>();
		servicesList = new ArrayList<Service>();
	}
	
	public List<Agent> getAgentsList() {
		return agentsList;
	}
	
	public List<Service> getServicesList() {
		return servicesList;
	}
	
	public void setAgentsList(List<Agent> agentsList) {
		this.agentsList = agentsList;
	}
	
	public void setServicesList(List<Service> servicesList) {
		this.servicesList = servicesList;
	}
	
	
	
	
}
