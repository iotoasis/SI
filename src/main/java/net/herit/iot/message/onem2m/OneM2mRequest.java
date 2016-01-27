package net.herit.iot.message.onem2m;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import net.herit.iot.message.onem2m.OneM2mResponse.RESPONSE_STATUS;
import net.herit.iot.message.onem2m.format.Enums.*;
import net.herit.iot.onem2m.bind.codec.AbsSerializer;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.resource.Naming;
import net.herit.iot.onem2m.resource.PrimitiveContent;
import net.herit.iot.onem2m.resource.RequestPrimitive;
import net.herit.iot.onem2m.resource.ResponseTypeInfo;

@XmlRootElement(name = Naming.REQUESTPRIMITIVE_SN)
public class OneM2mRequest extends RequestPrimitive { //extends AbsMessage {

	private final static String UNKNOWN_OPERATION 	= "NONE Operation";
	private final static String NOT_TO 				= "NOT To";
	private final static String NOT_FROM 				= "NOT From";
	private final static String NOT_REQUESTID			= "NOT Request Identifier";
	private final static String NOT_RESOURCETYPE		= "NOT Resource Type";
	private final static String NOT_CONTENT			= "NOT Content";
	private final static String INVALID_EVT_CATEGORY = "INVALID Event Category";
	
	public enum OPERATION {
		NONE(0, "NONE"), CREATE(1, "CREATE"), RETRIEVE(2, "RETRIEVE"), UPDATE(4, "UPDATE"),
		DELETE(8, "DELETE"), DISCOVERY(16, "DISCOVERY"), NOTIFY(32, "NOTIFY");
		
		final int value;
		final String name;
		
		private OPERATION(int value, String name) {
			this.value = value;
			this.name = name;
		}
		
		public BigInteger BigInteger() {
			return BigInteger.valueOf(this.value);
		}
		
		public int Value() {
			return this.value;
		}
		
		public String Name() {
			return this.name;
		}

		private static final Map<Integer, OPERATION> map = 
				new HashMap<Integer, OPERATION>();
		static {
			for(OPERATION en : OPERATION.values()) {
				map.put(en.value, en);
			}
		}
		
		public static OPERATION get(int value) {
			OPERATION en = map.get(value);
			if(en == null) return NONE;
			return en;
		}
	};
	
	public enum RESPONSE_TYPE {
		NONE(0, "NONE"), 
		NBLOCK_REQ_SYNC(1, "NONBLOCKING_REQUEST_SYNC"),
		NBLOCK_REQ_ASYNC(2, "NONEBLOCKING_REQUEST_ASYNC"), 
		BLOCK_REQ(3, "BLOCKING_REQUEST");
		
		final int value;
		final String name;
		
		private RESPONSE_TYPE(int value, String name) {
			this.value = value;
			this.name = name;
		}
		
		public BigInteger BigInteger() {
			return BigInteger.valueOf(this.value);
		}
		
		public int Value() {
			return this.value;
		}
		
		public String Name() {
			return this.name;
		}

		private static final Map<Integer, RESPONSE_TYPE> map = 
				new HashMap<Integer, RESPONSE_TYPE>();
		static {
			for(RESPONSE_TYPE en : RESPONSE_TYPE.values()) {
				map.put(en.value, en);
			}
		}
		
		public static RESPONSE_TYPE get(int value) {
			RESPONSE_TYPE en = map.get(value);
			if(en == null) return NONE;
			return en;
		}
		
		public RESPONSE_TYPE get2(int value) {
			if(NBLOCK_REQ_SYNC.value == value) return NBLOCK_REQ_SYNC;
			if(NBLOCK_REQ_ASYNC.value == value) return NBLOCK_REQ_ASYNC;
			if(BLOCK_REQ.value == value) return BLOCK_REQ;
			return NONE;
		}
		
	}
	
	public enum RESULT_CONT {	// Result Content
		NONE(-1, "NONE"),
		NOTHING(0, "NOTHING"),
		ATTRIBUTE(1, "ATTRIBUTES"),
		HIERARCHY_ADDR(2, "HIERARCHICAL_ADDRESS"),
		HIERARCHY_ADDR_N_ATTR(3, "HIERARCHICAL_ADDRESS_N_ATTRIBUTES"),
		ATTR_N_CHILD_RES(4, "ATTRIBUTES_N_CHILD_RESOURCES"),
		ATTR_N_CHILD_RES_REF(5, "ATTRIBUTES_N_CHILD_RESOURCE_REFERENCES"),
		CHILD_RES_REF(6, "CHILD_RESOURCE_REFERENCES"),
		ORIGINAL_RES(7, "ORIGINAL_RESOURCE");
		
