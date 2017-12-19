package net.herit.iot.onem2m.incse.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.bson.Document;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;

import net.herit.iot.message.onem2m.OneM2mResponse.RESPONSE_STATUS;
import net.herit.iot.message.onem2m.format.Enums.CONTENT_TYPE;
import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.message.onem2m.OneM2mRequest.OPERATION;
import net.herit.iot.message.onem2m.OneM2mRequest.RESOURCE_TYPE;
import net.herit.iot.message.onem2m.OneM2mRequest.RESPONSE_TYPE;
import net.herit.iot.message.onem2m.OneM2mRequest.RESULT_CONT;
import net.herit.iot.onem2m.ae.emul.Constants;
import net.herit.iot.onem2m.bind.codec.AbsSerializer;
import net.herit.iot.onem2m.bind.http.client.HttpClient;
import net.herit.iot.onem2m.core.convertor.ConvertorFactory;
import net.herit.iot.onem2m.core.convertor.JSONConvertor;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.core.util.Utils;
import net.herit.iot.onem2m.incse.context.OneM2mContext;
import net.herit.iot.onem2m.incse.controller.dm.Tr069DMAdapter;
import net.herit.iot.onem2m.incse.controller.dm.Tr069DMAdapter.MOUri;
import net.herit.iot.onem2m.incse.controller.dm.Tr069DMController;
import net.herit.iot.onem2m.incse.facility.CfgManager;
import net.herit.iot.onem2m.incse.facility.OneM2mUtil;
import net.herit.iot.onem2m.incse.manager.ResourceManager;
import net.herit.iot.onem2m.resource.AE;
import net.herit.iot.onem2m.resource.AreaNwkDeviceInfo;
import net.herit.iot.onem2m.resource.AreaNwkInfo;
import net.herit.iot.onem2m.resource.Battery;
import net.herit.iot.onem2m.resource.Container;
import net.herit.iot.onem2m.resource.ContentInstance;
import net.herit.iot.onem2m.resource.DeviceCapability;
import net.herit.iot.onem2m.resource.DeviceInfo;
import net.herit.iot.onem2m.resource.EventLog;
import net.herit.iot.onem2m.resource.Firmware;
import net.herit.iot.onem2m.resource.Memory;
import net.herit.iot.onem2m.resource.MgmtResource;
import net.herit.iot.onem2m.resource.Naming;
import net.herit.iot.onem2m.resource.Node;
import net.herit.iot.onem2m.resource.Reboot;
import net.herit.iot.onem2m.resource.Resource;
import net.herit.iot.onem2m.resource.RestCommand;
import net.herit.iot.onem2m.resource.RestCommandCI;
import net.herit.iot.onem2m.resource.RestCommandResult;
import net.herit.iot.onem2m.resource.RestCommandResult2;
import net.herit.iot.onem2m.resource.RestSubscription;
import net.herit.iot.onem2m.resource.Software;
import net.herit.iot.onem2m.resource.Subscription;


public class RestCommandController {
	
	private final static RestCommandController INSTANCE = new RestCommandController();

	private Logger log = LoggerFactory.getLogger(RestCommandController.class);
	
	private OneM2mContext context;
	private HashMap<String, RestCommand> requestMap;
	private Timer expireTimer;
	
	public static RestCommandController getInstance() {
		return INSTANCE;
	}
	
	public void initialize(OneM2mContext context) {
		
		this.context = context;
		this.requestMap = new HashMap<String, RestCommand>();

		expireTimer = new Timer();
		expireTimer.schedule(new ExpiredCmdTimer(), CfgManager.getInstance().getCommandExpireTimerInterval()*1000);
		
	}
	
