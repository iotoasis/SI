package net.herit.iot.message.onem2m.format;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;



public class Enums {

	public enum CONTENT_TYPE {
		NONE(0, "none"),
		XML(1, "application/xml"),
		JSON(2, "application/json"),
		RES_XML(3, "application/vnd.onem2m-res+xml"),
		RES_JSON(4, "application/vnd.onem2m-res+json"),
		NTFY_XML(5, "application/vnd.onem2m-ntfy+xml"),
		NTFY_JSON(6, "application/vnd.onem2m-ntfy+json"),
		ATTRS_XML(7, "application/vnd.onem2m-attrs+xml"),
		ATTRS_JSON(8, "application/vnd.onem2m-attrs+json"),
		TEXT_PLAIN(100, "text/plain");	// not standard. not defined in oneM2M specific MIME media types(TS.0004-6.7)
	
		final int value;
		final String name;
		private CONTENT_TYPE(int value, String name) {
			this.value = value;
			this.name = name;
		}
		
		public int Value() {
			return this.value;
		}
		
		public String Name() {
			return this.name;
		}
	
		private static final Map<String, CONTENT_TYPE> map = 
				new HashMap<String, CONTENT_TYPE>();
		static {
			for(CONTENT_TYPE en : CONTENT_TYPE.values()) {
				map.put(en.name, en);
			}
		}
		
		public static CONTENT_TYPE get(String format) {
			CONTENT_TYPE en = map.get(format);
			return en;
		}		
	};

	public enum CSE_TYPE { 
		IN_CSE(1, "IN_CSE"),
		MN_CSE(2, "MN_CSE"),
		ASN_CSE(3, "ASN_CSE");
	
		final int value;
		final String name;
		private CSE_TYPE(int value, String name) {
			this.value = value;
			this.name = name;
		}
		
		public int Value() {
			return this.value;
		}
		
		public String Name() {
			return this.name;
		}
	
		private static final Map<String, CONTENT_TYPE> map = 
				new HashMap<String, CONTENT_TYPE>();
		static {
			for(CONTENT_TYPE en : CONTENT_TYPE.values()) {
				map.put(en.name, en);
			}
		}
		
		public static CONTENT_TYPE get(String format) {
			CONTENT_TYPE en = map.get(format);
			return en;
		}
	};

	public enum REQUEST_STATUS { 
		COMPLETED(1, "COMPLETED"),
		FAILED(2, "FAILED"),
		PENDING(3, "PENDING"),
		FORWARDED(4, "FORWARDED");
	
		final int value;
		final String name;
		private REQUEST_STATUS(int value, String name) {
			this.value = value;
			this.name = name;
		}
		
		public int Value() {
			return this.value;
		}
		
		public String Name() {
			return this.name;
		}
	
		private static final Map<String, REQUEST_STATUS> map = 
				new HashMap<String, REQUEST_STATUS>();
		static {
			for(REQUEST_STATUS en : REQUEST_STATUS.values()) {
				map.put(en.name, en);
			}
		}
		
		public static REQUEST_STATUS get(String format) {
			REQUEST_STATUS en = map.get(format);
			return en;
		}		
	};

	public enum EVENT_CATEGORY { 
		IMMEDIATE(1, "IMMEDIATE"),
		BEST_EFFORT(2, "BEST_EFFORT"),
		LATEST(3, "LATEST");
	
		final int value;
		final String name;
		private EVENT_CATEGORY(int value, String name) {
			this.value = value;
			this.name = name;
		}
		
		public int Value() {
			return this.value;
		}
		
		public String Name() {
			return this.name;
		}
	
		private static final Map<Integer, EVENT_CATEGORY> map = 
				new HashMap<Integer, EVENT_CATEGORY>();
		static {
			for(EVENT_CATEGORY en : EVENT_CATEGORY.values()) {
				map.put(en.value, en);
			}
		}
		
		public static EVENT_CATEGORY get(int format) {
			EVENT_CATEGORY en = map.get(format);
			return en;
		}	
	};

	public enum FILTER_USAGE { 
		DISCOVERY(1, "DISCOVERY"),
		CONDITIONAL_RETRIEVAL(2, "CONDITIONAL_RETRIEVAL");
	
		final int value;
		final String name;
		private FILTER_USAGE(int value, String name) {
			this.value = value;
			this.name = name;
		}
		
		public int Value() {
			return this.value;
		}
		
		public String Name() {
			return this.name;
		}
	
		private static final Map<Integer, FILTER_USAGE> map = 
				new HashMap<Integer, FILTER_USAGE>();
		static {
			for(FILTER_USAGE en : FILTER_USAGE.values()) {
				map.put(en.value, en);
			}
		}
		
		public static FILTER_USAGE get(int format) {
			FILTER_USAGE en = map.get(format);
			return en;
		}	
	};
	
	public enum CONSISTENCY_STRATEGY {
		NONE(0, "NONE"),
		ABANDON_MEMBER(1, "ABANDON_MEMBER"),
		ABANDON_GROUP(2, "ABANDON_GROUP"),
		SET_MIXED(3, "SET_MIXED");
	
