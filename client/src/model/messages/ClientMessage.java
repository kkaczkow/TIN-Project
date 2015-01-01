package model.messages;

import java.io.IOException;
import java.io.OutputStream;

public abstract class ClientMessage {
	protected final byte type;
	
	ClientMessage(final byte type) {
		this.type = type;
	}
	public abstract void send(OutputStream ostream) throws IOException;
}