	public OneM2mResponse processControl(RestCommand command) {	
		
		try {

			OneM2mRequest reqMessage = new OneM2mRequest();
			
			String commandId = command.getCommandId();
			if (commandId == null || commandId.length() == 0) {
				commandId = OneM2mUtil.createRequestId();
				command.setCommandId(commandId);
			}
			
			long hourLater = System.currentTimeMillis() + (1 * 60 * 60 * 1000);		// 1 hour settting
			String expirationTime = new SimpleDateFormat(Naming.DATE_FORMAT).format(new java.util.Date(hourLater));
			
			ContentInstance ci = new ContentInstance();
			 	
			ci.setContentInfo("application/json:1");		// updated at 2017-05-12
			//ci.setContentInfo("application/json:0");		// updated at 2016-09-30
			//ci.setContentInfo("text/plain:0");				
			//ci.setExpirationTime(CfgManager.getInstance().getDefaultExpirationTime());  // added at 2016-12-23
			ci.setExpirationTime(expirationTime);			// added at 2016-12-28, set by 1-hour-later
			Document doc = new Document();
			doc.append("exec_id", commandId);
			doc.append("data", command.getContent());
			String json = doc.toJson();
			//String json = "{ \"exec_id\": \""+commandId+"\", \"data\": \""+command.getContent()+"\"}";

			ci.setContent(Base64.encode(json.getBytes()));	// updated at 2017-05-12
			//ci.setContent(command.getContent());			// updated at 2016-09-30
			
			AbsSerializer serializer = AbsSerializer.getSerializer(CONTENT_TYPE.JSON);			
			byte[] content = serializer.serialize(ci).getBytes();
			
			reqMessage.setTo(command.getUri()+"/"+command.getCommand()+"/execute");
			reqMessage.setFrom("C-AE-Internal");
			reqMessage.setContent(content);
			reqMessage.setOperation(OPERATION.CREATE);		
			reqMessage.setRequestIdentifier(commandId);
			reqMessage.setResourceType(RESOURCE_TYPE.CONTENT_INST);
			reqMessage.setContentType(CONTENT_TYPE.RES_JSON);
			reqMessage.setResultContent(RESULT_CONT.ATTRIBUTE);
			reqMessage.setResponseType(RESPONSE_TYPE.BLOCK_REQ);
					
			try {
				putCommandToMap(commandId, command);
				
				OneM2mResponse response = new ResourceManager(this.context).processEx(reqMessage, false);
				log.debug("RestCommand Control processsed:"+response.toString());
				
				return response;
			} catch (OneM2MException ex) {
				log.debug("Exception during process internal request triggered by RestCommand Control :"+reqMessage.toString());
				log.debug("Exception:"+ex.toString());
				OneM2mResponse response = new OneM2mResponse();
				response.setResponseStatusCodeEnum(ex.getResponseStatusCode());
				response.setRequest(reqMessage);
				log.debug("Handled exception", ex);
				return response;
			}
			
		} catch (Exception e) {
			log.debug("Handled exception", e);
			return null;
			
		}
	}
	
	public OneM2mResponse processIpeLwm2m(RestCommand command) {	
		
		try {

			OneM2mRequest reqMessage = new OneM2mRequest();
			
			String commandId = command.getCommandId();
			if (commandId == null || commandId.length() == 0) {
				commandId = OneM2mUtil.createRequestId();
				command.setCommandId(commandId);
			}
			
			long hourLater = System.currentTimeMillis() + (1 * 60 * 60 * 1000);		// 1 hour settting
			String expirationTime = new SimpleDateFormat(Naming.DATE_FORMAT).format(new java.util.Date(hourLater));
			
			ContentInstance ci = new ContentInstance();
			ci.setContentInfo(command.getContentInfo());		// updated at 2017-02-08
			//ci.setExpirationTime(CfgManager.getInstance().getDefaultExpirationTime());  // added at 2016-12-23
			ci.setExpirationTime(expirationTime);			// added at 2016-12-28, set by 1-hour-later
			ci.setContent(command.getContent());			// updated at 2017-02-08
			
			AbsSerializer serializer = AbsSerializer.getSerializer(CONTENT_TYPE.JSON);			
			byte[] content = serializer.serialize(ci).getBytes();
			
			reqMessage.setTo(command.getUri()+"/"+command.getCommand()+"/write");
			reqMessage.setFrom("C-AE-Internal");
			reqMessage.setContent(content);
			reqMessage.setOperation(OPERATION.CREATE);		
			reqMessage.setRequestIdentifier(commandId);
			reqMessage.setResourceType(RESOURCE_TYPE.CONTENT_INST);
			reqMessage.setContentType(CONTENT_TYPE.RES_JSON);
			reqMessage.setResultContent(RESULT_CONT.ATTRIBUTE);
			reqMessage.setResponseType(RESPONSE_TYPE.BLOCK_REQ);
					
			try {
				putCommandToMap(commandId, command);
				
				OneM2mResponse response = new ResourceManager(this.context).processEx(reqMessage, false);
				log.debug("RestCommand Control processsed:"+response.toString());
				
				return response;
			} catch (OneM2MException ex) {
				log.debug("Exception during process internal request triggered by RestCommand Control :"+reqMessage.toString());
				log.debug("Exception:"+ex.toString());
				OneM2mResponse response = new OneM2mResponse();
				response.setResponseStatusCodeEnum(ex.getResponseStatusCode());
				response.setRequest(reqMessage);
				log.debug("Handled exception", ex);
				return response;
			}
			
		} catch (Exception e) {
			log.debug("Handled exception", e);
			return null;
			
		}
	}
	
