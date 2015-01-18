package model.messages;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Service;
import common.CommandType;

public class ServicesListMessage extends ServerMessage {

	private final Map<Integer, List<Service> > services = new HashMap<Integer, List<Service> >();

	public ServicesListMessage() {
		super(CommandType.SERVICES_LIST.getByteToSend());
	}
	
	public Map<Integer, List<Service>> getServices() {
		return services;
	}

	@Override
	public void receive(InputStream istream) throws IOException {
		DataInputStream distream = new DataInputStream(istream);
		distream.readByte();
		int aid = distream.readInt();
		while (aid != 0)
		{
			List<Service> list = new ArrayList<Service>();
			services.put(aid, list);
			// 0 end of list
			byte flag = distream.readByte();
			while (flag != 0)
			{
				short port = distream.readShort();
				short sid = distream.readShort();
				list.add(new Service(flag, port, sid));
				flag = distream.readByte();
			}
			aid = distream.readInt();
		}
	}


}
