package model.messages;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import common.CommandType;

public class ListServicesMessage extends ClientMessage {

	List<Integer> agentsFilter;
	List<Short> servicesFilter;
	
	public ListServicesMessage(List<Integer> agentsFilter, List<Short> servicesFilter) {
		super(CommandType.LIST_SERVICES.getByteToSend());
		this.agentsFilter= agentsFilter;
		this.servicesFilter= servicesFilter;
	}

	@Override
	public void send(OutputStream ostream) throws IOException {
		DataOutputStream dostream = new DataOutputStream(ostream);
		dostream.writeByte(type);
		for (Integer agent : agentsFilter) {
			dostream.writeInt(agent);
		}
		dostream.writeInt(0);
		for (Short service : servicesFilter) {
			dostream.writeShort(service);
		}
		dostream.writeShort(0);
		dostream.flush();
	}

}
