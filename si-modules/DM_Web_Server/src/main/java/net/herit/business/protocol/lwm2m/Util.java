package net.herit.business.protocol.lwm2m;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.herit.business.protocol.lwm2m.resource.ResourceVO;

public class Util {
	
	// is Null Or Empty
	public static boolean isNOE(String txt){
		boolean result = false;
		if(txt == null || txt.replaceAll(" ", "").equals("")){
			result = true;
		}
		return result;
	}
	
	public static String makeDeviceId(JSONObject token){
		
		StringBuffer deviceId = new StringBuffer();
		
		try {
			deviceId.append(token.get("oui")).append("_");
			deviceId.append(token.get("modelName")).append("_");
			deviceId.append(token.get("sn"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		
		return deviceId.toString();
	}
	
	public static List<String[]> getUriAndName(JSONObject token){
		
		List<String[]> list = new ArrayList<String[]>();
		
		try {
			JSONArray objModels = token.getJSONArray("objectModels");
			for (int i = 0; i < objModels.length(); i++) {
				StringBuffer uriBase = new StringBuffer("/");
				uriBase.append(objModels.getJSONObject(i).get("id")).append("/-/");
				JSONArray resources = objModels.getJSONObject(i).getJSONArray("resources");
				for (int j = 0; j < resources.length(); j++) {
					StringBuffer uri = new StringBuffer(uriBase);
					uri.append(resources.getJSONObject(j).get("id"));
					
					String[] vo = new String[2];
					vo[0] = uri.toString();
					vo[1] = resources.getJSONObject(j).get("name").toString();
					list.add(vo);
				}
			}			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return list;
	}
	
	public static HashMap<String, String> jsonToMap(JSONObject token){
		HashMap<String, String> map = new HashMap<String, String>();
		Iterator<String> keys = token.keySet().iterator();
		while(keys.hasNext()){
			String key = keys.next();
			map.put(key, token.getString(key));
		}
		return map;
	}
	
	
	
	
	public static String getDataType(String txt){
		String result = "";
		String elseTxt = "";
		
		if("integer".equals(txt)){
			result = "I";
		} else if("string".equals(txt)){
			result = "S";
		} else if("boolean".equals(txt)){
			result = "B";
		} else if("float".equals(txt)){
			result = "F";
		} else if("long".equals(txt)){
			result = "L";
		} else if("double".equals(txt)){
			result = "D";
		} else if("time".equals(txt)){
			result = "T";
		} else if("opaque".equals(txt)){
			elseTxt = txt;
		} else {
			elseTxt = txt;
		}
		//System.out.println(result);
		//System.out.println(elseTxt);
		
		return result;
	}

}
