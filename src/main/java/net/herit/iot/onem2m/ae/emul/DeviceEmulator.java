package net.herit.iot.onem2m.ae.emul;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.AttributeKey;
import net.herit.iot.message.onem2m.OneM2mRequest.RESOURCE_TYPE;
import net.herit.iot.message.onem2m.OneM2mResponse.RESPONSE_STATUS;
import net.herit.iot.message.onem2m.format.Enums.CONTENT_TYPE;
import net.herit.iot.onem2m.ae.lib.AEController;
import net.herit.iot.onem2m.ae.lib.HttpBasicRequest;
import net.herit.iot.onem2m.ae.lib.HttpBasicResponse;
import net.herit.iot.onem2m.ae.lib.LGUAuthController;
import net.herit.iot.onem2m.ae.lib.dto.LGUAuthData;
import net.herit.iot.onem2m.ae.lib.dto.LGUAuthData.Http;
import net.herit.iot.onem2m.core.convertor.ConvertorFactory;
import net.herit.iot.onem2m.core.convertor.XMLConvertor;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.resource.AE;
import net.herit.iot.onem2m.resource.AccessControlPolicy;
import net.herit.iot.onem2m.resource.Container;
import net.herit.iot.onem2m.resource.ContentInstance;
import net.herit.iot.onem2m.resource.Notification;
import net.herit.iot.onem2m.resource.Subscription;
import net.herit.iot.onem2m.resource.Subscription.NOTIFICATIONCONTENT_TYPE;

/**
 * AE컨트롤러 클래스를 이용하여 디바이스 애뮬레이터를 구현한 클래스 
 * @version : 1.0
 * @author  : Lee inseuk
 */
public class DeviceEmulator {
	
	private final static HttpVersion		httpVersion 		= HttpVersion.HTTP_1_1;
	
	private Logger log = LoggerFactory.getLogger(DeviceEmulator.class);

	public static AttributeKey<FullHttpRequest>	ATTR_KEY_REQUEST	= AttributeKey.valueOf("httpRequest");


	//private String aeId = Constants.DEVICE_ENTITY_ID;
	private String aeId;
	private String aeName = Constants.DEVICE_AE_NAME;
	private String appId = Constants.DEVICE_APP_ID; 
	private String appName = Constants.DEVICE_APP_NAME;

	//private String cseAddr = "http://localhost:8080";
	//private String csebase = "/csebase";
	private String cseAddr = Constants.CSE_ADDR;
	private String csebase = Constants.CSE_ID +"/"+ Constants.CSE_BASENAME;
	private String csebaseName = Constants.CSE_BASENAME;
	private String cseId = Constants.CSE_ID;
	private String commonAcpId = Constants.CSE_ID +"/"+ Constants.CSE_BASENAME +"/acp-"+Constants.AS_AE_NAME+"-common";

	private String poa = "http://116.124.171.3:9901";

	// AE가 구독요청(subscription)을 걸때 Notification 지정하는 Notification 수신 주소
	// - AE의 poa와는 별도로 구독별로 지정할 수 있음
	// # LGU+는 구독별 notificationUri설정을 허용하지 않으며, notificationUri대신에 AE-ID를 셋팅하여 POA로만 Notification을 수신하도록 함 
	private String notificationUri = "http://10.101.101.180:9901";
	
	private AEController aeController; 
	private LGUAuthController authController;
	private DeviceEmulatorNotiHandler notiHandler;
	private DeviceEmulatorHttpListener httpListener;
	
	private AccessControlPolicy acp;
	private AE ae;
	private Container switchCnt;
	private Container switchCmdCnt;
	private Container switchResCnt;
	private Container temperatureCnt;
	private Container phoneBookCnt;
	private Subscription switchCmdCntSubs;
	private Subscription phoneBookCntSubs;

	// LGU Auth input
	private String mefAddr = "http://106.103.234.198/mef";
	private String deviceModel = "kidswatch1";
	private String serviceCode = "0079";
	private String m2mmType = "LTE-DEVICE";
	private String deviceSn = "00000000000000000302"; 
	private String mac = "";
	private String ctn = "01012345675";
	private String deviceType = "adn";
	private String iccId = "B244B1";
	
	private String ip;
	private int port;
	
