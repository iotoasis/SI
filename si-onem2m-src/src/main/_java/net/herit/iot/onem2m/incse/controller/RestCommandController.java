package net.herit.iot.onem2m.incse.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.bson.Document;
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
import net.herit.iot.onem2m.ae.emul.AppEmulator;
import net.herit.iot.onem2m.ae.emul.Constants;
import net.herit.iot.onem2m.bind.codec.AbsSerializer;
import net.herit.iot.onem2m.bind.http.client.HttpClient;
import net.herit.iot.onem2m.core.convertor.ConvertorFactory;
import net.herit.iot.onem2m.core.convertor.JSONConvertor;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.incse.context.OneM2mContext;
import net.herit.iot.onem2m.incse.facility.CfgManager;
import net.herit.iot.onem2m.incse.facility.OneM2mUtil;
import net.herit.iot.onem2m.incse.manager.ResourceManager;
import net.herit.iot.onem2m.resource.ContentInstance;
import net.herit.iot.onem2m.resource.RestCommand;
import net.herit.iot.onem2m.resource.RestCommandCI;
import net.herit.iot.onem2m.resource.RestCommandResult;


public class RestCommandController {
	
	private static RestCommandController INSTANCE = new RestCommandController();

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
					OneM2mRequest reqMessage = new OneM2mRequest();
					
					reqMessage.setTo(extractToFromFullUri(notiUri));
					reqMessage.setOperation(OPERATION.CREATE);
					reqMessage.setContentType(CONTENT_TYPE.JSON);
					reqMessage.setContent(doc.toJson().getBytes());
					
//					new HttpClient().process(notiUri, reqMessage);
					HttpClient.getInstance().sendRequest(notiUri, reqMessage);
					
				} catch (Exception e) {
					
					log.debug("Handled exception", e);
					
				}
			}

			expireTimer.schedule(new ExpiredCmdTimer(), CfgManager.getInstance().getCommandExpireTimerInterval()*1000);
		}	
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
