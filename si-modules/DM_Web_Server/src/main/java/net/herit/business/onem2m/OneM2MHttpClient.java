package net.herit.business.onem2m;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

import org.json.JSONObject;


public class OneM2MHttpClient {
	private final static String TAG = OneM2MHttpClient.class.getSimpleName();
    private String address;
    private URL url;
    private HttpURLConnection conn;
    private OutputStream os;
    private InputStream is;
    ByteArrayOutputStream baos;

    private int responseCode;

    public OneM2MHttpClient (String addr) {
        this.address = addr;
        this.responseCode = 0;
    }

    public HttpURLConnection openConnection() throws IOException {
        url = new URL(address);
        conn = (HttpURLConnection) url.openConnection();
        return conn;

    }
    
    public void setRequestHeaderBase(String host, String resourceId, String resourceName, String origin, int resourceType) throws IOException {
        
    	String rtHeaderStr = "; ty=";
    	if(resourceType == 0) {
    		rtHeaderStr = "";
    	} else {
    		rtHeaderStr = "; ty=" + resourceType;
    	}
    	
        conn.setConnectTimeout(5000);
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("HOST", host);
        conn.setRequestProperty("X-M2M-RI", resourceId);
        conn.setRequestProperty("X-M2M-NM", resourceName);
        conn.setRequestProperty("X-M2M-Origin", origin);
        conn.setRequestProperty("Content-Type", "application/vnd.onem2m-res+json" + rtHeaderStr);
        conn.setRequestProperty("Cache-Control", "no-cache");

        conn.setDoInput(true);
    }
    
    public void setRequestMethod( String requestMethod ){
        try {
            conn.setRequestMethod(requestMethod);
            conn.setDoOutput(true);
        } catch( ProtocolException pe ) {
           pe.printStackTrace();
        }
    }
    
    public void sendRequest(String jsonStr) throws Exception {
        os = conn.getOutputStream();
        os.write(jsonStr.getBytes());
        os.flush();
    }
    
    public String getResponseString() throws Exception {
    	String response;
        
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

            return response;
        } else {
            
            response = "RESPONSE-CODE :" + String.valueOf(this.responseCode);
        }

        return response;
    }
    
    public JSONObject getResponseJson() throws Exception {
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

            jsonReturn = new JSONObject(response);
        } else {
            JSONObject job = new JSONObject();
            job.put("RESPONSE-CODE", String.valueOf(this.responseCode));

            jsonReturn = job;
        }

        return jsonReturn;
    }
    
    public HttpURLConnection getConnection() {
    	return this.conn;
    }
    
    public void closeConnection() {
        conn.disconnect();
    }

    public int getResponseCode() {
        return this.responseCode;
    }

}
