package view;

import java.util.concurrent.BlockingQueue;

import common.events.ClientEvent;

public class View{
	
	private Dialog mDialog;
	
	public View(BlockingQueue <ClientEvent> mBlockingQueue) {
		mDialog = new Dialog(mBlockingQueue);
		mDialog.setVisible(true);
	}

}
