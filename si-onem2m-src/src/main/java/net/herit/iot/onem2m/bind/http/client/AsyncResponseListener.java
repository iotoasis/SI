package net.herit.iot.onem2m.bind.http.client;

import net.herit.iot.message.onem2m.OneM2mResponse;

public interface AsyncResponseListener {
	public void asyncResponse(OneM2mResponse resMessage);
}
