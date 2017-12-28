package net.herit.business.dm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import net.herit.business.api.service.OneM2MApiService;
import net.herit.business.dm.service.DeviceManagerService;
import net.herit.business.dm.service.NotificationVO;
import net.herit.business.onem2m.service.ConfigurationService;
import net.herit.business.onem2m.service.PlatformService;
import net.herit.common.conf.HeritProperties;
import net.herit.iot.message.onem2m.OneM2mRequest.RESOURCE_TYPE;
import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.security.dto.GroupAuthorization;

@Controller
@RequestMapping(value="/dm")
public class DeviceManagerController {

	@Resource(name = "DeviceManagerService")
	private DeviceManagerService deviceManagerService;
	
	@Resource(name = "ConfigurationService")
	private ConfigurationService configurationService;

	@Resource(name = "PlatformService")
	private PlatformService platformService;
	
	private String contextBrokerAddress;
	private String fiwareService;
	private String fiwareServicePath;
	private String fiwareAgentAccessKey;
	private String fiwareAgentUrl;
	
	// added in 2017-09-18 for mongo connection
	private MongoClient mongoClient;
	private DB db;
	
	public DeviceManagerController() {
		contextBrokerAddress = "http://" + HeritProperties.getProperty("Globals.fiwareServerHost") + ":" + HeritProperties.getProperty("Globals.fiwareServerPort");
		fiwareService = HeritProperties.getProperty("Globals.fiwareService");
		fiwareServicePath = HeritProperties.getProperty("Globals.fiwareServicePath");
		fiwareAgentAccessKey = HeritProperties.getProperty("Globals.fiwareAgentAccessKey");
		fiwareAgentUrl = HeritProperties.getProperty("Globals.fiwareAgentUrl");
		
		// added in 2017-09-18
		mongoClient = new MongoClient(HeritProperties.getProperty("Globals.MongoDB.Host"), Integer.parseInt( HeritProperties.getProperty("Globals.MongoDB.Port") ) );
		//db = mongoClient.getDatabase(HeritProperties.getProperty("Globals.MongoDB.DBName"));
		db = mongoClient.getDB(HeritProperties.getProperty("Globals.MongoDB.DBName"));

		
	}
	