		final int value;
		final String name;
		private CONSISTENCY_STRATEGY(int value, String name) {
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
		
		private static final Map<Integer, CONSISTENCY_STRATEGY> map = 
				new HashMap<Integer, CONSISTENCY_STRATEGY>();
		static {
			for(CONSISTENCY_STRATEGY en : CONSISTENCY_STRATEGY.values()) {
				map.put(en.value, en);
			}
		}
		
		public static CONSISTENCY_STRATEGY get(int value) {
			CONSISTENCY_STRATEGY en = map.get(value);
			if(en == null) return NONE;
			return en;
		}
	}

	public enum MEMBER_TYPE {
		MIXED(0, "mixed"),
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
		SEMANTICDESCRIPTOR(24, "semanticDescriptor"),
		NOTIFICATIONTARGETMGMTPOLICYREF(25, "notificationTargetMgmtPolicyRef"),
		NOTIFICATIONTARGETPOLICY(26, "notificationTargetPolicy"),
		POLICYDELETIONRULES(27, "policyDeletionRules"),
		FLEXCONTAINER(28, "flexContainer"),
		TIMESERIES(29, "timeSeries"),
		TIMESERIESINSTANCE(30, "timeSeriesInstance"),
		ROLE(31, "role"),
		TOKEN(32, "token"),
		TRAFFICPATTERN(33, "trafficPattern"),
		DYNAMICAUTHORIZATIONCONSULTATION(34, "dynamicAuthorizationConsultation");
	
		final int value;
		final String name;
		private MEMBER_TYPE(int value, String name) {
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
		
		private static final Map<Integer, MEMBER_TYPE> map = 
				new HashMap<Integer, MEMBER_TYPE>();
		static {
			for(MEMBER_TYPE en : MEMBER_TYPE.values()) {
				map.put(en.value, en);
			}
		}
		
		public static MEMBER_TYPE get(int value) {
			MEMBER_TYPE en = map.get(value);
			if(en == null) return MIXED;
			return en;
		}
	}
	
	

	// MgmtCmd 관련 
	public enum CMD_TYPE { 
		RESET(1, "RESET"),
		REBOOT(2, "REBOOT"),
		UPLOAD(3, "UPLOAD"),
		DOWNLOAD(4, "DOWNLOAD"),
		SOFTWAREINSTALL(5, "SOFTWARE_INSTALL"),
		SOFTWAREUNINSTALL(6, "SOFTWARE_UNINSTALL"),
		SOFTWAREUPDATE(7, "SOFTWARE_UPDATE");
	
		final int value;
		final String name;
		private CMD_TYPE(int value, String name) {
			this.value = value;
			this.name = name;
		}
		
		public int Value() {
			return this.value;
		}
		
		public String Name() {
			return this.name;
		}
	
		private static final Map<Integer, CMD_TYPE> map = 
				new HashMap<Integer, CMD_TYPE>();
		static {
			for(CMD_TYPE en : CMD_TYPE.values()) {
				map.put(en.Value(), en);
			}
		}
		
		public static CMD_TYPE get(Integer val) {
			CMD_TYPE en = map.get(val);
			return en;
		}
	};

	public enum EXEC_STATUS { 
		INITIATED(1, "INITIATED"),
		PENDING(2, "PENDING"),
		FINISHED(3, "FINISHED"),
		CANCELLING(4, "CANCELLING"),
		CANCELLED(5, "CANCELLED"),
		STATUS_NON_CANCELLABLEL(6, "STATUS_NON_CANCELLABLEL");
	
		final int value;
		final String name;
		private EXEC_STATUS(int value, String name) {
			this.value = value;
			this.name = name;
		}
		
		public int Value() {
			return this.value;
		}
		
		public String Name() {
			return this.name;
		}
	
		private static final Map<Integer, EXEC_STATUS> map = 
				new HashMap<Integer, EXEC_STATUS>();
		static {
			for(EXEC_STATUS en : EXEC_STATUS.values()) {
				map.put(en.Value(), en);
			}
		}
		
		public static EXEC_STATUS get(Integer val) {
			EXEC_STATUS en = map.get(val);
			return en;
		}
	};
	
