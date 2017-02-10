package net.herit.iot.onem2m.ae.emul;

import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.message.onem2m.OneM2mRequest.OPERATION;
import net.herit.iot.message.onem2m.OneM2mRequest.RESOURCE_TYPE;
import net.herit.iot.message.onem2m.format.Enums.CONTENT_TYPE;
import net.herit.iot.onem2m.bind.api.ResponseListener;
import net.herit.iot.onem2m.bind.mqtt.api.MqttClientListener;
import net.herit.iot.onem2m.bind.mqtt.client.MqttClientHandler;
import net.herit.iot.onem2m.resource.AE;
import net.herit.iot.onem2m.resource.PrimitiveContent;

public class MqttAeEmulator implements ResponseListener {

	MqttClientHandler mqttClient;
	
//	String brokerURL = "tcp://iot.eclipse.org:1883";
	String brokerURL = "tcp://10.10.0.22:1883";
	
	String AE_ID = "/herit-in/ae_0010";
	String CSE_ID = "/herit-in/herit-cse";

	public static void main(String[] args) {

		System.out.println("== START ==");

		MqttAeEmulator s = new MqttAeEmulator();
		s.init();
		
		try {
			s.sendRequest();
			
			Thread.sleep(2000);
			
//			s.sendRequest();
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {Thread.sleep(5000);} catch(Exception e) {}
			s.terminate();
		}
	}
	
	public void init() {
		try {
			mqttClient = MqttClientHandler.getInstance(AE_ID, 30);
			mqttClient.connect(brokerURL, false);
			mqttClient.setListener(this);
		} catch(Exception e) {
			e.printStackTrace(System.out);
		}
	}
	
	public void terminate() {
		try {
			mqttClient.terminator();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void sendRequest() throws Exception {
		
		OneM2mRequest req = new OneM2mRequest();
		req.setRequestIdentifier("req_222224");
//		req.setFrom("ae_0002");
		req.setFrom("S");
		req.setTo("/herit-cse");
		req.setContentType(CONTENT_TYPE.JSON);
		req.setOperation(OPERATION.CREATE);
		req.setResourceType(RESOURCE_TYPE.AE);
		
		AE ae = new AE();
		ae.addLabels("home");
		ae.setExpirationTime("20161231T235555");
		ae.setAppName("TEST");
		ae.setAppID("testApp");
		
		PrimitiveContent pCont = new PrimitiveContent();
		//pCont.addAny(ae);
		pCont.addAnyOrAny(ae);
		
		req.setPrimitiveContent(pCont);
		
		int msgId = mqttClient.sendReqMessage(CSE_ID, req);
		System.out.println("mqtt msgId: " + msgId);
	}
	

	@Override
	public void receiveResponse(OneM2mResponse response) {
		// TODO Auto-generated method stub
		
		System.out.println(response);
		System.out.println(((AE)response.getPrimitiveContent().getAnyOrAny().get(0)).toString());

	}

	@Override
	public void onError() {
		// TODO Auto-generated method stub
		
	}

}
