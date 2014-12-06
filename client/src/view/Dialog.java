package view;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.BlockingQueue;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import common.CommandType;
import common.events.ClientEvent;
import common.events.ConnectEvent;
import common.events.DisconnectEvent;
import common.events.ListAgentsEvent;
import common.events.ListServicesEvent;

public class Dialog extends JFrame {
	
	private JButton mConnectBtn;
	private JButton mDisconnectBtn;
	private JButton mListAgentsBtn;
	private JButton mListServicesBtn;
	private BlockingQueue<ClientEvent> mBlockingQueue;

    public Dialog(BlockingQueue<ClientEvent> mBlockingQueue) {
    	this.mBlockingQueue = mBlockingQueue;
    	initUI();
	}

	private void initUI() {
        JPanel panel = (JPanel) getContentPane();
        
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.setLayout(new GridLayout(4, 1, 10, 10));
        
        initButtons(panel);
        
        pack();
        setTitle("Client GUI");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
    private void initButtons(JPanel panel) {
    	mConnectBtn = new JButton("Connect");
        mConnectBtn.setToolTipText("Connect command");
        mConnectBtn.addActionListener(new ActionListener() { 
        	  public void actionPerformed(ActionEvent e) { 
        	    mBlockingQueue.add(new ConnectEvent());
        }});
        
        mDisconnectBtn = new JButton("Disconnect");
        mDisconnectBtn.setToolTipText("Disconnect command");
        mDisconnectBtn.addActionListener(new ActionListener() { 
      	  public void actionPerformed(ActionEvent e) { 
      		mBlockingQueue.add(new DisconnectEvent());
      	}});
        
        mListAgentsBtn = new JButton("List Agents");
        mListAgentsBtn.setToolTipText("List agents command");
        mListAgentsBtn.addActionListener(new ActionListener() { 
        	  public void actionPerformed(ActionEvent e) { 
        		  mBlockingQueue.add(new ListAgentsEvent());
        }});
        
        mListServicesBtn = new JButton("List Services");
        mListServicesBtn.setToolTipText("List services command");
        mListServicesBtn.addActionListener(new ActionListener() { 
        	  public void actionPerformed(ActionEvent e) { 
        		  mBlockingQueue.add(new ListServicesEvent());
        }});
        
        panel.add(mConnectBtn);
        panel.add(mListAgentsBtn);
        panel.add(mListServicesBtn);
        panel.add(mDisconnectBtn);
    }
}
