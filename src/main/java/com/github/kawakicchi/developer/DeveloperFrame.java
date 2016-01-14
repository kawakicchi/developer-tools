package com.github.kawakicchi.developer;

import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;

public class DeveloperFrame extends JFrame {

	public static void main(final String[] args) {
		DeveloperFrame frm = new DeveloperFrame();
		frm.setVisible(true);
	}
	
	/** serialVersionUID */
	private static final long serialVersionUID = -8105655263394442225L;

	private JDesktopPane desktop;
	
	public DeveloperFrame() {
		setLayout(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		desktop = new JDesktopPane();
		add(desktop);
		

		JInternalFrame iframe1 = new JInternalFrame();
		iframe1.setTitle("TEST");
		iframe1.setSize(150, 100);
		iframe1.setLocation(10, 10);
		iframe1.setVisible(true);
		iframe1.setResizable(true);
		iframe1.setMaximizable(true);
		iframe1.setIconifiable(true);
		iframe1.setClosable(true);

		desktop.add(iframe1);
		
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				Insets insets = getInsets();
				int width = getWidth() - (insets.left + insets.right);
				int height = getHeight() - (insets.top + insets.bottom);
				desktop.setSize(width, height);
			}
		});
		
		setSize(1024, 900);
	}
	
}
