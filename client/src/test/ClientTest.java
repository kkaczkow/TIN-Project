package test;

import static org.junit.Assert.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import common.events.ClientEvent;
import common.events.ConnectEvent;
import common.events.DisconnectEvent;
import controller.Controller;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ClientTest {
	private static ServerTestClass mServer;
	private static Controller mController;
	private static BlockingQueue<ClientEvent> mBlockingQueue;
	private static int port = 6000;
	private static final Logger LOGGER = Logger.getLogger(ClientTest.class);
	
	/**
	 * Test class initialization
	 */
	@BeforeClass
	public static void init() {
		LOGGER.info("init():");
		mBlockingQueue = new LinkedBlockingQueue<ClientEvent>();
		mController = new Controller(mBlockingQueue);
		BasicConfigurator.configure();
	}

	/**
	 * Testing connection with a dummy server.
	 */
	@Test
	public void ConnectionWithServerTest() {
		LOGGER.info("ConnectionWithServerTest():");
		
		mServer = new ServerTestClass(++port);
		mServer.setServerConnectionCounter(0);
		
		// polaczenie serwer- klient
		Thread serverThread = new Thread()
		{
		    public void run() {
		    	mServer.runConnectionTest();
		    }
		};
		serverThread.start();
		mController.setPortNumber(port);
		mBlockingQueue.add(new ConnectEvent());
		mController.processEvents();
		
		// rozlaczenie
		mBlockingQueue.add(new DisconnectEvent());
		if (serverThread != null) {
			serverThread.interrupt();
        }
		mController.processEvents();
		assertTrue(true);
		sleep(100);
	}
	
	/**
	 * Testing writing to the server
	 */
	@Test
	public void WriteToServerTest() {
		LOGGER.info("WriteToServerTest():");
		
		mServer = new ServerTestClass(++port);
		mServer.setServerConnectionCounter(0);
		
		// polaczenie serwer- klient
		Thread serverThread = new Thread()
		{
			public void run() {
				mServer.runConnectionTest();
				//mServer.runWriteTest("test message");
			}
		};
		serverThread.start();
		mController.setPortNumber(port);
		mBlockingQueue.add(new ConnectEvent());
		mController.processEvents();
		
		//TODO
		
		// rozlaczenie
		mBlockingQueue.add(new DisconnectEvent());
		if (serverThread != null) {
			serverThread.interrupt();
        }
		mController.processEvents();
		assertTrue(true);
		sleep(100);
	}
	
	/**
	 * Testing reading from the server
	 */
	@Test
	public void ReadFromServerTest() {
		LOGGER.info("ReadFromServerTest():");
		
		mServer = new ServerTestClass(++port);
		mServer.setServerConnectionCounter(0);
		
		// polaczenie serwer- klient
		Thread serverThread = new Thread()
		{
			public void run() {
				mServer.runConnectionTest();
				//mServer.runReadTest();
			}
		};
		serverThread.start();
		mController.setPortNumber(port);
		mBlockingQueue.add(new ConnectEvent());
		mController.processEvents();
				
		//TODO
				
		// rozlaczenie
		mBlockingQueue.add(new DisconnectEvent());
		if (serverThread != null) {
			serverThread.interrupt();
        }
		mController.processEvents();
		assertTrue(true);
		sleep(100);
	}
	
	/**
	 * Unimplemented test
	 */
	@Test
	public void AnotherTest() {
		
		//TODO
		//fail("Not yet implemented");
		assertTrue(true);
		
	}
	
	/**
	 * Clean up method
	 */
	@AfterClass
	public static void cleanup() {
		LOGGER.info("cleanup():");
	}
	
	/**
	 * 
	 */
	private void sleep(final long timeInterval) {
		try {
			Thread.sleep(timeInterval);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
