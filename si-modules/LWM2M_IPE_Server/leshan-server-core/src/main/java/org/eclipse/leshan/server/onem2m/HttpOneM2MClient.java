package org.eclipse.leshan.server.onem2m;

import org.eclipse.leshan.server.Constants;
import org.eclipse.leshan.server.Lwm2mServerConfig;
import org.eclipse.leshan.server.api.Util;
import org.eclipse.leshan.util.Base64;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by seung-wanmun on 2016. 8. 24..
 */
public class HttpOneM2MClient {
	
	private Lwm2mServerConfig config = Lwm2mServerConfig.getInstance();
	
	// connection
    private URL url;
    private static HttpOneM2MClient singleton;
    private HttpURLConnection conn;
    
    private OutputStream authOs;
    private InputStream authIs;
    
    private OutputStream os;
    private InputStream is;
    
    ByteArrayOutputStream baos;    
    private int responseCode;
    private String authority;
    
    // Singleton
    public static HttpOneM2MClient getInstance(){
    	if(singleton == null){
    		singleton = new HttpOneM2MClient();
    	}
    	return singleton;
    }
    
    // init
    public void init(String address) throws IOException {
    	url = new URL(address);
    	authority = url.getAuthority();
        this.responseCode = 0;
        conn = (HttpURLConnection) url.openConnection();
    }

    // request method handler
    public void setRequestMethodCustom( int requestMethod ){
        try {
            Constants.REQUEST_METHOD_TYPE obj = Constants.REQUEST_METHOD_TYPE.get( requestMethod );
            conn.setRequestMethod(obj.Name());
            conn.setDoOutput(obj.DoOutput());
        } catch( ProtocolException pe ) {
            
        }
    }

    // request header base
    public void setRequestHeaderBase(String host, String resourceId, String resourceName, String origin, int resourceType) throws IOException {
    	    	
        if( Util.isNoE(host) ){
            host =  config.getSiAddress();
        }
        if( Util.isNoEOprOr(resourceId, resourceName) ){
            resourceId = Constants.UNNAMED;
            resourceName = Constants.UNNAMED;
        }
        if( Util.isNoE(origin) ){
            origin = Constants.ORIGIN_BASE;
        }

        conn.setRequestProperty("Accept", Constants.CONTENT_TYPE);
        conn.setRequestProperty("HOST", host);
        conn.setRequestProperty("X-M2M-RI", resourceId);
        conn.setRequestProperty("X-M2M-NM", resourceName);
        conn.setRequestProperty("X-M2M-Origin", origin);
        conn.setRequestProperty("Content-Type", "application/vnd.onem2m-res+json; ty=" + resourceType);
        conn.setRequestProperty("Cache-Control", "no-cache");
        if(Constants.USE_AUTH){
        	conn.setRequestProperty("Authorization", "Basic a2ZjMjphYjEyMQ==");
        }
        conn.setDoInput(true);
    }
    public void setRequestHeader(String host, String resourceId, String resourceName, String origin, int resourceType) throws IOException {
    	setRequestHeaderBase(host, resourceId, resourceName, origin, resourceType);
    	setRequestMethodCustom(Constants.REQUEST_METHOD_TYPE.POST.Value());
    }
    public void setRequestHeader(String host, String resourceId, String resourceName, String origin, int resourceType, int request_method) throws IOException {
    	setRequestHeaderBase(host, resourceId, resourceName, origin, resourceType);
        setRequestMethodCustom(request_method);
    }

    /*
    public void setRequestHeaderForTemp() throws IOException {
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("HOST", "10.101.101.193");
        conn.setRequestProperty("X-M2M-RI", "pm_1506031022390012");
        conn.setRequestProperty("X-M2M-Origin", "//iot.herit.net/herit-cse/AE_00010");
        conn.setRequestProperty("Content-Type", "application/vnd.onem2m-res+json; ty=4");
        conn.setDoInput(true);
        conn.setDoOutput(true);
    }
    */
    
