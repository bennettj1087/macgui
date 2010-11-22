package com.lmco.sqq89;

import java.awt.Dimension;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

public class MacGui extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public MacGui(String title) {
		super(title);
	}

	public static void createAndShowGui() {
		MacGui mg = new MacGui("MAC GUI");
		mg.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menubar = new JMenuBar();
		menubar.setPreferredSize(new Dimension(200,20));
		
		mg.setJMenuBar(menubar);
		
		mg.pack();
		mg.setVisible(true);	
	}

	public static void main(String[] args) {
/*		DhcpdConf d = new DhcpdConf(new File("dhcpd.conf"));
		
		System.out.println(d.getHeader());
		for (Host h : d.getHosts())
			h.printHost();*/
		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGui();
			}
		});
	}
}
