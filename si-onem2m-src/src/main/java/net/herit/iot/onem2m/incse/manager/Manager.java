package net.herit.iot.onem2m.incse.manager;

import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.message.onem2m.OneM2mRequest.OPERATION;
import net.herit.iot.message.onem2m.OneM2mRequest.RESOURCE_TYPE;
import net.herit.iot.message.onem2m.OneM2mRequest.RESULT_CONT;
import net.herit.iot.message.onem2m.OneM2mResponse.RESPONSE_STATUS;
import net.herit.iot.onem2m.core.convertor.JSONConvertor;
import net.herit.iot.onem2m.core.convertor.XMLConvertor;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.incse.context.OneM2mContext;
import net.herit.iot.onem2m.incse.facility.OneM2mUtil;
import net.herit.iot.onem2m.incse.facility.SeqNumManager;
import net.herit.iot.onem2m.incse.manager.dao.ContainerDAO;
import net.herit.iot.onem2m.incse.manager.dao.DAOInterface;
import net.herit.iot.onem2m.incse.manager.dao.DeliveryDAO;
import net.herit.iot.onem2m.incse.manager.dao.RequestDAO;
import net.herit.iot.onem2m.incse.manager.dao.ResourceDAO;
import net.herit.iot.onem2m.resource.ChildResourceRef;
import net.herit.iot.onem2m.resource.Container;
import net.herit.iot.onem2m.resource.ContentInstance;
import net.herit.iot.onem2m.resource.Delivery;
import net.herit.iot.onem2m.resource.Request;
import net.herit.iot.onem2m.resource.Resource;
import net.herit.iot.onem2m.resource.UriContent;
import net.herit.iot.onem2m.resource.UriListContent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;

public class Manager {

	protected OneM2mContext context = null;
	
	protected String ALLOWED_PARENT = "";
	protected RESOURCE_TYPE RES_TYPE = RESOURCE_TYPE.NONE;
	
	private static Logger log = LoggerFactory.getLogger(Manager.class);
	
	
	public void initialize(OneM2mContext context) {
		this.context = context;
	}
	
	public Resource getResourceFromDB(String id) throws OneM2MException {
		
		ResourceDAO dao = new ResourceDAO(context);
		
		log.debug("resourceId: {}", id);
		Resource res = dao.getResource(id);
		
		return res;
	}

//	public static Resource getContentResource(AbsMessage message, ManagerInterface manager) throws OneM2MException {
	public static Resource getContentResource(OneM2mRequest message, ManagerInterface manager) throws OneM2MException {
		
		Object content = message.getContentObject();
		if (content != null && content instanceof Resource) {
			return (Resource)content;
		}
		
		try {
			Resource res;
			String str = new String(message.getContent(), "UTF-8");
			XMLConvertor<?> xmlCvt;
			JSONConvertor<?> jsonCvt;
			switch (message.getContentType()) {
				case RES_XML:
					xmlCvt = manager.getXMLConveter();
					res = (Resource)xmlCvt.unmarshal(str);
					System.out.println("resourceName: " + res.getResourceName());
					return res;
				case RES_JSON:
					jsonCvt = manager.getJSONConveter();
					res = (Resource)jsonCvt.unmarshal(str);
					System.out.println("resourceName: " + res.getResourceName());
					return res;
				case XML:	// cannot sure if content is resource or not, so just return null in case exception
					try {
						xmlCvt = manager.getXMLConveter();
						res = (Resource)xmlCvt.unmarshal(str);
						System.out.println("resourceName: " + res.getResourceName());
						return res;
					} catch(Exception e) {
						log.debug("Handled exception", e);
						return null;
					}
				case JSON:	// cannot sure if content is resource or not, so just return null in case exception
					try {
						jsonCvt = manager.getJSONConveter();
						res = (Resource)jsonCvt.unmarshal(str);
						log.debug("resourceName: " + res.getResourceName());
						return res;
					} catch(Exception e) {
						log.debug("Handled exception", e);
						return null;
					}
					
				default:
					return null;
			}	
		} catch (OneM2MException e) {
			throw e;
		} catch (Exception e) {
			log.debug("Handled exception", e);
			
			throw new OneM2MException(OneM2mResponse.RESPONSE_STATUS.INVALID_ARGUMENTS, "Invalid format");
		}

	}

