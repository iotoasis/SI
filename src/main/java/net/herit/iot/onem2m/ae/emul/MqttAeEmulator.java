package net.herit.iot.onem2m.ae.emul;

import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.message.onem2m.OneM2mRequest.OPERATION;
import net.herit.iot.message.onem2m.OneM2mRequest.RESOURCE_TYPE;
import net.herit.iot.message.onem2m.format.Enums.CONTENT_TYPE;
import net.herit.iot.onem2m.bind.mqtt.api.MqttClientListener;
import net.herit.iot.onem2m.bind.mqtt.client.MqttClientHandler;
import net.herit.iot.onem2m.resource.AE;
import net.herit.iot.onem2m.resource.PrimitiveContent;

public class MqttAeEmulator implements MqttClientListener {

	MqttClientHandler mqttClient;
	
	String brokerURL = "tcp://iot.eclipse.org:1883";
	String AE_ID = "/herit-in/ae_0001";
	String CSE_ID = "/herit-in";

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MqttAeEmulator s = new MqttAeEmulator();
		s.init();
		
		try {
			s.sendRequest();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void init() {
		try {
			mqttClient = MqttClientHandler.getInstance(AE_ID);
			mqttClient.connect(brokerURL);
			mqttClient.setListener(this);
		} catch(Exception e) {
			e.printStackTrace(System.out);
		}
	}

	public void sendRequest() throws Exception {
		
		OneM2mRequest req = new OneM2mRequest();
		req.setRequestIdentifier("req_222222");
		req.setFrom("ae_0001");
		req.setTo("/herit-cse");
		req.setContentType(CONTENT_TYPE.JSON);
		req.setOperation(OPERATION.CREATE);
		req.setResourceType(RESOURCE_TYPE.AE);
		
		AE ae = new AE();
		ae.addLabels("home");
		ae.setExpirationTime("20151231T235555");
		ae.setAppName("TEST");
		ae.setAppID("testApp");
		
		PrimitiveContent pCont = new PrimitiveContent();
		pCont.addAny(ae);
		
		req.setPrimitiveContent(pCont);
		
		mqttClient.sendReqMessage(CSE_ID, req);
	}
	
	
	@Override
	public void receiveMqttMessage(OneM2mRequest request) {
		// TODO Auto-generated method stub
		
		System.out.println(request);

	}
	
	@Override
	public void receiveMqttMessage(OneM2mResponse response) {
		// TODO Auto-generated method stub
		
		System.out.println(response);
		System.out.println(((AE)response.getPrimitiveContent().getAny().get(0)).toString());

	}

	@Override
	public void completedMqttDelivery(int messageID) {
		// TODO Auto-generated method stub
		System.out.println("deliveryComplete : " + messageID);
	}

	@Override
	public boolean sendMqttMessage(OneM2mRequest reqMessage) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sendMqttMessage(OneM2mResponse resMessage) {
		// TODO Auto-generated method stub
		return false;
	}	
}
