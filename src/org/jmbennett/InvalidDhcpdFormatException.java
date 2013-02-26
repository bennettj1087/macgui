package com.lmco.sqq89;

/**
 * Exception to be thrown when dhcpd.conf file format is found
 * to be invalid.
 * 
 * @author Justin Bennett
 */
public class InvalidDhcpdFormatException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidDhcpdFormatException(String message) {
		super(message);
	}
}
