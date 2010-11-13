package com.lmco.sqq89;

import java.util.LinkedList;

public class Host {
	private String hostname;
	private String ipAddress;
	private LinkedList<EthEntry> entries;
	
	/**
	 * Creates new Host for DhcpdConf
	 * 
	 * @param newHostname
	 */
	public Host(String newHostname, String newIP) {
		this.hostname = new String(newHostname);
		this.ipAddress = new String(newIP);
		
		this.entries = new LinkedList<EthEntry>();
	}
}
