package net.herit.business.protocol.lwm2m;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.herit.business.protocol.constant.KeyName;
import net.herit.business.protocol.constant.Target;

public class LWM2MKeyExtractor {
	
	// deviceId
	public String getDeviceId(JSONObject token){
		return getDeviceId(token, Target.DM);
	}
	public String getDeviceId(JSONObject token, Target to){
		StringBuffer deviceId = new StringBuffer();
		
		try {
			if(to == Target.DM){
				deviceId.append(token.get("oui")).append("_");
				deviceId.append(token.get("modelName")).append("_");
				deviceId.append(token.get("sn"));
			} else if(to == Target.IPE) {
				deviceId.append(token.get("authId"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return deviceId.toString();
	}
	
	// DM authId, authPwd 불러오기
	public String getAuthId(JSONObject token){
		return token.getString("authId");
	}
	public String getAuthPwd(JSONObject token){
		return token.getString("authPwd");
	}
	
	// 필수 항목 불러오기
	public String getKeyFromId(String deviceId, KeyName keyName){
		String result = null;
		String[] idSplit = deviceId.split("_");
		if(idSplit.length == 0){
			idSplit = deviceId.split("_");
		}
		switch(keyName){
		case MODEL_NAME:
			result = idSplit[1];
			break;
		case MANUFACTURER_OUI:
			result = idSplit[0];
			break;
		case SERIAL_NUMBER:
			result = idSplit[2];
			break;
		}
		return result;
	}
	
	// lwm2m resource uri 불러오기
	public List<String[]> getUriAndName(JSONObject token){
		
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
			e.printStackTrace();
		}
		
		return list;
	}
}
