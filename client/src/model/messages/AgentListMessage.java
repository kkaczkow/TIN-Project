package model.messages;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.util.ArrayList;
import java.util.List;

import model.Agent;
import common.CommandType;

public class AgentListMessage extends ServerMessage {

	private final List<Agent> agents = new ArrayList<Agent>();
	
	public AgentListMessage() {
		super(CommandType.AGENT_LIST.getByteToSend());
	}
	
	public List<Agent> getAgents() {
		return agents;
	}

	private static boolean checkIsZero(final byte[] array) {
        for (byte b : array) {
            if (b != 0) {
                return false;
            }
        }
        return true;
    }

	@Override
	public void receive(InputStream istream) throws IOException {
		DataInputStream distream = new DataInputStream(istream);
		distream.readByte();
		int aid = distream.readInt();
		while (aid != 0)
		{
			byte[] addr = new byte[4];
			List<Inet4Address> ipsv4 = new ArrayList<Inet4Address>();
			distream.readFully(addr);
			while (!checkIsZero(addr))
			{
				Inet4Address ipv4 = (Inet4Address) Inet4Address.getByAddress(addr);
				ipsv4.add(ipv4);
				distream.readFully(addr);
			}
			addr = new byte[16];
			List<Inet6Address> ipsv6 = new ArrayList<Inet6Address>();
			distream.readFully(addr);
			while (!checkIsZero(addr))
			{
				Inet6Address ipv6 = (Inet6Address) Inet6Address.getByAddress(addr);
				ipsv6.add(ipv6);
				distream.readFully(addr);
			}
			agents.add(new Agent(aid, ipsv4, ipsv6));
			aid = distream.readInt();
		}
	}

}
