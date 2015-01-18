package model.messages;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import common.CommandType;

public class RegisteredMessage extends ServerMessage {

	private int id;

	public RegisteredMessage() {
		super(CommandType.REGISTERED.getByteToSend());
	}
	
	public int getId() {
		return id;
	}

	@Override
	public void receive(InputStream istream) throws IOException {
		DataInputStream distream = new DataInputStream(istream);
		distream.readByte();
		id = distream.readInt();
	}

}
