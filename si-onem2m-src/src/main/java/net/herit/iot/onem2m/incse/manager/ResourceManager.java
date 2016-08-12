package net.herit.iot.onem2m.incse.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.message.onem2m.OneM2mRequest.OPERATION;
import net.herit.iot.message.onem2m.OneM2mRequest.RESOURCE_TYPE;
import net.herit.iot.message.onem2m.OneM2mResponse.RESPONSE_STATUS;
import net.herit.iot.onem2m.core.convertor.XMLConvertor;
import net.herit.iot.onem2m.core.util.LogManager;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.incse.context.OneM2mContext;
import net.herit.iot.onem2m.incse.controller.ForwardingController;
import net.herit.iot.onem2m.incse.manager.dao.ResourceDAO;
import net.herit.iot.onem2m.resource.Resource;

public class ResourceManager {
	
	private Logger log = LoggerFactory.getLogger(Resource.class);
	
	private static final String TAG = ResourceManager.class.getName();
	
	private OneM2mContext context;

	private ResourceDAO dao = null;
	
	private ForwardingController forward;
	
	
	public ResourceManager(OneM2mContext context) {
		this.context = context;
		this.dao = new ResourceDAO(context);
	}
	
//	private OneM2mResponse createResponse(OneM2mRequest reqMessage, RESPONSE_STATUS status) {
//		OneM2mResponse resMessage = new OneM2mResponse(status);
//		resMessage.setRequestId(reqMessage.getRequestId());
//		resMessage.setFrom(reqMessage.getFrom());
//		resMessage.setEventCategory(reqMessage.getEventCategory());
//		//..?
//		
//		return resMessage;
//	}
	
	public OneM2mResponse processEx(OneM2mRequest reqMessage, boolean useVirtualManager) throws OneM2MException {

		OneM2mResponse resMessage = null;
		
		reqMessage.validate();
		
		if (useVirtualManager) {
			VirtualManagerInterface vManager = VirtualManager.getInstance().getManager(reqMessage.getTo());
			if (vManager != null) {
				resMessage = vManager.process(reqMessage, context);
				if (resMessage != null)	return resMessage;

				if (vManager.getProcessResult() == VirtualManager.PROCESS_RESULT.COMPLETED) {
					return null;
				}
			}
			//if (vManager != null &&
			//	vManager.process(reqMessage, context) == VirtualManager.PROCESS_RESULT.COMPLETED) {
			//		return null;
			//}
		}

		ManagerInterface manager = ManagerFactory.create(reqMessage, context);
		
		OneM2mRequest.OPERATION op = reqMessage.getOperationEnum();

					
		switch (op) {
		case CREATE:
			resMessage = manager.create(reqMessage);
			break;
		case RETRIEVE:
			resMessage = manager.retrieve(reqMessage);
			break;
		case UPDATE:
			resMessage = manager.update(reqMessage);
			break;
		case DELETE:
			resMessage = manager.delete(reqMessage);
			break;
		case DISCOVERY:
			resMessage = manager.discovery(reqMessage);
			break;
		case NOTIFY: 
			resMessage = manager.notify(reqMessage);
			//new OneM2mResponse(RESPONSE_STATUS.NOT_IMPLEMENTED, reqMessage);
			break;
		default:
			resMessage = new OneM2mResponse(RESPONSE_STATUS.NOT_IMPLEMENTED, reqMessage); 		
		}

		if (resMessage == null) {
			resMessage = new OneM2mResponse(RESPONSE_STATUS.INTERNAL_SERVER_ERROR, reqMessage);
		} 
		
		log.debug("process", resMessage.toString());
		
		return resMessage;
		
	}
	
