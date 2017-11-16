package org.eclipse.leshan.server.extension.dm.handler;

import java.net.*;
import java.util.HashMap;

import org.eclipse.leshan.server.Lwm2mServerConfig;
import org.eclipse.leshan.server.extension.Constants;
import org.eclipse.leshan.server.extension.HttpOperator;
import org.json.JSONException;
import org.json.JSONObject;

public class DmOperator {
		
	public HashMap<String, String> getDeviceInfo(String deviceId){
		
		StringBuffer strUrl = new StringBuffer("http://");
		strUrl.append(Lwm2mServerConfig.getInstance().getIpeIp()).append(":");
		strUrl.append(Lwm2mServerConfig.getInstance().getIpePortWeb()).append("/api/hdm/");
		strUrl.append(deviceId);
		
		HashMap<String, String> deviceInfo = new HashMap<String, String>();
		String[] resources = new String[]{"manufacturer","modelName","sn"};
		
		HttpOperator operator = new HttpOperator();
		HttpURLConnection conn = null;
		JSONObject response = null;
		
		for (int i = 0; i < 3; i++) {
			try {
				conn = operator.sendGet(strUrl.toString()+"/3/0/"+i);
				response = operator.getResponse(conn);
				deviceInfo.put(resources[i], response.getJSONObject("content").getString("value"));
			} catch (JSONException e) {
				System.err.println(e.getMessage());
				if(Lwm2mServerConfig.getInstance().isDebug()){
					e.printStackTrace();
				}
			}
		}
		deviceInfo.put("oui", Constants.OUI);
		
		return deviceInfo;
	}
	
		
}
