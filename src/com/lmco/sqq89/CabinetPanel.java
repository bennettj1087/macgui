package com.lmco.sqq89;

import javax.swing.JPanel;

public class CabinetPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private int currY;
	
	public CabinetPanel() {
		super();
		currY = 15;
	}
	
	public int getCurrY() { return currY; }
	public void setCurrY(int i) { currY = i; }
}