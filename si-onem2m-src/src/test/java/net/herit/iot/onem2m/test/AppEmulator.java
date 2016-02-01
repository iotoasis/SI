package net.herit.iot.onem2m.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.herit.iot.message.onem2m.format.Enums.CONTENT_TYPE;
import net.herit.iot.onem2m.ae.emul.AppEmulatorHttpListener;
import net.herit.iot.onem2m.ae.emul.AppEmulatorNotiHandler;
import net.herit.iot.onem2m.ae.emul.Constants;
import net.herit.iot.onem2m.ae.lib.AEController;
import net.herit.iot.onem2m.ae.lib.LGUAuthController;
import net.herit.iot.onem2m.ae.lib.dto.LGUAuthData;
import net.herit.iot.onem2m.ae.lib.dto.LGUAuthData.Http;
import net.herit.iot.onem2m.core.util.LogManager;
import net.herit.iot.onem2m.resource.AE;
import net.herit.iot.onem2m.resource.AccessControlPolicy;
import net.herit.iot.onem2m.resource.Subscription.NOTIFICATIONCONTENT_TYPE;

public class AppEmulator {
	
	private LogManager 		logManager = LogManager.getInstacne();	
	private Logger log = LoggerFactory.getLogger(AppEmulator.class);

	//
	// AE 정보 - 미리 가지고 있어야 함.
	//
	private String aeId = Constants.AS_ENTITY_ID;
	private String aeName = Constants.AS_AE_NAME;
	private String appId = Constants.AS_APP_ID; 
	private String appName = Constants.AS_APP_NAME;

	//
	// AE가 접속할 CSE정보 - 미리 가지고 있어야 함.
	//
	private String cseAddr = Constants.CSE_ADDR;
	private String csebase = Constants.CSE_ID +"/"+ Constants.CSE_BASENAME;
	private String csebaseName = Constants.CSE_BASENAME;
	private String cseId = Constants.CSE_ID;
	
	// AE IP 주소: Notification 수신에 사용됨
	private String ip;
	// AE 포트 번호: Notification 수신에 사용됨
	private int port;
	
	// CSE가 AE에 Notification 요청을 전송할때 사용하는 네트워크 주소, 예:http://10.101.101.111:8080
	// - 서버에 등록되는 AE의 속성 정보임
	private String poa = "http://116.124.171.3:9902";

	// AE가 구독요청(subscription)을 걸때 Notification 지정하는 Notification 수신 주소
	// - AE의 poa와는 별도로 구독별로 지정할 수 있음
	// # LGU+는 구독별 notificationUri설정을 허용하지 않으며, notificationUri대신에 AE-ID를 셋팅하여 POA로만 Notification을 수신하도록 함 
	private String notificationUri = "http://10.101.101.180:9902";
	
	
	// AE로서 CSE를 연동하는 기능을 제공하는 오브젝트 
	private AEController aeController;
	
	// LGU+ 인증정보 연동 기능을 제공하는 오브젝트
	private LGUAuthController authController;
	
	// Notification 메시지를 처리하는 핸들러 클래스
	private AppEmulatorNotiHandler notiHandler;
	
	// HTTP 서버 수신 메시지를 처리하는 핸들러 클래스
	private AppEmulatorHttpListener httpListener;
	
	// AE 정보 오브젝트
	private AE ae;
	
