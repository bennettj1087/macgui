package com.lmco.sqq89;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
			String line = new String();
			
			// Temp variables for parsing each line
			String currHostname = new String();
			String currIP = new String();
			String currMac = new String();
			int currEth = 0;
			boolean newHost = false;
			
			// Host to hold information as the file is parsed
			Host h = new Host();
			
			while ((line = br.readLine()) != null) {
				if (line.startsWith("host")) {
					// If this is a new hostname, instantiate a new host
					if (!line.substring(line.indexOf(' ')+1, line.indexOf('-')).equals(currHostname)) {
						currHostname = line.substring(line.indexOf(' ')+1, line.indexOf('-'));
						newHost = true;
					} else
						newHost = false;
					
					// Save off the current eth interface and move to next line
					currEth = new Integer(line.substring(line.length()-3, line.length()-2)).intValue();
					continue;
				}
				// MAC Address line starts with "hardware"
				else if (line.startsWith("hardware")) {
					currMac = line.substring(line.lastIndexOf(' '), line.indexOf(';')-1);
					continue;
				}
				// IP Address line starts with "fixed-address"
				else if (line.startsWith("fixed-address")) {
					currIP = line.substring(line.indexOf(' '), line.indexOf(';')-1);
					continue;
				}
				// This line is the same for all hosts, no need to save it
				else if (line.startsWith("filename")) {
					continue;
				}
				// This line contains the host name, it should match with
				// whatever the current hostname is.
				else if (line.startsWith("option")) {
					// If the hostname listed here does not match the
					// current hostname we're working with, then throw
					// an exception
					if (!line.substring(line.indexOf('"')+1, line.lastIndexOf('"')).equals(currHostname)) {
						InvalidDhcpdFormatException up = new InvalidDhcpdFormatException("Invalid dhcpd.conf format.");
						throw up;
					}
				}
				// If it's a }, a # or a blank line, just ignore it
				else if (line.startsWith("}"))
					continue;
				else if (line.startsWith("#"))
					continue;
				else if (line.equals(""))
					continue;
				// If it wasn't any of those, it must be the header information
				else {
					header += line + '\n';
					continue;
				}
				
				if (newHost) {
					h = new Host(currHostname, currIP);
					h.addEth(currEth, currMac);
				}
				else
					h.addEth(currEth, currMac);
				
				if (currEth == 3)
					hosts.add(h);
			}
			
		} catch(FileNotFoundException fnfe) {
			System.err.println("FileNotFoundException: " + fnfe.getMessage());
		} catch(IOException ioe) { 
			System.err.println("IOException: " + ioe.getMessage());
		} catch(InvalidDhcpdFormatException idfe) {
			System.err.println("Exception: " + idfe.getMessage());
		} 
	}
	
	public LinkedList<Host> getHosts() { return hosts; }
	public String getHeader() { return header; }
	
/*	public static void main(String[] args) {
		DhcpdConf d = new DhcpdConf(new File("dhcpd.conf"));
		
		System.out.println(d.getHeader());
		for (Host h : d.getHosts())
			h.printHost();
	}*/
}
