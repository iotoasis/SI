package net.herit.iot.onem2m.incse.controller.dm;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.message.onem2m.OneM2mRequest.OPERATION;
import net.herit.iot.message.onem2m.format.Enums.CONTENT_TYPE;
import net.herit.iot.onem2m.bind.http.client.HttpClient;

import org.bson.Document;
import org.json.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OneM2MDmAdapter {
	private String dmAddr;
	
	
	private Logger log = LoggerFactory.getLogger(OneM2MDmAdapter.class);
	
	public OneM2MDmAdapter(String dmAddr) {
		this.dmAddr = dmAddr;
	}
	
	private JSONArray callJsonGetApi(String to, Document content) throws HitDMException {
		
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
	
	private Document callDocGetApi(String to, Document content) throws HitDMException, IOException {

	/*	OneM2mRequest reqMessage = new OneM2mRequest();
		String json = content.toJson();
		reqMessage.setContent(json.getBytes());

		reqMessage.setTo(to);
		reqMessage.setOperation(OPERATION.RETRIEVE);
		reqMessage.setContentType(CONTENT_TYPE.JSON);
		
		OneM2mResponse resMessage = HttpClient.getInstance().sendRequest(dmAddr+to, reqMessage);
		
		if (resMessage == null) {
			throw new HitDMException(5002, "oneM2M Agent does not respond:"+dmAddr+to);
		}

		if (resMessage.getHttpStatusCode() != 200) {
			String debugStr = "oneM2M Agent return:"+resMessage.getHttpStatusCode();
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
			
			Document resDoc = Document.parse(resJson);

			String result = resDoc.getString("r");
				
			if (!result.equals("200")) {
				throw new HitDMException(Integer.parseInt(result), "oneM2M Agent return:"+resJson);
			}
			if (resDoc.getString("r") == null) {
				resDoc.put("r", resMessage.getHttpStatusCode());
			}
			
			return resDoc; 
			
		} catch (Exception e) {

			log.debug("Handled exception", e);
			
			throw new HitDMException(5001, "Invalid response:"+resMessage.toString());			
			
		} */
	
		GenericHttpClient hc = new GenericHttpClient(dmAddr+to);
		hc.openConnection();
		hc.setRequestHeader();
		hc.setRequestMethod("GET");
		
		String result;
		try {
			result = hc.getResponseString();
			
			Document resDoc = Document.parse(result);
			
			return resDoc;
		} catch(Exception e) {
			log.debug("Handled exception", e);
			throw new HitDMException(5001, "Invalid response:"+ e.toString());
		}
		
	}
	
	private Document callPostApi(String to, Document content) throws HitDMException, IOException {
	/*	
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
	*/
		GenericHttpClient hc = new GenericHttpClient(dmAddr+to);
		hc.openConnection();
		hc.setRequestHeader();
		hc.setRequestMethod("POST");
		
		String result;
		try {
			hc.sendRequest(content.toJson());
			result = hc.getResponseString();
			
			Document resDoc = new Document();
			
			if(result.equalsIgnoreCase("OK")) {
				resDoc.append("result", "200");
				resDoc.append("message", "successful");
			} else {
				resDoc.append("result", "400");
				resDoc.append("message", "failure");
			}
			
			return resDoc;
		} catch(Exception e) {
			log.debug("Handled exception", e);
			throw new HitDMException(5001, "Invalid response:"+ e.toString());
		}
		
	}
	
	protected Document factoryReset(String deviceId) throws HitDMException, IOException {

		String to = "/shutdown";
		
		Document content = new Document();
		content.put("d", deviceId);
		
		return callDocGetApi(to, content);	
		
	}
	
	protected Document reboot(String deviceId) throws HitDMException, IOException {

		String to = "/reboot";
		
		Document content = new Document();
		content.put("d", deviceId);
		
		return callDocGetApi(to, content);	
		
	}
	
	protected Document download(String deviceId, String url) throws HitDMException, IOException {

		String to = "/download";
		
		Document content = new Document();
		content.put("d", deviceId);
		content.put("url", url);
		
		return callPostApi(to, content);	
		
	}
	
	protected Document softwareInstall(String deviceId, String url, String version, String packageName) throws HitDMException, IOException {
		String to = "/software/install";
		
		Document content = new Document();
		content.put("d", deviceId);
		content.put("url", url);
		content.put("version", version);
		content.put("pkgName", packageName);
		
		// return callDocGetApi(to, content); blocked in 2017-09-16
		return callPostApi(to, content);
	}
	
	protected Document softwareUninstall(String deviceId, String url, String packageName) throws HitDMException, IOException {
		String to = "/software/unstall";
		
		Document content = new Document();
		content.put("d", deviceId);
		content.put("url", url);
		content.put("pkgName", packageName);
		
		//return callDocGetApi(to, content);	blocked in 2017-09-16
		return callPostApi(to, content);
	}
	
	protected Document firmwareUpdate(String deviceId, String url, String version, String packageName) throws HitDMException, IOException {
		String to = "/firmware";
		
		Document content = new Document();
		content.put("d", deviceId);
		content.put("url", url);
		content.put("version", version);
		content.put("pkgName", packageName);
		
		return callPostApi(to, content);
	}
	
	protected Document readDeviceStatus(String deviceId, String command) throws HitDMException, IOException {
		String to = "/" + command;
		
		Document content = new Document();
		content.put("d", deviceId);
		
		return callDocGetApi(to, content);
	}
	
	protected Document readMemoryStatus(String deviceId) throws HitDMException, IOException {
		String to = "/freemem";
		
		Document content = new Document();
		content.put("d", deviceId);
		
		return callDocGetApi(to, content);
	}
	
	protected Document readFirmwareStatus(String deviceId) throws HitDMException, IOException {
		String to = "/freemem";
		
		Document content = new Document();
		content.put("d", deviceId);
		
		return callDocGetApi(to, content);
	}
}