		final int value;
		final String name;
		
		private RESULT_CONT(int value, String name) {
			this.value = value;
			this.name = name;
		}
		
		public BigInteger BigInteger() {
			return BigInteger.valueOf(this.value);
		}
		
		public int Value() {
			return this.value;
		}
		
		public String Name() {
			return this.name;
		}
		
		private static final Map<Integer, RESULT_CONT> map = 
				new HashMap<Integer, RESULT_CONT>();
		static {
			for(RESULT_CONT en : RESULT_CONT.values()) {
				map.put(en.value, en);
			}
		}
		
		public static RESULT_CONT get(int value) {
			RESULT_CONT em = map.get(value);
			if(em == null) return NONE;
			return em;
		}
	}
	
	public enum DISCOV_RESTYPE {  // Discovery Result Type
		
		NONE(0, "NONE"),
		HIERARCHY(1, "HIERARCHICAL"),
		NON_HIERARCHY(2, "NON_HIERARCHICAL"),
		SCEID_N_RESCID(3, "cseID_N_resourceID");
		
		final int value;
		final String name;
		private DISCOV_RESTYPE(int value, String name) {
			this.value = value;
			this.name = name;
		}
		
		public BigInteger BigInteger() {
			return BigInteger.valueOf(this.value);
		}
		
		public int Value() {
			return this.value;
		}
		
		public String Name() {
			return this.name;
		}
		
		private static final Map<Integer, DISCOV_RESTYPE> map = 
				new HashMap<Integer, DISCOV_RESTYPE>();
		static {
			for(DISCOV_RESTYPE en : DISCOV_RESTYPE.values()) {
				map.put(en.value, en);
			}
		}
		
		public static DISCOV_RESTYPE get(int value) {
			DISCOV_RESTYPE en = map.get(value);
			if(en == null) return NONE;
			return en;
		}
	}
	
	public enum RESOURCE_TYPE {
		NONE(0, "NONE"),
		ACCESS_CTRL_POLICY(1, "accessControlPolicy"),
		AE(2, "AE"),
		CONTAINER(3, "container"),
		CONTENT_INST(4, "contentInstance"),
		CSE_BASE(5, "CSEBase"),
		DELIVERY(6, "delivery"),
		EVENT_CONFIT(7, "eventConfig"),
		EXEC_INST(8, "execInstance"),
		GROUP(9, "group"),
		LOCAT_POLICY(10, "locationPolicy"),
		M2M_SVC_SUBSC_PROF(11, "m2mServiceSubscriptionProfile"),
		MGMT_CMD(12, "mgmtCmd"),
		MGMT_OBJ(13, "mgmtObj"),
		NODE(14, "node"),
		POLLING_CHANN(15, "pollingChannel"),
		REMOTE_CSE(16, "remoteCSE"),
		REQUEST(17, "request"),
		SCHEDULE(18, "schedule"),
		SVC_SUBSC_APP_RULE(19, "serviceSubscribedAppRule"),
		SVC_SUBSC_NODE(20, "serviceSubscribedNode"),
		STATS_COLLECT(21, "statsCollect"),
		STATS_CONFIG(22, "statsConfig"),
		SUBSCRIPTION(23, "subscription"),
		ACCESS_CTRL_POLICY_ANNC(10001, "accessControlPolicyAnnc"),
		AE_ANNC(10002, "AEAnnc"),
		CONTAINER_ANNC(10003, "containerAnnc"),
		CONTENT_INST_ANNC(10004, "contentInstanceAnnc"),
		GROUP_ANNC(10009, "groupAnnc"),
		LOCAT_POLICY_ANNC(10010, "locationPolicyAnnc"),
		MGMT_OBJ_ANNC(10013, "mgmtObjAnnc"),
		NODE_ANNC(10014, "nodeAnnc"),
		REMOTE_CSE_ANNC(10016, "remoteCSEAnnc"),
		SCHEDULE_ANNC(10018, "scheduleAnnc"),
		NOTIFICATION(90001, "notification"),
		MGMT_FIRMWARE(1001, "firmware"),
		MGMT_SOFTWARE(1002, "software"),
		MGMT_MEMORY(1003, "memory"),
		MGMT_AREANWK_INFO(1004, "areaNwkInfo"),
		MGMT_AREANWK_DEVICEINFO(1005, "areaNwkDeviceInfo"),
		MGMT_BATTERY(1006, "battery"),
		MGMT_DEVICE_INFO(1007, "deviceInfo"),
		MGMT_DEVICE_CAPA(1008, "deviceCapability"),
		MGMT_REBOOT(1009, "reboot"),
		MGMT_EVENT_LOG(1010, "eventLog"),
		MGMT_CMDH_POLICY(1011, "eventLog"),
		MGMT_ACTIVE_CMDH_POLICY(1012, "eventLog"),
		MGMT_CMDH_DEFAULTS(1013, "eventLog"),
		MGMT_CMDH_DEFECVALUCE(1014, "eventLog"),
		MGMT_CMDH_ECDEF_PARAM_VALUES(1015, "eventLog"),
		MGMT_CMDH_LIMIT(1016, "eventLog"),
		MGMT_CMDH_NETWORK_ACCESS_RULES(1017, "eventLog"),
		MGMT_CMDH_NW_ACCESS_RULE(1018, "eventLog"),
		MGMT_CMDH_BUFFER(1019, "eventLog"),	// 90001 ~ : user defined
		AGGREGATED_RESPONSE(90010, "aggregatedResponse");
	
