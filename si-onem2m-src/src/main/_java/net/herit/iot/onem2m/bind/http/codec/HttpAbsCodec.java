package net.herit.iot.onem2m.bind.http.codec;

public abstract class HttpAbsCodec {
	protected final static String HTTP_HEADER_REQUESTID 				= "X-M2M-RI";
	protected final static String HTTP_HEADER_FROM 					= "X-M2M-ORIGIN";
	protected final static String HTTP_HEADER_ORIGIN_TIME 			= "X-M2M-OT";
	protected final static String HTTP_HEADER_RESULT_EXPIRETIME 	= "X-M2M-RST";
	protected final static String HTTP_HEADER_EVENT_CATEGORI 		= "X-M2M-EC";
	
	protected final static String HTTP_HEADER_CONTENT_TYPE			= "CONTENT-TYPE";
	protected final static String HTTP_HEADER_CONTENT_LENGTH			= "CONTENT-LENGTH";
}
