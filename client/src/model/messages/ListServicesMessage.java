package model.messages;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import model.Agent;
import model.Service;
import common.CommandType;

public class ListServicesMessage extends ClientMessage {

	List<Agent> agents;
	List<Service> services;
	
	public ListServicesMessage(List<Agent> ag, List<Service> serv) {
		super(CommandType.LIST_SERVICES.getByteToSend());
		agents= ag;
		services= serv;
	}

	@Override
	public void send(OutputStream ostream) throws IOException {
		DataOutputStream dostream = new DataOutputStream(ostream);
		dostream.writeByte(type);
		for (Agent agent : agents) {
			dostream.writeInt(agent.getID());
		}
		dostream.writeByte(0);
		
		for (Service service : services) {
			dostream.writeShort(service.getId());
		}
		dostream.writeByte(0);
	}

}
