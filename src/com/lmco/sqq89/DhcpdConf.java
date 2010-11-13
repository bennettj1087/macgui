package com.lmco.sqq89;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;

public class DhcpdConf {
	private String header;
	private LinkedList<Host> hosts;
	
	/**
	 * TODO
	 * 
	 * Reads in and parses dhcpd.conf file
	 * 
	 */
	public DhcpdConf(File f) {
		hosts = new LinkedList<Host>();
		header = new String();
		
		// Open dhcpd.conf file and create BufferedReader
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			
			
		} catch(FileNotFoundException fnfe) {
			System.err.println("FileNotFoundException: " + fnfe.getMessage());
		}
	}
	
	public LinkedList<Host> getHosts() { return hosts; }
	
	public static void main(String[] args) {
		DhcpdConf d = new DhcpdConf(new File("dhcpd.conf"));
	}
}
