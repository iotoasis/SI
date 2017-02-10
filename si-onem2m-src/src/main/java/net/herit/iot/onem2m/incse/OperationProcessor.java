package net.herit.iot.onem2m.incse;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.message.onem2m.OneM2mRequest.OPERATION;
import net.herit.iot.message.onem2m.OneM2mRequest.RESOURCE_TYPE;
import net.herit.iot.message.onem2m.OneM2mRequest.RESPONSE_TYPE;
import net.herit.iot.message.onem2m.OneM2mResponse.RESPONSE_STATUS;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.core.util.Utils;
import net.herit.iot.onem2m.incse.context.OneM2mContext;
import net.herit.iot.onem2m.incse.controller.ForwardingController;
import net.herit.iot.onem2m.incse.controller.NonBlockRequestController;
import net.herit.iot.onem2m.incse.facility.CfgManager;
import net.herit.iot.onem2m.incse.facility.OneM2mUtil;
import net.herit.iot.onem2m.incse.facility.QosManager;
import net.herit.iot.onem2m.incse.facility.QosManager.TpsInfo;
import net.herit.iot.onem2m.incse.manager.ManagerFactory;
import net.herit.iot.onem2m.incse.manager.ManagerInterface;
import net.herit.iot.onem2m.incse.manager.RequestManager;
import net.herit.iot.onem2m.incse.manager.ResourceManager;
import net.herit.iot.onem2m.incse.manager.dao.RemoteCSEDAO;
import net.herit.iot.onem2m.resource.RemoteCSE;
import net.herit.iot.onem2m.resource.Request;
import net.herit.iot.onem2m.resource.Resource;
import net.herit.iot.onem2m.resource.UriContent;

public class OperationProcessor {

	private OneM2mContext context;
	private Logger log = LoggerFactory.getLogger(OperationProcessor.class);
	
	public OperationProcessor(OneM2mContext context) {
		this.context = context;
	}
	
	/**
	 * 
	 * @param reqMessage
	 * @param status
	 * @return
	 */
	private OneM2mResponse createResponse(OneM2mRequest reqMessage, RESPONSE_STATUS status) {
//		OneM2mResponse resMessage = new OneM2mResponse(status);
//		resMessage.setRequestIdentifier(reqMessage.getRequestIdentifier());
//		resMessage.setFrom(reqMessage.getFrom());
//		resMessage.setEventCategory(reqMessage.getEventCategory());
		//..?

		OneM2mResponse resMessage = new OneM2mResponse(status, reqMessage);
		
		return resMessage;
	}
	
	/**
	 * 
	 * @param reqMessage
	 * @return
	 */
	public void processRequest(OneM2mRequest reqMessage) {

		try {
			// for test using POSTMan
			// - POSTMan에서 http://xxx 형식의 RequestLine을 만들수 없어서 //xxx 형식으로 전송
			// - //xxx 형식으로 전송되는 http request를 http://xxx 형식으로 변환하여 내부 처리 테스트
			//if (reqMessage.getTo().startsWith("//")) {
			//	reqMessage.setTo("http:"+reqMessage.getTo());
			//}
			
			reqMessage.setOriginator(OneM2mUtil.createOriginator(reqMessage, this.context));
			//String absId = reqMessage.getOriginator().getAbsoluteUnstructuredId();
			String entityId = reqMessage.getFrom();
			
			if (QosManager.getInstance().checkCseTps(entityId) == false) {
				TpsInfo tpsInfo = QosManager.getInstance().getTpsInfo(entityId);
				throw new OneM2MException(RESPONSE_STATUS.ACCESS_DENIED, "Tps Quota("+entityId+":"+tpsInfo.getMaxTps()+") Exceeded!!!");
			}
			
			if(reqMessage.getOperationEnum() == OPERATION.NOTIFY) {
				log.debug("NOTIFY message received..");
				sendResponse(new OneM2mResponse(RESPONSE_STATUS.OK, reqMessage));
				return;
			}

			if(!checkValidityOfPrimitiveRequest(reqMessage)) {
				throw new OneM2MException(RESPONSE_STATUS.INVALID_ARGUMENTS, "Fail to check validity of primitive request "+reqMessage.toString());
			}
			
			
			//if(checkIfForwardingNeeded(reqMessage)) {
				
			String forwardingUri = getForwardingUriIfNeeded(reqMessage.getTo());
			if (forwardingUri != null) {
				
				ForwardingController forward = new ForwardingController(context);
				reqMessage.setTo(forwardingUri);
				OneM2mResponse resMessage = forward.processRequest(reqMessage);
				if (resMessage == null) {
					throw new OneM2MException(RESPONSE_STATUS.TARGET_NOT_REACHABLE, "Fail to forward message to "+reqMessage.getRemoteHost() +"/"+reqMessage.getTo()); 
				}
				resMessage.setRequestIdentifier(reqMessage.getRequestIdentifier());
				resMessage.setRequest(reqMessage);
				log.debug("Response ContentType: ", resMessage.getContentType());
				this.sendResponse(resMessage);

				return;

			}
			
			if(!revisePrimitiveRequest(reqMessage)) {
				sendErrorResponse(createResponse(reqMessage, RESPONSE_STATUS.INTERNAL_SERVER_ERROR));
				return;
			}
	
			
			// Non-block REQUEST
			if (checkIfNonBlockRequest(reqMessage)) {
				// check if notificationUri exist in case async request
				RESPONSE_TYPE resType = reqMessage.getResponseTypeEnum();
				List<String> uriList = reqMessage.getNotificationUri(); 
				if ( resType == RESPONSE_TYPE.NBLOCK_REQ_ASYNC && (uriList == null || uriList.size() == 0)) {
					throw new OneM2MException(RESPONSE_STATUS.BAD_REQUEST, "No notification uri in Non-block async request."); 
				}
				
				// make request resource			
				RequestManager reqMngr;
					reqMngr = (RequestManager)ManagerFactory.create(RESOURCE_TYPE.REQUEST, context);
				ManagerInterface resMngr = ManagerFactory.create(reqMessage, context);
				Request req = reqMngr.createResource(reqMessage);
								
				// return accepted with request resource
				UriContent content = new UriContent();
				content.setUri(req.getUri());
				OneM2mResponse resMessage = new OneM2mResponse(RESPONSE_STATUS.ACCEPTED, reqMessage);
				resMessage.setContentObject(content);
				context.getNseManager().sendResponseMessage(resMessage);			

				Runnable arc = new NonBlockRequestController(context, reqMngr, req, resMngr, reqMessage);
				new Thread(arc).start();
				
				return;
			}
		} catch (OneM2MException e) {
			log.debug("Handled exception", e);

			OneM2mResponse resMessage = new OneM2mResponse(e.getResponseStatusCode(), reqMessage);
			resMessage.setContent(new String(e.getMessage()).getBytes());
			context.getNseManager().sendResponseMessage(resMessage);
			
			return;
		} catch (Exception e) {
			log.debug("Handled exception", e);
			OneM2mResponse resMessage = new OneM2mResponse(RESPONSE_STATUS.INTERNAL_SERVER_ERROR, reqMessage);
			resMessage.setContent(new String(e.getMessage()).getBytes());
			context.getNseManager().sendResponseMessage(resMessage);
		
			return;
		}
		//sendResponse(createResponse(reqMessage, RESPONSE_STATUS.ACCEPTED));
		
		// Blocking REQUEST
		new ResourceManager(this.context).process(reqMessage);
		
	}
	public void sendErrorResponse(OneM2mResponse resMessage) {
		///
		sendResponse(resMessage);
	}
	
