package net.herit.business.mobius.converter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.apache.poi.ss.formula.functions.T;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.herit.business.onem2m.resource.OneM2MResource;

public class JSONConverter<T extends OneM2MResource> {
	private T resource = null;
	
	public JSONConverter(T resource){
		this.resource = resource;
	}
	
	public JSONObject convert(){
		
		JSONObject body_wrap = new JSONObject();
		JSONObject body = new JSONObject();
		JSONArray lbl = new JSONArray();
		String shortName = null;

		try {
			if( resource.getLabelList().size() > 0 ){
				for(String label : resource.getLabelList()) {
					lbl.put(label);
				}
				body.put("lbl", lbl);
			}

			Field[] fields= resource.getClass().getDeclaredFields();
			for(Field field : fields){
				if( !field.getName().equals("shortName") ){	
					StringBuffer methodName = new StringBuffer(field.getType()==boolean.class?"is":"get");
					methodName.append(field.getName().substring(0, 1).toUpperCase());
					methodName.append(field.getName().substring(1));
					Method mtd = resource.getClass().getMethod(methodName.toString());
					body.put(field.getName(), mtd.invoke(resource));
				} else {
					Method mtd = resource.getClass().getMethod("getShortName");
					shortName = (String) mtd.invoke(resource);
				}
				//System.out.println("########## "+field.getName() + "("+field.getType()+")");
			}

			body_wrap.put(resource.nameSpace+shortName, body);

		} catch(JSONException e) {
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return body_wrap;

	}
}
