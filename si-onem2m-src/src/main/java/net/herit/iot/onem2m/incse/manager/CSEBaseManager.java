package net.herit.iot.onem2m.incse.manager;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.message.onem2m.OneM2mRequest.OPERATION;
import net.herit.iot.message.onem2m.OneM2mRequest.RESOURCE_TYPE;
import net.herit.iot.message.onem2m.OneM2mResponse.RESPONSE_STATUS;
import net.herit.iot.message.onem2m.format.Enums.CSE_TYPE;
import net.herit.iot.onem2m.core.convertor.ConvertorFactory;
import net.herit.iot.onem2m.core.convertor.JSONConvertor;
import net.herit.iot.onem2m.core.convertor.XMLConvertor;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.incse.context.OneM2mContext;
import net.herit.iot.onem2m.incse.controller.NotificationController;
import net.herit.iot.onem2m.incse.facility.CfgManager;
import net.herit.iot.onem2m.incse.manager.dao.CSEBaseDAO;
import net.herit.iot.onem2m.incse.manager.dao.DAOInterface;
import net.herit.iot.onem2m.resource.CSEBase;
import net.herit.iot.onem2m.resource.Resource;


public class CSEBaseManager extends AbsManager {
	
	static String ALLOWED_PARENT = "";  
	static RESOURCE_TYPE RES_TYPE = RESOURCE_TYPE.CSE_BASE; 
	
	private Logger log = LoggerFactory.getLogger(CSEBaseManager.class);

	private static final String TAG = CSEBaseManager.class.getName();

	public CSEBaseManager(OneM2mContext context) {
		super(RES_TYPE, ALLOWED_PARENT);
		this.context = context;
	}
	
	@Override
	public OneM2mResponse create(OneM2mRequest reqMessage) throws OneM2MException {
		
		throw new OneM2MException(RESPONSE_STATUS.OPERATION_NOT_ALLOWED, "CSEbase create not allowed!!!");
		
	}

	@Override
	public OneM2mResponse retrieve(OneM2mRequest reqMessage) throws OneM2MException {

		return retrieve(reqMessage, this);

	}

	@Override
	public OneM2mResponse update(OneM2mRequest reqMessage) throws OneM2MException {
		
		throw new OneM2MException(RESPONSE_STATUS.OPERATION_NOT_ALLOWED, "CSEbase update not allowed!!!");
		
	}

	@Override
	public OneM2mResponse delete(OneM2mRequest reqMessage) throws OneM2MException {
		
		throw new OneM2MException(RESPONSE_STATUS.OPERATION_NOT_ALLOWED, "CSEbase delete not allowed!!!");
	
	}


	@Override
	public OneM2mResponse notify(OneM2mRequest reqMessage) throws OneM2MException {
		// TODO Auto-generated method stub

		return new OneM2mResponse();
	}
	
	@Override 
	public void notification(Resource parentRes, Resource reqRes, Resource orgRes, OPERATION op) throws OneM2MException {
		Document reqDoc = reqRes == null? null : Document.parse( ((CSEBaseDAO)getDAO()).resourceToJson(reqRes));
		Document orgDoc = orgRes == null? null : Document.parse( ((CSEBaseDAO)getDAO()).resourceToJson(orgRes));
		NotificationController.getInstance().notify(parentRes, reqDoc, reqRes, orgDoc, orgRes, op, this);
	}

	@Override
	public DAOInterface getDAO() {
		return new CSEBaseDAO(context);
	}

	@Override
	public Class<?> getResourceClass() {
		return CSEBase.class;
	}

	@Override
	public XMLConvertor<?> getXMLConveter() throws OneM2MException {
		return ConvertorFactory.getXMLConvertor(CSEBase.class, CSEBase.SCHEMA_LOCATION);
	}
	
	@Override
	public JSONConvertor<?> getJSONConveter() throws OneM2MException {
		return ConvertorFactory.getJSONConvertor(CSEBase.class, CSEBase.SCHEMA_LOCATION);
	}

	public void createIfNotExist() throws OneM2MException {
		
		CSEBaseDAO dao = new CSEBaseDAO(context);
		CSEBase csebase = (CSEBase) dao.retrieve("/"+CfgManager.getInstance().getCSEBaseName(), null);
		if (csebase == null) {

			csebase = new CSEBase();
			csebase.setCSEID(CfgManager.getInstance().getCSEBaseCid());
			csebase.setCseType(CSE_TYPE.IN_CSE.Value());
			csebase.setResourceID(CfgManager.getInstance().getCSEBaseRid());
			csebase.setResourceName(CfgManager.getInstance().getCSEBaseName());
			csebase.setResourceType(RESOURCE_TYPE.CSE_BASE.Value());
			csebase.setUri(CfgManager.getInstance().getCSEBaseUri());
			csebase.addSupportedResourceType(RESOURCE_TYPE.ACCESS_CTRL_POLICY.Value());
			csebase.addSupportedResourceType(RESOURCE_TYPE.AE.Value());
			csebase.addSupportedResourceType(RESOURCE_TYPE.CONTAINER.Value());
			csebase.addSupportedResourceType(RESOURCE_TYPE.CONTENT_INST.Value());
			csebase.addSupportedResourceType(RESOURCE_TYPE.CSE_BASE.Value());
			csebase.addSupportedResourceType(RESOURCE_TYPE.GROUP.Value());
			csebase.addSupportedResourceType(RESOURCE_TYPE.NODE.Value());
			csebase.addSupportedResourceType(RESOURCE_TYPE.POLLING_CHANN.Value());
			csebase.addSupportedResourceType(RESOURCE_TYPE.REMOTE_CSE.Value());
			csebase.addSupportedResourceType(RESOURCE_TYPE.REQUEST.Value());
			csebase.addSupportedResourceType(RESOURCE_TYPE.SUBSCRIPTION.Value());
			csebase.addSupportedResourceType(RESOURCE_TYPE.FLEXCONTAINER.Value());						// added in 2017-03-13
			csebase.addSupportedResourceType(RESOURCE_TYPE.SCHEDULE.Value());						// added in 2017-03-13
			csebase.addSupportedResourceType(RESOURCE_TYPE.NOTIFICATIONTARGETPOLICY.Value());						// added in 2017-03-13
			csebase.addPointOfAccess(CfgManager.getInstance().getPointOfAccess());
			
			dao.create(csebase);
			
			dao.createResourceIndex(); // added in 2017-03-13
			
		}
		
	}

}
