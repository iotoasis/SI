package net.herit.iot.onem2m.bind.coap.codec;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.californium.core.coap.CoAP.Code;
import org.eclipse.californium.core.coap.Option;
import org.eclipse.californium.core.coap.OptionNumberRegistry;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.core.network.Exchange;
import org.eclipse.californium.core.server.resources.CoapExchange;

import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mRequest.OPERATION;
import net.herit.iot.message.onem2m.OneM2mRequest.RESOURCE_TYPE;
import net.herit.iot.message.onem2m.OneM2mRequest.RESULT_CONT;
import net.herit.iot.message.onem2m.format.Enums.CONTENT_TYPE;
import net.herit.iot.message.onem2m.format.Enums.FILTER_USAGE;
import net.herit.iot.onem2m.bind.codec.Utils;
import net.herit.iot.onem2m.resource.FilterCriteria;

public class CoapRequestCodec extends CoapAbsCodec {
	


	public static OneM2mRequest decode(CoapExchange exchange) throws Exception {
		OneM2mRequest reqMessage = new OneM2mRequest();
		
		reqMessage.setRemoteHost(exchange.getSourceAddress().getHostName());
		
		String coapUri = "/" + exchange.getRequestOptions().getUriPathString();
		if(exchange.getRequestOptions().getUriQueryString().length() > 0) {
			coapUri += "?" + exchange.getRequestOptions().getUriQueryString();
		}
		
		HashMap<String, Object> queries = Utils.urlQueryParse(coapUri);
		Utils.decodeQueryString(reqMessage, queries);
		
		// Header decode. (Coap Options)
		List<Option> optionList = exchange.getRequestOptions().asSortedList();
		for (Option option : optionList) {
			int number = option.getNumber();
			String value = option.getStringValue().trim();
		
			switch(number) {
			// Defined Option in Coap
			case OptionNumberRegistry.ACCEPT:
				CONTENT_TYPE type = COAP_CONTENT_TYPE.getContentType(option.getIntegerValue());
				if(type != null && type != CONTENT_TYPE.NONE) {
					reqMessage.addAcceptType(type);
				}
				break;
			case OptionNumberRegistry.CONTENT_FORMAT:
				CONTENT_TYPE type2 = COAP_CONTENT_TYPE.getContentType(option.getIntegerValue());
				if(type2 != null && type2 != CONTENT_TYPE.NONE) {
					reqMessage.setContentType(type2);
				}
				break;
				
			
			// Defined Option in OneM2M
			case ONEM2M_FR:
				reqMessage.setFrom(value);
				break;
			case ONEM2M_RQI:
				reqMessage.setRequestIdentifier(value);
				break;
//			case ONEM2M_NM:
//				reqMessage.setName(value);  removed. XSD-1.6.0
//				break;
			case ONEM2M_OT:
				reqMessage.setOriginatingTimestamp(value);
				break;
			case ONEM2M_RQET:
				reqMessage.setRequestExpirationTimestamp(value);
				break;
			case ONEM2M_RSET:
				reqMessage.setResultExpirationTimestamp(value);
				break;
			case ONEM2M_OET:
				reqMessage.setOperationExecutionTime(value);
				break;
			case ONEM2M_RTURI:
				if (null != value) {
					String[] split = value.split("&");
					for (String uri : split) {
						reqMessage.addNotificationUri(uri);
					}
				}
				break;
			case ONEM2M_EC:
				reqMessage.setEventCategory(option.getIntegerValue());		// User defined value: 100 ~ 999
				break;
			case ONEM2M_GID:
				reqMessage.setGroupRequestIdentifier(value);
				break;
			case ONEM2M_TY:
				RESOURCE_TYPE resourceType = RESOURCE_TYPE.get(option.getIntegerValue());
				reqMessage.setResourceType(resourceType);
			
			}
		}

		FilterCriteria filterCriteria = reqMessage.getFilterCriteria();
		OPERATION operation = OPERATION.NONE;
		switch(exchange.getRequestCode()) {
		case GET:
			if(filterCriteria != null && filterCriteria.getFilterUsage() == FILTER_USAGE.DISCOVERY.Value()) {
				operation = OPERATION.DISCOVERY;
			} else {
				operation = OPERATION.RETRIEVE;
			}
			break;
		
		case DELETE:
			operation = OPERATION.DELETE;
			break;
			
		case PUT:
			operation = OPERATION.UPDATE;
			break;
			
		case POST:
			if(reqMessage.getResourceTypeEnum() != OneM2mRequest.RESOURCE_TYPE.NONE) {
				operation = OPERATION.CREATE;
			} else {
				if (reqMessage.getContentType() == CONTENT_TYPE.NTFY_JSON ||
						 reqMessage.getContentType() == CONTENT_TYPE.NTFY_XML||
						 reqMessage.getContentType() == CONTENT_TYPE.NTFY_CBOR) {		// added in 2017-05-23
					operation = OPERATION.NOTIFY;
				}
//				else {   // 2016.05.13 removed.
//					operation = OPERATION.UPDATE;
//				}
			}
			break;
			
		}
		reqMessage.setOperation(operation);
		// TODO: if operation is NONE..?

		if (reqMessage.getResultContentEnum().equals(RESULT_CONT.NONE)) {
			reqMessage.setResultContent(Utils.getDefaultResultContent(operation));
		}
		
		if(exchange.getRequestPayload() != null) {
			// added in 2017-05-23 to support CBOR bind stream format
			if(reqMessage.getContentType() != null && reqMessage.getContentType().Name().toUpperCase().indexOf("CBOR") > 0) {
				String strContent = net.herit.iot.onem2m.core.util.Utils.decodeCBOR(exchange.getRequestPayload());
				reqMessage.setContent(strContent.getBytes(StandardCharsets.UTF_8));
			} else {
				reqMessage.setContent(exchange.getRequestPayload());
			}
			//reqMessage.setContent(exchange.getRequestPayload());		// blocked in 2017-05-23
		}
		
		return reqMessage;
	}
	
	
	
