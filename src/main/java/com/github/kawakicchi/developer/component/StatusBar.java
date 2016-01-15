package com.github.kawakicchi.developer.component;

import java.awt.Component;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;

import javax.swing.JPanel;

public class StatusBar extends JPanel {

	/** serialVersionUID */
	private static final long serialVersionUID = -5198824301149388651L;

	public StatusBar() {
		setLayout(null);

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(final ComponentEvent e) {
				refresh();
			}
		});

		addContainerListener(new ContainerListener() {
			@Override
			public void componentRemoved(ContainerEvent e) {
				refresh();
			}

			@Override
			public void componentAdded(ContainerEvent e) {
				refresh();
			}
		});
	}

	private synchronized void refresh() {
		Insets insets = getInsets();
		int width = getWidth() - (insets.left + insets.right);
		int height = getHeight() - (insets.top + insets.bottom);

		int totalWidth = 0;
		Component stretchComponent = null;
		Component[] components = getComponents();
		for (Component component : components) {
			if (component instanceof StatusItem) {
				if (((StatusItem) component).isStretch()) {
					if (null == stretchComponent) {
						stretchComponent = component;
					}
				}
			}
			if (stretchComponent != component) {
				totalWidth += component.getWidth();
			}
		}

		if (null == stretchComponent) {
			//System.out.println("not found stretch component.");
		}

		if (null == stretchComponent && 0 < getComponentCount()) {
			//System.out.println("top component stretch.");
			stretchComponent = getComponent(0);
			totalWidth -= stretchComponent.getWidth();
		}

		//System.out.println(String.format("%d", totalWidth));

		int y = 2;
		int x = 4;
		for (int i = 0; i < getComponentCount(); i++) {
			Component component = getComponent(i);

			int cWidth = 0;
			if (stretchComponent == component) {
				cWidth = width - (totalWidth + 8);
			} else {
				cWidth = component.getWidth();
			}

			component.setBounds(x, y, cWidth, height - 8);
			x += cWidth;
		}
	}
}