	//@RequestMapping(value="/onem2m.do")
	@RequestMapping(value="/{page}.do")
	public String oneM2mDeviceMonitor(@PathVariable("page") String pageName, HttpServletRequest request, Locale locale, ModelMap model) throws Exception {
		HttpSession session = request.getSession(false);
		if(session != null){
			//페이지 권한 확인
			GroupAuthorization requestAuth = (GroupAuthorization) session.getAttribute("requestAuth");
			if(!requestAuth.getAuthorizationDBRead().equals("1")){
				model.addAttribute("authMessage", "fiware 페이지 접근 권한이 없습니다.");
				return "forward:" + HeritProperties.getProperty("Globals.MainPage");
			}
		}
		
		String resourceUri = request.getParameter("uri");
		OneM2MApiService onem2mService = OneM2MApiService.getInstance();
		String from = onem2mService.getAppName();
		
		if (resourceUri == null) {
			resourceUri = "/herit-in/herit-cse"; 
		}
		
		model.addAttribute("onem2m_host", HeritProperties.getProperty("Globals.cseAddr"));
		model.addAttribute("cse_base", HeritProperties.getProperty("Globals.csebaseName"));
		model.addAttribute("cse_id", HeritProperties.getProperty("Globals.cseId"));
		
		JSONArray retDeviceList = new JSONArray();
		
		if(pageName.equals("mobius")) {
			
			resourceUri = HeritProperties.getProperty("Globals.csebase");
			
			OneM2mResponse response = onem2mService.retrieve(resourceUri, from);
			System.out.println("####### trace-1");
			System.out.println(String.format("###### response.getContent() = %s", new String( response.getContent(), "UTF-8") ));
			JSONObject json = new JSONObject( new String( response.getContent(), "UTF-8") );
			JSONArray jsonAes = json.getJSONArray("m2m:ae");
			
			JSONObject retJson = null;
			
			for(int i = 0; i < jsonAes.length(); i++) {
				JSONObject jsonAe = jsonAes.getJSONObject(i);
				
				if(jsonAe.getString("api") != null && !jsonAe.getString("api").equals( HeritProperties.getProperty("Globals.appId") ) ) {
					retJson = new JSONObject();
					retJson.put("aei", jsonAe.getString("aei"));
					retJson.put("ae_rn", jsonAe.getString("rn"));
					retJson.put("ct", jsonAe.getString("ct"));
					
					retDeviceList.put(retJson);
				}
			}
			System.out.println(String.format("###### AE JSON length = %d", jsonAes.length()));
			
			model.addAttribute("main_uri", resourceUri);
			model.addAttribute("device_list", retDeviceList);
			
		} else {
			String response = onem2mService.discoverResource(resourceUri, from, RESOURCE_TYPE.AE);
			
			JSONObject json = new JSONObject(response);
			
			JSONArray jsonUriList = json.getJSONObject("uril").getJSONArray("URIList");
			
			List<Object> uriList = jsonUriList.toList();
			
			String aeResoureName, resourceNodeId = "", unstructResourceUri = "";
			
			JSONObject retJson = null;
			for(Object objUri : uriList) {
				
				retJson = new JSONObject();
				
				response = onem2mService.getResourceString(objUri.toString(), from);
				json = new JSONObject(response);
				
				aeResoureName = json.getJSONObject("m2m:ae").getString("rn");
				
				if(json.getJSONObject("m2m:ae").get("nl") != null) {
					resourceNodeId = json.getJSONObject("m2m:ae").getString("nl");
					
					unstructResourceUri = "/" + HeritProperties.getProperty("Globals.cseId") + "/" + resourceNodeId;
					response = onem2mService.getResourceString(unstructResourceUri, from);
				} else {
					resourceNodeId = json.getJSONObject("m2m:ae").getString("nl");
					
					unstructResourceUri = "/" + HeritProperties.getProperty("Globals.cseId") + "/" + resourceNodeId;
					response = "{}";
				}
				
				
				retJson.put("ae_rn", aeResoureName);
				retJson.put("node", new JSONObject(response));
				
				retDeviceList.put(retJson);
				
			}
			
			response = onem2mService.discoverResource(resourceUri, from, RESOURCE_TYPE.MGMT_CMD);
			json = new JSONObject(response);
			jsonUriList = json.getJSONObject("uril").getJSONArray("URIList");
			uriList = jsonUriList.toList();
			
			JSONArray retMgmtCmdList = new JSONArray();
			
			for(Object objUri2 : uriList) {
				response = onem2mService.getResourceString(objUri2.toString(), from);
				retMgmtCmdList.put(new JSONObject(response));
			}
			
			
			model.addAttribute("main_uri", resourceUri);
			model.addAttribute("device_list", retDeviceList);
			model.addAttribute("mgmt_cmd_list", retMgmtCmdList);
		}
		
		String remoteCseUri = HeritProperties.getProperty("Globals.csebase");
		if(remoteCseUri != null && !remoteCseUri.equals("")) {
			String[] splitItems = remoteCseUri.split("/");
			if(splitItems.length >= 3) {
				model.addAttribute("remote_cse_id", splitItems[splitItems.length - 1]);
				model.addAttribute("remote_cse_base", splitItems[splitItems.length - 2]);
			}
		}
		
		//return "/v2/dm/onem2m";
		return "/v2/dm/" + pageName;
		
	}
	
	@ResponseBody
	@RequestMapping(value="/postExecuteMessage.do")
	public Map<String, Object> postExecuteMessage(HttpServletRequest request) throws Exception {
		Map<String, Object> response = new HashMap<String, Object>();
		HttpSession session = request.getSession(false);
		
		if(session != null){
			//페이지 권한 확인
			GroupAuthorization requestAuth = (GroupAuthorization) session.getAttribute("requestAuth");
			if(!requestAuth.getAuthorizationDBRead().equals("1")){
				response.put("result", 1);
				response.put("errorCode", -1);
				response.put("content", "authMessage: onem2m 페이지 접근 권한이 없습니다.");
			} else {
				
				String resourceUri = request.getParameter("uri");
				String value = request.getParameter("value");
				
				String body = "{ \"con\":" + value  + "}";
				
				OneM2MApiService onem2mService = OneM2MApiService.getInstance();
				String from = onem2mService.getAppName();
				
				int nResult = onem2mService.createResourceOther(resourceUri, from, body, 4);
				
				response.put("result", 0);
				response.put("errorCode", 0);
				response.put("content", String.valueOf(nResult));
				
			}
		} else {
			response.put("result", 1);
			response.put("errorCode", -1);
			response.put("content", "session is null");
		}
		
		return response;
	}
	
