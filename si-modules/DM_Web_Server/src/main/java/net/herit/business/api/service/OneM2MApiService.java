package net.herit.business.api.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.herit.business.onem2m.OneM2MHttpClient;
import net.herit.common.conf.HeritProperties;
import net.herit.common.exception.UserSysException;
import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.message.onem2m.OneM2mRequest.OPERATION;
import net.herit.iot.message.onem2m.OneM2mRequest.RESOURCE_TYPE;
import net.herit.iot.message.onem2m.OneM2mRequest.RESULT_CONT;
import net.herit.iot.message.onem2m.OneM2mResponse.RESPONSE_STATUS;
import net.herit.iot.message.onem2m.format.Enums.CONTENT_TYPE;
import net.herit.iot.onem2m.ae.hubiss.AEControllerEx;
import net.herit.iot.onem2m.ae.hubiss.HubissEmulatorNotiHandler;
import net.herit.iot.onem2m.bind.http.client.HttpClient;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.resource.AE;
import net.herit.iot.onem2m.resource.AccessControlPolicy;
import net.herit.iot.onem2m.resource.CSEBase;
import net.herit.iot.onem2m.resource.Container;
import net.herit.iot.onem2m.resource.ContentInstance;
import net.herit.iot.onem2m.resource.Delivery;
import net.herit.iot.onem2m.resource.DynamicAuthorizationConsultation;
import net.herit.iot.onem2m.resource.EventConfig;
import net.herit.iot.onem2m.resource.ExecInstance;
import net.herit.iot.onem2m.resource.FilterCriteria;
import net.herit.iot.onem2m.resource.Group;
import net.herit.iot.onem2m.resource.LocationPolicy;
import net.herit.iot.onem2m.resource.M2MServiceSubscriptionProfile;
import net.herit.iot.onem2m.resource.MgmtCmd;
import net.herit.iot.onem2m.resource.Node;
import net.herit.iot.onem2m.resource.NotificationTargetMgmtPolicyRef;
import net.herit.iot.onem2m.resource.NotificationTargetPolicy;
import net.herit.iot.onem2m.resource.PolicyDeletionRules;
import net.herit.iot.onem2m.resource.PollingChannel;
import net.herit.iot.onem2m.resource.RemoteCSE;
import net.herit.iot.onem2m.resource.Request;
import net.herit.iot.onem2m.resource.Resource;
import net.herit.iot.onem2m.resource.Role;
import net.herit.iot.onem2m.resource.Schedule;
import net.herit.iot.onem2m.resource.SemanticDescriptor;
import net.herit.iot.onem2m.resource.ServiceSubscribedAppRule;
import net.herit.iot.onem2m.resource.ServiceSubscribedNode;
import net.herit.iot.onem2m.resource.StatsCollect;
import net.herit.iot.onem2m.resource.StatsConfig;
import net.herit.iot.onem2m.resource.Subscription;
import net.herit.iot.onem2m.resource.TimeSeries;
import net.herit.iot.onem2m.resource.TimeSeriesInstance;
import net.herit.iot.onem2m.resource.Token;
import net.herit.iot.onem2m.resource.TrafficPattern;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@Repository("OneM2MApiService")
public class OneM2MApiService {
	//SingletonPattern
	private static OneM2MApiService INSTANCE;
	