	public OneM2mResponse processCommand(RestCommand command) {	
				
		try {

			OneM2mRequest reqMessage = new OneM2mRequest();
			
			String commandId = command.getCommandId();
			if (commandId == null || commandId.length() == 0) {
				commandId = OneM2mUtil.createRequestId();
				command.setCommandId(commandId);
			}
			
			ContentInstance ci = new ContentInstance();
			ci.setContentInfo("application/json:1");
			
			Document doc = new Document();
			doc.append("exec_id", commandId);
			doc.append("data", command.getContent());
			String json = doc.toJson();
			//String json = "{ \"exec_id\": \""+commandId+"\", \"data\": \""+command.getContent()+"\"}";

			ci.setContent(Base64.encode(json.getBytes()));
			
			AbsSerializer serializer = AbsSerializer.getSerializer(CONTENT_TYPE.JSON);			
			byte[] content = serializer.serialize(ci).getBytes();
			
			reqMessage.setTo(command.getUri()+"/"+command.getCommand()+"/Execute");
			reqMessage.setFrom("C-AE-Internal");
			reqMessage.setContent(content);
			reqMessage.setOperation(OPERATION.CREATE);		
			reqMessage.setRequestIdentifier(commandId);
			reqMessage.setResourceType(RESOURCE_TYPE.CONTENT_INST);
			reqMessage.setContentType(CONTENT_TYPE.RES_JSON);
			reqMessage.setResultContent(RESULT_CONT.ATTRIBUTE);
			reqMessage.setResponseType(RESPONSE_TYPE.BLOCK_REQ);
					
			try {
				putCommandToMap(commandId, command);
				
				OneM2mResponse response = new ResourceManager(this.context).processEx(reqMessage, false);
				log.debug("RestCommand processsed:"+response.toString());
				
				return response;
			} catch (OneM2MException ex) {
				log.debug("Exception during process internal request triggered by RestCommand:"+reqMessage.toString());
				log.debug("Exception:"+ex.toString());
				OneM2mResponse response = new OneM2mResponse();
				response.setResponseStatusCodeEnum(ex.getResponseStatusCode());
				response.setRequest(reqMessage);
				log.debug("Handled exception", ex);
				return response;
			}
			
		} catch (Exception e) {
			log.debug("Handled exception", e);
			return null;
			
		}
		
	}
		
	private OneM2mContext getContext() {
		
		return this.context;
		
	}

	public void processResult(ContentInstance ci) {
				
		try {
			
			String contentInfo = ci.getContentInfo();
			if (contentInfo != null && contentInfo.equals("application/json:1")) {
				String content = ci.getContent();
				String json = new String(Base64.decode(content), StandardCharsets.UTF_8);
				
				JSONConvertor<RestCommandCI> cvtr = (JSONConvertor<RestCommandCI>) ConvertorFactory.getJSONConvertor(RestCommandCI.class, null);
				RestCommandCI resultCi = cvtr.unmarshal(json);
				
				RestCommand cmd = getCommandFromMap(resultCi.getExecId(), true);
				if (cmd != null && !ci.getUri().startsWith(cmd.getUri()+"/action/Result")) {
					putCommandToMap(cmd.getCommandId(), cmd);
					cmd = null;
				}
				if (cmd != null) {		
										
					String notiUri = cmd.getNotificationUri();
					String code = resultCi.getExecResult();
					String commandId = resultCi.getExecId();
					String data = resultCi.getData();
					
					RestCommandResult result = new RestCommandResult();
					result.setResultCode(code);
					result.setCommandId(commandId);
					result.setResult(data);
					
					JSONConvertor<RestCommandResult> cvtRcr = (JSONConvertor<RestCommandResult>) ConvertorFactory.getJSONConvertor(RestCommandResult.class, null);
					String jsonReult = cvtRcr.marshal(result);
					OneM2mRequest reqMessage = new OneM2mRequest();
					
					reqMessage.setContent(jsonReult.getBytes());
					
					reqMessage.setTo(extractToFromFullUri(notiUri));
					reqMessage.setOperation(OPERATION.CREATE);
					reqMessage.setContentType(CONTENT_TYPE.JSON);
					//reqMessage.setFrom("SI");
					
//					new HttpClient().process(notiUri, reqMessage);
					HttpClient.getInstance().sendRequest(notiUri, reqMessage);
				}
			}
			
		} catch (Base64DecodingException e) {
			log.debug("Handled exception", e);
			return;
		} catch (Exception e) {
			log.debug("Handled exception", e);
			return;
		}
		
	}
	
