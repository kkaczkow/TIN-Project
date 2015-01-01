package model.messages;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.util.List;

import common.CommandType;

public class RegisterMessage extends ClientMessage {
	List<Inet4Address> IPv4;
	List<Inet6Address> IPv6;
	
	public RegisterMessage(List<Inet4Address> ipv4, List<Inet6Address> ipv6) {
		super(CommandType.REGISTER.getByteToSend());
		IPv4 = ipv4;
		IPv6 = ipv6;
	}

	@Override
	public void send(OutputStream ostream) throws IOException {
		DataOutputStream dostream = new DataOutputStream(ostream);
		dostream.writeByte(type);
		for (Inet4Address ipv4 : IPv4)
			dostream.write(ipv4.getAddress());
		dostream.write(new byte[4]);
		for (Inet6Address ipv6 : IPv6)
			dostream.write(ipv6.getAddress());
		dostream.write(new byte[16]);
	}
}
