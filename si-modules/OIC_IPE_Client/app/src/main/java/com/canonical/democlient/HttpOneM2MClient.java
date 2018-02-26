package com.canonical.democlient;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by seung-wanmun on 2016. 8. 24..
 */
public class HttpOneM2MClient {
    private final static String TAG = HttpOneM2MClient.class.getSimpleName();
    private String address;
    private URL url;
    private HttpURLConnection conn;
    private OutputStream os;
    private InputStream is;
    ByteArrayOutputStream baos;
    JSONArray lblArray;
    private int responseCode;
    private String authority;

    public HttpOneM2MClient (String addr) {
        this.address = addr;
        this.responseCode = 0;
    }

    public HttpURLConnection openConnection() throws IOException {
        url = new URL(address);
        authority = url.getAuthority();
        conn = (HttpURLConnection) url.openConnection();
        return conn;
    }

    public void setRequestMethod( int requestMethod ){
        try {
            Constants.REQUEST_METHOD_TYPE obj = Constants.REQUEST_METHOD_TYPE.get( requestMethod );
            conn.setRequestMethod(obj.Name());
            conn.setDoOutput(obj.DoOutput());
        } catch( ProtocolException pe ) {
            Log.e(TAG, pe.toString());
        }
    }

    public void setRequestHeaderBase(String host, String resourceId, String resourceName, String origin, int resourceType) throws IOException {
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("HOST", host);
        conn.setRequestProperty("X-M2M-RI", resourceId);
        conn.setRequestProperty("X-M2M-NM", resourceName);
        conn.setRequestProperty("X-M2M-Origin", origin);
        conn.setRequestProperty("Content-Type", "application/vnd.onem2m-res+json; ty=" + resourceType);
        conn.setRequestProperty("Cache-Control", "no-cache");

        if( host.isEmpty() ){
            conn.setRequestProperty("HOST", Constants.HOST_IP);
        }
        if( resourceId.isEmpty() || resourceName.isEmpty() ){
            conn.setRequestProperty("X-M2M-RI", Constants.UNNAMED);
            conn.setRequestProperty("X-M2M-NM", Constants.UNNAMED);
        }
        if( origin.isEmpty() ){
            conn.setRequestProperty("X-M2M-Origin", Constants.ORIGIN_BASE);
        }

        //conn.setRequestProperty("Authorization", "Basic a2ZjMjphYjEyMQ==");
        conn.setDoInput(true);
    }
    public void setRequestHeader(String host, String resourceId, String resourceName, String origin, int resourceType) throws IOException {
        setRequestHeaderBase(host, resourceId, resourceName, origin, resourceType);
        setRequestMethod(Constants.REQUEST_METHOD_TYPE.POST.Value());

    }
    public void setRequestHeader(String host, String resourceId, String resourceName, String origin, int resourceType, int request_method) throws IOException {
        setRequestMethod(request_method);
        setRequestHeaderBase(host, resourceId, resourceName, origin, resourceType);
    }

    public void setRequestHeaderForPCU(String host, String resourceId, String origin) throws IOException {
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("HOST", host);
        conn.setRequestProperty("X-M2M-RI", resourceId);
        conn.setRequestProperty("X-M2M-Origin", origin);
        conn.setDoInput(true);
        conn.setDoOutput(false);
    }


    public void sendRequest(JSONObject json) throws Exception {
        os = conn.getOutputStream();
        os.write(json.toString().getBytes());
        os.flush();
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

            jsonReturn = new JSONObject(response);
        } else {
            JSONObject job = new JSONObject();
            job.put("RESPONSE-CODE", String.valueOf(this.responseCode));

            jsonReturn = job;
        }

        return jsonReturn;
    }

    public String getResponseByXml() throws Exception {
        String response;
        String xmlReturn;

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
            xmlReturn = response;
        } else {
            JSONObject job = new JSONObject();
            job.put("RESPONSE-CODE", String.valueOf(this.responseCode));

            xmlReturn = job.toString();
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
