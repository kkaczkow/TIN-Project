package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.BlockingQueue;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import common.events.ClientEvent;
import common.events.ConnectEvent;
import common.events.DisconnectEvent;
import common.events.ListAgentsEvent;
import common.events.ListServicesEvent;
import common.events.RegisterEvent;
import common.events.ServicesEvent;

public class Dialog extends JFrame {
	
	private JButton mConnectBtn;
	private JButton mDisconnectBtn;
	private JButton mListAgentsBtn;
	private JButton mListServicesBtn;
	private JButton mServicesBtn;
	private JButton mRegisterBtn;
	private JButton mConnRequestBtn;
	private JLabel ipv4Label;
	private JLabel ipv6Label;
	private JTextArea ipv4TextBox;
	private JTextArea ipv6TextBox;
	private BlockingQueue<ClientEvent> mBlockingQueue;

    public Dialog(BlockingQueue<ClientEvent> mBlockingQueue) {
    	this.mBlockingQueue = mBlockingQueue;
    	initUI();
	}

	private void initUI() {
        JPanel panel = (JPanel) getContentPane();
        setSize(600, 400);
        setMinimumSize(new Dimension(400, 140));
        
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.setLayout(new GridLayout(3, 4, 10, 10));
        
        initTextBoxes();
        initLabels();
        initButtons();
        setPanel(panel);
        
        pack();
        setTitle("Client GUI");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
	
	private void initTextBoxes() {
		ipv4TextBox = new JTextArea();
		ipv6TextBox = new JTextArea();
		
	}
	
	private void initLabels() {
		ipv4Label = new JLabel();
		ipv6Label = new JLabel();
		ipv4Label.setText("Type ipv4: ");
		ipv6Label.setText("Type ipv6: ");
	}
    
    private void initButtons() {
    	mConnectBtn = new JButton("Connect");
        mConnectBtn.setToolTipText("Connect to the server");
        mConnectBtn.addActionListener(new ActionListener() { 
        	  public void actionPerformed(ActionEvent e) { 
        	    mBlockingQueue.add(new ConnectEvent());
        }});
        
        mDisconnectBtn = new JButton("Disconnect");
        mDisconnectBtn.setToolTipText("Disconnect from the server");
        mDisconnectBtn.addActionListener(new ActionListener() { 
      	  public void actionPerformed(ActionEvent e) { 
      		mBlockingQueue.add(new DisconnectEvent());
      	}});
        
        mListAgentsBtn = new JButton("Get agents list");
        mListAgentsBtn.setToolTipText("Send request to the server for agents list");
        mListAgentsBtn.addActionListener(new ActionListener() { 
        	  public void actionPerformed(ActionEvent e) { 
        		  mBlockingQueue.add(new ListAgentsEvent());
        }});
        
        mListServicesBtn = new JButton("Get services list");
        mListServicesBtn.setToolTipText("Send request to the server for services list");
        mListServicesBtn.addActionListener(new ActionListener() { 
        	  public void actionPerformed(ActionEvent e) { 
        		  mBlockingQueue.add(new ListServicesEvent());
        }});
        
        mServicesBtn = new JButton("Send my services");
        mServicesBtn.setToolTipText("Send my services to the server");
        mServicesBtn.addActionListener(new ActionListener() { 
        	  public void actionPerformed(ActionEvent e) { 
        		  String ipv4 = ipv4TextBox.getText();
        		  String ipv6 = ipv6TextBox.getText();
        		  mBlockingQueue.add(new ServicesEvent(ipv4, ipv6));
        }});
        
        mRegisterBtn = new JButton("Register");
        mRegisterBtn.setToolTipText("Send register request to the server");
        mRegisterBtn.addActionListener(new ActionListener() { 
        	  public void actionPerformed(ActionEvent e) { 
        		  String ipv4 = ipv4TextBox.getText();
        		  String ipv6 = ipv6TextBox.getText();
        		  mBlockingQueue.add(new RegisterEvent(ipv4, ipv6));
        }});
        
        mConnRequestBtn = new JButton("NAT connection");
        mConnRequestBtn.setToolTipText("Connection with another client behind NAT");
        mConnRequestBtn.addActionListener(new ActionListener() { 
        	  public void actionPerformed(ActionEvent e) { 
        		  mBlockingQueue.add(new ListServicesEvent());
        }});
    }
    
    private void setPanel(JPanel panel) {
    	panel.add(mConnectBtn);
    	panel.add(mDisconnectBtn);
        panel.add(mListAgentsBtn);
        panel.add(mListServicesBtn);
        panel.add(ipv4Label);
        panel.add(ipv4TextBox);
        panel.add(ipv6Label);
        panel.add(ipv6TextBox);
        panel.add(mRegisterBtn);
        panel.add(mServicesBtn);
        panel.add(mConnRequestBtn);
    }
}
