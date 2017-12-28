package net.herit.iot.onem2m.bind.mqtt.codec;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;

import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mRequest.RESOURCE_TYPE;
import net.herit.iot.message.onem2m.OneM2mRequest.RESPONSE_TYPE;
import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.message.onem2m.OneM2mResponse.RESPONSE_STATUS;
import net.herit.iot.message.onem2m.format.Enums.CONTENT_TYPE;
import net.herit.iot.onem2m.bind.codec.JSONSerializer;
import net.herit.iot.onem2m.bind.mqtt.util.Utils;
import net.herit.iot.onem2m.core.convertor.ConvertorFactory;
import net.herit.iot.onem2m.core.convertor.JSONConvertor;
import net.herit.iot.onem2m.core.convertor.XMLConvertor;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.resource.AE;
import net.herit.iot.onem2m.resource.RequestPrimitive;
import net.herit.iot.onem2m.resource.Resource;
import net.herit.iot.onem2m.resource.ResponsePrimitive;

public class MqttResponseCodec {
	
	private static final XMLConvertor<OneM2mResponse> xmlConvertor = 
			new XMLConvertor<OneM2mResponse>(OneM2mResponse.class, OneM2mResponse.SCHEMA_LOCATION);
	private static final JSONConvertor<OneM2mResponse> decodeJsonConvertor =
			new JSONConvertor<OneM2mResponse>(OneM2mResponse.class);
	private static final JSONConvertor<ResponsePrimitive> encodeJsonConvertor =
			new JSONConvertor<ResponsePrimitive>(ResponsePrimitive.class);
	
	// added in 2017-09-01
	private final static String ONEM2M_RESPONSE_MESSAGE = "m2m:rsp";
	private final static String ONEM2M_TY	= "ty";
	private final static String ONEM2M_OPERATION = "op";
	private final static String ONEM2M_TO = "to";
	private final static String ONEM2M_REQUEST_IDENTIFIER = "rqi";
	private final static String ONEM2M_CONTENT = "pc";
	private final static String ONEM2M_FROM = "fr";
	private final static String ONEM2M_RESPONSE_CODE	= "rsc";