	public static Resource getContentResource(OneM2mRequest req, Class<?> cls, XMLConvertor<?> cvt) throws OneM2MException {
		
		Resource res;
		try {
			String xml = new String(req.getContent(), "UTF-8");
			res = (Resource)cvt.unmarshal(xml);
			//System.out.println("resourceName: " + res.getResourceName());
			return res;
		} catch (Exception e) {
			log.debug("Handled exception", e);
			
			throw new OneM2MException(OneM2mResponse.RESPONSE_STATUS.INVALID_ARGUMENTS, "Invalid format");
		}

	}


	public OneM2mResponse discovery(OneM2mRequest reqMessage) throws OneM2MException {
		
		String to = reqMessage.getTo();			
		
		ResourceDAO dao = new ResourceDAO(this.context);
		
		List<Resource> resources = dao.discovery(to, reqMessage.getFilterCriteria());

		OneM2mResponse resMessage = new OneM2mResponse(RESPONSE_STATUS.OK, reqMessage);
		
		UriListContent content = new UriListContent();
		List<String> uriList = content.getUriList();
		Iterator<Resource> i = resources.iterator();
		
		switch (reqMessage.getDiscoveryResultTypeEnum()) {
		case NON_HIERARCHY:
		case SCEID_N_RESCID:
			content.addUriList(null); // 검색된 리소스가 없을 경우 빈 목록을 만들기 위해서
			while (i.hasNext()) {
				content.addUriList(i.next().getResourceID());
				//uriList.add(i.next().getResourceID());
			}	
			break;
		case HIERARCHY:
		default:
			content.addUriList(null); // 검색된 리소스가 없을 경우 빈 목록을 만들기 위해서
			while (i.hasNext()) {
				content.addUriList(i.next().getUri());
				//uriList.add(i.next().getUri());
			}
			break;	
		}
		
		resMessage.setContentObject(content);

		return resMessage;
	}
	
	/*
	public Resource getContentResource(OneM2mRequest req) throws OneM2MException {
		
		// todo: primitiveContent 값을 resouceVO로 변환하는 기능 
		
		RESOURCE_TYPE type = req.getResourceType();
		if (type == RESOURCE_TYPE.AE) {
			XMLConvertor<AE> XC = new XMLConvertor<AE>();
			AE res;
			try {
				res = (AE)XC.unmarshal(RESOURCE_TYPE.AE, req.getContent().toString());
				System.out.println("resourceName: " + res.getResourceName());
				return res;
			} catch (Exception e) {
				log.debug("Handled exception", e);
				
				throw new OneM2MException(OneM2mResponse.RESPONSE_STATUS.INVALID_ARGUMENTS, "Invalid format");
			}

			//AE ae = new AE();
			//ae.setAE_ID("//onem2m.herit.net/admin");
			//ae.setApp_ID("C_AuthID_Admin");
		} else if (type == RESOURCE_TYPE.CONTAINER) {
			XMLConvertor<Container> XC = new XMLConvertor<Container>();
			Container res;
			try {
				res = (Container)XC.unmarshal(RESOURCE_TYPE.CONTAINER, req.getContent().toString());
				System.out.println("resourceName: " + res.getResourceName());
				return res;
			} catch (Exception e) {
				log.debug("Handled exception", e);
				
				throw new OneM2MException(OneM2mResponse.RESPONSE_STATUS.INVALID_ARGUMENTS, "Invalid format");
			}
			//Container res = new Container();
			
		} else if (type == RESOURCE_TYPE.CONTENT_INST) {
			XMLConvertor<ContentInstance> XC = new XMLConvertor<ContentInstance>();
			Container res;
			try {
				res = (Container)XC.unmarshal(RESOURCE_TYPE.CONTENT_INST, req.getContent().toString());
				System.out.println("resourceName: " + res.getResourceName());
				return res;
			} catch (Exception e) {
				log.debug("Handled exception", e);
				
				throw new OneM2MException(OneM2mResponse.RESPONSE_STATUS.INVALID_ARGUMENTS, "Invalid format");
			}
			
			
		}
		
		//return new CSEBase("heritcse", "heritcse");		
		return null;
	}*/
	
	
	public String createResourceID(RESOURCE_TYPE type, Resource res, OneM2mRequest req) throws OneM2MException {

		return type+"_"+SeqNumManager.getInstance().get(type.Name());

	}	
	
