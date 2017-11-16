package net.herit.iot.onem2m.incse.manager;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.message.onem2m.OneM2mRequest.OPERATION;
import net.herit.iot.message.onem2m.OneM2mRequest.RESOURCE_TYPE;
import net.herit.iot.message.onem2m.OneM2mResponse.RESPONSE_STATUS;
import net.herit.iot.message.onem2m.format.Enums.REQUEST_STATUS;
import net.herit.iot.onem2m.core.convertor.ConvertorFactory;
import net.herit.iot.onem2m.core.convertor.JSONConvertor;
import net.herit.iot.onem2m.core.convertor.XMLConvertor;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.incse.context.OneM2mContext;
import net.herit.iot.onem2m.incse.controller.NotificationController;
import net.herit.iot.onem2m.incse.facility.CfgManager;
import net.herit.iot.onem2m.incse.manager.dao.DAOInterface;
import net.herit.iot.onem2m.incse.manager.dao.RequestDAO;
import net.herit.iot.onem2m.resource.MetaInformation;
import net.herit.iot.onem2m.resource.PrimitiveContent;
import net.herit.iot.onem2m.resource.Request;
import net.herit.iot.onem2m.resource.Resource;


public class RequestManager extends AbsManager {
	
	static String ALLOWED_PARENT = "CSEBase";  
	static RESOURCE_TYPE RES_TYPE = RESOURCE_TYPE.REQUEST; 
	
	private Logger log = LoggerFactory.getLogger(RequestManager.class);

	private static final String TAG = RequestManager.class.getName();
	
	public RequestManager(OneM2mContext context) {
		super(RES_TYPE, ALLOWED_PARENT);
		this.context = context;
	}
	
	@Override
	public OneM2mResponse create(OneM2mRequest reqMessage) throws OneM2MException {
		
		throw new OneM2MException(RESPONSE_STATUS.OPERATION_NOT_ALLOWED, "Request creation is not allowed.");
		
	}

	@Override
	public OneM2mResponse retrieve(OneM2mRequest reqMessage) throws OneM2MException {
		
		return retrieve(reqMessage, this);
			
	}

	@Override
	public OneM2mResponse update(OneM2mRequest reqMessage) throws OneM2MException {
		
		throw new OneM2MException(RESPONSE_STATUS.OPERATION_NOT_ALLOWED, "Request modification is not allowed.");
			
	}
	
	@Override
	public OneM2mResponse delete(OneM2mRequest reqMessage) throws OneM2MException {
		
		throw new OneM2MException(RESPONSE_STATUS.OPERATION_NOT_ALLOWED, "Request deletion is not allowed.");
		
	}

	@Override
	public OneM2mResponse notify(OneM2mRequest reqMessage) throws OneM2MException {
		// TODO Auto-generated method stub

		return new OneM2mResponse();
	}

	@Override 
	public void notification(Resource parentRes, Resource reqRes, Resource orgRes, OPERATION op) throws OneM2MException {
		Document reqDoc = reqRes == null? null : Document.parse( ((RequestDAO)getDAO()).resourceToJson(reqRes));
		Document orgDoc = orgRes == null? null : Document.parse( ((RequestDAO)getDAO()).resourceToJson(orgRes));
		NotificationController.getInstance().notify(parentRes, reqDoc, reqRes, orgDoc, orgRes, op, this);
	}
	
	@Override
	public DAOInterface getDAO() {
		return new RequestDAO(context);
	}

	@Override
	public Class<?> getResourceClass() {
		return Request.class;
	}

	public Request createResource(OneM2mRequest reqMessage) throws OneM2MException {

		DAOInterface dao = this.getDAO();
		
		
		Request res = new Request();

		String resourceID = this.createResourceID(RESOURCE_TYPE.REQUEST, res, reqMessage);	
		String resName = resourceID;
		//String name = createResourceName(RESOURCE_TYPE.REQUEST, CfgManager.getInstance().getCSEBaseName()); 
	    
	    res.setStateTag(0);	
	    res.setOperation(reqMessage.getOperationEnum().Value());	
	    res.setTarget(reqMessage.getTo());	
	    res.setOriginator(reqMessage.getFrom());	
	    res.setRequestID(reqMessage.getRequestIdentifier());
	    
	    MetaInformation mi = new MetaInformation(reqMessage);
	    res.setMetaInformation(mi);
	    
	    res.setPrimitiveContent(new PrimitiveContent(reqMessage));
	    res.setRequestStatus(REQUEST_STATUS.PENDING.Value());
	    res.setOperationResult(null);

		res.validate(OPERATION.CREATE);
				
		res.setResourceName(resName);
		res.setUri(CfgManager.getInstance().getCSEBaseUri()+"/"+ resName);
		res.setResourceType(RESOURCE_TYPE.REQUEST.Value());
		res.setResourceID(resourceID);
		res.setParentID(CfgManager.getInstance().getCSEBaseName());
		
		dao.create(res);

		return res;
		
	}

	@Override
	public XMLConvertor<?> getXMLConveter() throws OneM2MException {
		return ConvertorFactory.getXMLConvertor(Request.class, Request.SCHEMA_LOCATION);
	}

	@Override
	public JSONConvertor<?> getJSONConveter() throws OneM2MException {
		return ConvertorFactory.getJSONConvertor(Request.class, Request.SCHEMA_LOCATION);
	}

	@Override
	public void updateResource(Resource resource, OneM2mRequest req) throws OneM2MException {
		Request res =  (Request)resource;
		
		//// TS-0001-XXX-V1_13_1 - 10.1.1.1	Non-registration related CREATE procedure (ExpirationTime..)
		if(res.getExpirationTime() == null) {
			res.setExpirationTime(CfgManager.getInstance().getDefaultExpirationTime());
		}
		// END - Non-registration related CREATE procedure
	}
}
