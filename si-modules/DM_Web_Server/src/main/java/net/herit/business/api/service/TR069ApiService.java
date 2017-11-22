package net.herit.business.api.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Resource;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.*;
import java.sql.Timestamp;

import net.herit.business.device.service.MoProfileVO;
import net.herit.business.protocol.HttpConnector;
import net.herit.business.protocol.Util;
import net.herit.business.protocol.tr069.CurlOperation;
import net.herit.common.conf.HeritProperties;
import net.herit.common.exception.UserSysException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;


@Repository("TR069ApiService")
public class TR069ApiService {
	@Resource(name="ApiHdhDAO")
	private ApiHdhDAO hdhDAO;
	@Resource(name="ApiHdmDAO")
	private ApiHdmDAO hdmDAO;
	@Resource(name="ApiHdpDAO")
	private ApiHdpDAO hdpDAO;
	
	private String deviceId;
	private long curTime;
	private String acsHost = "";
	private int acsPort = 7557;
	
	
	private String ip;
	private String port;
	
	// init variable
	/*
	private JSONObject msg = null;
	private HashMap<String, Object> res = null;
	private URL url = null;
	private HttpURLConnection conn = null;
	private StringBuffer baseUrlString = new StringBuffer("http://").append(acsHost).append(":").append(acsPort);
	private StringBuffer urlString = null;
	private List<String> urlList = null;
	private int maxSeconds = 2;
	public TR069ApiService() {
		acsHost = HeritProperties.getProperty("Globals.tr069ServerHost");
		acsPort = Integer.parseInt(HeritProperties.getProperty("Globals.tr069ServerPort"));
	}
	 */
	
	@SuppressWarnings("unchecked")
	public HashMap<String, Object> execute2(String operation, JSONObject parameters) throws JSONException, UserSysException {
		
		
		
		HashMap<String, Object> res = null;
		HashMap<String, String> param = null;
		
		ip = HeritProperties.getProperty("Globals.tr069ServerHost");
		port = HeritProperties.getProperty("Globals.tr069ServerPort");
		
		if(operation.equals("read")){
			res = read(parameters);
			
		} else if(operation.equals("write")){
			res = write(parameters);
		} else if(operation.equals("firmware_update")){
			res = firmware_update(parameters);
		} else if(operation.equals("reboot")){
			res = reboot(parameters);
		}
		
		return res;
	}
	
