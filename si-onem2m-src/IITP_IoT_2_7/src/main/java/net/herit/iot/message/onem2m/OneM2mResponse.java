package net.herit.iot.message.onem2m;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.herit.iot.message.onem2m.OneM2mRequest.OPERATION;
import net.herit.iot.message.onem2m.OneM2mRequest.RESULT_CONT;
import net.herit.iot.message.onem2m.format.Enums.*;
import net.herit.iot.onem2m.bind.codec.AbsSerializer;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.core.util.Utils;
import net.herit.iot.onem2m.incse.facility.CfgManager;
import net.herit.iot.onem2m.resource.Naming;
import net.herit.iot.onem2m.resource.PrimitiveContent;
import net.herit.iot.onem2m.resource.Resource;
import net.herit.iot.onem2m.resource.ResponsePrimitive;
import net.herit.iot.onem2m.resource.UriContent;

@XmlRootElement(name = Naming.RESPONSEPRIMITIVE_SN)
@XmlAccessorType(XmlAccessType.FIELD)			// added in 2017-11-03 to support TTA requirement that "xsi:type=oneM2MResponse" should be deleted..
public class OneM2mResponse extends ResponsePrimitive { // extends AbsMessage {
	
	public enum RESPONSE_STATUS {  // Response Status enum.
		UNDEFINED(-1, "UNDEFINED"), 		// -1 (Internal usage)
		NETWORK_FAILURE(9001, "NETWORK_FAILURE"),		// -1 (Network Failure) - Internal Usage
		COMMAND_TIMEOUT(9002, "COMMAND_TIMEOUT"),		// -1 (Command Timeout) - Internal Usage - SO/SDA 연동
		DMSERVER_ERROR(9101, "DMSERVER ERROR"), 
		BAD_RESPONSE(9102, "BAD_RESPONSE"),		// Invalid response message from remote system (IN-CSE)
		
