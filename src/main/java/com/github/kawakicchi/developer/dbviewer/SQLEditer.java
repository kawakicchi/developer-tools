package com.github.kawakicchi.developer.dbviewer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class SQLEditer extends JPanel {

	/** serialVersionUID */
	private static final long serialVersionUID = -1609621769258753201L;

	private JTextArea text;
	private JScrollPane scroll;

	public SQLEditer() {
		setLayout(null);
		

		text = new JTextArea();
		scroll = new JScrollPane(text);
		
		add(scroll);
		
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				Insets inset = getInsets();
				int height = getHeight() - (inset.top + inset.bottom);
				int width = getWidth() - (inset.left + inset.right);
				Dimension dimension = new Dimension(width, height);
				scroll.setSize(dimension);
				scroll.setPreferredSize(dimension);
			}
		});

		text.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(final KeyEvent e) {
				if(e.getModifiers() == KeyEvent.CTRL_MASK && e.getKeyCode() == KeyEvent.VK_R){
				}
				// repaint();
			}
		});

		setBackground(Color.red);
		scroll.setBackground(Color.blue);
	}
}
