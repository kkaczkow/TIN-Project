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
import org.apache.log4j.Logger;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ClientTest {
	private static ServerTestClass mServer;
	private static Controller mController;
	private static BlockingQueue<ClientEvent> mBlockingQueue;
	private static int port = 12345;
	private static final Logger LOGGER = Logger.getLogger(ClientTest.class);
	
	/**
	 * Test class initialization
	 */
	@BeforeClass
	public static void init() {
		LOGGER.info("init():");
		mBlockingQueue = new LinkedBlockingQueue<ClientEvent>();
		mController = new Controller(mBlockingQueue);
	}

	/**
	 * Testing connection with a dummy server.
	 */
	@Test
	public void ConnectionWithServerTest() {
		LOGGER.info("ConnectionWithServerTest():");
		
		mServer = new ServerTestClass(++port);
		mServer.setServerConnectionCounter(0);
		
		// connect
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
		
		// disconnect
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
		final String messageToSend = "test message";
		
		mServer = new ServerTestClass(++port);
		mServer.setServerConnectionCounter(0);
		
		// connect and send a message
		Thread serverThread = new Thread()
		{
			public void run() {
				int i = 0;
				while (i == 0) {
					mServer.runWriteTest(messageToSend);
					// check if sent and received message are the same
					assertEquals(messageToSend, mController.getData());
					++i;
				}
			}
		};
		serverThread.start();
		mController.setPortNumber(port);
		mBlockingQueue.add(new ConnectEvent());
		mController.processEvents();
		sleep(100);
		
		// disconnect
		mBlockingQueue.add(new DisconnectEvent());
		if (serverThread != null) {
			serverThread.interrupt();
        }
		mController.processEvents();
		sleep(100);
	}
	
	/**
	 * Testing reading from the server
	 */
	@Test
	public void ReadFromServerTest() {
		LOGGER.info("ReadFromServerTest():");
		final byte messageToSend = 4;
		
		mServer = new ServerTestClass(++port);
		mServer.setServerConnectionCounter(0);
		
		// connect and receive the message
		Thread serverThread = new Thread()
		{
			public void run() {
				int i = 0;
				while (i == 0) {
					mController.setPortNumber(port);
					mBlockingQueue.add(new ConnectEvent());
					mController.processEvents();
					mController.sendData(messageToSend);
					++i;
				}
					
					
			}
		};
		serverThread.start();
		assertEquals(Byte.toString(messageToSend), mServer.runReadTest());
		sleep(100);	
		
		// disconnect
		mBlockingQueue.add(new DisconnectEvent());
		if (serverThread != null) {
			serverThread.interrupt();
        }
		mController.processEvents();
		assertTrue(true);
		sleep(100);
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
			LOGGER.error(e);
		}
	}

}
