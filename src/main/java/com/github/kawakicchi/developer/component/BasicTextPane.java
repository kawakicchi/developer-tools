package com.github.kawakicchi.developer.component;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.JTextComponent;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.TabSet;
import javax.swing.text.TabStop;

public class BasicTextPane extends JTextPane {

	/** serialVersionUID */
	private static final long serialVersionUID = -699085174071789314L;

	// 
	private static final Color lineColor1 = new Color(255, 255, 255);
	private static final Color lineColor2 = new Color(248, 248, 248);

	// 行ハイライト
	private static final Color lineCaretColor = new Color(232, 242, 254);
	private final DefaultCaret caret;

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

		// 行ハイライト
		caret = new DefaultCaret() {
			/** serialVersionUID */
			private static final long serialVersionUID = -5227694834003006790L;

			@Override
			protected synchronized void damage(Rectangle r) {
				if (r != null) {
					JTextComponent c = getComponent();
					x = 0;
					y = r.y;
					width = c.getSize().width;
					height = r.height;
					c.repaint();
				}
			}
		};
		caret.setBlinkRate(getCaret().getBlinkRate());
		setCaret(caret);

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

	// 行ハイライト
	@Override
	protected void paintComponent(final Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		Insets insets = getInsets();

		int y = insets.top;
		int h = caret.height;
		int w = getWidth();

		int i = 0;
		if (0 < h) {
			while (y < getHeight()) {
				if (0 == i % 2) {
					g2.setColor(lineColor1);
				} else {
					g2.setColor(lineColor2);
				}
				g2.fillRect(0, y, w, h);
				y += h;
				i++;
			}
		}

		y = caret.y;
		g2.setPaint(lineCaretColor);
		g2.fillRect(0, y, w, h);

		//System.out.println(String.format("x: %d; y: %d; width: %d; height: %d", getX(), getY(), getWidth(), getHeight()));
		super.paintComponent(g);
	}

	public void setText(final String text) {
		super.setText(text);
		doChangeText();
	}

	protected void doChangeText() {
	}
}
