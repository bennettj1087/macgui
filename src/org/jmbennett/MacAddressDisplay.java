package org.jmbennett;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.regex.Pattern;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * Class representing one line on the MACGUI display containing an Ethernet
 * interface and six fields representing the six octets of a MAC address.
 * 
 * @author Justin Bennett
 * December 1, 2010
 */
public class MacAddressDisplay {
	private JLabel ethLabel;
	private JTextField[] macFields;
	
	/**
	 * Constructor for MacAddressDisplay accepts the Ethernet
	 * interface number and the mac address.
	 * 
	 * @param ethNumber
	 * @param macAddress
	 */
	public MacAddressDisplay(int ethNumber, String macAddress) {
		ethLabel = new JLabel("eth" + ethNumber);
		macFields = new JTextField[6];
		
		// Split mac address and set text fields
		String[] macPieces = macAddress.split(":");
		for (int i = 0; i < 6; i++) {
			macFields[i] = new JTextField(macPieces[i]);
			
			// Set InputVerifier for text fields.  This will restrict
			// input to a two digit hex number
			macFields[i].setInputVerifier(new MacInputVerifier());
			
			// Add FocusListener to select text on focus
			macFields[i].addFocusListener(new FocusListener(){
				public void focusGained(FocusEvent e) {
					((JTextField) e.getSource()).selectAll();
				}
				public void focusLost(FocusEvent e) {}});
			
			// Add listener to shade the box yellow when the text is changed
			macFields[i].addKeyListener(new KeyListener() {
				public void keyTyped(KeyEvent e) {
					((JTextField) e.getSource()).setBackground(new Color(255, 248, 149));
				}
				public void keyPressed(KeyEvent e) {}
				public void keyReleased(KeyEvent e) {}
			});
		}
	}
	
	// Get methods
	public JLabel getEthLabel() { return ethLabel; }
	public JTextField[] getMacFields() { return macFields; }
	
	/**
	 * Input Verifier for MAC Address fields.
	 * Restricts input to a 2 digit Hexadecimal number
	 * @author Justin Bennett
	 * December 15, 2010
	 */
	public class MacInputVerifier extends InputVerifier {
		@Override
		public boolean verify(JComponent input) {
			JTextField textField = (JTextField) input;
			String hexNumber = textField.getText();
			if (Pattern.matches("[a-fA-F0-9]{2}?", hexNumber))
				return true;
			return false;
		}

	}
}
