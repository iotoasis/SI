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
		DELETE(8, "DELETE"), NOTIFY(16, "NOTIFY"), DISCOVERY(32, "DISCOVERY");		// correct notify and discovery digit number from notify(32) and discovery(16) to notify(16) and discovery(32) in 2017-11-03 
		
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
		BLOCK_REQ(3, "BLOCKING_REQUEST"),
		FLEX_BLOCK(4, "FLEX_BLOCKING");
		
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
	
	public enum SHORT_RESOURCE_TYPE {
		NONE("none", 0),
		ACCESS_CTRL_POLICY("acp", 1),
		AE("ae", 2),
		CONTAINER("cnt", 3),
		CONTENT_INST("cin", 4),
		CSE_BASE("cb", 5),
		DELIVERY("dlv", 6),
		EVENT_CONFIG("evcg", 7),
		EXEC_INST("exin", 8),
		GROUP("grp", 9),
		LOCAT_POLICY("lcp", 10),
		M2M_SVC_SUBSC_PROF("mssp", 11),
		MGMT_CMD("mgc", 12),
		MGMT_OBJ("mgo", 13),
		NODE("nod", 14),
		POLLING_CHANN("pch", 15),
		REMOTE_CSE("csr", 16),
		REQUEST("req", 17),
		SCHEDULE("sch", 18),
		SVC_SUBSC_APP_RULE("asar", 19),
		SVC_SUBSC_NODE("svsn", 20),
		STATS_COLLECT("stcl", 21),
		STATS_CONFIG("stcg", 22),
		SUBSCRIPTION("sub", 23),
		SEMANTICDESCRIPTOR("smd" ,24),												// added in CDT-2.7.0
		NOTIFICATIONTARGETMGMTPOLICYREF("ntpr", 25),								// added in CDT-2.7.0
		NOTIFICATIONTARGETPOLICY("ntp", 26),					// added in CDT-2.7.0
		POLICYDELETIONRULES("pdr", 27),												// added in CDT-2.7.0
		//FLEXCONTAINER(28, "flexContainer"),											// added in CDT-2.7.0
		TIMESERIES("ts", 29),														// added in CDT-2.7.0
		TIMESERIESINSTANCE("tsi", 30),												// added in CDT-2.7.0
		ROLE("rol", 31),															// added in CDT-2.7.0
		TOKEN("tk", 32),															// added in CDT-2.7.0
		TRAFFICCHARACTERISTICS("trch", 33),											// added in CDT-2.7.0
		TRAFFICPATTERN("trpt", 34),													// added in CDT-2.7.0
		DYNAMICAUTHORIZATIONCONSULTATION("dac", 35),								// added in CDT-2.7.0
		ACCESS_CTRL_POLICY_ANNC("acpA", 10001),
		AE_ANNC("aeA", 10002),
		CONTAINER_ANNC("cntA", 10003),
		CONTENT_INST_ANNC("cinA", 10004),
		GROUP_ANNC("grpA", 10009),
		LOCAT_POLICY_ANNC("lcpA", 10010),
//		MGMT_OBJ_ANNC(10013, "mgmtObjAnnc"),
		NODE_ANNC("nodA", 10014),
		REMOTE_CSE_ANNC("csrA", 10016),
		SCHEDULE_ANNC("schA", 10018),
		SEMANTICDESCIRPTOR_ANNC("smdA", 10024),										// added in CDT-2.7.0
//		FLEXCONTAINER_ANNC(10028, "flexContainerAnnc"),								// added in CDT-2.7.0
		TIMESERIES_ANNC("tsa", 10029),												// added in CDT-2.7.0
		TIMESERIESINSTANCE_ANNC("tsia", 10030),										// added in CDT-2.7.0
		TRAFFICCHARACTERISTICS_ANNC("trchA", 10033),								// added in CDT-2.7.0
		TRAFFICPATTERN_ANNC("trptA", 10034),										// added in CDT-2.7.0
		DYNAMICAUTHORIZATIONCONSULTATION_ANNC("dacA", 10035),						// added in CDT-2.7.0
//		NOTIFICATION(90001, "notification"),
		MGMT_FIRMWARE("fwr", 1001),
		MGMT_SOFTWARE("swr", 1002),
		MGMT_MEMORY("mem", 1003),
		MGMT_AREANWK_INFO("ani", 1004),
		MGMT_AREANWK_DEVICEINFO("andi", 1005),
		MGMT_BATTERY("bat", 1006),
		MGMT_DEVICE_INFO("dvi", 1007),
		MGMT_DEVICE_CAPA("dvc", 1008),
		MGMT_REBOOT("rbo", 1009),
		MGMT_EVENT_LOG("evl", 1010),
		MGMT_CMDH_POLICY("cmp", 1011),
		MGMT_ACTIVE_CMDH_POLICY("acmp", 1012),
		MGMT_CMDH_DEFAULTS("cmdf", 1013),
		MGMT_CMDH_DEFECVALUCE("cmdv", 1014),
		MGMT_CMDH_ECDEF_PARAM_VALUES("cmpv", 1015),
		MGMT_CMDH_LIMIT("cml", 1016),
		MGMT_CMDH_NETWORK_ACCESS_RULES("cmnr", 1017),
		MGMT_CMDH_NW_ACCESS_RULE("cmwr", 1018),
		MGMT_CMDH_BUFFER("cmbf", 1019),	// 90001 ~ : user defined
		AGGREGATED_RESPONSE("agr", 90010);
		
		final String name;
		final int value;
		private SHORT_RESOURCE_TYPE(String name, int value) {
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
		
		private static final Map<String, SHORT_RESOURCE_TYPE> map = 
				new HashMap<String, SHORT_RESOURCE_TYPE>();
		static {
			for(SHORT_RESOURCE_TYPE en : SHORT_RESOURCE_TYPE.values()) {
				map.put(en.name, en);
			}
		}
		
		public static SHORT_RESOURCE_TYPE get(String name) {
			SHORT_RESOURCE_TYPE en = map.get(name);
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
		SEMANTICDESCRIPTOR(24, "semanticDescriptor"),								// added in CDT-2.7.0
		NOTIFICATIONTARGETMGMTPOLICYREF(25, "notificationTargetMgmtPolicyRef"),		// added in CDT-2.7.0
		NOTIFICATIONTARGETPOLICY(26, "notificationTargetPolicy"),					// added in CDT-2.7.0
		POLICYDELETIONRULES(27, "policyDeletionRules"),								// added in CDT-2.7.0
		FLEXCONTAINER(28, "flexContainer"),											// added in CDT-2.7.0
		TIMESERIES(29, "timeSeries"),												// added in CDT-2.7.0
		TIMESERIESINSTANCE(30, "timeSeriesInstance"),								// added in CDT-2.7.0
		ROLE(31, "role"),															// added in CDT-2.7.0
		TOKEN(32, "token"),															// added in CDT-2.7.0
		TRAFFICCHARACTERISTICS(33, "trafficCharacteristics"),						// added in CDT-2.7.0
		TRAFFICPATTERN(34, "trafficPattern"),										// added in CDT-2.7.0
		DYNAMICAUTHORIZATIONCONSULTATION(35, "dynamicAuthorizationConsultation"),	// added in CDT-2.7.0
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
		SEMANTICDESCIRPTOR_ANNC(10024, "semanticDescriptorAnnc"),					// added in CDT-2.7.0
		FLEXCONTAINER_ANNC(10028, "flexContainerAnnc"),								// added in CDT-2.7.0
		TIMESERIES_ANNC(10029, "timeSeriesAnnc"),									// added in CDT-2.7.0
		TIMESERIESINSTANCE_ANNC(10030, "timeSeriesInstanceAnnc"),					// added in CDT-2.7.0
		TRAFFICCHARACTERISTICS_ANNC(10033, "trafficCharacteristicsAnnc"),			// added in CDT-2.7.0
		TRAFFICPATTERN_ANNC(10034, "trafficPatternAnnc"),							// added in CDT-2.7.0
		DYNAMICAUTHORIZATIONCONSULTATION_ANNC(10035, "dynamicAuthorizationConsultationAnnc"),			// added in CDT-2.7.0
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

	public String getNotificationUriList() {
		if(responseType == null || responseType.getNotificationURI() == null) return "";
		
		StringBuilder bld = new StringBuilder();
		for(String uri:responseType.getNotificationURI()) {
			bld.append(uri).append(" ");
		}
		return bld.toString();
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
//		System.out.println("Content-Type=" + this.contentType);
		if(this.acceptTypes == null || this.acceptTypes.size() == 0) {
			if(this.contentType != null && this.contentType != CONTENT_TYPE.NONE) {	 // 2015.09.14 added.
//				this.acceptTypes.add(contentType);
				addAcceptType(contentType.Name());
			}
//			else 
//			{
//				//this.acceptTypes.add(CONTENT_TYPE.XML);
//				addAcceptType(CONTENT_TYPE.XML.Name());
//			}
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
	
	public void addAcceptType(CONTENT_TYPE type) {
		this.acceptTypes.add(type);
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
//		bld.append(" Name:").append(name).append("\n"); deleted. XSD-1.6.0
		bld.append(" OriginatingTimestamp:").append(originatingTimestamp).append("\n");
		bld.append(" RequestExpirationTimestamp:").append(requestExpirationTimestamp).append("\n");
		bld.append(" ResultExpirationTimestamp:").append(resultExpirationTimestamp).append("\n");
		bld.append(" OperationExecutionTime:").append(operationExecutionTime).append("\n");
		bld.append(" ResponseType:").append(getResponseTypeEnum().Name()).append(": ").append(getNotificationUriList()).append("\n");
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
		//if (this.operation == OPERATION.NONE.Value()) {
			throw new OneM2MException(OneM2mResponse.RESPONSE_STATUS.INVALID_CMDTYPE, "No operation parameter specified");
		} else if (this.to == null) {
			throw new OneM2MException(OneM2mResponse.RESPONSE_STATUS.INSUFFICIENT_ARGUMENT, "No to parameter specified");
		} else if (this.from == null) {
			// In case of AE, from should be set to CSE defined rule, added in 2017-03-09
			if(RESOURCE_TYPE.AE.Value() == this.resourceType) {
				this.from = "S";
			} else {
				throw new OneM2MException(OneM2mResponse.RESPONSE_STATUS.INSUFFICIENT_ARGUMENT, "No from parameter specified");
			}
			
		} else if (this.requestIdentifier == null) {
			throw new OneM2MException(OneM2mResponse.RESPONSE_STATUS.INSUFFICIENT_ARGUMENT, "No requestId parameter specified");
		} 
		
		if (this.operation.equals(OPERATION.CREATE)) {
		//if (this.operation == OPERATION.CREATE.Value()) {
			//if (this.resourceType.equals(RESOURCE_TYPE.NONE)) {
			if (this.resourceType == null || this.resourceType == RESOURCE_TYPE.NONE.Value()) {
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
		//} else if (this.operation.equals(OPERATION.RETRIEVE)) {
		} else if (this.operation == OPERATION.RETRIEVE.Value()) {
			//if (!this.resourceType.equals(RESOURCE_TYPE.NONE)) {
			if (this.resourceType != null && this.resourceType != RESOURCE_TYPE.NONE.Value()) {
				throw new OneM2MException(OneM2mResponse.RESPONSE_STATUS.INVALID_ARGUMENTS, "resourceType parameter should be null");
			}
//			if (this.name != null) {	//???? Optional..?
//				throw new OneM2MException(OneM2mResponse.RESPONSE_STATUS.INVALID_ARGUMENTS, "name parameter should be null");
//			}
		} else if (this.operation.equals(OPERATION.UPDATE)) {
		//} else if (this.operation == OPERATION.UPDATE.Value()) {
			if (this.primitiveContent == null) {
				throw new OneM2MException(OneM2mResponse.RESPONSE_STATUS.INSUFFICIENT_ARGUMENT, "No content parameter specified");
			}
			if (!this.resourceType.equals(RESOURCE_TYPE.NONE)) {
			//if (this.resourceType != null && this.resourceType != RESOURCE_TYPE.NONE.Value()) {
				throw new OneM2MException(OneM2mResponse.RESPONSE_STATUS.INVALID_ARGUMENTS, "resourceType parameter should be null");
			}
//			if (this.name != null) {   //???? Optional..?
//				throw new OneM2MException(OneM2mResponse.RESPONSE_STATUS.INVALID_ARGUMENTS, "name parameter should be null");
//			}

		} else if (this.operation.equals(OPERATION.DELETE)) {
		//} else if (this.operation == OPERATION.DELETE.Value()) {
			//if (!this.resourceType.equals(RESOURCE_TYPE.NONE)) {
			if (this.resourceType != null && this.resourceType != RESOURCE_TYPE.NONE.Value()) {
				throw new OneM2MException(OneM2mResponse.RESPONSE_STATUS.INVALID_ARGUMENTS, "resourceType parameter should be null");
			}
//			if (this.name != null) {   //???? Optional..?
//				throw new OneM2MException(OneM2mResponse.RESPONSE_STATUS.INVALID_ARGUMENTS, "name parameter should be null");
//			}

		} else if (this.operation.equals(OPERATION.NOTIFY)) {
		//} else if (this.operation == OPERATION.NOTIFY.Value()) {	
			//if (!this.resourceType.equals(RESOURCE_TYPE.NONE)) {
			if (this.resourceType != null && this.resourceType != RESOURCE_TYPE.NONE.Value()) {
				throw new OneM2MException(OneM2mResponse.RESPONSE_STATUS.INVALID_ARGUMENTS, "resourceType parameter should be null");
			}
//			if (this.name != null) {  //???? Optional..?
//				throw new OneM2MException(OneM2mResponse.RESPONSE_STATUS.INVALID_ARGUMENTS, "name parameter should be null");
//			}

		} else if (this.operation.equals(OPERATION.DISCOVERY)) {
		//} else if (this.operation == OPERATION.DISCOVERY.Value()) {  	
			//if (!this.resourceType.equals(RESOURCE_TYPE.NONE)) {
			if (this.resourceType != null && this.resourceType != RESOURCE_TYPE.NONE.Value()) {
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
		
//		if (content == null && contentObject != null) {
		if (content == null && getContentObject() != null) {
	//		AbsSerializer serializer = AbsSerializer.getSerializer(CONTENT_TYPE.XML);
			AbsSerializer serializer = AbsSerializer.getSerializer(getContentType());

			content = serializer.serialize(contentObject).getBytes();
		}
		
		return content;
	}
	
	/**
	 * @return the contentObject
	 */
	public Object getContentObject() {
		if (contentObject == null) {
			
		/* updated in 2016-10-17 for CDT-2.7.0 as below
		 * 	if(getPrimitiveContent() != null && getPrimitiveContent().getAny().size() > 0) {
		 *
				contentObject = getPrimitiveContent().getAny().get(0);				// TODO: List Object 처리 필요..
			}
		*/
			if(getPrimitiveContent() != null && getPrimitiveContent().getAnyOrAny().size() > 0) {
				contentObject = getPrimitiveContent().getAnyOrAny().get(0);
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
		protected String originIp;			// ipv4 or ipv6 added in 2017-08-25
		public Originator(String structuredId, String unstructuredId, String serviceProviderId, String originIp) {
		//	System.out.println("structuredId=" + structuredId + ", unstructuredId=" + unstructuredId + ", serviceProviderId=" + serviceProviderId);
			this.structuredId = structuredId;
			this.unstructuredId = unstructuredId;
			this.serviceProviderId = serviceProviderId;
			this.originIp = originIp;			// added in 2017-08-25
		}
		
		public Originator(String structuredId, String unstructuredId, String serviceProviderId) {
		//	System.out.println("structuredId=" + structuredId + ", unstructuredId=" + unstructuredId + ", serviceProviderId=" + serviceProviderId);
			this.structuredId = structuredId;
			this.unstructuredId = unstructuredId;
			this.serviceProviderId = serviceProviderId;

		} 
		
		public String toString() {
			StringBuilder strBld = new StringBuilder();
			strBld.append("Originator]").append("structuredId=").append(structuredId).append("\r\n");
			strBld.append("unstructuredId=").append(unstructuredId).append("\r\n");
			strBld.append("serviceProviderId=").append(serviceProviderId).append("\r\n");
			strBld.append("originIp=").append(originIp);
			
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
		public String getOriginIp() {			// added in 2017-08-25
			return this.originIp;
		}
	}
	
	private transient Originator originator;
}