	// DB에서 업데이트 된 값 조회
	public HashMap<String, Object> read(JSONObject parameters){
		HashMap<String, Object> res = null;
		String deviceId = parameters.getString("d");
		try{
			List<String> paramList = getParamList(parameters);
			ArrayList<MoProfileVO> resources = hdmDAO.getResources(parameters.getString("d"), paramList);
			
			HashMap<String, String> param = null;
			for(int i=0; i<resources.size(); i++){
				if("Y".equals(hdmDAO.getHistoricalOption(deviceId, resources.get(i).getResourceUri(), "R"))){
					param = hdmDAO.getResourceInfoByDeviceId(deviceId, resources.get(i).getResourceUri());
					param.put("data", resources.get(i).getData());
					param.put("trigger_type", "N");
					param.put("data_time", resources.get(i).getUpdateTime());
					hdhDAO.insertStatusHistory(param);
				}
			}
			
			res = Formatter.getInstance().readToDmFormat(resources);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	public HashMap<String, Object> write(JSONObject parameters){
		HashMap<String, Object> res = null;
		HttpURLConnection conn = null;
		
		try{
			String deviceId = Formatter.getInstance().getTR069DeviceIdFromDm(parameters.getString("d"));
			String url = "http://"+ip+":"+port+"/devices/"+deviceId+"/tasks?timeout=3000&connection_request";
			conn = HttpConnector.getInstance().getConnection(url);
			JSONObject msg = new JSONObject();
			msg.put("name", "setParameterValues");
			
			Object[][] parameterValues = new Object[1][2];
			parameterValues[0][0] = parameters.getJSONArray("e").getJSONObject(0).get("n");
			parameterValues[0][1] = parameters.getJSONArray("e").getJSONObject(0).get("sv");
			msg.put("parameterValues", parameterValues);
					
			String convertedMsg = Formatter.getInstance().getTR069ResourceUriFromDm(msg.toString());
			JSONObject result = new JSONObject(HttpConnector.getInstance().sendMsg(conn, convertedMsg));
			
			JSONArray resp = result.getJSONArray("parameterValues").getJSONArray(0);
			HashMap<String, String> param = null;
			String resourceUri = resp.getString(0).replace(".", "/");
			if("Y".equals(hdmDAO.getHistoricalOption(parameters.getString("d"), resourceUri,"RW"))){
				param = hdmDAO.getResourceInfoByDeviceId(parameters.getString("d"), resourceUri);
				param.put("ctl_type", "C");
				param.put("ctl_data", resp.getString(1));
				param.put("ctl_result", parameterValues[0][1].equals(resp.getString(1)) ? "OK" : "NO");
				param.put("error_code", "0");
				hdhDAO.insertControlHistory(param);
			}
			
			System.out.println(result);
			res = Formatter.getInstance().writeToDmFormat(result, conn);
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(conn != null){
				conn.disconnect();
				conn = null;
			}
		}
		return res;
	}
	
	public HashMap<String, Object> firmware_update(JSONObject parameters){
		String operation = "file download";
		HashMap<String, Object> res = new HashMap<String, Object>();
		
		try{	
			String deviceId = Formatter.getInstance().getTR069DeviceIdFromDm(parameters.getString("d"));
			
			CurlOperation co = new CurlOperation();
			String url = co.getUriByHttpMethod(co.getCmdList().get(operation), ip, port, deviceId);
			JSONObject msg = new JSONObject();
			msg.put("name", "download");
			msg.put("file", parameters.getString("fn"));
			msg.put("filename", parameters.getString("fn"));
			
			Object[] params = new Object[2];
			params[0] = url;
			params[1] = msg;
			
			String response = co.send(operation, params);
			System.out.println(response);
			
			res.put("status", 200);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return res;
	}
	
	public HashMap<String, Object> reboot(JSONObject parameters){
		String operation = "reboot";
		HashMap<String, Object> res = null;
		
		try{	
			String deviceId = Formatter.getInstance().getTR069DeviceIdFromDm(parameters.getString("d"));
			
			CurlOperation co = new CurlOperation();
			String url = co.getUriByHttpMethod(co.getCmdList().get(operation), ip, port, deviceId);
			JSONObject msg = new JSONObject();
			msg.put("name", "reboot");
			
			Object[] params = new Object[2];
			params[0] = url;
			params[1] = msg;
			
			String response = co.send(operation, params);
			System.out.println(response);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return res;
	}
	
	public ArrayList<String> getParamList(JSONObject parameters){
		ArrayList<String> paramList = new ArrayList<String>();
		for(int i=0; i<parameters.getJSONArray("e").length(); i++){
			paramList.add(parameters.getJSONArray("e").getJSONObject(i).getString("n"));
		}
		return paramList;
	}
	
	
	
	
	
	/*public HashMap<String, Object> execute(String operation, JSONObject inform) throws IOException
	{
		// init deviceId
		initDeviceId(inform);
		
		// set url from parameters
		urlList = new ArrayList<String>();
		for(int i=0; i<inform.getJSONArray("e").length(); i++){
			urlList.add(inform.getJSONArray("e").getJSONObject(i).getString("n"));
		}
		
		// refresh objects
		refreshObject();
		
		// getParameterValues & setParameterValues
		if(operation.equals("read")){
			StringBuffer projection = new StringBuffer();
			for(int i=0; i<urlList.size(); i++){
				projection.append(urlList.get(i));
				if(i<urlList.size()-1){
					projection.append(",");
				}
			}
			
			urlString = new StringBuffer(baseUrlString).append("/devices?query=");
			urlString.append(URLEncoder.encode("{\"_id\":\"+deviceId+\"}"));
			urlString.append("&projection=").append(projection.toString());
			
			url = new URL(urlString.toString());
			conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "application/json");
			
			conn.setDoOutput(false);
			conn.setDoInput(true);
			
		} else if(operation.equals("write")) {
			
		}
		
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(conn.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		//print result
		System.out.println("[ RESPONSE to STRING ] : "+response.toString());
		
		int resCode = conn.getResponseCode();
		
		JSONObject result = new JSONObject(response.toString().substring(1,response.toString().length()-1));
		
		JSONObject body = new JSONObject();
		JSONArray e = new JSONArray();
		
		for(int i=0; i<inform.getJSONArray("e").length(); i++){
			JSONObject e_o = new JSONObject();
			curTime = System.currentTimeMillis();
			String uri = inform.getJSONArray("e").getJSONObject(i).getString("n");
			String[] uriSplit = uri.split("/");
			JSONObject value = result;
			e_o.put("n", uri);
			for(int j=0; j<uriSplit.length; j++){
				value = value.getJSONObject(uriSplit[j]);
			}
			e_o.put("sv", value.getString("_value"));
			e_o.put("ti", curTime);
			e.put(e_o);
		}
		
		body.put("r", resCode);
		body.put("e", e);
		
		
		res.put("status", resCode);
		res.put("body", body);
		res.put("curTime", curTime);
			
		System.out.println("[ R E S ] : "+res);
		
		ObjectMapper mapper = new ObjectMapper();
		Object json = mapper.readValue(body.toString(), Object.class);
		res.put("json", json);
			
		return res;
	}
	
	
	// init deviceId from inform
	public void initDeviceId(JSONObject inform){
		deviceId = inform.getString("d").replace("_", "-");
	}
	
	// refresh objects
	public boolean refreshObject() throws IOException{
		boolean result = false;
		urlString = new StringBuffer(baseUrlString.toString()).append("/devices").append("/").append(deviceId).append("/tasks?timeout="+maxSeconds+"000&connection_request");
		
		System.out.println("[ LOG ][ CHECK POINT ][ 0 ]");
		for(int i=0; i<urlList.size(); i++){
			try{
				System.out.println("[ LOG ][ CHECK POINT ][ 0-"+i+" ]");
				msg = new JSONObject();
				msg.put("name", "refreshObject");
				msg.put("objectName", urlList.get(i).replace("/", "."));
				
				url = new URL(urlString.toString());
				conn = (HttpURLConnection)url.openConnection();
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Content-Type", "application/json");
				conn.setRequestProperty("Content-Length", String.valueOf(msg.length()));
				
				conn.setDoOutput(true);
				conn.setDoInput(true);
	
				OutputStream wr = conn.getOutputStream();
				wr.write(msg.toString().getBytes());
				wr.flush();
				wr.close();
				
				BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String line;
				StringBuffer resBody = new StringBuffer();
			 
				while ((line = in.readLine()) != null) {
					resBody.append(line);
				}
				in.close();
				
				// wait
				int curSeconds = 0;
				System.out.println("[ LOG ][ CHECK POINT ][ 1-"+i+" ]");
				while(Utility.isNOE(resBody.toString()) && curSeconds < maxSeconds){
					System.out.println("[ LOG ][ CHECK POINT ][ 1-"+curSeconds+" ]");
					Thread.sleep(1000);
					System.out.println("[ LOG ] refreshObject waiting for...");
					curSeconds += 1;
				}
				
				System.out.println("[ LOG ] refreshObject resBody : "+resBody.toString());
				
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	
	
	public JSONObject getParameterValues(){
		JSONObject result = null;
		
		return result;
	}
	
	*/
	
	
	//OLD
	public boolean refreshObject(String deviceId, String objectName) throws IOException{
		
		JSONObject msg = new JSONObject();
		msg.put("name", "refreshObject");
		msg.put("objectName", objectName);
		
		StringBuffer urlString = new StringBuffer("http://").append(acsHost).append(":").append(acsPort).append("/devices/").append(deviceId).append("/tasks?timeout=3000&connection_request");
		System.out.println("[ LOG ] url is : "+urlString.toString());
		URL url = new URL(urlString.toString());
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("Content-Length", String.valueOf(msg.length()));
		
		conn.setDoOutput(true);
		conn.setDoInput(true);

		OutputStream wr = conn.getOutputStream();
		wr.write(msg.toString().getBytes());
		wr.flush();
		wr.close();
		
		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line;
		StringBuffer resBody = new StringBuffer();
	 
		while ((line = in.readLine()) != null) {
			resBody.append(line);
		}
		in.close();
		
		System.err.println("[refreshObject] - resbody : " + resBody);
		
		return true;
	}
	
	public boolean refreshObjects(String deviceId, JSONObject parameters) throws InterruptedException {
		JSONArray urlList = parameters.getJSONArray("e");
		for(int i=0; i<urlList.length(); i++){
			try{
				refreshObject(deviceId, urlList.getJSONObject(i).getString("n").replace("/", "."));
			} catch(Exception e) {
				e.printStackTrace();
			}
			System.out.println("[refreshObjects] - pass");
			Thread.sleep(200);
		}
		return true;
	}
	
	public boolean refreshObject(String deviceId) throws IOException {
		refreshObject(deviceId, "");
		return true;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public HashMap<String, Object> execute(String operation, JSONObject parameters) 
			throws JsonGenerationException, JsonMappingException, IOException, UserSysException, JSONException, InterruptedException
	{	
		deviceId = parameters.getString("d").replace("_", "-");
		HashMap<String, Object> res = null;
		
		// refresh
		boolean result = refreshObjects(deviceId, parameters);
		//boolean result = refreshObject(deviceId, "InternetGatewayDevice");
		
		if(operation.equals("read")){
			int sec = 0;
			while(true){
				if(result){
					res = callOpenAPI(operation, parameters);
					sec = 0;
					break;
				} else {
					try {
						System.out.println("[refreshObject2] - sec : " + (++sec));
						Thread.sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		} else {
			res = callOpenAPI(operation, parameters);
		}
		return res;
	}
	
	public HashMap<String, Object> callOpenAPI(String operation, JSONObject inform) 
			throws IOException
	{	
		HashMap<String, Object> res = new HashMap<String, Object>();
		HttpURLConnection conn = openConnection(operation, inform);
		
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(conn.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		System.out.println("[ RESPONSE to STRING ] : "+response.toString());
		
		int resCode = conn.getResponseCode();
		
		JSONObject result = null;
		JSONObject body = new JSONObject();
		JSONArray e = new JSONArray();
		
		if(operation.equals("read")){
			result = new JSONObject(response.toString().substring(1,response.toString().length()-1));
			
			for(int i=0; i<inform.getJSONArray("e").length(); i++){
				JSONObject e_o = new JSONObject();
				curTime = System.currentTimeMillis();
				String uri = inform.getJSONArray("e").getJSONObject(i).getString("n");
				String[] uriSplit = uri.split("/");
				JSONObject value = result;
				e_o.put("n", uri);
				for(int j=0; j<uriSplit.length; j++){
					value = value.getJSONObject(uriSplit[j]);
				}
				e_o.put("sv", value.getString("_value"));
				e_o.put("ti", curTime);
				e.put(e_o);
			}
			
		} else if(operation.equals("write")) {
			result = new JSONObject(response.toString());
		}
		
		body.put("r", resCode);
		body.put("e", e);
		
		
		res.put("status", resCode);
		res.put("body", body);
		res.put("curTime", curTime);
			
		System.out.println("[ RESULT ] : "+result);
		System.out.println("[ R E S ] : "+res);
		
		ObjectMapper mapper = new ObjectMapper();
		Object json = mapper.readValue(body.toString(), Object.class);
		res.put("json", json);
		
		return res;
	}
	
	private HttpURLConnection openConnection(String operation, JSONObject inform) throws IOException {
		
		JSONObject msg = new JSONObject();
		StringBuffer urlString = new StringBuffer("http://");
		urlString.append(acsHost).append(":").append(acsPort);
		String idForm = URLEncoder.encode("{\"_id\":\""+deviceId+"\"}");
		if(operation.equals("read")){
			urlString.append("/devices?query=").append(idForm).append("&projection=");
			for(int i=0; i<inform.getJSONArray("e").length(); i++){
				urlString.append(inform.getJSONArray("e").getJSONObject(i).getString("n").replace("/", "."));
				if(i<inform.getJSONArray("e").length()-1){
					urlString.append(",");
				}
			}
		} else if (operation.equals("write")) {
			urlString.append("/devices/").append(deviceId).append("/tasks?timeout=3000&connection_request");
			JSONArray pvs = new JSONArray();
			
			msg.put("name", "setParameterValues");
			String value = inform.getJSONArray("e").getJSONObject(0).getString("sv").equals("ON") ? "1" : "0";
			String[] parameter = {inform.getJSONArray("e").getJSONObject(0).getString("n").replace("/", "."), value};
			pvs.put(parameter);
			msg.put("parameterValues",pvs);
		}
		
		System.out.println("[   I N F O R M   ] : " + inform.toString());
		System.out.println("[   M  S  G   ] : " + msg.toString());
		URL url = new URL(urlString.toString());
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setRequestProperty("Content-Type", "application/json");
		
		if(operation.equals("read")){
			conn.setRequestMethod("GET");
			conn.setDoOutput(false);
			conn.setDoInput(true);
		} else if (operation.equals("write")) {
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Length", String.valueOf(msg.length()));
			conn.setDoOutput(true);
			conn.setDoInput(true);
			
			OutputStream wr = conn.getOutputStream();
			wr.write(msg.toString().getBytes());
			wr.flush();
			wr.close();
		}
		
		return conn;
	}
	
	
		
}