    public void processResult2(ContentInstance ci) {		// added by brianmoon at 2016-09-26
		
		try {
			
			String contentInfo = ci.getContentInfo();
			if (contentInfo != null && contentInfo.equals("application/json:1")) {
				String content = ci.getContent();
				String json = new String(Base64.decode(content), StandardCharsets.UTF_8);
				
				JSONConvertor<RestCommandCI> cvtr = (JSONConvertor<RestCommandCI>) ConvertorFactory.getJSONConvertor(RestCommandCI.class, null);
				RestCommandCI resultCi = cvtr.unmarshal(json);
				
				RestCommand cmd = getCommandFromMap(resultCi.getExecId(), true);
			//	if (cmd != null && !ci.getUri().startsWith(cmd.getUri()+"/Power/Status")) {     // blocked in 2017-05-12
				if (cmd != null && !ci.getUri().startsWith(cmd.getUri() + "/" + cmd.getCommand() + "/result")) {		// added in 2017-06-08
					putCommandToMap(cmd.getCommandId(), cmd);
					cmd = null;
				}
				if (cmd != null) {		
										
					String notiUri = cmd.getNotificationUri();
					String code = resultCi.getExecResult();
					String commandId = resultCi.getExecId();
					String data = resultCi.getData();
					
					RestCommandResult2 result = new RestCommandResult2();
					 
					result.setCommandId(commandId);
					result.setUri(cmd.getUri());
					result.setResultCode(code);  // added in 2017-05-12
					//result.setDeviceStatus(ci.getContent());		// blocked in 2017-05-12
					result.setDeviceStatus(data);		// added in 2017-05-12
					result.setTimestamp(ci.getCreationTime());
					
					JSONConvertor<RestCommandResult2> cvtRcr = (JSONConvertor<RestCommandResult2>) ConvertorFactory.getJSONConvertor(RestCommandResult2.class, null);
					String jsonReult = cvtRcr.marshal(result);
					OneM2mRequest reqMessage = new OneM2mRequest();
					
					reqMessage.setContent(jsonReult.getBytes());
					
					reqMessage.setTo(extractToFromFullUri(notiUri));
					reqMessage.setOperation(OPERATION.CREATE);
					reqMessage.setContentType(CONTENT_TYPE.JSON);
					//reqMessage.setFrom("SI");
					
//					new HttpClient().process(notiUri, reqMessage);
					HttpClient.getInstance().sendRequest(notiUri, reqMessage);
				}
			}
			
		} catch (Base64DecodingException e) {
			log.debug("Handled exception", e);
			return;
		} catch (Exception e) {
			log.debug("Handled exception", e);
			return;
		}
		
	}
	
	private void putCommandToMap(String reqId, RestCommand cmd) {
		
		if (cmd.getCreateTime() == null) {
			cmd.setCreateTime(new Timestamp(new Date().getTime()));
		}
		synchronized(this) {
			this.requestMap.put(reqId, cmd);
		}
				
	}
	
	private RestCommand getCommandFromMap(String reqId, boolean delete) {
		
		synchronized(this) {
			RestCommand cmd = this.requestMap.get(reqId);
			if (cmd != null && delete) {
				requestMap.remove(reqId);
			}
			return cmd;
		}
				
	}
	
	private String extractToFromFullUri(String fullUri) {
		
		String to = "/";
		URI url;
		try {
			url = new URI(fullUri);

			String path = url.getPath();
			if (path.length() > 1) {
				int i = fullUri.indexOf(path);
				to = fullUri.substring(i);
			}
			
		} catch (URISyntaxException e) {
			
			log.debug("Handled exception", e);
		}
		return to;
		
	}
	
	private HashMap<String, RestCommand> getRequestMap() {
		
		return this.requestMap;
		
	}
	
	class ExpiredCmdTimer extends TimerTask {
		
		public void run() {
			
			//log.debug("ExpiredCmdTimer run!!!!");
			int expireSecond = CfgManager.getInstance().getCommandTimeout();
			Timestamp currentTime = new Timestamp(new Date().getTime());
			ArrayList<RestCommand> expiredCmd = new ArrayList<RestCommand>();
			
			synchronized(RestCommandController.getInstance()) {
				
				Set<String> keySet = requestMap.keySet();
				Iterator<String> it = keySet.iterator();
				
				List<String> expiredReqIds = new ArrayList<String>();
				
				while (it.hasNext()) {
					String reqId = it.next();
					RestCommand cmd = requestMap.get(reqId);
					Timestamp expireTime = new Timestamp(cmd.getCreateTime().getTime() + expireSecond*1000L);
					
					if (currentTime.after(expireTime)) {
						expiredCmd.add(cmd);
					}
				}			
				
				Iterator<RestCommand> itCmd = expiredCmd.iterator();
				while (itCmd.hasNext()) {
					requestMap.remove(itCmd.next().getCommandId());					
				}

			}
			
			Iterator<RestCommand> it = expiredCmd.iterator();
			while (it.hasNext()) {
				RestCommand cmd = it.next();
				
				try {

					log.debug("##########################################");
					log.debug("Processing expired command: {}", cmd.toString());
					
					Document doc = new Document();
					doc.put("_commandId", cmd.getCommand());
					doc.put("_resultCode", RESPONSE_STATUS.COMMAND_TIMEOUT.Value());
					doc.put("_result", RESPONSE_STATUS.COMMAND_TIMEOUT.toString());
					
					String notiUri = cmd.getNotificationUri();
					if(notiUri != null && !notiUri.equals("")) {		// updated by brianmoon at 2016-09-26
						OneM2mRequest reqMessage = new OneM2mRequest();
						
						reqMessage.setTo(extractToFromFullUri(notiUri));
						reqMessage.setOperation(OPERATION.CREATE);
						reqMessage.setContentType(CONTENT_TYPE.JSON);
						reqMessage.setContent(doc.toJson().getBytes());
						
	//					new HttpClient().process(notiUri, reqMessage);
						HttpClient.getInstance().sendRequest(notiUri, reqMessage);
					}
					
				} catch (Exception e) {
					
					log.debug("Handled exception", e);
					
				}
			}

			expireTimer.schedule(new ExpiredCmdTimer(), CfgManager.getInstance().getCommandExpireTimerInterval()*1000);
		}	
	}
	
