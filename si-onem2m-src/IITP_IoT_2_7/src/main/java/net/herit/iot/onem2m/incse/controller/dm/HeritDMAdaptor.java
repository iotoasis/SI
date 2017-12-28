package net.herit.iot.onem2m.incse.controller.dm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.message.onem2m.OneM2mRequest.OPERATION;
import net.herit.iot.message.onem2m.OneM2mResponse.RESPONSE_STATUS;
import net.herit.iot.message.onem2m.format.Enums.CONTENT_TYPE;
import net.herit.iot.onem2m.bind.http.client.HttpClient;
import net.herit.iot.onem2m.core.util.OneM2MException;

public class HeritDMAdaptor {
	
	private String dmAddr;
	
	private Logger log = LoggerFactory.getLogger(HeritDMAdaptor.class);


	static class MOUri {
		
		public static final String SVR_SERVER_ID = "/1/-/0";
		public static final String SVR_LIFE_TIME = "/1/-/1";
		public static final String SVR_NOTI_STOR_WHEN_OFFLINE = "/1/-/2";
		public static final String SVR_BINDING = "/1/-/3";
		public static final String SVR_SECRET_KEY = "/1/-/4";
		public static final String SVR_ID = "/1/-/5";
		public static final String SVR_PASSWORD = "/1/-/6";
		
		public static final String DVC_MANUFACTURER = "/2/-/0";
		public static final String DVC_MODEL_NUMBER = "/2/-/1";
		public static final String DVC_SERIAL_NUMBER = "/2/-/2";
		public static final String DVC_ENDPOINT_NAME = "/2/-/3";
		public static final String DVC_REBOOT = "/2/-/4";
		public static final String DVC_FACTORY_RESET = "/2/-/5";
		public static final String DVC_AVAL_PWR_SRC = "/2/-/6";
		public static final String DVC_PWR_SRC_VOLT = "/2/-/7";
		public static final String DVC_PWR_SRC_CURR = "/2/-/8";
		public static final String DVC_BATTERY_LEVEL = "/2/-/9";
		public static final String DVC_CPU_MAX = "/2/-/10";
		public static final String DVC_CPU_MAX_TIME = "/2/-/11";
		public static final String DVC_CPU_USAGE = "/2/-/12";
		public static final String DVC_CURRENT_TIME = "/2/-/13";
		public static final String DVC_UTC_OFFSET = "/2/-/14";
		public static final String DVC_TIMEZONE = "/2/-/15";
		public static final String DVC_SUPPORT_BIND_MODE = "/2/-/16";
		public static final String DVC_CPU_TYPE = "/2/-/17";
		public static final String DVC_MEM_TYPE = "/2/-/18";
		public static final String DVC_OS_TYPE = "/2/-/19";
		public static final String DVC_OS_VERSION = "/2/-/29";
		public static final String DVC_BOOT_TIME = "/2/-/21";
		public static final String DVC_LAST_CONN_TIME = "/2/-/22";
		public static final String DVC_FLASH_TOTAL = "/2/-/23";
		public static final String DVC_FLASH_FREE = "/2/-/24";
		public static final String DVC_RAM_TOTAL = "/2/-/25";
		public static final String DVC_RAM_FREE = "/2/-/26";
		public static final String DVC_DISK_TOTAL = "/2/-/27";
		public static final String DVC_DISK_FREE = "/2/-/28";
		public static final String DVC_ERROR_CODE = "/2/-/29";

		public static final String FW_VERSION = "/4/-/0";
		public static final String FW_PACKAGE_NAME = "/4/-/1";
		public static final String FW_STATE = "/4/-/2";
		public static final String FW_UPDATE_RESULT = "/4/-/3";
		public static final String FW_UPDATE = "/4/-/4";

		public static final String LOC_LATITUDE = "/5/-/0";
		public static final String LOC_LONGITUDE = "/5/-/1";
		public static final String LOC_ALTITUDE = "/5/-/2";
		public static final String LOC_TIMESTAMP = "/5/-/3";

		public static final String CONNSTAT_TX = "/6/-/0";
		public static final String CONNSTAT_RX = "/6/-/1";
		public static final String CONNSTAT_MAX_MSG_SIZE = "/6/-/2";
		public static final String CONNSTAT_AVG_MSG_SIZE = "/6/-/3";
		public static final String CONNSTAT_STARTORRESET = "/6/-/4";

		public static final String DIAG_DEBUG_ON = "/7/-/0";
		public static final String DIAG_DEBUG_OFF = "/7/-/1";
		public static final String DIAG_DEBUG_STATUS = "/7/-/2";
		public static final String DIAG_DIAGNOSIS = "/7/-/3";
		public static final String DIAG_DIAG_RESULT = "/7/-/4";
		
	}
	