	public void sendResponse(OneM2mResponse resMessage) {
		
		context.getNseManager().sendResponseMessage(resMessage);
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
	
	
	private boolean revisePrimitiveRequest(OneM2mRequest requestMessage) throws OneM2MException {

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
			} else if (uri.startsWith("//")) {
				int idx = uri.substring(2).indexOf("/");
				oriForm = uri.substring(2 + idx);
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
		
		if(to.equals(cseId)) {  // cseid의 _uri: /cseid/csename
			to = CfgManager.getInstance().getCSEBaseUri();
		}
		
		if (!OneM2mUtil.isUri(to) && to.startsWith(cseId))	{
			if (to.length() <= cseId.length()) {
				throw new OneM2MException(RESPONSE_STATUS.BAD_REQUEST, "Invalid 'to' parameter: "+to);
			} else {
				to = to.substring(cseId.length()+1);
			}
		}
		
		log.debug("revisePrimitiveRequest: {}", to);
		
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


	private String getForwardingUriIfNeeded(String to) throws OneM2MException {
		
		if (Utils.checkIfAbsoluteResId(to)) {
			if (OneM2mUtil.extractServiceProviderId(to).toLowerCase().startsWith(CfgManager.getInstance().getServiceProviderId().toLowerCase())) {
				return null;
			} else {
				return to;
			}
		} else {
			// check if cseid of to attribute is equal to local cseid
			log.debug("to={}", to);
			String cseId = OneM2mUtil.extractCseIdFromSPResId(to);
			
			//System.out.println("##################################### cseId = " + cseId + ", CfgManager.getInstance().getCSEBaseCid() = " + CfgManager.getInstance().getCSEBaseCid());
			
			if (cseId.equals(CfgManager.getInstance().getCSEBaseCid()) || cseId.equals("/"+CfgManager.getInstance().getCSEBaseName())) {
				return null;
			} else {
				
				RemoteCSEDAO dao = new RemoteCSEDAO(this.context);
				RemoteCSE cse = dao.retrieveByCseId(cseId);
				log.debug("cseId={}, cse={})", cseId, cse);
				if (cse == null || cse.getPointOfAccess() == null || cse.getCSEBase() == null) {
					throw new OneM2MException(RESPONSE_STATUS.TARGET_NOT_REACHABLE, "Cannot extract forwarding uri of target cse(cseID:"+cseId+")");
				}
				
				String poa = cse.getPointOfAccess().get(0);
				if (poa.substring(poa.length()-1, poa.length()-1) == "/") {
					poa = poa.substring(0, poa.length()-2);
				}
				return poa + to;
				
			}			
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
