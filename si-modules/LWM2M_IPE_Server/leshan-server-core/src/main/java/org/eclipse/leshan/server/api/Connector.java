package org.eclipse.leshan.server.api;

import java.io.*;
import java.net.*;
import java.util.HashMap;

import org.eclipse.leshan.server.Constants;
import org.eclipse.leshan.server.Lwm2mServerConfig;
import org.json.JSONObject;

public class Connector {
	
	private Lwm2mServerConfig config = Lwm2mServerConfig.getInstance();
	private static Connector instance;	
	private URL url;
	private String dmHost = config.getDmAddress();
	private int dmPort = config.getDmPort();
	
	private HttpURLConnection conn;
	private OutputStream os;
	private InputStream is;
	
	private ByteArrayOutputStream baos;
	private int responseCode;
	
	public Connector(){
		try {
			url = new URL("http://"+dmHost+":"+dmPort+"/hdm/lwm2m/conn.do");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	// singleton
	public static Connector getInstance(){
		if(instance == null){
			instance = new Connector();
		}
		return instance;
	}
	
	
	// request
	public void sendRequest(JSONObject token){
		try {
			os = conn.getOutputStream();
			os.write(token.toString().getBytes());
			os.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// response
	public JSONObject getResponse(){
		String response;
		JSONObject result = null;
			
	    try{
	    	responseCode = conn.getResponseCode();	    	
	
	        if (this.responseCode == HttpURLConnection.HTTP_OK) {
	            is = conn.getInputStream();
	            baos = new ByteArrayOutputStream();
	            byte[] byteBuffer = new byte[1024];
	            byte[] byteData = null;
	            int nLength = 0;
	            while ((nLength = is.read(byteBuffer, 0, byteBuffer.length)) != -1) {
	                baos.write(byteBuffer, 0, nLength);
	            }
	            byteData = baos.toByteArray();
	            response = new String(byteData);
	            
	            result = new JSONObject(response);
	        } else {
	            JSONObject job = new JSONObject();
	            job.put("RESPONSE-CODE", String.valueOf(this.responseCode));
	
	            result = job;
	        }
		} catch(Exception e) {
			
		} finally {
			disconnect();
		}
	    

        return result;
	}
	
	// init:connect
	public Connector connect(){
		try{	
			conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			
			conn.setDoOutput(true);
			conn.setDoInput(true);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return this;
	}
	
	public void disconnect(){
		conn.disconnect();
	}
	
	
	/**
	 * device connectivity status
	 * 
	 */
	
	public HashMap<String, String> getDeviceInfo(String deviceId){
		String baseUrl = "http://"+config.getLocalAddress()+":"+config.getWebPort()+"/api/hdm/"+deviceId;
		
		HashMap<String, String> deviceInfo = new HashMap<String, String>(); 
		String oui = "000001";
		String[] resources = new String[]{"manufacturer","modelName","sn"};
		
		for (int i = 0; i < 3; i++) {
			try{
				URL url = new URL(baseUrl+"/3/0/"+i);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				
				is = conn.getInputStream();
	            baos = new ByteArrayOutputStream();
	            byte[] byteBuffer = new byte[1024];
	            byte[] byteData = null;
	            int nLength = 0;
	            while ((nLength = is.read(byteBuffer, 0, byteBuffer.length)) != -1) {
	                baos.write(byteBuffer, 0, nLength);
	            }
	            byteData = baos.toByteArray();
	            deviceInfo.put(resources[i], new JSONObject(new String(byteData)).getJSONObject("content").getString("value"));
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		try{
			baos.close();
			is.close();
			conn.disconnect();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		deviceInfo.put("oui", oui);
		
		return deviceInfo;
		
	}
	
		
}
