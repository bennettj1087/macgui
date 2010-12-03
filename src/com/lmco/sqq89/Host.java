package com.lmco.sqq89;

import java.io.PrintStream;

public class Host {
	private String hostname;
	private String ipAddress;
	private String[] eths;
	
	public Host() {
		this.hostname = new String("");
		this.ipAddress = new String("");
		this.eths = new String[4];
	}
	
	public String getHostname() { return hostname; } 
	public String getIPAddress() { return ipAddress; } 
	public String[] getEths() { return eths; } 
	
	/**
	 * Creates new Host for DhcpdConf
	 * 
	 * @param newHostname
	 */
	public Host(String newHostname, String newIP) {
		this.hostname = new String(newHostname);
		this.ipAddress = new String(newIP);
		this.eths = new String[4];
	}
	
	/**
	 * Prints Host information properly formatted
	 * for dhcpd.conf file.
	 */
	public void printHost(PrintStream out) {
		for (int i = 0; i < eths.length; i++) {
			out.println("host " + hostname + "-eth" + i + " {");
			out.println("\t#--Unit " + hostname.substring(1, hostname.indexOf('a')) + " " +
					hostname.substring(hostname.indexOf('a')).toUpperCase() + "--#");
			out.println("\thardware ethernet\t" + eths[i] + ";");
			out.println("\tfixed-address\t\t" + ipAddress + ";");
			out.println("\tfilename\t\t\"pxelinux.0\";");
			out.println("\toption host-name\t\"" + hostname + "\";");
			out.println("}");
			out.println("");
		}
	}
	
	/**
	 * Adds/updates MAC address for a particular
	 * Ethernet interface.
	 * 
	 * @param eth
	 * @param macAddress
	 */
	public void addUpdateEth(int eth, String macAddress) {
		eths[eth] = new String(macAddress);
	}
}
