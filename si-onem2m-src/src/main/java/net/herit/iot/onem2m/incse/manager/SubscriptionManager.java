package net.herit.iot.onem2m.incse.manager;

import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.List;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.herit.iot.message.onem2m.OneM2mResponse.RESPONSE_STATUS;
import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.message.onem2m.OneM2mRequest.OPERATION;
import net.herit.iot.message.onem2m.OneM2mRequest.RESOURCE_TYPE;
import net.herit.iot.message.onem2m.format.Enums.CONTENT_TYPE;
import net.herit.iot.onem2m.core.convertor.ConvertorFactory;
import net.herit.iot.onem2m.core.convertor.JSONConvertor;
import net.herit.iot.onem2m.core.convertor.XMLConvertor;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.core.util.Utils;
import net.herit.iot.onem2m.incse.context.OneM2mContext;
import net.herit.iot.onem2m.incse.controller.NotificationController;
import net.herit.iot.onem2m.incse.facility.CfgManager;
import net.herit.iot.onem2m.incse.facility.OneM2mUtil;
import net.herit.iot.onem2m.incse.manager.dao.DAOInterface;
import net.herit.iot.onem2m.incse.manager.dao.SubscriptionDAO;
import net.herit.iot.onem2m.resource.Notification;
import net.herit.iot.onem2m.resource.Resource;
import net.herit.iot.onem2m.resource.Subscription;

public class SubscriptionManager extends AbsManager {
	
	static String ALLOWED_PARENT = "accessControlPolicy,accessControlPolicyAnnc,AE,AEAnnc,container," +
			"CSEBase,delivery,eventConfig,execInstance,group,groupAnnc,locationPolicy,mgmtCmd,mgmtObj," +
			"mgmtObjAnnc,m2mServiceSubscriptionProfile,node,nodeAnnc,serviceSubscribedNode,remoteCSE," +
			"remoteCSEAnnc,request,schedule,statsCollect,statsConfig,flexContainer" ;
		//	"allJoynApp,allJoynSvcObject,allJoynInterface,allJoynMethod,allJoynMethodCall,allJoynProperty"; 	// added in 2016-11-21
	static RESOURCE_TYPE RES_TYPE = RESOURCE_TYPE.SUBSCRIPTION; 
	
	private Logger log = LoggerFactory.getLogger(SubscriptionManager.class);

	private static final String TAG = SubscriptionManager.class.getName();
	
	public SubscriptionManager(OneM2mContext context) {
		super(RES_TYPE, ALLOWED_PARENT);
		this.context = context;
	}
	
	
	@Override
	public OneM2mResponse create(OneM2mRequest reqMessage) throws OneM2MException {

		return create(reqMessage, this);
		
	}

	@Override
	public OneM2mResponse retrieve(OneM2mRequest reqMessage) throws OneM2MException {

		return retrieve(reqMessage, this);

	}

	@Override
	public OneM2mResponse update(OneM2mRequest reqMessage) throws OneM2MException {

		return update(reqMessage, this);
				
	}

	@Override
	public OneM2mResponse delete(OneM2mRequest reqMessage) throws OneM2MException {

		return delete(reqMessage, this);
		
	}

	@Override
	public OneM2mResponse notify(OneM2mRequest reqMessage) throws OneM2MException {
		// TODO Auto-generated method stub

		return new OneM2mResponse();
	}

	@Override 
	public void notification(Resource parentRes, Resource reqRes, Resource orgRes, OPERATION op) throws OneM2MException {
		if (op == OPERATION.DELETE) {
			log.debug("DELETE notification");
			Document reqDoc = reqRes == null? null : Document.parse( ((SubscriptionDAO)getDAO()).resourceToJson(reqRes));
			Document orgDoc = orgRes == null? null : Document.parse( ((SubscriptionDAO)getDAO()).resourceToJson(orgRes));
			NotificationController.getInstance().notify(parentRes, reqDoc, reqRes, orgDoc, orgRes, op, this);
		}
	}
	

	@Override
	public DAOInterface getDAO() {
		return new SubscriptionDAO(context);
	}

	@Override
	public Class<?> getResourceClass() {
		return Subscription.class;
	}

	@Override
	public XMLConvertor<?> getXMLConveter() throws OneM2MException {
		return ConvertorFactory.getXMLConvertor(Subscription.class, Subscription.SCHEMA_LOCATION);
	}

	@Override
	public JSONConvertor<?> getJSONConveter() throws OneM2MException {
		return ConvertorFactory.getJSONConvertor(Subscription.class, Subscription.SCHEMA_LOCATION);
	}

