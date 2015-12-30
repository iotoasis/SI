package net.herit.iot.onem2m.bind.http.status;

import net.herit.iot.message.onem2m.OneM2mResponse.RESPONSE_STATUS;
import io.netty.handler.codec.http.HttpResponseStatus;

public class OneM2mResponseStatus extends HttpResponseStatus {

	public OneM2mResponseStatus(int code, String reasonPhrase) {
		super(code, reasonPhrase);
	}
	/**  OneM2M Code (Status) **/
	/** 1000 (ACCEPTED) **/
	public static final HttpResponseStatus ACCEPTED					= new HttpResponseStatus(202, "Accecpted");
	/** 2001 (CREATED) **/
	public static final HttpResponseStatus CREATED					= new HttpResponseStatus(201, "Created");
	/** 2002 (DELETED) **/
	public static final HttpResponseStatus DELETED					= new HttpResponseStatus(200, "Deleted");
	/** 2004 (CHANGED) **/
	public static final HttpResponseStatus CHANGED					= new HttpResponseStatus(200, "Changed");
	/** 2000 (OK) **/
	public static final HttpResponseStatus OK					= new HttpResponseStatus(200, "OK");
	/** 2101 (?) (CONFLICT) **/
	public static final HttpResponseStatus CONFLICT					= new HttpResponseStatus(409, "Conflict");
	/** 4000 (BAD_REQUEST) **/
	public static final HttpResponseStatus BAS_REQUEST				= new HttpResponseStatus(400, "Bad Request");
	/** 4004 (NOT_FOUND) **/
	public static final HttpResponseStatus NOT_FOUND				= new HttpResponseStatus(404, "Not Found"	);
	/** 4005 (OPERATION_NOT_ALLOWED) **/
	public static final HttpResponseStatus OPERATION_NOT_ALLOWED	= new HttpResponseStatus(405, "Method Not Allowed");
	/** 4008 (REQUEST_TIMEOUT) **/
	public static final HttpResponseStatus REQUEST_TIMEOUT			= new HttpResponseStatus(408, "Request Timeout");
	/** 4101 (SUBSCRIPTION_CREATOR_HAS_NO_PRIVILEGE) **/
	public static final HttpResponseStatus SUBSCRIP_CREATOR_NO_PRIVILEGE = new HttpResponseStatus(403, "Forbidden");
	/** 4102 (CONTENTS_UNACCEPTABLE) **/
	public static final HttpResponseStatus CONTENTS_UNACCEPTABLE	= new HttpResponseStatus(400, "Bad Request");
	/** 4103 (ACCESS_DENIED) **/
	public static final HttpResponseStatus ACCESS_DENIED			= new HttpResponseStatus(403, "Forbidden");
	/** 4104 (GROUP_REQUEST_IDENTIFIER_EXISTS) **/
	public static final HttpResponseStatus GROUP_REQ_ID_EXISTS		= new HttpResponseStatus(409, "Conflict");
	/** 5000 (INTERNAL_SERVER_ERROR) **/
	public static final HttpResponseStatus INTERNAL_SERVER_ERROR	= new HttpResponseStatus(500, "Internal Server Error");
	/** 5001 (NOT_IMPLEMENTED) **/
	public static final HttpResponseStatus NOT_IMPLEMENTED			= new HttpResponseStatus(501, "Not Implemented");
	/** 5103 (TARGET_NOT_REACHABLE) **/
	public static final HttpResponseStatus TARGET_NOT_REACHABLE	= new HttpResponseStatus(404, "Not Found"	);
	/** 5105 (NO_PRIVILEGE) **/
	public static final HttpResponseStatus NO_PRIVILEGE				= new HttpResponseStatus(403, "Forbidden");
	/** 5106 (ALREADY_EXISTS) **/
	public static final HttpResponseStatus ALREADY_EXISTS			= new HttpResponseStatus(403, "Forbidden");
	/** 5203 (TARGET_NOT_SUBSCRIBABLE) **/
	public static final HttpResponseStatus TARGET_NOT_SUBSCRIBABLE	= new HttpResponseStatus(403, "Forbidden");
	/** 5204 (SUBSCRIPTION_VERIFICATION_INITIATION_FAILED) **/
	public static final HttpResponseStatus SUBSCRIP_VERIFY_INIT_FAILED	= new HttpResponseStatus(500, "Internal Server Error");
	/** 5205 (SUBSCRIPTION_HOST_HAS_NO_PRIVILEGE) **/
	public static final HttpResponseStatus SUBSCRIP_HOST_NO_PRIVILEGE		= new HttpResponseStatus(403, "Forbidden");
	/** 5206 (NON_BLOCKING_REQUEST_NOT_SUPPORTED) **/
	public static final HttpResponseStatus NON_BLOCK_REQ_NOT_SUPPORTED	= new HttpResponseStatus(501, "Not Implemented");
	/** 6003 (EXTERNAL_OBJECT_NOT_REACHABLE) **/
	public static final HttpResponseStatus EXTERNAL_OBJECT_NOT_REACHABLE	= new HttpResponseStatus(404, "Not Found"	);
	/** 6005 (EXTERNAL_OBJECT_NOT_FOUND) **/
	public static final HttpResponseStatus EXTERNAL_OBJECT_NOT_FOUND		= new HttpResponseStatus(404, "Not Found"	);
	/** 6010 (MAX_NUMBER_OF_MEMBER_EXCEEDED) **/
	public static final HttpResponseStatus MAX_NUMBER_OF_MEMBER_EXCEEDED	= new HttpResponseStatus(400, "Bad Request");
	/** 6011 (MEMBER_TYPE_INCONSISTENT) **/
	public static final HttpResponseStatus MEMBER_TYPE_INCONSISTENT		= new HttpResponseStatus(400, "Bad Request");
	/** 6020 (MANAGEMENT_SESSION_CANNOT_BE_ESTABLISHED) **/
	public static final HttpResponseStatus MGMT_SESSION_CANNOT_ESTABLISH	= new HttpResponseStatus(500, "Internal Server Error");
	/** 6021 (MANAGEMENT_SESSION_ESTABLISHMENT_TIMEOUT) **/
	public static final HttpResponseStatus MGMT_SESSION_ESTABLISH_TIMEOUT= new HttpResponseStatus(500, "Internal Server Error");
	/** 6022 (INVALID_CMDTYPE) **/
	public static final HttpResponseStatus INVALID_CMDTYPE			= new HttpResponseStatus(400, "Bad Request");
	/** 6023 (INVALID_ARGUMENTS) **/
	public static final HttpResponseStatus INVALID_ARGUMENTS		= new HttpResponseStatus(400, "Bad Request");
	/** 6024 (INSUFFICIENT_ARGUMENT) **/
	public static final HttpResponseStatus INSUFFICIENT_ARGUMENT	= new HttpResponseStatus(400, "Bad Request");
	/** 6025 (MGMT_CONVERSION_ERROR) **/
	public static final HttpResponseStatus MGMT_CONVERSION_ERROR	= new HttpResponseStatus(500, "Internal Server Error");
	/** 6026 (CANCELLATION_FAILED) **/
	public static final HttpResponseStatus CANCELLATION_FAILED		= new HttpResponseStatus(500, "Internal Server Error");
	/** 6028 (ALREADY_COMPLETE) **/
	public static final HttpResponseStatus ALREADY_COMPLETE		= new HttpResponseStatus(400, "Bad Request");
	/** 6029 (COMMAND_NOT_CANCELLABLE) **/
	public static final HttpResponseStatus COMMAND_NOT_CANCELLABLE	= new HttpResponseStatus(400, "Bad Request");

	

