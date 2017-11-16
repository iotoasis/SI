package net.herit.iot.onem2m.bind.ws.codec;

public class WSAbsCodec {
	protected final static String HTTP_HEADER_UPGRADE			= "Upgrade";
	protected final static String HTTP_HEADER_CONNECTION			= "Connection";
	protected final static String HTTP_HEADER_SEC_WEBSOCKET_PROTOCOL	= "Sec-WebSocket-Protocol";
	protected final static String HTTP_HEADER_SEC_WEBSOCKET_EXTENSIONS	= "Sec-WebSocket-Extensions";
	
	protected final static String ONEM2M_TY	= "ty";
	protected final static String ONEM2M_OPERATION = "op";
	protected final static String ONEM2M_TO = "to";
	protected final static String ONEM2M_REQUEST_IDENTIFIER = "rqi";
	protected final static String ONEM2M_CONTENT = "pc";
	protected final static String ONEM2M_FROM = "fr";
	protected final static String ONEM2M_RESPONSE_CODE	= "rsc";
}
