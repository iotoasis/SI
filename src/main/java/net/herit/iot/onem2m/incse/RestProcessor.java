package net.herit.iot.onem2m.incse;

import io.netty.handler.codec.http.HttpResponseStatus;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.UnmarshalException;

import org.bson.Document;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.org.apache.xml.internal.security.utils.Base64;

import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.message.onem2m.OneM2mRequest.OPERATION;
import net.herit.iot.message.onem2m.OneM2mRequest.RESPONSE_TYPE;
import net.herit.iot.message.onem2m.OneM2mResponse.RESPONSE_STATUS;
import net.herit.iot.onem2m.bind.http.status.OneM2mResponseStatus;
import net.herit.iot.onem2m.core.convertor.ConvertorFactory;
import net.herit.iot.onem2m.core.convertor.JSONConvertor;
import net.herit.iot.onem2m.incse.context.RestContext;
import net.herit.iot.onem2m.incse.controller.RestCommandController;
import net.herit.iot.onem2m.incse.controller.dm.Tr069DMAdapter.MOUri;
import net.herit.iot.onem2m.incse.facility.CASAuthManager;
import net.herit.iot.onem2m.incse.facility.CfgManager;
import net.herit.iot.onem2m.incse.facility.OneM2mUtil;
import net.herit.iot.onem2m.incse.manager.dao.RestSubscriptionDAO;
import net.herit.iot.onem2m.resource.ContentInstance;
import net.herit.iot.onem2m.resource.RestAuth;
import net.herit.iot.onem2m.resource.RestCommand;
import net.herit.iot.onem2m.resource.RestSubscription;

public class RestProcessor {

	private RestContext context;
	private Logger log = LoggerFactory.getLogger(RestProcessor.class);
	private RestSubscriptionDAO subsDao;
	
	public RestProcessor(RestContext context) {
		this.context = context;
		subsDao = new RestSubscriptionDAO(context.getDatabaseManager());
	}
	
	/**
	 * 
	 * @param reqMessage
	 * @param status
	 * @return
	 */
	private OneM2mResponse createResponse(OneM2mRequest reqMessage, RESPONSE_STATUS status) {
		OneM2mResponse resMessage = new OneM2mResponse(status);
		resMessage.setRequestIdentifier(reqMessage.getRequestIdentifier());
		resMessage.setFrom(reqMessage.getFrom());
		resMessage.setEventCategory(reqMessage.getEventCategory());
		//..?
		
		return resMessage;
	}
	
