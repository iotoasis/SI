package net.herit.iot.onem2m.incse.controller;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.herit.iot.message.onem2m.format.Enums.CONTENT_TYPE;
import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.message.onem2m.OneM2mRequest.OPERATION;
import net.herit.iot.message.onem2m.OneM2mRequest.RESOURCE_TYPE;
import net.herit.iot.onem2m.bind.http.client.AsyncResponseListener;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.incse.AccessPointManager;
import net.herit.iot.onem2m.incse.context.OneM2mContext;
import net.herit.iot.onem2m.incse.facility.CfgManager;
import net.herit.iot.onem2m.incse.facility.DatabaseManager;
import net.herit.iot.onem2m.incse.facility.NseManager;
import net.herit.iot.onem2m.incse.facility.OneM2mUtil;
import net.herit.iot.onem2m.incse.manager.AbsManager;
import net.herit.iot.onem2m.incse.manager.dao.ResourceDAO;
import net.herit.iot.onem2m.resource.Attribute;
import net.herit.iot.onem2m.resource.EventNotificationCriteria;
import net.herit.iot.onem2m.resource.Naming;
import net.herit.iot.onem2m.resource.Notification;
import net.herit.iot.onem2m.resource.PrimitiveContent;
import net.herit.iot.onem2m.resource.RequestPrimitive;
import net.herit.iot.onem2m.resource.Resource;
import net.herit.iot.onem2m.resource.Subscription;
import net.herit.iot.onem2m.resource.Subscription.NOTIFICATIONCONTENT_TYPE;
import net.herit.iot.onem2m.resource.Subscription.NOTIFICATIONEVENT_TYPE;


public class NotificationController extends AbsController implements AsyncResponseListener {

	private Logger log = LoggerFactory.getLogger(NotificationController.class);
	private OneM2mContext ctx = null;
	//private LogManager logManager = null;
	private NseManager nseManager = null;
	private DatabaseManager dbManager = null;
	
	private static final long TIME_NOT_SET = -1L;
	private static final int NOT_SET = -1;
//	private static final String DATE_FORMAT = "yyyyMMdd'T'HHmmss";
	private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(Naming.DATE_FORMAT);
	
	private static NotificationController INSTANCE = new NotificationController();
	
	private NotificationController() {
	}
	
	public static NotificationController getInstance() {
		return INSTANCE;
	}

	public void initialize(OneM2mContext context) {
		super.setContext(context);

		this.ctx = context;
		this.nseManager = context.getNseManager();
		this.dbManager = context.getDatabaseManager();

	}
	
	/* accessControlPolicy, accessControlPolicyAnnc, AE, AEAnnc,
	 * container, CSEBase, delivery, eventConfig, execInstance, 
	 * group, groupAnnc, locationPolicy, mgmtCmd, mgmtObj, mgmtObjAnnc,
	 * m2mServiceSubscriptionProfile, node, nodeAnnc, serviceSubscribedNode, 
	 * remoteCSE, remoteCSEAnnc, request, schedule, statsCollect, statsConfig
	*/
	

	
	public enum STDEVENT_CATS {
		IMMEDIATE(2,  "Immediate"),
		BESETEFFORT(3, "BestEffort"),
		LATEST(4, "Latest");
		
		final int value;
		final String name;
		private STDEVENT_CATS(int value, String name) {
			this.value = value;
			this.name = name;
		}
		
		public int Value() {
			return this.value;
		}
		
		public String Name() {
			return this.name;
		}
	
		private static final Map<Integer, STDEVENT_CATS> map = 
				new HashMap<Integer, STDEVENT_CATS>();
		static {
			for(STDEVENT_CATS en : STDEVENT_CATS.values()) {
				map.put(en.value, en);
			}
		}
		
		public static STDEVENT_CATS get(int value) {
			STDEVENT_CATS en = map.get(value);
			return en;
		}
	};
	
