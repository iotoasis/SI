package net.herit.iot.onem2m.incse.manager;


import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.herit.iot.message.onem2m.OneM2mResponse.RESPONSE_STATUS;
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
import net.herit.iot.onem2m.incse.facility.QosManager;
import net.herit.iot.onem2m.incse.facility.SeqNumManager;
import net.herit.iot.onem2m.incse.manager.dao.AEDAO;
import net.herit.iot.onem2m.incse.manager.dao.DAOInterface;
import net.herit.iot.onem2m.incse.manager.dao.ResourceDAO;
import net.herit.iot.onem2m.resource.AE;
import net.herit.iot.onem2m.resource.Resource;

public class AEManager extends AbsManager {
	
	static String ALLOWED_PARENT = "remoteCSE,remoteCSEAnnc,CSEBase";  
	static RESOURCE_TYPE RES_TYPE = RESOURCE_TYPE.AE; 
	
	private Logger log = LoggerFactory.getLogger(AEManager.class);

	private static final String TAG = AEManager.class.getName();

	public AEManager(OneM2mContext context) {
		super(RES_TYPE, ALLOWED_PARENT);
		this.context = context;
	}
	
	@Override
	public OneM2mResponse create(OneM2mRequest reqMessage) throws OneM2MException {
		
		QosManager qm = QosManager.getInstance(); 
		if (qm.checkNAddAENumber(1)) {
			try {
				return create(reqMessage, this);
			} catch (OneM2MException e) {

				qm.reduceAENumber(1);
				throw e;
			}
		} else {
			throw new OneM2MException(RESPONSE_STATUS.MAX_NUMBER_OF_MEMBER_EXCEEDED, 
					"AE Quota already fulled!!! ("+qm.getMaxAENumber()+")");
		}
		
		
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
//		AE ae = (AE)getContentResource(reqMessage, AE.class, new XMLConvertor<AE>(AE.class) );
//		
//		ae.validate(OPERATION.CREATE);
//		
//		ae.setResourceName(name);
//		ae.setUri(parent.getUri() +"/"+ name);
//		ae.setResourceType(reqMessage.getResourceType().Value());
//		ae.setResourceID(resourceID);
//		ae.setParentID(parent.getResourceID());
//
//		AEDAO dao = new AEDAO(this.context);
//		dao.create(ae);
//		
//		//ae.validate(null);
//
//		OneM2mResponse resMessage = new OneM2mResponse(RESPONSE_STATUS.ACCEPTED, reqMessage);
//		resMessage.setContentObject(ae);
//
//		return resMessage;
		
	}
	
	@Override
	public String createResourceID(RESOURCE_TYPE type, Resource res, OneM2mRequest req) throws OneM2MException {
		String from = req.getFrom();
		if (from == null || from.length() == 0) {
			//return RESID_PREFIX + "SAE_"+UUID.randomUUID().toString();
			return "SAE_"+SeqNumManager.getInstance().get(RESOURCE_TYPE.AE.Name());
		} else if (from.equals("S")) {
			//return RESID_PREFIX + "SAE_"+UUID.randomUUID().toString();
			return "SAE_"+SeqNumManager.getInstance().get(RESOURCE_TYPE.AE.Name());
		} else if (from.equals("C")) {
			//return RESID_PREFIX + "CAE_"+UUID.randomUUID().toString();
			return "CAE_"+SeqNumManager.getInstance().get(RESOURCE_TYPE.AE.Name());
		} else if (from.startsWith("S") || from.startsWith("C")){
			ResourceDAO dao = this.getCommonDAO();
			if (dao.checkIfResourceExist(from)) {
				throw new OneM2MException(RESPONSE_STATUS.CONFLICT, "Resource already exist:"+from);
			}
			return from;
		} else if (from.length() > 0) {
			String resId = OneM2mUtil.extractResourceId(from);
			return resId;
//			if (resId.startsWith(RESID_PREFIX)) {
//
//				return resId;
//			} else {
//				return RESID_PREFIX + resId;
//			}
		} else {
			throw new OneM2MException(RESPONSE_STATUS.BAD_REQUEST, "Invalid AE-ID(From):"+from);
		}
	}	

