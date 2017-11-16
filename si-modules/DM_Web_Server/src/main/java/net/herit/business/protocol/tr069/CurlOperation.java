package net.herit.business.protocol.tr069;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.eclipse.persistence.sessions.Session;
import org.json.JSONObject;

import com.mashape.unirest.http.Headers;

import net.herit.business.protocol.Util;

public class CurlOperation {
	
	// HTTP Objects
	private HttpClient httpClient = null;
	private HttpEntity entity = null;
	private HttpResponse httpResponse = null;
	
	// Initialize
	public CurlOperation(){
		headers = new HashMap<String, String>();
		
		cmdList = new HashMap<String, String>();
		numberList = new HashMap<String, Integer>();
		
		setCmdList();
		setNumberList();
		httpClient = new DefaultHttpClient();
	}
	
	// Command list
	private HashMap<String, String> cmdList = null;
	public HashMap<String, String> getCmdList(){
		return cmdList;
	}
	public void setCmdList(){
		cmdList.put("get file","get");
		cmdList.put("file upload","put");
		cmdList.put("getParameterValues","post");
		cmdList.put("setParameterValues","post");
		cmdList.put("refresh","post");
		cmdList.put("reboot","post");
		cmdList.put("file download","post");
		cmdList.put("delete task","delete");
		cmdList.put("delete file","delete");
	}
	
	// The number of Parameter by operation
	private HashMap<String, Integer> numberList = null;
	public HashMap<String, Integer> getNumberList(){
		return numberList;
	}
	public void setNumberList(){
		numberList.put("get",1);
		numberList.put("put",3);
		numberList.put("post",2);
		numberList.put("delete",1);
	}
	
	// Get,Set HTTP Header
	private HashMap<String, String> headers = null;
	public HashMap<String, String> getHeader(){
		return headers;
	}
	public void setHeader(String key, String value){
		headers.put(key, value);
	}
		

	// Send msg
	public String send(String operation, Object ...params) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException{
		// 입력값, 개수 확인
		String httpMethod = cmdList.get(operation);
		int tnop = numberList.get(httpMethod);
		if(params == null || params.length < 1 || tnop != params.length){
			throw new ArrayIndexOutOfBoundsException(operation+" 은/는 "+tnop+"개의 parameter가 필요합니다.");
		}
		
		// ACS서버 확인
		String acsUri = (String)params[0];
		if(Util.getInstance().isNOE(acsUri)){
			throw new NullPointerException("ACS address정보가 필요합니다.");
		}
		
		System.out.println("DDDDDDDDDDDDDD555555555555DDDDDDDDDDD");
		// Method별 호출
		/*CurlOperation co = new CurlOperation();
		Method mtd = co.getClass().getMethod(httpMethod, new Class[]{Object.class});
		String response = (String)mtd.invoke(co, params);*/
		
		String response = null;
		if(httpMethod.equals("put")){
			response = put(params);
		} else if(httpMethod.equals("post")) {
			response = post(params);
		}
		return response;
	}
	
	// PUT API
	public String put(Object[] params){
		// params 초기화
		String uri = (String)params[0];
		HashMap<String, String> headers = (HashMap<String, String>)params[1];
		String path = (String)params[2];
		HttpEntity httpEntity = null;
		
		HttpPut httpPut = null;
		try{
			httpClient = HttpClientBuilder.create().build();
			httpPut = new HttpPut(uri+"/files/"+path.substring(path.lastIndexOf("/")+1));
			Iterator<String> keys = headers.keySet().iterator();
			while(keys.hasNext()){
				String name = keys.next();
				String value = headers.get(name);
				System.out.println("……………  "+name+" : "+value);
				httpPut.setHeader(name, value);
			}
			
			File file = new File(path);
			httpEntity = MultipartEntityBuilder.create().setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
					.addBinaryBody("file", file, ContentType.create("application/json"), path.substring(0, path.lastIndexOf("/"))).build();
			httpPut.setEntity(httpEntity);
			
			httpResponse = httpClient.execute(httpPut);
			entity = httpResponse.getEntity();
			
			// 응답 결과
			System.out.println("----------------------------------------");
			System.out.println(httpResponse.getStatusLine());
			if (entity != null) {
				System.out.println("Response content length: " + entity.getContentLength());
				BufferedReader rd = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));

				String line = "";
				while ((line = rd.readLine()) != null) {
					System.out.println(line);
				}
			}
			httpPut.abort();
			System.out.println("----------------------------------------");
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(httpClient != null){
				httpClient.getConnectionManager().shutdown();				
			}
		}
		
		return httpResponse.toString();
	}
	
	// POST API
	public String post(Object[] params){
		// params 초기화
		String uri = (String)params[0];
		JSONObject param = (JSONObject)params[1];
		
		HttpPost httpPost = null;
		try{
			httpClient = HttpClientBuilder.create().build();
			httpPost = new HttpPost(uri);
			
			
			StringEntity stringEntity = new StringEntity(param.toString());
			httpPost.addHeader("content-type", "application/json");
			httpPost.setEntity(stringEntity);
			
			httpResponse = httpClient.execute(httpPost);
			
			// 응답 결과
			System.out.println("----------------------------------------");
			System.out.println(httpResponse.getStatusLine());
			if (httpResponse != null) {
				BufferedReader rd = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));

				String line = "";
				while ((line = rd.readLine()) != null) {
					System.out.println(line);
				}
			}
			httpPost.abort();
			System.out.println("----------------------------------------");
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(httpClient != null){
				httpClient.getConnectionManager().shutdown();				
			}
		}
		
		return httpResponse.toString();
	}
	
	// GET API
	public String get(Object[] params){
		HttpGet httpGet = null;
		try{
			
		} catch(Exception e) {
			
		} finally {
			
		}
		return null;
	}
	
	// DELETE API
	public String delete(Object[] params){
		HttpDelete httpDelete = null;
		try{
			
		} catch(Exception e) {
			
		} finally {
			
		}
		return null;
	}
	
	public String getUriByHttpMethod(String httpMethod, String ip, String port, String deviceId){
		StringBuffer sb = new StringBuffer("http://");
		sb.append(ip).append(":").append(port);
		
		if(httpMethod.equals("put")){
			
		} else if(httpMethod.equals("post")){
			sb.append("/devices/").append(deviceId).append("/tasks?timeout=3000&connection_request");
		} else if(httpMethod.equals("get")){
			
		} else if(httpMethod.equals("delete")){
			
		}
		
		return sb.toString();
	}
	
	
	
	
}
