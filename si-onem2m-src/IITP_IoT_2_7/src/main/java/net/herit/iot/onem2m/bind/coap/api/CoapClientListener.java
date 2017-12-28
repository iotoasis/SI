package net.herit.iot.onem2m.bind.coap.api;

import org.eclipse.californium.core.CoapResponse;

public interface CoapClientListener {

	public void receiveCoapResponse(CoapResponse response);
	
	public void onError();
}
