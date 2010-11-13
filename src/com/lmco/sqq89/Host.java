package com.lmco.sqq89;

public class Host {
	private String hostname;
	private String ipAddress;
	private String[] eths;
	
	/**
	 * Creates new Host for DhcpdConf
	 * 
	 * @param newHostname
	 */
	public Host(String newHostname, String newIP) {
		this.hostname = new String(newHostname);
		this.ipAddress = new String(newIP);
	}
	
	public void printHost() {
		String out = new String();
		
		for (int i = 0; i < eths.length; i++) {
			out += "host " + hostname + "-eth" + i + " {\n"
				+  "\t## Unit " + hostname.substring(1, hostname.indexOf('a')-1) + " " 
					+ hostname.substring(hostname.indexOf('a')).toUpperCase() + " ##\n"
				+  "\thardware ethernet " + eths[i] + ";\n"
				+  "\tfixed-address " + ipAddress + ";\n"
				+  "\tfilename \"pxelinux.0\";\n"
				+  "\toption hostname " + hostname + ";\n"
				+  "}\n\n";
		}
	}
}
