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
import net.herit.iot.onem2m.incse.facility.QosManager;
import net.herit.iot.onem2m.incse.manager.dao.DAOInterface;
import net.herit.iot.onem2m.incse.manager.dao.RemoteCSEAnncDAO;
import net.herit.iot.onem2m.resource.RemoteCSEAnnc;
import net.herit.iot.onem2m.resource.Resource;


public class RemoteCSEAnncManager extends AbsManager {
	
	static String ALLOWED_PARENT = "CSEBase";  
	static RESOURCE_TYPE RES_TYPE = RESOURCE_TYPE.REMOTE_CSE_ANNC; 
	
	private Logger log = LoggerFactory.getLogger(RemoteCSEAnncManager.class);

	private static final String TAG = RemoteCSEAnncManager.class.getName();

	public RemoteCSEAnncManager(OneM2mContext context) {
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
		
		//return update(reqMessage, this);
		// added in 2017-10-25
		update(reqMessage, this);
		
		String to = reqMessage.getTo();
		RemoteCSEAnncDAO dao = (RemoteCSEAnncDAO)this.getDAO();
		RemoteCSEAnnc res = (RemoteCSEAnnc)dao.retrieve(to,  null);
		if (res == null) {
			throw new OneM2MException(RESPONSE_STATUS.NOT_FOUND, "Not found");	
		}
		
		OneM2mResponse resMessage = new OneM2mResponse(RESPONSE_STATUS.CHANGED, reqMessage);
		setResponseContentObjectForRetrieve(resMessage, reqMessage.getResultContentEnum(), res, this);
		
		return resMessage;
		
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
	public void notification(Resource parentRes, Resource reqRes, Resource orgRes, OPERATION op) throws OneM2MException {
		Document reqDoc = reqRes == null? null : Document.parse( ((RemoteCSEAnncDAO)getDAO()).resourceToJson(reqRes));
		Document orgDoc = orgRes == null? null : Document.parse( ((RemoteCSEAnncDAO)getDAO()).resourceToJson(orgRes));
		NotificationController.getInstance().notify(parentRes, reqDoc, reqRes, orgDoc, orgRes, op, this);
	}
	
	@Override
	public DAOInterface getDAO() {
		return new RemoteCSEAnncDAO(context);
	}

	@Override
	public Class<?> getResourceClass() {
		return RemoteCSEAnnc.class;
	}


	@Override
	public XMLConvertor<?> getXMLConveter() throws OneM2MException {
		return ConvertorFactory.getXMLConvertor(RemoteCSEAnnc.class, RemoteCSEAnnc.SCHEMA_LOCATION);
	}

	@Override
	public JSONConvertor<?> getJSONConveter() throws OneM2MException {
		return ConvertorFactory.getJSONConvertor(RemoteCSEAnnc.class, RemoteCSEAnnc.SCHEMA_LOCATION);
	}

}
