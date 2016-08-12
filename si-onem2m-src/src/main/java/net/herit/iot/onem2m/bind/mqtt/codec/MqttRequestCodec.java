package net.herit.iot.onem2m.bind.mqtt.codec;

import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.message.onem2m.OneM2mRequest.OPERATION;
import net.herit.iot.message.onem2m.OneM2mRequest.RESULT_CONT;
import net.herit.iot.message.onem2m.format.Enums.CONTENT_TYPE;
import net.herit.iot.onem2m.core.convertor.JSONConvertor;
import net.herit.iot.onem2m.core.convertor.XMLConvertor;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.resource.RequestPrimitive;
import net.herit.iot.onem2m.bind.mqtt.util.Utils;

public class MqttRequestCodec {
	
	private static final XMLConvertor<OneM2mRequest> xmlConvertor = 
			new XMLConvertor<OneM2mRequest>(OneM2mRequest.class, OneM2mRequest.SCHEMA_LOCATION);
	private static final JSONConvertor<OneM2mRequest> decodeJsonConvertor =
			new JSONConvertor<OneM2mRequest>(OneM2mRequest.class);
	private static final JSONConvertor<RequestPrimitive> encodeJsonConvertor =
			new JSONConvertor<RequestPrimitive>(RequestPrimitive.class);

	public static OneM2mRequest decode(byte[] contents) throws OneM2MException {

		OneM2mRequest reqMessage = null;
		CONTENT_TYPE contentType = CONTENT_TYPE.NONE;
		
		try {
			String strContents = new String(contents, "UTF-8").trim();

			contentType = Utils.getContentType(strContents);

			switch (contentType) {
			case XML:
			case ATTRS_XML:
			case NTFY_XML:
			case RES_XML:
				reqMessage = (OneM2mRequest)xmlConvertor.unmarshal(strContents);
				break;
			case JSON:
			case ATTRS_JSON:
			case NTFY_JSON:
			case RES_JSON:
				reqMessage = (OneM2mRequest)decodeJsonConvertor.unmarshal(strContents);
				break;
			default:
				throw new OneM2MException(OneM2mResponse.RESPONSE_STATUS.INVALID_ARGUMENTS, "Invalid format");
			}

//			if(contentType == CONTENT_TYPE.XML) {
//				reqMessage = (OneM2mRequest)decodeXmlConvertor.unmarshal(strContents);
//				
//			} else if(contentType == CONTENT_TYPE.JSON) {
//				reqMessage = (OneM2mRequest)decodeJsonConvertor.unmarshal(strContents);
//			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			throw new OneM2MException(OneM2mResponse.RESPONSE_STATUS.INVALID_ARGUMENTS, "Invalid format");
		}
		
		if(reqMessage.getResultContentEnum() == RESULT_CONT.NONE) {
			reqMessage.setResultContent(getDefaultResultContent(reqMessage.getOperationEnum()));
		}
		reqMessage.setContentType(contentType);
		
		if(reqMessage.getPrimitiveContent() != null) {
			System.out.println(reqMessage.getPrimitiveContent().getAny().size());
		} else {
			System.out.println("primitiveContent is null.");
		}
		
		return reqMessage;

	}

	public static byte[] encode(OneM2mRequest reqMessage) throws OneM2MException {

		String strContents = null;
		CONTENT_TYPE contentType = reqMessage.getContentType();

		RequestPrimitive reqPrimitive = (RequestPrimitive)reqMessage;
		
		try {

			switch (contentType) {
			case XML:
			case ATTRS_XML:
			case NTFY_XML:
			case RES_XML:
				strContents = xmlConvertor.marshal(reqMessage);
				break;
				
			case JSON:
			case ATTRS_JSON:
			case NTFY_JSON:
			case RES_JSON:
				strContents = encodeJsonConvertor.marshal(reqPrimitive);
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
	
	
	protected static RESULT_CONT getDefaultResultContent(OPERATION operation) {
		switch(operation) {
		case CREATE:
			return RESULT_CONT.ATTRIBUTE;	// TS-0013 8.1.2.1
		case RETRIEVE:
			return RESULT_CONT.ATTRIBUTE;
		case UPDATE:
			return RESULT_CONT.ATTRIBUTE;	// TS-0013 8.1.2.3
		case DELETE:
			return RESULT_CONT.NOTHING;
		case NOTIFY:
			return RESULT_CONT.NOTHING;
		case DISCOVERY:
			return RESULT_CONT.HIERARCHY_ADDR;	
		default:
			return RESULT_CONT.NOTHING;
		}
	}
	
}