	static public String createResourceID(RESOURCE_TYPE type) {
		
		return type+"_"+SeqNumManager.getInstance().get(type.Name());
		
	}
	
//	public String createResourceName(RESOURCE_TYPE type, Resource parent, String name) throws OneM2MException {
//
//		ResourceDAO dao = new ResourceDAO(context);
//		
//		// Case1. Client provides resourceName
//		if (name != null && name.length() > 0) {
//
//			String uri = parent.getUri()+"/"+name;
//			if (dao.checkIfResourceExist(uri)) {
//				throw new OneM2MException(RESPONSE_STATUS.CONFLICT, "Name conflict:"+name);
//			}
//			return name;
//		}
//
//		// Case2. ResourceName of Polling channel should be <POLLING_CHANN.Name()>
//		//        Only one polling channel can be exist under each parent.
//		if(type.equals(RESOURCE_TYPE.POLLING_CHANN)) {
//			String uri = parent.getUri()+"/"+RESOURCE_TYPE.POLLING_CHANN.Name();
//			if (dao.checkIfResourceExist(uri)) {
//				throw new OneM2MException(RESPONSE_STATUS.CONFLICT, 
//						RESOURCE_TYPE.POLLING_CHANN.Name() + " is already exist. ("+uri+")");
//			}
//			return RESOURCE_TYPE.POLLING_CHANN.Name();
//		}
//		
//		// Case3. ...
//		int id = SeqNumManager.getInstance().get(type.Name());
//				
//		String zero = "";
//		String idx = Integer.toString(id+1);
//		final int numSize = 5;
//		for (int i=idx.length(); i<numSize; i++) zero += "0";
//		return type +"_"+zero+ idx;
//		
//	}
	
//	public String createResourceName(RESOURCE_TYPE type, String parentId) throws OneM2MException {
//
//		ResourceDAO dao = new ResourceDAO(context);
//		
//		int id = SeqNumManager.getInstance().get(type.Name());
//		
//		if(type.equals(RESOURCE_TYPE.POLLING_CHANN) && id != 0 ) {
//			throw new OneM2MException(RESPONSE_STATUS.CONFLICT, 
//									RESOURCE_TYPE.POLLING_CHANN.Name() + " is already exist");
//		}
//		
//		String zero = "";
//		String idx = Integer.toString(id+1);
//		final int numSize = 5;
//		for (int i=idx.length(); i<numSize; i++) zero += "0";
//		return type +"_"+zero+ idx;
//		
//	}
	
	
	/*public DAOInterface getResourceDAO(BigInteger resourceType) {
	
		if (resourceType == OneM2mRequest.RESOURCE_TYPE.AE.BigInteger()) {
			return (DAOInterface)new AEDAO(context); 
		} else if (resourceType == OneM2mRequest.RESOURCE_TYPE.CONTAINER.BigInteger()) {
			return (DAOInterface)new ContainerDAO(context);
		} else if (resourceType == OneM2mRequest.RESOURCE_TYPE.CONTENT_INST.BigInteger()) {
			return (DAOInterface)new ContentInstanceDAO(context);
		} else {
			return null;
		}
		
	}*/
	
	
	public ResourceDAO getCommonDAO() {
		return new ResourceDAO(context);
	}
	