    public void setRequestHeaderForPCU(String host, String resourceId, String origin) throws IOException {
        conn.setRequestMethod("DELETE");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("HOST", host);
        conn.setRequestProperty("X-M2M-RI", resourceId);
        conn.setRequestProperty("X-M2M-Origin", origin);
        conn.setDoInput(true);
        conn.setDoOutput(false);
    }

    public void sendRequest(JSONObject json) throws Exception {
    	URL authUrl = new URL(config.getAuthAddress());
    	HttpURLConnection authConn = (HttpURLConnection) authUrl.openConnection();
    	
    	authConn.setRequestMethod("POST");
    	authConn.setRequestProperty("Content-Type", "application/json");
    	authConn.setDoInput(true);
    	authConn.setDoOutput(true);
    	
    	String userId = "USERIID-X121312121";
    	String reqVal = "{\"_devId\":\""+userId+"\"}";
    	authOs = authConn.getOutputStream();
    	authOs.write(reqVal.getBytes());
    	authOs.flush();
    	
    	is = authConn.getInputStream();
        baos = new ByteArrayOutputStream();
        byte[] byteBuffer = new byte[1024];
        byte[] byteData = null;
        int nLength = 0;
        while ((nLength = is.read(byteBuffer, 0, byteBuffer.length)) != -1) {
            baos.write(byteBuffer, 0, nLength);
        }
        byteData = baos.toByteArray();
        JSONObject response = new JSONObject(new String(byteData));
        authConn.disconnect();
        is.close();
        
        try{
	        if(authConn.getResponseCode() == 200){
	        	String pwd = new JSONObject(response.getString("content")).getString("DEV_PWD");
	        	if( pwd.equals("Iv2F_BvNDvJgIuFxhCB0R5v25FQ") ){
	        		String strHeader = userId+":"+pwd;
	            	String auth = new String(Base64.encodeBase64(strHeader.getBytes()));
	            	conn.setRequestProperty("Authorization","Basic "+auth);
	            	
	            	os = conn.getOutputStream();
	            	os.write(json.toString().getBytes());
	                os.flush();
	        	} else {
	        		System.out.println("ID or password was entered incorrectly.");
	        	}
	        	
	        } else {
	        	System.out.println("Failed to connect to server.");
	        }
        } catch(Exception e) {
        	System.out.println("Authentication failed.");
        	e.printStackTrace();
        	os = conn.getOutputStream();
        	os.write(json.toString().getBytes());
            os.flush();
        }
    }

    public JSONObject getResponse() throws Exception {
        String response;
        JSONObject jsonReturn;

        this.responseCode = conn.getResponseCode();

        if (this.responseCode >= HttpURLConnection.HTTP_OK && this.responseCode <= 202) {
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
            System.out.println(response);

            jsonReturn = new JSONObject(response);
        } else {
            JSONObject job = new JSONObject();
            job.put("RESPONSE-CODE", String.valueOf(this.responseCode));

            jsonReturn = job;
        }

        return jsonReturn;
    }

    public String getResponseByXml(){
    	
        String xmlReturn = null;
        try{
	        this.responseCode = conn.getResponseCode();
	      
	        if (this.responseCode >= HttpURLConnection.HTTP_OK && this.responseCode <= 202) {
	            is = conn.getInputStream();
	            baos = new ByteArrayOutputStream();
	            byte[] byteBuffer = new byte[1024];
	            int nLength = 0;
	            while ((nLength = is.read(byteBuffer, 0, byteBuffer.length)) != -1) {
	                baos.write(byteBuffer, 0, nLength);
	            }
	            xmlReturn = new String(baos.toByteArray());
	        } else {
	            JSONObject job = new JSONObject();
	            job.put("RESPONSE-CODE", String.valueOf(this.responseCode));
	
	            xmlReturn = job.toString();
	        }
        } catch(Exception e){
        	e.printStackTrace();
        }

        return xmlReturn;
    }

    public void closeConnection() {
        conn.disconnect();
    }

    public int getResponseCode() {
        return this.responseCode;
    }

    public String getAuthority() {
        return this.authority;
    }


}
