package net.herit.iot.onem2m.incse.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.message.onem2m.OneM2mRequest.RESOURCE_TYPE;
import net.herit.iot.message.onem2m.OneM2mRequest.RESULT_CONT;
import net.herit.iot.message.onem2m.OneM2mResponse.RESPONSE_STATUS;
import net.herit.iot.onem2m.core.convertor.ConvertorFactory;
import net.herit.iot.onem2m.core.convertor.JSONConvertor;
import net.herit.iot.onem2m.core.convertor.XMLConvertor;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.incse.context.OneM2mContext;
import net.herit.iot.onem2m.incse.controller.AnnounceController;
import net.herit.iot.onem2m.incse.controller.dm.DMControllerFactory;
import net.herit.iot.onem2m.incse.facility.CfgManager;
import net.herit.iot.onem2m.incse.manager.dao.DAOInterface;
import net.herit.iot.onem2m.incse.manager.dao.MgmtFirmwareDAO;
import net.herit.iot.onem2m.resource.Firmware;
import net.herit.iot.onem2m.resource.Resource;


public class MgmtFirmwareManager extends AbsManager {
	
	static String ALLOWED_PARENT = "node";  
	static RESOURCE_TYPE RES_TYPE = RESOURCE_TYPE.MGMT_OBJ; 
	
	private Logger log = LoggerFactory.getLogger(MgmtFirmwareManager.class);

	private static final String TAG = MgmtFirmwareManager.class.getName();

	public MgmtFirmwareManager(OneM2mContext context) {
		super(RES_TYPE, ALLOWED_PARENT);
		this.context = context;
	}
	
	@Override
	public OneM2mResponse create(OneM2mRequest reqMessage) throws OneM2MException {
		
		return create(reqMessage, this);
		
	}

	@Override
	public OneM2mResponse retrieve(OneM2mRequest reqMessage) throws OneM2MException {
		
		Firmware firmware = (Firmware)this.retrieveResource(reqMessage, this);
		
		//firmware = (Firmware)DMControllerFactory.getController(firmware).getStatus(firmware);
		firmware = (Firmware)DMControllerFactory.getController(firmware, context).getStatus(firmware);	//updated in 2017-08-23
		
		OneM2mResponse resMessage = new OneM2mResponse(RESPONSE_STATUS.OK, reqMessage);
		setResponseContentObjectForRetrieve(resMessage, reqMessage.getResultContentEnum(), firmware, this);
				
		return resMessage;
			
	}

	@Override
	public OneM2mResponse update(OneM2mRequest reqMessage) throws OneM2MException {
		
		Firmware resInDB = (Firmware)this.getDAO().retrieve(reqMessage.getTo(), RESULT_CONT.ATTRIBUTE);
		if (resInDB == null) {
			throw new OneM2MException(RESPONSE_STATUS.NOT_FOUND, "No resource found:"+reqMessage.getTo());
		}
		
		Firmware firmware = (Firmware)getContentResource(reqMessage, this);
		System.out.println("###### firmware =" + firmware.isUpdate());
		//DMControllerFactory.getController(firmware).executeMgmtObj(resInDB, firmware);
		//DMControllerFactory.getController(firmware, context).executeMgmtObj(resInDB, firmware);	// updated in 2017-08-23
		DMControllerFactory.getController(resInDB, context).executeMgmtObj(resInDB, firmware);	// updated in 2017-09-16
		
		return updateResource(firmware, reqMessage, this);
		
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
		return new MgmtFirmwareDAO(context);
	}

	@Override
	public Class<?> getResourceClass() {
		return MgmtFirmwareManager.class;
	}

	@Override
	public XMLConvertor<?> getXMLConveter() throws OneM2MException {
		return ConvertorFactory.getXMLConvertor(Firmware.class, Firmware.SCHEMA_LOCATION);
	}

	@Override
	public JSONConvertor<?> getJSONConveter() throws OneM2MException {
		return ConvertorFactory.getJSONConvertor(Firmware.class, Firmware.SCHEMA_LOCATION);
	}
	
	
	@Override
	public void updateResource(Resource resource, OneM2mRequest req) throws OneM2MException {
		Firmware res =  (Firmware)resource;
		
		//// TS-0001-XXX-V1_13_1 - 10.1.1.1	Non-registration related CREATE procedure (ExpirationTime..)
		if(res.getExpirationTime() == null) {
			res.setExpirationTime(CfgManager.getInstance().getDefaultExpirationTime());
		}
		// END - Non-registration related CREATE procedure
	}
}
