package org.eclipse.leshan.server;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.configuration.XMLConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lwm2mServerConfig {
	private static final String CONFIG_FILENAME = "config.xml";
	
	private final static XMLConfiguration xmlConfig = new XMLConfiguration();
	private Logger log = LoggerFactory.getLogger(Lwm2mServerConfig.class);
	
	private static Lwm2mServerConfig instance = null;
	
	// si_server
	private String siIp = "[siIp]";
	private int siPortNormal = 0;
	private int siPortAuth = 0;
	private boolean siAuthUse = false;
	private String siUri = "[siUri]";
	private String siUriAuth = "[siUriAuth]";
	private String siResourceId = "[siResourceId]";
	private String siResourceName = "[siResourceName]";
	private int retryInterval = 10;
	
	// dm_server
	private boolean dmUsing = false;
	private String dmIp = "[dmIp]";
	private int dmPort = 0;
	
	// lwm2m_ipe
	private boolean ipeUsing = false;
	private String ipeIp = "[ipeIp]";
	private int ipePortNormal = 0;
	private int ipePortSecure = 0;
	private int ipePortWeb = 0;
	private int reportInterval = 30;
	
	// debug log
	private boolean debug = false;	
	
	
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
			
			// si_server
			siIp = xmlConfig.getString("si.ip");
			siPortNormal = xmlConfig.getInt("si.port.normal");
			siPortAuth = xmlConfig.getInt("si.port.auth");
			siAuthUse = xmlConfig.getString("si.auth").equals("yes");
			siResourceId = xmlConfig.getString("si.resourceId");
			siResourceName = xmlConfig.getString("si.resourceName");
			siUri = "http://"+siIp+":"+siPortNormal+"/"+siResourceId+"/"+siResourceName;
			siUriAuth = "http://"+siIp+":"+siPortAuth+"/si/dev_inf";
			retryInterval = xmlConfig.getInt("si.retryInterval") * 1000;
			
			// dm_server
			dmUsing = xmlConfig.getString("dm.use").equals("yes");
			dmIp = xmlConfig.getString("dm.web.ip");
			dmPort = xmlConfig.getInt("dm.web.port");
			
			// lwm2m_ipe
			ipeUsing = xmlConfig.getString("ipe.use").equals("yes");
			ipeIp = xmlConfig.getString("ipe.ip");
			ipePortNormal = xmlConfig.getInt("ipe.port.normal");
			ipePortSecure = xmlConfig.getInt("ipe.port.secure");
			ipePortWeb = xmlConfig.getInt("ipe.port.web");
			reportInterval = xmlConfig.getInt("ipe.report.interval") * 1000;
			
			// debug log
			debug = xmlConfig.getString("log.debug").equals("yes");
			
			log.info("*= Configuration loading succeeded!!!");
			
		} catch (Exception e) {
			log.error("Exception during Configuration loading: ", e);
			throw e;
		}
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		/*
		Method[] methods = this.getClass().getMethods();
		for(Method method : methods){
			try{
				Object result = method.invoke(new Object());
				
				
			} catch(Exception e) {
				e.printStackTrace();
			}
			try {
				sb.append("[").append(method.getName()).append("] : ");
			} catch (Exception e) {}
		}*/
		return null;
	}

	public String getSiIp() {
		return siIp;
	}

	public int getSiPortNormal() {
		return siPortNormal;
	}

	public int getSiPortAuth() {
		return siPortAuth;
	}

	public String getSiUri() {
		return siUri;
	}

	public String getSiUriAuth() {
		return siUriAuth;
	}

	public String getSiResourceId() {
		return siResourceId;
	}

	public String getSiResourceName() {
		return siResourceName;
	}

	public int getRetryInterval() {
		return retryInterval;
	}

	public boolean isDmUsing() {
		return dmUsing;
	}

	public String getDmIp() {
		return dmIp;
	}

	public int getDmPort() {
		return dmPort;
	}

	public boolean isIpeUsing() {
		return ipeUsing;
	}

	public String getIpeIp() {
		return ipeIp;
	}

	public int getIpePortNormal() {
		return ipePortNormal;
	}

	public int getIpePortSecure() {
		return ipePortSecure;
	}

	public int getIpePortWeb() {
		return ipePortWeb;
	}

	public boolean isDebug() {
		return debug;
	}

	public int getReportInterval() {
		return reportInterval;
	}

	public boolean isSiAuthUse() {
		return siAuthUse;
	}
	
	
}
