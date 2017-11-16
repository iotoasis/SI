package net.herit.business.api.service;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.json.JSONObject;

import net.herit.business.device.service.MoProfileVO;
import net.herit.business.protocol.HttpConnector;
import net.herit.business.protocol.tr069.CurlOperation;


public class TR069Apis {

	@Resource(name="ApiHdmDAO")
	private ApiHdmDAO hdmDAO;
	
	private String ip;
	private String port;
	
	public TR069Apis(String ip, String port){
		this.ip = ip;
		this.port = port;
	}
	
	public ArrayList<String> getParamList(JSONObject parameters){
		ArrayList<String> paramList = new ArrayList<String>();
		for(int i=0; i<parameters.getJSONArray("e").length(); i++){
			paramList.add(parameters.getJSONArray("e").getJSONObject(i).getString("n"));
		}
		return paramList;
	}
	
	// DB에서 업데이트 된 값 조회
	public HashMap<String, Object> read(JSONObject parameters){
		HashMap<String, Object> res = null;
		try{
			List<String> paramList = getParamList(parameters);
			ArrayList<MoProfileVO> resources = hdmDAO.getResources(parameters.getString("d"), paramList);
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
		HashMap<String, Object> res = null;
		
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
	
	
}
