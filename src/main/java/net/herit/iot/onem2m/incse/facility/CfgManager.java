package net.herit.iot.onem2m.incse.facility;

import io.netty.handler.codec.http.HttpVersion;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.herit.iot.message.onem2m.format.Enums.CONTENT_TYPE;
//import net.herit.iot.onem2m.incse.context.OneM2mContext;
import net.herit.iot.onem2m.resource.Naming;

public class CfgManager {
	
	private final static CfgManager INSTANCE = new CfgManager();
	
//	private OneM2mContext context = null;
	
	private static final int DEFAULT_HTTP_PORT = 8080;
	private static final int DEFAULT_REST_HTTP_PORT = 8081;
	
	private static final String RESOURCE_DBNAME = "resource";
	private static final String CONFIGURATION_DBNAME = "configuration";
	private static final String RESTSUBSCRIPTION_DBNAME = "restSubscription";
	private static final String CASAUTH_DBNAME = "auth";						// added by brianmoon at 2016-09-05
	private static final CONTENT_TYPE DEFAULT_CONTENT_TYPE = CONTENT_TYPE.XML;

	private final static HttpVersion		HTTP_VERSION 		= HttpVersion.HTTP_1_1;

	private Logger log = LoggerFactory.getLogger(CfgManager.class);

	private final static String	CONFIG_FILENAME	= "incse.xml"; //"iitp";
	
	private final static XMLConfiguration xmlConfig = new XMLConfiguration();
	
	private static Map<String, CSEQoSConfig> cseQoSMap = new HashMap<String, CSEQoSConfig>();
	
	public class CSEQoSConfig {
		private String cseId;
		private String cseName;
		private String cseHost;
		private int maxTps;
		
		CSEQoSConfig (String cseId, String cseName, String cseHost, int maxTps) {
			this.cseId = cseId;
			this.cseName = cseName;
			this.cseHost = cseHost;
			this.maxTps = maxTps;
		}
		public String getCseId() {	return cseId; }
		public String getCseName() {	return cseName; }
		public String getCseHost() {	return cseHost; }
		public int getCseMaxTps() {	return maxTps; }
		public String toString() { 
			StringBuilder sb = new StringBuilder();
			sb.append("cseId:"+ cseId +",");
			sb.append("cseName:"+ cseName +",");
			sb.append("cseHost:"+ cseHost +",");
			sb.append("maxTps:"+ maxTps);
			return sb.toString();
		}
	}
	
	private CfgManager() {}
		
	public static CfgManager getInstance() {
		return INSTANCE;
	}
	
	public void initialize() throws Exception {
		
		try {
			log.info("Configuration loading started!!!");
			xmlConfig.load(CONFIG_FILENAME);
			
			final List<HierarchicalConfiguration> cses = xmlConfig.configurationsAt("remoteCSEs.remoteCSE");
			if(cses instanceof Collection)
			{
				for(final HierarchicalConfiguration cse : cses) {
					CSEQoSConfig cfg = new CSEQoSConfig(cse.getString("cseId"),
							cse.getString("cseName"), 
							cse.getString("cseHost"), 
							cse.getInt("maxTPS"));					
					cseQoSMap.put(cse.getString("cseId"), cfg);
					log.info("CSE Config:"+cfg.toString());
					
					
					this.addRemoteCSEList(new RemoteCSEInfo("/" + cse.getString("cseId"),
//							"/" + cse.getString("cseName"), cse.getString("poa")));
//					this.addRemoteCSEList(new RemoteCSEInfo("/" + cse.getString("cseName"),
							"/" + cse.getString("cseName"), cse.getString("poa")));
				}
			}

			log.info("Configuration loading succeeded!!!");

		} catch (Exception e) {

			log.error("Exception during Configuration loading!!!", e);
			
		}
		// remoteCSE 목록 추가
//		this.addRemoteCSEList(new RemoteCSEInfo("herit-cse", "http://166.104.112.34:8080"));
		//this.addRemoteCSEList(new RemoteCSEInfo("//in-cse", "http://217.167.116.81:8080"));

//		String databasehaot = xmlConfig.getString("database.host");	//DATABASE_HOST;
//		int dbport = xmlConfig.getInt("database.port");	//DATABASE_PORT;
//		String dbname = xmlConfig.getString("database.dbname");	//DATABASE_NAME;
//		String dbuser = xmlConfig.getString("database.user");	//DATABASE_USER;
//		String dbpwd = xmlConfig.getString("database.password");	//DATABASE_PASSWD;
//		String basename = xmlConfig.getString("cse.baseName");	//CSEBASE_NAME;
//		String rid = xmlConfig.getString("cse.resourceId");	//CSEBASE_RID;
//		String chost = xmlConfig.getString("cse.host");	//HOST_NAME;
//		int cmdTimeout = xmlConfig.getInt("cmdh.commandTimeout");
//		int cmdExpireInterval = xmlConfig.getInt("cmdh.commandExpireTimerInterval");
//		
//		String dmaddr = xmlConfig.getString("dms.hitdm.address");	// "http://10.101.101.107:8888";
//		int httpPort = xmlConfig.getInt("cse.httpPort");	// 8080;
//		int restPort = xmlConfig.getInt("cse.restPort");	//8081;
		
	}

