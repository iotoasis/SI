package net.herit.iot.onem2m.incse.facility;

import io.netty.handler.codec.http.HttpVersion;

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
import net.herit.iot.onem2m.incse.context.OneM2mContext;

public class CfgManager {
	
	private static CfgManager INSTANCE = new CfgManager();
	
	private OneM2mContext context = null;
	
	private static final String RESOURCE_DBNAME = "resource";
	private static final String CONFIGURATION_DBNAME = "configuration";
	private static final String RESTSUBSCRIPTION_DBNAME = "restSubscription";
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
				}
			}

			log.info("Configuration loading succeeded!!!");

		} catch (Exception e) {

			log.error("Exception during Configuration loading!!!", e);
			
		}
		// remoteCSE 목록 추가
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
		return "http://"+xmlConfig.getString("cse.host")+":8080";
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
	
	public int getCommandTimeout() {
		return xmlConfig.getInt("cmdh.commandTimeout");
		//return 10;	
	}
	
	public int getCommandExpireTimerInterval() {
		return xmlConfig.getInt("cmdh.commandExpireTimerInterval");
		//return 1;
	}
	
	public class RemoteCSEInfo {
		private String cseid;
		private List<String> pointOfAccess;
		public RemoteCSEInfo(String cseid, String poa) {	setCseId(cseid);	addPointOfAccess(poa); }
		public String getCseId() { return cseid; }
		public void setCseId(String cseid) { this.cseid = cseid; }
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

	public int getHttpServerPort() {
		try {
			return xmlConfig.getInt("cse.httpPort");	// 8080;
		} catch (Exception e) {
			return 8080;	// default port number
		}
	}

	public int getRestServerPort() {
		try {
			return xmlConfig.getInt("cse.restPort");	//8081;
		} catch (Exception e) {
			return 8081;	// default port number
		}
	}
	
	public String getMqttBrokerAddress() {
		return xmlConfig.getString("mqtt.broker.address");
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
	
	public boolean isSupportHttp() {
		try {
			return xmlConfig.getBoolean("binding.http");
		} catch (Exception e) {
			return true;
		}
	}
	
	public boolean isSupportMqtt() {
		try {
			return xmlConfig.getBoolean("binding.mqtt");
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean isSupportCoap() {
		try {
			return xmlConfig.getBoolean("binding.coap");
		} catch (Exception e) {
			return false;
		}
	}
}
