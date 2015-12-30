package net.herit.iot.onem2m.incse.manager;

import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.incse.context.OneM2mContext;
import net.herit.iot.onem2m.incse.manager.VirtualManager.PROCESS_RESULT;

public interface VirtualManagerInterface {
	
	//public PROCESS_RESULT process(OneM2mRequest request, OneM2mContext context) throws OneM2MException;
	public OneM2mResponse process(OneM2mRequest request, OneM2mContext context) throws OneM2MException;
	public PROCESS_RESULT getProcessResult();
	
}
