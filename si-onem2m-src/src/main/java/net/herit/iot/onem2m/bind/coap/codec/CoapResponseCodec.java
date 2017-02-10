package net.herit.iot.onem2m.bind.coap.codec;


import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.coap.Option;
import org.eclipse.californium.core.coap.OptionNumberRegistry;
import org.eclipse.californium.core.coap.Response;
import org.eclipse.californium.core.server.resources.CoapExchange;

import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.message.onem2m.OneM2mResponse.RESPONSE_STATUS;
import net.herit.iot.message.onem2m.format.Enums.CONTENT_TYPE;
import net.herit.iot.onem2m.bind.coap.codec.CoapAbsCodec.COAP_CONTENT_TYPE;


public class CoapResponseCodec extends CoapAbsCodec {

	private static ResponseCode getCoapResponseCode(RESPONSE_STATUS oneM2mResCode) {

		switch(oneM2mResCode) {
		//UNDEFINED(-1,
		//NETWORK_FAILURE(9001,
		//COMMAND_TIMEOUT(9002,
		//DMSERVER_ERROR(9101,
		//BAD_RESPONSE(9102,
		
//		case ACCEPTED:						// ???
//			return ResponseCode.CONTENT;

		case OK:
			return ResponseCode.CONTENT;
		case CREATED:
			return ResponseCode.CREATED;
		case DELETED:
			return ResponseCode.DELETED;
		case CHANGED:
			return ResponseCode.CHANGED;
		case BAD_REQUEST:
			return ResponseCode.BAD_REQUEST;
		//case UNAUTHORIZED(4001, "UNAUTHORIZED"),	// 401 (Unauthroized)		----- LGU+ Defined status code
		case NOT_FOUND:
			return ResponseCode.NOT_FOUND;
		case OPERATION_NOT_ALLOWED:
			return ResponseCode.METHOD_NOT_ALLOWED;
		case REQUEST_TIMEOUT:
			return ResponseCode.NOT_FOUND;
		case SUBSCRIP_CREATOR_NO_PRIVILEGE:
			return ResponseCode.FORBIDDEN;
		case CONTENTS_UNACCEPTABLE:
			return ResponseCode.BAD_REQUEST;
		case ACCESS_DENIED:
			return ResponseCode.FORBIDDEN;
		case GROUP_REQ_ID_EXISTS:
			return ResponseCode.BAD_REQUEST;
		case CONFLICT:
			return ResponseCode.FORBIDDEN;
		case INTERNAL_SERVER_ERROR:
			return ResponseCode.INTERNAL_SERVER_ERROR;
		case NOT_IMPLEMENTED:
			return ResponseCode.NOT_IMPLEMENTED;
		case TARGET_NOT_REACHABLE:
			return ResponseCode.NOT_FOUND;
		case NO_PRIVILEGE:
			return ResponseCode.FORBIDDEN;
		case ALREADY_EXISTS:
			return ResponseCode.BAD_REQUEST;
		case TARGET_NOT_SUBSCRIBABLE:
			return ResponseCode.FORBIDDEN;
		case SUBSCRIP_VERIFY_INIT_FAILED:
			return ResponseCode.INTERNAL_SERVER_ERROR;
		case SUBSCRIP_HOST_NO_PRIVILEGE:
			return ResponseCode.FORBIDDEN;
		case NON_BLOCK_REQ_NOT_SUPPORTED:
			return ResponseCode.INTERNAL_SERVER_ERROR;
		case EXTERNAL_OBJECT_NOT_REACHABLE:
			return ResponseCode.NOT_FOUND;
		case EXTERNAL_OBJECT_NOT_FOUND:
			return ResponseCode.NOT_FOUND;
		case MAX_NUMBER_OF_MEMBER_EXCEEDED:
			return ResponseCode.BAD_REQUEST;
		case MEMBER_TYPE_INCONSISTENT:
			return ResponseCode.BAD_REQUEST;
		case MGMT_SESSION_CANNOT_ESTABLISH:
			return ResponseCode.INTERNAL_SERVER_ERROR;
		case MGMT_SESSION_ESTABLISH_TIMEOUT:
			return ResponseCode.INTERNAL_SERVER_ERROR;
		case INVALID_CMDTYPE:
			return ResponseCode.BAD_REQUEST;
		case INVALID_ARGUMENTS:
			return ResponseCode.BAD_REQUEST;
		case INSUFFICIENT_ARGUMENT:
			return ResponseCode.BAD_REQUEST;
		case MGMT_CONVERSION_ERROR:
			return ResponseCode.INTERNAL_SERVER_ERROR;
		case CANCELLATION_FAILED:
			return ResponseCode.INTERNAL_SERVER_ERROR;
		case ALREADY_COMPLETE:
			return ResponseCode.BAD_REQUEST;
		case COMMAND_NOT_CANCELLABLE:
			return ResponseCode.BAD_REQUEST;
		default:
			return ResponseCode._UNKNOWN_SUCCESS_CODE;
		}
	}
	
