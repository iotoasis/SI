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
import net.herit.iot.onem2m.incse.controller.RestSemanticController;
import net.herit.iot.onem2m.incse.facility.CfgManager;
import net.herit.iot.onem2m.incse.manager.dao.ContainerDAO;
import net.herit.iot.onem2m.incse.manager.dao.DAOInterface;
import net.herit.iot.onem2m.incse.manager.dao.SemanticDescriptorDAO;
import net.herit.iot.onem2m.resource.Container;
import net.herit.iot.onem2m.resource.Resource;
import net.herit.iot.onem2m.resource.SemanticDescriptor;

public class SemanticDescriptorManager extends AbsManager {

	static String ALLOWED_PARENT = "AE,container,contentInstance,group,node,flexContainer,timeSeries";  // added in 2016-10-18
	static RESOURCE_TYPE RES_TYPE = RESOURCE_TYPE.SEMANTICDESCRIPTOR;
	
	private Logger log = LoggerFactory.getLogger(SemanticDescriptorManager.class);

	private static final String TAG = SemanticDescriptorManager.class.getName();
	
	public SemanticDescriptorManager(OneM2mContext context) {
		super(RES_TYPE, ALLOWED_PARENT);
		this.context = context;
		RestSemanticController.getInstance().initialize(context);
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
	public void announceTo(OneM2mRequest reqMessage, Resource reqRes, Resource orgRes) throws OneM2MException {
		new AnnounceController(context).announce(reqMessage, reqRes, orgRes, this);
	}

	@Override 
	public void notification(Resource parentRes, Resource reqRes, Resource orgRes, OPERATION op) throws OneM2MException {
		Document reqDoc = reqRes == null? null : Document.parse( ((SemanticDescriptorDAO)getDAO()).resourceToJson(reqRes));
		Document orgDoc = orgRes == null? null : Document.parse( ((SemanticDescriptorDAO)getDAO()).resourceToJson(orgRes));
		NotificationController.getInstance().notify(parentRes, reqDoc, reqRes, orgDoc, orgRes, op, this);
	}
	
	@Override
	public DAOInterface getDAO() {
		return new SemanticDescriptorDAO(context);
	}

	@Override
	public XMLConvertor<?> getXMLConveter() throws OneM2MException {
		return ConvertorFactory.getXMLConvertor(SemanticDescriptor.class, SemanticDescriptor.SCHEMA_LOCATION);
	}

	@Override
	public JSONConvertor<?> getJSONConveter() throws OneM2MException {
		return ConvertorFactory.getJSONConvertor(SemanticDescriptor.class, SemanticDescriptor.SCHEMA_LOCATION);
	}

	@Override
	public Class<?> getResourceClass() {
		return SemanticDescriptor.class;
	}
	
	@Override
	public void updateResource(Resource resource, OneM2mRequest req) throws OneM2MException {
		
		// update link attribute using ae id		 
		SemanticDescriptor desc = (SemanticDescriptor)resource;
		desc.setCreator(req.getFrom());

		//// TS-0001-XXX-V1_13_1 - 10.1.1.1	Non-registration related CREATE procedure (ExpirationTime..)
		if(desc.getExpirationTime() == null) {
			desc.setExpirationTime(CfgManager.getInstance().getDefaultExpirationTime());
		}
		// END - Non-registration related CREATE procedure
	}

}
