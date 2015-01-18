package view;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import model.Agent;
import model.Service;
import common.events.ClientEvent;
import common.events.ConnectEvent;
import common.events.DisconnectEvent;
import common.events.ListAgentsEvent;
import common.events.ListServicesEvent;
import common.events.RegisterEvent;
import common.events.ServicesEvent;

public class Dialog extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private JLabel mStatusLabel;
	private JButton mConnectBtn;
	private JButton mDisconnectBtn;
	private JButton mListAgentsBtn;
	private JButton mListServicesBtn;
	private JButton mServicesBtn;
	private JButton mRegisterBtn;
	private JButton mConnRequestBtn;
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
        panel.setLayout(new GridLayout(0, 1, 10, 10));
        
        initLabels();
        initButtons();
        setPanel(panel);
        showButtons(false, false);
        
        pack();
        setTitle("Client GUI");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
	
	private void initLabels() {
		mStatusLabel = new JLabel("Disconnected");
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
        		  String agentFilter = JOptionPane.showInputDialog("Agent filter");
        		  String serviceFilter = JOptionPane.showInputDialog("Service filter");
        		  mBlockingQueue.add(new ListServicesEvent(agentFilter, serviceFilter));
        	  }
        });
        
        mServicesBtn = new JButton("Send my services");
        mServicesBtn.setToolTipText("Send my services to the server");
        mServicesBtn.addActionListener(new ActionListener() { 
        	  public void actionPerformed(ActionEvent e) { 
        		  String services = JOptionPane.showInputDialog("Services list");
        		  mBlockingQueue.add(new ServicesEvent(services));
        	  }
        });
        
        mRegisterBtn = new JButton("Register");
        mRegisterBtn.setToolTipText("Send register request to the server");
		mRegisterBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String ipv4 = JOptionPane.showInputDialog("IP v4 list");
				String ipv6 = JOptionPane.showInputDialog("IP v6 list");
				mBlockingQueue.add(new RegisterEvent(ipv4, ipv6));
			}
		});
        
        mConnRequestBtn = new JButton("NAT connection");
        mConnRequestBtn.setToolTipText("Connection with another client behind NAT");
		mConnRequestBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});
    }
    
    private void showButtons(boolean connected, boolean registered) {
		mConnectBtn.setVisible(!connected);
		mDisconnectBtn.setVisible(connected);
		mListAgentsBtn.setVisible(connected);
		mListServicesBtn.setVisible(connected);
		mRegisterBtn.setVisible(connected&&!registered);
		mServicesBtn.setVisible(connected&&registered);
		mConnRequestBtn.setVisible(connected&&registered);
	}	
    
    void connected() {
    	showButtons(true, false);
    	mStatusLabel.setText("Connected");
    }
    
    void disconnected() {
		showButtons(false, false);
		mStatusLabel.setText("Connected");
    }
    
    void registered(final int id) {
    	showButtons(true, true);
    	mStatusLabel.setText("Registered with id: "+Integer.toString(id));
    }
    
    public void unregistered() {
    	showButtons(true, false);
    	mStatusLabel.setText("Connected");
	}
	
	public void showAgentList(List<Agent> list) {
		StringBuilder sb = new StringBuilder();
		for (Agent agent : list) {
			sb.append("Agent #");
			sb.append(agent.getID());
			sb.append('\n');
			for (Inet4Address addr : agent.getIPv4()) {
				sb.append('\t');
				sb.append(addr.getHostAddress());
				sb.append('\n');
			}
			for (Inet6Address addr : agent.getIPv6()) {
				sb.append('\t');
				sb.append(addr.getHostAddress());
				sb.append('\n');
			}
		}
		JDialog dialog = new JDialog(this, "Agent list");
		JPanel panel = (JPanel)dialog.getContentPane();
		JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setText(sb.toString());
		panel.add(textArea);
		dialog.setSize(300, 500);
		dialog.setVisible(true);
	}
	
	public void showServices(Map<Integer, List<Service>> services) {
		StringBuilder sb = new StringBuilder();
		for (Entry<Integer, List<Service>> entry : services.entrySet()) {
			sb.append("Agent #");
			sb.append(entry.getKey());
			sb.append('\n');
			for (Service service : entry.getValue()) {
				sb.append('\t');
				sb.append(service.toString());
				sb.append('\n');
			}
		}
		JDialog dialog = new JDialog(this, "Services list");
		JPanel panel = (JPanel)dialog.getContentPane();
		JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setText(sb.toString());
		panel.add(textArea);
		dialog.setSize(300, 500);
		dialog.setVisible(true);
	}
    
    private void setPanel(JPanel panel) {
    	panel.add(mStatusLabel);
    	panel.add(mConnectBtn);
    	panel.add(mDisconnectBtn);
        panel.add(mListAgentsBtn);
        panel.add(mListServicesBtn);
        panel.add(mRegisterBtn);
        panel.add(mServicesBtn);
        //panel.add(mConnRequestBtn);
    }
}