	public static OneM2MApiService getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new OneM2MApiService();
			return INSTANCE;
		}
		else return INSTANCE;
	}
	
	@SuppressWarnings("unused")
	private String oneM2MHost = "";
	@SuppressWarnings("unused")
	private int oneM2MPort = 9901;
	
	//private int aeTimeout = 10000;
	private Logger log = LoggerFactory.getLogger(OneM2MApiService.class);

	private String GLOBALS_AE_ID = "Globals.aeId";
	private String GLOBALS_AE_NAME = "Globals.aeName";
	private String GLOBALS_APP_ID = "Globals.appId";
	private String GLOBALS_APP_NAME = "Globals.appName";
	private String GLOBALS_CSE_ADDR = "Globals.cseAddr";
	private String GLOBALS_CSE_BASE = "Globals.csebase";
	private String GLOBALS_CSE_BASE_NAME = "Globals.csebaseName";
	private String GLOBALS_CSE_ID = "Globals.cseId";
	private String GLOBALS_IP = "Globals.ip";
	private String GLOBALS_PORT = "Globals.port";
	private String GLOBALS_POA = "Globals.poa";
	private String GLOBALS_AE_TIMEOUT = "Globals.aeTimeout";
	
	String aeId = HeritProperties.getProperty(GLOBALS_AE_ID);
	String aeName = HeritProperties.getProperty(GLOBALS_AE_NAME);
	String appId = HeritProperties.getProperty(GLOBALS_APP_ID);
	String appName = HeritProperties.getProperty(GLOBALS_APP_NAME);
	String cseAddr = HeritProperties.getProperty(GLOBALS_CSE_ADDR);
	String csebase = HeritProperties.getProperty(GLOBALS_CSE_BASE);
	String csebaseName = HeritProperties.getProperty(GLOBALS_CSE_BASE_NAME);
	String cseId = HeritProperties.getProperty(GLOBALS_CSE_ID);
	String ip = HeritProperties.getProperty(GLOBALS_IP);
	int port = Integer.valueOf(HeritProperties.getProperty(GLOBALS_PORT));
	String poa = HeritProperties.getProperty(GLOBALS_POA);
	int aeTimeout = Integer.parseInt(HeritProperties.getProperty(GLOBALS_AE_TIMEOUT));
	
	private OneM2MApiService() {
		oneM2MHost = HeritProperties.getProperty("Globals.ip");
		oneM2MPort = Integer.parseInt(HeritProperties.getProperty("Globals.port"));
		
		System.out.println("test: test1234");
		
	}
	
	public String getOneM2MHost() {
		return this.oneM2MHost;
	}
	
	public int getOneM2MPort() {
		return this.oneM2MPort;
	}
	
	public String getCseAddr() {
		return this.cseAddr;
	}
	
	public String getAppName() {
		return this.appName;
	}
	
	AEControllerEx aeController;  
	HubissEmulatorNotiHandler notiHandler;
	//�ʱ�ȭ
	@SuppressWarnings("unused")
	public void init() {
		try {

			// AE ��Ʈ�ѷ� ��
			aeController = new AEControllerEx(cseAddr, cseId, csebaseName, CONTENT_TYPE.RES_XML, null);
			
			// Notification Handler ��
			notiHandler = new HubissEmulatorNotiHandler(aeController);
			aeController.doHttpServerStart(ip, port, notiHandler);
			
			// AE ��
			AE ae = aeController.doCreateAE(csebase, aeId, aeName, appId, appName, poa, true);
			
			/*OneM2MInitData.getInstance().data.put("notiHandler", notiHandler);
			OneM2MInitData.getInstance().data.put("aeController", aeController);
			OneM2MInitData.getInstance().data.put("ae", ae);*/

		} catch (OneM2MException oe) {

			RESPONSE_STATUS status = oe.getResponseStatusCode();
			System.out.println("Status:" + status.toString());

			System.out.println("OneM2MException occurred!!!");
			System.out.println(oe.toString());
			oe.printStackTrace();
			return;

		} catch (Exception e) {

			System.out.println("Exception occurred!!!");
			System.out.println(e.toString());
			e.printStackTrace();
			return;

		}
	}
	
	/** oneM2M read, execute (������ȸ, ����)*/
	public HashMap<String, Object> execute(String operation, ArrayList<String> resUris, String deviceUri, String exContent) 
		throws JsonGenerationException, JsonMappingException, IOException, UserSysException
	{
		System.out.println("here here here here here here here here here here here here here");
		System.out.println("operation : " + operation);
		System.out.println("deviceUri : " + deviceUri);
		
		HashMap<String, Object> res = new HashMap<String, Object>();
		
		try {
			ContentInstance ci;
			
			if (operation.equals("read")) {
				res.put("o", "r");//XXX
				for (int i=0; i < resUris.size(); i++) {
					String resourceUri = resUris.get(i);
					String creationTime = resUris.get(i)+"_"+i;
					System.out.println("resourceUri:" + resourceUri);
					System.out.println("creationTime : " + creationTime);
					
					ci = aeController.doGetLatestContent(resourceUri+"/Data", aeId);
					System.out.println(creationTime + ":" + ci.getCreationTime());
					System.out.println(resourceUri + ":" + ci.getContent());
					res.put(resourceUri, ci.getContent());
					res.put(creationTime, ci.getCreationTime());
					
				}
				//System.out.println("content:" + res.get("/herit-in/herit-cse/ae-gaslock1004/VALVE"));
				
				
			} else if (operation.equals("execute") || operation.equals("write")) {
				res.put("o", "e");
				ContentInstance ciCommand = new ContentInstance();
				ciCommand.setContentInfo("text/plain:0");
				// ������ �Ѿ�� ���� ������
				ciCommand.setContent(exContent);
				//TODO:�̰͵� ������ ó�� �ؾ��ϳ�...?
				System.out.println("resUris:" + resUris.get(0));
				System.out.println("exContent:" + exContent);
				
				ci = aeController.doControlCommand(resUris.get(0), aeId, ciCommand, aeTimeout);
				if(ci != null) {
					res.put(resUris.get(0), ci.getContent());
				}
				System.out.println("Control result:" + (ci != null ? ci.getContent() : "null"));
				 
				
			}
			
		} catch (OneM2MException oe) {
			System.out.println("OneM2MException occurred!!!");
			System.out.println(oe.toString());
			oe.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	/** oneM2M ����̽� �˻� (discovery) */
	public HashMap<String, Object> discovery(String operation, String sn) throws JsonGenerationException, JsonMappingException, IOException, UserSysException {
		System.out.println("discovery discovery discovery discovery discovery discovery discovery");
		System.out.println("operation :" + operation);
		System.out.println("sn :" + sn);
		
		
		HashMap<String, Object> res = new HashMap<String, Object>();
		//List<String> devIds = new ArrayList<String>();
		
		try {
			
			if (operation.equals("discovery")) {
				FilterCriteria fc = new FilterCriteria();
				fc.setResourceType(RESOURCE_TYPE.AE.Value());
				fc.addLabels(sn);
				
				//res.put("o", "dis");//XXX 
				List<String> devIds = aeController.doDiscovery(csebase, aeId, fc);
				System.out.println("devIds : " + devIds.toString());
				
				if (devIds == null || devIds.isEmpty()) {
					System.out.println("No device discovered.");
				} else {
					res.put(sn, devIds);
				}
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
		return res;
	}
	
	public String discoverResource(String resuri, String from, RESOURCE_TYPE type) throws Exception {
		CONTENT_TYPE contentType = CONTENT_TYPE.RES_JSON;
		OPERATION operationType = OPERATION.DISCOVERY;
		String requestId = "rqi";
		
		String host = this.GLOBALS_IP;
		String xM2MRi = "pm_12233254546";
		String xM2MOrigin = requestId; 
		
		String targetAddr  = this.cseAddr + resuri + "?fu=1&lbl=onem2m&ty=" + type.Value();
		
		OneM2MHttpClient hc = new OneM2MHttpClient(targetAddr);
		
		hc.openConnection();
		hc.setRequestHeaderBase(host, xM2MRi, "", xM2MOrigin, 0);
		hc.setRequestMethod("GET");
		
		String response = hc.getResponseString();
		
		return response;
	}
	
	public OneM2mResponse retrieve(String resuri, String from) throws OneM2MException {
		
		CONTENT_TYPE contentType = CONTENT_TYPE.RES_JSON;
		OPERATION operationType = OPERATION.RETRIEVE;
		String requestId = "rqi";
		String path = this.cseAddr + resuri;
		
		OneM2mRequest req = new OneM2mRequest();
		req.setResultContent(RESULT_CONT.ATTR_N_CHILD_RES_REF);
		req.setFrom(from);
		req.setTo(resuri);
		req.setContentType(contentType);
		req.setOperation(operationType);
		req.setRequestIdentifier(requestId);
		
		OneM2mResponse res = HttpClient.getInstance().sendRequest(path, req);

		if (res == null) {
			System.out.println("retrieve failed.");
			throw new OneM2MException(RESPONSE_STATUS.NETWORK_FAILURE, "network failure");
		}
		
		if (res.getResponseStatusCodeEnum() == RESPONSE_STATUS.OK) {
			
			try {
				return res;
			} catch (Exception e) {
				throw new OneM2MException(RESPONSE_STATUS.BAD_RESPONSE, "Fail to parse RES:"+e.getMessage());
			}
			
			
		} else {
			log.error("Discovery failed. {}", res.toString());
			throw new OneM2MException(res.getResponseStatusCodeEnum(), "Faile to retrieve RES:"+res.toString());
		}
		
	}
	
	public RESPONSE_STATUS delete(String resuri, String from) throws OneM2MException {
		
		OPERATION operationType = OPERATION.DELETE;
		String requestId = "rqi";
		String path = this.cseAddr + resuri;
		CONTENT_TYPE contentType = CONTENT_TYPE.RES_JSON;
		
		OneM2mRequest req = new OneM2mRequest();
		req.setResultContent(RESULT_CONT.ATTR_N_CHILD_RES_REF);
		req.setFrom(from);
		req.setTo(resuri);
		req.setContentType(contentType);
		req.setOperation(operationType);
		req.setRequestIdentifier(requestId);
		
		OneM2mResponse res = HttpClient.getInstance().sendRequest(path, req);

		if (res == null) {
			System.out.println("delete failed.");
			throw new OneM2MException(RESPONSE_STATUS.NETWORK_FAILURE, "network failure");
		}
		
		return res.getResponseStatusCodeEnum();
		
	}
	
	//public HashMap<String, Object> getResourceMap(String resuri, String from) throws Exception {
	public String getResourceString(String resuri, String from) throws Exception {
		
		HashMap<String, Object> res = new HashMap<String, Object>();
		
		OneM2mResponse resMessage = this.retrieve(resuri, from);
		
		CONTENT_TYPE contentType =resMessage.getContentType();
		
		//CSEBase resource = (CSEBase)OneM2MConversion.getInstance().convertToResource(RESOURCE_TYPE.CSE_BASE, resMessage, contentType);
		
		return new String(resMessage.getContent(), "UTF-8");
		
		
	}
	
	public OneM2mResponse createResource(String resuri, Resource resource, String from) throws OneM2MException {
		
		
		
		CONTENT_TYPE contentType = CONTENT_TYPE.RES_JSON;
		OPERATION operationType = OPERATION.CREATE;
		String requestId = "rqi";
		String path = this.cseAddr + resuri;
		
		OneM2mRequest req = new OneM2mRequest();
		req.setResultContent(RESULT_CONT.ATTRIBUTE);
		req.setFrom(from);
		req.setTo(resuri);
		req.setContentType(contentType);
		req.setOperation(operationType);
		req.setRequestIdentifier(requestId);
		
		switch(resource.getResourceType()) {
		case 1 :
			req.setContentObject((AccessControlPolicy)resource);
			break;
		case 2 :
			req.setContentObject((AE)resource);
			break;
		case 3 :
			req.setContentObject((Container)resource);
			break;
		case 4:
			req.setContentObject((ContentInstance)resource);
			break;
		case 5:
			req.setContentObject((CSEBase)resource);
			break;
		case 6: 
			req.setContentObject((Delivery)resource);
			break;
		case 7: 
			req.setContentObject((EventConfig)resource);
			break;
		case 8: 
			req.setContentObject((ExecInstance)resource);
			break;
		case 9: 
			req.setContentObject((Group)resource);
			break;
		case 10: 
			req.setContentObject((LocationPolicy)resource);
			break;
		case 11: 
			req.setContentObject((M2MServiceSubscriptionProfile)resource);
			break;
		case 12: 
			req.setContentObject((MgmtCmd)resource);
			break;
		case 14: 
			req.setContentObject((Node)resource);
			break;
		case 15: 
			req.setContentObject((PollingChannel)resource);
			break;
		case 16: 
			req.setContentObject((RemoteCSE)resource);
			break;
		case 17: 
			req.setContentObject((Request)resource);
			break;
		case 18: 
			req.setContentObject((Schedule)resource);
			break;
		case 19: 
			req.setContentObject((ServiceSubscribedAppRule)resource);
			break;
		case 20: 
			req.setContentObject((ServiceSubscribedNode)resource);
			break;
		case 21: 
			req.setContentObject((StatsCollect)resource);
			break;
		case 22: 
			req.setContentObject((StatsConfig)resource);
			break;
		case 23: 
			req.setContentObject((Subscription)resource);
			break;
		case 24: 
			req.setContentObject((SemanticDescriptor)resource);
			break;
		case 25: 
			req.setContentObject((NotificationTargetMgmtPolicyRef)resource);
			break;
		case 26: 
			req.setContentObject((NotificationTargetPolicy)resource);
			break;
		case 27: 
			req.setContentObject((PolicyDeletionRules)resource);
			break;
		case 29: 
			req.setContentObject((TimeSeries)resource);
			break;
		case 30: 
			req.setContentObject((TimeSeriesInstance)resource);
			break;
		case 31: 
			req.setContentObject((Role)resource);
			break;
		case 32: 
			req.setContentObject((Token)resource);
			break;
		case 34: 
			req.setContentObject((TrafficPattern)resource);
			break;
		case 35:
			req.setContentObject((DynamicAuthorizationConsultation)resource);
			break;
		default:
			req.setContentObject(resource);
		}
		
		OneM2mResponse res = HttpClient.getInstance().sendRequest(path, req);
		
		if (res == null) {
			log.error("createAE failed.");
			throw new OneM2MException(RESPONSE_STATUS.NETWORK_FAILURE, "network failure");
		}		
		
		if (res.getResponseStatusCodeEnum() == RESPONSE_STATUS.CREATED) {			
			return res;
		} else {
			log.debug("createAE failed. {}", res.toString());
			throw new OneM2MException(res.getResponseStatusCodeEnum(), "Fail to createAE RES:"+res.toString());
		}
		
	}
	
	public int createResourceAE(String resuri, String origin, String body, String rn, int rt) throws Exception {
		
		String host = this.GLOBALS_IP;
		String xM2MRi = "pm_12233254546";
		String xM2MOrigin = origin; 
		String resourceName = rn;
		
		String targetAddr  = this.cseAddr + resuri;
		
		OneM2MHttpClient hc = new OneM2MHttpClient(targetAddr);
		
		hc.openConnection();
		hc.setRequestHeaderBase(host, xM2MRi, resourceName, xM2MOrigin, rt);
		hc.setRequestMethod("POST");
		hc.sendRequest(body);
		String response = hc.getResponseString();
		
		return hc.getResponseCode();
		
	}
	
	public int createResourceOther(String resuri, String origin, String body, int rt) throws Exception {
		
		String host = this.GLOBALS_IP;
		String xM2MRi = "pm_12233254546";
		String xM2MOrigin = origin; 
		
		String targetAddr  = this.cseAddr + resuri;
		
		OneM2MHttpClient hc = new OneM2MHttpClient(targetAddr);
		
		hc.openConnection();
		hc.setRequestHeaderBase(host, xM2MRi, "OTHER", xM2MOrigin, rt);
		hc.setRequestMethod("POST");
		hc.sendRequest(body);
		String response = hc.getResponseString();
		
		return hc.getResponseCode();
		
	}

	public int updateResource(String resuri, String origin, String body) throws Exception {
		String host = this.GLOBALS_IP;
		String xM2MRi = "pm_12233254546";
		String xM2MOrigin = origin; 
		
		String targetAddr  = this.cseAddr + resuri;
		
		OneM2MHttpClient hc = new OneM2MHttpClient(targetAddr);
		
		hc.openConnection();
		hc.setRequestHeaderBase(host, xM2MRi, "", xM2MOrigin, 0);
		hc.setRequestMethod("PUT");
		hc.sendRequest(body);
		String response = hc.getResponseString();
		
		return hc.getResponseCode();
	}
	
	public JSONObject updateResourceNew(String resuri, String origin, String body) throws Exception {
		String host = this.GLOBALS_IP;
		String xM2MRi = "pm_12233254546";
		String xM2MOrigin = origin; 
		
		JSONObject jsonResult = new JSONObject();
		
		String targetAddr  = this.cseAddr + resuri;
		
		OneM2MHttpClient hc = new OneM2MHttpClient(targetAddr);
		
		hc.openConnection();
		hc.setRequestHeaderBase(host, xM2MRi, "", xM2MOrigin, 0);
		hc.setRequestMethod("PUT");
		hc.sendRequest(body);
		String response = hc.getResponseString();
		
		jsonResult.append("responseCode", hc.getResponseCode());
		jsonResult.append("content", response);
		
		return jsonResult;
	}
}