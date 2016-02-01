package net.herit.iot.onem2m.ae.emul.server;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.AttributeKey;
import net.herit.iot.message.onem2m.OneM2mResponse.RESPONSE_STATUS;
import net.herit.iot.message.onem2m.format.Enums.CONTENT_TYPE;
import net.herit.iot.onem2m.ae.emul.Constants;
import net.herit.iot.onem2m.ae.emul.DeviceEmulator;
import net.herit.iot.onem2m.ae.emul.KidswatchEmulatorHttpListener;
import net.herit.iot.onem2m.ae.emul.KidswatchEmulatorNotiHandler;
import net.herit.iot.onem2m.ae.lib.AEController;
import net.herit.iot.onem2m.ae.lib.LGUAuthController;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.resource.AE;
import net.herit.iot.onem2m.resource.AccessControlPolicy;
import net.herit.iot.onem2m.resource.Container;
import net.herit.iot.onem2m.resource.Subscription;
import net.herit.iot.onem2m.resource.Subscription.NOTIFICATIONCONTENT_TYPE;

public class KidswatchEmulator {
	
	private final static HttpVersion		httpVersion 		= HttpVersion.HTTP_1_1;
	
	private Logger log = LoggerFactory.getLogger(DeviceEmulator.class);

	public static AttributeKey<FullHttpRequest>	ATTR_KEY_REQUEST	= AttributeKey.valueOf("httpRequest");

	public static String DEVICE_IP = "10.101.101.104";
	public static int DEVICE_PORT = 9901;
	
	// LGU Auth input
	private String mefAddr = "http://106.103.234.198/mef";
	private String deviceModel = "ZTF31";
	private String serviceCode = "0079";
	private String m2mmType = "LTE-DEVICE";
	private String deviceSn = "0000000001"; 
	private String mac = "";
	private String ctn = "01011119991";
	private String deviceType = "adn";
	private String iccId = "123456";

	private String aeId;
	private String aeName = "ae-kidswatch_01011119991";
	private String appId = "kidswatch"; 
	private String appName = "kidswatch";
	
	public static final String CNT_KIDSWATCH_LOCATION_REQ = "cnt-location_req";
	public static final String CNT_KIDSWATCH_RINGERMODE_REQ = "cnt-ringtone_req";

	private String cseAddr = Constants.CSE_ADDR;
	private String csebase = Constants.CSE_ID +"/"+ Constants.CSE_BASENAME;
	private String csebaseName = Constants.CSE_BASENAME;
	private String cseId = Constants.CSE_ID;
	private String commonAcpId = Constants.CSE_ID +"/"+ Constants.CSE_BASENAME +"/acp-"+Constants.AS_AE_NAME+"-common";

	private String poa = "";

	// AE가 구독요청(subscription)을 걸때 Notification 지정하는 Notification 수신 주소
	// - AE의 poa와는 별도로 구독별로 지정할 수 있음
	// # LGU+는 구독별 notificationUri설정을 허용하지 않으며, notificationUri대신에 AE-ID를 셋팅하여 POA로만 Notification을 수신하도록 함 
	private String notificationUri = "http://10.101.101.180:9901";
	
	private AEController aeController; 
	private LGUAuthController authController;
	private KidswatchEmulatorNotiHandler notiHandler;
	private KidswatchEmulatorHttpListener httpListener;
	
	private AccessControlPolicy acp;
	private AE ae;
	private Container locationCnt;
	private Container ringtoneCnt;
	private Subscription locationCntSubs;
	private Subscription ringtoneCntSubs;


	
	private String ip;
	private int port;
	
