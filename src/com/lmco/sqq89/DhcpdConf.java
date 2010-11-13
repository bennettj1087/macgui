package com.lmco.sqq89;

import java.io.File;
import java.util.HashMap;

public class DhcpdConf {
	private String header;
	private HashMap<String,Host> hosts;
	
	/**
	 * TODO
	 * 
	 * Reads in and parses dhcpd.conf file
	 * 
	 */
	public DhcpdConf() {
		hosts = new HashMap<String,Host>();
		Host h;
		
		h = new Host("s858a30", "192.168.31.130");
		h.addEth(0, "00:30:48:aa:bb:cc");
		h.addEth(1, "00:30:48:aa:bb:cd");
		h.addEth(2, "00:15:17:aa:bb:cc");
		h.addEth(3, "00:15:17:aa:bb:cd");
		hosts.put("s858a30", h);
		
		h = new Host("s845a20", "192.168.31.131");
		h.addEth(0, "00:30:48:aa:bb:cc");
		h.addEth(1, "00:30:48:aa:bb:cd");
		h.addEth(2, "00:15:17:aa:bb:cc");
		h.addEth(3, "00:15:17:aa:bb:cd");
		hosts.put("s845a20", h);
		
		h = new Host("s871a20", "192.168.31.132");
		h.addEth(0, "00:30:48:aa:bb:cc");
		h.addEth(1, "00:30:48:aa:bb:cd");
		h.addEth(2, "00:15:17:aa:bb:cc");
		h.addEth(3, "00:15:17:aa:bb:cd");
		hosts.put("s871a20", h);
		
		h = new Host("s846a34", "192.168.31.133");
		h.addEth(0, "00:30:48:aa:bb:cc");
		h.addEth(1, "00:30:48:aa:bb:cd");
		h.addEth(2, "00:15:17:aa:bb:cc");
		h.addEth(3, "00:15:17:aa:bb:cd");
		hosts.put("s846a34", h);
	}
	
	public HashMap<String,Host> getHosts() { return hosts; }
	
	public static void main() {
		DhcpdConf d = new DhcpdConf();
		

	}
}
