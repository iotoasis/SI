package org.eclipse.leshan.server;

import java.util.HashMap;
import java.util.Map;

public class Constants {

	// request header
	public static String CONTENT_TYPE = "application/json";
	public static String ORIGIN_BASE = "S";
	public static String UNNAMED = "unnamed";
	public static boolean USE_AUTH = false;
	
	// oneM2M info
	public static final String BASIC_NAME_SPACE = "m2m";
	public static final String BASIC_AE_NAME = "lwm2m_ipe";
	
	public static final String ORDER_EXECUTE = "write";
	public static final String ORDER_RESULT = "read";
	
	public static final String BASIC_CONTAINER_LBL = "cnt";
	
	
	// oneM2M options
	public static final long DAYS_FOR_EXP = 3650L;
	
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
    
    // LWM2M Resource
    public static final String[][] RESOURCES = {
    		//{"/3/0/4","reboot"},
    		{"/3/0/1","model_number"},
    		{"/5/0/1","package_uri"},
    		{"/1024/10/1","humidity"},
    		{"/1024/10/3","temperature"},
    		//{"/1024/11/1",""},
    		{"/1024/11/3","ultra_sonic"},
    		{"/1024/12/1","led"},
    		{"/1024/12/3","sound"}
    };
	
    public static String getResourceByUri(String uri){
    	String result = null;
    	for(int i=0; i<RESOURCES.length; i++){
    		if(RESOURCES[i][0].equals(uri)){
    			result = RESOURCES[i][1];
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
	
	// threads operation
	public enum THREAD_OPERATION{
		READ(0, "READ"),
		WRITE(1, "WRITE");
		
		private int value;
		private String name;
		THREAD_OPERATION(int value, String name){
			this.value = value;
			this.name = name;
		}
		public int Value() {
		    return this.value;
		}
		public String Name() {
		    return this.name;
		}
	}
	
	// thread option
	public static final int HANDLE_INTERVAL = 1;
}
