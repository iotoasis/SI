package net.herit.iot.onem2m.incse.manager;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.message.onem2m.OneM2mRequest.OPERATION;
import net.herit.iot.message.onem2m.OneM2mRequest.RESOURCE_TYPE;
import net.herit.iot.message.onem2m.OneM2mResponse.RESPONSE_STATUS;
import net.herit.iot.message.onem2m.format.Enums.EXEC_STATUS;
import net.herit.iot.message.onem2m.format.Enums.REQUEST_STATUS;
import net.herit.iot.onem2m.core.convertor.ConvertorFactory;
import net.herit.iot.onem2m.core.convertor.JSONConvertor;
import net.herit.iot.onem2m.core.convertor.XMLConvertor;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.incse.context.OneM2mContext;
import net.herit.iot.onem2m.incse.controller.NotificationController;
import net.herit.iot.onem2m.incse.facility.CfgManager;
import net.herit.iot.onem2m.incse.manager.dao.DAOInterface;
import net.herit.iot.onem2m.incse.manager.dao.ExecInstanceDAO;
import net.herit.iot.onem2m.resource.ExecInstance;
import net.herit.iot.onem2m.resource.MetaInformation;
import net.herit.iot.onem2m.resource.MgmtCmd;
import net.herit.iot.onem2m.resource.PrimitiveContent;
import net.herit.iot.onem2m.resource.Resource;


public class ExecInstanceManager extends AbsManager {
	
	static String ALLOWED_PARENT = "CSEBase";  
	static RESOURCE_TYPE RES_TYPE = RESOURCE_TYPE.EXEC_INST; 
	
	private Logger log = LoggerFactory.getLogger(ExecInstanceManager.class);

	private static final String TAG = ExecInstanceManager.class.getName();
	
	public ExecInstanceManager(OneM2mContext context) {
		super(RES_TYPE, ALLOWED_PARENT);
		this.context = context;
	}
	
	public ExecInstance createResource(MgmtCmd mgmtCmd, String targetId) throws OneM2MException {
		
		ExecInstance execInst = new ExecInstance();
		execInst.setExecStatus(EXEC_STATUS.INITIATED.Value());
		execInst.setExecTarget(targetId);
		execInst.setExecMode(mgmtCmd.getExecMode());
		execInst.setExecFrequency(mgmtCmd.getExecFrequency());
		execInst.setExecDelay(mgmtCmd.getExecDelay());
		execInst.setExecNumber(mgmtCmd.getExecNumber());
		execInst.setExecReqArgs(mgmtCmd.getExecReqArgs());

		String execInstId = Manager.createResourceID(RESOURCE_TYPE.EXEC_INST);
						
		execInst.setUri(mgmtCmd.getUri()+"/"+ execInstId);
		execInst.setResourceType(RESOURCE_TYPE.EXEC_INST.Value());
		execInst.setResourceID(execInstId);
		execInst.setParentID(mgmtCmd.getResourceID());
		
		getDAO().create(execInst);
		
		return execInst;
		
	}
	
	@Override
	public OneM2mResponse create(OneM2mRequest reqMessage) throws OneM2MException {
		
		throw new OneM2MException(RESPONSE_STATUS.OPERATION_NOT_ALLOWED, "ExecInstance creation is not allowed.");
		
	}

	@Override
	public OneM2mResponse retrieve(OneM2mRequest reqMessage) throws OneM2MException {
		
		return retrieve(reqMessage, this);
			
	}

	@Override
	public OneM2mResponse update(OneM2mRequest reqMessage) throws OneM2MException {
		
		throw new OneM2MException(RESPONSE_STATUS.OPERATION_NOT_ALLOWED, "ExecInstance modification is not allowed.");
			
	}
	
	@Override
	public OneM2mResponse delete(OneM2mRequest reqMessage) throws OneM2MException {
		
		throw new OneM2MException(RESPONSE_STATUS.OPERATION_NOT_ALLOWED, "ExecInstance deletion is not allowed.");
		
	}

	@Override
	public OneM2mResponse notify(OneM2mRequest reqMessage) throws OneM2MException {
		// TODO Auto-generated method stub

		return new OneM2mResponse();
	}

//	@Override 
//	public void notification(Resource parentRes, Resource reqRes, Resource orgRes, OPERATION op) throws OneM2MException {
//		Document reqDoc = reqRes == null? null : Document.parse( ((RequestDAO)getDAO()).resourceToJson(reqRes));
//		Document orgDoc = orgRes == null? null : Document.parse( ((RequestDAO)getDAO()).resourceToJson(orgRes));
//		NotificationController.getInstance().notify(parentRes, reqDoc, reqRes, orgDoc, orgRes, op, this);
//	}
	
	@Override
	public DAOInterface getDAO() {
		return new ExecInstanceDAO(context);
	}

	@Override
	public Class<?> getResourceClass() {
		return ExecInstance.class;
	}

//	public ExecInstance createResource(OneM2mRequest reqMessage) throws OneM2MException {
//
//		DAOInterface dao = this.getDAO();
//		
//		
//		ExecInstance res = new ExecInstance();
//
//		String resourceID = this.createResourceID(RESOURCE_TYPE.REQUEST, res, reqMessage);	
//		String resName = resourceID;
//		//String name = createResourceName(RESOURCE_TYPE.REQUEST, CfgManager.getInstance().getCSEBaseName()); 
//	    
////	    res.setStateTag(0);	
////	    res.setOperation(reqMessage.getOperation().Value());	
////	    res.setTarget(reqMessage.getTo());	
////	    res.setOriginator(reqMessage.getFrom());	
////	    res.setRequestID(reqMessage.getRequestId());
////	    
////	    MetaInformation mi = new MetaInformation(reqMessage);
////	    res.setMetaInformation(mi);
////	    
////	    res.setPrimitiveContent(new PrimitiveContent(reqMessage));
////	    res.setRequestStatus(REQUEST_STATUS.PENDING.Value());
////	    res.setOperationResult(null);
////
////		res.validate(OPERATION.CREATE);
////				
////		res.setResourceName(resName);
////		res.setUri("/"+CfgManager.getInstance().getCSEBaseName()+"/"+ resName);
////		res.setResourceType(RESOURCE_TYPE.REQUEST.Value());
////		res.setResourceID(resourceID);
////		res.setParentID(CfgManager.getInstance().getCSEBaseName());
//		
//		dao.create(res);
//
//		return res;
//		
//	}

	@Override
	public XMLConvertor<?> getXMLConveter() throws OneM2MException {
		return ConvertorFactory.getXMLConvertor(ExecInstance.class, ExecInstance.SCHEMA_LOCATION);
	}

	@Override
	public JSONConvertor<?> getJSONConveter() throws OneM2MException {
		return ConvertorFactory.getJSONConvertor(ExecInstance.class, ExecInstance.SCHEMA_LOCATION);
	}
	
	@Override
	public void updateResource(Resource resource, OneM2mRequest req) throws OneM2MException {
		ExecInstance res =  (ExecInstance)resource;
		
		//// TS-0001-XXX-V1_13_1 - 10.1.1.1	Non-registration related CREATE procedure (ExpirationTime..)
		if(res.getExpirationTime() == null) {
			res.setExpirationTime(CfgManager.getInstance().getDefaultExpirationTime());
		}
		// END - Non-registration related CREATE procedure
	}

}
