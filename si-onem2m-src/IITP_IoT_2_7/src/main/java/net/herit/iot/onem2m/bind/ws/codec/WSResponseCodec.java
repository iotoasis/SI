package net.herit.iot.onem2m.bind.ws.codec;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.XStream;

import net.herit.iot.message.onem2m.OneM2mRequest.OPERATION;
import net.herit.iot.message.onem2m.OneM2mRequest.RESOURCE_TYPE;
import net.herit.iot.message.onem2m.OneM2mRequest.RESPONSE_TYPE;
import net.herit.iot.message.onem2m.OneM2mRequest.RESULT_CONT;
import net.herit.iot.message.onem2m.OneM2mResponse.RESPONSE_STATUS;
import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.message.onem2m.format.Enums.CONTENT_TYPE;
import net.herit.iot.onem2m.bind.codec.JSONSerializer;
import net.herit.iot.onem2m.bind.codec.XMLSerializer;
import net.herit.iot.onem2m.bind.mqtt.util.Utils;
import net.herit.iot.onem2m.bind.ws.codec.WSRequestCodec.MapEntryConverter;
import net.herit.iot.onem2m.bind.ws.server.HWebSocketServer;
import net.herit.iot.onem2m.core.convertor.ConvertorFactory;
import net.herit.iot.onem2m.core.convertor.JSONConvertor;
import net.herit.iot.onem2m.core.convertor.XMLConvertor;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.resource.AE;
import net.herit.iot.onem2m.resource.PrimitiveContent;
import net.herit.iot.onem2m.resource.RequestPrimitive;
import net.herit.iot.onem2m.resource.Resource;
import net.herit.iot.onem2m.resource.ResponsePrimitive;
import net.herit.iot.onem2m.resource.RestSubscription;

public class WSResponseCodec extends WSAbsCodec {
private final static String HTTP_HEADER_SEC_WEBSOCKET_ACCEPT 		= "Sec-WebSocket-Accept";

	private static final XMLConvertor<OneM2mResponse> xmlConvertor = 
			new XMLConvertor<OneM2mResponse>(OneM2mResponse.class, OneM2mResponse.SCHEMA_LOCATION);
	private static final JSONConvertor<OneM2mResponse> decodeJsonConvertor =
			new JSONConvertor<OneM2mResponse>(OneM2mResponse.class);
	private static final JSONConvertor<ResponsePrimitive> encodeJsonConvertor =
			new JSONConvertor<ResponsePrimitive>(ResponsePrimitive.class);
	
	private final static String ONEM2M_RESPONSE_MESSAGE = "m2m:rsp";
	
	public static String encode(OneM2mResponse resMessage) throws Exception {
		
		String strContents = null;
		
		String resultMsg = "";
		String strContentObject = "";
		CONTENT_TYPE cont_type = resMessage.getContentType();
		
		JSONSerializer jsonSerializer = new JSONSerializer();
		
		switch(cont_type) {
		case CBOR:
		case NTFY_CBOR:
		case RES_CBOR:
		//	Map<String, Object> msg = new HashMap<String, Object>();
		//	Map<String, Object> contents = new HashMap<String, Object>();	
			
			JSONObject jsonCborContents = new JSONObject();
			
			jsonCborContents.put(ONEM2M_RESPONSE_CODE, resMessage.getResponseStatusCode());
			jsonCborContents.put(ONEM2M_REQUEST_IDENTIFIER, resMessage.getRequestIdentifier());
			
			
			strContentObject = jsonSerializer.serialize(resMessage.getContentObject());

			if(strContentObject != null) {						// added in 2017-10-26
				JSONObject jsonContentObject = new JSONObject(strContentObject);
				
				jsonCborContents.put(ONEM2M_CONTENT, jsonContentObject);
			}
			
			////System.out.println("##### CBOR jsonCborContents.toString()=" + jsonCborContents.toString());
			
			byte[] cbor = net.herit.iot.onem2m.core.util.Utils.encodeCBOR(jsonCborContents.toString());
			//System.out.println("##### cbor=" + new String(cbor, "UTF-8"));
			
			//System.out.println("### CBOR trace-2");
			strContents = new String(cbor, "UTF-8");
			strContents = strContents.toUpperCase();
/**	blocked in 2017-12-08 		
			byte[] content = resMessage.getContent();
			String strContent = new String(content, "UTF-8");
			//System.out.println("CBOR strContent = " + strContent);
			byte[] cbor = net.herit.iot.onem2m.core.util.Utils.encodeCBOR(strContent);
			
			StringBuffer sb = new StringBuffer();
			
			//String cborEncodedStr = new BigInteger(cborBytes).toString(16);
			for(int i = 0; i < cbor.length; i++) {
				sb.append(Integer.toHexString(0x0100 + (cbor[i] & 0x00FF)).substring(1));

			}
			strContentObject = sb.toString();
			contents.put(ONEM2M_CONTENT, strContentObject);
			msg.put(ONEM2M_RESPONSE_MESSAGE, contents);
	
			resultMsg = new ObjectMapper().writeValueAsString(msg);			
*/					
			break;	
			
		case JSON:
		case RES_JSON:
		case NTFY_JSON:
		case ATTRS_JSON:
			/*JSONSerializer jsonSerializer = new JSONSerializer();
			strContentObject = jsonSerializer.serialize(resMessage.getContentObject());
			JSONObject jsonContentObject = new JSONObject(strContentObject);
			
			jsonContents.put(ONEM2M_CONTENT, jsonContentObject);
			
			jsonMsg.put(ONEM2M_RESPONSE_MESSAGE, jsonContents);
			
			resultMsg = jsonMsg.toString();*/		
			JSONObject jsonMsg = new JSONObject();
			JSONObject jsonContents = new JSONObject();
			
			strContentObject = "";
			
			jsonContents.put(ONEM2M_RESPONSE_CODE, resMessage.getResponseStatusCode());
			jsonContents.put(ONEM2M_REQUEST_IDENTIFIER, resMessage.getRequestIdentifier());
			
			strContentObject = jsonSerializer.serialize(resMessage.getContentObject());

			if(strContentObject != null) {						// added in 2017-10-26
				JSONObject jsonContentObject = new JSONObject(strContentObject);
				
				jsonContents.put(ONEM2M_CONTENT, jsonContentObject);
			}
			
			//jsonMsg.put(ONEM2M_RESPONSE_MESSAGE, jsonContents);   blocked in 2017-10-17 to remove m2m:rsp root
			//strContents = jsonMsg.toString();						blocked in 2017-10-17 to remove m2m:rsp root
			strContents = jsonContents.toString();					// added in 2017-10-17 to remove m2m:rsp root			
			break;
			
		case XML:
		case RES_XML:
		case NTFY_XML: 
		case ATTRS_XML:
		default:		
			/*ResponsePrimitive resPriv = new ResponsePrimitive();
			resPriv.setFrom(resMessage.getFrom());
			resPriv.setResponseStatusCode(resMessage.getResponseStatusCode());
			resPriv.setRequestIdentifier(resMessage.getRequestIdentifier());
			resPriv.setPrimitiveContent(resMessage.getPrimitiveContent());
		
			XMLSerializer xmlSerializer = new XMLSerializer();
			resultMsg = xmlSerializer.serialize(resPriv);*/
			strContents = xmlConvertor.marshal(resMessage);
			strContents = strContents.replaceAll("rsp", ONEM2M_RESPONSE_MESSAGE);		// added in 2017-09-01 to add prefix "m2m:"
			
			if(strContents.indexOf("oneM2MResponse") > 0) {
				//System.out.println("===============================> xsi:type=oneM2MResponse is in the current message yet..... ");
				int nFirst = strContents.indexOf("xsi:type");
				int nLast = strContents.indexOf("oneM2MResponse");
				String firstStr = strContents.substring(0, nFirst-1);
				String lastStr = strContents.substring(nLast + 15);
				strContents = firstStr + lastStr;
				
			} else {
				//System.out.println("==================================> NOOOOO oneM2MResponse in xsi:type");
			}
			strContents = strContents.replaceAll("[\n\r\t]", "");   // added in 2017-11-02 to support TTA
			strContents = strContents.replaceAll(">\\s+<", "><");
			break;
		}
		
		return strContents;
	}
	