		final int value;
		final String name;
		private RESOURCE_TYPE(int value, String name) {
			this.value = value;
			this.name = name;
		}
		
		public BigInteger BigInteger() {
			return BigInteger.valueOf(this.value);
		}
		
		public int Value() {
			return this.value;
		}
		
		public String Name() {
			return this.name;
		}
		
		private static final Map<Integer, RESOURCE_TYPE> map = 
				new HashMap<Integer, RESOURCE_TYPE>();
		static {
			for(RESOURCE_TYPE en : RESOURCE_TYPE.values()) {
				map.put(en.value, en);
			}
		}
		
		public static RESOURCE_TYPE get(int value) {
			RESOURCE_TYPE en = map.get(value);
			if(en == null) return NONE;
			return en;
		}
	}
	
//	public static class FilterCriteria {
//		private String		createdBefore = null;
//		private String		createdAfter = null;
//		private String		modifiedSince = null;
//		private String		unmodifiedSince = null;
//		private int			stateTagSmaller = NOT_SET;
//		private int			stateTagBigger = NOT_SET;
//		private String		expireBefore = null;
//		private String		expireAfter = null;
//		private List<String>	labels = new ArrayList<String>();
//		private RESOURCE_TYPE resourceType = RESOURCE_TYPE.NONE;
//		private int			sizeAbove = NOT_SET;
//		private int			sizeBelow = NOT_SET;
//		private CONTENT_TYPE	 contentType = CONTENT_TYPE.NONE;
//		private Map<String, Object>	 attribute = new HashMap<String, Object>();
//		private int			filterUsage = NOT_SET;
//		private int			limit = NOT_SET;
//		
//		
//		public String getCreatedBefore() {
//			return createdBefore;
//		}
//		public void setCreatedBefore(String createdBefore) {
//			this.createdBefore = createdBefore;
//		}
//		public String getCreatedAfter() {
//			return createdAfter;
//		}
//		public void setCreatedAfter(String createdAfter) {
//			this.createdAfter = createdAfter;
//		}
//		public String getModifiedSince() {
//			return modifiedSince;
//		}
//		public void setModifiedSince(String odifiedSince) {
//			this.modifiedSince = odifiedSince;
//		}
//		public String getUnmodifiedSince() {
//			return unmodifiedSince;
//		}
//		public void setUnmodifiedSince(String unmodifiedSince) {
//			this.unmodifiedSince = unmodifiedSince;
//		}
//		public int getStateTagSmaller() {
//			return stateTagSmaller;
//		}
//		public void setStateTagSmaller(int stateTagSmaller) {
//			this.stateTagSmaller = stateTagSmaller;
//		}
//		public int getStateTagBigger() {
//			return stateTagBigger;
//		}
//		public void setStateTagBigger(int stateTagBigger) {
//			this.stateTagBigger = stateTagBigger;
//		}
//		public String getExpireBefore() {
//			return expireBefore;
//		}
//		public void setExpireBefore(String expireBefore) {
//			this.expireBefore = expireBefore;
//		}
//		public String getExpireAfter() {
//			return expireAfter;
//		}
//		public void setExpireAfter(String expireAfter) {
//			this.expireAfter = expireAfter;
//		}
//		public List<String> getLabels() {
//			return labels;
//		}
//		public void addLabels(String label) {
//			this.labels.add(label);
//		}
//		public RESOURCE_TYPE getResourceType() {
//			return resourceType;
//		}
//		public void setResourceType(RESOURCE_TYPE resType) {
//			this.resourceType = resType;
//		}
//		public int getSizeAbove() {
//			return sizeAbove;
//		}
//		public void setSizeAbove(int sizeAbove) {
//			this.sizeAbove = sizeAbove;
//		}
//		public int getSizeBelow() {
//			return sizeBelow;
//		}
//		public void setSizeBelow(int sizeBelow) {
//			this.sizeBelow = sizeBelow;
//		}
//		public CONTENT_TYPE getContentType() {
//			return contentType;
//		}
//		public void setContentType(CONTENT_TYPE contentType) {
//			this.contentType = contentType;
//		}
//		public Map<String, Object> getAttribute() {
//			return attribute;
//		}
//		public void putAttribute(String key, Object value) {
//			this.attribute.put(key, value);
//		}
//		public int getFilterUsage() {
//			return filterUsage;
//		}
//		public void setFilterUsage(int filterUsage) {
//			this.filterUsage = filterUsage;
//		}
//		public int getLimit() {
//			return limit;
//		}
//		public void setLimit(int limit) {
//			this.limit = limit;
//		}
//
//		public String toString() {
//			StringBuilder bld = new StringBuilder();
//			
//			bld.append(" FilterCriteria ").append("\n");
//			if(createdBefore != null)
//				bld.append("  .CreatedBefore:").append(createdBefore).append("\n");
//			if(createdAfter != null)	
//				bld.append("  .CreatedAfter:").append(createdAfter).append("\n");
//			if(modifiedSince != null)
//				bld.append("  .ModifiedSince:").append(modifiedSince).append("\n");
//			if(unmodifiedSince != null)
//				bld.append("  .UnmodifiedSince:").append(unmodifiedSince).append("\n");
//			if(stateTagSmaller != NOT_SET)
//				bld.append("  .StateTagSmaller:").append(stateTagSmaller).append("\n");
//			if(stateTagBigger != NOT_SET)
//				bld.append("  .StateTagBigger:").append(stateTagBigger).append("\n");
//			if(expireBefore != null)	
//				bld.append("  .ExpireBefore:").append(expireBefore).append("\n");
//			if(expireAfter != null)
//				bld.append("  .ExpireAfter:").append(expireAfter).append("\n");
//			if(labels.size() > 0) {
//				bld.append("  .Labels:");
//				for(String label : labels) {
//					bld.append(label + "\t");
//				}
//				bld.append("\n");
//			}
//			bld.append("resourceType:").append(resourceType.Name()).append("\n");
//			if(sizeAbove != NOT_SET)
//				bld.append("  .SizeAbove:").append(sizeAbove).append("\n");
//			if(sizeBelow != NOT_SET)
//				bld.append("  .SizeBelow:").append(sizeBelow).append("\n");
//			if(contentType != null)
//				bld.append("  .ContentType:").append(contentType).append("\n");
//			if(attribute.size() > 0) {
//				bld.append("  .Attribute:");
//				Set<Entry<String, Object>> entries = attribute.entrySet();
//				for(Entry<String, Object> entry : entries) {
//					bld.append(entry.getKey()).append(":").append(entry.getValue().toString()).append("\t");
//				}
//				bld.append("\n");
//			}
//			if(filterUsage != NOT_SET)
//				bld.append("  .FilterUsage:").append(filterUsage).append("\n");
//			if(limit != NOT_SET)
//				bld.append("  .Limit:").append(limit).append("\n");
//			
//			return bld.toString();
//		}
//		
//	}
	

//	private OPERATION 		operation = OPERATION.NONE;
//	private RESOURCE_TYPE	resourceType = RESOURCE_TYPE.NONE;		// 1 ~ 10018
//	private String			name = null;
//	private String			reqExpireTime = null;  					// request Expiration Time stamp
//	private String			operationExecTime = null;  				// operation Execution Time
//	private RESPONSE_TYPE	responseType = RESPONSE_TYPE.NONE;		// 1: nonBlockingRequestSync, 2: nonBlockingRequestAsync, 3: blockingRequest
//	private List<String>		notificationUri = new ArrayList<String>();
//	private String			resultPersistence = null;
//	private RESULT_CONT		resultContent = RESULT_CONT.NONE;
//	private boolean			deliveryAggregation = false;
//	private String			groupRequestId = null;
//	private DISCOV_RESTYPE	discovResType = DISCOV_RESTYPE.NONE;	// Discovery Result Type
//	private FilterCriteria 	filterCriteria = null;	
	