	// added in 2017-08-03 to create onem2m device management resource related with TR-069
	public void setParameterValues(String deviceId, HashMap<String, Object> paramMap) {
		Tr069DMController controller = new Tr069DMController();
		
		controller.setStatus(deviceId, paramMap);

	}
	
	// added in 2017-08-03 to create onem2m device management resource related with TR-069
	public OneM2mResponse createContentInstance(HashMap<String, Object> paramMap) {
			try {
				String deviceId = paramMap.get(MOUri.DVC_OUI) + "-" + paramMap.get(MOUri.DVC_PRODUCTCLASS) + "-" + paramMap.get(MOUri.DVC_SERIAL);
				OneM2mResponse response = null;
				String parentUri = CfgManager.getInstance().getCSEBaseCid() + "/" + CfgManager.getInstance().getCSEBaseName() + "/"
				                               + "AE_" + deviceId;
				ContentInstance conInstance = null;
				if(paramMap.get(MOUri.DVC_TEMP_STATUS) != null) {
					
					parentUri = parentUri + "/" + this.getLastString(MOUri.DVC_TEMP_STATUS) + "/status"; 
					conInstance = new ContentInstance();
					
					conInstance.setContentInfo("application/json:1");
					JSONObject json = new JSONObject();
					json.put("deviceId", deviceId);
					json.put("val", paramMap.get(MOUri.DVC_TEMP_STATUS));
					
					conInstance.setContent( Base64.encode(json.toString().getBytes()) );
					
					response = createRegularResource(parentUri, conInstance, RESOURCE_TYPE.CONTENT_INST);
				}
				
				if(paramMap.get(MOUri.DVC_HUMIDITY_STATUS) != null) {
					
					parentUri = parentUri + "/" + this.getLastString(MOUri.DVC_HUMIDITY_STATUS) + "/status"; 
					conInstance = new ContentInstance();
					
					conInstance.setContentInfo("application/json:1");
					JSONObject json = new JSONObject();
					json.put("deviceId", deviceId);
					json.put("val", paramMap.get(MOUri.DVC_HUMIDITY_STATUS));
					
					conInstance.setContent( Base64.encode(json.toString().getBytes()) );
					
					response = createRegularResource(parentUri, conInstance, RESOURCE_TYPE.CONTENT_INST);
				}
				
				if(paramMap.get(MOUri.DVC_LED_STATUS) != null && !paramMap.get(MOUri.DVC_LED_STATUS).equals("")) {
					
					parentUri = parentUri + "/" + this.getLastString(MOUri.DVC_LED_STATUS) + "/result";
					conInstance = new ContentInstance();
					
					conInstance.setContentInfo("application/json:1");
					JSONObject json = new JSONObject();
					json.put("deviceId", deviceId);
					json.put("val", paramMap.get(MOUri.DVC_LED_STATUS));
					
					conInstance.setContent( Base64.encode(json.toString().getBytes()) );
					
					response = createRegularResource(parentUri, conInstance, RESOURCE_TYPE.CONTENT_INST);
				}
				
				return response;
				
			} catch (Exception e) {
				log.debug("Handled exception", e);
				return null;
				
			}
	}
	
