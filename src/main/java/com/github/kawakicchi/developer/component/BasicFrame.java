package com.github.kawakicchi.developer.component;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;
import javax.swing.JMenuBar;

public abstract class BasicFrame extends JFrame {

	/** serialVersionUID */
	private static final long serialVersionUID = -627350412387145995L;

	private JMenuBar menuBar;
	private StatusBar statusBar;

	public BasicFrame() {
		setLayout(null);

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		statusBar = new StatusBar();
		add(statusBar);
		doInitStatusBar(statusBar);

		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		doInitMenuBar(menuBar);

		doInit();

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(final ComponentEvent e) {
				Insets insets = getInsets();
				int width = getWidth() - (insets.left + insets.right);
				int height = getHeight() - (insets.top + insets.bottom);
				if (null != menuBar) {
					height -= menuBar.getHeight();
				}
				if (null != statusBar) {
					statusBar.setBounds(0, height - 24, width, 24);
					height -= 24;
				}
				doResized(new Dimension(width, height));
			}
		});

		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Rectangle desktopBounds = env.getMaximumWindowBounds();
		desktopBounds.height -= 240;
		setBounds(desktopBounds);
	}

	protected final StatusBar getStatusBar() {
		return statusBar;
	}

	protected void doInit() {

	}

	protected void doInitMenuBar(final JMenuBar menuBar) {

	}
	
	protected void doInitStatusBar(final StatusBar statusBar) {
		
	}

	protected void doResized(final Dimension dimension) {

	}
}
