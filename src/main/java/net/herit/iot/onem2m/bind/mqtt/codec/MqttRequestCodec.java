package net.herit.iot.onem2m.bind.mqtt.codec;

import java.util.Iterator;

import org.json.JSONObject;

import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mRequest.SHORT_RESOURCE_TYPE;
import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.message.onem2m.OneM2mRequest.OPERATION;
import net.herit.iot.message.onem2m.OneM2mRequest.RESOURCE_TYPE;
import net.herit.iot.message.onem2m.OneM2mRequest.RESPONSE_TYPE;
import net.herit.iot.message.onem2m.OneM2mRequest.RESULT_CONT;
import net.herit.iot.message.onem2m.format.Enums.CONTENT_TYPE;
import net.herit.iot.onem2m.core.convertor.ConvertorFactory;
import net.herit.iot.onem2m.core.convertor.JSONConvertor;
import net.herit.iot.onem2m.core.convertor.XMLConvertor;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.resource.RequestPrimitive;
import net.herit.iot.onem2m.resource.Resource;
import net.herit.iot.onem2m.bind.codec.JSONSerializer;
import net.herit.iot.onem2m.bind.mqtt.util.Utils;
import net.herit.iot.onem2m.resource.*;

public class MqttRequestCodec {
	
	private static final XMLConvertor<OneM2mRequest> xmlConvertor = 
			new XMLConvertor<OneM2mRequest>(OneM2mRequest.class, OneM2mRequest.SCHEMA_LOCATION);
	private static final JSONConvertor<OneM2mRequest> decodeJsonConvertor =
			new JSONConvertor<OneM2mRequest>(OneM2mRequest.class);
//	private static final JSONConvertor<RequestPrimitive> encodeJsonConvertor =
//			new JSONConvertor<RequestPrimitive>(RequestPrimitive.class);
	private static final JSONConvertor<OneM2mRequest> encodeJsonConvertor =
			new JSONConvertor<OneM2mRequest>(OneM2mRequest.class);
	
	private final static String ONEM2M_REQUEST_MESSAGE = "m2m:rqp";
	protected final static String ONEM2M_TY	= "ty";
	protected final static String ONEM2M_OPERATION = "op";
	protected final static String ONEM2M_TO = "to";
	protected final static String ONEM2M_REQUEST_IDENTIFIER = "rqi";
	protected final static String ONEM2M_CONTENT = "pc";
	protected final static String ONEM2M_FROM = "fr";
	protected final static String ONEM2M_RESPONSE_CODE	= "rsc";
	
	public static OneM2mRequest decode(byte[] contents) throws OneM2MException {

		OneM2mRequest reqMessage = null;
		RequestPrimitive reqPrimitive = null;
		CONTENT_TYPE contentType = CONTENT_TYPE.NONE;
		
		try {
			String strContents = new String(contents, "UTF-8").trim();

			contentType = Utils.getContentType(strContents);
			//System.out.println("contentType =======> " + contentType.Name());
			switch (contentType) {
			case XML:
			case ATTRS_XML:
			case NTFY_XML:
			case RES_XML:
				//reqMessage = (OneM2mRequest)xmlConvertor.unmarshal(strContents);	blocked in 2017-09-01
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
				int op = Utils.getOneM2mOperation(reqPrimitive.getOperation(), to);	// added in 2017-11-03 for operation matching to OneM2mRequest.OPERATION
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
			// Added in 2017-05-23 to decode CBOR into JSON string
			case CBOR:
			case NTFY_CBOR:
			case RES_CBOR:
				strContents = net.herit.iot.onem2m.core.util.Utils.decodeCBOR(contents);
			
			case JSON:
			case ATTRS_JSON:
			case NTFY_JSON:
			case RES_JSON:
				
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
					op = Utils.getOneM2mOperation(reqPrimitive.getOperation(), to);	// added in 2017-11-03 for operation matching to OneM2mRequest.OPERATION
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
				System.out.println("#### reqMessage => " + reqMessage.toString());
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
		//	System.out.println(reqMessage.getPrimitiveContent().getAny().size());  updated as below due to CDT-2.7.0
			System.out.println(reqMessage.getPrimitiveContent().getAnyOrAny().size());
		} else {
			System.out.println("primitiveContent is null.");
		}
		
		return reqMessage;

	}

	public static byte[] encode(OneM2mRequest reqMessage) throws OneM2MException {

		String strContents = null;
		CONTENT_TYPE contentType = reqMessage.getContentType();

		//RequestPrimitive reqPrimitive = (RequestPrimitive)reqMessage;
		
		try {

			switch (contentType) {
			case XML:
			case ATTRS_XML:
			case NTFY_XML:
			case RES_XML:
				int op = reqMessage.getOperation();
				String to = reqMessage.getTo();
				reqMessage.setOperation(Utils.getMqttOperation(op, to));	// added in 2017-11-03 to comply with MQTT requestPrimitive operation.
				strContents = xmlConvertor.marshal(reqMessage);
				System.out.println("################# strContents(MqttRequestCodec:encode) = " + strContents);
				strContents = strContents.replaceAll("rqp", ONEM2M_REQUEST_MESSAGE);		// added in 2017-09-01 to add prefix "m2m:"
				
				if(strContents.indexOf("oneM2MRequest") > 0) {
					System.out.println("===============================> xsi:type=oneM2MRequest is in the current message yet..... ");
					int nFirst = strContents.indexOf("xsi:type");
					int nLast = strContents.indexOf("oneM2MRequest");
					String firstStr = strContents.substring(0, nFirst-1);
					String lastStr = strContents.substring(nLast + 14);
					strContents = firstStr + lastStr;
					
				} else {
					System.out.println("==================================> NOOOOO oneM2MRequest in xsi:type");
				}
				strContents = strContents.replaceAll("[\n\r\t]", "");   // added in 2017-11-02 to support TTA
				strContents = strContents.replaceAll(">\\s+<", "><");
				break;
				
			case JSON:
			case ATTRS_JSON:
			case NTFY_JSON:
			case RES_JSON:
				//strContents = encodeJsonConvertor.marshal(reqMessage);    blocked in 2017-10-18
				// added in 2017-10-18
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