	protected OneM2mResponse create(OneM2mRequest reqMessage, ManagerInterface manager) throws OneM2MException {
		
		DAOInterface dao = manager.getDAO();
		
		Resource res = (Resource)getContentResource(reqMessage, manager);

		String resourceID = manager.createResourceID(reqMessage.getResourceTypeEnum(), res, reqMessage);
						
		Resource parent = manager.getResourceFromDB(reqMessage.getTo());
		if (parent == null) {
			throw new OneM2MException(RESPONSE_STATUS.NOT_FOUND, "Resource("+reqMessage.getTo()+") does not exist!!!");
		}
		if (!OneM2mUtil.checkIfParentAllowed(parent, manager.getAllowedParent())) {
			throw new OneM2MException(RESPONSE_STATUS.BAD_REQUEST, "Now allowed parent:"+RESOURCE_TYPE.get(parent.getResourceType()).Name());
		}
		//if (!manager.checkAccessControlPolicy(reqMessage.getFrom(), OPERATION.CREATE, null, parent)) {
		if (!manager.checkAccessControlPolicy(reqMessage, OPERATION.CREATE, null, parent)) {
			throw new OneM2MException(RESPONSE_STATUS.ACCESS_DENIED, "Access denied");
		}
		
//		String resName = reqMessage.getName() != null ? reqMessage.getName() : resourceID;	// remove '/'
		// name removed. XSD-1.6.0
		String resName = res.getResourceName() != null ? res.getResourceName() : resourceID;
		
//		if (reqMessage.getResourceType() == RESOURCE_TYPE.POLLING_CHANN) {			// 2015.09.14 removed.
//			resName = RESOURCE_TYPE.POLLING_CHANN.Name();
//		}

		String uri = parent.getUri()+"/"+resName;
		ResourceDAO resDao = new ResourceDAO(context);
		if (resDao.checkIfResourceExist(uri)) {
			throw new OneM2MException(RESPONSE_STATUS.CONFLICT, "Resource conflict!!! ("+uri+")");
		}
		
		manager.validateResource(res, reqMessage, parent);
		// res.validate(OPERATION.CREATE);
		
		res.setResourceName(resName);
		res.setUri(uri);
		res.setResourceType(reqMessage.getResourceTypeEnum().Value());
		res.setResourceID(resourceID);
		res.setParentID(parent.getResourceID());

		//// TS-0001-XXX-V1_13_1 - 10.1.1.1	Non-registration related CREATE procedure (ExpirationTime..)
//		if(reqMessage.getResourceTypeEnum() != RESOURCE_TYPE.AE &&
//		   reqMessage.getResourceTypeEnum() != RESOURCE_TYPE.REMOTE_CSE) {
//			//RegularResource, AnnouncedSubordinateResource, AnnouncedResource, AnnounceableSubordinateResource
//			if(res instanceof RegularResource) { 
//				if(((RegularResource) res).getExpirationTime() == null) {
//					((RegularResource) res).setExpirationTime(CfgManager.getInstance().getDefaultExpirationTime());
//				}
//			} else if(res instanceof AnnouncedSubordinateResource) { 
//				if(((AnnouncedSubordinateResource) res).getExpirationTime() == null) {
//					((AnnouncedSubordinateResource) res).setExpirationTime(CfgManager.getInstance().getDefaultExpirationTime());
//				}
//			} else if(res instanceof AnnouncedResource) { 
//				if(((AnnouncedResource) res).getExpirationTime() == null) {
//					((AnnouncedResource) res).setExpirationTime(CfgManager.getInstance().getDefaultExpirationTime());
//				}
//			} else	if(res instanceof AnnounceableSubordinateResource) { 
//				if(((AnnounceableSubordinateResource) res).getExpirationTime() == null) {
//					((AnnounceableSubordinateResource) res).setExpirationTime(CfgManager.getInstance().getDefaultExpirationTime());
//				}
//			}
//		}
		// END - Non-registration related CREATE procedure
		
		//// ------- stateTag setting.. TS-0001-V1_13_1 : <9.6.1.3.2>
		//// Container, Delivery, Request resource는 DAO에서 처리
		updateParentStateTagValue(parent);
		
		if (res instanceof ContentInstance) {
			((ContentInstance) res).setStateTag(((Container)parent).getStateTag());
		}
		//------- End stateTag setting.
		
		
		// 각각 ResourceManager에서 resource 의 값을 업데이트해야 할 경우 사용.
		manager.updateResource(res, reqMessage);
		
		dao.create(res);

		manager.announceTo(reqMessage, res, null);
		
		OneM2mResponse resMessage = new OneM2mResponse(RESPONSE_STATUS.CREATED, reqMessage);
		setResponseContentObjectForCreate(resMessage, reqMessage.getResultContentEnum(), res, manager);
		
		manager.notification(parent, res, null, OPERATION.CREATE);
		
		return resMessage;
		
	}

