package com.github.kawakicchi.developer.component;

import java.awt.Frame;

import javax.swing.JDialog;

public class BaseDialog extends JDialog {

	/** serialVersionUID */
	private static final long serialVersionUID = 6605953298670959625L;


	public BaseDialog(final Frame owner, final boolean modal) {
		super(owner, modal);
	}

}
