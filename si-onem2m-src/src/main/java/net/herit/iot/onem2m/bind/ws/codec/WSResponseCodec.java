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

import net.herit.iot.message.onem2m.OneM2mRequest.RESOURCE_TYPE;
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
		
		Map<String, Object> msg = new HashMap<String, Object>();
		Map<String, Object> contents = new HashMap<String, Object>();
		Map<String, Object> body = new HashMap<String, Object>();
		
		String resultMsg = "";
		
		contents.put(ONEM2M_RESPONSE_CODE, resMessage.getResponseStatusCode());
		contents.put(ONEM2M_REQUEST_IDENTIFIER, resMessage.getRequestIdentifier());	
		//System.out.println("====================> " + resMessage.toString());
		//System.out.println("====================> " + resMessage.getContentObject().toString());
		
		String bodyName = "m2m:";
		Resource resource = (Resource)resMessage.getContentObject();
		
		
		
		bodyName += RESOURCE_TYPE.get(resource.getResourceType()).Name();
		
		CONTENT_TYPE cont_type = resMessage.getContentType();
		String strContentObject = "";
		switch(cont_type) {
		case JSON:
		case RES_JSON:
		case NTFY_JSON:
		case ATTRS_JSON:
			JSONSerializer jsonSerializer = new JSONSerializer();
			strContentObject = jsonSerializer.serialize(resMessage.getContentObject());
			
			body.put(bodyName, "###CONTENT###");
			contents.put(ONEM2M_CONTENT, body);
			
			msg.put(ONEM2M_RESPONSE_MESSAGE, contents);
			
			resultMsg = new ObjectMapper().writeValueAsString(msg);
			
			resultMsg = resultMsg.replace("\"###CONTENT###\"", strContentObject);
			
			break;
			
		case XML:
		case RES_XML:
		case NTFY_XML: 
		case ATTRS_XML:
		default:
			XMLSerializer xmlSerializer = new XMLSerializer();
			strContentObject = xmlSerializer.serialize(resMessage.getContentObject());
			
			body.put(bodyName, "###CONTENT###");
			
			contents.put(ONEM2M_CONTENT, body);
			
			msg.put(ONEM2M_RESPONSE_MESSAGE, contents);
			
			XStream magicApi = new XStream();
			magicApi.registerConverter(new MapEntryConverter());
			magicApi.alias("root", Map.class);
			
			resultMsg = magicApi.toXML(msg);
			
			resultMsg = resultMsg.replace("###CONTENT###", strContentObject);
			break;
		}
		
		return resultMsg;
	}
	
	public static OneM2mResponse decode(byte[] message) throws OneM2MException {
		OneM2mResponse resMessage = null;
		CONTENT_TYPE contentType = CONTENT_TYPE.NONE;
		
		try {
			String strContents = new String(message, "UTF-8").trim();
			
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
			
				Resource resource = (Resource)jsonCvt.unmarshal(jsonBodyObj.get("m2m:" + resourceType).toString());
				
				resMessage = (OneM2mResponse)decodeJsonConvertor.unmarshal(jsonMsgObj.get("m2m:rqp").toString());
				resMessage.setContentObject(resource);
				
				//System.out.println("jsonObj Resource======>" + new String(resMessage.getContent(), "UTF-8").trim() );
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
