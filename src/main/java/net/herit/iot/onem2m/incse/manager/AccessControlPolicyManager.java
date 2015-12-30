package net.herit.iot.onem2m.incse.manager;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.message.onem2m.OneM2mRequest.OPERATION;
import net.herit.iot.message.onem2m.OneM2mRequest.RESOURCE_TYPE;
import net.herit.iot.onem2m.core.convertor.ConvertorFactory;
import net.herit.iot.onem2m.core.convertor.JSONConvertor;
import net.herit.iot.onem2m.core.convertor.XMLConvertor;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.incse.context.OneM2mContext;
import net.herit.iot.onem2m.incse.controller.AnnounceController;
import net.herit.iot.onem2m.incse.controller.NotificationController;
import net.herit.iot.onem2m.incse.facility.OneM2mUtil;
import net.herit.iot.onem2m.incse.manager.dao.AccessControlPolicyDAO;
import net.herit.iot.onem2m.incse.manager.dao.DAOInterface;
import net.herit.iot.onem2m.resource.AccessControlPolicy;
import net.herit.iot.onem2m.resource.Resource;

public class AccessControlPolicyManager extends AbsManager {
		
	static String ALLOWED_PARENT = "AE,AEAnnc,remoteCSE,remoteCSEAnnc,CSEBase";  
	static RESOURCE_TYPE RES_TYPE = RESOURCE_TYPE.ACCESS_CTRL_POLICY;
	
	private Logger log = LoggerFactory.getLogger(AccessControlPolicyManager.class);
	
	public AccessControlPolicyManager(OneM2mContext context) {
		super(RES_TYPE, ALLOWED_PARENT);
		this.context = context;
	}
	
	@Override
	public OneM2mResponse create(OneM2mRequest reqMessage) throws OneM2MException {

		return create(reqMessage, this);
		
//		String resourceID = createResourceID(reqMessage.getResourceType());
//		
//		Resource parent = getResourceFromDB(reqMessage.getTo());
//		checkIfCreatePossible(reqMessage, parent, ALLOWED_PARENT);
//		
//		String name = reqMessage.getName();
//		if(name == null) {
//			name = createResourceName(reqMessage.getResourceType(), parent.getResourceID());
//		}
//
//		AccessControlPolicy res = (AccessControlPolicy)getContentResource(reqMessage, AE.class, new XMLConvertor<AccessControlPolicy>(AccessControlPolicy.class) );
//
//		res.validate(OPERATION.CREATE);
//		
//		res.setResourceName(name);
//		res.setUri(parent.getUri() +"/"+ name);
//		res.setResourceType(reqMessage.getResourceType().Value());
//		res.setResourceID(resourceID);
//		res.setParentID(parent.getResourceID());
//
//		AccessControlPolicyDAO dao = new AccessControlPolicyDAO(this.context);
//		dao.create(res);
//		
//		//ae.validate(null);
//
//		OneM2mResponse resMessage = new OneM2mResponse(RESPONSE_STATUS.ACCEPTED, reqMessage);
//		resMessage.setContentObject(res);
//
//		return resMessage;
		
	}

	@Override
	public OneM2mResponse retrieve(OneM2mRequest reqMessage) throws OneM2MException {

		return retrieve(reqMessage, this);
		
//		String to = reqMessage.getTo();			
//		
//		AccessControlPolicyDAO dao = new AccessControlPolicyDAO(this.context);
//		AccessControlPolicy res = (AccessControlPolicy)dao.retrieveByUri(to);
//		if (res == null) {
//			throw new OneM2MException(RESPONSE_STATUS.NOT_FOUND, "Not found");			
//		}
//		if (checkAccessControlPolicy(reqMessage.getFrom(), OPERATION.RETRIEVE, extractAccessControlPolicies(res))) {
//			throw new OneM2MException(RESPONSE_STATUS.ACCESS_DENIED, "Access denied");
//		}
//
//		res.validate(OPERATION.RETRIEVE);
//
//		OneM2mResponse resMessage = new OneM2mResponse(RESPONSE_STATUS.ACCEPTED, reqMessage);
//		resMessage.setContentObject(res);
//
//		return resMessage;
			
	}

