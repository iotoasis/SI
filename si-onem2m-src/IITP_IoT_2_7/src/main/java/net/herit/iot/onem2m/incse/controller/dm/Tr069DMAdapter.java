package net.herit.iot.onem2m.incse.controller.dm;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.message.onem2m.OneM2mRequest.OPERATION;
import net.herit.iot.message.onem2m.format.Enums.CONTENT_TYPE;
import net.herit.iot.onem2m.bind.http.client.HttpClient;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.methods.FileRequestEntity;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.http.HttpException;
import org.bson.Document;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Tr069DMAdapter {
	private String dmAddr;
	private int timeout;
	
	private Logger log = LoggerFactory.getLogger(Tr069DMAdapter.class);
	
	public static class MOUri {
		
		public static final String DVC_MANUFACTURER = "Device.DeviceInfo.Manufacturer";
		public static final String DVC_OUI = "Device.DeviceInfo.ManufacturerOUI";
		public static final String DVC_PRODUCTCLASS = "Device.DeviceInfo.ProductClass";
		public static final String DVC_SERIAL = "Device.DeviceInfo.SerialNumber";
		public static final String DVC_MODEL = "Device.DeviceInfo.ModelNumber";
		public static final String DVC_HW_VERSION = "Device.DeviceInfo.HardwareVersion";
		public static final String DVC_SW_VERSION = "Device.DeviceInfo.SoftwareVersion";
		public static final String DVC_PROVISIONING_CODE = "Device.DeviceInfo.ProvisioningCode";
		public static final String DVC_SPEC_VERSION = "Device.DeviceInfo.SpecVersion";
		public static final String DVC_TEMP_STATUS = "Device.DeviceInfo.TemperatureStatus";
		public static final String DVC_HUMIDITY_STATUS = "Device.DeviceInfo.HumidityStatus";
		public static final String DVC_MEMORY_TOTAL = "Device.DeviceInfo.MemoryStatus.Total";
		public static final String DVC_MEMORY_FREE = "Device.DeviceInfo.MemoryStatus.Free";
		public static final String DVC_LED_STATUS = "Device.DeviceInfo.LEDStatus";
		public static final String DVC_ALARM_STATUS = "Device.DeviceInfo.AlarmStatus";
		//
		public static final String DVC_BATTERY_LEVEL = "Device.DeviceInfo.X_oneM2M_org_BatteryStatus.Battery.0.Level";
		public static final String DVC_BATTERY_STATUS = "Device.DeviceInfo.X_oneM2M_org_BatteryStatus.Battery.0.Status";
		//
		public static final String DVC_EVT_LOG_TYPE_ID = "Device.DeviceInfo.X_oneM2M_org_Diagnostics.EventLog.0.Type";
		public static final String DVC_EVT_LOG_DATA = "Device.DeviceInfo.X_oneM2M_org_Diagnostics.EventLog.0.Data";
		public static final String DVC_EVT_LOG_STATUS = "Device.DeviceInfo.X_oneM2M_org_Diagnostics.EventLog.0.Status";
		public static final String DVC_EVT_LOG_ENABLE = "Device.DeviceInfo.X_oneM2M_org_Diagnostics.EventLog.0.Enable";
		//
		
		public static final String MGMT_SERVER_URL = "Device.ManagementServer.URL";
	}
	
	public Tr069DMAdapter(String dmAddr, int timeout) {
		this.dmAddr = dmAddr;
		this.timeout = timeout;
	}
	
	private Object getValue(JSONObject jsonObject) {
		
		Object resValue = "";
		
		if(jsonObject.get("_value") != null) {
			resValue = jsonObject.get("_value");
		} 
		
		return resValue;
	}
	
	private void callPutFileApi(String fileType, String url, String filePath, String oui, String prodClass, String version) throws HttpException, IOException, HitDMException {
		org.apache.commons.httpclient.HttpClient client = new org.apache.commons.httpclient.HttpClient();
		
		PutMethod putMethod = new PutMethod(url);
		File input = new File(filePath);
		RequestEntity entity = new FileRequestEntity(input, "Content-Length: 1431");
		putMethod.setRequestEntity(entity);
		putMethod.setRequestHeader("fileType", fileType);
		putMethod.setRequestHeader("oui", oui);
		putMethod.setRequestHeader("productClass", prodClass);
		putMethod.setRequestHeader("version", version);
		
		client.executeMethod(putMethod);
		
		if (putMethod.getStatusCode() != 200) {
			String debugStr = "DM Server return:"+ putMethod.getStatusCode();
			byte[] body;
			try {
				body = putMethod.getResponseBody();
				if (body != null) {
					debugStr += "\r\nBody:"+new String(body);
				}
			} catch (Exception e) {
				debugStr += e.getMessage();
			}
			
			throw new HitDMException(putMethod.getStatusCode(), debugStr);
		}
		
	}
	
	private JSONArray callGetApi(String to, Document content) throws HitDMException {
		
		OneM2mRequest reqMessage = new OneM2mRequest();
		
		String target = to;
		
		try {	
			String encodeResult = URLEncoder.encode(content.toJson(), "UTF-8");
			
			target = target + encodeResult;
		
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		reqMessage.setTo(target);
		reqMessage.setOperation(OPERATION.RETRIEVE);
		reqMessage.setContentType(CONTENT_TYPE.JSON);
		
		OneM2mResponse resMessage = HttpClient.getInstance().sendRequest(dmAddr+target, reqMessage);
		
		if (resMessage == null) {
			throw new HitDMException(5002, "Dm server does not respond:"+dmAddr+target);
		}
		

		if (resMessage.getHttpStatusCode() != 200) {
			String debugStr = "DM Server return:"+resMessage.getHttpStatusCode();
			byte[] body;
			try {
				body = resMessage.getContent();
				if (body != null) {
					debugStr += "\r\nBody:"+new String(body);
				}
			} catch (Exception e) {
				debugStr += e.getMessage();
			}
			
			throw new HitDMException(resMessage.getHttpStatusCode(), debugStr);
		}
		
		String resJson;
		try {
			List<Document> resultList = new ArrayList<Document>();
			
			resJson = new String(resMessage.getContent());
			
			JSONParser parser = new JSONParser();
			JSONArray jsonArray = (JSONArray)parser.parse(resJson);
			
			return jsonArray; 
			
		} catch (Exception e) {

			log.debug("Handled exception", e);
			
			throw new HitDMException(5001, "Invalid response:"+resMessage.toString());			
			
		}
	}
	
	private Document callPostApi(String to, Document content) throws HitDMException {

		OneM2mRequest reqMessage = new OneM2mRequest();
		String json = content.toJson();
		reqMessage.setContent(json.getBytes());

		reqMessage.setTo(to);
		reqMessage.setOperation(OPERATION.CREATE);
		reqMessage.setContentType(CONTENT_TYPE.JSON);
		
		OneM2mResponse resMessage = HttpClient.getInstance().sendRequest(dmAddr+to, reqMessage);
		
		if (resMessage == null) {
			throw new HitDMException(5002, "Dm server does not respond:"+dmAddr+to);
		}

		if (resMessage.getHttpStatusCode() != 200) {
			String debugStr = "DM Server return:"+resMessage.getHttpStatusCode();
			byte[] body;
			try {
				body = resMessage.getContent();
				if (body != null) {
					debugStr += "\r\nBody:"+new String(body);
				}
			} catch (Exception e) {
				debugStr += e.getMessage();
			}
			
			throw new HitDMException(resMessage.getHttpStatusCode(), debugStr);
		}
		
		String resJson;
		try {
			
			resJson = new String(resMessage.getContent());
			
			Document resultDoc = Document.parse(resJson);
			
			return resultDoc; 
			
		} catch (Exception e) {

			log.debug("Handled exception", e);
			
			throw new HitDMException(5001, "Invalid response:"+resMessage.toString());			
			
		}
		
	}
	
	protected HashMap<String, Object> readFileInfo(String deviceId, String url, int fileType) throws HitDMException {
HashMap<String, Object> resMap = new HashMap<String, Object>();
		
		String[] strItems = url.split("/");
		String fileName = strItems[strItems.length - 1];
		
		Document paramDoc = new Document();
		paramDoc.put("filename", fileName);
		
		OneM2mRequest reqMessage = new OneM2mRequest();
		
		String target = "/files/?query=";
		
		try {	
			String encodeResult = URLEncoder.encode(paramDoc.toJson(), "UTF-8");
			
			target = target + encodeResult;
		
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		reqMessage.setTo(target);
		reqMessage.setOperation(OPERATION.RETRIEVE);
		reqMessage.setContentType(CONTENT_TYPE.JSON);
		
		OneM2mResponse resMessage = HttpClient.getInstance().sendRequest(dmAddr+target, reqMessage);
		
		if (resMessage == null) {
			throw new HitDMException(5002, "Dm server does not respond:"+dmAddr+target);
		}
	

		if (resMessage.getHttpStatusCode() != 200) {
			String debugStr = "DM Server return:"+resMessage.getHttpStatusCode();
			byte[] body;
			try {
				
				body = resMessage.getContent();
				if (body != null) {
					debugStr += "\r\nBody:"+new String(body);
				}
			} catch (Exception e) {
				debugStr += e.getMessage();
			}
			
			throw new HitDMException(resMessage.getHttpStatusCode(), debugStr);
		}
		
		String resJson;
		try {
			List<Document> resultList = new ArrayList<Document>();
			
			resJson = new String(resMessage.getContent());
			
			JSONParser parser = new JSONParser();
			JSONArray jsonArray = (JSONArray)parser.parse(resJson);
			JSONObject jsonObj = null, metadata = null;
			String version = "";
			
			for(int i = 0; i < jsonArray.size(); i++) {
				jsonObj = (JSONObject)jsonArray.get(i);
				
				metadata = (JSONObject)jsonObj.get("metadata");
				
				if(metadata.get("fileType").toString().startsWith( String.valueOf(fileType) ) && jsonObj.get("filename").toString().equals(fileName)) {
					if(metadata.get("version") != null && metadata.get("version").toString().compareTo(version) >= 0) {
						version = metadata.get("version").toString();
					}
				}
			}
			
			resMap.put("version", version);
			resMap.put("filename", fileName);
			
		} catch (Exception e) {

			log.debug("Handled exception", e);
			
			throw new HitDMException(5001, "Invalid response:"+resMessage.toString());			
			
		}
		
		return resMap;
	}
	
	protected HashMap<String, Object> readFirmware(String deviceId, String url) throws HitDMException {
		HashMap<String, Object> resMap = new HashMap<String, Object>();
		
		String[] strItems = url.split("/");
		String fileName = strItems[strItems.length - 1];
		
		Document paramDoc = new Document();
		paramDoc.put("filename", fileName);
		
		OneM2mRequest reqMessage = new OneM2mRequest();
		
		String target = "/files/?query=";
		
		try {	
			String encodeResult = URLEncoder.encode(paramDoc.toJson(), "UTF-8");
			
			target = target + encodeResult;
		
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		reqMessage.setTo(target);
		reqMessage.setOperation(OPERATION.RETRIEVE);
		reqMessage.setContentType(CONTENT_TYPE.JSON);
		
		OneM2mResponse resMessage = HttpClient.getInstance().sendRequest(dmAddr+target, reqMessage);
		
		if (resMessage == null) {
			throw new HitDMException(5002, "Dm server does not respond:"+dmAddr+target);
		}
		
		

		if (resMessage.getHttpStatusCode() != 200) {
			String debugStr = "DM Server return:"+resMessage.getHttpStatusCode();
			byte[] body;
			try {
				
				body = resMessage.getContent();
				if (body != null) {
					debugStr += "\r\nBody:"+new String(body);
				}
			} catch (Exception e) {
				debugStr += e.getMessage();
			}
			
			throw new HitDMException(resMessage.getHttpStatusCode(), debugStr);
		}
		
		String resJson;
		try {
			List<Document> resultList = new ArrayList<Document>();
			
			resJson = new String(resMessage.getContent());
			
			JSONParser parser = new JSONParser();
			JSONArray jsonArray = (JSONArray)parser.parse(resJson);
			JSONObject jsonObj = null, metadata = null;
			String version = "";
			
			for(int i = 0; i < jsonArray.size(); i++) {
				jsonObj = (JSONObject)jsonArray.get(i);
				
				metadata = (JSONObject)jsonObj.get("metadata");
				
				if(metadata.get("fileType").toString().startsWith("1 Firmware Upgrade Image") && jsonObj.get("filename").toString().equals(fileName)) {
					if(metadata.get("version") != null && metadata.get("version").toString().compareTo(version) >= 0) {
						version = metadata.get("version").toString();
					}
				}
			}
			
			resMap.put("version", version);
			resMap.put("filename", fileName);
			
		} catch (Exception e) {

			log.debug("Handled exception", e);
			
			throw new HitDMException(5001, "Invalid response:"+resMessage.toString());			
			
		}
		
		return resMap;
	}
	
	protected HashMap<String, Object> read(String deviceId, List<String> moIds) throws HitDMException {
		
		if(timeout < 0) {
			timeout = 3000;
		}
		
		String to = "/devices/" + deviceId + "/tasks?timeout=" + this.timeout + "&connection_request";
		
		Document content = new Document();
		content.put("name", "getParameterValues");
		
		if(moIds != null) {
			content.put("parameterNames", moIds);
		} else {
			content.put("parameterNames", new ArrayList<String>());
		}		
		
		Document resultDoc = callPostApi(to, content);
		
		Document paramDoc = new Document();
		paramDoc.put("_id", deviceId);
		
		to = "/devices/?query=";
		
		JSONArray jsonArray = callGetApi(to, paramDoc);
		
		HashMap<String, Object> resMap = new HashMap<String, Object>();
		
		JSONObject jsonObj = (JSONObject)jsonArray.get(0);
		JSONObject device = (JSONObject)jsonObj.get("Device");
		JSONObject deviceInfo = (JSONObject)device.get("DeviceInfo");
		
		for(Iterator it = deviceInfo.keySet().iterator(); it.hasNext();) {
			String key = (String) it.next();
			for(String moId : moIds) {
				
				String item = moId.split("\\.")[2];
				
				if(item.equals(key)) {
					JSONObject selObject = (JSONObject)deviceInfo.get(key);
					
					if(moId.split("\\.").length == 4) {
						for(Iterator its = selObject.keySet().iterator(); its.hasNext();) {
							String subKey = (String) its.next();
							String subItem = moId.split("\\.")[3];
							if(subKey.equals(subItem)) {
								JSONObject valueObject = (JSONObject)selObject.get(subKey);
								
								resMap.put(moId, this.getValue(valueObject));
								
							}
						}
					} else if(moId.split("\\.").length == 6) {
						
						String subItem = moId.split("\\.")[3];
						selObject = (JSONObject)selObject.get(subItem);
						subItem = moId.split("\\.")[4];
						selObject = (JSONObject)selObject.get(subItem);
						//selObject = (JSONObject)selObject.get("0");
						
						for(Iterator its = selObject.keySet().iterator(); its.hasNext();) {
							String subKey = (String) its.next();
							subItem = moId.split("\\.")[5];
							
							if(subKey.equals(subItem)) {
								JSONObject valueObject = (JSONObject)selObject.get(subKey);
								
								resMap.put(moId, this.getValue(valueObject));
								
							}
						}
						
						
					} else {
						resMap.put(moId, this.getValue(selObject));
					}
					
				}
			} 
			//System.out.println(key + "=>" + deviceInfo.get(key).toString());
		}
		
		return resMap;
	}
	
	protected Document write(String deviceId, HashMap<String, Object> mos) throws HitDMException {
		
		if(timeout < 0) {
			timeout = 3000;
		}
		
		String to = "/devices/" + deviceId + "/tasks?timeout=" + this.timeout + "&connection_request";
		
		Document content = new Document();
		content.put("name", "setParameterValues");
		
		List<Object> resultParams = new ArrayList<Object>();
		
		if(mos.size() > 0) {	
			List<Object> keyValue = null;
			
			for(Iterator it = mos.keySet().iterator(); it.hasNext();) {
				String key = (String) it.next();
				Object value = mos.get(key);
				
				keyValue = new ArrayList<Object>();
				
				keyValue.add(key);
				keyValue.add(value);
				
				resultParams.add(keyValue);
				
				keyValue = null;
			}
			
		} 
		
		content.put("parameterValues", resultParams);
		
		Document resultDoc = callPostApi(to, content);
		
		return resultDoc;
	}
	
	protected Document execute(String deviceId, HashMap<String, Object> mos) throws HitDMException {
		
		if(timeout < 0) {
			timeout = 3000;
		}
		
		String to = "/devices/" + deviceId + "/tasks?timeout=" + this.timeout + "&connection_request";
		
		Document content = new Document();
		content.put("name", "setParameterValues");
		
		List<Object> resultParams = new ArrayList<Object>();
		
		if(mos.size() > 0) {	
			List<Object> keyValue = null;
			
			for(Iterator it = mos.keySet().iterator(); it.hasNext();) {
				String key = (String) it.next();
				Object value = mos.get(key);
				
				keyValue = new ArrayList<Object>();
				
				keyValue.add(key);
				keyValue.add(value);
				
				resultParams.add(keyValue);
				
				keyValue = null;
			}
			
		} 
		
		content.put("parameterValues", resultParams);
		
		Document resultDoc = callPostApi(to, content);
		
		return resultDoc;
	}
	
	protected Document reboot(String deviceId) throws HitDMException {
		if(timeout < 0) {
			timeout = 3000;
		}
		
		String to = "/devices/" + deviceId + "/tasks?timeout=" + this.timeout + "&connection_request";
		
		Document content = new Document();
		content.put("name", "reboot");
		
		Document resultDoc = callPostApi(to, content);
		
		return resultDoc;
		
	}
	
	protected Document factoryReset(String deviceId) throws HitDMException {
		if(timeout < 0) {
			timeout = 3000;
		}
		
		String to = "/devices/" + deviceId + "/tasks?timeout=" + this.timeout + "&connection_request";
		
		Document content = new Document();
		content.put("name", "factoryReset");
		
		Document resultDoc = callPostApi(to, content);
		
		return resultDoc;
		
	}
	
	protected Document download(String deviceId, String fileName) throws HitDMException {
		if(timeout < 0) {
			timeout = 3000;
		}
		
		Document paramDoc = new Document();
		paramDoc.put("_id", fileName);
		
		String to = "/files/?query=";
		
		JSONArray jsonArray = callGetApi(to, paramDoc);
		JSONObject jsonObj = (JSONObject)jsonArray.get(0);
		
		String md5 = jsonObj.get("md5").toString();
		
		if(md5 == null || fileName == null) {
			return null;
		} else {
			to = "/devices/" + deviceId + "/tasks?timeout=" + this.timeout + "&connection_request";
			
			Document content = new Document();
			content.put("name", "download");
			//content.put("file", md5);			// Not supported in genieACS 
			content.put("file", fileName);
			content.put("filename", fileName);
			
			Document resultDoc = callPostApi(to, content);
			
			return resultDoc;
		}
		
	}	
	
	public void upload(String deviceId, String fileType, String url, String filePath, String version) throws HttpException, IOException, HitDMException {
		Document resultDoc = new Document();
		
		String oui = deviceId.split("-")[0];
		String productClass = deviceId.split("-")[1];
		
		callPutFileApi(fileType, url, filePath, oui, productClass, version);
		
	}
	
	public static void main(String[] args) throws Exception {
		
		Tr069DMAdapter adaptor = new Tr069DMAdapter("http://10.10.0.82:7557", 3000);
		
		try {
			String deviceId = "625009-TTB2706-T2T1B9";
			int timeout = 3000;
			
			Document content = new Document();
			content.put("name", "getParameterValues");
			
			//List<String> list = new ArrayList<String>();
			//list.add(MOUri.DVC_TEMP_STATUS);
			//list.add(MOUri.DVC_OUI);
			HashMap<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put(MOUri.DVC_LED_STATUS, true);
			
			Document resultDoc = adaptor.write(deviceId, paramMap);
			
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

}
