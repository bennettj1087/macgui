package com.lmco.sqq89;

import javax.swing.JPanel;

/**
 * CabinetPanel is an extension of JPanel that holds an
 * additional private member to keep track of the current
 * Y-position in the panel.  This is used when the GUI
 * is built at runtime.
 * 
 * @author Justin Bennett
 * 12/2010
 */
public class CabinetPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private int currY;
	
	/**
	 * Default constructor sets currY to an initial value of 15
	 */
	public CabinetPanel() {
		super();
		currY = 15;
	}
	
	// Get and set methods for currY
	public int getCurrY() { return currY; }
	public void setCurrY(int i) { currY = i; }
}