	protected transient String unvalidReason;
	protected transient CONTENT_TYPE		contentType;
	private transient List<CONTENT_TYPE>	acceptTypes = new ArrayList<CONTENT_TYPE>();	// for response's content type: application/json, application/xml, application/vnd.onem2m-res+xml, application/vnd.onem2m-res+json, application/vnd.onem2m-ntfy+xml, application/vnd.onem2m-ntfy+json, application/vnd.onem2m-attrs+xml, application/vnd.onem2m-attrs+json
	private transient String remoteHost;
	private transient String credentialID;
	protected transient HashMap<String, String> userDefinedHeaders = new HashMap<String, String>();
	protected transient byte[]	content = null;
	protected transient Object	contentObject = null;
	
	public String getUnvalidReason() {
		return unvalidReason;
	}
	
	public boolean isValid() {
		if(operation == OPERATION.NONE.Value()) {
			unvalidReason = UNKNOWN_OPERATION;
			return false;
		}
		
		if(to == null) {
			unvalidReason = NOT_TO;
			return false;
		}

		if(from == null) {
			unvalidReason = NOT_FROM;
			return false;
		}
		
//		if(requestId == null) {
		if(requestIdentifier == null) {
			unvalidReason = NOT_REQUESTID;
			return false;
		}

		if(operation == OPERATION.CREATE.Value()) {
			if(resourceType == RESOURCE_TYPE.NONE.Value()) {
				unvalidReason = NOT_RESOURCETYPE;
				return false;
			}
			if(primitiveContent == null) {
				unvalidReason = NOT_CONTENT;
				return false;
			}
		}
		
		if(operation == OPERATION.UPDATE.Value() && primitiveContent == null) {
			unvalidReason = NOT_CONTENT;
			return false;
		}
		
		if(eventCategory < 100 || eventCategory > 999) {
			unvalidReason = INVALID_EVT_CATEGORY;
			return false;
		}
		
		return true;
	}
	
