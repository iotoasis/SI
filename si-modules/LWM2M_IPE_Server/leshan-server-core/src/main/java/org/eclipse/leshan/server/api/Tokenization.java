package org.eclipse.leshan.server.api;

import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.leshan.server.client.Client;
import org.json.JSONArray;
import org.json.JSONObject;

public class Tokenization {
	
	public static JSONObject makesBasicToken(String authId, String connect){
		// 기본 인스턴스 생성
		JSONObject result = null;
		
		try{
			result = new JSONObject();
			result.put("conn", connect);
			
			HashMap<String, String> deviceInfo = Connector.getInstance().getDeviceInfo(authId);
			Iterator<String> resources = deviceInfo.keySet().iterator();
			while(resources.hasNext()){
				try{
					String resource = resources.next();
					String value = deviceInfo.get(resource);
					result.put(resource, value);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		} catch(Exception e) {}
		
		
		return result;
	}
	
	
	public static JSONObject makesTokenForConnect(String authId, String authPwd, Client client){
		
		// 기본 인스턴스 생성
		JSONObject result = null;
		
		try{
			
			result = makesBasicToken(authId, "connect");
			
			HashMap<String, String> deviceInfo = Connector.getInstance().getDeviceInfo(authId);
			Iterator<String> resources = deviceInfo.keySet().iterator();
			while(resources.hasNext()){
				try{
					String resource = resources.next();
					String value = deviceInfo.get(resource);
					result.put(resource, value);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
			
			String[] uris = Util.getUri(client.getObjectLinks());
			JSONArray objectModels = new JSONArray();
			for (String uri : uris) {
				JSONObject uriWrap = new JSONObject();
				uriWrap.put("uri",uri);
				objectModels.put(uriWrap);
			}
			result.put("objectModels", objectModels);
			
			// 기본 속성 : 인증 속성
			result.put("authId", authId);
			result.put("authPwd", authPwd);
			
		} catch(Exception e) {}
			
		return result;
	}
	
	public static JSONObject makesTokenForDisconnect(String authId, Client client){
		
		// 기본 인스턴스 생성
		JSONObject result = null;
		
		try{
			
			result = makesBasicToken(authId, "disconnect");
			
			HashMap<String, String> deviceInfo = Connector.getInstance().getDeviceInfo(authId);
			Iterator<String> resources = deviceInfo.keySet().iterator();
			while(resources.hasNext()){
				try{
					String resource = resources.next();
					String value = deviceInfo.get(resource);
					result.put(resource, value);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
			
			String[] uris = Util.getUri(client.getObjectLinks());
			JSONArray objectModels = new JSONArray();
			for (String uri : uris) {
				JSONObject uriWrap = new JSONObject();
				uriWrap.put("uri",uri);
				objectModels.put(uriWrap);
			}
			result.put("objectModels", objectModels);
			
			// 기본 속성 : 인증 속성
			result.put("authId", authId);
			
		} catch(Exception e) {}
			
		return result;
	}
}
