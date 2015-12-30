package net.herit.iot.onem2m.incse.controller;

import java.net.MalformedURLException;

import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.onem2m.bind.http.client.AsyncResponseListener;
import net.herit.iot.onem2m.core.util.Utils;
import net.herit.iot.onem2m.incse.context.OneM2mContext;
import net.herit.iot.onem2m.incse.facility.OneM2mUtil;


public class ForwardingController extends AbsController implements AsyncResponseListener {
	
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
			reqMessage.setTo(resPath);
			return getContext().getNseManager().sendRequestMessage(baseUrl, reqMessage);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean processAsyncRequest(OneM2mRequest reqMessage) {
		return getContext().getNseManager().sendAsyncRequestMessage(reqMessage.getTo(), reqMessage, this);
	}

	@Override
	public void asyncResponse(OneM2mResponse resMessage) {
		getContext().getLogManager().debug(ForwardingController.class.getName() + ": asyncResponse");
		getContext().getLogManager().debug(resMessage.toString());
		
	}

}