	@ResponseBody
	@RequestMapping(value="/getCurrentNotification.do")
	public Map<String, Object> getCurrentNotification(HttpServletRequest request) throws Exception {
		Map<String, Object> response = new HashMap<String, Object>();
		HttpSession session = request.getSession(false);
		
		if(session != null){
			//페이지 권한 확인
			GroupAuthorization requestAuth = (GroupAuthorization) session.getAttribute("requestAuth");
			if(!requestAuth.getAuthorizationDBRead().equals("1")){
				response.put("result", 1);
				response.put("errorCode", -1);
				response.put("content", "authMessage: onem2m 페이지 접근 권한이 없습니다.");
			} else {
				
				NotificationVO notiVo = deviceManagerService.getCurrentNotificationInfo();
					
				response.put("result", 0);
				response.put("errorCode", 0);
				response.put("content", notiVo.toJsonString());
				
			}
		} else {
			response.put("result", 1);
			response.put("errorCode", -1);
			response.put("content", "session is null");
		}
		
		return response;
	}
	
	@ResponseBody
	@RequestMapping(value="/getOneM2mContainers.do")
	public Map<String, Object> getOneM2mContainers(HttpServletRequest request) throws Exception {
		
		Map<String, Object> response = new HashMap<String, Object>();
		HttpSession session = request.getSession(false);
		
		JSONArray retContainerList = new JSONArray();
		
		if(session != null){
			//페이지 권한 확인
			GroupAuthorization requestAuth = (GroupAuthorization) session.getAttribute("requestAuth");
			if(!requestAuth.getAuthorizationDBRead().equals("1")){
				response.put("result", 1);
				response.put("errorCode", -1);
				response.put("content", "authMessage: onem2m 페이지 접근 권한이 없습니다.");
			} else {
				String uri = request.getParameter("uri");
				
				OneM2MApiService onem2mService = OneM2MApiService.getInstance();
				String from = onem2mService.getAppName();
				
				OneM2mResponse oneM2MResponse = onem2mService.retrieve(uri, from);
				
				JSONObject json = new JSONObject( new String( oneM2MResponse.getContent(), "UTF-8") );
				JSONArray jsonContainers = json.getJSONArray("m2m:cnt");
				
				JSONObject retJson = null;
				
				for(int i = 0; i < jsonContainers.length(); i++) {
					JSONObject jsonContainer = jsonContainers.getJSONObject(i);
		
					if(jsonContainer.getString("rn") != null && jsonContainer.getString("rn").startsWith("cnt") )  {
						retJson = new JSONObject();
						retJson.put("cnt_rn", jsonContainer.getString("rn"));
						retJson.put("cnt_cni", jsonContainer.getInt("cni"));
						
						retContainerList.put(retJson); 
					} 
				}	
				response.put("result", 0);
				response.put("errorCode", 0);
				response.put("content", retContainerList.toString());
				
			}
		} else {
			response.put("result", 1);
			response.put("errorCode", -1);
			response.put("content", "session is null");
		}
		
		return response;
	}
	
	
	@ResponseBody
	@RequestMapping(value="/getOneM2mMgmtResource.do")
	public Map<String, Object> getOneM2mMgmtResource(HttpServletRequest request) throws Exception {
		
		Map<String, Object> response = new HashMap<String, Object>();
		HttpSession session = request.getSession(false);
		
		if(session != null){
			//페이지 권한 확인
			GroupAuthorization requestAuth = (GroupAuthorization) session.getAttribute("requestAuth");
			if(!requestAuth.getAuthorizationDBRead().equals("1")){
				response.put("result", 1);
				response.put("errorCode", -1);
				response.put("content", "authMessage: onem2m 페이지 접근 권한이 없습니다.");
			} else {
				
				String uri = request.getParameter("uri");
				//System.out.println("###################################### uri = " + uri);
				if (uri == null || uri.equals("")) {
					response.put("result", 1);
					response.put("errorCode", -1);
					response.put("content", "parameters[uri] is missing");
				}
				
				OneM2MApiService onem2mService = OneM2MApiService.getInstance();
				String from = onem2mService.getAppName();
				String strResult = onem2mService.getResourceString(uri, from);
				
				response.put("result", 0);
				response.put("errorCode", 0);
				response.put("content", strResult);
				
			}
		} else {
			response.put("result", 1);
			response.put("errorCode", -1);
			response.put("content", "session is null");
		}
		
		return response;
	}
	
