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
import net.herit.iot.onem2m.incse.facility.CfgManager;
import net.herit.iot.onem2m.incse.manager.dao.AllJoynMethodCallDAO;
import net.herit.iot.onem2m.incse.manager.dao.AllJoynMethodDAO;
import net.herit.iot.onem2m.incse.manager.dao.DAOInterface;
import net.herit.iot.onem2m.resource.AllJoynMethod;
import net.herit.iot.onem2m.resource.AllJoynMethodCall;
import net.herit.iot.onem2m.resource.FlexContainerResource;
import net.herit.iot.onem2m.resource.Resource;

public class AllJoynMethodCallManager extends AbsManager {
	
	static String ALLOWED_PARENT = "CSEBase,AE,remoteCSE,container";  
	static RESOURCE_TYPE RES_TYPE = RESOURCE_TYPE.FLEXCONTAINER; 
	
	private Logger log = LoggerFactory.getLogger(AllJoynMethodCallManager.class);

	private static final String TAG = AllJoynMethodCallManager.class.getName();

	public AllJoynMethodCallManager(OneM2mContext context) {
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
	public void announceTo(OneM2mRequest reqMessage, Resource resource, Resource orgRes) throws OneM2MException {
		new AnnounceController(context).announce(reqMessage, resource, orgRes, this);
	}

	@Override 
	public void notification(Resource parentRes, Resource reqRes, Resource orgRes, OPERATION op) throws OneM2MException {
		Document reqDoc = reqRes == null? null : Document.parse( ((AllJoynMethodCallDAO)getDAO()).resourceToJson(reqRes));
		Document orgDoc = orgRes == null? null : Document.parse( ((AllJoynMethodCallDAO)getDAO()).resourceToJson(orgRes));
		NotificationController.getInstance().notify(parentRes, reqDoc, reqRes, orgDoc, orgRes, op, this);
	}
	
	@Override
	public DAOInterface getDAO() {
		return new AllJoynMethodCallDAO(context);
	}

	@Override
	public Class<?> getResourceClass() {
		return AllJoynMethodCall.class;
	}

	@Override
	public XMLConvertor<?> getXMLConveter() throws OneM2MException {
		return ConvertorFactory.getXMLConvertor(AllJoynMethodCall.class, AllJoynMethodCall.SCHEMA_LOCATION);
	}

	@Override
	public JSONConvertor<?> getJSONConveter() throws OneM2MException {
		return ConvertorFactory.getJSONConvertor(AllJoynMethodCall.class, AllJoynMethodCall.SCHEMA_LOCATION);
	}
	
	@Override
	public void updateResource(Resource resource, OneM2mRequest req) throws OneM2MException {
		
		
		// update link attribute using ae id		 
		FlexContainerResource cont = (FlexContainerResource)resource;
		cont.setCreator(req.getFrom());

		//// TS-0001-XXX-V1_13_1 - 10.1.1.1	Non-registration related CREATE procedure (ExpirationTime..)
		if(cont.getExpirationTime() == null) {
			cont.setExpirationTime(CfgManager.getInstance().getDefaultExpirationTime());
		}
		// END - Non-registration related CREATE procedure
	}

}
