package com.github.kawakicchi.developer.component;

import java.awt.Component;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.github.kawakicchi.developer.explain.LabelManager;

public class ConfigurationDialog extends BaseDialog {

	/** serialVersionUID */
	private static final long serialVersionUID = 3265917964513834419L;
	
	private static final int BUTTON_WIDTH = 120;
	private static final int BUTTON_HEIGHT = 24;
	
	private JPanel pnlClient;
	private JButton btnOK;
	private JButton btnCancel;

	public ConfigurationDialog(final Frame owner, final boolean modal) {
		super(owner, modal);
		
		pnlClient = new JPanel();
		pnlClient.setLocation(0, 0);
		pnlClient.setLayout(null);

		btnOK = new JButton(LabelManager.get("Button.OK"));
		btnOK.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);

		btnCancel = new JButton(LabelManager.get("Button.Cancel"));
		btnCancel.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		

		setLayout(null);

		super.add(pnlClient);
		super.add(btnOK);
		super.add(btnCancel);

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(final ComponentEvent e) {
				Insets insets = getInsets();
				int width = getWidth() - (insets.left + insets.right);
				int height = getHeight() - (insets.top + insets.bottom);

				pnlClient.setSize(width, height - (4 + BUTTON_HEIGHT + 8));
				btnOK.setLocation( width - (BUTTON_WIDTH * 2 + 8 + 8) , height - (BUTTON_HEIGHT + 8));
				btnCancel.setLocation( width - (BUTTON_WIDTH * 1 + 8) , height - (BUTTON_HEIGHT + 8));
			}
		});
		
	}

	public Insets getClientInsets() {
		return pnlClient.getInsets();
	}
	public int getClientWidth() {
		return pnlClient.getWidth();
	}
	public int getClientHeight() {
		return pnlClient.getHeight();
	}
	public void addClientComponent(final Component c) {
		pnlClient.add(c);
	}
}