	public static HttpResponseStatus valueOf(RESPONSE_STATUS code) {
		switch (code) {
		case ACCEPTED:
			return ACCEPTED;
		case OK:
			return OK;
		case CREATED:
			return CREATED;
		case CONFLICT:
			return CONFLICT;
		case BAD_REQUEST:
			return BAD_REQUEST;
		case NOT_FOUND:
			return NOT_FOUND;
		case OPERATION_NOT_ALLOWED:
			return OPERATION_NOT_ALLOWED;
		case REQUEST_TIMEOUT:
			return REQUEST_TIMEOUT;
		case SUBSCRIP_CREATOR_NO_PRIVILEGE:
			return SUBSCRIP_CREATOR_NO_PRIVILEGE;
		case CONTENTS_UNACCEPTABLE:
			return CONTENTS_UNACCEPTABLE;
		case ACCESS_DENIED:
			return ACCESS_DENIED;
		case GROUP_REQ_ID_EXISTS:
			return GROUP_REQ_ID_EXISTS;
		case INTERNAL_SERVER_ERROR:
			return INTERNAL_SERVER_ERROR;
		case NOT_IMPLEMENTED:
			return NOT_IMPLEMENTED;
		case TARGET_NOT_REACHABLE:
			return TARGET_NOT_REACHABLE;
		case NO_PRIVILEGE:
			return NO_PRIVILEGE;
		case ALREADY_EXISTS:
			return ALREADY_EXISTS;
		case TARGET_NOT_SUBSCRIBABLE:
			return TARGET_NOT_SUBSCRIBABLE;
		case SUBSCRIP_VERIFY_INIT_FAILED:
			return SUBSCRIP_VERIFY_INIT_FAILED;
		case SUBSCRIP_HOST_NO_PRIVILEGE:
			return SUBSCRIP_HOST_NO_PRIVILEGE;
		case NON_BLOCK_REQ_NOT_SUPPORTED:
			return NON_BLOCK_REQ_NOT_SUPPORTED;
		case EXTERNAL_OBJECT_NOT_REACHABLE:
			return EXTERNAL_OBJECT_NOT_REACHABLE;
		case EXTERNAL_OBJECT_NOT_FOUND:
			return EXTERNAL_OBJECT_NOT_FOUND;
		case MAX_NUMBER_OF_MEMBER_EXCEEDED:
			return MAX_NUMBER_OF_MEMBER_EXCEEDED;
		case MEMBER_TYPE_INCONSISTENT:
			return MEMBER_TYPE_INCONSISTENT;
		case MGMT_SESSION_CANNOT_ESTABLISH:
			return MGMT_SESSION_CANNOT_ESTABLISH;
		case MGMT_SESSION_ESTABLISH_TIMEOUT:
			return MGMT_SESSION_ESTABLISH_TIMEOUT;
		case INVALID_CMDTYPE:
			return INVALID_CMDTYPE;
		case INVALID_ARGUMENTS:
			return INVALID_ARGUMENTS;
		case INSUFFICIENT_ARGUMENT:
			return INSUFFICIENT_ARGUMENT;
		case MGMT_CONVERSION_ERROR:
			return MGMT_CONVERSION_ERROR;
		case CANCELLATION_FAILED:
			return CANCELLATION_FAILED;
		case ALREADY_COMPLETE:
			return ALREADY_COMPLETE;
		case COMMAND_NOT_CANCELLABLE:
			return COMMAND_NOT_CANCELLABLE;
		default:
			return OK;
		}
	}
}