	@Override
	public OneM2mResponse update(OneM2mRequest reqMessage) throws OneM2MException {

		return update(reqMessage, this);
		
//		AccessControlPolicy res = (AccessControlPolicy)getContentResource(reqMessage, AccessControlPolicy.class, new XMLConvertor<AccessControlPolicy>(AccessControlPolicy.class) );
//		
//		res.validate(OPERATION.UPDATE);
//		
//		res.setUri(reqMessage.getTo());
//		
//		AccessControlPolicyDAO dao = new AccessControlPolicyDAO(this.context);
//		dao.update(res);
//
//		OneM2mResponse resMessage = new OneM2mResponse(RESPONSE_STATUS.ACCEPTED, reqMessage);
//		resMessage.setContentObject(res);
//
//		return resMessage;
			
	}

	@Override
	public OneM2mResponse delete(OneM2mRequest reqMessage) throws OneM2MException {

		return delete(reqMessage, this);
			
//		String to = reqMessage.getTo();			
//
//		AccessControlPolicyDAO dao = new AccessControlPolicyDAO(this.context);
//		dao.deleteByUri(to);
//
//		OneM2mResponse resMessage = new OneM2mResponse(RESPONSE_STATUS.ACCEPTED);
//		resMessage.setRequestId(reqMessage.getRequestId());
//		resMessage.setFrom(reqMessage.getFrom());
//		resMessage.setEventCategory(reqMessage.getEventCategory());
//		resMessage.setRequest(reqMessage);
//
//		return resMessage;
		
	}

	@Override
	public OneM2mResponse notify(OneM2mRequest reqMessage) throws OneM2MException {
		// TODO Auto-generated method stub

		return new OneM2mResponse();
	}

	@Override
	public void announceTo(OneM2mRequest reqMessage, Resource resource, Resource orgRes) throws OneM2MException {
		new AnnounceController(context).announce(reqMessage, resource, orgRes, this);
	}
	
	@Override 
	public void notification(Resource parentRes, Resource reqRes, Resource orgRes, OPERATION op) throws OneM2MException {
		Document reqDoc = reqRes == null? null : Document.parse( ((AccessControlPolicyDAO)getDAO()).resourceToJson(reqRes));
		Document orgDoc = orgRes == null? null : Document.parse( ((AccessControlPolicyDAO)getDAO()).resourceToJson(orgRes));
		NotificationController.getInstance().notify(parentRes, reqDoc, reqRes, orgDoc, orgRes, op, this);
	}
	
	@Override
	public DAOInterface getDAO() {
		return new AccessControlPolicyDAO(context);
	}

	@Override
	public Class<?> getResourceClass() {
		return AccessControlPolicy.class;
	}

	@Override
	public XMLConvertor<?> getXMLConveter() throws OneM2MException {
		return ConvertorFactory.getXMLConvertor(AccessControlPolicy.class, AccessControlPolicy.SCHEMA_LOCATION);
	}

	@Override
	public JSONConvertor<?> getJSONConveter() throws OneM2MException {
		return ConvertorFactory.getJSONConvertor(AccessControlPolicy.class, AccessControlPolicy.SCHEMA_LOCATION);
	}
	
	@Override
	public boolean checkAccessControlPolicy(OneM2mRequest req, OPERATION op, Resource res, Resource parent) throws OneM2MException {
		
		if (op != OPERATION.CREATE) {

			AccessControlPolicy acp = (AccessControlPolicy)res;			
			return OneM2mUtil.checkAccessControlPolicy(req, op, acp.getSelfPrivileges());
						
		} else {
			
			return OneM2mUtil.checkAccessControlPolicy(req, op, OneM2mUtil.extractAccessControlPolicies(parent, context));
			
		}
	}
}
