package com.github.kawakicchi.developer.component;

import javax.swing.JLabel;

public class LabelStatusItem extends JLabel implements StatusItem{

	/** serialVersionUID */
	private static final long serialVersionUID = 3878328999908626007L;

	private boolean stretchFlag;
	
	public LabelStatusItem() {
		stretchFlag = false;
	}
	
	public void setStretch(final boolean stretch) {
		this.stretchFlag = stretch;
	}
	
	@Override
	public boolean isStretch() {
		return stretchFlag;
	}

}