		ACCEPTED(1000, "ACCEPTED"), 		// 202 (Accepted)
		OK(2000, "OK"),						// 200 (OK)
		CREATED(2001, "CREATED"),			// 201 (Created)
		DELETED(2002, "DELETED"),			// 200 (Deleted)
		CHANGED(2004, "CHANGED"),			// 200 (Changed)
		BAD_REQUEST(4000, "BAD_REQUEST"),	// 400 (Bad Request)
		UNAUTHORIZED(4001, "UNAUTHORIZED"),	// 401 (Unauthroized)		----- LGU+ Defined status code
		NOT_FOUND(4004, "NOT_FOUND"),		// 404 (Not Found)
		OPERATION_NOT_ALLOWED(4005, "OPERATION_NOT_ALLOWED"),	// 405 (Method Not Allowed)
		REQUEST_TIMEOUT(4008, "REQUEST_TIMEOUT"),					// 408 (Request Timeout)
		SUBSCRIP_CREATOR_NO_PRIVILEGE(4101, "SUBSCRIPTION_CREATOR_HAS_NO_PRIVILEGE"), 	// 403 (Forbidden)
		CONTENTS_UNACCEPTABLE(4102, "CONTENTS_UNACCEPTABLE"), 			// 400 (Bad Request)
		ACCESS_DENIED(4103, "ACCESS_DENIED"),								// 403 (Forbidden)
		GROUP_REQ_ID_EXISTS(4104, "GROUP_REQUEST_IDENTIFIER_EXISTS"),	// 409 (Conflict)
		CONFLICT(4105, "CONFLICT"),											// 409 (Conflict)
		INTERNAL_SERVER_ERROR(5000, "INTERNAL_SERVER_ERROR"),			// 500 (Internal Server Error)
		NOT_IMPLEMENTED(5001, "NOT_IMPLEMENTED"), 						// 501 (Not Implemented)
		TARGET_NOT_REACHABLE(5103, "TARGET_NOT_REACHABLE"),				// 404 (Not Found)
		NO_PRIVILEGE(5105, "NO_PRIVILEGE"),								// 403 (Forbidden)
		ALREADY_EXISTS(5106, "ALREADY_EXISTS"),							// 403 (Forbidden)
		TARGET_NOT_SUBSCRIBABLE(5203, "TARGET_NOT_SUBSCRIBABLE"),		// 403 (Forbidden)
		SUBSCRIP_VERIFY_INIT_FAILED(5204, "SUBSCRIPTION_VERIFICATION_INITIATION_FAILED"),		// 500 (Internal Server Error)
		SUBSCRIP_HOST_NO_PRIVILEGE(5205, "SUBSCRIPTION_HOST_HAS_NO_PRIVILEGE"), 	// 403 (Forbidden)
		NON_BLOCK_REQ_NOT_SUPPORTED(5206, "NON_BLOCKING_REQUEST_NOT_SUPPORTED"),	// 501 (Not Implemented)
		NOT_ACCEPTABLE(5207, "NOT_ACCEPTABLE"),										// 501 (Not Implemented)
		EXTERNAL_OBJECT_NOT_REACHABLE(6003, "EXTERNAL_OBJECT_NOT_REACHABLE"),		// 404 (Not Found)
		EXTERNAL_OBJECT_NOT_FOUND(6005, "EXTERNAL_OBJECT_NOT_FOUND"),					// 404 (Not Found)
		MAX_NUMBER_OF_MEMBER_EXCEEDED(6010, "MAX_NUMBER_OF_MEMBER_EXCEEDED"),		// 400 (Bad Request)
		MEMBER_TYPE_INCONSISTENT(6011, "MEMBER_TYPE_INCONSISTENT"),					// 400 (Bad Request)
		MGMT_SESSION_CANNOT_ESTABLISH(6020, "MANAGEMENT_SESSION_CANNOT_BE_ESTABLISHED"),	// 500 (Internal Server Error)
		MGMT_SESSION_ESTABLISH_TIMEOUT(6021, "MANAGEMENT_SESSION_ESTABLISHMENT_TIMEOUT"),	// 500 (Internal Server Error)
		INVALID_CMDTYPE(6022, "INVALID_CMDTYPE"),							// 400 (Bad Request)
		INVALID_ARGUMENTS(6023, "INVALID_ARGUMENTS"),					// 400 (Bad Request)
		INSUFFICIENT_ARGUMENT(6024, "INSUFFICIENT_ARGUMENT"), 			// 400 (Bad Request)
		MGMT_CONVERSION_ERROR(6025, "MGMT_CONVERSION_ERROR"),			// 500 (Internal Server Error)
		CANCELLATION_FAILED(6026, "CANCELLATION_FAILED"),				// 500 (Internal Server Error)
		ALREADY_COMPLETE(6028, "ALREADY_COMPLETE"),						// 400 (Bad Request)	
		COMMAND_NOT_CANCELLABLE(6029, "COMMAND_NOT_CANCELLABLE");		// 400 (Bad Request)
		
		
		
		final int value;
		final String name;
		private RESPONSE_STATUS(int value, String name) {
			this.value = value;
			this.name = name;
		}
		
		public int Value() {
			return this.value;
		}
		
		public String Name() {
			return this.name;
		}
		
		private static final Map<Integer, RESPONSE_STATUS> map = 
				new HashMap<Integer, RESPONSE_STATUS>();
		static {
			for(RESPONSE_STATUS en : RESPONSE_STATUS.values()) {
				map.put(en.value, en);
			}
		}
		
		public static RESPONSE_STATUS get(int value) {
			RESPONSE_STATUS en = map.get(value);
			if(en == null) return UNDEFINED;
			return en;
		}
		
		public static boolean isSuccess(RESPONSE_STATUS status) {
			return status.equals(RESPONSE_STATUS.ACCEPTED) || 
					status.equals(RESPONSE_STATUS.OK) || 
					status.equals(RESPONSE_STATUS.CHANGED) || 
					status.equals(RESPONSE_STATUS.DELETED) || 
					status.equals(RESPONSE_STATUS.CREATED);
		}
	}
	
	private transient Logger log = LoggerFactory.getLogger(OneM2mResponse.class);
	private transient int	httpStatusCode;
	//private RESPONSE_STATUS			resStatusCode = RESPONSE_STATUS.ACCEPTED;

	protected transient String unvalidReason;
	protected transient CONTENT_TYPE		contentType;
	private transient String remoteHost;
	protected transient byte[]	content = null;
	protected transient Object	contentObject = null;
	
//	protected String	requestId = null;
//	protected String	to = null;
//	protected String	from = null;
//	protected String	originTime = null;
//	protected String	resultExpireTime = null;
//	protected int		eventCategory = NOT_SET;		// 100 ~ 999: User defined.
//	protected byte[]	content = null;
	
//	private final static String NOT_RES_STATCODE		= "NOT Response Status Code";
	private final static String NOT_REQUESTID			= "NOT Request Identifier";
	
