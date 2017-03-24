package net.herit.iot.onem2m.bind.ws.codec;


import java.io.ByteArrayOutputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mRequest.RESPONSE_TYPE;
import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.message.onem2m.OneM2mRequest.OPERATION;
import net.herit.iot.message.onem2m.OneM2mRequest.RESOURCE_TYPE;
import net.herit.iot.message.onem2m.OneM2mRequest.RESULT_CONT;
import net.herit.iot.message.onem2m.format.Enums.CONTENT_TYPE;
import net.herit.iot.message.onem2m.format.Enums.FILTER_USAGE;
import net.herit.iot.onem2m.bind.codec.JSONSerializer;
import net.herit.iot.onem2m.bind.codec.XMLSerializer;
//import net.herit.iot.onem2m.bind.mqtt.util.Utils;
import net.herit.iot.onem2m.bind.codec.Utils;
import net.herit.iot.onem2m.core.convertor.ConvertorFactory;
import net.herit.iot.onem2m.core.convertor.JSONConvertor;
import net.herit.iot.onem2m.core.convertor.XMLConvertor;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.resource.FilterCriteria;
import net.herit.iot.onem2m.resource.PrimitiveContent;
import net.herit.iot.onem2m.resource.RequestPrimitive;
import net.herit.iot.onem2m.resource.Resource;
import net.herit.iot.onem2m.resource.ResponsePrimitive;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;


public class WSRequestCodec extends WSAbsCodec {
	private final static String HTTP_HEADER_SEC_WEBSOCKET_KEY 		= "Sec-WebSocket-Key";
	private final static String HTTP_HEADER_SEC_WEBSOCKET_VERSION 		= "Sec-WebSocket-Version";
	
	//private static final XMLConvertor<OneM2mRequest> xmlConvertor = 
	//		new XMLConvertor<OneM2mRequest>(OneM2mRequest.class, OneM2mRequest.SCHEMA_LOCATION);
	private static final XMLConvertor<RequestPrimitive> xmlConvertor = 
			new XMLConvertor<RequestPrimitive>(RequestPrimitive.class, RequestPrimitive.SCHEMA_LOCATION);
			
	private static final JSONConvertor<OneM2mRequest> decodeJsonConvertor =
			new JSONConvertor<OneM2mRequest>(OneM2mRequest.class);
	private static final JSONConvertor<RequestPrimitive> encodeJsonConvertor =
			new JSONConvertor<RequestPrimitive>(RequestPrimitive.class);
	
	private static final JSONConvertor<RequestPrimitive> jsonConvertor =
			new JSONConvertor<RequestPrimitive>(RequestPrimitive.class);
	
	private final static String ONEM2M_REQUEST_MESSAGE = "m2m:rqp";
	
	
	public static byte[] encode(OneM2mRequest reqMessage) throws Exception {
		
		Map<String, Object> msg = new HashMap<String, Object>();
		Map<String, Object> contents = new HashMap<String, Object>();
		
		String resultMsg = "";
		
		contents.put(ONEM2M_OPERATION, reqMessage.getOperation());
		contents.put(ONEM2M_TO, reqMessage.getTo());
		contents.put(ONEM2M_FROM, reqMessage.getFrom());
		contents.put(ONEM2M_REQUEST_IDENTIFIER, reqMessage.getRequestIdentifier());
		contents.put(ONEM2M_TY, reqMessage.getResourceType());
		
		String bodyName = "m2m:";
		bodyName += reqMessage.getResourceTypeEnum().Name();
		
		CONTENT_TYPE cont_type = reqMessage.getContentType();
		String strContentObject = "";
		switch(cont_type) {
		case JSON:
		case RES_JSON:
		case NTFY_JSON:
		case ATTRS_JSON:
			
			JSONSerializer jsonSerializer = new JSONSerializer();
			strContentObject = jsonSerializer.serialize(reqMessage.getContentObject());
		
			contents.put(ONEM2M_CONTENT, strContentObject);
			msg.put(ONEM2M_REQUEST_MESSAGE, contents);
	
			resultMsg = new ObjectMapper().writeValueAsString(msg);		
			break;
			
		case XML:
		case RES_XML:
		case NTFY_XML: 
		case ATTRS_XML:
		default:		
			RequestPrimitive reqPriv = new RequestPrimitive();
			reqPriv.setFrom(reqMessage.getFrom());
			reqPriv.setTo(reqMessage.getTo());
			reqPriv.setOperation(reqMessage.getOperation());
			reqPriv.setResourceType(reqMessage.getResourceType());
			reqPriv.setRequestIdentifier(reqMessage.getRequestIdentifier());
			reqPriv.setPrimitiveContent(reqMessage.getPrimitiveContent());
		
			XMLSerializer xmlSerializer = new XMLSerializer();
			resultMsg = xmlSerializer.serialize(reqPriv);
			break;
		/*	
			XMLSerializer xmlSerializer = new XMLSerializer();
			strContentObject = xmlSerializer.serialize(reqMessage.getContentObject());
			
			body.put(bodyName, "###CONTENT###");
			
			contents.put(ONEM2M_CONTENT, body);
			
			msg.put(ONEM2M_REQUEST_MESSAGE, contents);
			
			XStream magicApi = new XStream();
			magicApi.registerConverter(new MapEntryConverter());
			magicApi.alias("root", Map.class);
			
			resultMsg = magicApi.toXML(msg);
			
			resultMsg = resultMsg.replace("###CONTENT###", strContentObject);
			break;   */
		}
		
		return resultMsg.getBytes();
	}
	
	
	public static class MapEntryConverter implements Converter {