	@ResponseBody
	@RequestMapping(value="/execMgmtCmd.do")
	public Map<String, Object> execMgmtCmd(HttpServletRequest request) throws Exception {
		Map<String, Object> response = new HashMap<String, Object>();
		HttpSession session = request.getSession(false);
		
		if(session != null){
			//페이지 권한 확인
			GroupAuthorization requestAuth = (GroupAuthorization) session.getAttribute("requestAuth");
			if(!requestAuth.getAuthorizationDBRead().equals("1")){
				response.put("result", 1);
				response.put("errorCode", -1);
				response.put("content", "authMessage: onem2m 페이지 접근 권한이 없습니다.");
			} else {
				
				String uri = request.getParameter("uri");
				
				//System.out.println("###################################### uri = " + uri);
				if (uri == null || uri.equals("")) {
					response.put("result", 1);
					response.put("errorCode", -1);
					response.put("content", "parameters[uri] is missing");
				}
				
				String body = "{\"m2m:mgc\": {\"exe\": true}}";
								
				OneM2MApiService onem2mService = OneM2MApiService.getInstance();
				String from = onem2mService.getAppName();
				int nResult = onem2mService.updateResource(uri, from, body);
				
				response.put("result", 0);
				response.put("errorCode", 0);
				response.put("content", nResult);
				
			}
		} else {
			response.put("result", 1);
			response.put("errorCode", -1);
			response.put("content", "session is null");
		}
		
		return response;
	}
	
	@ResponseBody
	@RequestMapping(value="/getMgmtCmdResult.do")
	public Map<String, Object> getMgmtCmdResult(HttpServletRequest request) throws Exception {
		Map<String, Object> response = new HashMap<String, Object>();
		HttpSession session = request.getSession(false);
		
		if(session != null){
			//페이지 권한 확인
			GroupAuthorization requestAuth = (GroupAuthorization) session.getAttribute("requestAuth");
			if(!requestAuth.getAuthorizationDBRead().equals("1")){
				response.put("result", 1);
				response.put("errorCode", -1);
				response.put("content", "authMessage: onem2m 페이지 접근 권한이 없습니다.");
			} else {
				String collection = HeritProperties.getProperty("Globals.MongoDB.Collection");
				DBCollection col = db.getCollection(collection);
				
				String rn = request.getParameter("rn");
				
				if (rn == null || rn.equals("")) {
					response.put("result", 1);
					response.put("errorCode", -1);
					response.put("content", "parameters[rn] is missing");
				}
				
				BasicDBObject query = new BasicDBObject();
				query.put("ty", 8);
				query.put("ext", rn);
				
				DBCursor cursor = col.find(query);
				
				List<DBObject> dbObjList = new ArrayList<DBObject>();
				try {
				    while (cursor.hasNext()) {
				
				        dbObjList.add(cursor.next());
				        //System.out.println("############# dbObjList.toString() = " + dbObjList.toString());
				    }
				} finally {
				    cursor.close();
				}
				
				response.put("result", 0);
				response.put("errorCode", 0);
				response.put("content", dbObjList.toString());
				
			}
		} else {
			response.put("result", 1);
			response.put("errorCode", -1);
			response.put("content", "session is null");
		}
		
		return response;
	}
	