	// added in 2017-08-03 to create onem2m device management resource related with TR-069
	public OneM2mResponse addTr69DMResource(HashMap<String, Object> paramMap) {
		
		try {

			OneM2mRequest reqMessage = new OneM2mRequest();
			String deviceId = paramMap.get(MOUri.DVC_OUI) + "-" + paramMap.get(MOUri.DVC_PRODUCTCLASS) + "-" + paramMap.get(MOUri.DVC_SERIAL);
			
			Node node = new Node();
			
			node.setNodeID(deviceId);
					
			AbsSerializer serializer = AbsSerializer.getSerializer(CONTENT_TYPE.JSON);			
			byte[] content = serializer.serialize(node).getBytes();
			String targetUri = CfgManager.getInstance().getCSEBaseCid() + "/" + CfgManager.getInstance().getCSEBaseName();
			reqMessage.setTo(targetUri);
			reqMessage.setFrom("C-AE-Internal");
			reqMessage.setContent(content);
			reqMessage.setOperation(OPERATION.CREATE);		
			reqMessage.setRequestIdentifier("REQ_" + deviceId);
			reqMessage.setResourceType(RESOURCE_TYPE.NODE);
			reqMessage.setContentType(CONTENT_TYPE.RES_JSON);
			reqMessage.setResultContent(RESULT_CONT.ATTRIBUTE);
			reqMessage.setResponseType(RESPONSE_TYPE.BLOCK_REQ);
					
			try {
				
				OneM2mResponse response = new ResourceManager(this.context).processEx(reqMessage, false);
				log.debug("addTr69DMResource processsed:"+response.toString());
				
				JSONConvertor<?> jsonCvt = ConvertorFactory.getJSONConvertor(Node.class, Node.SCHEMA_LOCATION);
				String json = new String(response.getContent());
				//json = "{ \"restSubs\" : " + json + "}"; 				// added in 2016-11-24 to process JSON ROOT, updated in 2017-07-27 from rest to restSubs
				Node resNode = (Node)jsonCvt.unmarshal(json);
				
				String mgmtObjUri = targetUri + "/" + resNode.getResourceName();
				
				// AE
				AE ae = new AE();
				String parentUri = "";
				
				String aeResourceName = "AE_" + deviceId;
				ae.setResourceName(aeResourceName);
				ae.setAppID("TR-069_AE_" + deviceId);
				ae.setRequestReachability(true);
				ae.setNodeLink(resNode.getResourceID());
				ae.addPointOfAccess(CfgManager.getInstance().getPointOfAccess() + "/" + aeResourceName);
				
				response = createRegularResource(targetUri, ae, RESOURCE_TYPE.AE);
				
				Container container = null;
				
				if(paramMap.get(MOUri.DVC_TEMP_STATUS) != null && !paramMap.get(MOUri.DVC_TEMP_STATUS).toString().equals("")) {
					// Container Temperature
					container = new Container();
					
					container.setResourceName(this.getLastString(MOUri.DVC_TEMP_STATUS));
					
					parentUri = targetUri + "/" + aeResourceName;
					
					response = createRegularResource(parentUri, container, RESOURCE_TYPE.CONTAINER);
					
					// Container Status
					
					container = new Container();
					container.setResourceName("status");
					container.setMaxNrOfInstances(1000);
					container.setMaxInstanceAge(36000);
					container.setMaxByteSize(1024000);
					
					parentUri = targetUri + "/" + aeResourceName + "/" + this.getLastString(MOUri.DVC_TEMP_STATUS);
					
					response = createRegularResource(parentUri, container, RESOURCE_TYPE.CONTAINER);
				}
				
				if(paramMap.get(MOUri.DVC_HUMIDITY_STATUS) != null && !paramMap.get(MOUri.DVC_HUMIDITY_STATUS).toString().equals("")) {
					// Container Humidity
					container = new Container();
					
					container.setResourceName(this.getLastString(MOUri.DVC_HUMIDITY_STATUS));
					
					parentUri = targetUri + "/" + aeResourceName;
					
					response = createRegularResource(parentUri, container, RESOURCE_TYPE.CONTAINER);
					
					// Container Status
					
					container = new Container();
					container.setResourceName("status");
					container.setMaxNrOfInstances(1000);
					container.setMaxInstanceAge(36000);
					container.setMaxByteSize(1024000);
					
					parentUri = targetUri + "/" + aeResourceName + "/" + this.getLastString(MOUri.DVC_HUMIDITY_STATUS);;
					
					response = createRegularResource(parentUri, container, RESOURCE_TYPE.CONTAINER);
				}
				
				if(paramMap.get(MOUri.DVC_LED_STATUS) != null && !paramMap.get(MOUri.DVC_LED_STATUS).toString().equals("")) {
					// Container LED
					container = new Container();
					
					container.setResourceName(this.getLastString(MOUri.DVC_LED_STATUS));
					
					parentUri = targetUri + "/" + aeResourceName;
					
					response = createRegularResource(parentUri, container, RESOURCE_TYPE.CONTAINER);
					
					// Container execute
					
					container = new Container();
					container.setResourceName("execute");
					container.setMaxNrOfInstances(1000);
					container.setMaxInstanceAge(36000);
					container.setMaxByteSize(1024000);
					
					parentUri = targetUri + "/" + aeResourceName + "/" + this.getLastString(MOUri.DVC_LED_STATUS);;
					
					response = createRegularResource(parentUri, container, RESOURCE_TYPE.CONTAINER);
					
					// Subscription
					Subscription sub = new Subscription();
					String notiUrl = "http://" + CfgManager.getInstance().getHostname() + ":" + CfgManager.getInstance().getRestServerPort()
							                          + "/dm/tr69/noti/receive";
					sub.addNotificationURI(notiUrl);
					response = createRegularResource(parentUri, sub, RESOURCE_TYPE.SUBSCRIPTION);
					
					// Container result
					
					container = new Container();
					container.setResourceName("result");
					container.setMaxNrOfInstances(1000);
					container.setMaxInstanceAge(36000);
					container.setMaxByteSize(1024000);
					
					parentUri = targetUri + "/" + aeResourceName + "/" + this.getLastString(MOUri.DVC_LED_STATUS);;
					
					response = createRegularResource(parentUri, container, RESOURCE_TYPE.CONTAINER);
				}
				
				// deviceInfo
				DeviceInfo deviceInfo = new DeviceInfo();
				deviceInfo.setMgmtDefinition(RESOURCE_TYPE.MGMT_DEVICE_INFO.Value());
				deviceInfo.setDeviceLabel(paramMap.get(MOUri.DVC_SERIAL).toString() );
				deviceInfo.setManufacturer( paramMap.get(MOUri.DVC_MANUFACTURER).toString() );
				deviceInfo.setModel( paramMap.get(MOUri.DVC_MODEL).toString() );
				deviceInfo.setDeviceType(paramMap.get(MOUri.DVC_PRODUCTCLASS).toString() );
				deviceInfo.setFwVersion( paramMap.get(MOUri.DVC_SW_VERSION).toString() );
				deviceInfo.setSwVersion( paramMap.get(MOUri.DVC_SW_VERSION).toString() );
				deviceInfo.setHwVersion( paramMap.get(MOUri.DVC_HW_VERSION).toString() );
				
				response = createMgmtObjectResource(mgmtObjUri, deviceInfo, RESOURCE_TYPE.MGMT_DEVICE_INFO);
				
				//reoot
				Reboot reboot = new Reboot();
				reboot.setMgmtDefinition(RESOURCE_TYPE.MGMT_REBOOT.Value());
				
				response = createMgmtObjectResource(mgmtObjUri, reboot, RESOURCE_TYPE.MGMT_REBOOT);
				
				if(paramMap.get(MOUri.DVC_MEMORY_TOTAL) != null && paramMap.get(MOUri.DVC_MEMORY_TOTAL) != null) {
					// memory
					Memory memory = new Memory();
					memory.setMgmtDefinition(RESOURCE_TYPE.MGMT_MEMORY.Value());
					memory.setMemTotal(Integer.parseInt( paramMap.get(MOUri.DVC_MEMORY_TOTAL).toString() ));
					memory.setMemAvailable(Integer.parseInt( paramMap.get(MOUri.DVC_MEMORY_FREE).toString() ));
					
					response = createMgmtObjectResource(mgmtObjUri, memory, RESOURCE_TYPE.MGMT_MEMORY);
				}
				
				if(paramMap.get(MOUri.DVC_BATTERY_LEVEL) != null && paramMap.get(MOUri.DVC_BATTERY_STATUS) != null) {
					//battery
					Battery battery = new Battery();
					battery.setMgmtDefinition(RESOURCE_TYPE.MGMT_BATTERY.Value());
					battery.setBatteryLevel(Long.parseLong( paramMap.get(MOUri.DVC_BATTERY_LEVEL).toString() ));
					battery.setBatteryStatus(Integer.parseInt( paramMap.get(MOUri.DVC_BATTERY_STATUS).toString() ));
					
					response = createMgmtObjectResource(mgmtObjUri, battery, RESOURCE_TYPE.MGMT_BATTERY);
				}
				
				if(paramMap.get(MOUri.DVC_EVT_LOG_TYPE_ID) != null && paramMap.get(MOUri.DVC_EVT_LOG_DATA) != null
															  && paramMap.get(MOUri.DVC_EVT_LOG_STATUS) != null) {
					//eventLog
					EventLog eventLog = new EventLog();
					eventLog.setMgmtDefinition(RESOURCE_TYPE.MGMT_EVENT_LOG.Value());
					eventLog.setLogTypeId( Integer.parseInt( paramMap.get(MOUri.DVC_EVT_LOG_TYPE_ID).toString() ) );
					eventLog.setLogData( paramMap.get(MOUri.DVC_EVT_LOG_DATA).toString() );
					eventLog.setLogStatus( Integer.parseInt( paramMap.get(MOUri.DVC_EVT_LOG_STATUS).toString() ) );
					
					response = createMgmtObjectResource(mgmtObjUri, eventLog, RESOURCE_TYPE.MGMT_EVENT_LOG);
					
				}
				
				return response;
			} catch (OneM2MException ex) {
				log.debug("Exception during process internal request triggered by addTr69DMResource:"+reqMessage.toString());
				log.debug("Exception:"+ex.toString());
				OneM2mResponse response = new OneM2mResponse();
				response.setResponseStatusCodeEnum(ex.getResponseStatusCode());
				response.setRequest(reqMessage);
				log.debug("Handled exception", ex);
				return response;
			}
			
		} catch (Exception e) {
			log.debug("Handled exception", e);
			return null;
			
		}
		
	}
	
