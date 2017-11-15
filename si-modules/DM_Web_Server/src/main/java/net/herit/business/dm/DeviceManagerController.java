package net.herit.business.dm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import net.herit.business.api.service.OneM2MApiService;
import net.herit.business.onem2m.service.ConfigurationService;
import net.herit.business.onem2m.service.PlatformService;
import net.herit.common.conf.HeritProperties;
import net.herit.iot.message.onem2m.OneM2mRequest.RESOURCE_TYPE;
import net.herit.security.dto.GroupAuthorization;

@Controller
@RequestMapping(value="/dm")
public class DeviceManagerController {

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
		
		// added in 2017-09-18, blocked in 2017-11-15
		//mongoClient = new MongoClient(HeritProperties.getProperty("Globals.MongoDB.Host"), Integer.parseInt( HeritProperties.getProperty("Globals.MongoDB.Port") ) );
		//db = mongoClient.getDB(HeritProperties.getProperty("Globals.MongoDB.DBName"));
		
		// added in 2017-11-15 to support authentication
		String mongoDbName = HeritProperties.getProperty("Globals.MongoDB.DBName");
		String mongoHost = HeritProperties.getProperty("Globals.MongoDB.Host");
		String mongoPort = HeritProperties.getProperty("Globals.MongoDB.Port");
		
		if(HeritProperties.getProperty("Globals.MongoDB.User") != null && !HeritProperties.getProperty("Globals.MongoDB.User").equals("")
				&& HeritProperties.getProperty("Globals.MongoDB.Password") != null && !HeritProperties.getProperty("Globals.MongoDB.Password").equals("")) {
			
			String user = HeritProperties.getProperty("Globals.MongoDB.User");
			String pwd = HeritProperties.getProperty("Globals.MongoDB.Password");
			
			MongoCredential credential = MongoCredential.createScramSha1Credential(user, mongoDbName, pwd.toCharArray());
			mongoClient = new MongoClient(new ServerAddress(mongoHost, Integer.parseInt(mongoPort)), Arrays.asList(credential));
		} else {
			mongoClient = new MongoClient(mongoHost, Integer.parseInt( mongoPort ) );
		}
		db = mongoClient.getDB(mongoDbName);
		
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
		
		if (resourceUri == null) {
			resourceUri = "/herit-in/herit-cse"; 
		}
		
		OneM2MApiService onem2mService = OneM2MApiService.getInstance();
		String from = onem2mService.getAppName();
		
		String response = onem2mService.discoverResource(resourceUri, from, RESOURCE_TYPE.AE);
		
		JSONObject json = new JSONObject(response);
		
		JSONArray jsonUriList = json.getJSONObject("uril").getJSONArray("URIList");
		
		List<Object> uriList = jsonUriList.toList();
		
		String aeResoureName, resourceNodeId = "", unstructResourceUri = "";
		JSONArray retDeviceList = new JSONArray();
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
		
		model.addAttribute("onem2m_host", HeritProperties.getProperty("Globals.cseAddr"));
		model.addAttribute("cse_base", HeritProperties.getProperty("Globals.csebaseName"));
		model.addAttribute("cse_id", HeritProperties.getProperty("Globals.cseId"));
		model.addAttribute("main_uri", resourceUri);
		model.addAttribute("device_list", retDeviceList);
		model.addAttribute("mgmt_cmd_list", retMgmtCmdList);
		
		//return "/v2/dm/onem2m";
		return "/v2/dm/" + pageName;
		
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