	// LGU+ 인증연동을 위한 파라미터 정보 - 미리 제공받아야 함
	private final String mefAddr = "http://106.103.234.198/mef";
	private final String deviceModel = "kidswatch1";
	private final String serviceCode = "0078";
	private final String m2mmType = "20";
	private final String deviceSn = "00000000000000000301"; 
	private final String mac = "";
	private final String ctn = "0109999999";
	private final String deviceType = "adn";
	private final String iccId = "A244A1";

	
	public AppEmulator(String ip, int port) {
		try {
			this.poa = "http://"+ip+":"+port;
			this.notificationUri = "http://"+ip+":"+port+"/notify";
			
			this.ip = ip;
			this.port = port;
			
			logManager.initialize(LoggerFactory.getLogger("IITP-IOT-APP-AE"), null);
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
	}
	
	private String getCiNotificationUri() {
		
		return notificationUri;
		
	}
	
	public void start() throws Exception {
		
		this.authController = new LGUAuthController(mefAddr, deviceModel, serviceCode, m2mmType, deviceSn, mac, ctn, deviceType, iccId);

		// 임시 테스트용 코드
		// - 인증결과 정보를 미리 입력하면 authController가 인증서버 연동을 수행하지 않고 제공받은 인증정보를 사용함.
		// - 인증서버 연동기능이 테스트되지 않았으므로 임시로 제공받은 인증정보를 활용해서 서버 연동 시험 진행해야함
		LGUAuthData authData = new LGUAuthData();
		Http httpAuth = new Http();
		httpAuth.setEnrmtKey("test");
		httpAuth.setEntityId(Constants.AS_ENTITY_ID);
		httpAuth.setToken(Constants.AS_AUTH_TOKEN);
		authData.setHttp(httpAuth);
		
		authData.setKeId(Constants.AS_AUTH_KEID);
		authData.setDeviceModel(Constants.AS_DEVICE_MODEL);
		authData.setNetworkInfo(Constants.AS_NETWORK_INFO);
		this.authController.setPreSharedAuthData(authData);
		
		// 컨트롤러 및 핸들러 객체 생성
		this.aeController = new AEController(cseAddr, cseId, csebaseName, CONTENT_TYPE.RES_XML, authController);
		notiHandler = new AppEmulatorNotiHandler(aeController);
		httpListener = new AppEmulatorHttpListener(ip, port, aeController, notiHandler);

		// Notification 수신을 위한 HTTP 서버 시작
		//
		// # 애뮬레이터에 사용된  서버는 애뮬레이터 테스트용으로 제작되었으며 상용서버에서는 별도의 WAS 또는 HTTP 어뎁터를 이용하여 HTTP 서버 기능을 구현해야 함!!!!
		httpListener.start();

		// AE 등록 수행
		// - 이미 등록된 경우는 등록된 정보를 조회함
		this.ae = aeController.doCreateAE(this.csebase, this.aeId, this.aeName, this.appId, this.appName, this.poa, true);

		// ACP 생성
		List<String> oriIds = new ArrayList<String>();
		oriIds.add(ae.getResourceID());			
		AccessControlPolicy acp = aeController.doCreateACP(ae.getUri(), this.aeId, "acp-"+ae.getResourceID(), oriIds, 63, oriIds, 63, true);
		
		List<String> acpIds = new ArrayList<String>();
		acpIds.add(acp.getResourceID());
				
		while (true) {

			System.out.println("Enter command!");
			System.out.println("'r:[deviceName]' - device registration");
			System.out.println("'s:[deviceName]' [switch status('ON'/'OFF')] - switch control");
			System.out.println("'u:[PointOfAccess]' - update PointOfAccess");
			System.out.println("'bye': Exit");
			
		    BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
		    String s = bufferRead.readLine();
		    
			try{
			      
			    if (s.equalsIgnoreCase("BYE")) {
			    	break;
			    }
			    
			    
			    System.out.println(s);
			    
			    if (s.startsWith("r:")) {
			    	//
			    	// 디바이스에 대한 구독(Subscription) 신청
			    	// - 처음 디바이스가 등록되었을 때 해당 디바이스의 상태를 수신하기 위해서 1회 구독 신청하여야 함
			    	// - 일반적으로 사용자의 디바이스 등록 또는 프로비저닝 단계에서 실행됨
			    	//
			    	String deviceName;
			    	deviceName = s.substring(2);
			    
			    	String aeUri = this.csebase+"/"+deviceName;
					//AE ae = aeController.doRetrieveAE(aeUri);

//						aeController.doContainerSubscription(aeUri + "/"+Constants.CNT_SWITCH, this.aeId, this.aeId, NOTIFICATIONCONTENT_TYPE.ALL_ATRRIBUTES.Value());
//						aeController.doContainerSubscription(aeUri + "/"+Constants.CNT_PHONEBOOK, this.aeId, this.aeId, NOTIFICATIONCONTENT_TYPE.RESOURCE_ID.Value());
//						aeController.doContainerSubscription(aeUri + "/"+Constants.CNT_TEMPERATURE, this.aeId, this.aeId, NOTIFICATIONCONTENT_TYPE.ALL_ATRRIBUTES.Value());
//						aeController.doContainerSubscription(aeUri + "/"+Constants.CNT_SWITCH_RES, this.aeId, this.aeId, NOTIFICATIONCONTENT_TYPE.ALL_ATRRIBUTES.Value());

					aeController.doCreateSubscription(aeUri + "/"+Constants.CNT_SWITCH, this.aeId, "/sub-"+this.ae.getResourceName().replace("-", "_"), this.getCiNotificationUri(), NOTIFICATIONCONTENT_TYPE.ALL_ATRRIBUTES.Value(), acpIds, true);
					aeController.doCreateSubscription(aeUri + "/"+Constants.CNT_PHONEBOOK, this.aeId, "/sub-"+this.ae.getResourceName().replace("-", "_"), this.getCiNotificationUri(), NOTIFICATIONCONTENT_TYPE.RESOURCE_ID.Value(), acpIds, true);
					aeController.doCreateSubscription(aeUri + "/"+Constants.CNT_TEMPERATURE, this.aeId, "/sub-"+this.ae.getResourceName().replace("-", "_"), this.getCiNotificationUri(), NOTIFICATIONCONTENT_TYPE.ALL_ATRRIBUTES.Value(), acpIds, true);
					aeController.doCreateSubscription(aeUri + "/"+Constants.CNT_SWITCH_RES, this.aeId, "/sub-"+this.ae.getResourceName().replace("-", "_"), this.getCiNotificationUri(), NOTIFICATIONCONTENT_TYPE.ALL_ATRRIBUTES.Value(), acpIds, true);

			    } else if (s.startsWith("s:")) {
			    	//
			    	// 스위치 제어 명령을 전송하는 예제
			    	// - 스위치 제어 명령 전송을 위해서 정의된 CNT_SWITCH_CMD 컨테이너에 contentInstance를 생성하여 제어명령을 전송함
			    	// - 디바이스는 제어명령 수신을 위해서 CNT_SWITCH_CMD 컨테이너에 대해서 구독하고 있어야 함
			    	// - 제어결과는 별도의 컨테이너(CNT_SWITCH_RES)를 통해서 수신하므로 AS는 제어명령을 보내기 전에 결과 수신 컨테이너를  구독하고 있어야 함
			    	// - 제어전송 및 결과 수신은 별도의 메시지 요청/전송을 통해서 일어나므로 해당 메시지에 대한 매핑은 AS에서 구현하여야 함
			    	//
			    	String temp = s.substring(2);
			    	String[] tokens = temp.split(" ");
			    	if (tokens.length != 2) {
						System.out.println("Invalid command: "+s);
						continue;
			    	}
			    	
			    	aeController.doCreateContentInstance(this.csebase+"/"+ tokens[0] +"/"+Constants.CNT_SWITCH_CMD, Constants.CNT_SWITCH_CMD, this.aeId, tokens[1]);
				    	
			    } else if (s.startsWith("u:")) {
			    	//
			    	// AE poa 정보 업데이트 예
			    	// - 입력된 poa정보를 IN-CSE에 전송함
			    	//
			    	String pb = s.substring(2);
			    	aeController.doUpdateAEPoa(this.ae.getUri(), this.aeId, pb);
			    } else {
					System.out.println("Unknown command: "+s);
					continue;						
				}
			}
			catch(IOException e)
			{
				System.out.println("Invalid command: "+s);
				e.printStackTrace();
				continue;
			}
		}

		httpListener.stop();
	}
	private String getNotificationUri() {
		return "http://"+Constants.AS_IP+":"+Constants.AS_PORT+"/notify";
	}

	// 에뮬레이트 실행 함수
	public static void main(String[] args) throws Exception {
		new AppEmulator(Constants.AS_IP, Constants.AS_PORT).start();		
	}

}
