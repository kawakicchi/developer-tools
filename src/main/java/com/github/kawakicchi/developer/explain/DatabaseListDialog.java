package com.github.kawakicchi.developer.explain;

import java.awt.Frame;

import com.github.kawakicchi.developer.component.TableConfigurationDialog;


public class DatabaseListDialog extends TableConfigurationDialog {

	/** serialVersionUID */
	private static final long serialVersionUID = -8679170627554038540L;

	public DatabaseListDialog(final Frame owner, final boolean modal) {
		super(owner, modal);
		
		setSize(800, 600);
	}
}
