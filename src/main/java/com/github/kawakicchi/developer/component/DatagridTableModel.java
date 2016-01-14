package com.github.kawakicchi.developer.component;

import javax.swing.table.DefaultTableModel;

public class DatagridTableModel extends DefaultTableModel {

	/** serialVersionUID */
	private static final long serialVersionUID = -4730539557410712088L;

	@Override
	public boolean isCellEditable(int row, int column) {
		// 編集不可
	    return false;
	}
}
