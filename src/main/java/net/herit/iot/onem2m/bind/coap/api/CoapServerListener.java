package net.herit.iot.onem2m.bind.coap.api;

import org.eclipse.californium.core.server.resources.CoapExchange;

import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.onem2m.core.util.OneM2MException;

public interface CoapServerListener {

	public void receiveCoapRequest(CoapExchange exchange);

	public boolean sendCoapResponse(OneM2mResponse resMessage) throws OneM2MException;
}
