package com.github.kawakicchi.developer.tools.dialog;

import java.awt.Frame;

import javax.swing.JDialog;

public class TemplatorDialog extends JDialog {

	/** serialVersionUID */
	private static final long serialVersionUID = 403672882078869321L;

	public TemplatorDialog(final Frame owner) {
		super(owner, true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(400,400);
	}
}