	private void checkNotificationPolicy(OneM2mRequest reqMessage, Subscription sub, Notification noti, 
//			Resource reqRes, Resource orgRes) {
			Document reqDoc, Resource reqRes, Document orgDoc, Resource orgRes, ResourceDAO dao, OPERATION op) throws Exception {
		
		Integer notiType = sub.getNotificationContentType();
		if(notiType == null) notiType = 0;
		

		switch (NOTIFICATIONCONTENT_TYPE.get(notiType.intValue())) {
		case MODIFIED_ATTRIBUTES:
//			Resource resource = dao.createResourceWithDoc(reqRes);
			//noti.getNotificationEvent().setRepresentation(reqRes);
			noti.getNotificationEvent().setRepresentation(new Notification.NotificationEvent.Representation(reqRes));
			break;
		case RESOURCE_ID:
			String resourceId = null;
			if(op == OPERATION.CREATE) {
				resourceId = reqDoc.getString(Naming.RESOURCEID_SN);
			} else {
				resourceId = orgDoc.getString(Naming.RESOURCEID_SN);
			}
			
			//UriContent content = new UriContent();
			//content.setUri(resourceId);
			//noti.getNotificationEvent().setRepresentation(content);
			noti.getNotificationEvent().setRepresentation(new Notification.NotificationEvent.Representation(resourceId));
			break;
		case ALL_ATRRIBUTES:
		default:
//			noti.getNotificationEvent().setRepresentation(dao.createResourceWithDoc(orgRes));
			log.debug(reqRes.toString());
			//noti.getNotificationEvent().setRepresentation(reqRes);
			noti.getNotificationEvent().setRepresentation(new Notification.NotificationEvent.Representation(reqRes));
			break;
		}
		
		Integer notiEvtCat = sub.getNotificationEventCat();
		if(notiEvtCat != null) {
			reqMessage.setEventCategory(notiEvtCat);
			return;
		}
		
		if(sub.isLatestNotify() !=null && sub.isLatestNotify()) {
			reqMessage.setEventCategory(STDEVENT_CATS.LATEST.Value());
		}
		
		
		return;
	}
	
	
//	private boolean isEventNotify(EventNotificationCriteria evt, Resource reqRes, Resource orgRes, OPERATION op) throws Exception {
	private boolean isEventNotify(EventNotificationCriteria evt, Document reqRes, Document orgRes, OPERATION op) throws Exception {

/*
		// creationTime, lastModifiedTime : Resource
		// stateTag: Container, ContentInstance, Delivery, Request
		// expirationTime: All except ContentInstance, CSEBase
		// contentSize: ContentInstance

		// createdBefore, createdAfter, modifiedSince, unmodifiedSince, 
		// stateTagSmaller, stateTagBigger
		// expireBefore, expireAfter;
	    // sizeAbove, sizeBelow;
	    //List<Integer> operationMonitor;
	    //List<Attribute> attribute;
	    //List<Integer> notificationEventType;
		
		// AccessControlPolicy, AccessControlPolicyAnnc, AE, AEAnnc, Container," +
		// "CSEBase, Delivery, EventConfig, ExecInstance, Group, GroupAnnc, LocationPolicy, MgmtCmd, MgmtObj," +
		//	"MgmtObjAnnc, M2MServiceSubscriptionProfile, Node, NodeAnnc, ServiceSubscribedNode, RemoteCSE," +
		//	"RemoteCSEAnnc, Request, Schedule, StatsCollect, StatsConfig"; 
	
*/	
		// createdBefore, createdAfter, modifiedSince, unmodifiedSince check...
//		if ( reqRes instanceof Resource ) { 

			long createTime = TIME_NOT_SET;
			long lastModifiedTime = TIME_NOT_SET;
//			if( reqRes.getCreationTime() != null) {
			if (reqRes.getString(Naming.CREATIONTIME_SN) != null) {
//				createTime = SIMPLE_DATE_FORMAT.parse(reqRes.getCreationTime()).getTime();
				createTime = SIMPLE_DATE_FORMAT.parse(reqRes.getString(Naming.CREATIONTIME_SN)).getTime();
			}
//			if (reqRes.getLastModifiedTime() != null) {
			if (reqRes.getString(Naming.LASTMODIFIEDTIME_SN) != null) {
//				lastModifiedTime = SIMPLE_DATE_FORMAT.parse(reqRes.getLastModifiedTime()).getTime();
				lastModifiedTime = SIMPLE_DATE_FORMAT.parse(reqRes.getString(Naming.LASTMODIFIEDTIME_SN)).getTime();
			}
			
			if(evt.getCreatedBefore() != null) {
				if(createTime == TIME_NOT_SET) return false;

				long beforeTime = SIMPLE_DATE_FORMAT.parse(evt.getCreatedBefore()).getTime();
				if( createTime >= beforeTime) return false;
			}
			
			if(evt.getCreatedAfter() != null) {
				if(createTime == TIME_NOT_SET) return false;
				
				long afterTime = SIMPLE_DATE_FORMAT.parse(evt.getCreatedAfter()).getTime();
				if( createTime <= afterTime ) return false;
			}
			
			if(evt.getModifiedSince() != null) {
				if (lastModifiedTime == TIME_NOT_SET) return false;
				
				long modifiedSince = SIMPLE_DATE_FORMAT.parse(evt.getModifiedSince()).getTime();
				if ( lastModifiedTime >= modifiedSince ) return false;  //...?
			}
			
			if(evt.getUnmodifiedSince() != null) {
				if (lastModifiedTime == TIME_NOT_SET) return false;
				
				long unmodifiedSince = SIMPLE_DATE_FORMAT.parse(evt.getUnmodifiedSince()).getTime();
				if (lastModifiedTime <= unmodifiedSince) return false; // ....?
			}
			
//		}
		
		
		// stateTagSmaller, stateTagBigger (stateTag) Check..
//		int stateTag = NOT_SET;
//		if ( reqRes instanceof Container) {
//			if(((Container)reqRes).getStateTag() != null) 
//				stateTag = ((Container)reqRes).getStateTag().intValue();
//		} else	if ( reqRes instanceof ContentInstance) {
//			if(((ContentInstance)reqRes).getStateTag() != null) 
//				stateTag = ((ContentInstance)reqRes).getStateTag().intValue();
//		} else if ( reqRes instanceof Delivery ) {
//			if (((Delivery)reqRes).getStateTag() != null)
//				stateTag = ((Delivery)reqRes).getStateTag().intValue();
//		} else if ( reqRes instanceof Request ) {
//			if(((Request)reqRes).getStateTag() != null)
//				stateTag = ((Request)reqRes).getStateTag().intValue();
//		}
		int stateTag = reqRes.getInteger(Naming.STATETAG_SN, NOT_SET);
		
		if(evt.getStateTagSmaller() != null) {
			if (stateTag == NOT_SET) return false;
			
			if (stateTag >= evt.getStateTagSmaller().intValue()) return false;
		}
		if(evt.getStateTagBigger() != null) {
			if (stateTag == NOT_SET) return false;
			
			if (stateTag <= evt.getStateTagBigger()) return false;
		}
		
		//sizeAbove, sizeBelow (contentSize) Check..
//		if( reqRes instanceof ContentInstance ) {
//			int contentSize = NOT_SET;
//			if( ((ContentInstance)reqRes).getContentSize() != null ) {
//				contentSize = ((ContentInstance)reqRes).getContentSize().intValue();
//			}
			
			int contentSize = reqRes.getInteger(Naming.CONTENTSIZE_SN, NOT_SET);
			
			if(evt.getSizeAbove() != null) {
				if (contentSize == NOT_SET) return false;
				
				int sizeAbove = evt.getSizeAbove().intValue();
				if (contentSize <= sizeAbove) return false;
			}
			if(evt.getSizeBelow() != null) {
				if (contentSize == NOT_SET) return false;
				
				int sizeBelow = evt.getSizeBelow().intValue();
				if(contentSize >= sizeBelow) return false;
			}
//		}

		// expireBefore, expireAfter (expirationTime) check..
//		if( (reqRes instanceof ContentInstance == false) && (reqRes instanceof CSEBase == false) ) {
//		}
		long expireTime = TIME_NOT_SET;
//		if (reqRes instanceof RegularResource) {
//			if ( ((RegularResource)reqRes).getExpirationTime() != null ) {
//				expireTime = SIMPLE_DATE_FORMAT.parse(((RegularResource)reqRes).getExpirationTime()).getTime();
//			}
//		} else if (reqRes instanceof AnnouncedResource) {
//			if ( ((AnnouncedResource)reqRes).getExpirationTime() != null ) {
//				expireTime = SIMPLE_DATE_FORMAT.parse(((AnnouncedResource)reqRes).getExpirationTime()).getTime();
//			}
//		} else if (reqRes instanceof AnnounceableSubordinateResource) {
//			if ( ((AnnounceableSubordinateResource)reqRes).getExpirationTime() != null ) {
//				expireTime = SIMPLE_DATE_FORMAT.parse(((AnnounceableSubordinateResource)reqRes).getExpirationTime()).getTime();
//			}
//		} else if (reqRes instanceof AnnouncedSubordinateResource) {
//			if ( ((AnnouncedSubordinateResource)reqRes).getExpirationTime() != null ) {
//				expireTime = SIMPLE_DATE_FORMAT.parse(((AnnouncedSubordinateResource)reqRes).getExpirationTime()).getTime();
//			}
//		}
		
		if (reqRes.getString(Naming.EXPIRATIONTIME_SN) != null) {
			expireTime = SIMPLE_DATE_FORMAT.parse(reqRes.getString(Naming.EXPIRATIONTIME_SN)).getTime();
		}
		
			
		if(evt.getExpireBefore() != null) {
			if (expireTime == TIME_NOT_SET) return false;
			
			long expireBefore = SIMPLE_DATE_FORMAT.parse(evt.getExpireBefore()).getTime();
			if ( expireTime >= expireBefore) return false;
		}
		if(evt.getExpireAfter() != null) {
			if (expireTime == TIME_NOT_SET) return false;
			
			long expireAfter = SIMPLE_DATE_FORMAT.parse(evt.getExpireAfter()).getTime();
			if (expireTime <= expireAfter) return false;
		}
		
		if (evt.getOperationMonitor() != null && evt.getOperationMonitor().size() > 0) {
			if (!evt.getOperationMonitor().contains(op.Value())) return false;
		}
		
		if (evt.getAttribute() != null) {
			for (Attribute attr : evt.getAttribute()) {
				if (!reqRes.get(attr.getName()).equals(attr.getValue())) return false;
				
			}
		}		
		
		return true;
	}
	
	
	private void sendNotifyWithPollingChannel(OneM2mRequest reqMessage, String pcu) {
		
		RequestPrimitive request = new RequestPrimitive();
		request.setOperation(OPERATION.NOTIFY.Value());
		request.setTo(reqMessage.getTo());
		request.setFrom(reqMessage.getFrom());
		request.setRequestIdentifier(reqMessage.getRequestIdentifier());
		PrimitiveContent content = new PrimitiveContent();
		content.addAny(reqMessage.getContentObject());
		request.setPrimitiveContent(content);
		

//		for(String pcu : pollingChannels) {
			try {
				AccessPointManager.getInstance().sendRequest(pcu, request);
			} catch (OneM2MException e) {
				log.debug("AccessPointManager.sendRequest failed. {}", e);
			}
//		}
	}
	
	
	private void sendNotify(OneM2mRequest reqMessage, String to, ResourceDAO dao) throws Exception {
		
		log.debug("Notify message: {}", reqMessage);
		
		reqMessage.setRequestIdentifier(OneM2mUtil.createRequestId());
		
		List<String> targetURI = new ArrayList<String>();
		
		if(to.startsWith("http")) {
			log.debug("AnnouncedAttribute is Address, sendAnnonce to ===> {}", to);
//			if(sendAnnounce(reqMessage, resAnnc, to, op)) {
//				successAnnounceTo.add(to);
//			}
//			continue;
			targetURI.add(to);
		} else {
		
		
			// TODO: requestReachability checking for POA..
			String id = OneM2mUtil.toUriOrResourceId(to);
			List<Object> accessPoints = 
					dao.getAttributesWithUri(OneM2mUtil.isUri(id) ? Naming.URI_KEY : Naming.RESID_KEY, id, Naming.POINTOFACCESS_SN);
			log.debug("to: {}, address size: {}", to, accessPoints.size());
//			if(accessPoints.size() > 0) {
//				if(sendAnnounce(reqMessage, resAnnc, accessPoints, op)) {
//					successAnnounceTo.add(to);
//				}
//			} else {
//				List<String> pollingChannels = dao.getPollingChannelUriWithUri(to);
//				log.debug("PollingChannelUri: {}", pollingChannels);
//				
//				sendAnnounceWithPollingChannel(reqMessage, resAnnc, pollingChannels, op);
//			}
			
			for (Object obj : accessPoints) {
				for(String addr : (List<String>)obj) {
					targetURI.add(addr);
				}
				
			}
		}
		
		for(String uri : targetURI) {
			try {
				URI _uri_ = new URI(uri);
			
				if(_uri_.getPath().length() > 1) {
					reqMessage.setTo(uri.substring(uri.indexOf(_uri_.getPath())));
				} else {
					reqMessage.setTo("/");
				}
				
				if(_uri_.getHost().equals(CfgManager.getInstance().getHostname())) {
					log.debug("pollingChannel={}, pcu={}", uri, _uri_.getPath());
					if(dao.isVaildPollingChannelUri(_uri_.getPath())) {
						sendNotifyWithPollingChannel(reqMessage, _uri_.getPath());
					} else {
						log.warn("PollingChannel is not available..{}", uri);
					}
				} else {
					OneM2mResponse notiRes = this.context.getNseManager().sendRequestMessage(uri, reqMessage);
					if(notiRes != null)
						log.debug("Notify response: {}", notiRes.toString());
				}
			} catch(Exception e) {
				log.error("sendNotify exceptione. ", e);
			}
		}
	}
	
	
//	public void notify(Resource reqRes, Resource orgRes, OPERATION op, AbsManager manager) {
	public void notify(Resource parentRes, Document reqDoc, Resource reqRes, Document orgDoc, Resource orgRes, OPERATION op, AbsManager manager) {
		try {

			ResourceDAO dao = (ResourceDAO)manager.getDAO();
			OneM2mRequest reqMessage = new OneM2mRequest();
			reqMessage.setOperation(OPERATION.NOTIFY);
			Notification noti = new Notification();
			
			if (op == OPERATION.DELETE && orgDoc.getInteger(Naming.RESOURCETYPE_SN) == RESOURCE_TYPE.SUBSCRIPTION.Value()) {

				log.debug("Deleted Subscription: {}", orgDoc);
				noti.setSubscriptionDeletion(true);
//				noti.setSubscriptionReference(orgDoc.getString(Naming.URI_KEY));
				noti.setSubscriptionReference(orgRes.getUri());
				noti.setCreator(CfgManager.getInstance().getCSEBaseCid());
				
				reqMessage.setContentObject(noti);
				reqMessage.setContentType(CfgManager.getInstance().getDefaultContentType());
				reqMessage.setFrom(CfgManager.getInstance().getAbsoluteCSEBaseId());
				
				for (String uri : (List<String>)orgDoc.get(Naming.NOTIFICATIONURI_SN)) {
					try {
						sendNotify(reqMessage, uri, dao);
					} catch (Exception e) {
						log.error("sendNotify exception: ", e);
						continue;
					}
				}

				return;
			}
			
			
			String resourceId = null;
			if (op == OPERATION.CREATE) resourceId = reqDoc.getString(Naming.RESOURCEID_SN); //reqRes.getResourceID();
			else resourceId = orgDoc.getString(Naming.RESOURCEID_SN);	// orgRes.getResourceID();
			
			List<Subscription> subscriptions = dao.getSubscriptionWithParentId(resourceId);
			subscriptions.addAll(dao.getSubscriptionWithParentId(parentRes.getResourceID()));
			log.debug("subscriptions:{}", subscriptions);
			
			
			noti.setNotificationEvent(new Notification.NotificationEvent());
			noti.setCreator(CfgManager.getInstance().getCSEBaseCid());
			
			
			for (Subscription sub : subscriptions) {
				log.debug(sub.toString());
				
				EventNotificationCriteria evt = sub.getEventNotificationCriteria();
				if(evt != null) {
					try {
						
						if(evt.getNotificationEventType() != null) {
							log.debug("NotificationEventType: {}", evt.getNotificationEventType());
							boolean checkEventType = false;
							if(sub.getParentID() == resourceId) {
								if(op == OPERATION.UPDATE && 
										evt.getNotificationEventType().contains(NOTIFICATIONEVENT_TYPE.UPDATE_OF_RESOURCE.Value())) {
									checkEventType = true;
								} else if(op == OPERATION.DELETE &&
										evt.getNotificationEventType().contains(NOTIFICATIONEVENT_TYPE.DELETE_OF_RESOURCE.Value())) {
									checkEventType = true;
								}
							} else {	// Parent's ResourceId
								if(op == OPERATION.CREATE &&
										evt.getNotificationEventType().contains(NOTIFICATIONEVENT_TYPE.CREATE_OF_CHILD_RESOURCE.Value())) {
									checkEventType = true;
								} else if(op == OPERATION.DELETE &&
										evt.getNotificationEventType().contains(NOTIFICATIONEVENT_TYPE.DELETE_OF_CHILD_RESOURCE.Value())) {
									checkEventType = true;
								} 
							}
							
							if(!checkEventType) {
								log.warn("operation: {}, but not contains NotificationEventType", op);
								continue;
							}
						}
						
						if( !isEventNotify(evt, reqDoc, orgDoc, op) ) continue;
					} catch(Exception e) {
						log.error("EventNotificationCriteria check exception: ", e);
						continue;
					}
				}

				try {
					checkNotificationPolicy(reqMessage, sub, noti, reqDoc, reqRes, orgDoc, orgRes, dao, op);
				} catch (Exception e) {
					log.error("checkNotificationPolicy exception: ", e);
					continue;
				}
				// TODO: sub.getSchedule();
				// TODO: sub.getPendingNotification();
				// ToDo: sub.getExpirationCounter();
				
				reqMessage.setContentObject(noti);
				reqMessage.setContentType(CfgManager.getInstance().getDefaultContentType());
				reqMessage.setFrom(CfgManager.getInstance().getAbsoluteCSEBaseId());	// get cse id - from configuration - TBD
				
				for (String uri : sub.getNotificationURI()) {
					try {
						sendNotify(reqMessage, uri, dao);
					} catch (Exception e) {
						log.error("sendNotify exception: ", e);
						continue;
					}
				}
			}
		
			
			
		} catch (Exception e) {
			log.error("notify process exception: ", e);
		}
	}

	@Override
	public OneM2mResponse processRequest(OneM2mRequest reqMessage) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean processAsyncRequest(OneM2mRequest reqMessage) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void asyncResponse(OneM2mResponse resMessage) {
		// TODO Auto-generated method stub
		
	}
	
//	public static void main(String[] args) throws Exception {
//		String url = "http://10.101.101.44:9090/dkdk/dkfkd?dkfd=33&dksld=lll";
//		URI uri = new URI(url);
//		
//		if(uri.getPath().length() > 1) {
//			System.out.println(url.substring(url.indexOf(uri.getPath())));
//		} else {
//			System.out.println("/");
//		}
//	}
}
