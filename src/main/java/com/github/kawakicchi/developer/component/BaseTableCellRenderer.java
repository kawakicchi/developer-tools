package com.github.kawakicchi.developer.component;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.github.kawakicchi.developer.component.ResultSetTable.ValueLabel;

public class BaseTableCellRenderer extends DefaultTableCellRenderer {

	/** serialVersionUID */
	private static final long serialVersionUID = -3694018784183642741L;

	private static final Color evenColor = new Color(240, 240, 255);

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		
		if (isSelected) {
			setForeground(table.getSelectionForeground());
			setBackground(table.getSelectionBackground());
		} else {
			if (value instanceof ValueLabel) {
				ValueLabel vl = (ValueLabel) value;
				if (null == vl.getValue()) {
					setForeground(Color.GRAY);
				} else {
					setForeground(table.getForeground());
				}
			} else {
				setForeground(table.getForeground());
			}
			setBackground((row % 2 == 1) ? evenColor : table.getBackground());
		}
		
		setHorizontalAlignment((value instanceof Number) ? RIGHT : LEFT);
		return this;
	}
}