	@ResponseBody
	@RequestMapping(value="/execMgmtObj.do")
	public Map<String, Object> execMgmtObj(HttpServletRequest request) throws Exception {
		Map<String, Object> response = new HashMap<String, Object>();
		HttpSession session = request.getSession(false);
		
		if(session != null){
			//페이지 권한 확인
			GroupAuthorization requestAuth = (GroupAuthorization) session.getAttribute("requestAuth");
			if(!requestAuth.getAuthorizationDBRead().equals("1")){
				response.put("result", 1);
				response.put("errorCode", -1);
				response.put("content", "authMessage: onem2m 페이지 접근 권한이 없습니다.");
			} else {
				
				String uri = request.getParameter("uri");
				String attrName = request.getParameter("attr");
				String cmdType = request.getParameter("cmd");
			
				if ((uri == null || uri.equals("")) || (attrName == null || attrName.equals("")) || (cmdType == null || cmdType.equals("")) ) {
					response.put("result", 1);
					response.put("errorCode", -1);
					response.put("content", "parameters[uri or attr] are missing");
				}
				
				String body = "{\"m2m:" + cmdType + "\": {\"" + attrName + "\": true}}";
				
				//System.out.println("###################################### body = " + body);
								
				OneM2MApiService onem2mService = OneM2MApiService.getInstance();
				String from = onem2mService.getAppName();
				JSONObject jsonResult = onem2mService.updateResourceNew(uri, from, body);
				
				response.put("result", 0);
				response.put("errorCode", 0);
				response.put("content", jsonResult.toString());
				
			}
		} else {
			response.put("result", 1);
			response.put("errorCode", -1);
			response.put("content", "session is null");
		}
		
		return response;
	}
	
	/*
	 * Fiware 연동 API
	 */
	@RequestMapping(value="/fiware.do")
	public String fiwareDeviceMonitor(HttpServletRequest request, Locale locale, ModelMap model) throws Exception {
		
		HttpSession session = request.getSession(false);
		if(session != null){
			//페이지 권한 확인
			GroupAuthorization requestAuth = (GroupAuthorization) session.getAttribute("requestAuth");
			if(!requestAuth.getAuthorizationDBRead().equals("1")){
				model.addAttribute("authMessage", "fiware 페이지 접근 권한이 없습니다.");
				return "forward:" + HeritProperties.getProperty("Globals.MainPage");
			}
		}
		
		String resourceUri = request.getParameter("uri");
		
		if (resourceUri == null) {
			resourceUri = "/herit-in/herit-cse"; 
		}
		
		String address = this.contextBrokerAddress + "/v2/entities";
		
		FiwareHttpClient hc = new FiwareHttpClient(address);
		hc.openConnection();
		hc.setRequestGetHeader(fiwareService, fiwareServicePath);
		hc.setRequestMethod("GET");
		String entities = hc.getResponseString();
		
		address = this.contextBrokerAddress + "/v2/subscriptions?limit=1000";
		
		hc = new FiwareHttpClient(address);
		hc.openConnection();
		hc.setRequestGetHeader(fiwareService, fiwareServicePath);
		hc.setRequestMethod("GET");
		String subscriptions = hc.getResponseString();
		
		model.addAttribute("fiware_host", HeritProperties.getProperty("Globals.fiwareServerHost"));
		model.addAttribute("fiware_port", HeritProperties.getProperty("Globals.fiwareServerPort"));
		model.addAttribute("fiware_service", fiwareService);
		model.addAttribute("fiware_service_path", fiwareServicePath);
		model.addAttribute("fiware_agent_url", fiwareAgentUrl);
		model.addAttribute("fiware_agent_key", fiwareAgentAccessKey);
		model.addAttribute("entity_list", entities);
		model.addAttribute("sub_list", subscriptions);
		model.addAttribute("main_uri", resourceUri);
		
		return "/v2/dm/fiware";
	}
	