        public boolean canConvert(Class clazz) {
            return AbstractMap.class.isAssignableFrom(clazz);
        }

        public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {

            AbstractMap map = (AbstractMap) value;
            for (Object obj : map.entrySet()) {
                Map.Entry entry = (Map.Entry) obj;
                writer.startNode(entry.getKey().toString());
                Object val = entry.getValue();
                if ( null != val ) {
                    writer.setValue(val.toString());
                }
                writer.endNode();
            }

        }

        public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {

            Map<String, String> map = new HashMap<String, String>();

            while(reader.hasMoreChildren()) {
                reader.moveDown();

                String key = reader.getNodeName(); // nodeName aka element's name
                String value = reader.getValue();
                map.put(key, value);

                reader.moveUp();
            }

            return map;
        }

    }
	
	public static OneM2mRequest decode(byte[] message) throws OneM2MException {
		OneM2mRequest reqMessage = new OneM2mRequest();
		RequestPrimitive reqPrimitive = new RequestPrimitive();
		
		CONTENT_TYPE contentType = CONTENT_TYPE.NONE;
		
		try {
			String strContents = new String(message, "UTF-8").trim();
			
			contentType = WSRequestCodec.getContentType(strContents);
			
			Object contObject = null;
			
			switch (contentType) {
			case XML:
			case ATTRS_XML:
			case NTFY_XML:
			case RES_XML:
				reqPrimitive = (RequestPrimitive)xmlConvertor.unmarshal(strContents);
				
				reqMessage.setFrom(reqPrimitive.getFrom());
				reqMessage.setTo(reqPrimitive.getTo());
				reqMessage.setOperation(reqPrimitive.getOperation());
				reqMessage.setRequestIdentifier(reqPrimitive.getRequestIdentifier());
				reqMessage.setPrimitiveContent(reqPrimitive.getPrimitiveContent());
				reqMessage.setContentType(contentType);
				reqMessage.setDiscoveryResultType(reqPrimitive.getDiscoveryResultType());
				reqMessage.setEventCategory(reqPrimitive.getEventCategory());
				reqMessage.setFilterCriteria(reqPrimitive.getFilterCriteria());
				reqMessage.setResourceType(reqPrimitive.getResourceType());
				
				if(reqPrimitive.getResponseType() == null || reqPrimitive.getResponseType().getResponseTypeValue() == RESPONSE_TYPE.NONE.Value()) {
					reqMessage.setResponseType(RESPONSE_TYPE.BLOCK_REQ);
				} else {
					reqMessage.setResponseType(reqPrimitive.getResponseType());
				}
				
				//PrimitiveContent ptx = reqPrimitive.getPrimitiveContent();
				
				//System.out.println("################### reqPrimitive.getPrimitiveContent() === " + ptx.getAnyOrAny().size());
				
				contObject = reqMessage.getPrimitiveContent().getAnyOrAny().get(0);
			
				reqMessage.setContentObject(contObject);
				
				reqMessage.setResultContent(reqPrimitive.getResultContent());
				
				if (reqMessage.getResultContentEnum().equals(RESULT_CONT.NONE)) {
					OPERATION operation = reqMessage.getOperationEnum();
					reqMessage.setResultContent(Utils.getDefaultResultContent(operation));
				}
				
				break;
			case JSON:
			case ATTRS_JSON:
			case NTFY_JSON:
			case RES_JSON:
				reqPrimitive = (RequestPrimitive)jsonConvertor.unmarshal(strContents);
				
				reqMessage.setFrom(reqPrimitive.getFrom());
				reqMessage.setTo(reqPrimitive.getTo());
				reqMessage.setOperation(reqPrimitive.getOperation());
				reqMessage.setRequestIdentifier(reqPrimitive.getRequestIdentifier());
				reqMessage.setContentType(contentType);
				reqMessage.setDiscoveryResultType(reqPrimitive.getDiscoveryResultType());
				reqMessage.setEventCategory(reqPrimitive.getEventCategory());
				reqMessage.setFilterCriteria(reqPrimitive.getFilterCriteria());
				reqMessage.setResourceType(reqPrimitive.getResourceType());				
				
				if(reqPrimitive.getResponseType() == null || reqPrimitive.getResponseType().getResponseTypeValue() == RESPONSE_TYPE.NONE.Value()) {
					reqMessage.setResponseType(RESPONSE_TYPE.BLOCK_REQ);
				} else {
					reqMessage.setResponseType(reqPrimitive.getResponseType());
				}
				
				JSONObject jsonMsgObj = new JSONObject(strContents);
				JSONObject jsonContentsObj = (JSONObject)jsonMsgObj.get("m2m:rqp");
				JSONObject jsonBodyObj = jsonContentsObj.getJSONObject("pc");
				//int nType = (int)jsonContentsObj.get("ty");
				int nType = reqPrimitive.getResourceType();
				
				String resourceType = RESOURCE_TYPE.get(nType).Name();
				
				String pkgName = "net.herit.iot.onem2m.resource"; 
				Class<?> classObj = Class.forName(pkgName + "." + resourceType);
				String schemaLoc = (String)classObj.getField("SCHEMA_LOCATION").get( classObj.newInstance() );
				
				JSONConvertor<?> jsonCvt = ConvertorFactory.getJSONConvertor(classObj, schemaLoc);
			
				Resource resource = (Resource)jsonCvt.unmarshal(jsonBodyObj.toString());
				
				//reqMessage = (OneM2mRequest)decodeJsonConvertor.unmarshal(jsonMsgObj.get("m2m:rqp").toString());
				
				FilterCriteria filterCriteria = reqMessage.getFilterCriteria();
				//OPERATION operation = OPERATION.NONE;
				
				//if(jsonContentsObj.getInt("op") > 0) {
				//	int op = jsonContentsObj.getInt("op");
				//	operation = OPERATION.get(op);
				//}
				
				/*operation = reqMessage.getOperationEnum();
				
				if(filterCriteria != null && filterCriteria.getFilterUsage() == FILTER_USAGE.DISCOVERY.Value()) {
					operation = OPERATION.DISCOVERY;
				} else {
					operation = OPERATION.RETRIEVE;
				}
				
				if(reqMessage.getResourceTypeEnum() != OneM2mRequest.RESOURCE_TYPE.NONE) {
					operation = OPERATION.CREATE;
				} else {
					if (reqMessage.getContentType() == CONTENT_TYPE.NTFY_JSON ||
							 reqMessage.getContentType() == CONTENT_TYPE.NTFY_XML) {
						operation = OPERATION.NOTIFY;
					} else {
						operation = OPERATION.UPDATE;
					}
				}
				
				reqMessage.setOperation(operation);*/
				
				//System.out.println("################### reqPrimitive.getResultContent() ===== " + reqPrimitive.getResultContent());
				reqMessage.setResultContent(reqPrimitive.getResultContent());
				
				if (reqMessage.getResultContentEnum().equals(RESULT_CONT.NONE)) {
					OPERATION operation = reqMessage.getOperationEnum();
					reqMessage.setResultContent(Utils.getDefaultResultContent(operation));
				}
				
				reqMessage.setContentObject(resource);
				
				//System.out.println("jsonObj Resource======>" + new String(reqMessage.getContent(), "UTF-8").trim() );
				
				break;
			default:
				throw new OneM2MException(OneM2mResponse.RESPONSE_STATUS.INVALID_ARGUMENTS, "Invalid format");
			}
		} catch(Exception e) {
			e.printStackTrace();
			throw new OneM2MException(OneM2mResponse.RESPONSE_STATUS.INVALID_ARGUMENTS, "Invalid format");
		}
		
		return reqMessage;
	}
	
	
	private static CONTENT_TYPE getContentType(String strContents) {
		String contents = strContents.trim();
		CONTENT_TYPE contentType = CONTENT_TYPE.NONE;
		if(contents.startsWith("<")) {				// XML format
			contentType = CONTENT_TYPE.XML;
		} else if(contents.startsWith("{")) {		// JSON format
			contentType = CONTENT_TYPE.JSON;
		}
		
		return contentType;
	}
}