	public void process(OneM2mRequest reqMessage) {

		OneM2mResponse resMessage = null;
		
		try {

			resMessage = processEx(reqMessage, true);
			
			if (resMessage != null)	{
				log.debug("ResourceManager.process", resMessage.toString());
				
				context.getNseManager().sendResponseMessage(resMessage);
			}
			
		} catch (OneM2MException e) {
			log.error("Handled exception", e);

			reqMessage.setContent(e.getMessage().getBytes());
			
			resMessage = new OneM2mResponse(e.getResponseStatusCode(), reqMessage);
			if(e.getMessage() != null) {
				resMessage.setContent(e.getMessage().getBytes());
			}
			context.getNseManager().sendResponseMessage(resMessage);
		} catch (Exception e) {

			log.debug("Handled exception", e);

			resMessage = new OneM2mResponse(RESPONSE_STATUS.INTERNAL_SERVER_ERROR, reqMessage);
			if(e.getMessage() != null)
				resMessage.setContent(e.getMessage().getBytes());
			
			context.getNseManager().sendResponseMessage(resMessage);
		}
		
	
		/*
		MongoCollection<Document> collection = context.getDatabaseManager().getCollection("test");
		Document doc = new Document("name", "iitp")
        .append("type", "database")
        .append("count", 1)
        .append("info", new Document("x", 203).append("y", 102));
		
		collection.insertOne(doc);
		
		System.out.println("collection count: " + collection.count());
		Document doc2 = collection.find().first();
		System.out.println(doc2);
		
		
		context.getNseManager().sendResponseMessage(createResponse(reqMessage, RESPONSE_STATUS.ACCEPTED));
		*/
		
	}
//	static protected boolean checkIfParentAllowed(Resource parent, String allowed) {
//		return allowed.indexOf(RESOURCE_TYPE.get(parent.getResourceType()).Name()) >= 0;
//	}
	//void checkAccessControlPolicy(reqMessage.getFrom(), OPERATION.CREATE, getAccessControlPolicies(parent)) {
	// create:1, retrieve:2, update:4, delete:8, discovery:16, n:32
//	protected boolean checkAccessControlPolicy(String from, OPERATION op, List<AccessControlPolicy> acps) {
//		
//		if (acps == null || acps.size() == 0) {
//			return true;
//		}
//		
//		Iterator<AccessControlPolicy> it = acps.iterator();
//		
//		while (it.hasNext()) {
//			AccessControlPolicy acp = it.next();
//			Iterator<AccessControlRule> acrIt = acp.getPrivileges().getAccessControlRule().iterator();
//			while (acrIt.hasNext()) {
//				boolean oriOk = false;
//				boolean oprOk = false;
//				AccessControlRule acr = acrIt.next();
//				
//				int opr = acr.getAccessControlOperations();
//				switch (op) {
//				case CREATE:
//					oprOk = ((opr & 1) == 1);
//					break;
//				case RETRIEVE:
//					oprOk = ((opr & 2) == 2);
//					break;
//				case UPDATE:
//					oprOk = ((opr & 4) == 4);
//					break;
//				case DELETE:
//					oprOk = ((opr & 8) == 8);
//					break;
//				case DISCOVERY:
//					oprOk = ((opr & 16) == 16);
//					break;
//				case NOTIFY:
//					oprOk = ((opr & 32) == 32);
//					break;
//				}
//				
//				Iterator<String> idIt = acr.getAccessControlOriginators().iterator();
//				while (idIt.hasNext()) {
//					if (from.equalsIgnoreCase(idIt.next())) {
//						oriOk = true;
//						break;
//					}
//				}
//
//				if (oriOk && oprOk) {
//					return true;
//				}
//			}
//		}
//		
//		return false;
//		
//	}
	
//	protected boolean checkIfCreatePossible(OneM2mRequest reqMessage, Resource parent, String allowedParent) throws OneM2MException {
//
//		if (parent == null) {
//			throw new OneM2MException(RESPONSE_STATUS.NOT_FOUND, "Not found");
//		}
//		if (!checkIfParentAllowed(parent, allowedParent)) {
//			throw new OneM2MException(RESPONSE_STATUS.BAD_REQUEST, "Now allowed parent:"+RESOURCE_TYPE.get(parent.getResourceType()).Name());
//		}
//		if (!checkAccessControlPolicy(reqMessage.getFrom(), OPERATION.CREATE, extractAccessControlPolicies(parent))) {
//			throw new OneM2MException(RESPONSE_STATUS.ACCESS_DENIED, "Access denied");
//		}
//		return true;
//		
//	}
//
//	protected List<AccessControlPolicy> extractAccessControlPolicies(Resource res) throws OneM2MException {
//		List<String> acpIds = null;
//		if (res instanceof RegularResource) {
//			RegularResource res2 = (RegularResource)res;
//			acpIds = res2.getAccessControlPolicyIDs();
//		} else if (res instanceof AnnouncedResource) {
//			AnnouncedResource res2 = (AnnouncedResource)res;
//			acpIds = res2.getAccessControlPolicyIDs();
//		} else if (res instanceof CSEBase) {
//			CSEBase res2 = (CSEBase)res;
//			acpIds = res2.getAccessControlPolicyIDs();
//		}
//
//		ArrayList<AccessControlPolicy> acps = new ArrayList<AccessControlPolicy>();
//		if (acpIds != null) {
//			Iterator<String> it = acpIds.iterator();
//			while (it.hasNext()) {
//				AccessControlPolicyDAO dao = new AccessControlPolicyDAO(this.context);
//				AccessControlPolicy acp = (AccessControlPolicy) dao.retrieveByResId(it.next(), null);
//				if (acp != null) {
//					acps.add(acp);
//				}
//			}
//		}
//		return acps;
//	}
	
//	public String createResourceName(RESOURCE_TYPE type, String parentId) {
//
//		// DAO로 이동 예정 TBD: 이름을 이용해서 num추출 로직 추가해야 함
//		DatabaseManager dbMngr = this.context.getDatabaseManager();
//		MongoCollection<Document> entityCol = dbMngr.getCollection("resEntity");
//		
//		BasicDBObject query = new BasicDBObject("parentID", parentId)
//			.append("resourceType", type.Value());
//		
//		long cnt = entityCol.count(query);
//		return type+"_"+Long.toString(cnt);
//		
//	}

	public Resource getContentResource(OneM2mRequest req, Class<?> cls, XMLConvertor<?> cvt) throws OneM2MException {
		
		Resource res;
		try {
			String xml = new String(req.getContent(), "UTF-8");
			res = (Resource)cvt.unmarshal(xml);
			log.debug("resourceName: {}", res.getResourceName());
			//System.out.println("resourceName: " + res.getResourceName());
			return res;
		} catch (Exception e) {
			log.debug("Handled exception", e);
			
			throw new OneM2MException(OneM2mResponse.RESPONSE_STATUS.INVALID_ARGUMENTS, "Invalid format");
		}

	}
	
	protected RESOURCE_TYPE getResourceTypeWithTo(String to) throws OneM2MException {
		int resType = dao.getResourceType(to);
		return RESOURCE_TYPE.get(resType);
	}

}