	public void setCredentialID(String credentialId) {
		this.credentialID = credentialId;
	}
	
	public String getCredentialID() {
		return this.credentialID;
	}
	
	public OPERATION getOperationEnum() {
		if (operation == null) return OPERATION.NONE;

		return OPERATION.get(operation);
	}

	public void setOperation(OPERATION operation) {
		this.operation = operation.Value();
	}

	public RESOURCE_TYPE getResourceTypeEnum() {
		if(resourceType == null) return RESOURCE_TYPE.NONE;
		
		return RESOURCE_TYPE.get(resourceType);
	}

	public void setResourceType(RESOURCE_TYPE resourceType) {
		this.resourceType = resourceType.Value();
	}
	
	public HashMap<String, String> getUserDefinedHeaders() {
		return this.userDefinedHeaders;
	}
	
	public void addUserDefinedHeaders(String name, String value) {
		if (this.userDefinedHeaders == null) {
			this.userDefinedHeaders = new HashMap<String, String>();
		}
		this.userDefinedHeaders.put(name, value);
	}

//	public String getRequestExpirationTimestamp() {
//		return reqExpireTime;
//	}

//	public void setRequestExpirationTimestamp(String reqExpireTime) {
//		this.reqExpireTime = reqExpireTime;
//	}

//	public String getOperationExecutionTime() {
//		return operationExecTime;
//	}

//	public void setOperationExecutionTime(String operationExecTime) {
//		this.operationExecTime = operationExecTime;
//	}

	public RESPONSE_TYPE getResponseTypeEnum() {
		if(responseType == null) return RESPONSE_TYPE.NONE;
		
		return RESPONSE_TYPE.get(responseType.getResponseTypeValue());
	}
	
	public void setResponseType(RESPONSE_TYPE responseType) {
//		this.responseType = responseType;
		if(this.responseType == null) this.responseType = new ResponseTypeInfo();
		this.responseType.setResponseTypeValue(responseType.Value());
	}

	public List<String> getNotificationUri() {
//		return notificationUri;
		return responseType.getNotificationURI();
	}
	
	public void addNotificationUri(String uri) {
//		notificationUri.add(uri);
		responseType.addNotificationURI(uri);
	}

//	public String getResultPersistenceString() {
//		return resultPersistence.;
//	}

//	public void setResultPersistence(String resultPersistence) {
//		this.resultPersistence = resultPersistence;
//	}

