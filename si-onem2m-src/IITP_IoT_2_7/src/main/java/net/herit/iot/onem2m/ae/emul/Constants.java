package net.herit.iot.onem2m.ae.emul;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.Duration;


public class Constants {
	
	//Common
	public static final String CNT_TEMPERATURE = "cnt-temperature";
	public static final String CNT_PHONEBOOK = "cnt-phonebook";
	public static final String CNT_SWITCH = "cnt-switch";
	public static final String CNT_SWITCH_CMD = "cnt-cmd_switch";
	public static final String CNT_SWITCH_RES = "cnt-res_switch";

	// cns 개발기
	public static final String CSE_ADDR = "http://106.103.234.117";
	//public static final String CSE_ADDR = "http://106.103.234.118";
	public static final String CSE_BASENAME = "cb-1";
	public static final String CSE_ID = "/IN_CSE-BASE-1";

	// herit 개발기
//	public static final String CSE_ADDR = "http://10.101.101.195:8080";
//	public static final String CSE_BASENAME = "herit-cse";
//	public static final String CSE_ID = "/herit-in";

	// 로컬 개발용	
//	public static final String CSE_ADDR = "http://localhost:8080";
//	public static final String CSE_BASENAME = "herit-cse";
//	public static final String CSE_ID = "/herit-in";

	public static final String DEVICE_AUTH_KEID = "B9-i1BBRWbnqZUJ0eAndzw";
	public static final String DEVICE_AUTH_TOKEN = "IFOulS6IeWUUY0L4rKc4iBh2nM2Re3uxrtrY0Z8hwWU";
	public static final String DEVICE_ENTITY_ID = "C_AE-D-49bed3a113-0078";
//	public static final String DEVICE_ENTITY_ID = "C_AE-D-GASLOCK1004";
	public static final String DEVICE_DEVICE_MODEL = "TEST_DEVICE_MODEL";
	public static final String DEVICE_NETWORK_INFO = "20";

	public static final String DEVICE_AE_NAME = "ae-gaslock10003";
	public static final String DEVICE_APP_ID = "gaslock_emul";
	public static final String DEVICE_APP_NAME ="gaslock_emulator";
	
	public static final String DEVICE_IP = "localhost";
	public static final int DEVICE_PORT = 9901;
	
	public static final String AS_AUTH_KEID = "B9-i1BBRWbnqZUJ0eAndzw";
	public static final String AS_AUTH_TOKEN = "IFOulS6IeWUUY0L4rKc4iBh2nM2Re3uxrtrY0Z8hwWU";
	public static final String AS_ENTITY_ID = "C_AE-D-49bed3a113-0078";
	public static final String AS_DEVICE_MODEL = "TEST_AS_MODEL";
	public static final String AS_NETWORK_INFO = "20";

	public static final String AS_AE_NAME = "ae-as010";
	public static final String AS_APP_ID = "as_emul"; 
	public static final String AS_APP_NAME ="as_emulator";
	
	public static final String AS_IP = "localhost";
	public static final int AS_PORT = 9902;

}