	@ResponseBody
	@RequestMapping(value="/entities.do")
	public Map<String, Object> getEntityDetailInfo(HttpServletRequest request) throws Exception {
		
		Map<String, Object> response = new HashMap<String, Object>();
		HttpSession session = request.getSession(false);
		
		if(session != null){
			//페이지 권한 확인
			GroupAuthorization requestAuth = (GroupAuthorization) session.getAttribute("requestAuth");
			if(!requestAuth.getAuthorizationDBRead().equals("1")){
				response.put("result", 1);
				response.put("errorCode", -1);
				response.put("content", "authMessage: onem2m 페이지 접근 권한이 없습니다.");
			} else {
				String entityId = request.getParameter("id");
				if (entityId == null) {
					response.put("result", 1);
					response.put("errorCode", -1);
					response.put("content", "parameter[entitiy Id] is missing");
				}
				
				String address = this.contextBrokerAddress + "/v2/entities/" + entityId;
				
				FiwareHttpClient hc = new FiwareHttpClient(address);
				hc.openConnection();
				hc.setRequestGetHeader(fiwareService, fiwareServicePath);
				hc.setRequestMethod("GET");
				
				String strResult = hc.getResponseString();
				
				response.put("result", 0);
				response.put("errorCode", 0);
				response.put("content", strResult);
				
			}
		} else {
			response.put("result", 1);
			response.put("errorCode", -1);
			response.put("content", "session is null");
		}
		
		return response;
	}
	
	@ResponseBody
	@RequestMapping(value="/subscribe.do")
	public Map<String, Object> setEntitySubscription(HttpServletRequest request) throws Exception {
		Map<String, Object> response = new HashMap<String, Object>();
		HttpSession session = request.getSession(false);
		
		if(session != null){
			//페이지 권한 확인
			GroupAuthorization requestAuth = (GroupAuthorization) session.getAttribute("requestAuth");
			if(!requestAuth.getAuthorizationDBRead().equals("1")){
				response.put("result", 1);
				response.put("errorCode", -1);
				response.put("content", "authMessage: onem2m 페이지 접근 권한이 없습니다.");
			} else {
				String body = this.getBody(request);
				
				System.out.println("####################### body = " + body);
				
				if (body == null) {
					response.put("result", 1);
					response.put("errorCode", -1);
					response.put("content", "parameters[body] is missing");
				}
				
				String address = this.contextBrokerAddress + "/v2/subscriptions";
				
				
				
				FiwareHttpClient hc = new FiwareHttpClient(address);
				hc.openConnection();
				hc.setRequestPostHeader(fiwareService, fiwareServicePath);
				hc.setRequestMethod("POST");
				hc.sendRequest(body);
				
				String strResult = hc.getResponseString();
				
				response.put("result", 0);
				response.put("errorCode", 0);
				response.put("content", strResult);
				
			}
		} else {
			response.put("result", 1);
			response.put("errorCode", -1);
			response.put("content", "session is null");
		}
		
		return response;
	}
	
	@ResponseBody
	@RequestMapping(value="/actuate.do")
	public Map<String, Object> setEntityActuation(HttpServletRequest request) throws Exception {
		Map<String, Object> response = new HashMap<String, Object>();
		HttpSession session = request.getSession(false);
		
		if(session != null){
			//페이지 권한 확인
			GroupAuthorization requestAuth = (GroupAuthorization) session.getAttribute("requestAuth");
			if(!requestAuth.getAuthorizationDBRead().equals("1")){
				response.put("result", 1);
				response.put("errorCode", -1);
				response.put("content", "authMessage: onem2m 페이지 접근 권한이 없습니다.");
			} else {
				String entityId = request.getParameter("id");
				String attribute = request.getParameter("attr");
				String value = request.getParameter("value");
				
				String body = value;
				
				if (body == null || body.equals("")) {
					response.put("result", 1);
					response.put("errorCode", -1);
					response.put("content", "parameters[body] is missing");
				}
				
				String address = this.contextBrokerAddress + "/v2/entities/" + entityId + "/attrs/" + attribute + "/value";
				
				System.out.println("############################# body = " + body);
				
				FiwareHttpClient hc = new FiwareHttpClient(address);
				hc.openConnection();
				hc.setRequestPutHeader(fiwareService, fiwareServicePath);
				hc.setRequestMethod("PUT");
				hc.sendRequest(body);
				
				String strResult = hc.getResponseString();
				
				response.put("result", 0);
				response.put("errorCode", 0);
				response.put("content", strResult);
				
			}
		} else {
			response.put("result", 1);
			response.put("errorCode", -1);
			response.put("content", "session is null");
		}
		
		return response;
	}
	
	private String getBody(HttpServletRequest request) throws IOException {

        String body = null;
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }

        body = stringBuilder.toString();
        return body;
    }
}

