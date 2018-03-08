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
	public HashMap<String, Object> execute(String operation, JSONObject parameters) throws JSONException, UserSysException {
		
		
		
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
			
			//System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
			//System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
			
			String deviceId = Formatter.getInstance().getTR069DeviceIdFromDm(parameters.getString("d"));
			String url = "http://"+ip+":"+port+"/devices/"+deviceId+"/tasks?timeout=3000&connection_request";
			conn = HttpConnector.getInstance().getConnection(url);
			JSONObject msg = new JSONObject();
			
			Object[][] parameterValues = new Object[1][2];
			parameterValues[0][0] = parameters.getJSONArray("e").getJSONObject(0).get("n");
			parameterValues[0][1] = parameters.getJSONArray("e").getJSONObject(0).get("sv");
			
			msg.put("name", "refreshObject");
			msg.put("objectName", parameterValues[0][0]);
			//System.out.println(msg);
			
			String convertedMsg = Formatter.getInstance().getTR069ResourceUriFromDm(msg.toString());
			JSONObject result = new JSONObject(HttpConnector.getInstance().sendMsg(conn, convertedMsg));
			conn.disconnect();
			conn = null;
			//System.out.println(result);
			
			msg = new JSONObject();
			msg.put("name", "setParameterValues");
			msg.put("parameterValues", parameterValues);
			//System.out.println(msg);
			
			conn = HttpConnector.getInstance().getConnection(url);
			convertedMsg = Formatter.getInstance().getTR069ResourceUriFromDm(msg.toString());
			result = new JSONObject(HttpConnector.getInstance().sendMsg(conn, convertedMsg));
			
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
			
			//System.out.println(result);
			res = Formatter.getInstance().writeToDmFormat(result, conn);
			
			//System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
			//System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
			
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
			//System.out.println(response);
			
			HashMap<String, String> param = new HashMap<String, String>();
			String[] deviceInfo = deviceId.split("-");
			param.put("device_id", deviceId.replace("-", "_"));
			param.put("model_name", deviceInfo[1]);
			param.put("sn", deviceInfo[2]);
			param.put("package", parameters.getString("fn"));
			param.put("version", parameters.getString("v"));
			param.put("status", "101");
			param.put("result", "0");
			int result = hdhDAO.insertFirmwareUpgradeHistory(param);
			//System.out.println("DB INSERT RESULT : "+result);
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
			//System.out.println(response);
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
	
	
		
}
