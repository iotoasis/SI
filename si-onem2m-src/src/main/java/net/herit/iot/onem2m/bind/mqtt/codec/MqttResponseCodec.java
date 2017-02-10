package net.herit.iot.onem2m.bind.mqtt.codec;

import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.message.onem2m.format.Enums.CONTENT_TYPE;
import net.herit.iot.onem2m.bind.mqtt.util.Utils;
import net.herit.iot.onem2m.core.convertor.JSONConvertor;
import net.herit.iot.onem2m.core.convertor.XMLConvertor;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.resource.AE;
import net.herit.iot.onem2m.resource.ResponsePrimitive;

public class MqttResponseCodec {
	
	private static final XMLConvertor<OneM2mResponse> xmlConvertor = 
			new XMLConvertor<OneM2mResponse>(OneM2mResponse.class, OneM2mResponse.SCHEMA_LOCATION);
	private static final JSONConvertor<OneM2mResponse> decodeJsonConvertor =
			new JSONConvertor<OneM2mResponse>(OneM2mResponse.class);
	private static final JSONConvertor<ResponsePrimitive> encodeJsonConvertor =
			new JSONConvertor<ResponsePrimitive>(ResponsePrimitive.class);

	public static OneM2mResponse decode(byte[] contents) throws OneM2MException {

		OneM2mResponse resMessage = null;
		CONTENT_TYPE contentType = CONTENT_TYPE.NONE;
		
		try {
			String strContents = new String(contents, "UTF-8").trim();

			contentType = Utils.getContentType(strContents);

			switch (contentType) {
			case XML:
			case ATTRS_XML:
			case NTFY_XML:
			case RES_XML:
				resMessage = (OneM2mResponse)xmlConvertor.unmarshal(strContents);
				break;
			case JSON:
			case ATTRS_JSON:
			case NTFY_JSON:
			case RES_JSON:
				resMessage = (OneM2mResponse)decodeJsonConvertor.unmarshal(strContents);
				break;
			default:
				throw new OneM2MException(OneM2mResponse.RESPONSE_STATUS.INVALID_ARGUMENTS, "Invalid format");
			}
			
//			if(contentType == CONTENT_TYPE.XML) {
//				resMessage = (OneM2mResponse)decodeXmlConvertor.unmarshal(strContents);
//			} else if(contentType == CONTENT_TYPE.JSON) {
//				resMessage = (OneM2mResponse)decodeJsonConvertor.unmarshal(strContents);
//			} else {
//				throw new OneM2MException(OneM2mResponse.RESPONSE_STATUS.INVALID_ARGUMENTS, "Invalid format");
//			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			throw new OneM2MException(OneM2mResponse.RESPONSE_STATUS.INVALID_ARGUMENTS, "Invalid format");
		}
		
		resMessage.setContentType(contentType);
		
		return resMessage;

	}

	public static byte[] encode(OneM2mResponse resMessage) throws OneM2MException {

		String strContents = null;
		CONTENT_TYPE contentType = resMessage.getContentType();

		ResponsePrimitive resPrimitive = (ResponsePrimitive)resMessage;
		
	/*  updated as below due to CDT-2.7.0
	 * 	if(resPrimitive.getPrimitiveContent() != null && resPrimitive.getPrimitiveContent().getAny().size() > 0) {
			System.out.println("########## PrimitiveContent exist...");
			if (resPrimitive.getPrimitiveContent().getAny().get(0) instanceof net.herit.iot.onem2m.resource.AE) {
				System.out.println(((net.herit.iot.onem2m.resource.AE)resPrimitive.getPrimitiveContent().getAny().get(0)).getAEID());
			} else {
				System.out.println("failed casting to AE..");
			}
		}  */
		if(resPrimitive.getPrimitiveContent() != null && resPrimitive.getPrimitiveContent().getAnyOrAny().size() > 0) {
			System.out.println("########## PrimitiveContent exist...");
			if (resPrimitive.getPrimitiveContent().getAnyOrAny().get(0) instanceof net.herit.iot.onem2m.resource.AE) {
				System.out.println(((net.herit.iot.onem2m.resource.AE)resPrimitive.getPrimitiveContent().getAnyOrAny().get(0)).getAEID());
			} else {
				System.out.println("failed casting to AE..");
			}
		}
		else
			System.out.println("########## PrimitiveContent none exist...");
		
		
		try {

			switch (contentType) {
			case XML:
			case ATTRS_XML:
			case NTFY_XML:
			case RES_XML:
				strContents = xmlConvertor.marshal(resMessage);
				break;
				
			case JSON:
			case ATTRS_JSON:
			case NTFY_JSON:
			case RES_JSON:
				strContents = encodeJsonConvertor.marshal(resPrimitive);
				break;
			
			default:
				throw new OneM2MException(OneM2mResponse.RESPONSE_STATUS.INVALID_ARGUMENTS, "Invalid format");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			throw new OneM2MException(OneM2mResponse.RESPONSE_STATUS.INVALID_ARGUMENTS, "Invalid format");
		}
		
		return strContents.getBytes();
	}
}
