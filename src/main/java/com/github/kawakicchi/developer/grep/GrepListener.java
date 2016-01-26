package com.github.kawakicchi.developer.grep;

public interface GrepListener {

	public void grepStart(final GrepEvent e);
	
	public void grepStop(final GrepEvent e);
}
