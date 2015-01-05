package model.messages;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import model.Service;
import common.CommandType;

public class ServicesMessage extends ClientMessage {
	
	List<Service> servicesList;

	public ServicesMessage(List<Service> list) {
		super(CommandType.SERVICES.getByteToSend());
		servicesList= list;
	}

	@Override
	public void send(OutputStream ostream) throws IOException {
		DataOutputStream dostream = new DataOutputStream(ostream);
		dostream.writeByte(type);
		for (Service service : servicesList) {
			dostream.writeByte(service.getFlags());
			dostream.writeShort(service.getId());
			dostream.writeShort(service.getPort());
		}
		dostream.writeByte(0);
		dostream.close();
	}
}