	private OneM2mResponse createMgmtObjectResource(String parentUri, MgmtResource mgmtObj, RESOURCE_TYPE resType) {
		
		try {
			OneM2mRequest reqMessage = new OneM2mRequest();
			
			AbsSerializer serializer = AbsSerializer.getSerializer(CONTENT_TYPE.JSON);			
			byte[] content = serializer.serialize(mgmtObj).getBytes();
			
			reqMessage.setTo(parentUri);
			reqMessage.setFrom("C-AE-Internal");
			reqMessage.setContent(content);
			reqMessage.setOperation(OPERATION.CREATE);		
			reqMessage.setRequestIdentifier("REQ_1234567890");
			reqMessage.setResourceType(resType);
			reqMessage.setContentType(CONTENT_TYPE.RES_JSON);
			reqMessage.setResultContent(RESULT_CONT.ATTRIBUTE);
			reqMessage.setResponseType(RESPONSE_TYPE.BLOCK_REQ);
			
			try {
				
				OneM2mResponse response = new ResourceManager(this.context).processEx(reqMessage, false);
				log.debug("createMgmtObjectResource processsed:"+response.toString());
				
				return response;
			} catch (OneM2MException ex) {
				log.debug("Exception during process internal request triggered by addTr69DMResource:"+reqMessage.toString());
				log.debug("Exception:"+ex.toString());
				OneM2mResponse response = new OneM2mResponse();
				response.setResponseStatusCodeEnum(ex.getResponseStatusCode());
				response.setRequest(reqMessage);
				log.debug("Handled exception", ex);
				return response;
			}
			
		} catch (Exception e) {
			log.debug("Handled exception", e);
			return null;
			
		}
		
	}
	
