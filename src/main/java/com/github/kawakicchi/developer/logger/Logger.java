package com.github.kawakicchi.developer.logger;

public interface Logger {

	public void debug(final String message);
	
	public void debug(final String message, final Throwable cause);
	
	public void info(final String message);
	
	public void info(final String message, final Throwable cause);

	public void warn(final String message);
	
	public void warn(final String message, final Throwable cause);

	public void error(final String message);
	
	public void error(final String message, final Throwable cause);

	public void fatal(final String message);
	
	public void fatal(final String message, final Throwable cause);
}
