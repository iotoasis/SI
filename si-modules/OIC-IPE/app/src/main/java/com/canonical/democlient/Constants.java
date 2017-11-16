package com.canonical.democlient;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by moon on 2016-09-22.
 */
public class Constants {
    public static final String HOST_IP = "192.168.123.196";
    public final static String INCSE_ADDR = "http://"+HOST_IP+":8080/herit-in/herit-cse";

    public final static String[] PASS_URL = {"oic/d","oic/p","pollingChannel"};
    public final static String[] POLLING_CHANNEL_URL = {"pch_svc","pch_ipe"};
    public final static String[] COMMAND_CONTAINER_URL = {"led","buzzer"};
    public final static String[] OBSERVE_CONTAINER_URL = {"light","sound","touch","temperature","button"};
    public final static String[] BASE_CONTAINER_URL = {"execute","result"};

    // FoundDeviceInfo
    public static String old_host = "";
    public static FoundDeviceInfo deviceInfo = null;

    public static String UNNAMED = "unnamed";
    public static String ORIGIN_BASE = "S";
    public static final String EXECUTE = "execute";
    public static final String RESULT = "result";

    public static final String BASE_CONTAINER_LBL = "cnt";
    public static final String BASE_AE = "oic_ipe";

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