	public static Request encode(OneM2mRequest reqMessage) throws Exception {
		Request reqCoap = null;
		
		switch(reqMessage.getOperationEnum()) {
		case RETRIEVE:
			reqCoap = Request.newGet();
			break;
		
		case DELETE:
			reqCoap = Request.newDelete();
			break;
			
		case UPDATE:
			reqCoap = Request.newPut();
			break;
			
		case CREATE:
			reqCoap = Request.newPost();
			if(reqMessage.getResourceType() != null) {
				reqCoap.getOptions().addOption(new Option(ONEM2M_TY, reqMessage.getResourceTypeEnum().Value()));
			} else {
				throw new Exception("Not setting Resource type");
			}
			break;
			
		case NOTIFY:
			reqCoap = Request.newPost();
			break;
			
		case DISCOVERY:
			reqCoap = Request.newGet();
			break;
			
		default:
			throw new Exception("Unknown Operation");
		}
		
		String queryString = Utils.makeQueryString(reqMessage);
		
		//Generate Query string..
		String path = null;
		if (reqMessage.getTo() != null) {
			if(!reqMessage.getTo().startsWith("/")) {
				path = "/" + reqMessage.getTo();
			} else {
				path = reqMessage.getTo();
			}
		} else {
			path = "/";
		}
		reqCoap.getOptions().setUriPath(path);
		if(queryString != null) {
			reqCoap.getOptions().setUriQuery(queryString);
		}
		

		int cont_len = reqMessage.getContent() != null ? reqMessage.getContent().length : 0;
		if(cont_len > 0) {
			reqCoap.setPayload(reqMessage.getContent());

			COAP_CONTENT_TYPE contentType = COAP_CONTENT_TYPE.NONE;
			// TODO: content-type -> CONTENT_TYPE.NTFY_JSON or NTFY_XML
			if (reqMessage.getOperationEnum() == OPERATION.NOTIFY) {
				CONTENT_TYPE cont_type = reqMessage.getContentType();
				
				switch(cont_type) {
				// CBOR added in 2017-05-12
				case CBOR:
				case RES_CBOR:
				case NTFY_CBOR:
					contentType = COAP_CONTENT_TYPE.NTFY_CBOR;
					break;
				
				case JSON:
				case RES_JSON:
				case NTFY_JSON:
				case ATTRS_JSON:
					contentType = COAP_CONTENT_TYPE.NTFY_JSON;
					break;
				
				case XML:
				case RES_XML:
				case NTFY_XML:
				case ATTRS_XML:
				default:
					contentType = COAP_CONTENT_TYPE.NTFY_XML;
					break;
				}
			} else {
				if (reqMessage.getContentType() == null) {
					contentType = COAP_CONTENT_TYPE.NONE;
				}
				else if (reqMessage.getContentType() != null &&
						reqMessage.getContentType() != CONTENT_TYPE.NONE) {
					contentType = COAP_CONTENT_TYPE.getCoapContentType(reqMessage.getContentType());
				} else {
					contentType = COAP_CONTENT_TYPE.XML;
				}
			}
			if(contentType != COAP_CONTENT_TYPE.NONE) {
				reqCoap.getOptions().setContentFormat(contentType.Value());
			}
		}

		if(reqMessage.getAcceptTypes() != null && reqMessage.getAcceptTypes().size() > 0) {
//			System.out.println("AcceptType: " + reqMessage.getAcceptTypes().get(0));
//			System.out.println(COAP_CONTENT_TYPE.getCoapContentType(reqMessage.getAcceptTypes().get(0)).Value());
			reqCoap.getOptions().setAccept(COAP_CONTENT_TYPE.getCoapContentType(
					reqMessage.getAcceptTypes().get(0)).Value());
		}
		if (reqMessage.getRequestIdentifier() != null) {
			reqCoap.getOptions().addOption(new Option(ONEM2M_RQI, reqMessage.getRequestIdentifier()));
		} else {
			throw new Exception("Not Setting RequestIdentifier");
		}
		if (reqMessage.getFrom() != null) {
			reqCoap.getOptions().addOption(new Option(ONEM2M_FR, reqMessage.getFrom()));
		}
		if (reqMessage.getOriginatingTimestamp() != null) {
			reqCoap.getOptions().addOption(new Option(ONEM2M_OT, reqMessage.getOriginatingTimestamp()));
		}
		if (reqMessage.getResultExpirationTimestamp() != null) {
			reqCoap.getOptions().addOption(new Option(ONEM2M_RSET, reqMessage.getResultExpirationTimestamp()));
		}
		if (reqMessage.getEventCategory() != null) {
			reqCoap.getOptions().addOption(new Option(ONEM2M_EC, reqMessage.getEventCategory()));
		}
//		if (reqMessage.getName() != null) {
//			reqCoap.getOptions().addOption(new Option(ONEM2M_NM, reqMessage.getName()));
//		}  removed. XSD-1.6.0
		
		if (reqMessage.getRequestExpirationTimestamp() != null) {
			reqCoap.getOptions().addOption(new Option(ONEM2M_RQET, reqMessage.getRequestExpirationTimestamp()));
		}
		if (reqMessage.getOperationExecutionTime() != null) {
			reqCoap.getOptions().addOption(new Option(ONEM2M_OET, reqMessage.getOperationExecutionTime()));
		}
		if (reqMessage.getGroupRequestIdentifier() != null) {
			reqCoap.getOptions().addOption(new Option(ONEM2M_GID, reqMessage.getGroupRequestIdentifier()));
		}
//		if (reqMessage.getNotificationUri() != null && reqMessage.getNotificationUri().size() > 0) {
//			reqCoap.getOptions().addOption(new Option(ONEM2M_RTURI, reqMessage.getNotificationUri().get(0)));
//		}
		
		return reqCoap;
	}
	
}
