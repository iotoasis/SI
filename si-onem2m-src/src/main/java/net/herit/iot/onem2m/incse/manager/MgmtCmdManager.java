package net.herit.iot.onem2m.incse.manager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.herit.iot.message.onem2m.OneM2mResponse.RESPONSE_STATUS;
import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.message.onem2m.OneM2mRequest.OPERATION;
import net.herit.iot.message.onem2m.OneM2mRequest.RESOURCE_TYPE;
import net.herit.iot.message.onem2m.OneM2mRequest.RESULT_CONT;
import net.herit.iot.onem2m.core.convertor.ConvertorFactory;
import net.herit.iot.onem2m.core.convertor.JSONConvertor;
import net.herit.iot.onem2m.core.convertor.XMLConvertor;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.incse.context.OneM2mContext;
import net.herit.iot.onem2m.incse.controller.AnnounceController;
import net.herit.iot.onem2m.incse.controller.MgmtCmdController;
import net.herit.iot.onem2m.incse.facility.CfgManager;
import net.herit.iot.onem2m.incse.manager.dao.DAOInterface;
import net.herit.iot.onem2m.incse.manager.dao.ExecInstanceDAO;
import net.herit.iot.onem2m.incse.manager.dao.MgmtCmdDAO;
import net.herit.iot.onem2m.incse.manager.dao.ResourceDAO;
import net.herit.iot.onem2m.resource.ExecInstance;
import net.herit.iot.onem2m.resource.Group;
import net.herit.iot.onem2m.resource.MgmtCmd;
import net.herit.iot.onem2m.resource.Naming;
import net.herit.iot.onem2m.resource.Node;
import net.herit.iot.onem2m.resource.Resource;
import net.herit.iot.onem2m.resource.UriListContent;


public class MgmtCmdManager extends AbsManager {
	
	static String ALLOWED_PARENT = "CSEBase";  
	static RESOURCE_TYPE RES_TYPE = RESOURCE_TYPE.MGMT_CMD; 
	
	private Logger log = LoggerFactory.getLogger(MgmtCmdManager.class);

	private static final String TAG = MgmtCmdManager.class.getName();

