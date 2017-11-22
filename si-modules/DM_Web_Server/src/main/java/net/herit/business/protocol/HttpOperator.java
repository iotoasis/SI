package net.herit.business.protocol;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import net.herit.business.protocol.lwm2m.exception.JsonFormatException;
import net.herit.common.conf.HeritProperties;

public class HttpOperator {
	
	// send get with base header
	public HttpURLConnection sendGet(String strUri){
		
		URL url = null;
		HttpURLConnection conn = null;
		
		try {
			url = new URL(strUri);
			conn = (HttpURLConnection)url.openConnection();
			
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return conn;
	}
	
	// send get with oneM2M header
	public HttpURLConnection sendGet(String strUri, HashMap<String, String> header){
		
		URL url = null;
		HttpURLConnection conn = null;
		
		try {
			url = new URL(strUri);
			conn = (HttpURLConnection)url.openConnection();
			
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			if(header != null){
				// header print(출력)
				// Util.printMap(header);
				Iterator<String> it = header.keySet().iterator();
				while(it.hasNext()){
					String key = it.next();
					String value = header.get(key);
					conn.addRequestProperty(key, value);
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return conn;
	}
		
	// send post with base header
	public HttpURLConnection sendPost(String strUri, JSONObject data){
		
		URL url = null;
		HttpURLConnection conn = null;
		OutputStream os = null;
		
		try {
			url = new URL(strUri);
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
	public HttpURLConnection sendPost(String strUri, JSONObject data, HashMap<String, String> header){
		
		URL url = null;
		HttpURLConnection conn = null;
		OutputStream os = null;
		
		try {
			url = new URL(strUri);
			conn = (HttpURLConnection)url.openConnection();
			
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			if(header != null){
				// header print(출력)
				// Util.printMap(header);
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
	
	// send delete with onem2m header
	public HttpURLConnection sendLongPolling(String strUri, HashMap<String, String> header){
		
		URL url = null;
		HttpURLConnection conn = null;
		OutputStream os = null;
		
		try {
			url = new URL(strUri);
			conn = (HttpURLConnection)url.openConnection();
			
			conn.setRequestMethod("DELETE");
			conn.setDoInput(true);
	        conn.setDoOutput(false);
			if(header != null){
				// header print(출력)
				// Util.printMap(header);
				Iterator<String> it = header.keySet().iterator();
				while(it.hasNext()){
					String key = it.next();
					String value = header.get(key);
					conn.addRequestProperty(key, value);
				}
			}
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
	
	// send file
	public String sendFile(MultipartHttpServletRequest mptRequest) {
		
		int fileType = Integer.parseInt(mptRequest.getParameter("fileType").substring(0, mptRequest.getParameter("fileType").indexOf(" ")));
		StringBuffer result = new StringBuffer();
		String fileName = mptRequest.getFile("packageName").getOriginalFilename();
		String uploadFile = null;
		if(fileType == 1){
			uploadFile = HeritProperties.getProperty("Globals.firmwareDir") + fileName;
		} else {
			uploadFile = HeritProperties.getProperty("Globals.tomcatDir") + HeritProperties.getProperty("Globals.uploadDir") + fileName;
		}
		String actionUrl = "http://"+HeritProperties.getProperty("Globals.tr069ServerHost")+":"+HeritProperties.getProperty("Globals.tr069ServerPort")+"/files/"+mptRequest.getFile("packageName").getOriginalFilename();
		String end = "\r\n";
		String boundary = Long.toHexString(System.currentTimeMillis());

		URL url = null;
		HttpURLConnection conn = null;
		DataOutputStream dos = null;
		FileInputStream fis = null;
		
		try{
			url = new URL(actionUrl);
			conn = (HttpURLConnection)url.openConnection();
			
			// set option
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("PUT");
			
			// set header
			conn.setRequestProperty("fileType", mptRequest.getParameter("fileType"));
			conn.setRequestProperty("oui", mptRequest.getParameter("oui"));
			conn.setRequestProperty("productClass", mptRequest.getParameter("productClass"));
			conn.setRequestProperty("version", mptRequest.getParameter("version"));
			conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="+ boundary);
			
			//set I/O
			dos = new DataOutputStream(conn.getOutputStream());
			fis = new FileInputStream(uploadFile);
			
			// set buffer
			int bufferSize = 1024;
			byte[] buffer = new byte[bufferSize];
			int length = -1;
			
			// write
			while((length = fis.read(buffer)) != -1) {
				dos.write(buffer, 0, length);
			}
			dos.writeBytes(end);
			dos.flush();
			dos.close();
			
			// log
			System.out.println(" ::::::::::::::::::: "+conn.getResponseCode());
			
			// get result
			InputStream is = conn.getInputStream();
			byte[] data = new byte[bufferSize];
			int leng = -1;
			while((leng = is.read(data)) != -1) {
				result.append(new String(data, 0, leng));
			}
			result.append("resultCode : ");
			result.append(conn.getResponseCode());
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(fis != null){
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(conn != null){
				conn.disconnect();
			}
		}
		
		return result.toString();
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
				if(baos != null){
					baos.close();
				}
				if(is != null){
					is.close();
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
	
	
	public String getResponseString(HttpURLConnection conn){
    	
        String result = null;
        InputStream is = null;
		ByteArrayOutputStream baos = null;
		
        try{
	        int responseCode = conn.getResponseCode();
	      
	        if (responseCode >= HttpURLConnection.HTTP_OK && responseCode <= 202) {
	            is = conn.getInputStream();
	            baos = new ByteArrayOutputStream();
	            byte[] byteBuffer = new byte[1024];
	            int nLength = 0;
	            while ((nLength = is.read(byteBuffer, 0, byteBuffer.length)) != -1) {
	                baos.write(byteBuffer, 0, nLength);
	            }
	            result = new String(baos.toByteArray());
	        } else {
	            JSONObject job = new JSONObject();
	            job.put("RESPONSE-CODE", String.valueOf(responseCode));
	
	            result = job.toString();
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
	
	
	// get request by json from the http request
	public JSONObject getParamFromRequest(HttpServletRequest request) throws JsonFormatException{
		
		JSONObject result = null;
		InputStream is = null;
		ByteArrayOutputStream baos = null;
		
		try{
			is = request.getInputStream();
			baos = new ByteArrayOutputStream();
			byte[] byteBuffer = new byte[1024];
			byte[] byteData;
			int nLength = 0;
			
			while((nLength=is.read(byteBuffer,0,byteBuffer.length)) != -1){
				baos.write(byteBuffer,0,nLength);
			}
			byteData = baos.toByteArray();
			result = new JSONObject(new String(byteData));
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(baos != null){
					baos.close();
				}
				if(is != null){
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println(result);
		return result;
	}
}
