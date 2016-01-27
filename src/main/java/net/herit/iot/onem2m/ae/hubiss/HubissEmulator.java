package net.herit.iot.onem2m.ae.hubiss;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
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
import net.herit.iot.onem2m.ae.lib.LGUAuthController;
import net.herit.iot.onem2m.ae.lib.dto.LGUAuthData;
import net.herit.iot.onem2m.ae.lib.dto.LGUAuthData.Http;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.resource.AE;
import net.herit.iot.onem2m.resource.AccessControlPolicy;
import net.herit.iot.onem2m.resource.Container;
import net.herit.iot.onem2m.resource.ContentInstance;
import net.herit.iot.onem2m.resource.FilterCriteria;
import net.herit.iot.onem2m.resource.Subscription;
import net.herit.iot.onem2m.resource.Subscription.NOTIFICATIONCONTENT_TYPE;

/**
 * AE컨트롤러 클래스를 이용하여 디바이스 애뮬레이터를 구현한 클래스 
 * @version : 1.0
 * @author  : Lee inseuk
 */
public class HubissEmulator {
	
	private final static HttpVersion		httpVersion 		= HttpVersion.HTTP_1_1;
	
	private Logger log = LoggerFactory.getLogger(HubissEmulator.class);

	public static AttributeKey<FullHttpRequest>	ATTR_KEY_REQUEST	= AttributeKey.valueOf("httpRequest");


	
	public HubissEmulator() {
		
	}
	
	public void start() throws Exception {
				
		String aeId = "CAE_HUBISS_ADMIN";
		String aeName = "hubiss_admin";
		String appId = "HUBISS"; 
		String appName = "HUBISS_ADMIN";

		String cseAddr = "http://10.101.101.195:8090";
		//String csebase = "/herit-in/herit-cse";
		//String csebaseName = "herit-cse";
		String csebase = "/mobius-yt";
		String csebaseName = "";
		String cseId = "mobius-yt";

		String ip = "10.101.101.180";
		int port = 9902;
		String poa = "http://10.101.101.180:9901";

		AEControllerEx aeController; 
		HubissEmulatorNotiHandler notiHandler;

		
		try {
			
			// AE 컨트롤러 생성
			aeController = new AEControllerEx(cseAddr, cseId, csebaseName, CONTENT_TYPE.RES_XML, null);
			
			// Notification Handler 생성
			notiHandler = new HubissEmulatorNotiHandler(aeController);
			aeController.doHttpServerStart(ip, port, notiHandler);
		
			// AE 생성
			AE ae = new AE();
			ae.setAppID(appId);
			ae.setAppName(appName);
			ae.addPointOfAccess(poa);
			ae.setRequestReachability(true);
			ae.addLabels("hubiss");
			ae.addLabels("admin");
			ae = aeController.doCreateAE(csebase, aeId, aeName, ae, true);
						
				
		} catch (OneM2MException e) {
			
			RESPONSE_STATUS status = e.getResponseStatusCode();
			log.debug("Status:" + status.toString());			
			
			log.error("OneM2MException occurred!!!");
			log.error(e.toString());
			e.printStackTrace();
			return;
			
		} catch (Exception e) {

			log.error("Exception occurred!!!");
			log.error(e.toString());
			e.printStackTrace();
			return;
			
		}

		try{
			// 디바이스 등록
			String deviceUri = null, valveUri = null, tempUri = null, co2Uri = null, humidUri = null;

		    BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
			while (true) {
				System.out.println("Enter command!");
				System.out.println("'d:[deviceId]': discover & subscribe device.");
				System.out.println("'r': retrieve status (valve, humidity, temperature, co2)");
				System.out.println("'v:[ON/OFF]': control valve");
				System.out.println("'bye': stop program");
				
			    String s = bufferRead.readLine();
			      
			    if (s.equalsIgnoreCase("BYE")) {
			    	break;
			    }
			    System.out.println(s);
			    
			    String param;
			    
			    try {
			    	if (s.startsWith("bye"))
			    		break;
			    	
			    	String cmd = s.substring(0,1);
			    	switch (cmd) {
			    	case "d":
			    		param = s.substring(2);
						FilterCriteria fc = new FilterCriteria();
						fc.setResourceType(RESOURCE_TYPE.AE.Value());
						fc.addLabels(param);
						fc.setResourceType(2);
						List<String> devIds = aeController.doDiscovery(csebase, aeId, fc);
						
						if (devIds.size() == 0) {
							System.out.println("No device discovered. Retry with another deviceId!");
							continue;
						}
						
						int i = 1;
						String deviceList = "[Device IDs]\r\n";
						Iterator<String> it = devIds.iterator();
						while (it.hasNext()) {
							deviceList += Integer.toString(i)+":"+it.next()+"\r\n";
						}
						System.out.println(deviceList);
						System.out.println("Input device number:");
						
					    s = bufferRead.readLine();
					    int selected = Integer.parseInt(s);
					    deviceUri = devIds.get(selected-1);

						valveUri = deviceUri + "/VALVE";
						tempUri = deviceUri + "/TEMPERATURE";
						co2Uri = deviceUri + "/CO2";
						humidUri = deviceUri + "/HUMIDITY";
						
					    AE aeDevice = aeController.doRetrieveAE(deviceUri, aeId);
						Subscription valveExecuteSubs = aeController.doCreateSubscription(valveUri+"/Result", aeId, "sub-"+aeId, poa /*aeId notificationUri*/, NOTIFICATIONCONTENT_TYPE.ALL_ATRRIBUTES.Value(), null, true);
					    	
			    		break;
			    		
			    	case "r":
				    	if (deviceUri == null) {
							System.out.println("No device selected. please do discover first.");
				    		continue;
				    	}
						// 상태 조회
						ContentInstance ci;
						ci = aeController.doGetLatestContent(valveUri, aeId);
						log.debug("valve:"+ci.getContent());
						ci = aeController.doGetLatestContent(tempUri, aeId);
						log.debug("temperature:"+ci.getContent());
						ci = aeController.doGetLatestContent(co2Uri, aeId);
						log.debug("co2:"+ci.getContent());
						ci = aeController.doGetLatestContent(humidUri, aeId);
						log.debug("humidity:"+ci.getContent());
				    	break;
				    	
			    	case "v":
			    		param = s.substring(2);
			    		ContentInstance ciCommand = new ContentInstance();
			    		ciCommand.setContentInfo("text/plain:0");
			    		ciCommand.setContent(param);
						ci = aeController.doControlCommand(valveUri, aeId, ciCommand, 20000 /*timeout*/);
						log.debug("Control result:"+ci.getContent());
				    	break;

				    default:
						System.out.println("Unknown command: "+s);
						
			    	}
			    	continue;
				} catch (OneM2MException e) {

					log.error("OneM2MException occurred!!!");
					log.error(e.toString());
					e.printStackTrace();
					
			    } catch (Exception e) {
			    	
					System.out.println("Unknown command or exception: "+s +"\r\n"+e.getMessage());
					continue;
					
			    }
			}
			
			aeController.doHttpServerStop();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) throws Exception {
		new HubissEmulator().start();		
	}

}
