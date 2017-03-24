package org.eclipse.leshan.server;

import org.apache.commons.configuration.XMLConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lwm2mServerConfig {
	private static final String CONFIG_FILENAME = "config.xml";
	
	private final static XMLConfiguration xmlConfig = new XMLConfiguration();
	private Logger log = LoggerFactory.getLogger(Lwm2mServerConfig.class);
	
	private static Lwm2mServerConfig instance = null;
	
	// lwm2m_ipe
	private String localAddress = "";
	private int localPort = 0;
	private String secureLocalAddress = "";
	private int secureLocalPort = 0;
	private int webPort = 8085;
	private String redisUrl = null;
	
	// dm_server
	private String dmAddress = "";
	private int dmPort = 0;
	
	// si_server
	private boolean isUsing = false;
	private String siAddress = "";
	private int siNormalPort = 0;
	private int siAuthPort = 0;
	private String incseAddress = "";
	private String authAddress = "";
	
	public static Lwm2mServerConfig getInstance(){
		if(instance == null){
			try {
				instance = new Lwm2mServerConfig();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return instance;
	}
	
	public Lwm2mServerConfig() throws Exception {
		loadConfig();
	}
	
	private void loadConfig() throws Exception {
		
		try {
			log.info("Configuration loading started!!!");
			xmlConfig.load(CONFIG_FILENAME);
			
			
			// dm_server
			dmAddress = xmlConfig.getString("dm.web.address");
			dmPort = xmlConfig.getInt("dm.web.port");

			// lwm2m_ipe
			isUsing = xmlConfig.getString("ipe.use").equals("yes") ? true : false;
			localAddress = xmlConfig.getString("ipe.local.address");
			localPort = xmlConfig.getInt("ipe.local.port");
			secureLocalAddress = xmlConfig.getString("ipe.secure.local.address");
			secureLocalPort = xmlConfig.getInt("ipe.secure.local.port");
			webPort = xmlConfig.getInt("ipe.web.port");
			redisUrl = xmlConfig.getString("ipe.redis.url");

			// si_server
			siAddress = xmlConfig.getString("si.local.address");
			siNormalPort = xmlConfig.getInt("si.local.port.normal");
			siAuthPort = xmlConfig.getInt("si.local.port.auth");
			incseAddress = "http://"+siAddress+":"+siNormalPort+"/herit-in/herit-cse";
			authAddress = "http://"+siAddress+":"+siAuthPort+"/si/dev_inf";
			

			log.info("*= Configuration loading succeeded!!!");
		} catch (Exception e) {

			log.error("Exception during Configuration loading: ", e);
			
			throw e;
		}
	}

	@Override
	public String toString() {
		return "Lwm2mServerConfig [localAddress=" + localAddress + ", localPort=" + localPort + ", secureLocalAddress="
				+ secureLocalAddress + ", secureLocalPort=" + secureLocalPort + ", webPort=" + webPort + ", redisUrl="
				+ redisUrl + "]";
	}

	public String getLocalAddress() {
		return localAddress;
	}

	public void setLocalAddress(String localAddress) {
		this.localAddress = localAddress;
	}

	public int getLocalPort() {
		return localPort;
	}

	public void setLocalPort(int localPort) {
		this.localPort = localPort;
	}

	public String getSecureLocalAddress() {
		return secureLocalAddress;
	}

	public void setSecureLocalAddress(String secureLocalAddress) {
		this.secureLocalAddress = secureLocalAddress;
	}

	public int getSecureLocalPort() {
		return secureLocalPort;
	}

	public void setSecureLocalPort(int secureLocalPort) {
		this.secureLocalPort = secureLocalPort;
	}

	public int getWebPort() {
		return webPort;
	}

	public void setWebPort(int webPort) {
		this.webPort = webPort;
	}

	public String getRedisUrl() {
		return redisUrl;
	}

	public void setRedisUrl(String redisUrl) {
		this.redisUrl = redisUrl;
	}

	public String getDmAddress() {
		return dmAddress;
	}

	public void setDmAddress(String dmAddress) {
		this.dmAddress = dmAddress;
	}

	public int getDmPort() {
		return dmPort;
	}

	public void setDmPort(int dmPort) {
		this.dmPort = dmPort;
	}

	public String getSiAddress() {
		return siAddress;
	}

	public void setSiAddress(String siAddress) {
		this.siAddress = siAddress;
	}

	public int getSiNormalPort() {
		return siNormalPort;
	}

	public void setSiNormalPort(int siNormalPort) {
		this.siNormalPort = siNormalPort;
	}

	public int getSiAuthPort() {
		return siAuthPort;
	}

	public void setSiAuthPort(int siAuthPort) {
		this.siAuthPort = siAuthPort;
	}

	public String getIncseAddress() {
		return incseAddress;
	}

	public void setIncseAddress(String incseAddress) {
		this.incseAddress = incseAddress;
	}

	public String getAuthAddress() {
		return authAddress;
	}

	public void setAuthAddress(String authAddress) {
		this.authAddress = authAddress;
	}

	public boolean isUsing() {
		return isUsing;
	}

	public void setUsing(boolean isUsing) {
		this.isUsing = isUsing;
	}

	
}
