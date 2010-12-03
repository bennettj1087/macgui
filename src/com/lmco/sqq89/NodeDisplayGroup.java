package com.lmco.sqq89;

import javax.swing.JLabel;

/**
 * Class representing one node's set of MAC addresses. This
 * will typically contain either 2 or 4 mac addresses.
 * 
 * @author Justin Bennett
 * December 1, 2010
 */
public class NodeDisplayGroup {
	private JLabel nodeNameLabel;
	private MacAddressDisplay[] macs; 
	
	/**
	 * Constructor for NodeDisplayGroup accepting a node name
	 * and a number of Ethernet interfaces for that node.
	 * 
	 * @param nodeName
	 * @param numEths
	 */
	public NodeDisplayGroup(Host h) {
		nodeNameLabel = new JLabel(h.getHostname());
		macs = new MacAddressDisplay[h.getEths().length];
		
		for (int i = 0; i < macs.length; i++) {
			macs[i] = new MacAddressDisplay(i, h.getEths()[i]);
		}
	}
	
	public JLabel getNodeNameLabel() { return nodeNameLabel; }
	public MacAddressDisplay[] getMacs() { return macs; }
}
