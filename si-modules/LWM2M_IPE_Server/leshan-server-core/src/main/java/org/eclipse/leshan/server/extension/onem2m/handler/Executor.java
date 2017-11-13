package org.eclipse.leshan.server.extension.onem2m.handler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.eclipse.leshan.server.Lwm2mServerConfig;
import org.json.JSONObject;

public class Executor {
	
	// return value
	private String result = null;
	
	// i/o
	private HttpURLConnection conn = null;
	private InputStream in = null;
	private OutputStream out = null;
	private ByteArrayOutputStream bout = null;
	
	// constructor
	private int number = 0;
	private JSONObject item = null;
	public Executor(JSONObject item, int number){
		this.item = item;
		this.number = number;
	}

	public String execute(){

		try{
			StringBuffer uri = new StringBuffer("http://");
			uri.append(Lwm2mServerConfig.getInstance().getIpeIp()).append(":");
			uri.append(Lwm2mServerConfig.getInstance().getIpePortWeb()).append("/api/clients/");
			uri.append(item.getString("authId")).append(item.getString("resourceUri"));
			
			/*
			if(item.getOperation().equals("observe")){
				strUrl += "/observe";
			}//*/
			
			if(Lwm2mServerConfig.getInstance().isDebug()){				
				System.out.println(uri.toString());
			}
			URL url = new URL(uri.toString());
			conn = (HttpURLConnection)url.openConnection();
			conn.setRequestProperty("Content-Type", "text/plain");
			
			if( "read".equals(item.getString("operation")) ){
				conn.setRequestMethod("GET");
			} else if( "execute".equals(item.getString("operation")) ){
				conn.setRequestMethod("PUT");
				conn.setDoOutput(true);
				
				out = conn.getOutputStream();
				out.write(item.getString("sv").getBytes());
				out.flush();
			}
			
			in = conn.getInputStream();
			bout = new ByteArrayOutputStream();
			byte[] buf = new byte[1024 * 8];
			int length = 0;
			while ((length = in.read(buf)) != -1) {
				bout.write(buf, 0, length);
			}
			
			result = new String(bout.toByteArray(), "UTF-8");
			System.out.println("["+String.format("%6d", number)+" RESULT ] " + result);
			
		} catch(Exception e) {
			System.err.println(e.getMessage());
			if(Lwm2mServerConfig.getInstance().isDebug()){
				e.printStackTrace();				
			}
		} finally {
			if(bout != null){
				try {
					bout.close();
				} catch (IOException e) {
					System.err.println(e.getMessage());
					if(Lwm2mServerConfig.getInstance().isDebug()){
						e.printStackTrace();				
					}
				}
			}
			if(in != null){
				try {
					in.close();
				} catch (IOException e) {
					System.err.println(e.getMessage());
					if(Lwm2mServerConfig.getInstance().isDebug()){
						e.printStackTrace();				
					}
				}
			}if(out != null){
				try {
					out.close();
				} catch (IOException e) {
					System.err.println(e.getMessage());
					if(Lwm2mServerConfig.getInstance().isDebug()){
						e.printStackTrace();				
					}
				}
			}
			if(conn != null){
				conn.disconnect();
			}
		}
		
		return result;
	}
}