	public static Response encode(OneM2mResponse resMessage, CoapExchange exchange) throws Exception {
		Response resCoap = null;
		
		RESPONSE_STATUS resCode = resMessage.getResponseStatusCodeEnum();
		
		resCoap = Response.createResponse(exchange.advanced().getRequest(), getCoapResponseCode(resCode));
		
		if (resMessage.getContent() != null && resMessage.getContent().length > 0) {
			resCoap.setPayload(resMessage.getContent());
		}
		
		resCoap.getOptions().addOption(new Option(ONEM2M_RSC, resCode.Value()));
		if (resMessage.getRequestIdentifier() != null) {
			resCoap.getOptions().addOption(new Option(ONEM2M_RQI, resMessage.getRequestIdentifier()));
		} else {
			throw new Exception("Not Setting RequestIdentifier");
		}
		if (resMessage.getFrom() != null) {
			resCoap.getOptions().addOption(new Option(ONEM2M_FR, resMessage.getFrom()));
		}
		if (resMessage.getOriginatingTimestamp() != null) {
			resCoap.getOptions().addOption(new Option(ONEM2M_OT, resMessage.getOriginatingTimestamp()));
		}
		if (resMessage.getResultExpirationTimestamp() != null) {
			resCoap.getOptions().addOption(new Option(ONEM2M_RSET, resMessage.getResultExpirationTimestamp()));
		}
		if (resMessage.getEventCategory() != null) {
			resCoap.getOptions().addOption(new Option(ONEM2M_EC, resMessage.getEventCategory()));
		}
		
		if(resMessage.getContentLocation() != null) {  // added 2016.05.13
			resCoap.getOptions().setLocationPath(resMessage.getContentLocation());
		}
		
		if (resMessage.getContentType() != null && resMessage.getContentType() != CONTENT_TYPE.NONE  &&
				resMessage.getContent() != null && resMessage.getContent().length > 0) {
//			System.out.println("RES>> Content-Type: " + resMessage.getContentType());
			COAP_CONTENT_TYPE contentType = COAP_CONTENT_TYPE.getCoapContentType(resMessage.getContentType());
			resCoap.getOptions().setContentFormat(contentType.Value());
		}
		
		return resCoap;
	}
	
	
	public static OneM2mResponse decode(CoapResponse resCoap) throws Exception {
		
		OneM2mResponse resMessage = new OneM2mResponse();

		if (resCoap.advanced().getSource().getHostName() != null) {
			resMessage.setRemoteHost(resCoap.advanced().getSource().getHostName());
		}
		
		for (Option option : resCoap.getOptions().asSortedList()) {
			int number = option.getNumber();
			String value = option.getStringValue().trim();
			
			switch(number) {
			
			case OptionNumberRegistry.CONTENT_FORMAT:
				CONTENT_TYPE type = COAP_CONTENT_TYPE.getContentType(option.getIntegerValue());
				if(type != null && type != CONTENT_TYPE.NONE) {
					resMessage.setContentType(type);
				}
				break;
			
			case ONEM2M_RSC:
				resMessage.setResponseStatusCode(option.getIntegerValue());
				break;
			case ONEM2M_RQI:
				resMessage.setRequestIdentifier(value);
				break;
			case ONEM2M_FR:
				resMessage.setFrom(value);
				break;
			case ONEM2M_OT:
				resMessage.setOriginatingTimestamp(value);
				break;
			case ONEM2M_RSET:
				resMessage.setResultExpirationTimestamp(value);
				break;
			case ONEM2M_EC:
				resMessage.setEventCategory(option.getIntegerValue());
				break;
			}
		}

		if (resCoap.getPayload() != null && resCoap.getPayload().length > 0) {
			resMessage.setContent(resCoap.getPayload());
		}
		
		return resMessage;
	}
}
