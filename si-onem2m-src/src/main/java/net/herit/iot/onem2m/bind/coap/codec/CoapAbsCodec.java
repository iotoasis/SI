package net.herit.iot.onem2m.bind.coap.codec;

import java.util.HashMap;
import java.util.Map;

import net.herit.iot.message.onem2m.format.Enums.CONTENT_TYPE;

public class CoapAbsCodec {
	public enum COAP_CONTENT_TYPE {
		NONE(0, CONTENT_TYPE.NONE),
		XML(41, CONTENT_TYPE.XML),
		JSON(50, CONTENT_TYPE.JSON),
		RES_XML(10000, CONTENT_TYPE.RES_XML),
		RES_JSON(10001, CONTENT_TYPE.RES_JSON),
		NTFY_XML(10002, CONTENT_TYPE.NTFY_XML),
		NTFY_JSON(10003, CONTENT_TYPE.NTFY_JSON),
		ATTRS_XML(10004, CONTENT_TYPE.ATTRS_XML),
		ATTRS_JSON(10005, CONTENT_TYPE.ATTRS_JSON); //,
		
		//Not supported.
//		PREQ_XML(10006, CONTENT_TYPE.PREQ_XML),
//		PREQ_JSON(10007, CONTENT_TYPE.PREQ_JSON),
//		PRSP_XML(10008, CONTENT_TYPE.PRSP_XML),
//		PRSP_JSON(10009, CONTENT_TYPE.PSRP_JSON);
		
	
		final int value;
		final CONTENT_TYPE type;
		private COAP_CONTENT_TYPE(int value, CONTENT_TYPE type) {
			this.value = value;
			this.type = type;
		}
		
		public int Value() {
			return this.value;
		}
		
		public CONTENT_TYPE Type() {
			return this.type;
		}
	
		private static final Map<CONTENT_TYPE, COAP_CONTENT_TYPE> map = 
				new HashMap<CONTENT_TYPE, COAP_CONTENT_TYPE>();
		private static final Map<Integer, CONTENT_TYPE> map2 =
				new HashMap<Integer, CONTENT_TYPE>();
		static {
			for(COAP_CONTENT_TYPE en : COAP_CONTENT_TYPE.values()) {
				map.put(en.type, en);
				map2.put(en.value, en.type);
			}
		}
		
		public static COAP_CONTENT_TYPE getCoapContentType(CONTENT_TYPE type) {
			COAP_CONTENT_TYPE en = map.get(type);
			if (en != null) return en;
			return NONE;
		}
		
		public static CONTENT_TYPE getContentType(int key) {
			CONTENT_TYPE type = map2.get(key);
			if(type != null) return type;
			return CONTENT_TYPE.NONE;
		}
	};
	
	
	public static final int ONEM2M_FR		= 256; // "OneM2M-From"
	public static final int ONEM2M_RQI		= 257; // "OneM2M-RequestIdentifier"
//	public static final int ONEM2M_NM		= 258;	// "OneM2M-Name"
	public	static final int ONEM2M_OT		= 259; // "OneM2M-OriginatingTimestamp"
	public static final int ONEM2M_RQET	= 260; // "OneM2M-RequestExpirationTimestamp"
	public static final int ONEM2M_RSET	= 261;	// "OneM2M-ResultExpirationTimestamp"
	public static final int ONEM2M_OET		= 262; // "OneM2M-OperationExecutionTime"
	public static final int ONEM2M_RTURI	= 263; // "OneM2M-notificationURIofResponseType"
	public static final int ONEM2M_EC		= 264;	// "OneM2M-EventCategory"
	public static final int ONEM2M_RSC		= 265; // "OneM2M-ResponseStatusCode"
	public static final int ONEM2M_GID		= 266; // "OneM2M-GroupRequestIdentifier"
	public static final int ONEM2M_TY		= 267; // "OneM2M-ResourceType"
	
	
		
}
