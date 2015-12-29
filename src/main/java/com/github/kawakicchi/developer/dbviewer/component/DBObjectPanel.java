package com.github.kawakicchi.developer.dbviewer.component;

import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.github.kawakicchi.developer.dbviewer.model.DatabaseModel;
import com.github.kawakicchi.developer.dbviewer.model.UserEntity;

public class DBObjectPanel extends JPanel {

	/** serialVersionUID */
	private static final long serialVersionUID = -1545074734344651645L;

	private static final int COMPONENT_LABEL_WIDTH = 80;
	private static final int COMPONENT_HEIGHT = 24;
	private static final int COMPONENT_MARGIN = 4;
	private static final int COMPOENNT_INTERVAL = 2;

	private JLabel lblUser;
	private JComboBox<String> cmbUser;
	private JLabel lblType;
	private JComboBox<String> cmbType;

	private DatabaseModel model;

	public DBObjectPanel() {
		setLayout(null);

		int x = COMPONENT_MARGIN;
		int y = COMPONENT_MARGIN;
		lblUser = new JLabel("User");
		lblUser.setLocation(x, y);
		lblUser.setSize(COMPONENT_LABEL_WIDTH, COMPONENT_HEIGHT);
		add(lblUser);
		cmbUser = new JComboBox<String>();
		cmbUser.setLocation(x + COMPONENT_LABEL_WIDTH, y);
		add(cmbUser);

		y += COMPONENT_HEIGHT + COMPOENNT_INTERVAL;
		lblType = new JLabel("Type");
		lblType.setLocation(x, y);
		lblType.setSize(COMPONENT_LABEL_WIDTH, COMPONENT_HEIGHT);
		add(lblType);
		cmbType = new JComboBox<String>();
		cmbType.setLocation(x + COMPONENT_LABEL_WIDTH, y);
		add(cmbType);

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				resize();
			}

			@Override
			public void componentResized(ComponentEvent e) {
				resize();
			}

			private void resize() {
				Insets insets = getInsets();
				int width = getWidth() - (insets.left + insets.right);
				cmbUser.setSize(width - (COMPONENT_LABEL_WIDTH + COMPONENT_MARGIN * 2), COMPONENT_HEIGHT);
				cmbType.setSize(width - (COMPONENT_LABEL_WIDTH + COMPONENT_MARGIN * 2), COMPONENT_HEIGHT);
			}
		});

		cmbType.addItem("TABLE");
		cmbType.addItem("VIEW");
		cmbType.addItem("SYNONYM");
		cmbType.addItem("INDEX");
		cmbType.addItem("CLUSTER");
		cmbType.addItem("SEQUENCE");
		cmbType.addItem("DATABASE LINK");
		cmbType.addItem("SNAPSHOT");
		cmbType.addItem("SNAPSHOT LOG");
		cmbType.addItem("PACKAGE");
		cmbType.addItem("PACKAGE BODY");
		cmbType.addItem("FUNCTION");
		cmbType.addItem("PROCEDURE");
		cmbType.addItem("TRIGGER");
		cmbType.addItem("TYPE");
		cmbType.addItem("TYPE BODY");
		cmbType.addItem("LIBRARY");
	}

	public void setDatabaseModel(final DatabaseModel model) {
		this.model = model;

		try {
			cmbUser.removeAllItems();
			List<UserEntity> users = model.getUserList();
			for (UserEntity user : users) {
				cmbUser.addItem(user.getName());
			}

			cmbType.removeAllItems();
			List<String> types = model.getTypeList();
			for (String type : types) {
				cmbType.addItem(type);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

}
