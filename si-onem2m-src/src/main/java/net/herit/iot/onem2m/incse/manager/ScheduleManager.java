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
import net.herit.iot.onem2m.incse.controller.AnnounceController;
import net.herit.iot.onem2m.incse.controller.NotificationController;
import net.herit.iot.onem2m.incse.facility.CfgManager;
import net.herit.iot.onem2m.incse.facility.OneM2mUtil;
import net.herit.iot.onem2m.incse.manager.dao.DAOInterface;
import net.herit.iot.onem2m.incse.manager.dao.ScheduleDAO;
import net.herit.iot.onem2m.resource.Resource;
import net.herit.iot.onem2m.resource.Schedule;


public class ScheduleManager extends AbsManager {
	
	static String ALLOWED_PARENT = "subscription,mgmtObj,CSEBase,remoteCSE,AE";  	
	static RESOURCE_TYPE RES_TYPE = RESOURCE_TYPE.SCHEDULE; 
	
	private Logger log = LoggerFactory.getLogger(ScheduleManager.class);

	private static final String TAG = ScheduleManager.class.getName();

	public ScheduleManager(OneM2mContext context) {
		super(RES_TYPE, ALLOWED_PARENT);
		this.context = context;
	}
	
	@Override
	public OneM2mResponse create(OneM2mRequest reqMessage) throws OneM2MException {
		throw new OneM2MException(RESPONSE_STATUS.NOT_IMPLEMENTED, "Not implemented!!!");

		//return create(reqMessage, this);
		
	}

	@Override
	public OneM2mResponse retrieve(OneM2mRequest reqMessage) throws OneM2MException {
		throw new OneM2MException(RESPONSE_STATUS.NOT_IMPLEMENTED, "Not implemented!!!");

		//return retrieve(reqMessage, this);
			
	}

	@Override
	public OneM2mResponse update(OneM2mRequest reqMessage) throws OneM2MException {
		throw new OneM2MException(RESPONSE_STATUS.NOT_IMPLEMENTED, "Not implemented!!!");

		//return update(reqMessage, this);
		
	}

	@Override
	public OneM2mResponse delete(OneM2mRequest reqMessage) throws OneM2MException {
		throw new OneM2MException(RESPONSE_STATUS.NOT_IMPLEMENTED, "Not implemented!!!");

		//return delete(reqMessage, this);
	
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
		Document reqDoc = reqRes == null? null : Document.parse( ((ScheduleDAO)getDAO()).resourceToJson(reqRes));
		Document orgDoc = orgRes == null? null : Document.parse( ((ScheduleDAO)getDAO()).resourceToJson(orgRes));
		NotificationController.getInstance().notify(parentRes, reqDoc, reqRes, orgDoc, orgRes, op, this);
	}
	
	@Override
	public DAOInterface getDAO() {
		return new ScheduleDAO(context);
	}

	@Override
	public Class<?> getResourceClass() {
		return Schedule.class;
	}

	@Override
	public XMLConvertor<?> getXMLConveter() throws OneM2MException {
		return ConvertorFactory.getXMLConvertor(Schedule.class, Schedule.SCHEMA_LOCATION);
	}

	@Override
	public JSONConvertor<?> getJSONConveter() throws OneM2MException {
		return ConvertorFactory.getJSONConvertor(Schedule.class, Schedule.SCHEMA_LOCATION);
	}
	
	@Override
	public boolean checkAccessControlPolicy(OneM2mRequest req, OPERATION op, Resource res, Resource parent) throws OneM2MException {
				
		return OneM2mUtil.checkAccessControlPolicy(req, op, OneM2mUtil.extractAccessControlPolicies(parent, context));		
	}

	@Override
	public void updateResource(Resource resource, OneM2mRequest req) throws OneM2MException {
		Schedule res =  (Schedule)resource;
		
		//// TS-0001-XXX-V1_13_1 - 10.1.1.1	Non-registration related CREATE procedure (ExpirationTime..)
		if(res.getExpirationTime() == null) {
			res.setExpirationTime(CfgManager.getInstance().getDefaultExpirationTime());
		}
		// END - Non-registration related CREATE procedure
	}
}
