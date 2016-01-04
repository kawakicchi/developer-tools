package com.github.kawakicchi.developer.tools.rakrak.analyze;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 
 * @author Kawakicchi
 */
public final class SystemProperties {

	private Set<String> xpds;
	private Set<String> xwds;
	private Set<String> xmds;
	private Set<String> xdds;

	public SystemProperties() {
		xpds = new HashSet<String>();
		xwds = new HashSet<String>();
		xmds = new HashSet<String>();
		xdds = new HashSet<String>();
	}

	public void addXPD(final String name) {
		xpds.add(name);
	}

	public void addXPDList(final List<String> names) {
		xpds.addAll(names);
	}

	public List<String> getXPDList() {
		return toList(xpds);
	}

	public void addXWD(final String name) {
		xwds.add(name);
	}

	public void addXWDList(final List<String> names) {
		xwds.addAll(names);
	}

	public List<String> getXWDList() {
		return toList(xwds);
	}

	public void addXMD(final String name) {
		xmds.add(name);
	}

	public void addXMDList(final List<String> names) {
		xmds.addAll(names);
	}

	public List<String> getXMDList() {
		return toList(xmds);
	}

	public void addXDD(final String name) {
		xdds.add(name);
	}

	public void addXDDList(final List<String> names) {
		xdds.addAll(names);
	}

	public List<String> getXDDList() {
		return toList(xdds);
	}

	private List<String> toList(final Set<String> set) {
		List<String> list = new ArrayList<String>();
		Iterator<String> i = set.iterator();
		while (i.hasNext()) {
			String value = i.next();
			list.add(value);
		}
		return list;
	}
}