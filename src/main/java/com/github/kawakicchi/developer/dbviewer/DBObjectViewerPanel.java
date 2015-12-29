package com.github.kawakicchi.developer.dbviewer;

import javax.swing.JSplitPane;

import com.github.kawakicchi.developer.dbviewer.component.DBObjectInformationPanel;
import com.github.kawakicchi.developer.dbviewer.component.DBObjectPanel;
import com.github.kawakicchi.developer.dbviewer.model.DatabaseModel;

public class DBObjectViewerPanel extends JSplitPane {

	/** serialVersionUID */
	private static final long serialVersionUID = 6351729022646400350L;

	private DBObjectPanel pnlObject;
	private DBObjectInformationPanel pnlInformation;

	public DBObjectViewerPanel() {
		setOrientation(JSplitPane.VERTICAL_SPLIT);
		setContinuousLayout(true);

		pnlObject = new DBObjectPanel();
		setTopComponent(pnlObject);

		pnlInformation = new DBObjectInformationPanel();
		setBottomComponent(pnlInformation);

		setDividerLocation(120);
	}

	public void setDatabaseModel(final DatabaseModel model) {
		pnlObject.setDatabaseModel(model);
	}

}
