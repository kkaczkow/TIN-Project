package test;

import static org.junit.Assert.*;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.junit.BeforeClass;
import org.junit.Test;

import common.events.ClientEvent;
import common.events.ConnectEvent;
import common.events.DisconnectEvent;

import controller.Controller;

public class ClientTest {
	
	private static ServerTestClass mServer;
	private static Controller mController;
	private static BlockingQueue<ClientEvent> mBlockingQueue;
	
	@BeforeClass
	public static void init() {
		mServer = new ServerTestClass();
		mBlockingQueue = new LinkedBlockingQueue<ClientEvent>();
		mController = new Controller(mBlockingQueue);
		mServer.setServerConnectionCounter(0);
	}

	@Test
	public void test() {
		
		// polaczenie serwer- klient
		mServer.start();
		
			mBlockingQueue.add(new ConnectEvent());
			mController.processEvents();
		
		// rozlaczenie
		mBlockingQueue.add(new DisconnectEvent());
		if (mServer != null) {
			mServer.interrupt();
        }
		
		System.out.println("Test passed.");
		//fail("Not yet implemented");
		
	}

}
