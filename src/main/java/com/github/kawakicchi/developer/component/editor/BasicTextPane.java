package com.github.kawakicchi.developer.component.editor;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.TabSet;
import javax.swing.text.TabStop;

public class BasicTextPane extends JTextPane {

	/** serialVersionUID */
	private static final long serialVersionUID = -699085174071789314L;

	public BasicTextPane() {
		setOpaque(false);

		Font font = new Font("ＭＳ ゴシック", Font.PLAIN, 12);
		setFont(font);

		boolean tabFlag = false;
		if (tabFlag) {
			FontMetrics fontMetrics = getFontMetrics(font);
			int charWidth = fontMetrics.charWidth('m');
			int tabLength = charWidth * 4;
			TabStop[] tabs = new TabStop[100];
			for (int j = 0; j < tabs.length; j++) {
				tabs[j] = new TabStop((j + 1) * tabLength);
			}
			TabSet tabSet = new TabSet(tabs);
			SimpleAttributeSet atrTabSpace = new SimpleAttributeSet();
			StyleConstants.setTabSet(atrTabSpace, tabSet);

			setStyledDocument(new DefaultStyledDocument());
			getStyledDocument().setParagraphAttributes(0, getDocument().getLength(), atrTabSpace, false);
		}

		setEditorKit(new NoWrapEditorKit());

		getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				//System.out.println(String.format("remove"));
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				//System.out.println(String.format("insert"));
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				//System.out.println("change");
			}
		});

		addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				//System.out.println("keyTyped");
			}

			@Override
			public void keyReleased(KeyEvent e) {
				//System.out.println("keyReleased");
				doChangeText();
			}

			@Override
			public void keyPressed(KeyEvent e) {
				//System.out.println("keyPressed");
			}
		});
	}

	public void setText(final String text) {
		super.setText(text);
		doChangeText();
	}

	protected void doChangeText() {
	}
}