	public RESULT_CONT getResultContentEnum() {
		if(resultContent == null) return RESULT_CONT.NONE;
		
		return RESULT_CONT.get(resultContent);
	}

	public void setResultContent(RESULT_CONT resultContent) {
		this.resultContent = resultContent.Value();
	}

//	public boolean isDeliveryAggregation() {
//		return deliveryAggregation;
//	}
//
//	public void setDeliveryAggregation(boolean deliveryAggregation) {
//		this.deliveryAggregation = deliveryAggregation;
//	}

//	public String getGroupRequestIdentifier() {
//		return groupRequestId;
//	}
//
//	public void setGroupRequestIdentifier(String groupRequestId) {
//		this.groupRequestId = groupRequestId;
//	}

	public DISCOV_RESTYPE getDiscoveryResultTypeEnum() {
		if(discoveryResultType == null) return DISCOV_RESTYPE.NONE;
		
		return DISCOV_RESTYPE.get(discoveryResultType);
	}

	public void setDiscoveryResultType(DISCOV_RESTYPE discovResType) {
		this.discoveryResultType = discovResType.Value();
	}

//	public FilterCriteria getFilterCriteria() {
//		return filterCriteria;
//	}
//
//	public void setFilterCriteria(FilterCriteria filterCriteria) {
//		this.filterCriteria = filterCriteria;
//	}
	
	public List<CONTENT_TYPE> getAcceptTypes() {
		System.out.println("Content-Type=" + this.contentType);
		if(this.acceptTypes == null || this.acceptTypes.size() == 0) {
			if(this.contentType != null && this.contentType != CONTENT_TYPE.NONE) {	 // 2015.09.14 added.
//				this.acceptTypes.add(contentType);
				addAcceptType(contentType.Name());
			} else 
			{
				//this.acceptTypes.add(CONTENT_TYPE.XML);
				addAcceptType(CONTENT_TYPE.XML.Name());
			}
		}
		
		return this.acceptTypes;
	}
	
	public void addAcceptType(String acceptType) {

		if(acceptType == null) return;

		CONTENT_TYPE cont_type = CONTENT_TYPE.get(acceptType);
		if(cont_type != null) {
			this.acceptTypes.add(cont_type);
		}
	}
		
	public CONTENT_TYPE getContentType() {
		return contentType;
	}