	public HttpVersion getHttpVersion() {
		return HTTP_VERSION;
	}
	public String getDatabaseHost() {
		return xmlConfig.getString("database.host");	//DATABASE_HOST;
	}
	public int getDatabasePort() {
		return xmlConfig.getInt("database.port");	//DATABASE_PORT;
	}
	public String getDatabaseName() {
		return xmlConfig.getString("database.dbname");	//DATABASE_NAME;
	}
	public String getDatabaseUser() {
		return "".equals(xmlConfig.getString("database.user")) ? null : xmlConfig.getString("database.user");	//DATABASE_USER;
	}
	public String getDatabasePassword() {
		return "".equals(xmlConfig.getString("database.password")) ? null : xmlConfig.getString("database.password");	//DATABASE_PASSWD;
	}
	
	public CONTENT_TYPE getDefaultContentType() {
		return DEFAULT_CONTENT_TYPE;
	}
	
	public String getCSEBaseName() {
		return xmlConfig.getString("cse.baseName");	//CSEBASE_NAME;
	}
	
	public String getCSEBaseUri() {
		return "/"+xmlConfig.getString("cse.resourceId")+"/"+xmlConfig.getString("cse.baseName");
	}
	
	public String getAbsoluteCSEBaseId() {
		return getServiceProviderId() + getCSEBaseCid();
	}

	public String getCSEBaseRid() {
		return xmlConfig.getString("cse.resourceId");	//CSEBASE_RID;
	}

	public String getCSEBaseCid() {
		return "/"+xmlConfig.getString("cse.resourceId");	//CSEBASE_RID;
	}

	public String getServiceProviderId() {
		return "//"+getHostname();	//HOST_NAME;
	}

	public String getHostname() {
		return xmlConfig.getString("cse.host");	//HOST_NAME;
	}
	
	public String getPointOfAccess() {
//		return "http://"+xmlConfig.getString("cse.host")+":8080";
//		return "http://"+xmlConfig.getString("cse.host")+":"+getHttpServerPort();
		return xmlConfig.getString("cse.poa");
	}
	
	public String getSemanticEngineProtocol() {				// added in 2016-10-26
		return xmlConfig.getString("semanticEngine.protocol");
	}
	
	public String getSemanticEngineHost() {				// added in 2016-10-26
		return xmlConfig.getString("semanticEngine.host");
	}
	
	public String getSemanticEnginePort() {				// added in 2016-10-26
		return xmlConfig.getString("semanticEngine.port");
	}
	
	public String getResourceDatabaseName() {
		return RESOURCE_DBNAME;
	}
	
	public String getConfigurationDatabaseName() {
		return CONFIGURATION_DBNAME;
	}
	
