package com.github.kawakicchi.developer.component;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import com.github.kawakicchi.developer.logger.Logger;

public class LoggerConsole extends JTextPane implements Logger {
	
	/** serialVersionUID */
	private static final long serialVersionUID = 3853702971329778903L;

	private final Document document;
	
	private MutableAttributeSet asDebug;
	private MutableAttributeSet asInfo;
	private MutableAttributeSet asWarn;
	private MutableAttributeSet asError;
	private MutableAttributeSet asFatal;

	public LoggerConsole() {
		setEditable(false);
		Font font = new Font("ＭＳ ゴシック", Font.PLAIN, 12);
		setFont(font);

		document = getDocument();
		
		asDebug = new SimpleAttributeSet();
		StyleConstants.setForeground(asDebug, new Color(0, 128, 0));

		asInfo = new SimpleAttributeSet();
		StyleConstants.setForeground(asInfo, new Color(0,0,0));

		asWarn = new SimpleAttributeSet();
		StyleConstants.setForeground(asWarn, new Color(0, 0, 128));

		asError = new SimpleAttributeSet();
		StyleConstants.setForeground(asError, new Color(128, 0, 0));

		asFatal = new SimpleAttributeSet();
		StyleConstants.setForeground(asFatal, new Color(128, 0, 0));
	}

	@Override
	public void debug(final String message) {
		log(message, asDebug);
	}

	@Override
	public void debug(final String message, final Throwable cause) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void info(final String message) {
		log(message, asInfo);
	}

	@Override
	public void info(final String message, final Throwable cause) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void warn(final String message) {
		log(message, asWarn);
	}

	@Override
	public void warn(final String message, final Throwable cause) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void error(final String message) {
		log(message, asError);
	}

	@Override
	public void error(final String message, final Throwable cause) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void fatal(final String message) {
		log(message, asFatal);
	}

	@Override
	public void fatal(final String message, final Throwable cause) {
		// TODO 自動生成されたメソッド・スタブ

	}

	private synchronized void log(final String message, final AttributeSet attributeSet) {
		try {
			document.insertString(document.getLength(), message+"\n", attributeSet);
		} catch (BadLocationException ex) {
			ex.printStackTrace();
		}
	}
}
