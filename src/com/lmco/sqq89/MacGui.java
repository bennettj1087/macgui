package com.lmco.sqq89;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 * Main MACGUI display class. This class was created using the Eclipse Visual
 * Editor Project to generate a portion of the static GUI code.  However, the
 * majority of the GUI is generated dynamically based on the contents of the files
 * being parsed.
 * 
 * This program is heavily biased towards a dhcpd.conf and ethers file formatted
 * for the SQQ89 system (particularly ACB09 and ACB11).  If the file does not match
 * a certain format, the program will complain.  Over time, I hope to build in some
 * intelligence to parse things that may be formatted differently.
 * 
 * @author Justin Bennett
 * December 1, 2010
 */
public class MacGui extends JFrame {
	private static final long serialVersionUID = 1L;
	private DhcpdConf d = null;  //  @jve:decl-index=0:
	private Ethers e = null;  //  @jve:decl-index=0:
	private LinkedList<NodeDisplayGroup> nodes;  //  @jve:decl-index=0:
	private HashMap<String,JScrollPane> panes;  //  @jve:decl-index=0:
	
	// Auto-generated GUI elements from VEP
	private JPanel jContentPane = null;
	private JButton saveButton = null;
	private JButton distButton = null;
	private JButton restartDHCPButton = null;
	private JMenuBar mbar = null;
	private JMenu fileMenu = null;
	private JMenuItem closeMenuItem = null;
	private JTabbedPane macTabbedPane = null;
	private JMenuItem openMenuItem = null;
	private JMenu helpMenu = null;
	private JMenuItem aboutMenuItem = null;
	private JLabel dhcpdFileLabel = null;
	private JLabel ethersFileLabel = null;
	
	/**
	 * This method initializes saveButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getSaveButton() {
		if (saveButton == null) {
			saveButton = new JButton();
			saveButton.setBounds(new Rectangle(15, 455, 111, 26));
			saveButton.setText("Save");
			saveButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent ae) {
					// Try to write out the same dhcpd.conf file that was opened
					saveChanges();
					d.writeDhcpdConf();
					e.writeEthers();
					
					// Change all backgrounds back to white
					for (NodeDisplayGroup node : nodes) {
						for (int i = 0; i < node.getMacs().length; i++)
							if (node.getMacs()[i] != null)
								for (int j = 0; j < 6; j++)
									node.getMacs()[i].getMacFields()[j].setBackground(Color.WHITE);
					}
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
			distButton.setBounds(new Rectangle(141, 455, 111, 26));
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
			restartDHCPButton.setBounds(new Rectangle(267, 455, 111, 26));
			restartDHCPButton.setText("Restart DHCP");
		}
		return restartDHCPButton;
	}

	/**
	 * This is the default constructor
	 */
	public MacGui(String dhcpdFilename, String ethersFilename) {
		super();
		initialize(dhcpdFilename, ethersFilename);		
	}
	
	/**
	 * This method clears the existing data structures and calls the initialize method
	 * to re-populate the GUI.
	 * 
	 * @param dhcpdFilename
	 * @param ethersFilename
	 */
	public void reinitialize(String dhcpdFilename, String ethersFilename) {
		panes.clear();
		nodes.clear();
		getMacTabbedPane().removeAll();
		initialize(dhcpdFilename, ethersFilename);
	}
	
	/**
	 * This method clears the existing data structures and calls the initialize method
	 * to re-populate the GUI.
	 * 
	 * @param dhcpdFilename
	 */
	public void reinitialize(String dhcpdFilename) {
		panes.clear();
		nodes.clear();
		getMacTabbedPane().removeAll();
		initialize(dhcpdFilename, e.getFileName());
	}

