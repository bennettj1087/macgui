package org.jmbennett;

import java.io.PrintStream;

/**
 * The Host class is used to store a set of entries from 
 * the dhcpd.conf (or ethers) file pertaining to a single
 * node in the SQQ89 system.  Since each node typically has
 * more than one network interface, the hosts are differentiated
 * using the form hostname-ethX.  A host object will store the
 * MAC address information for up to 4 eths per host (the
 * current maximum per node on SQQ89).
 * 
 * @author Justin Bennett
 * 12/2010
 */
public class Host {
	// Private members to store host information
	private String hostname;
	private String ipAddress;
	private String[] eths;
	
	/**
	 * Default constructor
	 */
	public Host() {
		this.hostname = new String("");
		this.ipAddress = new String("");
		this.eths = new String[4];
	}

	// Get methods for private members
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
	 * @param out
	 */
	public void printHost(PrintStream out) {
		for (int i = 0; i < eths.length; i++) {
			if (eths[i] != null) {
				out.println("host " + hostname + "-eth" + i + " {");
				out.println("\t#--Unit " + hostname.substring(1, hostname.indexOf('a')) + " " +
						hostname.substring(hostname.indexOf('a')).toUpperCase() + "--#");
				out.println("\thardware ethernet\t" + eths[i] + ";");
				out.println("\tfixed-address\t\t" + ipAddress + ";");
				out.println("\tfilename\t\t\t\"pxelinux.0\";");
				out.println("\toption host-name\t\"" + hostname + "\";");
				out.println("}");
				out.println("");
			}
		}
	}
	
	/**
	 * Prints Host information properly formatted
	 * for ethers file
	 * 
	 * @param out
	 */
	public void printEthersHost(PrintStream out) {
		for (int i = 0; i < eths.length; i++) {
			if (eths[i] != null)
				out.println(hostname + "\t" + eths[i] + ";");
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
