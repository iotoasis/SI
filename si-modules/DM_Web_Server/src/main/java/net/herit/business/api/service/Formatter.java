package net.herit.business.api.service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.herit.business.device.service.MoProfileVO;
import net.herit.common.conf.HeritProperties;

public class Formatter {
	
	private static Formatter instance = null;
	public static Formatter getInstance(){
		if(instance == null){
			instance = new Formatter();
		}
		return instance;
	}
	
	/*
	"res":{
		"curTime"="20170907"
		"status"=200,
		"body"={
			"r":200,
			"e":[
				{
					"n":"",
					"sv":"",
					"ti":"20170907"
				}, {
					"n":"",
					"sv":"",
					"ti":"20170907"
				}
			]
		},
		"json"={
			"r":200,
			"e":[
				{
					"n":"",
					"sv":"",
					"ti":"20170907"
				}, {
					"n":"",
					"sv":"",
					"ti":"20170907"
				}
			]
		}
	}
	*/
	public HashMap<String, Object> readToDmFormat(ArrayList<MoProfileVO> resources){
		HashMap<String, Object> form = new HashMap<String, Object>();
		
		try{
			int resCode = 200;
			JSONObject body = new JSONObject();
			JSONArray e = new JSONArray();
			for(int i=0; i<resources.size(); i++){
				JSONObject e_o = new JSONObject();
				e_o.put("n", resources.get(i).getResourceUri());
				e_o.put("sv", resources.get(i).getData());
				e_o.put("ti", resources.get(i).getCreateTime());
				e.put(e_o);
			}
			body.put("r", resCode);
			body.put("e", e);
			
			ObjectMapper mapper = new ObjectMapper();
			Object json = mapper.readValue(body.toString(), Object.class);
			
			form.put("curTime", System.currentTimeMillis());
			form.put("status", resCode);
			form.put("body", body);
			form.put("json", json);
		} catch(Exception e) {
			e.printStackTrace();
			form = null;
		}
		
		return form;
	}
	public HashMap<String, Object> writeToDmFormat(JSONObject result, HttpURLConnection conn) {
		HashMap<String, Object> form = new HashMap<String, Object>();
		
		try{
			JSONObject body = new JSONObject();
			JSONArray e = new JSONArray();
			
			JSONObject e_o = new JSONObject();
			JSONArray parameterValues = result.getJSONArray("parameterValues");
			e_o.put("n", parameterValues.getJSONArray(0).getString(0));
			e_o.put("sv", parameterValues.getJSONArray(0).getString(1));
			e_o.put("ti", result.getString("timestamp"));
			e.put(e_o);
			
			body.put("r", conn.getResponseCode());
			body.put("e", e);
			
			ObjectMapper mapper = new ObjectMapper();
			Object json = mapper.readValue(body.toString(), Object.class);
			
			form.put("curTime", System.currentTimeMillis());
			form.put("status", conn.getResponseCode());
			form.put("body", body);
			form.put("json", json);
		} catch(Exception e) {
			e.printStackTrace();
			form = null;
		}
		
		return form;
	}
	
	
	//{ "name": "setParameterValues", "parameterValues": [["InternetGatewayDevice.ManagementServer.UpgradesManaged",false]] }
	public JSONObject toACSFormat(JSONObject parameter){
		JSONObject result = new JSONObject();
		result.put("name", "setParameterValues");
		
		Object[][] parameterValues = new Object[1][2];
		if(parameter.getJSONArray("e").getJSONObject(0).get("sv") instanceof Boolean){
			
		} else if(parameter.getJSONArray("e").getJSONObject(0).get("sv") instanceof String) {
			
		} else if(parameter.getJSONArray("e").getJSONObject(0).get("sv") instanceof Integer) {
		
		}
		parameterValues[0][0] = parameter.getJSONArray("e").getJSONObject(0).get("n");
		parameterValues[0][1] = parameter.getJSONArray("e").getJSONObject(0).get("sv");
		result.put("parameterValues", parameterValues);
		return result;
	}
	
	public String getAcsUrl(String target){
		StringBuffer url = new StringBuffer("http://");
		url.append(HeritProperties.getProperty("Globals.tr069ServerHost")).append(":").append(HeritProperties.getProperty("Globals.tr069ServerPort"));
		url.append("/devices/").append(target).append("/tasks?timeout=3000&connection_request");
		return url.toString();
	}
	
	public String getTR069DeviceIdFromDm(String deviceId){
		return deviceId.replace("_", "-");
	}
	public String getTR069DeviceIdToDm(String deviceId){
		return deviceId.replace("-", "_");
	}
	public String getTR069ResourceUriFromDm(String uri){
		return uri.replace("/", ".");
	}
	public String getTR069ResourceUriToDm(String uri){
		return uri.replace(".", "/");
	}
	
}
