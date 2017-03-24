package org.eclipse.leshan.server.onem2m.handler;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.eclipse.leshan.server.Constants;
import org.eclipse.leshan.server.Lwm2mServerConfig;

public class OrderExecutor {
	
	private Lwm2mServerConfig config = Lwm2mServerConfig.getInstance();
	private HttpURLConnection conn;
	private String result;
	InputStream in;
	ByteArrayOutputStream out;
	
	private Item item;
	public OrderExecutor(Item item){
		this.item = item;
	}

	public String execute(){

		try{
			
			String strUrl = "http://"+config.getSecureLocalAddress()+":"+config.getWebPort()+"/api/clients/"+item.getAuthId()+item.getUri();
			/*
			if(item.getOperation().equals("observe")){
				strUrl += "/observe";
			}//*/
			
			System.out.println(strUrl);
			URL url = new URL(strUrl);
			conn = (HttpURLConnection)url.openConnection();
			conn.setRequestProperty("Content-Type", "text/plain");
			
			if( "read".equals(item.getOperation()) ){
				conn.setRequestMethod("GET");
			} else if( "execute".equals(item.getOperation()) ){
				conn.setRequestMethod("PUT");
				conn.setDoOutput(true);
			}
			
			
			if( "execute".equals(item.getOperation()) ){
				try (OutputStream out = conn.getOutputStream()) {
					out.write(item.getValue().getBytes());
					out.flush();
		        } catch(Exception e) {
		        	e.printStackTrace();
		        }
			}
			
			in = conn.getInputStream();
			out = new ByteArrayOutputStream();
			byte[] buf = new byte[1024 * 8];
			int length = 0;
			while ((length = in.read(buf)) != -1) {
			    out.write(buf, 0, length);
			}
			
			result = new String(out.toByteArray(), "UTF-8");
			
			System.out.println("[   RESULT   ] " + result);
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try{
				
			} catch(Exception e) {
				e.printStackTrace();
			}
			conn.disconnect();
		}
		
		return result;
	}
}