	public KidswatchEmulator(String ip, int port) {
		try {
			this.poa = "http://"+ip+":"+port;
			
			this.ip = ip;
			this.port = port;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private String getCiNotificationUri() {
		
		return notificationUri;
		
	}
	
	public void start() throws Exception {
		
		try {
			// 인증 컨트롤러 생성
			this.authController = new LGUAuthController(mefAddr, deviceModel, serviceCode, m2mmType, deviceSn, mac, ctn, deviceType, iccId);
			
			//인증
			//인증후 Http, Coap, Mqtt 의 token, key, entityId 값과 계산된 keId값은 authController의 authData에 저장된다.
			//인증정보 가져오기 authController.getAuthData()
			authController.doAuth();
			aeId = authController.getAuthData().getHttp().getEntityId();
			
			// AE 컨트롤러 생성
			this.aeController = new AEController(cseAddr, cseId, csebaseName, CONTENT_TYPE.RES_XML, this.authController);

			System.out.println("poa:" + poa);
			// AE 생성
			this.ae = aeController.doCreateAE(this.csebase, this.aeId, this.aeName, this.appId, this.appName, this.poa, true);
			
			// AccessControlPolicy 생성 - AE가 AE하위에 생성한 리소스에 엑세스 할 수 있는 권한정책리소스를 생성한다.
			List<String> oriIds = new ArrayList<String>();
			oriIds.add(ae.getResourceID());			
			this.acp = aeController.doCreateACP(ae.getUri(), this.aeId, "acp-"+ae.getResourceID().replace("-", "_"), oriIds, 63, oriIds, 63, true);				
		
			// AE 하위에 생성될 리소스에 설정될 권한정책리소스 ID목록을 생성한다.
			// - ID목록에는 AE 자신이 하위 리소스에 CRUD 오퍼레이션을 수행할 수 있는 ACP id와
			// - 공통 엔티티(애플리케이션서버 AE)가 CRUD 오퍼레이션을 수행할 수 있는 ACP id (공통권한정책은 미리 생성되어 있고, 해당 ID가 AE에 셋팅되어 있다고 전제함)
			List<String> acpIds = new ArrayList<String>();
			acpIds.add(acp.getResourceID());
			acpIds.add(this.commonAcpId);				// 공통권한정책ID를 추가한다.

			// 컨테이너 생성
			locationCnt = aeController.doCreateContainer(ae.getUri(), ae.getAEID(), CNT_KIDSWATCH_LOCATION_REQ, acpIds, true);
			ringtoneCnt = aeController.doCreateContainer(ae.getUri(), ae.getAEID(), CNT_KIDSWATCH_RINGERMODE_REQ, acpIds, true);
			
			// 컨테이너에 대한 구독 요청
			locationCntSubs = aeController.doCreateSubscription(locationCnt.getUri(), this.aeId, 
					"sub-"+this.ae.getResourceName().replace("-", "_"), this.ae.getResourceID(), NOTIFICATIONCONTENT_TYPE.RESOURCE_ID.Value(), acpIds, true);
			ringtoneCntSubs = aeController.doCreateSubscription(ringtoneCnt.getUri(), this.aeId, 
					"sub-"+this.ae.getResourceName().replace("-", "_"), this.ae.getResourceID(), NOTIFICATIONCONTENT_TYPE.RESOURCE_ID.Value(), acpIds, true);
			
			//aeController.doCreateContentInstance(this.temperatureCnt, this.ae.getResourceID(), "13", null);
			//aeController.doCreateContentInstance(this.switchCnt, this.ae.getResourceID(), "Off", null);

						
			// Notification Handler 생성
			this.notiHandler = new KidswatchEmulatorNotiHandler(this.aeController);
			
			// HTTP 서버 모듈 생성
			this.httpListener = new KidswatchEmulatorHttpListener(ip, port, aeController, notiHandler);
			httpListener.start();

				
		} catch (OneM2MException e) {
			log.error("OneM2MException occurred!!!");
			RESPONSE_STATUS status = e.getResponseStatusCode();
			log.error("Error code: " + status.Value());
			log.error("Error message: " + status.Name());			
		} catch (Exception e) {
			log.error("Exception occurred!!!");
			log.error(e.toString());
			e.printStackTrace();
		}
		
	}
	
	private String getNotificationUri() {
		return "http://"+DEVICE_IP+":"+DEVICE_PORT+"/notify";
	}

	public static void main(String[] args) {
		try {
			new KidswatchEmulator(DEVICE_IP, DEVICE_PORT).start();
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

}
