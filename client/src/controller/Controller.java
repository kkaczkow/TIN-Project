package controller;

import java.util.concurrent.BlockingQueue;

import model.Model;
import view.View;
import common.CommandType;
import common.events.ClientEvent;

public class Controller {
	
	private static Connector mConnector;
	private static Model mModel;
	private static View mView;
	private static String serverName ="localhost";
	private static int port = 666;
	private static BlockingQueue <ClientEvent> mBlockingQueue;
	
	public Controller(Model mModel, BlockingQueue <ClientEvent> mBlockingQueue) {
		mConnector = new Connector();
		Controller.mModel = mModel;
		Controller.mBlockingQueue = mBlockingQueue;
	}
	
	
	public Controller(Model mModel, View mWiew, BlockingQueue <ClientEvent> mBlockingQueue) {
		mConnector = new Connector();
		Controller.mView = mView;
		Controller.mModel = mModel;
		Controller.mBlockingQueue = mBlockingQueue;
	}
	
	public void go() {
        Thread thread = new Thread();
        thread.start();
        while(true) {
            processEvents();
        }
    }
	
	
	public static void processEvents() {
		
		try {
			ClientEvent event = mBlockingQueue.take();
			
			switch(event.getCommand()) {
			case LIST_AGENTS:
				System.out.println("list agents");
				System.out.println(CommandType.LIST_AGENTS.getByteToSend());
				mConnector.sendData(CommandType.LIST_AGENTS.getByteToSend());
				mConnector.getData();
				break;
				
			case LIST_SERVICES:
				System.out.println("list services");
				System.out.println(CommandType.LIST_SERVICES.getByteToSend());
				mConnector.sendData(CommandType.LIST_SERVICES.getByteToSend());
				mConnector.getData();
				break;	
				
			case CONNECT:
				mConnector.connect(serverName, port);
				System.out.println("connect");
				break;
				
			case DISCONNECT:
				mConnector.disconnect();
				System.out.println("disconnect");
				break;
			
			default:
				throw new RuntimeException("unknow command");
			}
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}