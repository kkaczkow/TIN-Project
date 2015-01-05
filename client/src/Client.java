import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import model.Agent;
import model.Model;
import common.CommandType;
import common.events.*;
import controller.Controller;
import view.View;


public final class Client {
	
	private static BlockingQueue<ClientEvent> mBlockingQueue = new LinkedBlockingQueue<ClientEvent>();
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		Controller controller;
		Model model = new Model();
		
		// start from commandline
		if(args != null && args.length > 0) {
			
			String command = null;
			for(String elem: args)
			{
				command += elem;
			}
			controller = new Controller(model, mBlockingQueue);
			handleCommand(command);
			
		// start with gui
		} else {
			View view = new View(mBlockingQueue);
			controller = new Controller(model, view, mBlockingQueue);
		}
		controller.go();

	}
	
	public static void handleCommand(final String command) {
		
		switch(CommandType.fromString(command)) {
		
		case LIST_AGENTS:
			mBlockingQueue.add(new ListAgentsEvent());
			break;
			
		case LIST_SERVICES:
			//TODO spr. czy new ArrayList<Agent>() jest ok
			mBlockingQueue.add(new ListServicesEvent(new ArrayList<Agent>(), new String()));
			break;
			
		case CONNECT:
			mBlockingQueue.add(new ConnectEvent());
			break;
			
		case DISCONNECT:
			mBlockingQueue.add(new DisconnectEvent());
			break;
		
		default:
			throw new RuntimeException("unknow command");
		
		}
	}

}
