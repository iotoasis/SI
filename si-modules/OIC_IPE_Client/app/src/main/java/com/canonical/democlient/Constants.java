package com.canonical.democlient;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by 문선호 on 2016-09-22.
 */
public class Constants {

    //* at.company
    public static final String HOST_IP = "10.10.0.23";
    public static final String CSE_BASE = "herit-cse";
    public static final String INCSE_ADDR = "http://10.10.0.23:8080/herit-in/herit-cse";
    //*/

    /* at.home
    public static final String HOST_IP = "172.30.1.30";
    public static final String CSE_BASE = "herit-cse";
    public static final String INCSE_ADDR = "http://172.30.1.30:8080/herit-in/herit-cse";
    //*/

    public static String UNNAMED = "unnamed";
    public static String ORIGIN_BASE = "S";

    public static final String BASE_CONTAINER_LBL = "cnt_h";
    public static final String EXECUTE = "execute";
    public static final String RESULT = "result";

    public static final String POLLING_CHANNEL_I = "pch_ipe";
    public static final String POLLING_CHANNEL_S = "pch_svc";


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
