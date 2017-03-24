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
import net.herit.iot.onem2m.incse.manager.dao.ContainerDAO;
import net.herit.iot.onem2m.incse.manager.dao.DAOInterface;
import net.herit.iot.onem2m.incse.manager.dao.DynamicAuthorizationConsultationDAO;
import net.herit.iot.onem2m.resource.Container;
import net.herit.iot.onem2m.resource.DynamicAuthorizationConsultation;
import net.herit.iot.onem2m.resource.Resource;

public class DynamicAuthorizationConsultationManager extends AbsManager {

	static String ALLOWED_PARENT = "AE,AEAnnc,remoteCSE,remoteCSEAnnc,CSEBase";  
	static RESOURCE_TYPE RES_TYPE = RESOURCE_TYPE.DYNAMICAUTHORIZATIONCONSULTATION; 
	
	private Logger log = LoggerFactory.getLogger(DynamicAuthorizationConsultationManager.class);

	private static final String TAG = DynamicAuthorizationConsultationManager.class.getName();

	public DynamicAuthorizationConsultationManager(OneM2mContext context) {
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

	public OneM2mResponse retrieveLatest(OneM2mRequest reqMessage) throws OneM2MException {

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
		Document reqDoc = reqRes == null? null : Document.parse( ((DynamicAuthorizationConsultationDAO)getDAO()).resourceToJson(reqRes));
		Document orgDoc = orgRes == null? null : Document.parse( ((DynamicAuthorizationConsultationDAO)getDAO()).resourceToJson(orgRes));
		NotificationController.getInstance().notify(parentRes, reqDoc, reqRes, orgDoc, orgRes, op, this);
	}
	
	@Override
	public DAOInterface getDAO() {
		return new DynamicAuthorizationConsultationDAO(context);
	}

	@Override
	public XMLConvertor<?> getXMLConveter() throws OneM2MException {
		return ConvertorFactory.getXMLConvertor(DynamicAuthorizationConsultation.class, DynamicAuthorizationConsultation.SCHEMA_LOCATION);
	}

	@Override
	public JSONConvertor<?> getJSONConveter() throws OneM2MException {
		return ConvertorFactory.getJSONConvertor(DynamicAuthorizationConsultation.class, DynamicAuthorizationConsultation.SCHEMA_LOCATION);
	}

	@Override
	public Class<?> getResourceClass() {
		return DynamicAuthorizationConsultation.class;
	}

}
