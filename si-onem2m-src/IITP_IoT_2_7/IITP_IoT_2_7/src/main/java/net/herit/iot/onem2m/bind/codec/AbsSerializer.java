package net.herit.iot.onem2m.bind.codec;

import net.herit.iot.message.onem2m.format.Enums.*;

public abstract class AbsSerializer {

	private static JSONSerializer json = null;
	private static XMLSerializer xml = null;
	
	public static AbsSerializer getSerializer(CONTENT_TYPE format) {
		
		if (format == null) {
			if (xml == null) {
				xml = new XMLSerializer();
			}
			return xml;			
		}
		
		switch(format) {
		case JSON:
		case RES_JSON:
		case NTFY_JSON:
		case ATTRS_JSON:
		case CBOR:				// added in 2017-05-22
		case RES_CBOR:			// added in 2017-05-22
		case NTFY_CBOR:			// added in 2017-05-22
			
			if (json == null) {
				json = new JSONSerializer();
			}
			return json;

		case XML:
			if (xml == null) {
				xml = new XMLSerializer();
			}
			return xml;

		default:
			if (xml == null) {
				xml = new XMLSerializer();
			}
			return xml;
		}
		
//		if (format.indexOf("JSON") >= 0) {
//			if (json == null) {
//				json = new JSONSerializer();
//			}
//			return json;
//		} else {
//			if (xml == null) {
//				xml = new XMLSerializer();
//			}
//			return xml;
//		}
	}
	
	public abstract String serialize(Object content) throws Exception;
}
