package model.messages;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import common.CommandType;

public class ConnRequestMessage extends ClientMessage {

	byte flags;
	int agentID;
	int port;
	
	ConnRequestMessage(byte fl, int id, int po) {
		super(CommandType.CONN_REQUEST.getByteToSend());
		flags= fl;
		agentID= id;
		port= po;
	}

	@Override
	public void send(OutputStream ostream) throws IOException {
		DataOutputStream dostream = new DataOutputStream(ostream);
		dostream.writeByte(type);
		dostream.writeByte(flags);
		dostream.writeInt(agentID);
		dostream.writeShort(port);
		dostream.flush();
	}

}
