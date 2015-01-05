package model.messages;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import common.CommandType;

public class ListAgentMessage extends ClientMessage {

	public ListAgentMessage() {
		super(CommandType.LIST_AGENTS.getByteToSend());
	}

	@Override
	public void send(OutputStream ostream) throws IOException {
		DataOutputStream dostream = new DataOutputStream(ostream);
		dostream.writeByte(type);
		dostream.close();
	}

}
