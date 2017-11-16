package net.herit.business.protocol;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONObject;

public class HttpOperator {
	
	// send get with base header
	public HttpURLConnection sendGet(String strUrl){
		
		URL url = null;
		HttpURLConnection conn = null;
		
		try {
			url = new URL(strUrl);
			conn = (HttpURLConnection)url.openConnection();
			
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return conn;
	}
		
	// send post with base header
	public HttpURLConnection sendPost(String strUrl, JSONObject data){
		
		URL url = null;
		HttpURLConnection conn = null;
		OutputStream os = null;
		
		try {
			url = new URL(strUrl);
			conn = (HttpURLConnection)url.openConnection();
			
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			
			os = conn.getOutputStream();
			os.write(data.toString().getBytes());
			os.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try{
				if(os != null){
					os.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return conn;
	}
	
	// send post with onem2m header
	public HttpURLConnection sendPost(String strUrl, JSONObject data, HashMap<String, String> header){
		
		URL url = null;
		HttpURLConnection conn = null;
		OutputStream os = null;
		
		try {
			url = new URL(strUrl);
			conn = (HttpURLConnection)url.openConnection();
			
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			if(header != null){
				// header print(출력)
				Util.printMap(header);
				Iterator<String> it = header.keySet().iterator();
				while(it.hasNext()){
					String key = it.next();
					String value = header.get(key);
					conn.addRequestProperty(key, value);
				}
			}
			
			os = conn.getOutputStream();
			os.write(data.toString().getBytes());
			os.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try{
				if(os != null){
					os.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return conn;
	}
	
	
	// get response from the server
	public JSONObject getResponse(HttpURLConnection conn){
		
		JSONObject result = null;
		InputStream is = null;
		ByteArrayOutputStream baos = null;
			
	    try{
	    	int responseCode = conn.getResponseCode();	    	
	
	        if (responseCode == HttpURLConnection.HTTP_OK) {
	            is = conn.getInputStream();
	            baos = new ByteArrayOutputStream();
	            byte[] byteBuffer = new byte[1024];
	            byte[] byteData = null;
	            int nLength = 0;
	            while ((nLength = is.read(byteBuffer, 0, byteBuffer.length)) != -1) {
	                baos.write(byteBuffer, 0, nLength);
	            }
	            byteData = baos.toByteArray();
	            result = new JSONObject(new String(byteData));
	        } else {
	            result = new JSONObject();
	            result.put("RESPONSE-CODE", String.valueOf(responseCode));
	        }
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(is != null){
					is.close();
				}
				if(baos != null){
					baos.close();
				}
				if(conn != null){
					conn.disconnect();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

        return result;
	}
}