	public MgmtCmdManager(OneM2mContext context) {
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
	
	protected List<Node> getTargetNodeList(String target) throws OneM2MException {

		List<Node> nodes = new ArrayList<Node>();
		
		ResourceDAO dao = new ResourceDAO(context);
		Resource res = dao.getResourceWithID(target);
		if (res != null) {	// in case target is resourceId
			switch (RESOURCE_TYPE.get(res.getResourceType())) {
			case NODE:
				nodes.add((Node)res);
				return nodes;

			case GROUP:
				List<String> ids = ((Group)res).getMemberIDs();
				Iterator<String> it = ids.iterator();
				while (it.hasNext()) {
					Resource memRes = dao.getResourceWithID(it.next());
					if (memRes.getResourceType() == RESOURCE_TYPE.NODE.Value()) {
						nodes.add((Node)memRes);
					}
				}
				if (nodes.size() == 0) {
					throw new OneM2MException(RESPONSE_STATUS.BAD_REQUEST, "Specified group has no node member.");
				}
				return nodes;
				
			default:
				throw new OneM2MException(RESPONSE_STATUS.BAD_REQUEST, "Specified resource is not node or group.");
			}
		}
		
		res = dao.getResource(Naming.NODEID_SN, target);
		if (res.getResourceType() == RESOURCE_TYPE.NODE.Value()) {
			nodes.add((Node)res);
			return nodes;
		} else {
			throw new OneM2MException(RESPONSE_STATUS.BAD_REQUEST, "Specified resource is not node or group.");
		}

	}

	@Override
	public OneM2mResponse update(OneM2mRequest reqMessage) throws OneM2MException {
		
		MgmtCmd mgmtCmd = (MgmtCmd)getContentResource(reqMessage, this);
		
		MgmtCmd mgmtCmdDb = (MgmtCmd)this.getDAO().retrieve(reqMessage.getTo(), RESULT_CONT.ATTRIBUTE); 
		
		Boolean execEnable = mgmtCmd.isExecEnable();
		if (execEnable != null && mgmtCmd.isExecEnable()) {
			
			mgmtCmdDb.setExecReqArgs(mgmtCmd.getExecReqArgs());
			mgmtCmdDb.setExecEnable(mgmtCmd.isExecEnable());
						
			List<Node> nodes = getTargetNodeList(mgmtCmdDb.getExecTarget());
			if (nodes.size() == 0) {
				throw new OneM2MException(RESPONSE_STATUS.BAD_REQUEST, "No valid execTarget included!!!");
			}

			ExecInstanceManager execInstMngr= (ExecInstanceManager)ManagerFactory.create(RESOURCE_TYPE.EXEC_INST, context);
			ExecInstance execInst;
			UriListContent uriList = new UriListContent();
			
			Iterator<Node> it = nodes.iterator();
			while (it.hasNext()) {
				Node node = it.next();
				
				execInst = execInstMngr.createResource(mgmtCmdDb, node.getUri());
				
				uriList.addUriList(execInst.getUri());
				MgmtCmdController.executeCommand(context, mgmtCmdDb, execInst, node);
			}
			
			// return ok with execInstance resource
			OneM2mResponse resMessage = new OneM2mResponse(RESPONSE_STATUS.OK, reqMessage);
			resMessage.setContentObject(uriList);
						
			return resMessage;
			
		} else {
			
			return updateResource(mgmtCmd, reqMessage, this);
		}
		
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

//	@Override 
//	public void notification(Resource parentRes, Resource reqRes, Resource orgRes, OPERATION op) throws OneM2MException {
//		Document reqDoc = reqRes == null? null : Document.parse( ((GroupDAO)getDAO()).resourceToJson(reqRes));
//		Document orgDoc = orgRes == null? null : Document.parse( ((GroupDAO)getDAO()).resourceToJson(orgRes));
//		NotificationController.getInstance().notify(parentRes, reqDoc, reqRes, orgDoc, orgRes, op, this);
//	}
	
	
	@Override
	public DAOInterface getDAO() {
		return new MgmtCmdDAO(context);
	}

	@Override
	public Class<?> getResourceClass() {
		return MgmtCmd.class;
	}

	@Override
	public XMLConvertor<?> getXMLConveter() throws OneM2MException {
		return ConvertorFactory.getXMLConvertor(MgmtCmd.class, MgmtCmd.SCHEMA_LOCATION);
	}

	@Override
	public JSONConvertor<?> getJSONConveter() throws OneM2MException {
		return ConvertorFactory.getJSONConvertor(MgmtCmd.class, MgmtCmd.SCHEMA_LOCATION);
	}

	@Override
	public void validateResource(Resource res, OneM2mRequest req, Resource curRes) throws OneM2MException {
		log.debug("MgmtCmd.validate called: {}, {}", res.getUri(), req.getOperationEnum().Name());
				
		super.validateResource(res, req, curRes);
		
		MgmtCmd mgmtCmd = (MgmtCmd)res;
		OPERATION op = req.getOperationEnum(); 
		MgmtCmdDAO dao = (MgmtCmdDAO)this.getDAO();
		String target;
		Resource node;
		
		switch (op) {
		case CREATE:
			
			target = mgmtCmd.getExecTarget();
			node = dao.getResource(Naming.NODEID_SN, target);
			if (node == null || node.getResourceType() != RESOURCE_TYPE.NODE.Value()) {
				throw new OneM2MException(RESPONSE_STATUS.INVALID_ARGUMENTS, "Specified node does not exist. nodeid:"+target+")");
			}
			
			break;
		case RETRIEVE:
			break;
		case UPDATE:
			
			target = mgmtCmd.getExecTarget();
			if (target != null) {
				node = dao.getResource(Naming.NODEID_SN, target);
				if (node == null || node.getResourceType() != RESOURCE_TYPE.NODE.Value()) {
					throw new OneM2MException(RESPONSE_STATUS.INVALID_ARGUMENTS, "Specified node does not exist. nodeid:"+target+")");
				}
			}

			break;
		case DELETE:
			break;
		case DISCOVERY:
			break;
		case NOTIFY:
			break;
		default:
			//break;
			
		}
	}
	
	@Override
	public void updateResource(Resource resource, OneM2mRequest req) throws OneM2MException {
		MgmtCmd res =  (MgmtCmd)resource;
		
		//// TS-0001-XXX-V1_13_1 - 10.1.1.1	Non-registration related CREATE procedure (ExpirationTime..)
		if(res.getExpirationTime() == null) {
			res.setExpirationTime(CfgManager.getInstance().getDefaultExpirationTime());
		}
		// END - Non-registration related CREATE procedure
	}
}
