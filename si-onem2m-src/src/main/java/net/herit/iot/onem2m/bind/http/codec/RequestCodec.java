package net.herit.iot.onem2m.bind.http.codec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;
import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mRequest.DISCOV_RESTYPE;
import net.herit.iot.message.onem2m.OneM2mRequest.OPERATION;
import net.herit.iot.message.onem2m.OneM2mRequest.RESOURCE_TYPE;
import net.herit.iot.message.onem2m.OneM2mRequest.RESPONSE_TYPE;
import net.herit.iot.message.onem2m.OneM2mRequest.RESULT_CONT;
import net.herit.iot.message.onem2m.OneM2mResponse.RESPONSE_STATUS;
import net.herit.iot.message.onem2m.format.Enums.CONTENT_TYPE;
import net.herit.iot.message.onem2m.format.Enums.FILTER_USAGE;
import net.herit.iot.onem2m.ae.lib.HttpBasicRequest;
import net.herit.iot.onem2m.bind.http.codec.AbsCodec;
import net.herit.iot.onem2m.bind.http.util.Utils;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.resource.Attribute;
import net.herit.iot.onem2m.resource.FilterCriteria;

public class RequestCodec extends AbsCodec {
	/**********************************************************************
	 * URI Query
	 **********************************************************************/
	private final static String HTTP_QUERY_RESPONSE_TYPE 		= "rt";
	private final static String HTTP_QUERY_RESULT_PERSISTENCE 	= "rp";
	private final static String HTTP_QUERY_RESULT_CONTENT1 	= "rcn";
	private final static String HTTP_QUERY_RESULT_CONTENT2 	= "rc";
	private final static String HTTP_QUERY_DELIVERY_AGGREATION = "da";
	private final static String HTTP_QUERY_DISCOVERY_RESTYPE 	= "drt";
	
	// Filter Criteria
	private final static String HTTP_QUERY_CREATED_BEFORE 		= "crb";
	private final static String HTTP_QUERY_CREATED_AFTER 		= "cra";
	private final static String HTTP_QUERY_MODIFIED_SINCE		= "ms";
	private final static String HTTP_QUERY_UNMODIFIED_SINCE 	= "us";
	private final static String HTTP_QUERY_STATETAG_SMALLER 	= "sts";
	private final static String HTTP_QUERY_STATETAG_BIGGER 	= "stb";
	private final static String HTTP_QUERY_EXPIRE_BEFORE 		= "exb";
	private final static String HTTP_QUERY_EXPIRE_AFTER 		= "exa";
	private final static String HTTP_QUERY_LABELS 				= "lbl";
	private final static String HTTP_QUERY_RESOURCE_TYPE 		= "rty";  // TS-0001과 TS-0009에서 서로다름.
	private final static String HTTP_QUERY_SIZE_ABOVE 			= "sza";
	private final static String HTTP_QUERY_SIZE_BELOW 			= "szb";
	private final static String HTTP_QUERY_CONTENT_TYPE 		= "cty";
	private final static String HTTP_QUERY_LIMIT 				= "lim";
	private final static String HTTP_QUERY_ATTRIBUTE 			= "atr";
	private final static String HTTP_QUERY_FILTER_USAGE 		= "fu";
	
	/**********************************************************************
	 * HTTP Extension Header
	 **********************************************************************/
//	protected final static String HTTP_HEADER_REQUESTID 			= "X-M2M-RI";
//	protected final static String HTTP_HEADER_FROM 					= "X-M2M_Origin";
//	protected final static String HTTP_HEADER_ORIGIN_TIME			= "X-M2M-OT";
//	protected final static String HTTP_HEADER_RESULT_EXPIRETIME 	= "X-M2M-RST";
//	protected final static String HTTP_HEADER_EVENT_CATEGORI 		= "X-M2M-EC";
	private final static String HTTP_HEADER_NAME 					= "X-M2M-NM";
	private final static String HTTP_HEADER_REQUEST_EXPTIMESTAMP 	= "X-M2M-RET";
	private final static String HTTP_HEADER_OPERATION_EXECTIME 	= "X-M2M-OET";
	private final static String HTTP_HEADER_GROUP_REQUESTID 		= "X-M2M-GID";
	private final static String HTTP_HEADER_RESPONSE_TYPE_URI		= "X-M2M-RTU";

	private final static String HTTP_HEADER_HOST 					= "Host";
	private final static String HTTP_HEADER_ACCEPT					= "Accept";
	