	@Override
	public OneM2mResponse retrieve(OneM2mRequest reqMessage) throws OneM2MException {
		
		return retrieve(reqMessage, this);
		
//		String to = reqMessage.getTo();			
//		
//		AEDAO dao = new AEDAO(this.context);
//		AE res = (AE)dao.retrieveByUri(to);
//		if (res == null) {
//			throw new OneM2MException(RESPONSE_STATUS.NOT_FOUND, "Not found");			
//		}
//		if (!checkAccessControlPolicy(reqMessage.getFrom(), OPERATION.RETRIEVE, extractAccessControlPolicies(res))) {
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

		//return update(reqMessage, this);
		// added in 2017-10-25
		update(reqMessage, this);
		String to = reqMessage.getTo();
		AEDAO dao = (AEDAO)this.getDAO();
		AE res = (AE)dao.retrieve(to, null);
		
		if (res == null) {
			throw new OneM2MException(RESPONSE_STATUS.NOT_FOUND, "Not found");	
		}
		
		OneM2mResponse resMessage = new OneM2mResponse(RESPONSE_STATUS.CHANGED, reqMessage);
		setResponseContentObjectForRetrieve(resMessage, reqMessage.getResultContentEnum(), res, this);
		
		return resMessage;
			
	}

	@Override
	public OneM2mResponse delete(OneM2mRequest reqMessage) throws OneM2MException {

		OneM2mResponse res = delete(reqMessage, this);
		QosManager.getInstance().reduceAENumber(1);
		return res;
			
//		String to = reqMessage.getTo();			
//
//		AEDAO dao = new AEDAO(this.context);
//
//		AE curRes = (AE)dao.retrieveByUri(reqMessage.getTo());
//		if (curRes == null) {
//			throw new OneM2MException(RESPONSE_STATUS.NOT_FOUND, "Not found");
//		}
//		if (!checkAccessControlPolicy(reqMessage.getFrom(), OPERATION.DELETE, extractAccessControlPolicies(curRes))) {
//			throw new OneM2MException(RESPONSE_STATUS.ACCESS_DENIED, "Access denied");
//		}		
//		
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
	public void updateResource(Resource resource, OneM2mRequest req) throws OneM2MException {
		
		AE ae = (AE)resource;
		String resId = ae.getResourceID();
		if (resId.startsWith("S")) {	// 
			ae.setAEID("/"+resId);
		} else {
			ae.setAEID(ae.getUri());
		}
		
	}
	
	@Override
	public void announceTo(OneM2mRequest reqMessage, Resource reqRes, Resource orgRes) throws OneM2MException {
		new AnnounceController(context).announce(reqMessage, reqRes, orgRes, this);
	}

	@Override 
	public void notification(Resource parentRes, Resource reqRes, Resource orgRes, OPERATION op) throws OneM2MException {
		Document reqDoc = reqRes == null? null : Document.parse( ((AEDAO)getDAO()).resourceToJson(reqRes));
		Document orgDoc = orgRes == null? null : Document.parse( ((AEDAO)getDAO()).resourceToJson(orgRes));
		NotificationController.getInstance().notify(parentRes, reqDoc, reqRes, orgDoc, orgRes, op, this);
	}
	
	
	@Override
	public DAOInterface getDAO() {
		return new AEDAO(context);
	}

	@Override
	public Class<?> getResourceClass() {
		return AE.class;
	}

	@Override
	public XMLConvertor<?> getXMLConveter() throws OneM2MException {
		return ConvertorFactory.getXMLConvertor(AE.class, AE.SCHEMA_LOCATION);
	}

	@Override
	public JSONConvertor<?> getJSONConveter() throws OneM2MException {
		return ConvertorFactory.getJSONConvertor(AE.class, AE.SCHEMA_LOCATION);
	}
	@Override
	public void validateResource(Resource res, OneM2mRequest req, Resource curRes) throws OneM2MException {
		log.debug("validateResource called:"+ res.getUri() +","+ req.getOperationEnum().Name());
		
		// AE-ID, App-ID, Credential-ID, CSE-ID verification using Service subscription profile
		// TBD
		
		super.validateResource(res, req, curRes);
	}
}
