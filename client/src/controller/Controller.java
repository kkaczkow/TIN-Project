package controller;

import java.util.concurrent.BlockingQueue;
import model.Model;
import view.View;
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
		this.mModel = mModel;
		this.mBlockingQueue = mBlockingQueue;
	}
	
	
	public Controller(Model mModel, View mWiew, BlockingQueue <ClientEvent> mBlockingQueue) {
		mConnector = new Connector();
		this.mView = mView;
		this.mModel = mModel;
		this.mBlockingQueue = mBlockingQueue;
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
				mConnector.sendData("list agents");
				mConnector.getData();
				System.out.println("list agents");
				break;
				
			case LIST_SERVICES:
				mConnector.sendData("list services");
				mConnector.getData();
				System.out.println("list services");
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