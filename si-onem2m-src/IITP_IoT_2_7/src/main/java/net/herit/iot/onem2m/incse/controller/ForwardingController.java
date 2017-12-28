package net.herit.iot.onem2m.incse.controller;

import java.net.MalformedURLException;

//import org.eclipse.californium.core.CoapResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.onem2m.bind.api.ResponseListener;
//import net.herit.iot.onem2m.bind.http.client.AsyncResponseListener;
import net.herit.iot.onem2m.core.util.Utils;
import net.herit.iot.onem2m.incse.context.OneM2mContext;
//import net.herit.iot.onem2m.incse.facility.OneM2mUtil;


public class ForwardingController extends AbsController implements ResponseListener {

	private Logger log = LoggerFactory.getLogger(ForwardingController.class);
	
	public ForwardingController(OneM2mContext context) {
		super(context);
	}

	private void makeReceiverAddress() {
		
	}

	@Override
	public OneM2mResponse processRequest(OneM2mRequest reqMessage) {	
		
		try {
			String baseUrl = Utils.extractBaseurlFromUrl(reqMessage.getTo());
			String resPath = Utils.extractResourceFromUrl(reqMessage.getTo());
			log.debug("baseUrl: {}, resPath: {}", baseUrl, resPath);
			
			reqMessage.setTo("/~" + resPath);		// updated at 2016-12-15
			
			return getContext().getNseManager().sendRequestMessage(baseUrl, reqMessage);
		} catch (MalformedURLException e) {
			log.debug("Handled exception", e);
			return null;
		}
	}

	@Override
	public boolean processAsyncRequest(OneM2mRequest reqMessage) {
		return getContext().getNseManager().sendAsyncRequestMessage(this, reqMessage.getTo(), reqMessage);
	}

//	@Override
//	public void asyncResponse(OneM2mResponse resMessage) {
//		getContext().getLogManager().debug(ForwardingController.class.getName() + ": asyncResponse");
//		getContext().getLogManager().debug(resMessage.toString());
//		
//	}

	@Override
	public void receiveResponse(OneM2mResponse resMessage) {
		getContext().getLogManager().debug(ForwardingController.class.getName() + ": asyncResponse");
		getContext().getLogManager().debug(resMessage.toString());
	}

	@Override
	public void onError() {
		// TODO Auto-generated method stub
		
	}

}
