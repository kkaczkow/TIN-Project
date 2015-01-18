import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import model.Model;
import common.CommandType;
import common.events.*;
import controller.Controller;
import view.View;


public final class Client {
	
	private static BlockingQueue<ClientEvent> mBlockingQueue = new LinkedBlockingQueue<ClientEvent>();
	private static Controller mController;
	private static View mView;
	private static Model mModel;
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		mModel = new Model();
		
		// start from commandline
		if(args != null && args.length > 0) {
			
			String command = null;
			for(String elem: args)
			{
				command += elem;
			}
			mController = new Controller(mModel, mBlockingQueue);
			handleCommand(command);
			
		// start with gui
		} else {
			mView = new View(mBlockingQueue);
			mController = new Controller(mModel, mView, mBlockingQueue);
		}
		mController.go();

	}
	
	public static void handleCommand(final String command) {
		
		switch(CommandType.fromString(command)) {
		
		case LIST_AGENTS:
			mBlockingQueue.add(new ListAgentsEvent());
			break;
			
		case LIST_SERVICES:
			//TODO spr. czy new ArrayList<Agent>() jest ok
			mBlockingQueue.add(new ListServicesEvent(new ArrayList<Integer>(), new ArrayList<Short>()));
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