	/**
	 * 
	 * @param reqMessage
	 * @return
	 */
	public void processRequest(OneM2mRequest reqMessage) {
		
		try {
						
			String uri = reqMessage.getTo();
			if (uri.startsWith("/si/subscription/data")) {
				
				JSONConvertor<?> jsonCvt = ConvertorFactory.getJSONConvertor(RestSubscription.class, RestSubscription.SCHEMA_LOCATION);
				String json = new String(reqMessage.getContent());
				json = "{ \"restSubs\" : " + json + "}"; 				// added in 2016-11-24 to process JSON ROOT, updated in 2017-07-27 from rest to restSubs
				RestSubscription subs = (RestSubscription)jsonCvt.unmarshal(json);

				log.debug(subs.toString());
				
				subsDao.addSubscription(subs.getUri(), subs.getNotificationUri());
				
				Document doc = new Document();
				doc.put("code", "2000");
				doc.put("message", "");
				doc.put("content", "");
				
				this.context.getRestHandler().sendResponseMessage(reqMessage.getRequestIdentifier(), "200", doc.toJson());
				
				log.debug("Subscription operation called!!!");
			} else if (uri.startsWith("/si/command")) {
				
				JSONConvertor<?> jsonCvt = ConvertorFactory.getJSONConvertor(RestCommand.class, null);
				String json = new String(reqMessage.getContent());
				json = "{ \"restComm\" : " + json + "}"; 				// added in 2016-11-24 to process JSON ROOT, updated in 2017-07-27 from rest to restComm
				RestCommand cmd = (RestCommand)jsonCvt.unmarshal(json);
				
				log.debug(cmd.toString());
				
				// for demo
				this.context.getRestHandler().sendResponseMessage(reqMessage.getRequestIdentifier(), "200", 
								"{\"code\":\"2000\",\"_commandId\":\""+cmd.getCommandId()+"\"}");	
				
				OneM2mResponse response = RestCommandController.getInstance().processCommand(cmd);

				// for demo
				/*
				if (response == null) {
					this.context.getRestHandler().sendResponseMessage(reqMessage.getRequestIdentifier(), "500", "fail to send command to "+ cmd.getUri());
					return;
				}
				
				RESPONSE_STATUS statuscode = response.getResponseStatusCodeEnum();
				if (statuscode == RESPONSE_STATUS.CREATED) {
					this.context.getRestHandler().sendResponseMessage(reqMessage.getRequestIdentifier(), "200", 
									"{\"code\":\"2000\",\"_commandId\":\""+cmd.getCommandId()+"\"}");	
				} else {
					HttpResponseStatus httpStatus = OneM2mResponseStatus.valueOf(statuscode);
					this.context.getRestHandler().sendResponseMessage(reqMessage.getRequestIdentifier(), Integer.toString(httpStatus.code()), 
							"{\"code\":\""+response.getResponseStatusCodeEnum().Value()+"\",\"message\":\""+httpStatus.toString()+"\"}");
				}
				*/
			} else if (uri.startsWith("/si/subscription/add")) {
				JSONConvertor<?> jsonCvt = ConvertorFactory.getJSONConvertor(RestSubscription.class, RestSubscription.SCHEMA_LOCATION);
				String json = new String(reqMessage.getContent());
				json = "{ \"restSubs\" : " + json + "}"; 				// added in 2016-11-24 to process JSON ROOT, updated in 2017-07-27 from rest to restSubs
				RestSubscription subs = (RestSubscription)jsonCvt.unmarshal(json);

				log.debug(subs.toString());
				
				//subsDao.addSubscription(subs.getUri(), subs.getNotificationUri());
				subsDao.addSubscription(subs.getUri(), subs.getNotificationUri(), subs.getCommandId());		// added in 2016-12-07	
				
				Document doc = new Document();
				doc.put("code", "2000");
				doc.put("message", "");
				doc.put("content", "");
				
				this.context.getRestHandler().sendResponseMessage(reqMessage.getRequestIdentifier(), "200", doc.toJson());
				
				log.debug("Subscription create operation called!!!");
			} else if (uri.startsWith("/si/subscription/del")) {
				JSONConvertor<?> jsonCvt = ConvertorFactory.getJSONConvertor(RestSubscription.class, RestSubscription.SCHEMA_LOCATION);
				String json = new String(reqMessage.getContent());
				json = "{ \"restSubs\" : " + json + "}"; 				// added in 2016-11-24 to process JSON ROOT, updated in 2017-07-27 from rest to restSubs
				RestSubscription subs = (RestSubscription)jsonCvt.unmarshal(json);

				log.debug(subs.toString());
				
				subsDao.delSubscription(subs.getUri(), subs.getNotificationUri());
				
				Document doc = new Document();
				doc.put("code", "2000");
				doc.put("message", "");
				doc.put("content", "");
				
				this.context.getRestHandler().sendResponseMessage(reqMessage.getRequestIdentifier(), "200", doc.toJson());
				
				log.debug("Subscription delete operation called!!!");
			} else if (uri.startsWith("/si/control")) { 
				JSONConvertor<?> jsonCvt = ConvertorFactory.getJSONConvertor(RestCommand.class, null);
				String json = new String(reqMessage.getContent());
				json = "{ \"restComm\" : " + json + "}"; 				// added in 2016-11-24 to process JSON ROOT, updated in 2017-07-27 from rest to restComm
				RestCommand cmd = (RestCommand)jsonCvt.unmarshal(json);
				
				log.debug(cmd.toString());
				
				// for demo
				this.context.getRestHandler().sendResponseMessage(reqMessage.getRequestIdentifier(), "200", 
								"{\"code\":\"2000\",\"_commandId\":\""+cmd.getCommandId()+"\"}");	
				
				OneM2mResponse response = RestCommandController.getInstance().processControl(cmd);
				
			}else if (uri.startsWith("/si/newcontrol")) { 
				JSONConvertor<?> jsonCvt = ConvertorFactory.getJSONConvertor(RestCommand.class, null);
				String json = new String(reqMessage.getContent());
				json = "{ \"restComm\" : " + json + "}"; 				// added in 2016-11-24 to process JSON ROOT, updated in 2017-07-27 from rest to restComm
				RestCommand cmd = (RestCommand)jsonCvt.unmarshal(json);
				String resourceUri = cmd.getUri();
				int pos = resourceUri.lastIndexOf('/');
			
				String devId = resourceUri.substring(pos+1, resourceUri.length());
				
				CASAuthManager casAuthMgr = new CASAuthManager();
				casAuthMgr.initialize(context);
				
				Document result = casAuthMgr.getAuth(devId);
				
				if(result == null) {
					this.context.getRestHandler().sendResponseMessage(reqMessage.getRequestIdentifier(), "400", 
							"{\"code\":\"4000\",\"_commandId\":\""+cmd.getCommandId()+"\"}");
				} else {
					log.debug(cmd.toString());
					
					// for demo
					this.context.getRestHandler().sendResponseMessage(reqMessage.getRequestIdentifier(), "200", 
									"{\"code\":\"2000\",\"_commandId\":\""+cmd.getCommandId()+"\"}");	
					
					OneM2mResponse response = RestCommandController.getInstance().processControl(cmd);
				}
				
			} else if (uri.startsWith("/si/ipe/lwm2m")) { 	// 2017-02-08
				JSONConvertor<?> jsonCvt = ConvertorFactory.getJSONConvertor(RestCommand.class, null);
				String json = new String(reqMessage.getContent());
				json = "{ \"restComm\" : " + json + "}"; 				// added in 2016-11-24 to process JSON ROOT, updated in 2017-07-27 from rest to restComm
				RestCommand cmd = (RestCommand)jsonCvt.unmarshal(json);
				
				log.debug(cmd.toString());
				
				// for demo
				this.context.getRestHandler().sendResponseMessage(reqMessage.getRequestIdentifier(), "200", 
								"{\"code\":\"2000\",\"_commandId\":\""+cmd.getCommandId()+"\"}");	
				
				OneM2mResponse response = RestCommandController.getInstance().processIpeLwm2m(cmd);
				
			} else if(uri.startsWith("/si/dev_t1_reg") || uri.startsWith("/si/dev_upd")) {
			
				JSONConvertor<?> jsonCvt = ConvertorFactory.getJSONConvertor(RestAuth.class, null);
				String json = new String(reqMessage.getContent());
				json = "{ \"restAuth\" : " + json + "}"; 				// added in 2016-11-24 to process JSON ROOT, updated in 2017-07-27 from rest to restAuth
	
				RestAuth auth = (RestAuth)jsonCvt.unmarshal(json);
				
				CASAuthManager casAuthMgr = new CASAuthManager();
				casAuthMgr.initialize(context);
				
				boolean result = casAuthMgr.updateAuth(auth.getDevId(), auth.getPwd());
				
				Document doc = new Document();
				
				if(result) {
					doc.put("code", "200");
					doc.put("message", "");
				} else {
					doc.put("code", "400");
					doc.put("message", "");
				}
				
				doc.put("content", "device Id:" + auth.getDevId());
				
				this.context.getRestHandler().sendResponseMessage(reqMessage.getRequestIdentifier(), "200", doc.toJson());
				
			} else if(uri.startsWith("/si/dev_t2_reg")) {
				
				JSONConvertor<?> jsonCvt = ConvertorFactory.getJSONConvertor(RestAuth.class, null);
				String json = new String(reqMessage.getContent());
				json = "{ \"restAuth\" : " + json + "}"; 				// added in 2016-11-24 to process JSON ROOT, updated in 2017-07-27 from rest to restAuth
				RestAuth auth = (RestAuth)jsonCvt.unmarshal(json);
				
				CASAuthManager casAuthMgr = new CASAuthManager();
				casAuthMgr.initialize(context);
				
				boolean result = casAuthMgr.regAuth(auth.getDevId());
				
				Document doc = new Document();
				
				if(result) {
					doc.put("code", "200");
					doc.put("message", "");
				} else {
					doc.put("code", "400");
					doc.put("message", "");
				}
				
				doc.put("content", "device Id:" + auth.getDevId());
				
				this.context.getRestHandler().sendResponseMessage(reqMessage.getRequestIdentifier(), "200", doc.toJson());
				
			} else if(uri.startsWith("/si/dev_inf")) {
				
				JSONConvertor<?> jsonCvt = ConvertorFactory.getJSONConvertor(RestAuth.class, null);
				String json = new String(reqMessage.getContent());
				json = "{ \"restAuth\" : " + json + "}"; 				// added in 2016-11-24 to process JSON ROOT, updated in 2017-07-27 from rest to restAuth
				RestAuth auth = (RestAuth)jsonCvt.unmarshal(json);
				
				CASAuthManager casAuthMgr = new CASAuthManager();
				casAuthMgr.initialize(context);
				
				Document result = casAuthMgr.getAuth(auth.getDevId());
				
				Document doc = new Document();
				
				if(result != null) {
					doc.put("code", "200");
					doc.put("message", "");
					doc.put("content", result.toJson());
				} else {
					doc.put("code", "400");
					doc.put("message", "");
					doc.put("content", "");
				}
				
				this.context.getRestHandler().sendResponseMessage(reqMessage.getRequestIdentifier(), "200", doc.toJson());
				
			} else if(uri.startsWith("/si/dev_del")) {
				
				JSONConvertor<?> jsonCvt = ConvertorFactory.getJSONConvertor(RestAuth.class, null);
				String json = new String(reqMessage.getContent());
				json = "{ \"restAuth\" : " + json + "}"; 				// added in 2016-11-24 to process JSON ROOT, updated in 2017-07-27 from rest to restAuth
				RestAuth auth = (RestAuth)jsonCvt.unmarshal(json);
				
				CASAuthManager casAuthMgr = new CASAuthManager();
				casAuthMgr.initialize(context);
				
				Document doc = new Document();
				
				if(auth.getDevId() != null && !auth.getDevId().equals("")) {
					
					casAuthMgr.deleteAuth(auth.getDevId());
					
					doc.put("code", "200");
					doc.put("message", "");
				} else {
					doc.put("code", "400");
					doc.put("message", "");
				}
				
				doc.put("content", "device Id:" + auth.getDevId());
				
				this.context.getRestHandler().sendResponseMessage(reqMessage.getRequestIdentifier(), "200", doc.toJson());
				
			} else if(uri.startsWith("/dm/tr69/add/acsInfo")) {
				
			} else if(uri.startsWith("/dm/tr69/add/cpe")) {
				String json = new String(reqMessage.getContent());
				
				HashMap<String, Object> paramMap = new HashMap<String, Object>();
				
				JSONParser parser = new JSONParser();
				JSONObject jsonObject = (JSONObject)parser.parse(json);
				JSONArray jsonParams = (JSONArray)jsonObject.get("param");
				String paramName;
				Object paramValue;
				for(Object paramObject : jsonParams) {
					if(paramObject instanceof JSONObject) {
						paramName = ((JSONObject)paramObject).get("Name").toString();
						paramValue = ((JSONObject)paramObject).get("Value");
						paramMap.put(paramName, paramValue);
						System.out.println("Name : " + paramName + ", Value : " + paramValue  );
					}
				}
				
				String deviceId = paramMap.get(MOUri.DVC_OUI) + "-" + paramMap.get(MOUri.DVC_PRODUCTCLASS) + "-" + paramMap.get(MOUri.DVC_SERIAL);
				
				this.context.getRestHandler().sendResponseMessage(reqMessage.getRequestIdentifier(), "200", 
						"{\"code\":\"2000\",\"deviceId\":\""+deviceId+"\"}");	
		
				OneM2mResponse response = RestCommandController.getInstance().addTr69DMResource(paramMap);
				
			//	System.out.println("device Id : " + deviceId  );
				
				
			} else if(uri.startsWith("/dm/tr69/noti/passive") || uri.startsWith("/dm/tr69/noti/active")) {
				String json = new String(reqMessage.getContent());
				
				HashMap<String, Object> paramMap = new HashMap<String, Object>();
				
				JSONParser parser = new JSONParser();
				JSONObject jsonObject = (JSONObject)parser.parse(json);
				JSONArray jsonParams = (JSONArray)jsonObject.get("param");
				
				String paramName;
				Object paramValue;
				for(Object paramObject : jsonParams) {
					if(paramObject instanceof JSONObject) {
						paramName = ((JSONObject)paramObject).get("Name").toString();
						paramValue = ((JSONObject)paramObject).get("Value");
						paramMap.put(paramName, paramValue);
					}
				}
				
				this.context.getRestHandler().sendResponseMessage(reqMessage.getRequestIdentifier(), "200", 
						"{\"code\":\"2000\",\"notification\":\""+paramMap.toString()+"\"}");	
		
				OneM2mResponse response = RestCommandController.getInstance().createContentInstance(paramMap);
				
			} else if(uri.startsWith("/dm/tr69/noti/receive")) {
				
				JSONConvertor<?> jsonCvt = ConvertorFactory.getJSONConvertor(ContentInstance.class, ContentInstance.SCHEMA_LOCATION);
				String json = new String(reqMessage.getContent());
				ContentInstance conInstance = (ContentInstance)jsonCvt.unmarshal(json);
				
				
				this.context.getRestHandler().sendResponseMessage(reqMessage.getRequestIdentifier(), "200", 
						"{\"code\":\"2000\",\"notification\":\""+conInstance.toString()+"\"}");
				
				String content = conInstance.getContent();
				String jsonContent = new String(Base64.decode(content), StandardCharsets.UTF_8);
				JSONParser parser = new JSONParser();
				JSONObject jsonObject = (JSONObject)parser.parse(json);
				HashMap<String, Object> dataMap = new HashMap<String, Object>();
				String deviceId = "";
				if(jsonObject.get("deviceId") != null) {
					deviceId = jsonObject.get("deviceId").toString();
				}
				dataMap.put(MOUri.DVC_LED_STATUS, jsonObject.get("val"));
				
				RestCommandController.getInstance().setParameterValues(deviceId, dataMap);
			}
			

		} catch (UnmarshalException e) {

			//OneM2mResponse resMessage = new OneM2mResponse(RESPONSE_STATUS.BAD_REQUEST, reqMessage);
			//resMessage.setContent(new String(e.getMessage()).getBytes());
			
			Document doc = new Document();
			doc.put("code", RESPONSE_STATUS.BAD_REQUEST.Value());
			doc.put("message", RESPONSE_STATUS.BAD_REQUEST.name() +", "+e.toString());
			doc.put("content", "");
			context.getRestHandler().sendResponseMessage(reqMessage.getRequestIdentifier(), "400", doc.toJson());
		
			return;
			
		} catch (Exception e) {
			log.debug("Handled exception", e);

			Document doc = new Document();
			doc.put("code", RESPONSE_STATUS.INTERNAL_SERVER_ERROR.name());
			doc.put("message", RESPONSE_STATUS.INTERNAL_SERVER_ERROR.name() +", "+e.toString());
			doc.put("content", "");
			context.getRestHandler().sendResponseMessage(reqMessage.getRequestIdentifier(), "500", doc.toJson());
		
			return;
		}
		
	}
	public void sendErrorResponse(OneM2mResponse resMessage) {
		///
		sendResponse(resMessage);
	}
	
