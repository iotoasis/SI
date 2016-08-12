package net.herit.iot.onem2m.incse.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.message.onem2m.OneM2mRequest.RESOURCE_TYPE;
import net.herit.iot.onem2m.core.convertor.ConvertorFactory;
import net.herit.iot.onem2m.core.convertor.JSONConvertor;
import net.herit.iot.onem2m.core.convertor.XMLConvertor;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.incse.LongPollingManager;
import net.herit.iot.onem2m.incse.context.OneM2mContext;
import net.herit.iot.onem2m.incse.facility.CfgManager;
import net.herit.iot.onem2m.incse.manager.dao.DAOInterface;
import net.herit.iot.onem2m.incse.manager.dao.PollingChannelDAO;
import net.herit.iot.onem2m.resource.Naming;
import net.herit.iot.onem2m.resource.PollingChannel;
import net.herit.iot.onem2m.resource.Resource;

public class PollingChannelManager extends AbsManager {

//	public static final String POLLINGCHANNEL_URI = "pcu";
	
	static String ALLOWED_PARENT = "remoteCSE,AE";	// TS-0001 9.6.1.1 Resource Type Summary
	static RESOURCE_TYPE RES_TYPE = RESOURCE_TYPE.POLLING_CHANN;
	
	private Logger log = LoggerFactory.getLogger(PollingChannelManager.class);
	
	private static final String TAG = PollingChannelManager.class.getName();

	public PollingChannelManager(OneM2mContext context) {
		super(RES_TYPE, ALLOWED_PARENT);
		this.context = context;
	}
	
	
	@Override
	public OneM2mResponse create(OneM2mRequest reqMessage)	throws OneM2MException {

		return create(reqMessage, this);

//		String resourceID = createResourceID(reqMessage.getResourceType());
//		
//		context.getLogManager().debug("reqMessage.getTo(): " + reqMessage.getTo());
//		
//		Resource parent = this.getResourceFromDB(reqMessage.getTo());
//		String name = reqMessage.getName();
//		if(name == null) {
//			name = createResourceName(reqMessage.getResourceType(), parent.getResourceID());
//		}
//		
//		PollingChannel pollingChann = (PollingChannel)this.getContentResource(reqMessage, 
//								PollingChannel.class, 
//								new XMLConvertor<PollingChannel>(PollingChannel.class));
//
//		
//		pollingChann.validate(OPERATION.CREATE);
//		
//		pollingChann.setResourceName(name);
//		pollingChann.setUri(parent.getUri() +"/"+ name);
//		pollingChann.setResourceType(reqMessage.getResourceType().Value());
//		pollingChann.setResourceID(resourceID);
//		pollingChann.setParentID(parent.getResourceID());
//		pollingChann.setPollingChannelURI(pollingChann.getUri() + "/" + PollingChannel.POLLINGCHANNELURI_SHORTNAME);
//
//		context.getLogManager().debug("pollingChannel: \n\r" + pollingChann.toString());
//		
//		PollingChannelDAO dao = new PollingChannelDAO(this.context);
//		dao.create(pollingChann);
//		
//		//ae.validate(null);
//
//		OneM2mResponse resMessage = new OneM2mResponse(RESPONSE_STATUS.ACCEPTED, reqMessage);
//		resMessage.setContentObject(pollingChann);
//
//		return resMessage;

	}

	@Override
	public OneM2mResponse retrieve(OneM2mRequest reqMessage)
			throws OneM2MException {

		return retrieve(reqMessage, this);
		
	}

	@Override
	public OneM2mResponse update(OneM2mRequest reqMessage)
			throws OneM2MException {

		return update(reqMessage, this);
	}

	@Override
	public OneM2mResponse delete(OneM2mRequest reqMessage)
			throws OneM2MException {

		OneM2mResponse resMessage = delete(reqMessage, this);
		
		LongPollingManager
				.getInstance()
				.removePollingChannelExpireTask(reqMessage.getTo());
		
		return resMessage;
	}
	
	@Override
	public OneM2mResponse notify(OneM2mRequest reqMessage)
			throws OneM2MException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public DAOInterface getDAO() {
		return new PollingChannelDAO(context);
	}

	@Override
	public Class<?> getResourceClass() {
		return PollingChannel.class;
	}

	@Override
	public void updateResource(Resource resource, OneM2mRequest req) throws OneM2MException {
		PollingChannel pollingChann = (PollingChannel)resource;
		
		pollingChann.setPollingChannelURI(pollingChann.getUri() + "/" + Naming.POLLINGCHANNELURI_SN);

		//// TS-0001-XXX-V1_13_1 - 10.1.1.1	Non-registration related CREATE procedure (ExpirationTime..)
		if(pollingChann.getExpirationTime() == null) {
			pollingChann.setExpirationTime(CfgManager.getInstance().getDefaultExpirationTime());
		}
		// END - Non-registration related CREATE procedure		

		if(pollingChann.getExpirationTime() != null) {
			LongPollingManager
					.getInstance()
					.addPollingChannelExpireTask(pollingChann.getUri(), pollingChann.getExpirationTime());
		}
	}

	@Override
	public XMLConvertor<?> getXMLConveter() throws OneM2MException {
		return ConvertorFactory.getXMLConvertor(PollingChannel.class, PollingChannel.SCHEMA_LOCATION);
	}

	@Override
	public JSONConvertor<?> getJSONConveter() throws OneM2MException {
		return ConvertorFactory.getJSONConvertor(PollingChannel.class, PollingChannel.SCHEMA_LOCATION);
	}
}
