package com.lmco.sqq89;

import java.awt.Rectangle;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;

/**
 * Main MACGUI display class 
 * 
 * @author Justin Bennett
 * December 1, 2010
 */
public class MacGui extends JFrame {

	private static final long serialVersionUID = 1L;
	private DhcpdConf d = null;
	private LinkedList<NodeDisplayGroup> nodes;
	private HashMap<String,JScrollPane> panes;
	
	private JPanel jContentPane = null;
	private JButton saveButton = null;
	private JButton distButton = null;
	private JButton restartDHCPButton = null;
	private JMenuBar mbar = null;
	private JMenu fileMenu = null;
	private JMenuItem closeMenuItem = null;
	private JTabbedPane macTabbedPane = null;
	/**
	 * This method initializes saveButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getSaveButton() {
		if (saveButton == null) {
			saveButton = new JButton();
			saveButton.setBounds(new Rectangle(14, 420, 111, 26));
			saveButton.setText("Save");
			saveButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					// Try to write out the same dhcpd.conf file that was opened
					saveChanges();
					d.writeDhcpdConf();
					
					// Change all backgrounds back to white
					// TODO
				}
			});
		}
		return saveButton;
	}

	/**
	 * This method initializes distButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getDistButton() {
		if (distButton == null) {
			distButton = new JButton();
			distButton.setBounds(new Rectangle(139, 420, 111, 26));
			distButton.setText("Distribute");
			distButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					// Save dhcpd.conf file and then sync changes across system
					saveChanges();
					d.writeDhcpdConf();
					d.syncDhcpdConf();
				}
			});
		}
		return distButton;
	}

	/**
	 * This method initializes restartDHCPButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getRestartDHCPButton() {
		if (restartDHCPButton == null) {
			restartDHCPButton = new JButton();
			restartDHCPButton.setBounds(new Rectangle(264, 420, 111, 26));
			restartDHCPButton.setText("Restart DHCP");
		}
		return restartDHCPButton;
	}

	/**
	 * This method initializes mbar	
	 * 	
	 * @return javax.swing.JMenuBar	
	 */
	private JMenuBar getMbar() {
		if (mbar == null) {
			mbar = new JMenuBar();
			mbar.add(getFileMenu());
		}
		return mbar;
	}

	/**
	 * This method initializes fileMenu	
	 * 	
	 * @return javax.swing.JMenu	
	 */
	private JMenu getFileMenu() {
		if (fileMenu == null) {
			fileMenu = new JMenu();
			fileMenu.setText("File");
			fileMenu.add(getCloseMenuItem());
		}
		return fileMenu;
	}

	/**
	 * This method initializes closeMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getCloseMenuItem() {
		if (closeMenuItem == null) {
			closeMenuItem = new JMenuItem();
			closeMenuItem.setText("Close");
		}
		return closeMenuItem;
	}

	/**
	 * This method initializes macTabbedPane	
	 * 	
	 * @return javax.swing.JTabbedPane	
	 */
	private JTabbedPane getMacTabbedPane() {
		if (macTabbedPane == null) {
			macTabbedPane = new JTabbedPane();
			macTabbedPane.setBounds(new Rectangle(0, 0, 391, 406));
		}
		return macTabbedPane;
	}

	/**
	 * This is the default constructor
	 */
	public MacGui(String dhcpdFilename) {
		super();
		initialize();
		
		panes = new HashMap<String,JScrollPane>();
		nodes = new LinkedList<NodeDisplayGroup>();
		
		// Hold current X, Y position
		int currX;
		int currY;
		
		// Parse dhcpd.conf file
		d = new DhcpdConf(new File(dhcpdFilename));
		
		for (Host h : d.getHosts()) {
			// Check for existence of correct tab and create one if there isn't
			String currCabinet = h.getHostname().substring(1,4);
			if (!panes.containsKey(currCabinet)) {
				panes.put(currCabinet, new JScrollPane(new JViewport(), 
						JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
						JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
				panes.get(currCabinet).getViewport().setView(new CabinetPanel());
				((CabinetPanel) panes.get(currCabinet).getViewport().getView()).setLayout(null);
				getMacTabbedPane().addTab(currCabinet, panes.get(currCabinet));
			}
			
			// Set currX and currY
			currX = 120;
			currY = ((CabinetPanel) panes.get(currCabinet).getViewport().getView()).getCurrY();
			
			// Add new NodeDisplayGroup to list
			NodeDisplayGroup newNode = new NodeDisplayGroup(h);
			nodes.add(newNode);
			
			// Add label for hostname to GUI
			newNode.getNodeNameLabel().setSize(49,16);
			newNode.getNodeNameLabel().setLocation(15,currY);
			((CabinetPanel) panes.get(currCabinet).getViewport().getView()).add(newNode.getNodeNameLabel());			
			
			// Cycle through eth interfaces and display MAC addresses
			for (int i = 0; i < h.getEths().length; i++) {
				// Add label for eth to GUI
				newNode.getMacs()[i].getEthLabel().setSize(25,16);
				newNode.getMacs()[i].getEthLabel().setLocation(75, currY);
				((CabinetPanel) panes.get(currCabinet).getViewport().getView()).add(newNode.getMacs()[i].getEthLabel());
				
				// Add JTextFields for MAC address octets
				for (int j = 0; j < 6; j++) {
					newNode.getMacs()[i].getMacFields()[j].setSize(31,16);
					newNode.getMacs()[i].getMacFields()[j].setLocation(currX,currY);
					newNode.getMacs()[i].getMacFields()[j].setHorizontalAlignment(JTextField.CENTER);
					((CabinetPanel) panes.get(currCabinet).getViewport().getView()).add(newNode.getMacs()[i].getMacFields()[j]);
					
					// Add listener to change background color for unsaved changes
					/*macFields[j].addKeyListener(new KeyListener() {
						public void keyTyped(KeyEvent e) {
							((JTextField) e.getSource()).setBackground(new Color(255, 157, 157));
						}
						public void keyPressed(KeyEvent e) {}
						public void keyReleased(KeyEvent e) {}
					});*/
					//this.getJPanel().add(macFields[j]);
					currX += 35;
				}
				
				currX = 120;
				currY += 17;
			}
			
			// Store Y-value
			((CabinetPanel) panes.get(currCabinet).getViewport().getView()).setCurrY(currY + 5);
		}
	}

	/**
	 * This method initializes MacGui
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(399, 519);
		this.setJMenuBar(getMbar());
		this.setContentPane(getJContentPane());
		try {
			this.setTitle("MAC GUI (Running on " + InetAddress.getLocalHost().getHostName() + ")");
		} catch(UnknownHostException uhe) {
			System.err.println("UnknownHostException: " + uhe.getMessage());
		}
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getSaveButton(), null);
			jContentPane.add(getDistButton(), null);
			jContentPane.add(getRestartDHCPButton(), null);
			jContentPane.add(getMacTabbedPane(), null);
		}
		return jContentPane;
	}
	
	/**
	 * This method reads data from GUI and commits changes to current instance of dhcpdConf.
	 */
	private void saveChanges() {
		// TODO: Logic to read changed data from GUI back into dhcpdConf instance
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {		
		// Start GUI
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MacGui mg = new MacGui("dhcpd.conf");
				mg.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				mg.setVisible(true);
			}
		});
	}

}  //  @jve:decl-index=0:visual-constraint="53,11"
