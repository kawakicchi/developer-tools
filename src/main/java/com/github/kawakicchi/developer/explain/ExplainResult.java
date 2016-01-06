package com.github.kawakicchi.developer.explain;

public class ExplainResult {

	private StringBuffer console;
	private String crlf;
	
	public ExplainResult () {
		console = new StringBuffer();
		
		crlf = "\n";
		try {
			crlf = System.getProperty("line.separator");
		} catch(SecurityException e) {
		}
	}
	
	
	public void print(final String message) {
		console.append(message);
	}

	public void println(final String message) {
		console.append(message);
		console.append(crlf);
	}

	public String getLog() {
		return console.toString();
	}
}
