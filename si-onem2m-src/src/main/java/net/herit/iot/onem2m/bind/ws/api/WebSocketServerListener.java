package net.herit.iot.onem2m.bind.ws.api;


import org.java_websocket.WebSocket;

import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.onem2m.core.util.OneM2MException;

public interface WebSocketServerListener {
	
	public void receiveWebSocketRequest(byte[] message, WebSocket ws);

	public boolean sendWebSocketResponse(OneM2mResponse resMessage) throws OneM2MException;
}
