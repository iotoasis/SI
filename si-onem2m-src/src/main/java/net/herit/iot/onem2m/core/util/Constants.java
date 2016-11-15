package 

import java.util.HashMap;
import java.util.Map;

public class Constants {

    //* at.company
    public final static String HOST_IP = "192.168.123.131";
    //public final static String INCSE_ADDR = "http://10.10.224.240:8080/nimbus-cse";
    public final static String INCSE_ADDR = "http://192.168.123.131:8080/herit-in/herit-cse";
    //*/

    /* at.home
    public final static String HOST_IP = "172.30.1.48";
    public final static String INCSE_ADDR = "http://172.30.1.30:8080/herit-in/herit-cse";
    //*/

    public final static String[] PASS_URL = {"oic/d","oic/p","pollingChannel"};
    public final static String[] POLLING_CHANNEL_URL = {"pch_svc","pch_ipe"};
    public final static String[] COMMAND_CONTAINER_URL = {"led","buzzer"};
    public final static String[] OBSERVE_CONTAINER_URL = {"light","sound","touch","temperature","button"};
    public final static String[] BASE_CONTAINER_URL = {"execute","result"};

    // FoundDeviceInfo
    public static String old_host = "";
    public static FoundDeviceInfo deviceInfo = null;

    //
    public static String UNNAMED = "unnamed";
    public static String ORIGIN_BASE = "S";

    public static final String BASE_CONTAINER_LBL = "cnt";
    public static final String BASE_AE = "Arduino";

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