	public static OneM2mResponse decode(byte[] contents) throws OneM2MException {

		OneM2mResponse resMessage = null;
		CONTENT_TYPE contentType = CONTENT_TYPE.NONE;
		ResponsePrimitive resPrimitive = new ResponsePrimitive();
		
		try {
			String strContents = new String(contents, "UTF-8").trim();

			contentType = Utils.getContentType(strContents);

			switch (contentType) {
			case XML:
			case ATTRS_XML:
			case NTFY_XML:
			case RES_XML:
				//resMessage = (OneM2mResponse)xmlConvertor.unmarshal(strContents); // blocked in 2017-10-26
				resMessage = new OneM2mResponse();
				resPrimitive = (ResponsePrimitive)xmlConvertor.unmarshal(strContents);
				//System.out.println("[MQTT]====>reqPrimitive.getPrimitiveContent().getAnyOrAny().size() =" + reqPrimitive.getPrimitiveContent().getAnyOrAny().size());
				if(resPrimitive.getFrom() != null) {
					resMessage.setFrom(resPrimitive.getFrom());
				}
				if(resPrimitive.getTo() != null) {
					resMessage.setTo(resPrimitive.getTo());
				}
				if(resPrimitive.getRequestIdentifier() != null) {
					resMessage.setRequestIdentifier(resPrimitive.getRequestIdentifier());
				}
				
				resMessage.setContentType(contentType);
				
				if(resPrimitive.getEventCategory() != null) {
					resMessage.setEventCategory(resPrimitive.getEventCategory());
				}
				
				/*resMessage.setResourceType(resPrimitive.getResourceType());				
				
				if(resPrimitive.getResponseType() == null || reqPrimitive.getResponseType().getResponseTypeValue() == RESPONSE_TYPE.NONE.Value()) {
					reqMessage.setResponseType(RESPONSE_TYPE.BLOCK_REQ);
				} else {
					resMessage.setResponseType(resPrimitive.getResponseType());
				}
				*/
				resMessage.setResponseStatusCode(resPrimitive.getResponseStatusCode());									// added in 2017-10-26
				resMessage.setResponseStatusCodeEnum( RESPONSE_STATUS.get( resPrimitive.getResponseStatusCode() ) );	// added in 2017-10-26
				
				if(resPrimitive.getPrimitiveContent() != null && resPrimitive.getPrimitiveContent().getAnyOrAny().size() > 0) {
					Resource resource = (Resource)resPrimitive.getPrimitiveContent().getAnyOrAny().get(0);
					resMessage.setContentObject(resource);
				}
				break;
			case JSON:
			case ATTRS_JSON:
			case NTFY_JSON:
			case RES_JSON:
				resMessage = new OneM2mResponse();
				
				//resMessage = (OneM2mResponse)decodeJsonConvertor.unmarshal(strContents);	//blocked in 2017-10-18
				// added in 2017-10-18
				strContents = "{\"m2m:rsp\" : " + strContents + "}";
				resPrimitive = (ResponsePrimitive)encodeJsonConvertor.unmarshal(strContents);
				
				if(resPrimitive.getFrom() != null) {
					resMessage.setFrom(resPrimitive.getFrom());
				}
				if(resPrimitive.getTo() != null) {
					resMessage.setTo(resPrimitive.getTo());
				}
				if(resPrimitive.getRequestIdentifier() != null) {
					resMessage.setRequestIdentifier(resPrimitive.getRequestIdentifier());	
				}
				if(resPrimitive.getResponseStatusCode() != null) {
					resMessage.setResponseStatusCode(resPrimitive.getResponseStatusCode());
				}
				
				resMessage.setContentType(contentType);
				
				JSONObject jsonMsgObj = new JSONObject(strContents);
				JSONObject jsonContentsObj = (JSONObject)jsonMsgObj.get("m2m:rsp");
				if(jsonContentsObj.has("pc")) {
					JSONObject jsonBodyObj = jsonContentsObj.getJSONObject("pc");
					Iterator<?> keys = jsonBodyObj.keys();
					int nType = 0;
					while(keys.hasNext()) {
						String key = (String)keys.next();
						if(jsonBodyObj.get(key) instanceof JSONObject) {
							JSONObject jsonContObj = jsonBodyObj.getJSONObject(key);
							nType = jsonContObj.getInt("ty");
						}
					}
					
					String resourceType = RESOURCE_TYPE.get(nType).Name();
					resourceType = net.herit.iot.onem2m.bind.mqtt.util.Utils.stringFirstUppper(resourceType);	// added in 2017-10-26
					
					String pkgName = "net.herit.iot.onem2m.resource"; 
					Class<?> classObj = Class.forName(pkgName + "." + resourceType);
					String schemaLoc = (String)classObj.getField("SCHEMA_LOCATION").get( classObj.newInstance() );
					
					JSONConvertor<?> jsonCvt = ConvertorFactory.getJSONConvertor(classObj, schemaLoc);
					Resource resource = (Resource)jsonCvt.unmarshal(jsonBodyObj.toString());
					resMessage.setContentObject(resource);
				}
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
		
		OneM2mRequest reqMessage = resMessage.getRequest();
		
		if(contentType == null) {	// added in 2017-10-26 to support MQTT Delete resource
			contentType  = reqMessage.getContentType();
		}
		
		try {

			switch (contentType) {
			case XML:
			case ATTRS_XML:
			case NTFY_XML:
			case RES_XML:
				strContents = xmlConvertor.marshal(resMessage);
				strContents = strContents.replaceAll("rsp", ONEM2M_RESPONSE_MESSAGE);		// added in 2017-09-01 to add prefix "m2m:"
				
				if(strContents.indexOf("oneM2MResponse") > 0) {
					System.out.println("===============================> xsi:type=oneM2MResponse is in the current message yet..... ");
					int nFirst = strContents.indexOf("xsi:type");
					int nLast = strContents.indexOf("oneM2MResponse");
					String firstStr = strContents.substring(0, nFirst-1);
					String lastStr = strContents.substring(nLast + 15);
					strContents = firstStr + lastStr;
					
				} else {
					System.out.println("==================================> NOOOOO oneM2MResponse in xsi:type");
				}
				strContents = strContents.replaceAll("[\n\r\t]", "");   // added in 2017-11-02 to support TTA
				strContents = strContents.replaceAll(">\\s+<", "><");
				break;
				
			case JSON:
			case ATTRS_JSON:
			case NTFY_JSON:
			case RES_JSON:
				// strContents = encodeJsonConvertor.marshal(resPrimitive);  blocked in 2017-09-01 to "m2m:" prefix 
				// added in 2017-09-01
				JSONObject jsonMsg = new JSONObject();
				JSONObject jsonContents = new JSONObject();
				
				String strContentObject = "";
				
				jsonContents.put(ONEM2M_RESPONSE_CODE, resMessage.getResponseStatusCode());
				jsonContents.put(ONEM2M_REQUEST_IDENTIFIER, resMessage.getRequestIdentifier());
				
				JSONSerializer jsonSerializer = new JSONSerializer();
				strContentObject = jsonSerializer.serialize(resMessage.getContentObject());
				if(strContentObject != null) {						// added in 2017-10-26
					JSONObject jsonContentObject = new JSONObject(strContentObject);
					
					jsonContents.put(ONEM2M_CONTENT, jsonContentObject);
				}
				
				//jsonMsg.put(ONEM2M_RESPONSE_MESSAGE, jsonContents);   blocked in 2017-10-17 to remove m2m:rsp root
				//strContents = jsonMsg.toString();						blocked in 2017-10-17 to remove m2m:rsp root
				strContents = jsonContents.toString();					// added in 2017-10-17 to remove m2m:rsp root
						
				break;
			// Added in 2017-05-23 to decode CBOR into JSON string
			case CBOR:
			case NTFY_CBOR:
			case RES_CBOR:
				String strContJson = encodeJsonConvertor.marshal(resPrimitive);
				byte[] cbor = net.herit.iot.onem2m.core.util.Utils.encodeCBOR(strContJson);
				
				StringBuffer sb = new StringBuffer();
				
				//String cborEncodedStr = new BigInteger(cborBytes).toString(16);
				for(int i = 0; i < cbor.length; i++) {
					sb.append(Integer.toHexString(0x0100 + (cbor[i] & 0x00FF)).substring(1));

				}	
				strContents = sb.toString();
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
