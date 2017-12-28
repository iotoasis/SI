package net.herit.iot.onem2m.bind.http.util;

import java.util.Map.Entry;

public class QueryEntry implements Entry {
	
	public final static String TO_NAME = "_to";
	
	private String key;
	private String value;
	
	public QueryEntry(String key, String value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public String getKey() {
		return this.key;
	}

	@Override
	public Object getValue() {
		return this.value;
	}

	@Override
	public Object setValue(Object value) {
		this.value = (String)value;
		return this.value;
	}
}