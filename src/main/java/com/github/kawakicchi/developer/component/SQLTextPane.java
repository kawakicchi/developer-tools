package com.github.kawakicchi.developer.component;

import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.BadLocationException;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class SQLTextPane extends BasicTextPane {

	/** serialVersionUID */
	private static final long serialVersionUID = 7284998526769136420L;

	private static final Pattern PTN_KEYWORD = Pattern.compile("(SELECT|END|GROUP|UNION|ALL|AS|MAX|LIKE|MINUS|SUM|CASE|WHEN|THEN|ELSE|NULL|IS|NOT|SYSDATE|FROM|WHERE|AND|OR|ORDER|BY)", Pattern.CASE_INSENSITIVE);
	private static final Pattern PTN_COMMENT = Pattern.compile("(--[^\\r\\n]*)", Pattern.CASE_INSENSITIVE);
	private static final Pattern PTN_STRING = Pattern.compile("('[^']*')", Pattern.CASE_INSENSITIVE);
	private static final Pattern PTN_SYMBOL = Pattern.compile("(,|=|<|>|/|\\.|\\*|\\+|\\(|\\)|\\|)", Pattern.CASE_INSENSITIVE);

	private MutableAttributeSet atrDefault;
	private MutableAttributeSet atrKeyword;
	private MutableAttributeSet atrCommnet;
	private MutableAttributeSet atrString;
	private MutableAttributeSet atrSymbol;
	
	private Color colorKeyword;
	private Color colorComment;
	private Color colorString;
	private Color colorSymbol;
	
	public SQLTextPane() {
		setEditorKit(new SmartEditorKit());

		colorKeyword = new Color(0, 0, 205);
		colorComment = new Color(0, 120, 0);
		colorString = new Color(220, 0, 0);
		colorSymbol = new Color(128, 0, 0);
		
		boolean boldFlag = false;
		
		atrDefault = new SimpleAttributeSet();
		StyleConstants.setForeground(atrDefault, Color.BLACK);

		atrKeyword = new SimpleAttributeSet();
		StyleConstants.setForeground(atrKeyword, colorKeyword);
		StyleConstants.setBold(atrKeyword, boldFlag);

		atrCommnet = new SimpleAttributeSet();
		StyleConstants.setForeground(atrCommnet, colorComment);
		StyleConstants.setBold(atrCommnet, boldFlag);
		
		atrString = new SimpleAttributeSet();
		StyleConstants.setForeground(atrString, colorString);
		StyleConstants.setBold(atrString, boldFlag);

		atrSymbol = new SimpleAttributeSet();
		StyleConstants.setForeground(atrSymbol, colorSymbol);
		StyleConstants.setBold(atrSymbol, boldFlag);
	}
	
	protected void doChangeText() {
		try {
			int length = getDocument().getLength();
			String text = getDocument().getText(0, length);
			
			getStyledDocument().setCharacterAttributes(0, length, atrDefault, true);
			
			Matcher m = null;
			m = PTN_KEYWORD.matcher(text);
			while (m.find()) {
				getStyledDocument().setCharacterAttributes(m.start(), m.end()-m.start(), atrKeyword, true);
			}

			m = PTN_SYMBOL.matcher(text);
			while (m.find()) {
				getStyledDocument().setCharacterAttributes(m.start(), m.end()-m.start(), atrSymbol, true);
			}

			m = PTN_STRING.matcher(text);
			while (m.find()) {
				getStyledDocument().setCharacterAttributes(m.start(), m.end()-m.start(), atrString, true);
			}

			m = PTN_COMMENT.matcher(text);
			while (m.find()) {
				getStyledDocument().setCharacterAttributes(m.start(), m.end()-m.start(), atrCommnet, true);
			}
		} catch (BadLocationException ex) {
			ex.printStackTrace();
		}
	}
}