	public enum EXEC_RESULT { 
		REQUEST_UNSUPPORTED(1, "STATUS_REQUEST_UNSUPPORTED"),
		REQUEST_DENIED(2, "STATUS_REQUEST_DENIED"),
		CANCELLATION_DENIED(3, "STATUS_CANCELLATION_DENIED"),
		INTERNAL_ERROR(4, "STATUS_INTERNAL_ERROR"),
		INVALID_ARGUMENTS(5, "STATUS_INVALID_ARGUMENTS"),
		RESOURCES_EXCEEDED(6, "STATUS_RESOURCES_EXCEEDED"),
		FILE_TRANSFER_FAILED(7, "STATUS_FILE_TRANSFER_FAILED"),
		FILE_TRANSFER_SERVER_AUTHENTICATION_FAILURE(8, "STATUS_FILE_TRANSFER_SERVER_AUTHENTICATION_FAILURE"),
		UNSUPPORTED_PROTOCOL(9, "STATUS_UNSUPPORTED_PROTOCOL"),
		UPLOAD_FAILED(10, "STATUS_UPLOAD_FAILED"),
		FILE_TRANSFER_FAILED_MULTICAST_GROUP_UNABLE_JOIN(11, "STATUS_FILE_TRANSFER_FAILED_MULTICAST_GROUP_UNABLE_JOIN"),
		FILE_TRANSFER_FAILED_SERVER_CONTACT_FAILED(12, "STATUS_FILE_TRANSFER_FAILED_SERVER_CONTACT_FAILED"),
		FILE_TRANSFER_FAILED_FILE_ACCESS_FAILED(13, "STATUS_FILE_TRANSFER_FAILED_FILE_ACCESS_FAILED"),
		FILE_TRANSFER_FAILED_DOWNLOAD_INCOMPLETE(14, "STATUS_FILE_TRANSFER_FAILED_DOWNLOAD_INCOMPLETE"),
		FILE_TRANSFER_FAILED_FILE_CORRUPTED(15, "STATUS_FILE_TRANSFER_FAILED_FILE_CORRUPTED"),
		FILE_TRANSFER_FILE_AUTHENTICATION_FAILURE(16, "STATUS_FILE_TRANSFER_FILE_AUTHENTICATION_FAILURE"),
		//FILE_TRANSFER_FAILED(17, "STATUS_FILE_TRANSFER_FAILED"),
		//FILE_TRANSFER_SERVER_AUTHENTICATION_FAILURE(18, "STATUS_FILE_TRANSFER_SERVER_AUTHENTICATION_FAILURE"),
		FILE_TRANSFER_WINDOW_EXCEEDED(19, "STATUS_FILE_TRANSFER_WINDOW_EXCEEDED"),
		INVALID_UUID_FORMAT(20, "STATUS_INVALID_UUID_FORMAT"),
		UNKNOWN_EXECUTION_ENVIRONMENT(21, "STATUS_UNKNOWN_EXECUTION_ENVIRONMENT"),
		DISABLED_EXECUTION_ENVIRONMENT(22, "STATUS_DISABLED_EXECUTION_ENVIRONMENT"),
		EXECUTION_ENVIRONMENT_MISMATCH(23, "STATUS_EXECUTION_ENVIRONMENT_MISMATCH"),
		DUPLICATE_DEPLOYMENT_UNIT(24, "STATUS_DUPLICATE_DEPLOYMENT_UNIT"),
		SYSTEM_RESOURCES_EXCEEDED(25, "STATUS_SYSTEM_RESOURCES_EXCEEDED"),
		UNKNOWN_DEPLOYMENT_UNIT(26, "STATUS_UNKNOWN_DEPLOYMENT_UNIT"),
		INVALID_DEPLOYMENT_UNIT_STATE(27, "STATUS_INVALID_DEPLOYMENT_UNIT_STATE"),
		INVALID_DEPLOYMENT_UNIT_UPDATE_DOWNGRADE_DISALLOWED(28, "STATUS_INVALID_DEPLOYMENT_UNIT_UPDATE_DOWNGRADE_DISALLOWED"),
		INVALID_DEPLOYMENT_UNIT_UPDATE_UPGRADE_DISALLOWED(29, "STATUS_INVALID_DEPLOYMENT_UNIT_UPDATE_UPGRADE_DISALLOWED"),
		INVALID_DEPLOYMENT_UNIT_UPDATE_VERSION_EXISTS(30, "STATUS_INVALID_DEPLOYMENT_UNIT_UPDATE_VERSION_EXISTS");
	
		final int value;
		final String name;
		private EXEC_RESULT(int value, String name) {
			this.value = value;
			this.name = name;
		}
		
		public int Value() {
			return this.value;
		}
		
		public String Name() {
			return this.name;
		}
	
		private static final Map<Integer, EXEC_RESULT> map = 
				new HashMap<Integer, EXEC_RESULT>();
		static {
			for(EXEC_RESULT en : EXEC_RESULT.values()) {
				map.put(en.Value(), en);
			}
		}
		
		public static EXEC_RESULT get(Integer val) {
			EXEC_RESULT en = map.get(val);
			return en;
		}
	};
	
	public enum LOGICAL_OPERATOR { 
		AND(1, "AND"),
		OR(2, "OR");
	
		final int value;
		final String name;
		private LOGICAL_OPERATOR(int value, String name) {
			this.value = value;
			this.name = name;
		}
		
		public int Value() {
			return this.value;
		}
		
		public String Name() {
			return this.name;
		}
	
		private static final Map<String, CONTENT_TYPE> map = 
				new HashMap<String, CONTENT_TYPE>();
		static {
			for(CONTENT_TYPE en : CONTENT_TYPE.values()) {
				map.put(en.name, en);
			}
		}
		
		public static CONTENT_TYPE get(String format) {
			CONTENT_TYPE en = map.get(format);
			return en;
		}
	};
}