	/**
	 * This method initializes MacGui
	 * 
	 * @return void
	 */
	private void initialize(String dhcpdFilename, String ethersFilename) {
		this.setSize(399, 546);
		this.setResizable(false);
		this.setJMenuBar(getMbar());
		this.setContentPane(getJContentPane());
		try {
			this.setTitle("MAC GUI (Running on " + InetAddress.getLocalHost().getHostName() + ")");
		} catch(UnknownHostException uhe) {
			System.err.println("UnknownHostException: " + uhe.getMessage());
			this.setTitle("MAC GUI");
		}
		
		this.setSize(((int) this.getMacTabbedPane().getSize().getWidth())+10, 546);
		
		panes = new HashMap<String,JScrollPane>();
		nodes = new LinkedList<NodeDisplayGroup>();
		
		// Parse dhcpd.conf file
		d = new DhcpdConf(new File(dhcpdFilename));
		e = new Ethers(new File(ethersFilename));
		dhcpdFileLabel.setText("Current dhcpd file: " + dhcpdFilename);
		ethersFileLabel.setText("Current ethers file: " + ethersFilename);
		
		// Add hosts from each of dhcpd.conf and ethers files
		addHosts(d.getHosts());
		addHosts(e.getHosts());
	}
	
	/**
	 * Adds hosts to GUI from a linked list of hosts provided by either an Ethers
	 * object or a DhcpdConf object.
	 * 
	 * @param hosts
	 */
	public void addHosts(LinkedList<Host> hosts) {
		// Hold current X, Y position
		int currX;
		int currY;
		
		for (Host h : hosts) {
			// Check for existence of correct tab and create one if there isn't
			String currCabinet = h.getHostname().substring(1,4);
			if (!panes.containsKey(currCabinet)) {
				panes.put(currCabinet, new JScrollPane(new JViewport(), 
						JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
						JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
				panes.get(currCabinet).getViewport().setView(new CabinetPanel());
				((CabinetPanel) panes.get(currCabinet).getViewport().getView()).setLayout(null);
				((CabinetPanel) panes.get(currCabinet).getViewport().getView()).setPreferredSize(new Dimension(350,375));
				getMacTabbedPane().addTab(currCabinet, panes.get(currCabinet));
			}
			
			// Set currX and currY
			currX = 120;
			currY = ((CabinetPanel) panes.get(currCabinet).getViewport().getView()).getCurrY();
			
			// Create node display group
			NodeDisplayGroup newNode = new NodeDisplayGroup(h);
			nodes.add(newNode);
			
			// Add label for hostname to GUI
			newNode.getNodeNameLabel().setSize(59,16);
			newNode.getNodeNameLabel().setLocation(10,currY);
			((CabinetPanel) panes.get(currCabinet).getViewport().getView()).add(newNode.getNodeNameLabel());		
			
			// Cycle through eth interfaces and display MAC addresses
			for (int i = 0; i < h.getEths().length; i++) {
				// Add label for eth to GUI
				if (newNode.getMacs()[i] != null) {
					newNode.getMacs()[i].getEthLabel().setSize(25,16);
					newNode.getMacs()[i].getEthLabel().setLocation(75, currY);
					((CabinetPanel) panes.get(currCabinet).getViewport().getView()).add(newNode.getMacs()[i].getEthLabel());
				
					// Add JTextFields for MAC address octets
					for (int j = 0; j < 6; j++) {
						newNode.getMacs()[i].getMacFields()[j].setSize(31,16);
						newNode.getMacs()[i].getMacFields()[j].setLocation(currX,currY);
						newNode.getMacs()[i].getMacFields()[j].setHorizontalAlignment(JTextField.CENTER);
						((CabinetPanel) panes.get(currCabinet).getViewport().getView()).add(newNode.getMacs()[i].getMacFields()[j]);
						currX += 35;
					}
				
					// Reset x position, up Y position
					currX = 120;
					currY += 17;
				}
			}
			
			// Store Y-value
			currY+=5;
			((CabinetPanel) panes.get(currCabinet).getViewport().getView()).setCurrY(currY);
			
			// Update preferred size of JPanel to match currY value
			((CabinetPanel) panes.get(currCabinet).getViewport().getView()).setPreferredSize(new Dimension(350,currY));	
		}
	}

	
	/**
	 * This method reads data from GUI and commits changes to current instance of dhcpdConf.
	 */
	private void saveChanges() {
		// Iterate over nodes in LinkedList
		for (NodeDisplayGroup node : nodes) {
			MacAddressDisplay macs[] = node.getMacs();
			
			// Process each mac address for the node
			for (int i = 0; i < macs.length; i++) {
				if (macs[i] != null) {
					String macAddress = new String("");
					JTextField fields[] = macs[i].getMacFields();
				
					// Assemble mac address
					for (int j = 0; j < fields.length; j++) {
						macAddress += fields[j].getText();
					
						// Only add a colon if we're not on the last octet
						if ( j != fields.length-1 )
							macAddress += ":";
					}
				
					// Update the mac address for this eth of this node
					if (d.findHost(node.getNodeNameLabel().getText()) != null)
						d.findHost(node.getNodeNameLabel().getText()).addUpdateEth(i, macAddress);
					else if (e.findHost(node.getNodeNameLabel().getText()) != null)
						e.findHost(node.getNodeNameLabel().getText()).addUpdateEth(i, macAddress);
					else
						System.err.println("Unknown host.");
				}
			}
		}
	}
	
	/**
	 * This method initializes openMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getOpenMenuItem() {
		if (openMenuItem == null) {
			openMenuItem = new JMenuItem("Open...");
			openMenuItem.setMnemonic(KeyEvent.VK_O);
			openMenuItem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					// Show open file dialog
					JFileChooser fc = new JFileChooser();
					int returnVal = fc.showOpenDialog(MacGui.this);
					
					// Re-initialize GUI with new dhcpd.conf file
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						String fileName = fc.getSelectedFile().getAbsolutePath();
						MacGui.this.reinitialize(fileName);
					}
				}
			});
		}
		return openMenuItem;
	}

	/**
	 * This method initializes helpMenu	
	 * 	
	 * @return javax.swing.JMenu	
	 */
	private JMenu getHelpMenu() {
		if (helpMenu == null) {
			helpMenu = new JMenu();
			helpMenu.setText("Help");
			helpMenu.setMnemonic(KeyEvent.VK_H);
			helpMenu.add(getAboutMenuItem());
		}
		return helpMenu;
	}

	/**
	 * This method initializes aboutMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getAboutMenuItem() {
		if (aboutMenuItem == null) {
			aboutMenuItem = new JMenuItem();
			aboutMenuItem.setText("About...");
			aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					JDialog dialog = new JDialog();
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setContentPane(new JTextArea("Author: Justin Bennett\nDate: December 17, 2010"));
					dialog.setSize(new Dimension(300,100));
					dialog.setVisible(true);
				}
			});
		}
		return aboutMenuItem;
	}

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		if (args.length != 2) {
			System.err.println("MacGui: Usage: java -jar MacGui.jar <dhcpd filename> <ethers filename>");
			System.exit(ERROR);
		}
		// Start GUI
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MacGui mg = new MacGui(args[0], args[1]);
				mg.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				mg.setVisible(true);
			}
		});
	}

	/********************************************************************************/
	//    Auto-generated GUI Code from VEP
	/********************************************************************************/
	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			ethersFileLabel = new JLabel();
			ethersFileLabel.setBounds(new Rectangle(15, 429, 362, 19));
			ethersFileLabel.setText("");
			ethersFileLabel.setHorizontalAlignment(SwingConstants.CENTER);
			dhcpdFileLabel = new JLabel();
			dhcpdFileLabel.setBounds(new Rectangle(15, 407, 362, 18));
			dhcpdFileLabel.setHorizontalAlignment(SwingConstants.CENTER);
			dhcpdFileLabel.setText("");
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getSaveButton(), null);
			jContentPane.add(getDistButton(), null);
			jContentPane.add(getRestartDHCPButton(), null);
			jContentPane.add(getMacTabbedPane(), null);
			jContentPane.add(dhcpdFileLabel, null);
			jContentPane.add(ethersFileLabel, null);
		}
		return jContentPane;
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
	 * This method initializes closeMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getCloseMenuItem() {
		if (closeMenuItem == null) {
			closeMenuItem = new JMenuItem();
			closeMenuItem.setText("Close");
			closeMenuItem.setMnemonic(KeyEvent.VK_C);
			closeMenuItem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.exit(0);
				}
			});
		}
		return closeMenuItem;
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
			fileMenu.setMnemonic(KeyEvent.VK_F);
			fileMenu.add(getOpenMenuItem());
			fileMenu.add(getCloseMenuItem());
		}
		return fileMenu;
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
			mbar.add(getHelpMenu());
		}
		return mbar;
	}
}  //  @jve:decl-index=0:visual-constraint="53,11"
