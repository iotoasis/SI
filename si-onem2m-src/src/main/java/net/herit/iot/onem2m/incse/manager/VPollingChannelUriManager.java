package net.herit.iot.onem2m.incse.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.incse.LongPollingManager;
import net.herit.iot.onem2m.incse.context.OneM2mContext;
import net.herit.iot.onem2m.incse.manager.VirtualManager.PROCESS_RESULT;
import net.herit.iot.onem2m.resource.Naming;

public class VPollingChannelUriManager implements VirtualManagerInterface {
	
	private Logger log = LoggerFactory.getLogger(VPollingChannelUriManager.class);

	public static String KEY = Naming.POLLINGCHANNELURI_SN;
	private final static VPollingChannelUriManager INSTANCE = new VPollingChannelUriManager();
	
	private PROCESS_RESULT processResult; 
	
	public PROCESS_RESULT getProcessResult() {
		return processResult;
	}
	
	public static VPollingChannelUriManager getInstance() {
		return INSTANCE;
	}

	//private static HashMap<String, VPollingChannelUriManager> map = new HashMap<String, VPollingChannelUriManager>();
	
	private VPollingChannelUriManager() {
		
		//VirtualManager.getInstance().addManager(PollingChannel.POLLINGCHANNELURI_SHORTNAME, this);
		
	}
	
	public OneM2mResponse process(OneM2mRequest reqMessage, OneM2mContext context) throws OneM2MException {
		processResult = PROCESS_RESULT.PROCESSING;

		LongPollingManager.getInstance().newAccessPoint(reqMessage);
		processResult = PROCESS_RESULT.COMPLETED;
		return null;
		//return PROCESS_RESULT.COMPLETED;
	};
}