	private void updateParentStateTagValue(Resource parent) throws OneM2MException {
		//// ------- stateTag setting.. TS-0001-V1_13_1 : <9.6.1.3.2>
		//// Container, Delivery, Request resource는 DAO에서 Update 시 StateTag값을 1 증가 시킴
		if(parent instanceof Container) {
			new ContainerDAO(context).update(parent);
		} else if (parent instanceof Delivery) {
			new DeliveryDAO(context).update(parent);
		} else if (parent instanceof Request) {
			new RequestDAO(context).update(parent);
		}
		//------- End stateTag setting.
	}
	
	
	protected OneM2mResponse createAnnc(OneM2mRequest reqMessage, ManagerInterface manager) throws OneM2MException {
		
		DAOInterface dao = manager.getDAO();
		
		Resource parent = manager.getResourceFromDB(reqMessage.getTo());
		if (parent == null) {
			throw new OneM2MException(RESPONSE_STATUS.NOT_FOUND, "Resource("+reqMessage.getTo()+") does not exist!!!");
		}
		if (!OneM2mUtil.checkIfParentAllowed(parent, manager.getAllowedParent())) {
			throw new OneM2MException(RESPONSE_STATUS.BAD_REQUEST, "Now allowed parent:"+RESOURCE_TYPE.get(parent.getResourceType()).Name());
		}
		if (!OneM2mUtil.checkAccessControlPolicy(reqMessage, OPERATION.CREATE, OneM2mUtil.extractAccessControlPolicies(parent, context))) {
			throw new OneM2MException(RESPONSE_STATUS.ACCESS_DENIED, "Access denied");
		}
		
		Resource res = (Resource)getContentResource(reqMessage, manager);

		String resourceID = manager.createResourceID(reqMessage.getResourceTypeEnum(), res, reqMessage);
		
		//String name = res.getResourceName();
//		String resName = reqMessage.getName() != null ? reqMessage.getName() : resourceID;	// remove '/'
		// name removed. XSD-1.6.0
		String resName = resourceID;	// remove '/'
		
		res.validate(OPERATION.CREATE);
		
		res.setResourceName(resName);
		res.setUri(parent.getUri() +"/"+ resName);
		res.setResourceType(reqMessage.getResourceTypeEnum().Value());
		res.setResourceID(resourceID);
		res.setParentID(parent.getResourceID());		

		// 각각 ResourceManager에서 resource 의 값을 업데이트해야 할 경우 사용.
		manager.updateResource(res, reqMessage);
		
		dao.create(res);

		OneM2mResponse resMessage = new OneM2mResponse(RESPONSE_STATUS.CREATED, reqMessage);
		setResponseContentObjectForCreate(resMessage, reqMessage.getResultContentEnum(), res, manager);
		
		manager.notification(parent, res, null, OPERATION.CREATE);
		
		return resMessage;
		
	}
	
	protected void setResponseContentObjectForCreate(OneM2mResponse resMessage,
			RESULT_CONT resultContent, Resource res, ManagerInterface manager) {
		switch (resultContent) {
		case NOTHING:
			resMessage.setContentObject(null);
			break;
		case ATTRIBUTE:
			resMessage.setContentObject(res);
			break;
		case HIERARCHY_ADDR:
			UriContent uriContent = new UriContent();
			uriContent.setUri(res.getUri());
			resMessage.setContentObject(uriContent);
			break;
		case HIERARCHY_ADDR_N_ATTR:
			res.setResourceUri(res.getUri());
			resMessage.setContentObject(res);
			break;
		case ATTR_N_CHILD_RES:
			resMessage.setContentObject(res);			
			break;
		case ATTR_N_CHILD_RES_REF:
			resMessage.setContentObject(res);
			break;
		case CHILD_RES_REF:
			break;
		case ORIGINAL_RES:
			break;
		default:
			resMessage.setContentObject(null);
			break;
		}
		
	}
	
