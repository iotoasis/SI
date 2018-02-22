package net.herit.business.protocol;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.web.multipart.MultipartHttpServletRequest;

import net.herit.common.conf.HeritProperties;

public class HttpConnector {
	
	private static HttpConnector instance = null;
	public static HttpConnector getInstance(){
		if(instance == null){
			instance = new HttpConnector();
		}
		return instance;
	}
	
	private HttpURLConnection conn = null;
	public HttpURLConnection getConnection(String target){
		URL url = null;
		try{
			url = new URL(target);
			conn = (HttpURLConnection)url.openConnection();
		} catch(Exception e){
			e.printStackTrace();
		}
		return conn;
	}
	
	public String sendMsg(HttpURLConnection conn, String msg){
		
		OutputStream wr = null;
		BufferedReader in = null;
		String line = null;
		StringBuffer resBody = null;
		
		try{
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Content-Length", String.valueOf(msg.length()));
			
			conn.setDoOutput(true);
			conn.setDoInput(true);

			wr = conn.getOutputStream();
			wr.write(msg.getBytes());
			wr.flush();
			
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			resBody = new StringBuffer();
		 
			while ((line = in.readLine()) != null) {
				resBody.append(line);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try{
				if(in != null){ in.close(); }
				if(wr != null){ wr.close(); }
			} catch(Exception e) {}
			
		}
		
		String resultStr;
		if(resBody == null || resBody.length() < 1) {
			resBody.append("");
			resultStr = resBody.toString();
		} else {
			resultStr = resBody.toString();
		}
		return resultStr;
	}
	
	public String sendMsg(String target, String msg){
		return sendMsg(getConnection(target), msg);
	}
	
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
	
	public void sendFileWithCurl(){
		String cmd = "curl -i 'http://10.10.0.82:7557/files/firmware.tar.gz' -X PUT --data-binary @\"C:/uploads/firmware/firmware.tar.gz\" --header \"fileType: 1 Firmware Upgrade Image\" --header \"oui: 625009\" --header \"productClass: TTB2706\" --header \"version: asdf\"";
		try{ 
			Process p = Runtime.getRuntime().exec(cmd); 
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = null;
			
			while((line = br.readLine()) != null){
				System.out.println(line); 
				}
		} catch(Exception e) {
			System.out.println(e);
		}
	}
}