	public static OneM2mResponse decode(byte[] message) throws OneM2MException {
		OneM2mResponse resMessage = new OneM2mResponse();
		ResponsePrimitive resPrimitive = new ResponsePrimitive();
		
		CONTENT_TYPE contentType = CONTENT_TYPE.NONE;
		
		try {
			String strContents = new String(message, "UTF-8").trim();
	
			contentType = Utils.getContentType(strContents);
			
			switch (contentType) {
			case XML:
			case ATTRS_XML:
			case NTFY_XML:
			case RES_XML:
				//resMessage = (OneM2mResponse)xmlConvertor.unmarshal(strContents);
				/*resPrimitive = (ResponsePrimitive)xmlConvertor.unmarshal(strContents);
				
				resMessage.setFrom(resPrimitive.getFrom());
				resMessage.setResponseStatusCode(resPrimitive.getResponseStatusCode());
				resMessage.setRequestIdentifier(resPrimitive.getRequestIdentifier());
				resMessage.setPrimitiveContent(resPrimitive.getPrimitiveContent());
				resMessage.setContentType(contentType);
				
				Object contObject = resPrimitive.getPrimitiveContent().getAnyOrAny().get(0);
			
				resMessage.setContentObject(contObject);*/
				resMessage = new OneM2mResponse();
				resPrimitive = (ResponsePrimitive)xmlConvertor.unmarshal(strContents);
				////System.out.println("[MQTT]====>reqPrimitive.getPrimitiveContent().getAnyOrAny().size() =" + reqPrimitive.getPrimitiveContent().getAnyOrAny().size());
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
				/*resPrimitive = (ResponsePrimitive)jsonConvertor.unmarshal(strContents);
				resMessage.setFrom(resPrimitive.getFrom());
				resMessage.setTo(resPrimitive.getTo());
				resMessage.setRequestIdentifier(resPrimitive.getRequestIdentifier());
				resMessage.setContentType(contentType);
				
				JSONObject jsonMsgObj = new JSONObject(strContents);
				JSONObject jsonContentsObj = (JSONObject)jsonMsgObj.get("m2m:rsp");
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
				
				String pkgName = "net.herit.iot.onem2m.resource"; 
				Class<?> classObj = Class.forName(pkgName + "." + resourceType);
				String schemaLoc = (String)classObj.getField("SCHEMA_LOCATION").get( classObj.newInstance() );
				
				JSONConvertor<?> jsonCvt = ConvertorFactory.getJSONConvertor(classObj, schemaLoc);
			
				Resource resource = (Resource)jsonCvt.unmarshal(jsonBodyObj.toString());
				resMessage.setContentObject(resource);*/
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
			
		} catch(Exception e) {
			e.printStackTrace();
			throw new OneM2MException(OneM2mResponse.RESPONSE_STATUS.INVALID_ARGUMENTS, "Invalid format");
		}
		
		resMessage.setContentType(contentType);
		
		return resMessage;
	}
}