	protected transient String contentLocation;	// delivered by Content-Location header
	
	//protected Resource	contentResource = null;
	protected transient OneM2mRequest request = null;
	
	public OneM2mResponse() {}
	
	public OneM2mResponse(RESPONSE_STATUS status) {
//		this.resStatusCode = status;
		this.responseStatusCode = status.Value();
	}
	
	public OneM2mResponse(RESPONSE_STATUS status, OneM2mRequest reqMessage) {
//		this.resStatusCode = status;
		this.responseStatusCode = status.Value();
		
		this.request = reqMessage;
		if (request != null) {
			this.setRequestIdentifier(request.getRequestIdentifier());
//			this.setFrom(request.getFrom());
			this.setFrom(CfgManager.getInstance().getAbsoluteCSEBaseId());
			this.setEventCategory(request.getEventCategory());
			this.setRequest(request);

			
			List<CONTENT_TYPE> format = request.getAcceptTypes();
			CONTENT_TYPE reqCt = request.getContentType();
			if(format != null && format.size() > 0) {
				this.setContentType(format.get(0));
			} else if(reqCt != null && reqCt != CONTENT_TYPE.NONE) {
				this.setContentType(reqCt);
			} else {
				
				
				RESULT_CONT rc = request.getResultContentEnum();
				
				boolean supportXml = false;
				if (reqCt == null && (format == null || format.size() == 0)) {
					supportXml = true;
				} else {		
					supportXml = Utils.isXMLContentType(reqCt);
					Iterator<CONTENT_TYPE> it = format.iterator();
					while (!supportXml && it.hasNext()) {
						if (Utils.isXMLContentType(it.next())) {
							supportXml = true;
							break;
						}
					}
				}
				
				if (request.getOperationEnum() == OPERATION.DISCOVERY) {
					this.setContentType(supportXml ? CONTENT_TYPE.XML : CONTENT_TYPE.JSON);
				} else {
					switch (rc) {
					case ATTRIBUTE:
					case HIERARCHY_ADDR_N_ATTR:
					case ATTR_N_CHILD_RES:
					case ATTR_N_CHILD_RES_REF:
					case CHILD_RES_REF:
					case ORIGINAL_RES:
						this.setContentType(supportXml ? CONTENT_TYPE.RES_XML : CONTENT_TYPE.RES_JSON);
						break;
					case HIERARCHY_ADDR:
						this.setContentType(supportXml ? CONTENT_TYPE.XML : CONTENT_TYPE.JSON);
						break;
					default:
						this.setContentType(supportXml ? CONTENT_TYPE.XML : CONTENT_TYPE.JSON);
					}			
				}
			}
						
		}
		log.debug("OneM2mResponse initialized with request!");
	}
	
	public boolean isValid() {
		
//		if(resStatusCode == RESPONSE_STATUS.NONE) {
//			unvalidReason = NOT_RES_STATCODE;
//			return false;
//		}
		
		if(requestIdentifier == null) {
			unvalidReason = NOT_REQUESTID;
			return false;
		}

		return true;
	}