	protected OneM2mResponse updateResource(Resource res, OneM2mRequest reqMessage, ManagerInterface manager) throws OneM2MException {
		
		DAOInterface dao = manager.getDAO();
		
		Resource curRes = dao.retrieve(reqMessage.getTo(), null);
		if (curRes == null) {
			throw new OneM2MException(RESPONSE_STATUS.NOT_FOUND, "Not found");
		}

		Resource parent = manager.getResourceFromDB(OneM2mUtil.getParentUri(curRes.getUri()));
		
		//if (!OneM2mUtil.checkAccessControlPolicy(reqMessage.getFrom(), OPERATION.UPDATE, OneM2mUtil.extractAccessControlPolicies(curRes,context))) {
		//if (!manager.checkAccessControlPolicy(reqMessage.getFrom(), OPERATION.UPDATE, curRes, parent)) {
		if (!manager.checkAccessControlPolicy(reqMessage, OPERATION.UPDATE, curRes, parent)) {
			throw new OneM2MException(RESPONSE_STATUS.ACCESS_DENIED, "Access denied");
		}
		
		res.validate(OPERATION.UPDATE);
		
		// 개별 리소스별 구현
		manager.validateResource(res, reqMessage, curRes);
		
		res.setUri(reqMessage.getTo());
		res.setResourceType(manager.getResourceType().Value());
		
		//// ------- stateTag setting.. TS-0001-V1_13_1 : <9.6.1.3.2>
		//// Container, Delivery, Request resource는 stateTag값을 업데이트 해야 함. 1증가는 DAO에서...
		if(res instanceof Container) {
			((Container) res).setStateTag(((Container) curRes).getStateTag());
		} else if (res instanceof Delivery) {
			((Delivery) res).setStateTag(((Delivery) curRes).getStateTag());
		} else if (res instanceof Request) {
			((Request) res).setStateTag(((Request) curRes).getStateTag());
		}
		//------- End stateTag setting.
		
		
		dao.update(res);

		OneM2mResponse resMessage = new OneM2mResponse(RESPONSE_STATUS.CHANGED, reqMessage);

		log.debug("ResutlContentType: {}", reqMessage.getResultContentEnum());
		switch (reqMessage.getResultContentEnum()) {
		case NOTHING:
			resMessage.setContentObject(null);
			break;
		case ATTRIBUTE:
			resMessage.setContentObject(res);
			break;
		case HIERARCHY_ADDR:
			UriContent uriContent = new UriContent();
			uriContent.setUri(res.getUri());
			resMessage.setContentObject(uriContent);
			break;
		case HIERARCHY_ADDR_N_ATTR:
			break;
		case ATTR_N_CHILD_RES:
			resMessage.setContentObject(res);			
			break;
		case ATTR_N_CHILD_RES_REF:
			resMessage.setContentObject(res);
			break;
		case CHILD_RES_REF:
			break;
		case ORIGINAL_RES:
			break;
		default:
			break; 
		}

//		if (res instanceof AE) {
//			log.debug(((AE)res).toString());
//		}
		
		res.setParentID(curRes.getParentID());
		manager.announceTo(reqMessage, res, curRes);
		
		//manager.notification(parent, res, dao.retrieve(reqMessage.getTo(), null), OPERATION.UPDATE);
		
//		if (res instanceof AE) {
//			log.debug(((AE)res).toString());
//		}
		
		return resMessage;
		
	}

	protected OneM2mResponse update(OneM2mRequest reqMessage, ManagerInterface manager) throws OneM2MException {

		Resource res = getContentResource(reqMessage, manager);
		
		return updateResource(res, reqMessage, manager);
		
	}
	