	public void setContentType(CONTENT_TYPE contentType) {
		this.contentType = contentType;
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
	
	public String toString() {
		StringBuilder bld = new StringBuilder();
		bld.append("\n Operation=").append(getOperationEnum().Name()).append("\n");
		bld.append(" To=").append(to).append("\n");
		bld.append(" From:").append(from).append("\n");
		bld.append(" RemoteHost:").append(remoteHost).append("\n");
		bld.append(" RequestIdentifier:").append(requestIdentifier).append("\n");
		bld.append(" ResourceType:").append(getResourceTypeEnum().Name()).append("\n");
		bld.append(" Name:").append(name).append("\n");
		bld.append(" OriginatingTimestamp:").append(originatingTimestamp).append("\n");
		bld.append(" RequestExpirationTimestamp:").append(requestExpirationTimestamp).append("\n");
		bld.append(" ResultExpirationTimestamp:").append(resultExpirationTimestamp).append("\n");
		bld.append(" OperationExecutionTime:").append(operationExecutionTime).append("\n");
		bld.append(" ResponseType:").append(getResponseTypeEnum().Name()).append("\n");
		bld.append(" ResultPersistence:").append(resultPersistence).append("\n");
		bld.append(" ResultContent:").append(getResultContentEnum().Name()).append("\n");
		bld.append(" EventCategory:").append(eventCategory).append("\n");
		bld.append(" DeliveryAggregation:").append(deliveryAggregation).append("\n");
		bld.append(" GroupRequestIdentifier:").append(groupRequestIdentifier).append("\n");
		bld.append(" DiscoveryResultType:").append(getDiscoveryResultTypeEnum().Name()).append("\n");
		bld.append(" Accept:");
		boolean flag = false;
		for(CONTENT_TYPE contType : acceptTypes) {
			if(flag)
				bld.append("; ");
			else {
				flag = true;
			}
			bld.append(contType.Name());
		}
		bld.append("\n ContentType:");
		if(contentType != null) {
			bld.append(contentType.Name());
		}
		bld.append("\n");
		
		
		if(filterCriteria != null) {
			bld.append(filterCriteria.toString());
		}
		
		if(content != null) {
			bld.append(" Content exist.").append("\n");
		}
		
		return bld.toString();
	}
	
	public void validate() throws OneM2MException {
		if (this.operation.equals(OPERATION.NONE)) {
			throw new OneM2MException(OneM2mResponse.RESPONSE_STATUS.INVALID_CMDTYPE, "No operation parameter specified");
		} else if (this.to == null) {
			throw new OneM2MException(OneM2mResponse.RESPONSE_STATUS.INSUFFICIENT_ARGUMENT, "No to parameter specified");
		} else if (this.from == null) {
			throw new OneM2MException(OneM2mResponse.RESPONSE_STATUS.INSUFFICIENT_ARGUMENT, "No from parameter specified");
		} else if (this.requestIdentifier == null) {
			throw new OneM2MException(OneM2mResponse.RESPONSE_STATUS.INSUFFICIENT_ARGUMENT, "No requestId parameter specified");
		} 
		
		if (this.operation.equals(OPERATION.CREATE)) {
			if (this.resourceType.equals(RESOURCE_TYPE.NONE)) {
				throw new OneM2MException(OneM2mResponse.RESPONSE_STATUS.INSUFFICIENT_ARGUMENT, "No resourceType parameter specified");
			}
			if (this.primitiveContent == null) {
				throw new OneM2MException(OneM2mResponse.RESPONSE_STATUS.INSUFFICIENT_ARGUMENT, "No content parameter specified");
			}
			if (this.filterCriteria != null) {
				throw new OneM2MException(OneM2mResponse.RESPONSE_STATUS.INVALID_ARGUMENTS, "Filtercriteria parameter should be null");
			} else if (!this.getDiscoveryResultTypeEnum().equals(DISCOV_RESTYPE.NONE)) {
				throw new OneM2MException(OneM2mResponse.RESPONSE_STATUS.INVALID_ARGUMENTS, "DiscoveryResultType parameter should be null");
			}
		} else if (this.operation.equals(OPERATION.RETRIEVE)) {
			if (!this.resourceType.equals(RESOURCE_TYPE.NONE)) {
				throw new OneM2MException(OneM2mResponse.RESPONSE_STATUS.INVALID_ARGUMENTS, "resourceType parameter should be null");
			}
//			if (this.name != null) {	//???? Optional..?
//				throw new OneM2MException(OneM2mResponse.RESPONSE_STATUS.INVALID_ARGUMENTS, "name parameter should be null");
//			}
		} else if (this.operation.equals(OPERATION.UPDATE)) {
			if (this.primitiveContent == null) {
				throw new OneM2MException(OneM2mResponse.RESPONSE_STATUS.INSUFFICIENT_ARGUMENT, "No content parameter specified");
			}
			if (!this.resourceType.equals(RESOURCE_TYPE.NONE)) {
				throw new OneM2MException(OneM2mResponse.RESPONSE_STATUS.INVALID_ARGUMENTS, "resourceType parameter should be null");
			}
//			if (this.name != null) {   //???? Optional..?
//				throw new OneM2MException(OneM2mResponse.RESPONSE_STATUS.INVALID_ARGUMENTS, "name parameter should be null");
//			}

		} else if (this.operation.equals(OPERATION.DELETE)) {
			if (!this.resourceType.equals(RESOURCE_TYPE.NONE)) {
				throw new OneM2MException(OneM2mResponse.RESPONSE_STATUS.INVALID_ARGUMENTS, "resourceType parameter should be null");
			}
//			if (this.name != null) {   //???? Optional..?
//				throw new OneM2MException(OneM2mResponse.RESPONSE_STATUS.INVALID_ARGUMENTS, "name parameter should be null");
//			}

		} else if (this.operation.equals(OPERATION.NOTIFY)) {
			if (!this.resourceType.equals(RESOURCE_TYPE.NONE)) {
				throw new OneM2MException(OneM2mResponse.RESPONSE_STATUS.INVALID_ARGUMENTS, "resourceType parameter should be null");
			}
//			if (this.name != null) {  //???? Optional..?
//				throw new OneM2MException(OneM2mResponse.RESPONSE_STATUS.INVALID_ARGUMENTS, "name parameter should be null");
//			}

		} else if (this.operation.equals(OPERATION.DISCOVERY)) {
			if (!this.resourceType.equals(RESOURCE_TYPE.NONE)) {
				throw new OneM2MException(OneM2mResponse.RESPONSE_STATUS.INVALID_ARGUMENTS, "resourceType parameter should be null");
			}
//			if (this.name != null) {   //???? Optional..?
//				throw new OneM2MException(OneM2mResponse.RESPONSE_STATUS.INVALID_ARGUMENTS, "name parameter should be null");
//			}
			if (this.filterCriteria == null) {
				throw new OneM2MException(OneM2mResponse.RESPONSE_STATUS.INSUFFICIENT_ARGUMENT, "No filterCriteria parameter specified");
			}
			
		}
	}

	public void setContent(byte[] content) {
		this.content = content;
	}
	
	public byte[] getContent() throws Exception {
		
//		AbsSerializer serializer = AbsSerializer.getSerializer(CONTENT_TYPE.XML);
		AbsSerializer serializer = AbsSerializer.getSerializer(getContentType());
		
		if (content == null && contentObject != null) {
			content = serializer.serialize(contentObject).getBytes();
		}
		
		return content;
	}
	
	/**
	 * @return the contentObject
	 */
	public Object getContentObject() {
		if (contentObject == null) {
			if(getPrimitiveContent() != null && getPrimitiveContent().getAny().size() > 0) {
				contentObject = getPrimitiveContent().getAny().get(0);				// TODO: List Object 처리 필요..
			}
		}
		return contentObject;
	}
	/**
	 * @param contentObject the contentObject to set
	 */
	public void setContentObject(Object contentObject) {
		this.contentObject = contentObject;
		this.setPrimitiveContent(new PrimitiveContent(contentObject));
	}

	public String getRemoteHost() {
 		return this.remoteHost;
	}

	public void setRemoteHost(String host) {

		this.remoteHost = host;
	}

	public void setOriginator(Originator value) {	
	
		this.originator = value;
		
	}
	
	public Originator getOriginator() {
		return originator;
	}
		
	public class Originator {
		protected String structuredId;			// sp relative "/<csebase>/<resourceName>/..."
		protected String unstructuredId;		// sp relative "/<resourceId>"
		protected String serviceProviderId;					// "//iot.herit.net"
		public Originator(String structuredId, String unstructuredId, String serviceProviderId) {
			System.out.println("structuredId=" + structuredId + ", unstructuredId=" + unstructuredId + ", serviceProviderId=" + serviceProviderId);
			this.structuredId = structuredId;
			this.unstructuredId = unstructuredId;
			this.serviceProviderId = serviceProviderId;
		}
		
		public String toString() {
			StringBuilder strBld = new StringBuilder();
			strBld.append("Originator]").append("structuredId=").append(structuredId).append("\r\n");
			strBld.append("unstructuredId=").append(unstructuredId).append("\r\n");
			strBld.append("serviceProviderId=").append(serviceProviderId);
			
			return strBld.toString();
		}
//		public Originator(String from, OneM2mContext context) {
//
//			try {
//				serviceProviderId = OneM2mUtil.extractServiceProviderId(from);
//				String resourceId = serviceProviderId != null ? from.substring(serviceProviderId.length()) : from;
//				
//				if (serviceProviderId == null) CfgManager.getInstance().getServiceProviderId();
//				ResourceDAO dao = new ResourceDAO(context);
//				
//				Resource res = dao.getResourceWithID(resourceId);
//				if (res != null) {
//					structuredId = res.getUri();
//					unstructuredId = res.getResourceID();
//				} else {
//					if (OneM2mUtil.isUri(resourceId)) {
//						structuredId = resourceId;
//					} else {
//						unstructuredId = resourceId;
//					}
//				}
//			} catch (OneM2MException e) {
//				
//				log.error("Exception", e);
//			
//			} catch (Exception e) {
//				
//				log.error("Exception", e);
//				
//			}
//			
//		}
		public String getSPRelativeStructuredId() {
			return structuredId;
		}
		public String getAbsoluteStructuredId() {
			return unstructuredId;
		}
		public String getSPRelativeUnstructuredId() {
			return structuredId == null ? null : serviceProviderId + structuredId;
		}
		public String getAbsoluteUnstructuredId() {
			return unstructuredId == null ? null : serviceProviderId + unstructuredId;
		}
		public boolean isSameResource(String resId) {
			return resId.equalsIgnoreCase(this.getSPRelativeStructuredId()) ||
					resId.equalsIgnoreCase(this.getSPRelativeUnstructuredId()) ||
					resId.equalsIgnoreCase(this.getAbsoluteStructuredId()) || 
					resId.equalsIgnoreCase(this.getAbsoluteUnstructuredId());
		}
		public String getServiceProviderId() {
			return this.serviceProviderId;
		}
	}
	
	private transient Originator originator;
}