	public void sendResponse(OneM2mResponse resMessage) {
		
		context.getRestHandler().sendHttpResponse(resMessage);
	}

	
	private boolean checkValidityOfPrimitiveRequest(OneM2mRequest requestMessage) {
		OPERATION operation = requestMessage.getOperationEnum();
		switch(operation) {
		case CREATE:
			break;
		
		case RETRIEVE:
			break;
		
		case UPDATE:
			break;
			
		case DELETE:
			break;
			
		case DISCOVERY:
			break;
		
		case NOTIFY:
			break;

		default:
			return false;
		}
		
		return true;
	}
	
	
	private boolean revisePrimitiveRequest(OneM2mRequest requestMessage) {

		// if absolute-form of local uri, change to origin-form
		String uri = requestMessage.getTo();
		int idxHttp = uri.toLowerCase().indexOf("http://");
		int idxHttps = uri.toLowerCase().indexOf("https:");
		
		if (checkIfLocalUri(uri)) {
			String oriForm = "";
			if (idxHttp >= 0) {
				int idx = uri.substring(idxHttp+7).indexOf("/");
				oriForm = uri.substring(idxHttp+7+idx);
			} else if (idxHttps >= 0) {
				int idx = uri.substring(idxHttps+8).indexOf("/");
				oriForm = uri.substring(idxHttps+8+idx);
			}
			requestMessage.setTo(oriForm);
		}
		
		// cse_id를 포함하고 있을 경우 cseid를 제거 => cse relative resource id로 변환
//		String to = requestMessage.getTo();
//		String cseId = CfgManager.getInstance().getCSEBaseCid();
//		if (to.startsWith(cseId)) {
//			requestMessage.setTo(to.substring(cseId.length()));
//		}
//		
		// cse_id가 없을 경우 cseId를 추가 (SP relative로 처리)
		String to = requestMessage.getTo();
		String cseId = CfgManager.getInstance().getCSEBaseCid();
		if (!to.startsWith("/"))	to = "/"+to;
		if (!to.startsWith(cseId)) 	to = cseId + to;
		
		if (!OneM2mUtil.isUri(to) && to.startsWith(cseId))	to = to.substring(cseId.length()+1);
		
		requestMessage.setTo(to);
		
		return true;
		
	}
	
