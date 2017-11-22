package org.eclipse.leshan.server.extension;

import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.leshan.server.Lwm2mServerConfig;
import org.eclipse.leshan.server.client.Client;
import org.eclipse.leshan.server.extension.dm.handler.DmOperator;
import org.eclipse.leshan.util.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Tokenization {
	
	// device info token
	public JSONObject makeDeviceInfoToken(Lwm2mVO vo) throws JSONException{
		
		JSONObject result = null;
		DmOperator dmOperator = new DmOperator();
		
		result = new JSONObject();
		HashMap<String, String> deviceInfo = dmOperator.getDeviceInfo(vo.getAuthId());
		Iterator<String> resources = deviceInfo.keySet().iterator();
		while(resources.hasNext()){
			String resource = resources.next();
			String value = deviceInfo.get(resource);
			result.put(resource, value);
		}
			
		result.put("authId", vo.getAuthId());
		result.put("authPwd", vo.getAuthPwd());
			
		return result;
	}
	
	// Using dm token when device connects, disconnects ..  
	public JSONObject makeDmToken(Lwm2mVO vo, String strConnect) throws JSONException{
		
		JSONObject result = makeDeviceInfoToken(vo);
		String[] uris = Util.getUri(vo.getClient().getObjectLinks());
		JSONArray objectModels = new JSONArray();
		for (String uri : uris) {
			JSONObject uriWrap = new JSONObject();
			uriWrap.put("uri",uri);
			objectModels.put(uriWrap);
		}
		result.put("conn", strConnect);
		result.put("objectModels", objectModels);
		
		return result;
	}
	
	
	
	
	
	
	
	
	
	public static JSONObject makesBasicToken(String authId, String connect){
		// 기본 인스턴스 생성
		JSONObject result = null;
		
		try{
			result = new JSONObject();
			result.put("conn", connect);
			DmOperator dmOperator = new DmOperator();
			HashMap<String, String> deviceInfo = dmOperator.getDeviceInfo(authId);
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
	
	public static String getDeviceInfoEncoded(String authId){
		
		String result = null;
		JSONObject info = new JSONObject();
		DmOperator dmOperator = new DmOperator();
		HashMap<String, String> deviceInfo = dmOperator.getDeviceInfo(authId);
		Iterator<String> resources = deviceInfo.keySet().iterator();
		while(resources.hasNext()){
			try{
				String resource = resources.next();
				String value = deviceInfo.get(resource);
				info.put(resource, value);
			} catch(Exception e) {
				System.out.println(e.getMessage());
				if(Lwm2mServerConfig.getInstance().isDebug()){
					e.printStackTrace();
				}
			}
		}
		
		try{
			//info.put("authId", authId);
			//info.put("authPwd", authPwd);
			
			result = Base64.encodeBase64String(info.toString().getBytes());
			System.out.println(result);
		} catch(Exception e) {
			System.out.println(e.getMessage());
			if(Lwm2mServerConfig.getInstance().isDebug()){
				e.printStackTrace();
			}
		}
			
		return result;
	}
	
	
	public static JSONObject makesTokenForConnect(String authId, String authPwd, Client client){
		
		JSONObject result = null;
		
		try{
			result = makesBasicToken(authId, "connect");
			DmOperator dmOperator = new DmOperator();
			HashMap<String, String> deviceInfo = dmOperator.getDeviceInfo(authId);
			Util.printMap(deviceInfo);
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
			
			System.out.println("reseult = "+result);
			
		} catch(Exception e) {}
			
		return result;
	}
	
	public static JSONObject makesTokenForDisconnect(String authId, Client client){
		
		// 기본 인스턴스 생성
		JSONObject result = null;
		
		try{
			
			result = makesBasicToken(authId, "disconnect");
			DmOperator dmOperator = new DmOperator();
			HashMap<String, String> deviceInfo = dmOperator.getDeviceInfo(authId);
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
