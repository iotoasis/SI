package net.herit.iot.onem2m.bind.mqtt;

import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.onem2m.bind.mqtt.api.MqttClientListener;
import net.herit.iot.onem2m.bind.mqtt.client.MqttClientHandler;

public class MqttCseClient implements MqttClientListener {
//	String brokerURL = "tcp://10.211.55.8:1883";
	String brokerURL = "tcp://iot.eclipse.org:1883";
	String CES_ID = "herit-in";

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MqttCseClient s = new MqttCseClient();
		s.init();
	}
	
	public void init() {
		try {
			MqttClientHandler.getInstance(CES_ID).connect(brokerURL);
			MqttClientHandler.getInstance().setListener(this);
		} catch(Exception e) {
			e.printStackTrace(System.out);
		}
	}

	@Override
	public void receiveMqttMessage(OneM2mRequest request) {
		// TODO Auto-generated method stub
		
		System.out.println(request);

		String from = request.getFrom();
		String to = request.getTo();
		
		OneM2mResponse res = new OneM2mResponse();
		res.setRequestIdentifier("1234556");
		try {
			MqttClientHandler.getInstance().sendResMessage(from, res);
		} catch(Exception e)	{
			e.printStackTrace(System.out);
		}
	}
	
	@Override
	public void receiveMqttMessage(OneM2mResponse response) {
		
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
