package view;

import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import javax.swing.SwingUtilities;

import model.Agent;
import model.Service;
import common.events.ClientEvent;

public class View{
	
	private Dialog mDialog;
	
	public View(BlockingQueue <ClientEvent> mBlockingQueue) {
		mDialog = new Dialog(mBlockingQueue);
		mDialog.setVisible(true);
	}
	
	public void connected() {
		SwingUtilities.invokeLater(new Runnable() {
			//@Override
			public void run() {
				mDialog.connected();
			}
		});
	}
	
	public void disconnected() {
		SwingUtilities.invokeLater(new Runnable() {
			//@Override
			public void run() {
				mDialog.disconnected();
			}
		});
	}
	
	public void registered(final int id) {
		SwingUtilities.invokeLater(new Runnable() {
			//@Override
			public void run() {
				mDialog.registered(id);
			}
		});
	}
	
	public void showAgentList(final List<Agent> list) {
		SwingUtilities.invokeLater(new Runnable() {
			//@Override
			public void run() {
				mDialog.showAgentList(list);
			}
		});
	}
	
	public void showServicesList(final Map<Integer, List<Service>> map) {
		SwingUtilities.invokeLater(new Runnable() {
			//@Override
			public void run() {
				mDialog.showServices(map);
			}
		});
	}
}
