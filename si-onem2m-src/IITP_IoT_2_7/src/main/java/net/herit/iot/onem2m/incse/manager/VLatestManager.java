package net.herit.iot.onem2m.incse.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.message.onem2m.OneM2mRequest.RESOURCE_TYPE;
import net.herit.iot.message.onem2m.OneM2mResponse.RESPONSE_STATUS;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.incse.context.OneM2mContext;
import net.herit.iot.onem2m.incse.manager.VirtualManager.PROCESS_RESULT;
import net.herit.iot.onem2m.resource.ContentInstance;

public class VLatestManager implements VirtualManagerInterface {
	
	private Logger log = LoggerFactory.getLogger(VLatestManager.class);

	public final static String KEY = ContentInstance.LATEST_SHORTNAME;		// blocked in 2017-10-18
	//public final static String KEY = "latest";		// added in 2017-10-18 to support IUT			
	private final static VLatestManager INSTANCE = new VLatestManager();

	private PROCESS_RESULT processResult; 
	
	public PROCESS_RESULT getProcessResult() {
		return processResult;
	}
	
	public static VLatestManager getInstance() {
		return INSTANCE;
	}	

	//private static HashMap<String, VLatestManager> map = new HashMap<String, VLatestManager>();
	
	private VLatestManager() {
		//VirtualManager.getInstance().addManager(ContentInstance.LATEST_SHORTNAME, this);
	}
	
	public OneM2mResponse process(OneM2mRequest reqMessage, OneM2mContext context) throws OneM2MException {

		processResult = PROCESS_RESULT.PROCESSING;
		ContentInstanceManager manager = (ContentInstanceManager) ManagerFactory.create(RESOURCE_TYPE.CONTENT_INST, context);
		switch (reqMessage.getOperationEnum()) {
		case RETRIEVE:
			OneM2mResponse resMessage = manager.retrieveLatest(reqMessage);
			processResult = PROCESS_RESULT.COMPLETED;
			return resMessage;
		case DELETE:
			resMessage = manager.deleteLatest(reqMessage);
			processResult = PROCESS_RESULT.COMPLETED;
			return resMessage;
		case UPDATE:
		case CREATE:
		default:
			throw new OneM2MException(RESPONSE_STATUS.OPERATION_NOT_ALLOWED, "latest does not support:"+reqMessage.getOperationEnum().Name());
		}
		
		//context.getNseManager().sendResponseMessage(resMessage);
		//return PROCESS_RESULT.COMPLETED;

	};
	
	static {
		//VirtualManager.getInstance().addManager(KEY, VLatestManager.getInstance());
	}
}