	public String getRestSubscriptionDatabaseName() {
		return RESTSUBSCRIPTION_DBNAME;
	}
	
	public String getCASAuthDatabaseName() {			// added by brianmoon at 2016-09-05
		return CASAUTH_DBNAME;
	}
	
	public int getCommandTimeout() {
		return xmlConfig.getInt("cmdh.commandTimeout");
		//return 10;	
	}
	
	public int getCommandExpireTimerInterval() {
		return xmlConfig.getInt("cmdh.commandExpireTimerInterval");
		//return 1;
	}
	
	public boolean getOptionsTTA() {
		return xmlConfig.getBoolean("options.tta");
	}
	
	public class RemoteCSEInfo {
		private String cseid;
		private String cseName;
		private List<String> pointOfAccess;
		public RemoteCSEInfo(String cseid, String cseName , String poa) {	
			setCseId(cseid);
			setCseName(cseName);
			addPointOfAccess(poa); }
		public String getCseId() { return cseid; }
		public void setCseId(String cseid) { this.cseid = cseid; }
		public String getCseName() { return cseName; }
		public void setCseName(String cseName) { this.cseName = cseName; }
		public List<String> getPointOfAccess() { return pointOfAccess != null ? pointOfAccess : new ArrayList<String>(); }
		public void setPointOfAccess(List<String> poa) { this.pointOfAccess = poa; } 
		public void addPointOfAccess(String poa) { if (pointOfAccess == null) pointOfAccess = new ArrayList<String>(); pointOfAccess.add(poa); } 
	}
	
	private List<RemoteCSEInfo> remoteCSEList;
	public List<RemoteCSEInfo> getRemoteCSEList() {
		return remoteCSEList;
	}
	public void setRemoteCSEList(List<RemoteCSEInfo> remoteCSEList) {
		this.remoteCSEList = remoteCSEList;
	}
	public void addRemoteCSEList(RemoteCSEInfo remoteCSE) {
		if (remoteCSEList == null)	remoteCSEList = new ArrayList<RemoteCSEInfo>();
		remoteCSEList.add(remoteCSE);
	}

	public String getHitDMAddress() {
		// TODO Auto-generated method stub
		return xmlConfig.getString("dms.hitdm.address");	// "http://10.101.101.107:8888";
	}
	
	public String getTr69DMAddress() {							// added in 2017-07-31
		return xmlConfig.getString("dms.tr69dm.address");
	}
	
	public int getTr69DMTimeout() {								// added in 2017-07-31
		return xmlConfig.getInt("dms.tr69dm.timeout");
	}
	
	public String getOneM2mAgentAddress() {						// added in 2017-08-21
		return xmlConfig.getString("dms.onem2mAgent.address");
	}

	public boolean isSupportHttp() {
		try {
			return xmlConfig.getString("binding.http.supported").equalsIgnoreCase("yes");
		} catch (Exception e) {
			return true;
		}
	}
	