	public DeviceEmulator(String ip, int port) {
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
	
	public void start2() throws Exception {

		try {

//			log.debug("# start init ContentInstance xml convertor");
//			XMLConvertor<?> xmlCvt4 = ConvertorFactory.getXMLConvertor(ContentInstance.class, null);
//			log.debug("# start init Container xml convertor");
//			XMLConvertor<?> xmlCvt2 = ConvertorFactory.getXMLConvertor(Container.class, null);
//			log.debug("# start init Notification xml convertor");
//			XMLConvertor<?> xmlCvt = ConvertorFactory.getXMLConvertor(Notification.class, null);
//			log.debug("# start init AE xml convertor");
//			XMLConvertor<?> xmlCvt3 = ConvertorFactory.getXMLConvertor(AE.class, null);
//			log.debug("# end init xml convertor");

			// AE 컨트롤러 생성
			this.aeController = new AEController(cseAddr, cseId, csebaseName, CONTENT_TYPE.RES_XML, this.authController, 5);
			// Notification Handler 생성
			this.notiHandler = new DeviceEmulatorNotiHandler(this.aeController);

			this.aeController.doInitializeConvertor(CONTENT_TYPE.XML, RESOURCE_TYPE.NOTIFICATION);
			this.aeController.doInitializeConvertor(CONTENT_TYPE.XML, RESOURCE_TYPE.AE);
			this.aeController.doInitializeConvertor(CONTENT_TYPE.XML, RESOURCE_TYPE.CONTAINER);
			this.aeController.doInitializeConvertor(CONTENT_TYPE.XML, RESOURCE_TYPE.CONTENT_INST);
			this.aeController.doInitializeConvertor(CONTENT_TYPE.XML, RESOURCE_TYPE.SUBSCRIPTION);
			
			// 
			try {
				HttpBasicRequest br = new HttpBasicRequest("POST", "/", "/", "<?xml version=\"1.0\" encoding=\"UTF-8\"?><m2m:sgn xmlns:m2m=\"http://www.onem2m.org/xml/protocols\"><nev><rep>test</rep></nev></m2m:sgn>".getBytes());
				br.addHeader("X-M2M-Origin", "local");
				HttpBasicResponse res = this.aeController.doProcessRequest(br, this.notiHandler);

				System.out.println("# Notification test - response:"+res.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			
			// HTTP 서버 모듈 생성 - 예제에 사용된 HTTP 서버 기능은 예시이며 모듈별로 별도로 구현되거나 타 서버 모듈을 활용해야 함 
			this.httpListener = new DeviceEmulatorHttpListener(ip, port, aeController, notiHandler);
			httpListener.start();
		
			
			//======================================================================================
			boolean switchVal = true;

			while (true) {
				System.out.println("Enter command!");
				System.out.println("'s': toggle switch");
				System.out.println("'t:[temperature]': change temperature");
				System.out.println("'p:[phonebookcontent]': change phonebook");
				System.out.println("'u:[PointOfAccess]': update PointOfAccess");
				
			    BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
			    String s = bufferRead.readLine();
			      
			    if (s.equalsIgnoreCase("BYE")) {
			    	break;
			    }
			    System.out.println(s);
			    
			    String temp;
			    

		    	if (s.startsWith("bye"))
		    		break;
		    	
		    	String cmd = s.substring(0,1);
		    	switch (cmd) {
		    	case "s":
		    		Container cnt = new Container();
		    		cnt.setResourceName("");
		    		cnt.setUri("");
			    	aeController.doCreateContentInstance(this.switchCnt, this.aeId, switchVal ? "ON" : "OFF", null);
			    	switchVal = !switchVal;
		    		break;
		    	case "t":
			    	temp = s.substring(2);
			    	int iTemp = Integer.parseInt(temp);
			    	aeController.doCreateContentInstance(this.temperatureCnt, this.aeId, temp, null);
			    	break;
		    	case "p":
		    		temp = s.substring(2);
			    	aeController.doCreateContentInstance(this.phoneBookCnt, this.aeId, temp, null);
			    	break;
		    	case "u":
					temp = s.substring(2);
					aeController.doUpdateAEPoa(this.ae.getUri(), this.aeId, temp);
			    	break;
			    default:
					System.out.println("Unknown command: "+s);
		    	}
		    	continue;
			}
				
			httpListener.stop();
				
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
	
	public void start() throws Exception {
		
		
		try {

			// AE 컨트롤러 생성
			this.aeController = new AEController(cseAddr, cseId, csebaseName, CONTENT_TYPE.RES_XML, null);
			HttpBasicRequest br = new HttpBasicRequest(aeId, aeId, aeId, null);
			
			this.aeController.doProcessRequest(br, null);
			
			// 인증 컨트롤러 생성
			this.authController = new LGUAuthController(mefAddr, deviceModel, serviceCode, m2mmType, deviceSn, mac, ctn, deviceType, iccId);
			
			
			//인증
			//인증후 Http, Coap, Mqtt 의 token, key, entityId 값과 계산된 keId값은 authController의 authData에 저장된다.
			//인증정보 가져오기 authController.getAuthData()
			authController.doAuth();
			aeId = authController.getAuthData().getHttp().getEntityId();
			
			// AE 컨트롤러 생성
			this.aeController = new AEController(cseAddr, cseId, csebaseName, CONTENT_TYPE.RES_XML, this.authController);
			
			// Notification Handler 생성
			this.notiHandler = new DeviceEmulatorNotiHandler(this.aeController);
			
			// HTTP 서버 모듈 생성 - 예제에 사용된 HTTP 서버 기능은 예시이며 모듈별로 별도로 구현되거나 타 서버 모듈을 활용해야 함 
			this.httpListener = new DeviceEmulatorHttpListener(ip, port, aeController, notiHandler);
			httpListener.start();
		
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
			Container cntrTemp = new Container();
			cntrTemp.setMaxNrOfInstances(1);
			cntrTemp.addLabels("add_label_if_necessary");
			this.temperatureCnt = aeController.doCreateContainer(ae.getUri(), ae.getAEID(), Constants.CNT_TEMPERATURE, acpIds, cntrTemp, true);
			this.phoneBookCnt = aeController.doCreateContainer(ae.getUri(), ae.getAEID(), Constants.CNT_PHONEBOOK, acpIds, true);
			this.switchCnt = aeController.doCreateContainer(ae.getUri(), ae.getAEID(), Constants.CNT_SWITCH, acpIds, true);
			this.switchCmdCnt = aeController.doCreateContainer(ae.getUri(), ae.getAEID(), Constants.CNT_SWITCH_CMD, acpIds, true);
			this.switchResCnt = aeController.doCreateContainer(ae.getUri(), ae.getAEID(), Constants.CNT_SWITCH_RES, acpIds, true);
			
			// 컨테이너에 대한 구독 요청
			//aeController.doContainerSubscription(switchCmdCnt.getUri(), this.aeId, getNotificationUri(), NOTIFICATIONCONTENT_TYPE.ALL_ATRRIBUTES.Value());
			//aeController.doContainerSubscription(phoneBookCnt.getUri(), this.aeId, getNotificationUri(), NOTIFICATIONCONTENT_TYPE.RESOURCE_ID.Value());
			this.switchCmdCntSubs = aeController.doCreateSubscription(switchCmdCnt.getUri(), this.aeId, "sub-"+this.ae.getResourceName().replace("-", "_"), this.ae.getResourceID(), NOTIFICATIONCONTENT_TYPE.ALL_ATRRIBUTES.Value(), acpIds, true);
			this.phoneBookCntSubs = aeController.doCreateSubscription(phoneBookCnt.getUri(), this.aeId, "sub-"+this.ae.getResourceName().replace("-", "_"), this.ae.getResourceID(), NOTIFICATIONCONTENT_TYPE.RESOURCE_ID.Value(), acpIds, true);
			
			this.notiHandler.setResource(this.ae, this.switchCmdCnt, this.switchCnt);
			
			aeController.doCreateContentInstance(this.temperatureCnt, this.ae.getResourceID(), "13", null);
			aeController.doCreateContentInstance(this.switchCnt, this.ae.getResourceID(), "Off", null);

			
			//======================================================================================
			boolean switchVal = true;

			while (true) {
				System.out.println("Enter command!");
				System.out.println("'s': toggle switch");
				System.out.println("'t:[temperature]': change temperature");
				System.out.println("'p:[phonebookcontent]': change phonebook");
				System.out.println("'u:[PointOfAccess]': update PointOfAccess");
				
			    BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
			    String s = bufferRead.readLine();
			      
			    if (s.equalsIgnoreCase("BYE")) {
			    	break;
			    }
			    System.out.println(s);
			    
			    String temp;
			    

		    	if (s.startsWith("bye"))
		    		break;
		    	
		    	String cmd = s.substring(0,1);
		    	switch (cmd) {
		    	case "s":
		    		Container cnt = new Container();
		    		cnt.setResourceName("");
		    		cnt.setUri("");
			    	aeController.doCreateContentInstance(this.switchCnt, this.aeId, switchVal ? "ON" : "OFF", null);
			    	switchVal = !switchVal;
		    		break;
		    	case "t":
			    	temp = s.substring(2);
			    	int iTemp = Integer.parseInt(temp);
			    	aeController.doCreateContentInstance(this.temperatureCnt, this.aeId, temp, null);
			    	break;
		    	case "p":
		    		temp = s.substring(2);
			    	aeController.doCreateContentInstance(this.phoneBookCnt, this.aeId, temp, null);
			    	break;
		    	case "u":
					temp = s.substring(2);
					aeController.doUpdateAEPoa(this.ae.getUri(), this.aeId, temp);
			    	break;
			    default:
					System.out.println("Unknown command: "+s);
		    	}
		    	continue;
			}
				
			httpListener.stop();
				
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
		return "http://"+Constants.DEVICE_IP+":"+Constants.DEVICE_PORT+"/notify";
	}
	
	public static void main(String[] args) throws Exception {
		new DeviceEmulator(Constants.DEVICE_IP, Constants.DEVICE_PORT).start2();		
	}

}
