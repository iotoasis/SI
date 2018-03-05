package net.herit.iot.onem2m.bind.ws.codec;


import java.io.ByteArrayOutputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mRequest.RESPONSE_TYPE;
import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.message.onem2m.OneM2mRequest.OPERATION;
import net.herit.iot.message.onem2m.OneM2mRequest.RESOURCE_TYPE;
import net.herit.iot.message.onem2m.OneM2mRequest.RESULT_CONT;
import net.herit.iot.message.onem2m.OneM2mRequest.SHORT_RESOURCE_TYPE;
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
		
		/*Map<String, Object> msg = new HashMap<String, Object>();
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
		String strContentObject = ""; */
		String strContents = null;
		CONTENT_TYPE contentType = reqMessage.getContentType();
		
		try {
			switch(contentType) {
			case JSON:
			case RES_JSON:
			case NTFY_JSON:
			case ATTRS_JSON:
				/*JSONSerializer jsonSerializer = new JSONSerializer();
				strContentObject = jsonSerializer.serialize(reqMessage.getContentObject());
			
				contents.put(ONEM2M_CONTENT, strContentObject);
				msg.put(ONEM2M_REQUEST_MESSAGE, contents);
		
				resultMsg = new ObjectMapper().writeValueAsString(msg);	*/	
				JSONObject contents = new JSONObject();
				
				String strContentObject = "";
				int oper = reqMessage.getOperation();
				
				//contents.put(ONEM2M_OPERATION, reqMessage.getOperation());
				contents.put(ONEM2M_OPERATION, oper);
				contents.put(ONEM2M_TO, reqMessage.getTo());
				contents.put(ONEM2M_FROM, reqMessage.getFrom());
				contents.put(ONEM2M_REQUEST_IDENTIFIER, reqMessage.getRequestIdentifier());
				if(oper == OPERATION.CREATE.Value() || oper == OPERATION.UPDATE.Value()) {		// added in 2017-10-26
					contents.put(ONEM2M_TY, reqMessage.getResourceType());
					
					JSONSerializer jsonSerializer = new JSONSerializer();
					strContentObject = jsonSerializer.serialize(reqMessage.getContentObject());
					JSONObject jsonMsgObj = new JSONObject(strContentObject);
				
					contents.put(ONEM2M_CONTENT, jsonMsgObj);
				}
				
				strContents = contents.toString();
				break;
				
			case XML:
			case RES_XML:
			case NTFY_XML: 
			case ATTRS_XML:		
				/*RequestPrimitive reqPriv = new RequestPrimitive();
				reqPriv.setFrom(reqMessage.getFrom());
				reqPriv.setTo(reqMessage.getTo());
				reqPriv.setOperation(reqMessage.getOperation());
				reqPriv.setResourceType(reqMessage.getResourceType());
				reqPriv.setRequestIdentifier(reqMessage.getRequestIdentifier());
				reqPriv.setPrimitiveContent(reqMessage.getPrimitiveContent());
			
				XMLSerializer xmlSerializer = new XMLSerializer();
				resultMsg = xmlSerializer.serialize(reqPriv);*/
				int op = reqMessage.getOperation();
				String to = reqMessage.getTo();
				reqMessage.setOperation(net.herit.iot.onem2m.bind.mqtt.util.Utils.getMqttOperation(op, to));	// added in 2017-11-03 to comply with MQTT requestPrimitive operation.
				strContents = xmlConvertor.marshal(reqMessage);
				
				strContents = strContents.replaceAll("rqp", ONEM2M_REQUEST_MESSAGE);		// added in 2017-09-01 to add prefix "m2m:"
				
				if(strContents.indexOf("oneM2MRequest") > 0) {
					
					int nFirst = strContents.indexOf("xsi:type");
					int nLast = strContents.indexOf("oneM2MRequest");
					String firstStr = strContents.substring(0, nFirst-1);
					String lastStr = strContents.substring(nLast + 14);
					strContents = firstStr + lastStr;
					
				} 
				
				strContents = strContents.replaceAll("[\n\r\t]", "");   // added in 2017-11-02 to support TTA
				strContents = strContents.replaceAll(">\\s+<", "><");
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
				/*reqPrimitive = (RequestPrimitive)xmlConvertor.unmarshal(strContents);
				
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
				contObject = reqMessage.getPrimitiveContent().getAnyOrAny().get(0);
			
				reqMessage.setContentObject(contObject);
				
				reqMessage.setResultContent(reqPrimitive.getResultContent());
				
				if (reqMessage.getResultContentEnum().equals(RESULT_CONT.NONE)) {
					OPERATION operation = reqMessage.getOperationEnum();
					reqMessage.setResultContent(Utils.getDefaultResultContent(operation));
				}*/
				reqMessage = new OneM2mRequest();
				reqPrimitive = (RequestPrimitive)xmlConvertor.unmarshal(strContents);
				reqMessage.setFrom(reqPrimitive.getFrom());
				// added in 2017-11-03 to preprocess 'to' parameter for cse-relative unstructured addessing.
				String to = "";
				if(reqPrimitive.getTo() != null && !reqPrimitive.getTo().startsWith("/")) {		
					to = "/" + reqPrimitive.getTo();
				} else {
					to = reqPrimitive.getTo();
				}
				reqMessage.setTo(to);
				
				//System.out.println("####### reqPrimitive.getOperation()=" + reqPrimitive.getOperation());
				int op = net.herit.iot.onem2m.bind.mqtt.util.Utils.getOneM2mOperation(reqPrimitive.getOperation(), to);	// added in 2017-11-03 for operation matching to OneM2mRequest.OPERATION
				reqMessage.setOperation(op);   // added in 2017-11-03 due to the above reason.
				//reqMessage.setOperation(reqPrimitive.getOperation());  // blocked in 2017-11-03 for that mqtt operation is not matching to OneM2mRequest.OPERATION
				
				reqMessage.setRequestIdentifier(reqPrimitive.getRequestIdentifier());
				reqMessage.setContentType(contentType);
				if(reqPrimitive.getDiscoveryResultType() != null) {
					reqMessage.setDiscoveryResultType(reqPrimitive.getDiscoveryResultType());
				}
				
				if(reqPrimitive.getEventCategory() != null) {
					reqMessage.setEventCategory(reqPrimitive.getEventCategory());
				}
				if(reqPrimitive.getFilterCriteria() != null) {
					reqMessage.setFilterCriteria(reqPrimitive.getFilterCriteria());
				}
				if(reqPrimitive.getResourceType() != null) {
					reqMessage.setResourceType(reqPrimitive.getResourceType());
				}
				
				if(reqPrimitive.getResponseType() == null || reqPrimitive.getResponseType().getResponseTypeValue() == RESPONSE_TYPE.NONE.Value()) {
					reqMessage.setResponseType(RESPONSE_TYPE.BLOCK_REQ);
				} else {
					reqMessage.setResponseType(reqPrimitive.getResponseType());
				}
				
				if(reqPrimitive.getPrimitiveContent() != null && reqPrimitive.getPrimitiveContent().getAnyOrAny().size() > 0) {
					Resource resource = (Resource)reqPrimitive.getPrimitiveContent().getAnyOrAny().get(0);
					reqMessage.setContentObject(resource);
				}
				break;
			case CBOR:
			case NTFY_CBOR:
			case RES_CBOR:
				strContents = net.herit.iot.onem2m.core.util.Utils.decodeCBOR(message);		
				
			case JSON:
			case ATTRS_JSON:
			case NTFY_JSON:
			case RES_JSON:
				/*reqPrimitive = (RequestPrimitive)jsonConvertor.unmarshal(strContents);
				
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
				
				int nType = reqPrimitive.getResourceType();
				
				String resourceType = RESOURCE_TYPE.get(nType).Name();
				
				String pkgName = "net.herit.iot.onem2m.resource"; 
				Class<?> classObj = Class.forName(pkgName + "." + resourceType);
				String schemaLoc = (String)classObj.getField("SCHEMA_LOCATION").get( classObj.newInstance() );
				
				JSONConvertor<?> jsonCvt = ConvertorFactory.getJSONConvertor(classObj, schemaLoc);
			
				Resource resource = (Resource)jsonCvt.unmarshal(jsonBodyObj.toString());
				
				FilterCriteria filterCriteria = reqMessage.getFilterCriteria();
				
				reqMessage.setResultContent(reqPrimitive.getResultContent());
				
				if (reqMessage.getResultContentEnum().equals(RESULT_CONT.NONE)) {
					OPERATION operation = reqMessage.getOperationEnum();
					reqMessage.setResultContent(Utils.getDefaultResultContent(operation));
				}
				
				reqMessage.setContentObject(resource);*/
				if(strContents.contains("pc") && !strContents.contains("m2m:")) {
					
					reqMessage = (OneM2mRequest)decodeJsonConvertor.unmarshal(strContents); 
				} else {	// added as below to support "m2m:" prefix parsing in 2017-08-31
					// added in 2017-10-17 to process message not including "m2m:rqp" root 
					if(!strContents.contains("m2m:rqp")) {
						strContents = "{ \"m2m:rqp\" : " + strContents + "}";
					}
					System.out.println("##### strContents = " + strContents);
					reqMessage = new OneM2mRequest();
					reqPrimitive = (RequestPrimitive)decodeJsonConvertor.unmarshal(strContents);
					
					reqMessage.setFrom(reqPrimitive.getFrom());
					to = "";
					if(reqPrimitive.getTo() != null && !reqPrimitive.getTo().startsWith("/")) {		
						to = "/" + reqPrimitive.getTo();
					} else {
						to = reqPrimitive.getTo();
					}
					reqMessage.setTo(to);
					//reqMessage.setTo(reqPrimitive.getTo());
					op = net.herit.iot.onem2m.bind.mqtt.util.Utils.getOneM2mOperation(reqPrimitive.getOperation(), to);	// added in 2017-11-03 for operation matching to OneM2mRequest.OPERATION
					reqMessage.setOperation(op);   // added in 2017-11-03 due to the above reason.
					
					//reqMessage.setOperation(reqPrimitive.getOperation());
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
					if(jsonContentsObj.has("pc")) {
						JSONObject jsonBodyObj = jsonContentsObj.getJSONObject("pc");
						
						// added in 2017-11-07 due to resourceTyepe exception in  resource update operation 
						int nType = 0;
						if(reqPrimitive.getResourceType() == null) {
							Iterator i = jsonBodyObj.keys();
							if(i.hasNext()) {
								String obj = i.next().toString();
								if(obj.startsWith("m2m:")) {
									String rsType = obj.split(":")[1];
									nType = SHORT_RESOURCE_TYPE.get(rsType).Value();
								} else {
									throw new OneM2MException(OneM2mResponse.RESPONSE_STATUS.INVALID_ARGUMENTS, "Invalid format");
								}	
							}
						} else {
							nType = reqPrimitive.getResourceType();
						}
						
						//int nType = reqPrimitive.getResourceType();
						String resourceType = RESOURCE_TYPE.get(nType).Name();
						resourceType = net.herit.iot.onem2m.bind.mqtt.util.Utils.stringFirstUppper(resourceType);	// added in 2017-10-26
						
						String pkgName = "net.herit.iot.onem2m.resource"; 
						Class<?> classObj = Class.forName(pkgName + "." + resourceType);
						String schemaLoc = (String)classObj.getField("SCHEMA_LOCATION").get( classObj.newInstance() );
						
						JSONConvertor<?> jsonCvt = ConvertorFactory.getJSONConvertor(classObj, schemaLoc);
					
						Resource resource = (Resource)jsonCvt.unmarshal(jsonBodyObj.toString());
						
						reqMessage.setResultContent(reqPrimitive.getResultContent());
						
						if (reqMessage.getResultContentEnum().equals(RESULT_CONT.NONE)) {
							OPERATION operation = reqMessage.getOperationEnum();
							reqMessage.setResultContent(net.herit.iot.onem2m.bind.codec.Utils.getDefaultResultContent(operation));
						}
						
						reqMessage.setContentObject(resource);
					}
					
				}
				
				break;
			default:
				throw new OneM2MException(OneM2mResponse.RESPONSE_STATUS.INVALID_ARGUMENTS, "Invalid format");
			}
		} catch(Exception e) {
			e.printStackTrace();
			throw new OneM2MException(OneM2mResponse.RESPONSE_STATUS.INVALID_ARGUMENTS, "Invalid format");
		}
		
		if(reqMessage.getResultContentEnum() == RESULT_CONT.NONE) {
			reqMessage.setResultContent(getDefaultResultContent(reqMessage.getOperationEnum()));
		}
		reqMessage.setContentType(contentType);
		if(reqMessage.getTo().startsWith("/~")) {	// added in 2017-12-08
			String tmpTo = reqMessage.getTo();
			reqMessage.setTo(tmpTo.substring(2));
		}
		/*
		if(reqMessage.getPrimitiveContent() != null) {
		//	System.out.println(reqMessage.getPrimitiveContent().getAny().size());  updated as below due to CDT-2.7.0
			System.out.println(reqMessage.getPrimitiveContent().getAnyOrAny().size());
		} else {
			System.out.println("primitiveContent is null.");
		}
		*/
		return reqMessage;
	}
	
	
	private static CONTENT_TYPE getContentType(String strContents) {
		String contents = strContents.trim();
		CONTENT_TYPE contentType = CONTENT_TYPE.NONE;
		if(contents.startsWith("<")) {				// XML format
			contentType = CONTENT_TYPE.XML;
		} else if(contents.startsWith("{")) {		// JSON format
			contentType = CONTENT_TYPE.JSON;
		} else {			/// added in 2017-12-08 to support NTELS
			contentType = CONTENT_TYPE.CBOR;
		}
		
		return contentType;
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
