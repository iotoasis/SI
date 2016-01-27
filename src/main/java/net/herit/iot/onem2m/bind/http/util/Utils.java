package net.herit.iot.onem2m.bind.http.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Utils {
	
	public static final String TO_NAME = "_to_";
	
	public static HashMap<String, Object> urlQueryParse(String url) {
		
		HashMap<String, Object> map = new HashMap<String, Object>();

		int indx = url.indexOf('?');
		if (indx == -1) {
			if (url.startsWith("http:")) url = url.substring(5);
			if (url.startsWith("https:")) url = url.substring(6);
			map.put(TO_NAME, url);
			return map;
		}

		if (url.startsWith("http:")) {
			map.put(TO_NAME, url.substring(5, indx));
		} else if (url.startsWith("https:")) {
			map.put(TO_NAME, url.substring(6, indx));
		} else {
			map.put(TO_NAME, url.substring(0, indx));
		}
		

		String query = url.substring(indx+1);
		String[] params = query.split("&");
		
		for (String param : params) {
			String[] split = param.split("=");
			
			if(split.length == 2) {
				Object val = map.get(split[0]);
				if (val == null || val.equals(split[0])) {
					map.put(split[0], split[1]);
				} else if (val instanceof String) {
					List<String> list = new ArrayList<String>();
					list.add((String)val);
					list.add(split[1]);
					map.put(split[0],  list);
				} else { 	//if (val instanceof ArrayList<?>) {
					List<String> list = (ArrayList<String>)val;
					list.add(split[1]);
				}
			}
	    }
		return map;
	}
}
