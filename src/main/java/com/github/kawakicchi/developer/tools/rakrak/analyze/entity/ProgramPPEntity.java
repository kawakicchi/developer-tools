package com.github.kawakicchi.developer.tools.rakrak.analyze.entity;

public class ProgramPPEntity {

	private String page;
	private ProgramOptionEntity option;

	public void setPage(final String value) {
		page = value;
	}
	
	public String getPage() {
		return page;
	}
	
	public void setOption(final ProgramOptionEntity value) {
		option = value;
	}
	
	public ProgramOptionEntity getOption() {
		return option;
	}
}
