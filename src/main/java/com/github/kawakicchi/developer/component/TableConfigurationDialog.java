package com.github.kawakicchi.developer.component;

import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JTable;

public class TableConfigurationDialog extends ConfigurationDialog {

	/** serialVersionUID */
	private static final long serialVersionUID = 8506362905269467952L;

	private JTable table;
	
	public TableConfigurationDialog(final Frame owner, final boolean modal) {
		super(owner, modal);

		table = new JTable();
		table.setLocation(4, 4);
		addClientComponent(table);

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				Insets insets = getClientInsets();
				int width = getClientWidth() - (insets.left + insets.right);
				int height = getClientHeight() - (insets.top + insets.bottom);
				
				table.setSize(width-8, height-8);
			}
		});
	}
}
