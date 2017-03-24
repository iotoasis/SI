package net.herit.iot.onem2m.bind.ws.api;

public interface WebSocketClientListener {
	public void receiveWebSocketResponse(byte[] response);
	
	public void onError();
}
