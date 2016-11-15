package net.herit.iot.onem2m.core.util;

import java.util.HashMap;
import java.util.Map;

// global variable to static
// not using yet
public class Constants {

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

        public int getValue() {
            return this.value;
        }
        public String getName() {
            return this.name;
        }
        public boolean getDoOutput(){
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
