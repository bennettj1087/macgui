package com.lmco.sqq89;

/**
 * EthEntry: Represents one Ethernet interface for
 * 			 particular host.
 * 
 * @author Justin Bennett
 */
public class EthEntry {
	private int eth;
	private String macAddress;
	
	/**
	 * Creates EthEntry for Host class
	 */
	public EthEntry(int newEth, String newMac) {
		this.eth = newEth;
		this.macAddress = new String(newMac);
	}
}
