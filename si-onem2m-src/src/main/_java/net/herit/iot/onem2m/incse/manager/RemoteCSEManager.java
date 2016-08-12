package net.herit.iot.onem2m.incse.manager;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.message.onem2m.OneM2mRequest.OPERATION;
import net.herit.iot.message.onem2m.OneM2mRequest.RESOURCE_TYPE;
import net.herit.iot.message.onem2m.OneM2mResponse.RESPONSE_STATUS;
import net.herit.iot.message.onem2m.format.Enums.CONTENT_TYPE;
import net.herit.iot.message.onem2m.format.Enums.CSE_TYPE;
import net.herit.iot.onem2m.core.convertor.ConvertorFactory;
import net.herit.iot.onem2m.core.convertor.JSONConvertor;
import net.herit.iot.onem2m.core.convertor.XMLConvertor;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.core.util.Utils;
import net.herit.iot.onem2m.incse.context.OneM2mContext;
import net.herit.iot.onem2m.incse.controller.AnnounceController;
import net.herit.iot.onem2m.incse.controller.NotificationController;
import net.herit.iot.onem2m.incse.facility.CfgManager;
import net.herit.iot.onem2m.incse.facility.OneM2mUtil;
import net.herit.iot.onem2m.incse.facility.QosManager;
import net.herit.iot.onem2m.incse.facility.SeqNumManager;
import net.herit.iot.onem2m.incse.manager.dao.DAOInterface;
import net.herit.iot.onem2m.incse.manager.dao.RemoteCSEDAO;
import net.herit.iot.onem2m.incse.manager.dao.ResourceDAO;
import net.herit.iot.onem2m.resource.RemoteCSE;
import net.herit.iot.onem2m.resource.Resource;


public class RemoteCSEManager extends AbsManager {
	
	static String ALLOWED_PARENT = "CSEBase";  
	static RESOURCE_TYPE RES_TYPE = RESOURCE_TYPE.REMOTE_CSE; 
	
	private Logger log = LoggerFactory.getLogger(RemoteCSEManager.class);

	private static final String TAG = RemoteCSEManager.class.getName();

	public RemoteCSEManager(OneM2mContext context) {
		super(RES_TYPE, ALLOWED_PARENT);
		this.context = context;
	}
	
	@Override
	public OneM2mResponse create(OneM2mRequest reqMessage) throws OneM2MException {

		QosManager qm = QosManager.getInstance(); 
		if (qm.checkNAddCSENumber(1)) {
			try {
				return create(reqMessage, this);
			} catch (OneM2MException e) {

				qm.reduceCSENumber(1);
				throw e;
			}
		} else {
			throw new OneM2MException(RESPONSE_STATUS.MAX_NUMBER_OF_MEMBER_EXCEEDED, 
					"CSE Quota already fulled!!! ("+qm.getMaxCSENumber()+")");
		}
		
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

		OneM2mResponse res = delete(reqMessage, this);
		QosManager.getInstance().reduceCSENumber(1);
		return res;
	
	}


	@Override
	public OneM2mResponse notify(OneM2mRequest reqMessage) throws OneM2MException {
		throw new OneM2MException(RESPONSE_STATUS.NOT_IMPLEMENTED, "Not implemented!!!");

	}

	@Override
	public void announceTo(OneM2mRequest reqMessage, Resource resource, Resource orgRes) throws OneM2MException {
		new AnnounceController(context).announce(reqMessage, resource, orgRes, this);
	}
	
	@Override 
	public void notification(Resource parentRes, Resource reqRes, Resource orgRes, OPERATION op) throws OneM2MException {
		Document reqDoc = reqRes == null? null : Document.parse( ((RemoteCSEDAO)getDAO()).resourceToJson(reqRes));
		Document orgDoc = orgRes == null? null : Document.parse( ((RemoteCSEDAO)getDAO()).resourceToJson(orgRes));
		NotificationController.getInstance().notify(parentRes, reqDoc, reqRes, orgDoc, orgRes, op, this);
	}
	
	@Override
	public DAOInterface getDAO() {
		return new RemoteCSEDAO(context);
	}

	@Override
	public Class<?> getResourceClass() {
		return RemoteCSE.class;
	}


	@Override
	public XMLConvertor<?> getXMLConveter() throws OneM2MException {
		return ConvertorFactory.getXMLConvertor(RemoteCSE.class, RemoteCSE.SCHEMA_LOCATION);
	}

	@Override
	public JSONConvertor<?> getJSONConveter() throws OneM2MException {
		return ConvertorFactory.getJSONConvertor(RemoteCSE.class, RemoteCSE.SCHEMA_LOCATION);
	}