	@Override
	public void validateResource(Resource res, OneM2mRequest req, Resource toRes) throws OneM2MException {
		log.debug("SubscriptionManager.validate called: {}, {}", res.getUri(), req.getOperationEnum().Name());
		
		SubscriptionDAO dao = new SubscriptionDAO(context);
		Subscription subRes = (Subscription)res;
		OPERATION op = req.getOperationEnum(); 
		String oriUri = req.getFrom();
		
		switch (op) {
		case CREATE:
			// check if this subscribed-to resource is subscribable. - TARGET_NOT_SUBSCRIBABLE.
			// This will be checked using ALLOWED_PARENT
			
			// check if the Originator has privileges for retrieving the subscribed-to resource - NO_PRIVILEGE
			if (OneM2mUtil.checkAccessControlPolicy(req, OPERATION.RETRIEVE, 
												OneM2mUtil.extractAccessControlPolicies(toRes, context)) == false) {
				throw new OneM2MException(RESPONSE_STATUS.NO_PRIVILEGE, "Originator("+oriUri+") does not have read privilege of resource("+toRes.getUri()+")!");
			};
			
			// if the notificationURI is not the Originator, the Hosting CSE should send a Notify request primitive to the
			// notificationURI with verificationRequest parameter set as TRUE - SUBSCRIPTION_VERIFICATION_INITIATION_FAILED
			// SUBSCRIPTION_CREATOR_HAS_NO_PRIVILEGE, SUBSCRIPTION_HOST_HAS_NO_PRIVILEGE
			// - notificationuri가 originator인지 여부 판단 방법 ?????

			
			List<String> uriList = subRes.getNotificationURI();
			if (uriList == null || uriList.size() == 0) {
				throw new OneM2MException(RESPONSE_STATUS.BAD_REQUEST, "notification uri omitted!");
			}
//			Iterator<String> it = uriList.iterator();
//			while (it.hasNext()) {
//				String notiUri = it.next();
//				try {
//					// TODO: nu : resource_id possible...
//					if (!Utils.checkIfSameHost(notiUri, req.getRemoteHost()) && 
//									Utils.checkIfSameHost(notiUri, CfgManager.getInstance().getHostname())) {
//					
//						log.debug("Notification validation request send: {}", notiUri);
//		
//						Notification noti = new Notification();
//						noti.setVerificationRequest(true);
//						noti.setSubscriptionDeletion(false);
//						noti.setCreator(req.getFrom());
//						
//						OneM2mRequest notiReq = new OneM2mRequest();
//						notiReq.setContentObject(noti);
//						notiReq.setFrom("/"+CfgManager.getInstance().getCSEBaseName());
//						notiReq.setOperation(OPERATION.NOTIFY);
//						notiReq.setRequestIdentifier(OneM2mUtil.createRequestId());
//						notiReq.setContentType(OneM2mUtil.isXMLContentType(req.getContentType()) ? CONTENT_TYPE.XML : CONTENT_TYPE.JSON);
//						
//						OneM2mResponse notiRes;
//						notiReq.setTo(Utils.extractResourceFromUrl(notiUri));	
//						notiRes = context.getNseManager().sendRequestMessage(Utils.extractBaseurlFromUrl(notiUri), notiReq);
//							
//						if (notiRes == null) {
//							throw new OneM2MException(RESPONSE_STATUS.BAD_REQUEST, "Notification uri("+notiUri+") unreachable!");
//						};
//						
//						switch (notiRes.getResponseStatusCodeEnum()) {
//						case OK:
//						case ACCEPTED:
//							continue;
//						case SUBSCRIP_CREATOR_NO_PRIVILEGE:
//						case SUBSCRIP_HOST_NO_PRIVILEGE:
//							throw new OneM2MException(notiRes.getResponseStatusCodeEnum(), "Error response from notification uri("+notiUri+")");
//						default:
//							throw new OneM2MException(RESPONSE_STATUS.BAD_REQUEST, "Not verified notification uri("+notiUri+")");
//							
//						} 
//					}
//						
//				} catch (MalformedURLException e) {
//					throw new OneM2MException(RESPONSE_STATUS.BAD_REQUEST, "Malformed Notification uri("+notiUri+")!");					
//				}
//			}			
			
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
			//break;
			
		}
		
		super.validateResource(res, req, toRes);
	}
	
	@Override
	public void updateResource(Resource resource, OneM2mRequest reqMessage) throws OneM2MException {
		Subscription subRes = (Subscription)resource;
		subRes.setCreator(reqMessage.getFrom());
		
		//// TS-0001-XXX-V1_13_1 - 10.1.1.1	Non-registration related CREATE procedure (ExpirationTime..)
		if(subRes.getExpirationTime() == null) {
			subRes.setExpirationTime(CfgManager.getInstance().getDefaultExpirationTime());
		}
		// END - Non-registration related CREATE procedure
	}
}
