package model.messages;

import java.io.IOException;
import java.io.InputStream;

public abstract class ServerMessage {
	protected final byte type;
	
	ServerMessage(final byte type) {
		this.type = type;
	}
	public abstract void receive(InputStream istream) throws IOException;
}