	protected OneM2mResponse delete(OneM2mRequest reqMessage, ManagerInterface manager) throws OneM2MException {
		
		DAOInterface dao = manager.getDAO();

		String to = reqMessage.getTo();		

		Resource curRes = dao.retrieve(reqMessage.getTo(), null);
		if (curRes == null) {
			throw new OneM2MException(RESPONSE_STATUS.NOT_FOUND, "Not found");
		}

		Resource parent = manager.getResourceFromDB(OneM2mUtil.getParentUri(curRes.getUri()));
		
		//if (!OneM2mUtil.checkAccessControlPolicy(reqMessage.getFrom(), OPERATION.DELETE, OneM2mUtil.extractAccessControlPolicies(curRes, context))) {
		//if (!manager.checkAccessControlPolicy(reqMessage.getFrom(), OPERATION.DELETE, curRes, parent)) {
		if (!manager.checkAccessControlPolicy(reqMessage, OPERATION.DELETE, curRes, parent)) {
			throw new OneM2MException(RESPONSE_STATUS.ACCESS_DENIED, "Access denied");
		}		
		
		//// ------- stateTag setting.. TS-0001-V1_13_1 : <9.6.1.3.2>
		updateParentStateTagValue(parent);
		//------- End stateTag setting.
		
		
		dao.delete(to);		

		OneM2mResponse resMessage = new OneM2mResponse(RESPONSE_STATUS.DELETED);
		resMessage.setRequestIdentifier(reqMessage.getRequestIdentifier());
		resMessage.setFrom(reqMessage.getFrom());
		resMessage.setEventCategory(reqMessage.getEventCategory());
		resMessage.setRequest(reqMessage);

		manager.announceTo(reqMessage, null, curRes);
		
		manager.notification(parent, null, curRes, OPERATION.DELETE);
		
		return resMessage;
	}
	
	protected OneM2mResponse retrieve(OneM2mRequest reqMessage, ManagerInterface manager) throws OneM2MException {
		
		Resource res = retrieveResource(reqMessage, manager);
		
		OneM2mResponse resMessage = new OneM2mResponse(RESPONSE_STATUS.OK, reqMessage);
		setResponseContentObjectForRetrieve(resMessage, reqMessage.getResultContentEnum(), res, manager);

		return resMessage;

	}
	
	protected Resource retrieveResource(OneM2mRequest reqMessage, ManagerInterface manager) throws OneM2MException {

		DAOInterface dao = manager.getDAO();

		String to = reqMessage.getTo();			
		
		Resource res = dao.retrieve(to, reqMessage.getResultContentEnum());
		if (res == null) {
			throw new OneM2MException(RESPONSE_STATUS.NOT_FOUND, "Not found");			
		}

		Resource parent = manager.getResourceFromDB(OneM2mUtil.getParentUri(res.getUri()));
		
		if (!manager.checkAccessControlPolicy(reqMessage, OPERATION.RETRIEVE, res, parent)) {
				throw new OneM2MException(RESPONSE_STATUS.ACCESS_DENIED, "Access denied");
		}
		
		res.validate(OPERATION.RETRIEVE);
		
		log.debug("Retrieve {}", res.toString());
		
		return res;
	}
	
	protected OneM2mResponse notify(OneM2mRequest reqMessage, ManagerInterface manager) throws OneM2MException {
		OneM2mResponse resMessage = null;
		
		
		return resMessage;
	}

	
	protected void setResponseContentObjectForRetrieve(OneM2mResponse resMessage,
			RESULT_CONT resultContent, Resource res, ManagerInterface manager) {

		switch (resultContent) {
		case ATTRIBUTE:
		case ATTR_N_CHILD_RES:
		case ATTR_N_CHILD_RES_REF:
		case ORIGINAL_RES:
		case HIERARCHY_ADDR_N_ATTR:	// inavlid for retrieve operation
			resMessage.setContentObject(res);
			break;
		case HIERARCHY_ADDR:
			UriContent uriContent = new UriContent();
			uriContent.setUri(res.getUri());
			resMessage.setContentObject(uriContent);
			break;
		case CHILD_RES_REF:
			UriListContent content = new UriListContent();
			List<String> uriList = content.getUriList();
			
			List<ChildResourceRef> childList = manager.extractChildFromRes(res);
			if (childList != null) {
				Iterator<ChildResourceRef> it = childList.iterator();
				while (it.hasNext()) {
					ChildResourceRef ref = it.next();
					uriList.add(res.getUri() +"/"+ ref.getName());
				}
			}
			resMessage.setContentObject(content);
			break;
		default:
			break;
		}
	}

}
