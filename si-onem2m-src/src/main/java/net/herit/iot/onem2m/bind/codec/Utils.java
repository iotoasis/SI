package net.herit.iot.onem2m.bind.codec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mRequest.DISCOV_RESTYPE;
import net.herit.iot.message.onem2m.OneM2mRequest.OPERATION;
import net.herit.iot.message.onem2m.OneM2mRequest.RESOURCE_TYPE;
import net.herit.iot.message.onem2m.OneM2mRequest.RESPONSE_TYPE;
import net.herit.iot.message.onem2m.OneM2mRequest.RESULT_CONT;
import net.herit.iot.message.onem2m.OneM2mResponse.RESPONSE_STATUS;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.resource.Attribute;
import net.herit.iot.onem2m.resource.FilterCriteria;


public class Utils {
	
	public static final String TO_NAME = "_to_";
	
	private static String toParamCheck(String url) {
		
		if(url.startsWith("/~/")) {	      // for http (structured SP-Relative)
			url = url.substring(2);
		} else if(url.startsWith("/~")) {  // for CoAP  2016.05.12
			url = url.substring(2);
			url = "/" + url;
		}	
		else if(url.startsWith("/_/")) {   // for http (structured Absolute)
			url = "/" + url.substring(2);
		} else if(url.startsWith("//_")) {  // for CoAP   2016.05.12
			url = "/" + url.substring(3);	
			url = "/" + url;
		}
		
		return url;
	}
	
	public static HashMap<String, Object> urlQueryParse(String url) {
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		int indx = url.indexOf("?");
		
		System.out.println("URL: " + url);
		if (indx == -1) {
			if (url.startsWith("http:")) url = url.substring(5);
			else if (url.startsWith("https:")) url = url.substring(6);
			url = toParamCheck(url);
			map.put(TO_NAME, url);
			return map;
		}

		String query = url.substring(indx+1);
		
		if (url.startsWith("http:")) url = url.substring(5, indx);
//		{
//			map.put(TO_NAME, url.substring(5, indx));
//		} 
		else if (url.startsWith("https:")) url = url.substring(6, indx);
//		{
//			map.put(TO_NAME, url.substring(6, indx));
//		} 
		else url = url.substring(0, indx);
//		{
//			map.put(TO_NAME, url.substring(0, indx));
//		}
		
		url = toParamCheck(url);
		map.put(TO_NAME, url);
		
		String[] params = query.split("&");
		
		for (String param : params) {
			String[] split = param.split("=");
			
			if(split.length == 2) {
				Object val = map.get(split[0]);
				if (val == null || val.equals(split[0])) {
					map.put(split[0], split[1]);
				} else if (val instanceof String) {
					List<String> list = new ArrayList<String>();
					list.add((String)val);
					list.add(split[1]);
					map.put(split[0],  list);
				} else { 	//if (val instanceof ArrayList<?>) {
					List<String> list = (ArrayList<String>)val;
					list.add(split[1]);
				}
			}
	    }
		return map;
	}
	
	
	
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
	
	public static void decodeQueryString(OneM2mRequest reqMessage, HashMap<String, Object> queries) throws Exception {
		
		if(reqMessage == null || queries == null) {
			throw new OneM2MException(RESPONSE_STATUS.INTERNAL_SERVER_ERROR, "Internal Server error");
		}
		
		FilterCriteria filterCriteria = null;
		
		// Query string decode..		
		Object value = queries.get(Utils.TO_NAME);
		if (null != value) {
			if (!(value instanceof String)) throw new OneM2MException(RESPONSE_STATUS.INVALID_ARGUMENTS, Utils.TO_NAME+ " cannot be list.");
			reqMessage.setTo((String)value);
		}
		
		value = queries.get(HTTP_QUERY_RESPONSE_TYPE);
		if (null != value) {
			if (!(value instanceof String)) throw new OneM2MException(RESPONSE_STATUS.INVALID_ARGUMENTS, HTTP_QUERY_RESPONSE_TYPE+ " cannot be list.");
			RESPONSE_TYPE resType = RESPONSE_TYPE.get(Integer.parseInt((String)value));
			// TODO: resType��NONE��寃쎌슦 �대뼸寃�泥섎━?
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
		
		reqMessage.setFilterCriteria(filterCriteria);
	}
	
	
	public static String makeQueryString(OneM2mRequest reqMessage) {
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
		if(queryStrBld.length() > 0) return queryStrBld.toString();
		return null;
	}
	
	
	public static RESULT_CONT getDefaultResultContent(OPERATION operation) {
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
	
	
	public static void main(String[] args) {
		String m = "~herit-in";
		System.out.println(m.substring(1));
	}
}