	public HeritDMAdaptor(String dmAddr) {
		
		this.dmAddr = dmAddr;
		
	}
	
	private Document callApi(String to, Document content) throws HitDMException {

		OneM2mRequest reqMessage = new OneM2mRequest();
		String json = content.toJson();
		reqMessage.setContent(json.getBytes());

		reqMessage.setTo(to);
		reqMessage.setOperation(OPERATION.CREATE);
		reqMessage.setContentType(CONTENT_TYPE.JSON);
		
//		OneM2mResponse resMessage = new HttpClient().process(dmAddr+to, reqMessage);
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
			
			Document resDoc = Document.parse(resJson);

			String result = resDoc.getString("r");
				
			if (!result.equals("200")) {
				throw new HitDMException(Integer.parseInt(result), "DM Server return:"+resJson);
			}
			if (resDoc.getString("r") == null) {
				resDoc.put("r", resMessage.getHttpStatusCode());
			}
			
			return resDoc; 
			
		} catch (Exception e) {

			log.debug("Handled exception", e);
			
			throw new HitDMException(5001, "Invalid response:"+resMessage.toString());
			//throw new OneM2MException(RESPONSE_STATUS.DMSERVER_ERROR, "Invalid response:"+resMessage.toString());			
			
		}
		
	}
	protected HashMap<String, String> read(String deviceId, List<String> moIds) throws HitDMException {

		String to = "/hit/openapi/dm/read";
		
		Document content = new Document();
		content.put("d", deviceId);
		
		List<Document> list = new ArrayList<Document>();
		Iterator<String> it = moIds.iterator();
		while (it.hasNext()) {
			Document e = new Document();
			e.put("n", it.next());
			list.add(e);
		}
		content.put("e", list);
		
		Document resDoc = callApi(to, content);
		List<Document> e = (List<Document>)resDoc.get("e");
			
			
		HashMap<String, String> resMap = new HashMap<String, String>();
		
		Iterator it2 = e.iterator();
		while (it2.hasNext()) {
			Document r = (Document)it2.next();
			resMap.put(r.getString("n"), r.getString("sv"));
		}
		
		return resMap;
	
	}
	
	protected Document write(String deviceId, HashMap<String, String> mos) throws HitDMException {

		String to = "/hit/openapi/dm/write";
		
		Document content = new Document();
		content.put("d", deviceId);
		
		List<Document> list = new ArrayList<Document>();
		Set<String> set = mos.keySet();
		
		Iterator<String> it = set.iterator();
		
		while (it.hasNext()) {
			Document e = new Document();
			String key = it.next();
			String val = mos.get(key);
			e.put("n", key);
			e.put("sv", val);
			list.add(e);
		}
		content.put("e", list);
		
		return callApi(to, content);
				
	}
	
	protected Document execute(String deviceId, HashMap<String, String> mos) throws HitDMException {

		String to = "/hit/openapi/dm/write";
		
		Document content = new Document();
		content.put("d", deviceId);
		
		List<Document> list = new ArrayList<Document>();
		Set<String> set = mos.keySet();
		
		Iterator<String> it = set.iterator();
		
		while (it.hasNext()) {
			Document e = new Document();
			String key = it.next();
			String val = mos.get(key);
			e.put("n", key);
			e.put("sv", val);
			list.add(e);
		}
		content.put("e", list);
		
		return callApi(to, content);
					
	}

	
	protected Document firmwareUpgrade(String deviceId, String packageName, String version) throws HitDMException {

		String to = "/hit/openapi/dm/firmware_upgrade";
		
		Document content = new Document();
		content.put("d", deviceId);
		content.put("pk", packageName);
		content.put("fv", version);
		
		return callApi(to, content);
				
	}

	
	protected Document startDebug(String deviceId, String interval, String duration) throws HitDMException {

		String to = "/hit/openapi/dm/debug_start";
		
		Document content = new Document();
		content.put("d", deviceId);
		content.put("it", interval);
		content.put("du", duration);
		
		return callApi(to, content);				
		
	}

	
	protected Document stopDebug(String deviceId) throws HitDMException {

		String to = "/hit/openapi/dm/debug_stop";
		
		Document content = new Document();
		content.put("d", deviceId);
		
		return callApi(to, content);				
		
	}

	
	protected Document reboot(String deviceId) throws HitDMException {

		String to = "/hit/openapi/dm/reboot";
		
		Document content = new Document();
		content.put("d", deviceId);
		
		return callApi(to, content);	
		
	}

	
	protected Document factoryReset(String deviceId) throws HitDMException {

		String to = "/hit/openapi/dm/factory_reset";
		
		Document content = new Document();
		content.put("d", deviceId);
		
		return callApi(to, content);	
		
	}
		
}
