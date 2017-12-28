package net.herit.iot.onem2m.bind.api;

import net.herit.iot.message.onem2m.OneM2mResponse;

public interface ResponseListener {

	public void receiveResponse(OneM2mResponse resMessage);
	
	public void onError();
}
