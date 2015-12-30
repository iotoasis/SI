package net.herit.iot.onem2m.incse.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mRequest.OPERATION;
import net.herit.iot.message.onem2m.OneM2mRequest.RESOURCE_TYPE;
import net.herit.iot.message.onem2m.OneM2mResponse.RESPONSE_STATUS;
import net.herit.iot.onem2m.core.convertor.JSONConvertor;
import net.herit.iot.onem2m.core.convertor.XMLConvertor;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.incse.context.OneM2mContext;
import net.herit.iot.onem2m.incse.controller.AnnounceController;
import net.herit.iot.onem2m.incse.manager.dao.DAOInterface;
import net.herit.iot.onem2m.resource.Resource;

public class LocationPolicyManager extends AbsManager {

	static String ALLOWED_PARENT = "";  
	static RESOURCE_TYPE RES_TYPE = RESOURCE_TYPE.LOCAT_POLICY;
	
	private Logger log = LoggerFactory.getLogger(LocationPolicyManager.class);

	public LocationPolicyManager(RESOURCE_TYPE resourceType,
			String allowed_parent, OneM2mContext context) {
		super(resourceType, allowed_parent);
		this.context = context;
	}

	@Override
	public void announceTo(OneM2mRequest reqMessage, Resource resource, Resource orgRes) throws OneM2MException {
		new AnnounceController(context).announce(reqMessage, resource, orgRes, this);
	}
	
	@Override 
	public void notification(Resource parentRes, Resource reqRes, Resource orgRes, OPERATION op) throws OneM2MException {
		//Document reqDoc = reqRes == null? null : Document.parse( ((LocationPolicyDAO)getDAO()).resourceToJson(reqRes));
		//Document orgDoc = orgRes == null? null : Document.parse( ((LocationPolicyDAO)getDAO()).resourceToJson(orgRes));
		//NotificationController.getInstance().notify(reqDoc, orgDoc, op, this);
		throw new OneM2MException(RESPONSE_STATUS.NOT_IMPLEMENTED, "LocationPolicy not implemented!!!");
	}
	
	@Override
	public DAOInterface getDAO() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public XMLConvertor<?> getXMLConveter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONConvertor<?> getJSONConveter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?> getResourceClass() {
		// TODO Auto-generated method stub
		return null;
	}

}
