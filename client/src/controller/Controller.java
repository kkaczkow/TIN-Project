package controller;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import org.apache.log4j.Logger;
import model.ClientMessageParser;
import model.Model;
import model.Service;
import model.messages.AgentListMessage;
import model.messages.ClientMessage;
import model.messages.ListAgentMessage;
import model.messages.ListServicesMessage;
import model.messages.RegisterMessage;
import model.messages.RegisteredMessage;
import model.messages.ServicesListMessage;
import model.messages.ServicesMessage;
import view.View;
import common.events.AgentListEvent;
import common.events.ClientEvent;
import common.events.ConnRequestEvent;
import common.events.ConnectEvent;
import common.events.DisconnectEvent;
import common.events.ListAgentsEvent;
import common.events.ListServicesEvent;
import common.events.RegisterEvent;
import common.events.RegisteredIdEvent;
import common.events.ServicesEvent;
import common.events.ServicesListEvent;

public class Controller {
	
	private static Map<Class<? extends ClientEvent>, EventHandler> mMapper;
	private static Connector mConnector;
	@SuppressWarnings("unused")
	private static Model mModel;
	private static View mView;
	private static String serverName ="localhost";
	private static int port = 12345;
	private static BlockingQueue <ClientEvent> mBlockingQueue;
	private static final Logger LOGGER = Logger.getLogger(Controller.class);
	
	public Controller(Model mModel, BlockingQueue <ClientEvent> mBlockingQueue) {
		Controller.mMapper= new HashMap<Class<? extends ClientEvent>, EventHandler>();
		Controller.mConnector = new Connector();
		Controller.mModel = mModel;
		Controller.mBlockingQueue = mBlockingQueue;
		setup();
	}
	
	public Controller(Model mModel, View mView, BlockingQueue <ClientEvent> mBlockingQueue) {
		Controller.mMapper= new HashMap<Class<? extends ClientEvent>, EventHandler>();
		Controller.mConnector = new Connector();
		Controller.mView = mView;
		Controller.mModel = mModel;
		Controller.mBlockingQueue = mBlockingQueue;
		setup();
	}
	
	/**
	 * Constructor for testing purpose
	 */
	public Controller(BlockingQueue <ClientEvent> mBlockingQueue) {
		Controller.mConnector = new Connector();
		Controller.mBlockingQueue = mBlockingQueue;
		Controller.mMapper= new HashMap<Class<? extends ClientEvent>, EventHandler>();
		setup();
	}
	
	private void setup() {
		mMapper.put(ConnectEvent.class, new EventHandler() {
			//@Override
			public void handle(ClientEvent event) {
				mConnector.connect(serverName, port);
				if (mConnector.isServerConnected() && mView != null)
					mView.connected();
			}
		});
		
		mMapper.put(DisconnectEvent.class, new EventHandler() {
			
			//@Override
			public void handle(ClientEvent event) {
				mConnector.disconnect();
				if(mView != null)
					mView.disconnected();
			}
		});
		
		mMapper.put(RegisterEvent.class, new EventHandler() {

			//@Override
			public void handle(ClientEvent event) {
				RegisterEvent e = (RegisterEvent)event;

				try {
					List<Inet4Address> ipv4 = ClientMessageParser.parserIPv4(e.getIpV4());
					List<Inet6Address> ipv6 = ClientMessageParser.parserIPv6(e.getIpV6());
					ClientMessage msg = new RegisterMessage(ipv4, ipv6);
					msg.send(mConnector.getSocket().getOutputStream());
					RegisteredMessage response = new RegisteredMessage();
					response.receive(mConnector.getSocket().getInputStream());
					mView.registered(response.getId());
				} 
				catch (UnknownHostException ex) {
					LOGGER.error(ex);
				} catch (IOException ex) {
					LOGGER.error(ex);
				}
			}
		});
		
		mMapper.put(ServicesEvent.class, new EventHandler() {
			
			//@Override
			public void handle(ClientEvent event) {
				ServicesEvent e= (ServicesEvent) event;
				List<Service> serv= ClientMessageParser.parserServices(e.getServicesList());
				ClientMessage msg= new ServicesMessage(serv);
				try {
					msg.send(mConnector.getSocket().getOutputStream());
				} catch (IOException ex) {
					LOGGER.error(ex);
				}
			}
		});
		
		mMapper.put(ListAgentsEvent.class, new EventHandler() {
			
			//@Override
			public void handle(ClientEvent event) {
				@SuppressWarnings("unused")
				ListAgentsEvent e= (ListAgentsEvent) event;
				ListAgentMessage msg= new ListAgentMessage();
				try {
					msg.send(mConnector.getSocket().getOutputStream());
					AgentListMessage response = new AgentListMessage();
					response.receive(mConnector.getSocket().getInputStream());
					mView.showAgentList(response.getAgents());
				} catch (IOException ex) {
					LOGGER.error(ex);
				}
			}
		});
		
		mMapper.put(ListServicesEvent.class, new EventHandler() {
			
			//@Override
			public void handle(ClientEvent event) {
				ListServicesEvent e= (ListServicesEvent) event;
				List<Integer> agentFilter = e.getAgentFilter();
				List<Short> servicesFilter= e.getServicesFilter();
				ListServicesMessage msg= new ListServicesMessage(agentFilter, servicesFilter);
				try {
					msg.send(mConnector.getSocket().getOutputStream());
					ServicesListMessage response = new ServicesListMessage();
					response.receive(mConnector.getSocket().getInputStream());
					mView.showServicesList(response.getServices());
				} catch (IOException ex) {
					LOGGER.error(ex);
				}
			}
		});
		
		mMapper.put(ConnRequestEvent.class, new EventHandler() {
			
			//@Override
			public void handle(ClientEvent event) {
				// TODO Auto-generated method stub
				
			}
		});
		
		mMapper.put(AgentListEvent.class, new EventHandler() {
			
			//@Override
			public void handle(ClientEvent event) {
				// TODO Auto-generated method stub
				
			}
		});
		
		mMapper.put(ServicesListEvent.class, new EventHandler() {
			
			//@Override
			public void handle(ClientEvent event) {
				// TODO Auto-generated method stub
				
			}
		});
		
		mMapper.put(RegisteredIdEvent.class, new EventHandler() {
			
			//@Override
			public void handle(ClientEvent event) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public void go() {
		LOGGER.info("Controller: go()");
        while(true) {
            processEvents();
        }
    }
	
	
	public static void processEvents() {
		LOGGER.info("Controller: processEvents()");
		try {
			ClientEvent event = mBlockingQueue.take();
			if (mMapper.containsKey(event.getClass()))
				mMapper.get(event.getClass()).handle(event);
			else
				; // FIXME unhandled error
		} catch (InterruptedException e) {
			LOGGER.error(e);
		}
	}
	
	/**
	 * For testing purposes
	 * @return
	 */
	public boolean isServerConnected() {
		return mConnector.isServerConnected();
	}
	
	/**
	 * For testing purposes
	 */
	public void setPortNumber(final int port) {
		this.port = port;
	}
	
	/**
	 * For testing purposes
	 */
	public String getData() {
		return mConnector.getData();
	}
	
	/**
	 * For testing purposes
	 */
	public void sendData(final byte message) {
		mConnector.sendData(message);
	}
}