	private boolean checkIfLocalUri(String uri) {
		String spId = CfgManager.getInstance().getServiceProviderId();
		if (uri.toLowerCase().indexOf(spId) >= 0) {
			return true;
		} else {
			return false;
		}
	}




//	private boolean checkIfForwardingNeeded(OneM2mRequest reqMessage) {
//		
//		String to = reqMessage.getTo();
//		
//		//uri.toLowerCase().indexOf("http://") >= 0
//		if (OneM2mUtil.checkIfAbsoluteResId(to)) {
//			String spId = OneM2mUtil.extractServiceProviderId(to);
//			return spId.equalsIgnoreCase(CfgManager.getInstance().getServiceProviderId());
//		} else {
//			// check if cseid of to attribute is equal to local cseid
//			String cseId = OneM2mUtil.extractCseIdFromSPResId(to);
//			return !cseId.equals(CfgManager.getInstance().getCSEBaseCid());
//			
//		}
//		
//	}
	
	private boolean checkIfNonBlockRequest(OneM2mRequest reqMessage) {
		return reqMessage.getResponseTypeEnum() == RESPONSE_TYPE.NBLOCK_REQ_ASYNC ||
				reqMessage.getResponseTypeEnum() == RESPONSE_TYPE.NBLOCK_REQ_SYNC;
	}	
}
