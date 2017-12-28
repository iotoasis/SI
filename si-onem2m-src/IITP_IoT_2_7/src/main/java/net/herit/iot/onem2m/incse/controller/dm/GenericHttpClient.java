package net.herit.iot.onem2m.incse.controller.dm;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

import org.json.JSONObject;

public class GenericHttpClient {
	private final static String TAG = GenericHttpClient.class.getSimpleName();
    private String address;
    private URL url;
    private HttpURLConnection conn;
    private OutputStream os;
    private InputStream is;
    ByteArrayOutputStream baos;
    
    private int responseCode;

    public GenericHttpClient (String addr) {
        this.address = addr;
        this.responseCode = 0;
    }

    public HttpURLConnection openConnection() throws IOException {
        url = new URL(address);
        conn = (HttpURLConnection) url.openConnection();
        return conn;

    }
    
    public void setRequestHeader() throws IOException {
        
        conn.setConnectTimeout(5000);
        conn.setRequestProperty("Content-Type", "application/json");
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
