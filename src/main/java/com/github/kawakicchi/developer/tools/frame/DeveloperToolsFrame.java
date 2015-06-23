package com.github.kawakicchi.developer.tools.frame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.github.kawakicchi.developer.tools.dialog.TemplatorDialog;

public class DeveloperToolsFrame extends JFrame {

	/** serialVersionUID */
	private static final long serialVersionUID = 5323461682591098537L;

	private JMenuBar menuBar;
	private JMenu menuFile;
	private JMenu menuRakRak;
	private JMenu menuTool;
	private JMenuItem menuToolTemplator;
	
	public static void main(final String[] args) {
		DeveloperToolsFrame frm = new DeveloperToolsFrame();
		frm.setVisible(true);
	}
	
	public DeveloperToolsFrame() {
		setTitle("DeveloperTools");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		initMenu();
		addMenuEvent();
		
		setSize(800, 600);
	}
	
	private void initMenu() {
		menuBar = new JMenuBar();
		
		// ファイル
		menuFile = new JMenu("ファイル");
		
		menuBar.add(menuFile);
		
		// ツール
		menuTool = new JMenu("ツール");
		menuToolTemplator = new JMenuItem("テンプレーター");
		menuTool.add(menuToolTemplator);
		menuBar.add(menuTool);
		
		// 楽々
		menuRakRak = new JMenu("楽々");
		
		menuBar.add(menuRakRak);
		
		setJMenuBar(menuBar);
	}
	
	private void addMenuEvent() {
		menuToolTemplator.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onClickMenuTemplator();
			}
		});
	}
	
	private void onClickMenuTemplator() {
		TemplatorDialog dlg = new TemplatorDialog(this);
		dlg.setVisible(true);
	}
}