	private OneM2mResponse createRegularResource(String parentUri, Resource resource, RESOURCE_TYPE resType) {
		
		try {
			OneM2mRequest reqMessage = new OneM2mRequest();
			
			AbsSerializer serializer = AbsSerializer.getSerializer(CONTENT_TYPE.JSON);			
			byte[] content = serializer.serialize(resource).getBytes();
			
			String origin = "C-AE-Internal";
			if(resType.equals(RESOURCE_TYPE.AE)) {
				origin = "S";
			} 
			
			reqMessage.setTo(parentUri);
			reqMessage.setFrom(origin);
			reqMessage.setContent(content);
			reqMessage.setOperation(OPERATION.CREATE);		
			reqMessage.setRequestIdentifier("REQ_1234567890");
			reqMessage.setResourceType(resType);
			reqMessage.setContentType(CONTENT_TYPE.RES_JSON);
			reqMessage.setResultContent(RESULT_CONT.ATTRIBUTE);
			reqMessage.setResponseType(RESPONSE_TYPE.BLOCK_REQ);
			
			try {
				
				OneM2mResponse response = new ResourceManager(this.context).processEx(reqMessage, false);
				log.debug("createMgmtObjectResource processsed:"+response.toString());
				
				return response;
			} catch (OneM2MException ex) {
				log.debug("Exception during process internal request triggered by addTr69DMResource:"+reqMessage.toString());
				log.debug("Exception:"+ex.toString());
				OneM2mResponse response = new OneM2mResponse();
				response.setResponseStatusCodeEnum(ex.getResponseStatusCode());
				response.setRequest(reqMessage);
				log.debug("Handled exception", ex);
				return response;
			}
			
		} catch (Exception e) {
			log.debug("Handled exception", e);
			return null;
			
		}
		
	}
	
	private String getLastString(String longStr) {
		int idx = longStr.lastIndexOf(".");
		return longStr.substring(idx+1);
	}
	
	// test code
	public static void main(String[] args) throws Exception {
		Document doc = new Document();
		doc.append("exec_id", "cmd_0506");
		doc.append("data", "{\"actionType\":\"ringAlarm\",\"user_id\":\"S000001\",\"alarm_id\":\"1\"}");
		String json1 = doc.toJson();
		String json2 = "{ \"exec_id\": \"commandid\", \"data\": \"{\"actionType\":\"testAlarm\",\"user_id\":\"u00002\",\"alarm_id\":\"1\"}\"}";
		String json3 = "{ \"exec_id\": \"commandid\", \"data\": \"{\\\"actionType\\\":\\\"testAlarm\\\",\\\"user_id\\\":\\\"u00002\\\",\\\"alarm_id\\\":\\\"1\\\"}\"}";
		System.out.println(json1);
		System.out.println(Base64.encode(json1.getBytes()));
		System.out.println(json2);
		System.out.println(Base64.encode(json2.getBytes()));
		System.out.println(json3);
		System.out.println(Base64.encode(json3.getBytes()));
		
	}

}