	public boolean isSupportMqtt() {
		try {
			return xmlConfig.getString("binding.mqtt.supported").equalsIgnoreCase("yes");
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean isSupportWebSocket() {
		try {
			return xmlConfig.getString("binding.websocket.supported").equalsIgnoreCase("yes");
		} catch (Exception e) {
			return false;
		}
	}
	
	public int getKeepaliveInterval() {
		return xmlConfig.getInt("binding.mqtt.keepalive");
	}
	
	public boolean isSupportCoap() {
		try {
			return xmlConfig.getString("binding.coap.supported").equalsIgnoreCase("yes");
		} catch (Exception e) {
			return false;
		}
	}
	
	public int getHttpServerPort() {
		try {
			return xmlConfig.getInt("binding.http.port");	// 8080;
		} catch (Exception e) {
			return DEFAULT_HTTP_PORT;	// default port number
		}
	}

	public int getHttpsServerPort() {
		try {
			return xmlConfig.getInt("binding.http.sec-port");
		} catch (Exception e) {
			return -1;
		}
	}
	
	public int getRestServerPort() {
		try {
			return xmlConfig.getInt("binding.http.rest-port");
		} catch (Exception e) {
			return DEFAULT_REST_HTTP_PORT;	// default port number
		}
	}
	
	public int getNettyBossThreadPoolSize() {
		try {
			return xmlConfig.getInt("binding.http.netty.boss-threadPool-size");
		} catch(Exception e) {
			return 0;
		}
	}
	
	public int getNettyWorkerThreadPoolSize() {
		try {
			return xmlConfig.getInt("binding.http.netty.worker-threadPool-size");
		} catch(Exception e) {
			return 0;
		}
	}
	
	public int getCoapServerPort() {
		try {
			return xmlConfig.getInt("binding.coap.port");
		} catch (Exception e) {
			return 5683;
		}
	}
	
	public int getCoapsServerPort() {
		try {
			return xmlConfig.getInt("binding.coap.sec-port");
		} catch (Exception e) {
			return -1;
		}
	}
	
	public int getWebSocketServerPort() {
		try {
			return xmlConfig.getInt("binding.websocket.port");
		} catch (Exception e) {
			return 8887;
		}
	}
	
	public String getMqttBrokerAddress() {
		return xmlConfig.getString("binding.mqtt.broker");
	}

	public int getQOSMaxPollingSessionNo() {
		try {
			return xmlConfig.getInt("qos.maxPollingSessionNo");
		} catch (Exception e) {			
			return -1;	// default value
		}
	}
	public int getQOSMaxAENo() {
		try {
			return xmlConfig.getInt("qos.maxAENo");
		} catch (Exception e) {
			return -1;	// default value
		}
	}
	public int getQOSMaxCSENo() {
		try {
			return xmlConfig.getInt("qos.maxCSENo");
		} catch (Exception e) {
			return -1;	// default value
		}
	}
	
	public Map<String, CSEQoSConfig> getCSEQoSConfigMap() {
		return cseQoSMap; 
	}
	
	
	public int getMaxCIPerContainer() {
		try {
			return xmlConfig.getInt("resourcePolicy.maxCIPerContainer");
		} catch (Exception e) {
			return 100;	// default max CI Per Container
		}
	}
	
	public int getMaxCIByteSizePerContainer() {
		try {
			return xmlConfig.getInt("resourcePolicy.maxCIByteSizePerContainer");
		} catch (Exception e) {
			return 102400;	// default max CI Per Container
		}
	}
	
	public int getMaxCIAgePerContainer() {
		try {
			return xmlConfig.getInt("resourcePolicy.maxCIAgePerContainer");
		} catch (Exception e) {
			return 3600;	// default max CI Per Container
		}
	}
	
	private final static int DEFALUT_EXPIRATION_TIME = 3; // days.
	public String getDefaultExpirationTime() {
		int default_time = DEFALUT_EXPIRATION_TIME;
		try {
			default_time = xmlConfig.getInt("cse.default-resource-expiration-time");
		} catch (Exception e) {
//			default_time = DEFALUT_EXPIRATION_TIME;
		}
		long currentTime = System.currentTimeMillis() + (default_time * 24 * 60 * 60 * 1000);
		
		return new SimpleDateFormat(Naming.DATE_FORMAT).format(new java.util.Date(currentTime));
	}
	
	private final static int DEFAULT_SUBGROUP_DEPTH = 3;
	public int getAllowedSubGroupDepth() {
		try {
			return xmlConfig.getInt("cse.allowed-subgroup-depth");
		} catch (Exception e) {
			return DEFAULT_SUBGROUP_DEPTH;
		}
	}
	
	
	public static void main(String[] args) {
		long currT = System.currentTimeMillis() + (3 * 24 * 60 * 60 * 1000);
		String strTime = new SimpleDateFormat(Naming.DATE_FORMAT).format(new java.util.Date(currT));
		System.out.println(strTime);
	}
	
}
