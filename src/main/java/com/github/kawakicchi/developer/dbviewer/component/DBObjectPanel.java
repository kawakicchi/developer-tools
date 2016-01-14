package com.github.kawakicchi.developer.dbviewer.component;

import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.github.kawakicchi.developer.dbviewer.model.DatabaseModel;
import com.github.kawakicchi.developer.dbviewer.model.UserEntity;

public class DBObjectPanel extends JPanel {

	/** serialVersionUID */
	private static final long serialVersionUID = -1545074734344651645L;

	private static final int COMPONENT_LABEL_WIDTH = 40;
	private static final int COMPONENT_HEIGHT = 24;
	private static final int COMPONENT_MARGIN = 6;
	private static final int COMPOENNT_INTERVAL = 2;

	private JLabel lblUser;
	private JComboBox<String> cmbUser;
	private JLabel lblType;
	private JComboBox<String> cmbType;
	private JLabel lblFilt;
	private JTextField txtFilt;

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

		y += COMPONENT_HEIGHT + COMPOENNT_INTERVAL;
		lblFilt = new JLabel("Filter");
		lblFilt.setLocation(x, y);
		lblFilt.setSize(COMPONENT_LABEL_WIDTH, COMPONENT_HEIGHT);
		add(lblFilt);
		txtFilt = new JTextField();
		txtFilt.setLocation(x + COMPONENT_LABEL_WIDTH, y);
		add(txtFilt);
		
		cmbType.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED){
					System.out.println( cmbType.getSelectedItem() );
				}
			}
		});

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
				txtFilt.setSize(width - (COMPONENT_LABEL_WIDTH + COMPONENT_MARGIN * 2), COMPONENT_HEIGHT);
			}
		});
	}

	public void setDatabaseModel(final DatabaseModel model) {
		try {
			cmbUser.removeAllItems();
			List<UserEntity> users = model.getUserList();
			for (UserEntity user : users) {
				cmbUser.addItem(user.getName());
			}

			cmbType.removeAllItems();
			List<String> types = model.getObjectTypeList();
			for (String type : types) {
				cmbType.addItem(type);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

}
