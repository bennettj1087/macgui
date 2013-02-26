package org.jmbennett;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedList;

/**
 * Ethers is an object used to hold the hostname - MAC address
 * pairs contained in an ethers file (used by rarpd for network
 * booting).
 * 
 * @author Justin Bennett
 * 12/2010
 */
public class Ethers {
	private File ethersFile;
	private LinkedList<Host> hosts;
	
	public Ethers(File f) {
		ethersFile = f;
		hosts = new LinkedList<Host>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line = new String();
			
			Host h;
			while ((line = br.readLine()) != null) {
				String splitLine[] = line.split("\\s+");
				h = new Host(splitLine[0], "");
				h.addUpdateEth(0, splitLine[1].substring(0, splitLine[1].length()-1));
				hosts.add(h);
			}			
		} catch(FileNotFoundException fnfe) {
			System.err.println("FileNotFoundException in Ethers(): " + fnfe.getMessage());
			fnfe.printStackTrace(System.err);
		} catch(IOException ioe) {
			System.err.println("IOException in Ethers(): " + ioe.getMessage());
			ioe.printStackTrace(System.err);
		}
	}
	
	public LinkedList<Host> getHosts() { return hosts; }
	public String getFileName() { return ethersFile.getAbsolutePath(); }
	
	public Host findHost(String hostname) {
		for (Host h : hosts) {
			if (h.getHostname().equals(hostname))
				return h;
		}
		
		return null;
	}
	
	public void writeEthers() {
		try {
			// Open the output file for writing
			PrintStream out = new PrintStream(ethersFile);
			
			// Print ethers file
			for (Host h : getHosts())
				h.printEthersHost(out);			
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}
}