	private final static String HTTP_CONTENTYPE_RESOURCE_TYPE		= "ty=";

	public static OneM2mRequest decode(DefaultFullHttpRequest request) throws Exception {
		return decode(request, null);
	}

	public static OneM2mRequest decode(HttpBasicRequest request) throws Exception {
		return decode(request.getMethod(), request.getUri(), request.getHeaders(), request.getHost(), request.getContent());
	}
	
	public static OneM2mRequest decode(String method, String httpUri, HashMap<String, String> originHeaders, String remoteHost, byte[] content) throws Exception {

		OneM2mRequest reqMessage = new OneM2mRequest();
		FilterCriteria filterCriteria = null;
		

		HashMap<String, String> headers = new HashMap<String, String>();
		Iterator<String> itKey = originHeaders.keySet().iterator();
		while (itKey.hasNext()) {
			String key = itKey.next();
			String val = originHeaders.get(key);
			headers.put(key.toUpperCase(), val);
		}
		
		if(remoteHost != null) {
			reqMessage.setRemoteHost(remoteHost);
		}
		
		HttpMethod httpMethod = HttpMethod.valueOf(method);
		
		if (httpUri.startsWith("//")) httpUri = "http:"+httpUri;	// for postman test
		
		HashMap<String, Object> queries = Utils.urlQueryParse(httpUri);
		
		// Query string decode..		
		Object value = queries.get(Utils.TO_NAME);
		if (null != value) {
			if (!(value instanceof String)) throw new OneM2MException(RESPONSE_STATUS.INVALID_ARGUMENTS, Utils.TO_NAME+ " cannot be list.");
			reqMessage.setTo((String)value);
		}
		
		value = queries.get(HTTP_QUERY_RESPONSE_TYPE);
		if (null != value) {
			if (!(value instanceof String)) throw new OneM2MException(RESPONSE_STATUS.INVALID_ARGUMENTS, HTTP_QUERY_RESULT_CONTENT1+ " cannot be list.");
			RESPONSE_TYPE resType = RESPONSE_TYPE.get(Integer.parseInt((String)value));
			reqMessage.setResponseType(resType);
		}
		
		value = queries.get(HTTP_QUERY_RESULT_PERSISTENCE);
		if (null != value) {
			if (!(value instanceof String)) throw new OneM2MException(RESPONSE_STATUS.INVALID_ARGUMENTS, HTTP_QUERY_RESULT_PERSISTENCE+ " cannot be list.");
			reqMessage.setResultPersistence((String)value);
		}

		value = queries.get(HTTP_QUERY_RESULT_CONTENT1);
		if (null != value) {
			if (!(value instanceof String)) throw new OneM2MException(RESPONSE_STATUS.INVALID_ARGUMENTS, HTTP_QUERY_RESULT_CONTENT1+ " cannot be list.");
			RESULT_CONT resCont = RESULT_CONT.get(Integer.parseInt((String)value));
			// TODO: if NONE..?
			reqMessage.setResultContent(resCont);
		}

		value = queries.get(HTTP_QUERY_RESULT_CONTENT2);
		if (null != value) {
			if (!(value instanceof String)) throw new OneM2MException(RESPONSE_STATUS.INVALID_ARGUMENTS, HTTP_QUERY_RESULT_CONTENT2+ " cannot be list.");
			RESULT_CONT resCont = RESULT_CONT.get(Integer.parseInt((String)value));
			// TODO: if NONE..?
			reqMessage.setResultContent(resCont);
		}
		
		value = queries.get(HTTP_QUERY_DELIVERY_AGGREATION);
		if (null != value) {
			if (!(value instanceof String)) throw new OneM2MException(RESPONSE_STATUS.INVALID_ARGUMENTS, HTTP_QUERY_DELIVERY_AGGREATION+ " cannot be list.");
			reqMessage.setDeliveryAggregation(Boolean.parseBoolean((String)value));
		}
		
		value = queries.get(HTTP_QUERY_DISCOVERY_RESTYPE);
		if (null != value) {
			if (!(value instanceof String)) throw new OneM2MException(RESPONSE_STATUS.INVALID_ARGUMENTS, HTTP_QUERY_DISCOVERY_RESTYPE+ " cannot be list.");
			DISCOV_RESTYPE disType = DISCOV_RESTYPE.get(Integer.parseInt((String)value));
			// TODO: if NONE..>?
			reqMessage.setDiscoveryResultType(disType);
		}
		
		// Filter Criteria
		value = queries.get(HTTP_QUERY_CREATED_BEFORE);
		if (null != value) {
			if (!(value instanceof String)) throw new OneM2MException(RESPONSE_STATUS.INVALID_ARGUMENTS, HTTP_QUERY_CREATED_BEFORE+ " cannot be list.");
			if(filterCriteria == null) filterCriteria = new FilterCriteria();
			filterCriteria.setCreatedBefore((String)value);
		}

		value = queries.get(HTTP_QUERY_CREATED_AFTER);
		if (null != value) {
			if (!(value instanceof String)) throw new OneM2MException(RESPONSE_STATUS.INVALID_ARGUMENTS, HTTP_QUERY_CREATED_AFTER+ " cannot be list.");
			if(filterCriteria == null) filterCriteria = new FilterCriteria();
			filterCriteria.setCreatedAfter((String)value);
		}

		value = queries.get(HTTP_QUERY_MODIFIED_SINCE);
		if (null != value) {
			if (!(value instanceof String)) throw new OneM2MException(RESPONSE_STATUS.INVALID_ARGUMENTS, HTTP_QUERY_MODIFIED_SINCE+ " cannot be list.");
			if(filterCriteria == null) filterCriteria = new FilterCriteria();
			filterCriteria.setModifiedSince((String)value);
		}

		value = queries.get(HTTP_QUERY_UNMODIFIED_SINCE);
		if (null != value) {
			if (!(value instanceof String)) throw new OneM2MException(RESPONSE_STATUS.INVALID_ARGUMENTS, HTTP_QUERY_UNMODIFIED_SINCE+ " cannot be list.");
			if(filterCriteria == null) filterCriteria = new FilterCriteria();
			filterCriteria.setUnmodifiedSince((String)value);
		}

		value = queries.get(HTTP_QUERY_STATETAG_SMALLER);
		if (null != value) {
			if (!(value instanceof String)) throw new OneM2MException(RESPONSE_STATUS.INVALID_ARGUMENTS, HTTP_QUERY_STATETAG_SMALLER+ " cannot be list.");
			if(filterCriteria == null) filterCriteria = new FilterCriteria();
			filterCriteria.setStateTagSmaller(Integer.parseInt((String)value));
		}
		
		value = queries.get(HTTP_QUERY_STATETAG_BIGGER);
		if (null != value) {
			if (!(value instanceof String)) throw new OneM2MException(RESPONSE_STATUS.INVALID_ARGUMENTS, HTTP_QUERY_STATETAG_BIGGER+ " cannot be list.");
			if(filterCriteria == null) filterCriteria = new FilterCriteria();
			filterCriteria.setStateTagBigger(Integer.parseInt((String)value));
		}

		value = queries.get(HTTP_QUERY_EXPIRE_BEFORE);
		if (null != value) {
			if (!(value instanceof String)) throw new OneM2MException(RESPONSE_STATUS.INVALID_ARGUMENTS, HTTP_QUERY_EXPIRE_BEFORE+ " cannot be list.");
			if(filterCriteria == null) filterCriteria = new FilterCriteria();
			filterCriteria.setExpireBefore((String)value);
		}

		value = queries.get(HTTP_QUERY_EXPIRE_AFTER);
		if (null != value) {
			if (!(value instanceof String)) throw new OneM2MException(RESPONSE_STATUS.INVALID_ARGUMENTS, HTTP_QUERY_EXPIRE_AFTER+ " cannot be list.");
			if(filterCriteria == null) filterCriteria = new FilterCriteria();
			filterCriteria.setExpireAfter((String)value);
		}

		value = queries.get(HTTP_QUERY_LABELS);
		if (null != value) {
			if(filterCriteria == null) filterCriteria = new FilterCriteria();
			
			if (value instanceof String) {
				filterCriteria.addLabels((String)value);
			} else if (value instanceof ArrayList<?>) {
				Iterator<String> it = ((ArrayList<String>)value).iterator();
				while (it.hasNext()) {
					filterCriteria.addLabels(it.next());
				}
			}		
		}

		value = queries.get(HTTP_QUERY_RESOURCE_TYPE);
		if (null != value) {
			if (!(value instanceof String)) throw new OneM2MException(RESPONSE_STATUS.INVALID_ARGUMENTS, HTTP_QUERY_RESOURCE_TYPE+ " cannot be list.");
			if(filterCriteria == null) filterCriteria = new FilterCriteria();
			RESOURCE_TYPE resourceType = RESOURCE_TYPE.get(Integer.parseInt((String)value));
			filterCriteria.setResourceType(resourceType.Value());
		}
		
		value = queries.get(HTTP_QUERY_SIZE_ABOVE);
		if (null != value) {
			if (!(value instanceof String)) throw new OneM2MException(RESPONSE_STATUS.INVALID_ARGUMENTS, HTTP_QUERY_SIZE_ABOVE+ " cannot be list.");
			if(filterCriteria == null) filterCriteria = new FilterCriteria();
			filterCriteria.setSizeAbove(Integer.parseInt((String)value));
		}

		value = queries.get(HTTP_QUERY_SIZE_BELOW);
		if (null != value) {
			if (!(value instanceof String)) throw new OneM2MException(RESPONSE_STATUS.INVALID_ARGUMENTS, HTTP_QUERY_SIZE_BELOW+ " cannot be list.");
			if(filterCriteria == null) filterCriteria = new FilterCriteria();
			filterCriteria.setSizeBelow(Integer.parseInt((String)value));
		}

		value = queries.get(HTTP_QUERY_CONTENT_TYPE);
		if (null != value) {
			if (!(value instanceof String)) throw new OneM2MException(RESPONSE_STATUS.INVALID_ARGUMENTS, HTTP_QUERY_CONTENT_TYPE+ " cannot be list.");
			if(filterCriteria == null) filterCriteria = new FilterCriteria();
//			filterCriteria.setContentType(value);
			filterCriteria.addContentType((String)value);
		}

		value = queries.get(HTTP_QUERY_LIMIT);
		if (null != value) {
			if (!(value instanceof String)) throw new OneM2MException(RESPONSE_STATUS.INVALID_ARGUMENTS, HTTP_QUERY_LIMIT+ " cannot be list.");
			if(filterCriteria == null) filterCriteria = new FilterCriteria();
			filterCriteria.setLimit(Integer.parseInt((String)value));
		}

		value = queries.get(HTTP_QUERY_ATTRIBUTE);
		if (null != value) {
			if (!(value instanceof String)) throw new OneM2MException(RESPONSE_STATUS.INVALID_ARGUMENTS, HTTP_QUERY_ATTRIBUTE+ " cannot be list.");
			if(filterCriteria == null) filterCriteria = new FilterCriteria();
			////???????? Fromat..????
						
			String[] attributes = ((String)value).split(" ");
			for(String attribute : attributes) {
				String[] split = attribute.split(":");
				
				if(split.length == 2)
//					filterCriteria.putAttribute(split[0], split[1]);
					filterCriteria.addAttribute(new Attribute(split[0], split[1]));
				//else 
					// TODO:..
			}
		}

		value = queries.get(HTTP_QUERY_FILTER_USAGE);
		if (null != value) {
			if (!(value instanceof String)) throw new OneM2MException(RESPONSE_STATUS.INVALID_ARGUMENTS, HTTP_QUERY_FILTER_USAGE+ " cannot be list.");
			if(filterCriteria == null) filterCriteria = new FilterCriteria();
			filterCriteria.setFilterUsage(Integer.parseInt((String)value));
		}

		
		// HTTP Header decode..
		String hvalue = headers.get(HTTP_HEADER_REQUESTID);
		if (null != hvalue) reqMessage.setRequestIdentifier(hvalue);
		
		hvalue = headers.get(HTTP_HEADER_FROM);
		if (null != hvalue) reqMessage.setFrom(hvalue);
		
		hvalue = headers.get(HTTP_HEADER_ORIGIN_TIME);
		if (null != hvalue) reqMessage.setOriginatingTimestamp(hvalue);
		
		hvalue = headers.get(HTTP_HEADER_RESULT_EXPIRETIME);
		if (null != hvalue) reqMessage.setResultExpirationTimestamp(hvalue);
		
		hvalue = headers.get(HTTP_HEADER_EVENT_CATEGORI);
		if (null != hvalue) reqMessage.setEventCategory(Integer.parseInt(hvalue));		// User defined value: 100 ~ 999
		
		hvalue = headers.get(HTTP_HEADER_NAME);
		if (null != hvalue) reqMessage.setName(hvalue);
		
		hvalue = headers.get(HTTP_HEADER_REQUEST_EXPTIMESTAMP);
		if (null != hvalue) reqMessage.setRequestExpirationTimestamp(hvalue);
		
		hvalue = headers.get(HTTP_HEADER_OPERATION_EXECTIME);
		if (null != hvalue) reqMessage.setOperationExecutionTime(hvalue);
		
		hvalue = headers.get(HTTP_HEADER_GROUP_REQUESTID);
		if (null != hvalue) reqMessage.setGroupRequestIdentifier(hvalue);
		
		hvalue = headers.get(HTTP_HEADER_CONTENT_TYPE);		// TODO: ty=? check(if ty != try ?). "ty" is resource type(rty).
		if (null != hvalue) {
			String[] split = hvalue.split(";");
			for(String type : split) {
				type = type.trim();
				if(type.toLowerCase().startsWith(HTTP_CONTENTYPE_RESOURCE_TYPE)) {
					RESOURCE_TYPE resourceType = 
							RESOURCE_TYPE.get(Integer.parseInt(type.substring(HTTP_CONTENTYPE_RESOURCE_TYPE.length(), type.length()).trim()));
					reqMessage.setResourceType(resourceType);
				} else {
					reqMessage.setContentType(type);
				}
			}
		}
		
		hvalue = headers.get(HTTP_HEADER_ACCEPT);
		if (null != hvalue) {
			String[] split = hvalue.split(";");
			for(String type: split) {
				type = type.trim();
				reqMessage.addAcceptType(type);
			}
		}
		
		hvalue = headers.get(HTTP_HEADER_RESPONSE_TYPE_URI);
		if (null != hvalue) {
			String[] split = hvalue.split("&");
			for (String uri : split) {
				reqMessage.addNotificationUri(uri);
			}
		}
		
		// TODO : HTTP Host header.. ?

		// OneM2m Operation Check.
		OPERATION operation = OPERATION.NONE;
		if (httpMethod.equals(HttpMethod.GET) 
				&& filterCriteria != null && filterCriteria.getFilterUsage() == FILTER_USAGE.DISCOVERY.Value()) {
			operation = OPERATION.DISCOVERY;
		} else 
		if (httpMethod.equals(HttpMethod.GET)) {
			operation = OPERATION.RETRIEVE;
		} else 
		if (httpMethod.equals(HttpMethod.DELETE)) {
			operation = OPERATION.DELETE;
		} else 
		if (httpMethod.equals(HttpMethod.PUT)) {
			operation = OPERATION.UPDATE;
		} else 
		if (httpMethod.equals(HttpMethod.POST) && reqMessage.getResourceTypeEnum() != OneM2mRequest.RESOURCE_TYPE.NONE) {
			operation = OPERATION.CREATE;
		} else
		if (httpMethod.equals(HttpMethod.POST) && reqMessage.getResourceTypeEnum() == OneM2mRequest.RESOURCE_TYPE.NONE) {
			// TODO: NOTIFY Operation check.
			if (reqMessage.getContentType() == CONTENT_TYPE.NTFY_JSON ||
				 reqMessage.getContentType() == CONTENT_TYPE.NTFY_XML) {
				operation = OPERATION.NOTIFY;
			} else {
				operation = OPERATION.UPDATE;
			}
		}
		
		reqMessage.setOperation(operation);

		// TODO: if operation is NONE..?
		
		
		if (reqMessage.getResultContentEnum().equals(RESULT_CONT.NONE)) {
			reqMessage.setResultContent(getDefaultResultContent(operation));
		}
		
		if(content != null) {
			reqMessage.setContent(content);
		}
		if(filterCriteria != null) 
			reqMessage.setFilterCriteria(filterCriteria);
		
		return reqMessage;
	}
	
//	public static OneM2mRequest decode(DefaultFullHttpRequest request, String remoteHost) throws Exception {
	public static OneM2mRequest decode(FullHttpRequest request, String remoteHost) throws Exception {
		
		Byte content[];
		String method;
		HashMap<String, String> headerMap = new HashMap<String, String>();
		HttpHeaders headers = request.headers();
		Iterator<Entry<String, String>> it = headers.iterator();
		while (it.hasNext()) {
			Entry<String, String> header = it.next();
			headerMap.put(header.getKey().toUpperCase(), header.getValue());
		}
		
		if(request.content().isReadable()) {
			return decode(request.getMethod().name(), request.getUri(), headerMap, remoteHost, request.content().copy().array());
		} else {
			return decode(request.getMethod().name(), request.getUri(), headerMap, remoteHost, null);				
		}
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
		
	
	public static DefaultFullHttpRequest encode(String baseUrl, OneM2mRequest reqMessage, HttpVersion httpVersion) throws Exception {
		DefaultFullHttpRequest request = null;
		
		HttpMethod httpMethod = null;
		switch (reqMessage.getOperationEnum()) {
		case RETRIEVE:
			httpMethod = HttpMethod.GET;
			break;
		case DELETE:
			httpMethod = HttpMethod.DELETE;
			break;
		case UPDATE:
			httpMethod = HttpMethod.PUT;	// POST..?
			break;
		case CREATE:
			httpMethod = HttpMethod.POST;
			break;
		case NOTIFY:
			httpMethod = HttpMethod.POST;
			break;
		case DISCOVERY:
			httpMethod = HttpMethod.GET;
			break;
			
		default:
			throw new Exception("Unknown Operation");
		}
		
		StringBuilder queryStrBld = new StringBuilder();
		String div = "";

		if (reqMessage.getResponseTypeEnum() != RESPONSE_TYPE.NONE) {
			queryStrBld.append(div);
			queryStrBld.append(HTTP_QUERY_RESPONSE_TYPE).append("=").append(reqMessage.getResponseTypeEnum().Value());
			div="&";
		}
		if (reqMessage.getResultPersistence() != null) {
			queryStrBld.append(div);
			queryStrBld.append(HTTP_QUERY_RESULT_PERSISTENCE).append("=").append(reqMessage.getResultPersistence());
			div = "&";
		}
		if (reqMessage.getResultContentEnum() != RESULT_CONT.NONE) {
			queryStrBld.append(div);
			queryStrBld.append(HTTP_QUERY_RESULT_CONTENT1).append("=").append(reqMessage.getResultContentEnum().Value());
			div = "&";
		}
		if (reqMessage.isDeliveryAggregation()) {
			queryStrBld.append(div);
			queryStrBld.append(HTTP_QUERY_DELIVERY_AGGREATION).append("=").append("true");
			div = "&";
		}
		if (reqMessage.getDiscoveryResultTypeEnum() != DISCOV_RESTYPE.NONE) {
			queryStrBld.append(div);
			queryStrBld.append(HTTP_QUERY_DISCOVERY_RESTYPE).append("=").append(reqMessage.getDiscoveryResultTypeEnum().Value());
			div = "&";
		}
		if (reqMessage.getFilterCriteria() != null) {
			FilterCriteria filterCriteria = reqMessage.getFilterCriteria();
			
			if (filterCriteria.getCreatedBefore() != null) {
				queryStrBld.append(div);
				queryStrBld.append(HTTP_QUERY_CREATED_BEFORE).append("=").append(filterCriteria.getCreatedBefore());
				div = "&";
			}
			if (filterCriteria.getCreatedAfter() != null) {
				queryStrBld.append(div);
				queryStrBld.append(HTTP_QUERY_CREATED_AFTER).append("=").append(filterCriteria.getCreatedAfter());
				div = "&";
			}
			if (filterCriteria.getModifiedSince() != null) {
				queryStrBld.append(div);
				queryStrBld.append(HTTP_QUERY_MODIFIED_SINCE).append("=").append(filterCriteria.getModifiedSince());
				div = "&";
			}
			if (filterCriteria.getUnmodifiedSince() != null) {
				queryStrBld.append(div);
				queryStrBld.append(HTTP_QUERY_UNMODIFIED_SINCE).append("=").append(filterCriteria.getUnmodifiedSince());
				div = "&";
			}
			if (filterCriteria.getStateTagSmaller() != null) { //OneM2mRequest.NOT_SET) {
				queryStrBld.append(div);
				queryStrBld.append(HTTP_QUERY_STATETAG_SMALLER).append("=").append(filterCriteria.getStateTagSmaller());
				div = "&";
			}
			if (filterCriteria.getStateTagBigger() != null) { //OneM2mRequest.NOT_SET) {
				queryStrBld.append(div);
				queryStrBld.append(HTTP_QUERY_STATETAG_BIGGER).append("=").append(filterCriteria.getStateTagBigger());
				div = "&";
			}
			if (filterCriteria.getExpireBefore() != null) {
				queryStrBld.append(div);
				queryStrBld.append(HTTP_QUERY_EXPIRE_BEFORE).append("=").append(filterCriteria.getExpireBefore());
				div = "&";
			}
			if (filterCriteria.getExpireAfter() != null) {
				queryStrBld.append(div);
				queryStrBld.append(HTTP_QUERY_EXPIRE_AFTER).append("=").append(filterCriteria.getExpireAfter());
				div = "&";
			}
			if (filterCriteria.getLabels().size() > 0) {
				queryStrBld.append(div);
				queryStrBld.append(HTTP_QUERY_LABELS).append("=");
				
				boolean isExist = false;
				for (String label : filterCriteria.getLabels()) {
					if(isExist) 
						queryStrBld.append(" ").append(label);
					else {
						queryStrBld.append(label);
						isExist = true;
					}
				}
				div = "&";
			}
			if (filterCriteria.getResourceType() != null) { //Enum() != RESOURCE_TYPE.NONE) {
				queryStrBld.append(div);
				queryStrBld.append(HTTP_QUERY_RESOURCE_TYPE).append("=").append(filterCriteria.getResourceType()); //Enum().Value());
				div = "&";
			}
			if (filterCriteria.getSizeAbove() != null) { //OneM2mRequest.NOT_SET) {
				queryStrBld.append(div);
				queryStrBld.append(HTTP_QUERY_SIZE_ABOVE).append("=").append(filterCriteria.getSizeAbove());
				div = "&";
			}
			if (filterCriteria.getSizeBelow() != null) { //OneM2mRequest.NOT_SET) {
				queryStrBld.append(div);
				queryStrBld.append(HTTP_QUERY_SIZE_BELOW).append("=").append(filterCriteria.getSizeBelow());
				div = "&";
			}
			if (filterCriteria.getContentType() != null) { // && filterCriteria.getContentType() != CONTENT_TYPE.NONE) {
				queryStrBld.append(div);
				queryStrBld.append(HTTP_QUERY_CONTENT_TYPE).append("=").append(filterCriteria.getContentType());   /// TODO: contentType은 array 임..
				div = "&";
			}
			if (filterCriteria.getLimit() != null) { //OneM2mRequest.NOT_SET) {
				queryStrBld.append(div);
				queryStrBld.append(HTTP_QUERY_LIMIT).append("=").append(filterCriteria.getLimit());
				div = "&";
			}
			if (filterCriteria.getAttribute().size() > 0) {
				queryStrBld.append(div);
				queryStrBld.append(HTTP_QUERY_ATTRIBUTE).append("=");
				
//				Set<Entry<String, Object>> entries = filterCriteria.getAttribute().entrySet();
				boolean isExist = false;
//				for(Entry<String, Object> entry : entries) {
				for(Attribute entry : filterCriteria.getAttribute()) {
					if (isExist) {
//						queryStrBld.append(" ").append(entry.getKey()).append(":").append(entry.getValue().toString());
						queryStrBld.append(" ").append(entry.getName()).append(":").append(entry.getValue().toString());
					} else {
//						queryStrBld.append(entry.getKey()).append(":").append(entry.getValue());
						queryStrBld.append(entry.getName()).append(":").append(entry.getValue());
						isExist = true;
					}
				}
				div = "&";
			}
			if (filterCriteria.getFilterUsage() != null) { //OneM2mRequest.NOT_SET) {
				queryStrBld.append(div);
				queryStrBld.append(HTTP_QUERY_FILTER_USAGE).append("=").append(filterCriteria.getFilterUsage());
				div = "&";
			}
		}

		// Generate Query string..
		String fullUrl = null; //baseUrl;
		if (reqMessage.getTo() != null) {
			if (!reqMessage.getTo().startsWith("/")) {
				fullUrl = "/" + reqMessage.getTo();
			} else {
				fullUrl = reqMessage.getTo();
			}
		} else {
			fullUrl = "/";
		}
		if(queryStrBld.length() > 0) {
			fullUrl += "?" + queryStrBld.toString();
		}
		
		int cont_len = reqMessage.getContent() != null ? reqMessage.getContent().length : 0;
		if(cont_len > 0) {
			request = new DefaultFullHttpRequest(httpVersion, httpMethod, fullUrl, 
												Unpooled.copiedBuffer(reqMessage.getContent()));
			request.headers().add(HTTP_HEADER_CONTENT_LENGTH, cont_len);
		} else {
			request = new DefaultFullHttpRequest(httpVersion, httpMethod, fullUrl);
		}

		String contentType = null;	
		// TODO: content-type -> CONTENT_TYPE.NTFY_JSON or NTFY_XML
		if (reqMessage.getOperationEnum() == OPERATION.NOTIFY) {
			CONTENT_TYPE cont_type = reqMessage.getContentType();
			
			switch(cont_type) {
			case JSON:
			case RES_JSON:
			case NTFY_JSON:
			case ATTRS_JSON:
				contentType = CONTENT_TYPE.NTFY_JSON.Name();
				break;
				
			case XML:
			case RES_XML:
			case NTFY_XML: 
			case ATTRS_XML:
			default:
				contentType = CONTENT_TYPE.NTFY_XML.Name();
				break;
			}
//		String contentType = null;
//		if(reqMessage.getContentType() != null && reqMessage.getContentType() != CONTENT_TYPE.NONE) {
//			contentType = reqMessage.getContentType().Name();
//		}
//		if(reqMessage.getResourceType() != null && reqMessage.getResourceType() != RESOURCE_TYPE.NONE) {
//			if(contentType != null) {
//				contentType = contentType + "; ";
			
		} else {
			if(reqMessage.getContentType() ==  null) {
				contentType = null;
			} else if (reqMessage.getContentType() != CONTENT_TYPE.NONE) {
				contentType = reqMessage.getContentType().Name();
			} else {
				contentType = CONTENT_TYPE.XML.Name();
			}
			if(reqMessage.getOperationEnum() == OPERATION.CREATE && reqMessage.getResourceTypeEnum() != RESOURCE_TYPE.NONE) {
//				if(contentType != null) {
//					contentType = contentType +  "; ";
//				}
				contentType = contentType + ";" + HTTP_CONTENTYPE_RESOURCE_TYPE + reqMessage.getResourceTypeEnum().Value();
			}
		}
		if (contentType != null) {
			request.headers().add(HTTP_HEADER_CONTENT_TYPE, contentType);
		}
		
		if (reqMessage.getAcceptTypes() != null && reqMessage.getAcceptTypes().size() > 0) {
			Iterator<CONTENT_TYPE> i =  reqMessage.getAcceptTypes().iterator();
			StringBuilder accept = new StringBuilder();
			while (i.hasNext()) {
				if (accept.length() > 0) {
					accept.append(";");
				}
				accept.append(i.next().Name());
			}
			
			request.headers().add(HTTP_HEADER_ACCEPT, accept);			
		}
		if (reqMessage.getRequestIdentifier() != null) {
			request.headers().add(HTTP_HEADER_REQUESTID, reqMessage.getRequestIdentifier());
		}
		if (reqMessage.getFrom() != null) {
			request.headers().add(HTTP_HEADER_FROM, reqMessage.getFrom());
		}
		if (reqMessage.getOriginatingTimestamp() != null) {
			request.headers().add(HTTP_HEADER_ORIGIN_TIME, reqMessage.getOriginatingTimestamp());
		}
		if (reqMessage.getResultExpirationTimestamp() != null) {
			request.headers().add(HTTP_HEADER_RESULT_EXPIRETIME, reqMessage.getResultExpirationTimestamp());
		}
		if (reqMessage.getEventCategory() != null) { //OneM2mRequest.NOT_SET) {
			request.headers().add(HTTP_HEADER_EVENT_CATEGORI, reqMessage.getEventCategory());
		}
		if (reqMessage.getName() != null) {
			request.headers().add(HTTP_HEADER_NAME, reqMessage.getName());
		}
		if (reqMessage.getRequestExpirationTimestamp() != null) {
			request.headers().add(HTTP_HEADER_REQUEST_EXPTIMESTAMP, reqMessage.getRequestExpirationTimestamp());
		}
		if (reqMessage.getOperationExecutionTime() != null) {
			request.headers().add(HTTP_HEADER_OPERATION_EXECTIME, reqMessage.getOperationExecutionTime());
		}
		if (reqMessage.getGroupRequestIdentifier() != null) {
			request.headers().add(HTTP_HEADER_GROUP_REQUESTID, reqMessage.getGroupRequestIdentifier());
		}
		
		if (reqMessage.getUserDefinedHeaders() != null) {
			Iterator<Entry<String, String>> it = reqMessage.getUserDefinedHeaders().entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, String> set = it.next();
				request.headers().add(set.getKey(), set.getValue());
			}
		}
		
		return request;
	}
	
}