	public String getUnvalidReason() {
		return unvalidReason;
	}	
	
	
	public CONTENT_TYPE getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) throws OneM2MException {
		
		if(contentType == null) return;
		
		CONTENT_TYPE cont_type = CONTENT_TYPE.get(contentType);
		if(cont_type != null) {
			this.contentType = cont_type;
		} else {
			throw new OneM2MException(RESPONSE_STATUS.BAD_REQUEST, "Invalid Content-Type header:"+contentType);
		}
	}
	
	public void setContentType(CONTENT_TYPE contentType) {
		this.contentType = contentType;
	}
	
	public void setHttpStatusCode(int code) {
		this.httpStatusCode = code;		
	}
	
	public int getHttpStatusCode() {
		return this.httpStatusCode;
	}
	
	public RESPONSE_STATUS getResponseStatusCodeEnum() {
		if (responseStatusCode == null)	return RESPONSE_STATUS.UNDEFINED;
		else return RESPONSE_STATUS.get(responseStatusCode);
	}

	public void setResponseStatusCodeEnum(RESPONSE_STATUS resStatusCode) {
		this.responseStatusCode = resStatusCode.Value();
	}
	
	public void setContentLocation(String contentLocation) {
		this.contentLocation = contentLocation;
	}
	
	public String getContentLocation() {
		if (this.contentLocation != null) {
			return this.contentLocation;
		} else if (contentObject instanceof Resource) {
			Resource res = (Resource)contentObject;
			return res.getUri();
		} else if (contentObject instanceof UriContent) {
			UriContent uri = (UriContent)contentObject;
			return uri.getUri();
		} else {
			return null;
		}
	}
	
	/**
	 * @return the request
	 */
	public OneM2mRequest getRequest() {
		return request;
	}
	/**
	 * @param request the request to set
	 */
	public void setRequest(OneM2mRequest request) {
		this.request = request;
	}

	public String getRemoteHost() {
		 
		return this.remoteHost;
	}

	public void setRemoteHost(String host) {

		this.remoteHost = host;
	}
	
	/**
	 * @return the contentObject
	 */
	public Object getContentObject() {
		return contentObject;
	}
	/**
	 * @param contentObject the contentObject to set
	 */
	public void setContentObject(Object contentObject) {
		this.contentObject = contentObject;
		
		this.setPrimitiveContent(new PrimitiveContent(contentObject));
	}
	
	public void setContent(byte[] content) {
		this.content = content;
	}
	
	public byte[] getContent() throws Exception {

		CONTENT_TYPE cntType = this.getContentType();
//		if (request != null) {
//			List<CONTENT_TYPE> format = request.getAcceptTypes();
////			//System.out.println("format is " + format);
//			if (format != null && format.size() > 0) {
//				cntType = format.get(0);
//			}
//		}
		
		if(cntType == CONTENT_TYPE.TEXT_PLAIN) return content; // non standard.
		
		AbsSerializer serializer = AbsSerializer.getSerializer(cntType == null ? CONTENT_TYPE.XML : cntType);
		
		if (content == null && contentObject != null) {
			content = serializer.serialize(contentObject).getBytes();
			
			/*OneM2mRequest.RESULT_CONT resultContent = request.getResultContent();
			if (resultContent == OneM2mRequest.RESULT_CONT.HIERARCHY_ADDR) {
				content = serializer.serialize(contentObject).getBytes();				
			} else if (resultContent == OneM2mRequest.RESULT_CONT.ATTRIBUTE || 
						resultContent == OneM2mRequest.RESULT_CONT.ORIGINAL_RES) {
				content = serializer.serialize(contentObject).getBytes();				
			} else if (resultContent == OneM2mRequest.RESULT_CONT.ATTR_N_CHILD_RES) {
				content = serializer.serialize(contentObject).getBytes();				
			} else if (resultContent == OneM2mRequest.RESULT_CONT.ATTR_N_CHILD_RES_REF) {
				content = serializer.serialize(contentObject).getBytes();			
			} else if (resultContent == OneM2mRequest.RESULT_CONT.CHILD_RES_REF) {
				content = serializer.serialize(contentObject).getBytes();			
			} else if (resultContent == OneM2mRequest.RESULT_CONT.HIERARCHY_ADDR_N_ATTR) {
				content = serializer.serialize(contentObject).getBytes();			
			} */
		}
		
		return content;
	}

	public String toString() {
		StringBuilder bld = new StringBuilder();
		
		bld.append("ResStatusCode:").append(getResponseStatusCodeEnum().Name()).append("\n");
		bld.append("RequestIdentifier:").append(requestIdentifier).append("\n");
		bld.append("TO:").append(to).append("\n");
		bld.append("From:").append(from).append("\n");
		bld.append("OriginatingTimestamp:").append(originatingTimestamp).append("\n");
		bld.append("ResultExpirationTimestamp:").append(resultExpirationTimestamp).append("\n");
		bld.append("EventCategory:").append(eventCategory);
		
		if(content != null) {
			try {
				bld.append(" Content: ").append(new String(content, "UTF-8")).append("\n");
			} catch (UnsupportedEncodingException e) {
				log.error("UnsupportedEncodingException", e);
			}
		}
		
		return bld.toString();
	}


}
