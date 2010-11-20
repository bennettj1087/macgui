package com.lmco.sqq89;

import java.io.File;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

public class MacGui extends JFrame {
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
/*		MacGui mg = new MacGui();

		JMenuBar mb = new JMenuBar();
		mb.add(new JMenu("File"));
		
		mg.add(mb);
		mg.setVisible(true);*/
		DhcpdConf d = new DhcpdConf(new File("dhcpd.conf"));
		
		System.out.println(d.getHeader());
		for (Host h : d.getHosts())
			h.printHost();
	}
}
