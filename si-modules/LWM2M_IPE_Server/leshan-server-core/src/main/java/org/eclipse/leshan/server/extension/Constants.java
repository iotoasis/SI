package org.eclipse.leshan.server.extension;

import java.util.HashMap;
import java.util.Map;

public class Constants {

	// Device Info
	public static String OUI = "000001";
	
	// request header
	public static String CONTENT_TYPE = "application/json";
	public static String ORIGIN_BASE = "S";
	public static String UNNAMED = "unnamed";
	public static boolean USE_AUTH = false;
	
	// oneM2M info
	public static final String BASIC_NAME_SPACE = "m2m";
	public static final String BASIC_AE_NAME = "lwm2m_ipe";
	public static final String BASIC_CONTAINER_LBL = "cnt";
	public static final String CONTAINER_NAME_CONTROL = "execute";
	public static final String CONTAINER_NAME_RESULT = "result";
	public static final String CONTAINER_NAME_STATUS = "status";
	
	// oneM2M options
	public static final long DAYS_FOR_EXPIRED = 3650L;
	
	// oneM2M Resource Type
	public static final int MIXED = 0;
	public static final int ACCESS_CTRL_PLCY = 1;
	public static final int AE = 2;
	public static final int CONTAINER = 3;
	public static final int CONTENTINSTANCE = 4;
	public static final int CSE_BASE = 5;
    public static final int GROUP = 9;
    public static final int POLLING_CHANNEL = 15; 
    public static final int SUBSCRIPTION = 23;
    public static final int SEMANTIC_DESCRIPTOR = 24;

    
    // LWM2M Resource(new) 2017.11.03
    public static final String[][] RESOURCE = {
    	{"info","Model_number","/3/0/1"},
    	{"info","Package_uri","/5/0/1"},
    	{"report","Humidity","/1024/10/1"},
    	{"report","Temperature","/1024/10/3"},
    	{"report","Distance","/1024/11/3"},
    	{"control","Reboot","/3/0/4"},
    	{"control","LEDPower","/1024/12/1"},
    	{"control","SoundPower","/1024/12/3"}
    };
    
    
	
    public static String getResourceByUri(String uri){
    	String result = null;
    	for(int i=0; i<RESOURCE.length; i++){
    		if(RESOURCE[i][2].equals(uri)){
    			result = RESOURCE[i][1];
    		}
    	}
    	return result;
    }
    
	// HTTP METHOD
	public enum REQUEST_METHOD_TYPE{
		POST(0, "POST", true),
		GET(1, "GET", false),
		PUT(2, "PUT", false),
		DELETE(3, "DELETE", false);
		
		private int value;
		private String name;
		private boolean doOutput;
		REQUEST_METHOD_TYPE(int value, String name, boolean doOutput) {
		    this.value = value;
		    this.name = name;
		    this.doOutput = doOutput;
		}
		
		public int Value() {
		    return this.value;
		}
		public String Name() {
		    return this.name;
		}
		public boolean DoOutput(){
		    return this.doOutput;
		}
		
		private static final Map<Integer, REQUEST_METHOD_TYPE> map = new HashMap<Integer, REQUEST_METHOD_TYPE>();
		static {
		    for(REQUEST_METHOD_TYPE rmt : REQUEST_METHOD_TYPE.values()) {
		        map.put(rmt.value, rmt);
		    }
		}
		
		public static REQUEST_METHOD_TYPE get(int value) {
		    REQUEST_METHOD_TYPE rmt = map.get(value);
		    return rmt;
		}
	}
	

}
