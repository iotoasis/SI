package net.herit.iot.onem2m.incse.manager;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.message.onem2m.OneM2mRequest.OPERATION;
import net.herit.iot.message.onem2m.OneM2mRequest.RESOURCE_TYPE;
import net.herit.iot.message.onem2m.OneM2mResponse.RESPONSE_STATUS;
import net.herit.iot.onem2m.core.convertor.ConvertorFactory;
import net.herit.iot.onem2m.core.convertor.JSONConvertor;
import net.herit.iot.onem2m.core.convertor.XMLConvertor;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.incse.context.OneM2mContext;
import net.herit.iot.onem2m.incse.controller.NotificationController;
import net.herit.iot.onem2m.incse.facility.OneM2mUtil;
import net.herit.iot.onem2m.incse.facility.QosManager;
import net.herit.iot.onem2m.incse.facility.SeqNumManager;
import net.herit.iot.onem2m.incse.manager.dao.AEAnncDAO;
import net.herit.iot.onem2m.incse.manager.dao.DAOInterface;
import net.herit.iot.onem2m.incse.manager.dao.ResourceDAO;
import net.herit.iot.onem2m.resource.AEAnnc;
import net.herit.iot.onem2m.resource.Resource;


public class AEAnncManager extends AbsManager {
	
	static String ALLOWED_PARENT = "remoteCSEAnnc";   
	static RESOURCE_TYPE RES_TYPE = RESOURCE_TYPE.AE_ANNC; 

	private Logger log = LoggerFactory.getLogger(AEAnncManager.class);
	
	public AEAnncManager(OneM2mContext context) {
		super(RES_TYPE, ALLOWED_PARENT);
		this.context = context;
	}
	
	@Override
	public OneM2mResponse create(OneM2mRequest reqMessage) throws OneM2MException {

		QosManager qm = QosManager.getInstance(); 
		if (qm.checkNAddAENumber(1)) {
			try {
				return createAnnc(reqMessage, this);
			} catch (OneM2MException e) {

				qm.reduceAENumber(1);
				throw e;
			}
		} else {
			throw new OneM2MException(RESPONSE_STATUS.MAX_NUMBER_OF_MEMBER_EXCEEDED, 
					"AE Quota already fulled!!! ("+qm.getMaxAENumber()+")");
		}
		
	}
	
//	@Override
//	public String createResourceID(RESOURCE_TYPE type, Resource res, OneM2mRequest req) throws OneM2MException {
//		String from = req.getFrom();
//		if (from == null || from.length() == 0) {
//			//return RESID_PREFIX + "SAE_"+UUID.randomUUID().toString();
//			return "SAE_"+SeqNumManager.getInstance().get(RESOURCE_TYPE.AE.Name());
//		} else if (from.equals("S")) {
//			//return RESID_PREFIX + "SAE_"+UUID.randomUUID().toString();
//			return "SAE_"+SeqNumManager.getInstance().get(RESOURCE_TYPE.AE.Name());
//		} else if (from.equals("C")) {
//			//return RESID_PREFIX + "CAE_"+UUID.randomUUID().toString();
//			return "CAE_"+SeqNumManager.getInstance().get(RESOURCE_TYPE.AE.Name());
//		} else if (from.startsWith("S") || from.startsWith("C")){
//			String resId = OneM2mUtil.extractResourceId(from);
//			ResourceDAO dao = this.getCommonDAO();
//			if (dao.checkIfResourceExist(resId)) {
//				throw new OneM2MException(RESPONSE_STATUS.CONFLICT, "Resource already exist:"+from);
//			}
//			return resId;
//		} else {
//			throw new OneM2MException(RESPONSE_STATUS.BAD_REQUEST, "Invalid AE-ID(From):"+from);
//		}
//	}	


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
		QosManager.getInstance().reduceAENumber(1);
		return res;
	
	}


	@Override
	public OneM2mResponse notify(OneM2mRequest reqMessage) throws OneM2MException {
		// TODO Auto-generated method stub

		return new OneM2mResponse();
	}
	
	@Override 
	public void notification(Resource parentRes, Resource reqRes, Resource orgRes, OPERATION op) throws OneM2MException {
		Document reqDoc = reqRes == null? null : Document.parse( ((AEAnncDAO)getDAO()).resourceToJson(reqRes));
		Document orgDoc = orgRes == null? null : Document.parse( ((AEAnncDAO)getDAO()).resourceToJson(orgRes));
		NotificationController.getInstance().notify(parentRes, reqDoc, reqRes, orgDoc, orgRes, op, this);
	}
	

	@Override
	public DAOInterface getDAO() {
		return new AEAnncDAO(context);
	}

	@Override
	public Class<?> getResourceClass() {
		return AEAnnc.class;
	}

	@Override
	public void updateResource(Resource resource, OneM2mRequest req) throws OneM2MException {
		
		// update link attribute using ae id		 
		AEAnnc aea = (AEAnnc)resource;
		
		String resId = aea.getResourceID();
		
		if (req.getFrom().equals("S")) {
			// create AE-ID-Stem
			String aeid = this.createResourceID(RES_TYPE.AE, resource, req);
			String link = aea.getLink();
			link = link.concat(aeid);
			aea.setLink(link);
		}
		
	}

	@Override
	public XMLConvertor<?> getXMLConveter() throws OneM2MException {
		return ConvertorFactory.getXMLConvertor(AEAnnc.class, AEAnnc.SCHEMA_LOCATION);
	}

	@Override
	public JSONConvertor<?> getJSONConveter() throws OneM2MException {
		return ConvertorFactory.getJSONConvertor(AEAnnc.class, AEAnnc.SCHEMA_LOCATION);
	}


}
