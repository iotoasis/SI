package net.herit.business.api.service;

import java.util.HashMap;
import java.util.List;
import javax.annotation.Resource;

import java.io.*;
import java.net.*;
import java.sql.Timestamp;

import net.herit.business.device.service.MoProfileVO;
import net.herit.business.protocol.lwm2m.Util;
import net.herit.common.conf.HeritProperties;
import net.herit.common.exception.UserSysException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;


@Repository("LWM2MApiService")
public class LWM2MApiService {
	@Resource(name="ApiHdhDAO")
	private ApiHdhDAO hdhDAO;
	@Resource(name="ApiHdpDAO")
	private ApiHdpDAO hdpDAO;
	
	/** 클래스 명칭 **/
	private final String CLASS_NAME = getClass().getName();
	/** 메소드 명칭 **/
	private String METHOD_NAME = "";
	private long curTime = 0;
	
	private String dmHost = "";
	private int dmPort = 8085;
	
	public LWM2MApiService() {
		dmHost = HeritProperties.getProperty("Globals.lwm2mServerHost");
		dmPort = Integer.parseInt(HeritProperties.getProperty("Globals.lwm2mServerPort"));
	}
	
	
	public HashMap<String, Object> execute(String operation, HashMap<String, Object> contentMap, String endPoint) 
			throws JsonGenerationException, JsonMappingException, IOException, UserSysException, JSONException
	{	
		HashMap<String, Object> res = callOpenAPI(operation, endPoint, contentMap);
		
		HashMap<String, String> uriList = new HashMap<String, String>();
		if( !Util.isNOE(res.toString()) ){
			JSONArray body = new JSONArray(res.get("body").toString());
			for(int i=0; i<body.length(); i++){
				try{
					JSONObject obj = body.getJSONObject(i);
					uriList.put(obj.getString("uri"), obj.getJSONObject("content").getString("value"));
				} catch(Exception e){}
			}

			String deviceId = (String) contentMap.get("d");
			String[] vals = deviceId.split("_");
			HashMap<String, String> info = new HashMap<String,String>();
			
			info.put("oui", vals[0]);
			info.put("model_name", vals[1]);
			
				
			List<MoProfileVO> statusUriList = hdpDAO.getStatusUris(info);
			HashMap<String, String> baseInfo = new HashMap<String, String>();
			baseInfo.put("device_id", deviceId);
			baseInfo.put("model_name", vals[1]);
			baseInfo.put("sn", vals[2]);
			baseInfo.put("trigger_type", "N");
			baseInfo.put("data_time", new Timestamp(curTime).toString());
			
			int r = 0;
			for(int i=0; i<statusUriList.size(); i++){
				String data = uriList.get(statusUriList.get(i).getResourceUri().replace("/-/","/0/"));
				if(data != null){
					baseInfo.put("resource_name", statusUriList.get(i).getDisplayName());
					baseInfo.put("resource_uri", statusUriList.get(i).getResourceUri());
					baseInfo.put("data", data);
					r += hdhDAO.insertStatusHistory(baseInfo);
				}
			}
		}
		
		JSONObject content = null;
		if( operation.equals("reboot") ){
			content = getRebootForm(res);
		} else {
			content = getBaseForm(res);
		}
		ObjectMapper mapper = new ObjectMapper();
		Object json = mapper.readValue(content.toString(), Object.class);
		res.put("json", json);
		
		return res;
	}
	
	public JSONObject getRebootForm(HashMap<String, Object> res){
		/*********************************************
		 * 		body rebuild
		 ********************************************/
		JSONObject e_o = new JSONObject();
		try{
			e_o.put("n", "/3/-/4");
			e_o.put("sv", "CHANGED");
			e_o.put("ti", curTime);
		} catch(Exception err) {
			
		}
		
		JSONObject content = new JSONObject();
		content.put("r", res.get("status"));
		content.put("e", e_o);
		
		/********************************************/
		return content;
	}
	
	public JSONObject getBaseForm(HashMap<String, Object> res){
		/*********************************************
		 * 		body rebuild
		 ********************************************/
		JSONArray e = new JSONArray();
		JSONObject content = new JSONObject();
		try{
			JSONArray body = new JSONArray((String)res.get("body"));
			for (int i = 0; i < body.length(); i++) {
				try{
					JSONObject o = body.getJSONObject(i);
					JSONObject e_o = new JSONObject();
					e_o.put("n", o.getString("uri").replace("/0/", "/-/"));
					if(e_o.getString("n").equals("/3/-/10")){
						e_o.put("sv", (Integer)o.getJSONObject("content").get("value")/ (1024*1024));
					} else {
						e_o.put("sv", o.getJSONObject("content").get("value"));					
					}
					e_o.put("ti", System.currentTimeMillis());
					e.put(e_o);
				} catch(Exception err) {
					
				}
			}
			content.put("r", res.get("status"));
			content.put("e", e);
			
		} catch(Exception ew){
			ew.printStackTrace();
		}
		/********************************************/
		return content;
	}
	
	
	
	public HashMap<String, Object> callOpenAPI(String operation, String endPoint, HashMap<String, Object> contentMap) 
			throws IOException
	{
		
		METHOD_NAME = "callOpenAPI";
		
		HashMap<String, Object> res = new HashMap<String, Object>();

		URL url = new URL(getUrl(endPoint));

		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		
		String uri = null;
		try{
			uri = (String) contentMap.get("n");
		}catch(Exception e){e.printStackTrace();}
		
		if(operation.equals("read") || operation.equals("reboot")){
			conn.setRequestMethod("POST");
		} else if(operation.equals("write") ){
			if(!Util.isNOE(uri) && uri.equals("/7/-/6")){
				conn.setRequestMethod("POST");				
			} else {
				conn.setRequestMethod("PUT");
			}
		}
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("Content-Length", String.valueOf(contentMap.size()));
		
		conn.setDoOutput(true);
		conn.setDoInput(true);

		JSONObject rr = new JSONObject(contentMap);
		OutputStream wr = conn.getOutputStream();
		wr.write(rr.toString().getBytes());
		wr.flush();
		wr.close();
		
		int resCode = conn.getResponseCode();
		
		if (resCode == 200) {

			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			StringBuffer resBody = new StringBuffer();
		 
			while ((line = in.readLine()) != null) {
				resBody.append(line);
			}
			in.close();
			
			res.put("status", resCode);
			res.put("body", resBody.toString());
			curTime = System.currentTimeMillis();
		} else {

			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
			String line;
			StringBuffer resBody = new StringBuffer();
		 
			while ((line = in.readLine()) != null) {
				resBody.append(line);
			}
			in.close();
			
			res.put("status", resCode);
			res.put("body", resBody.toString());
			
		}
			
		return res;
	}
	
	private String getUrl(String endPoint) {
		//System.err.println("http://"+ dmHost +":"+ dmPort +"/api/hdm/"+endPoint);
		return "http://"+ dmHost +":"+ dmPort +"/api/hdm/"+endPoint;
	}
		
}