	@Override
	public void validateResource(Resource res, OneM2mRequest req, Resource curRes) throws OneM2MException {
		context.getLogManager().debug("GroupManager.validate called:"+ res.getUri() +","+ req.getOperationEnum().Name());
		
		RemoteCSEDAO dao;
		RemoteCSE rcse = (RemoteCSE)res;
		OPERATION op = req.getOperationEnum(); 
				
		switch (op) {
		case CREATE:
			// compare number of memberID and maxNrOfMember -> MAX_NUMBER_OF_MEMBER_EXCEEDED
			rcse.getCSEBase();
			String cseID = rcse.getCSEID();	// sp relative cse id
			if (!checkIfCseIdFormatValid(cseID)) {
				throw new OneM2MException(RESPONSE_STATUS.BAD_REQUEST, "CseID not valid:"+cseID);
			}
			if (checkIfCseIdDuplicated(cseID)) {
				throw new OneM2MException(RESPONSE_STATUS.CONFLICT, "Duplicated CSE ID:"+cseID);
			}
			break;
		case RETRIEVE:
			break;
		case UPDATE:
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
		
		super.validateResource(res, req, curRes);
	}
	
	private boolean checkIfCseIdDuplicated(String cseId) {
		ResourceDAO dao = new ResourceDAO(context);
		if (dao.checkIfResourceExist(cseId.substring(1))) {
			return true;
		}
		return false;
	}
	
	private boolean checkIfCseIdFormatValid(String cseId) {
		if (cseId.startsWith("/")) {
			return true;
		}
		return false;
	}
	
	@Override
	public String createResourceID(RESOURCE_TYPE type, Resource res, OneM2mRequest req) throws OneM2MException {
		RemoteCSE rcse = (RemoteCSE)res;
		String cseID = rcse.getCSEID();
		if (cseID != null && cseID.length() > 1) {
			String resId = OneM2mUtil.extractResourceId(cseID);
			ResourceDAO dao = this.getCommonDAO();
			if (dao.checkIfResourceExist(resId)) {
				throw new OneM2MException(RESPONSE_STATUS.CONFLICT, "Resource already exist:"+cseID);
			}
			return resId;
		} else {
			//return RESID_PREFIX + type+"_"+UUID.randomUUID().toString();
			return type+"_"+SeqNumManager.getInstance().get(type.Name());
		}		
	}

	public boolean checkIfRegistered(String cseId, CSE_TYPE cseType, OneM2mContext context) {
		RemoteCSEDAO dao = new RemoteCSEDAO(context);
		return dao.checkIfRegistered(cseId, cseType);
	}

	public void registerToRemoteCSE(String remoteCseId, List<String> remotePoAs, OneM2mContext context) {
		RemoteCSEDAO dao = new RemoteCSEDAO(context);
		
		if (remotePoAs == null || remotePoAs.size() <= 0) {
			log.debug("Ignore registerToRemoteCSE("+remoteCseId+") because PoA not provided!!!");
			return;
		}
		
		String strUrl; 
		URL url;
		try {
			strUrl = remotePoAs.get(0);
			url = new URL(strUrl);
		} catch (MalformedURLException e) {
			log.debug("Ignore registerToRemoteCSE("+remoteCseId+") because cannot parse URL!!! ", e);
			return;
		}
		
		String lcId = CfgManager.getInstance().getCSEBaseCid();
		String lcBaseName = CfgManager.getInstance().getCSEBaseName();
		String lcPoA = CfgManager.getInstance().getPointOfAccess();
		
		OneM2mRequest req = new OneM2mRequest();
		req.setContentType(CONTENT_TYPE.RES_XML);
		req.setFrom(CfgManager.getInstance().getAbsoluteCSEBaseId());
		req.setOperation(OPERATION.CREATE);
		req.setRequestIdentifier(OneM2mUtil.createRequestId());
		req.setResourceType(RESOURCE_TYPE.REMOTE_CSE);
		req.setRemoteHost(url.getHost());
		req.setTo(remoteCseId);
		
		RemoteCSE rc = new RemoteCSE();
		rc.setCSEID(lcId);
		rc.setCSEBase(lcBaseName);
		rc.setCseType(CSE_TYPE.IN_CSE.Value());
		rc.setRequestReachability(true);
		rc.addPointOfAccess(lcPoA);
		req.setContentObject(rc);
		
		log.debug("Send Create to remoteCSE:"+strUrl +","+ lcId);
		OneM2mResponse res = context.getNseManager().sendRequestMessage(strUrl, req);
		if (res != null && 
				(Utils.isSuccessResponse(res) || res.getResponseStatusCodeEnum() == RESPONSE_STATUS.CONFLICT)) {
			log.debug("Receive response of Create remoteCSE:"+res.toString());

			RemoteCSE remoteCse = new RemoteCSE();
			try {
				//String resName = this.createResourceName(RES_TYPE.REMOTE_CSE, CfgManager.getInstance().getCSEBaseName());
				String resName = remoteCseId.substring(1);
				remoteCse.setCSEID(remoteCseId);
				remoteCse.setCSEBase(remoteCseId);
				remoteCse.setCseType(CSE_TYPE.IN_CSE.Value());
				remoteCse.setRequestReachability(true);
				remoteCse.setResourceID(this.createResourceID(RES_TYPE.REMOTE_CSE, remoteCse, null));
				remoteCse.setResourceName(resName);
				remoteCse.setResourceUri(CfgManager.getInstance().getCSEBaseUri() +"/"+ resName);
				remoteCse.setUri(CfgManager.getInstance().getCSEBaseUri() +"/"+ resName);
				Iterator<String> it = remotePoAs.iterator();
				while (it.hasNext()) {
					remoteCse.addPointOfAccess(it.next());
				}
				dao.create(remoteCse);
			} catch (OneM2MException e) {
				log.debug("Fail to createremoteCSE to Database: " + remoteCse.toString(), e);
				return;
			}			
		} else {			
			log.debug("Fail to create remoteCSE to remote Host");
			if (res != null) {
				log.debug(res.toString());
			}
		}		
		
	}	
	
}
