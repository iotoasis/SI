package net.herit.iot.onem2m.incse.manager;

import java.util.List;

import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.message.onem2m.OneM2mRequest.OPERATION;
import net.herit.iot.message.onem2m.OneM2mRequest.RESOURCE_TYPE;
import net.herit.iot.onem2m.core.convertor.JSONConvertor;
import net.herit.iot.onem2m.core.convertor.XMLConvertor;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.incse.manager.dao.DAOInterface;
import net.herit.iot.onem2m.resource.ChildResourceRef;
import net.herit.iot.onem2m.resource.Resource;

public interface ManagerInterface {
	//public void initialize(OneM2mContext context);
	public OneM2mResponse create(OneM2mRequest reqMessage) throws OneM2MException;
	public OneM2mResponse retrieve(OneM2mRequest reqMessage) throws OneM2MException;
	public OneM2mResponse update(OneM2mRequest reqMessage) throws OneM2MException;
	public OneM2mResponse delete(OneM2mRequest reqMessage) throws OneM2MException;
	public OneM2mResponse discovery(OneM2mRequest reqMessage) throws OneM2MException;
	public OneM2mResponse notify(OneM2mRequest reqMessage) throws OneM2MException;
	
	public String createResourceID(RESOURCE_TYPE resourceType, Resource res, OneM2mRequest req) throws OneM2MException;
	public Resource getResourceFromDB(String to) throws OneM2MException;
	
	public DAOInterface getDAO();
	public XMLConvertor<?> getXMLConveter() throws OneM2MException;
	public JSONConvertor<?> getJSONConveter() throws OneM2MException;
	public Class<?> getResourceClass();
	public RESOURCE_TYPE getResourceType();
	public String getAllowedParent();
	public void updateResource(Resource resource, OneM2mRequest reqMessage) throws OneM2MException;
	public void announceTo(OneM2mRequest reqMessage, Resource reqRes, Resource orgRes) throws OneM2MException;
	public void notification(Resource parentRes, Resource reqRes, Resource orgRes, OPERATION op) throws OneM2MException;
//	public boolean checkEventNotificationCriteria(EventNotificationCriteria eventCat,	
//			Resource reqRes, Resource orgRes, OPERATION op) throws OneM2MException;
	
	
	//public boolean checkIfCreatePossible(OneM2mRequest reqMessage, Resource parent, String allowedParent) throws OneM2MException;
	// method for Non-block request
	//public Request createRequestResource(OneM2mRequest reqMessage) throws OneM2MException;
	//public void queueRequest(Request req) throws OneM2MException;
	
	public List<ChildResourceRef> extractChildFromRes(Resource res);
	public void validateResource(Resource res, OneM2mRequest req, Resource curRes) throws OneM2MException;
	
	// If operation is CREATE, res should be parent of target
	//public boolean checkAccessControlPolicy(String from, OPERATION op, Resource res, Resource parent) throws OneM2MException;
	public boolean checkAccessControlPolicy(OneM2mRequest req, OPERATION op, Resource res, Resource parent) throws OneM2MException;
	
	
}
