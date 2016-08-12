package net.herit.iot.onem2m.bind.mqtt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.onem2m.bind.mqtt.api.MqttServerListener;
import net.herit.iot.onem2m.bind.mqtt.client.MqttClientHandler;

public class MqttCseClient implements MqttServerListener {
//	String brokerURL = "tcp://10.211.55.8:1883";
	String brokerURL = "tcp://iot.eclipse.org:1883";
	String CES_ID = "herit-in";
	
	private Logger log = LoggerFactory.getLogger(MqttCseClient.class);

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MqttCseClient s = new MqttCseClient();
		s.init();
	}
	
	public void init() {
		try {
			MqttClientHandler.getInstance(CES_ID, 30).connect(brokerURL, false);
			MqttClientHandler.getInstance().setListener(this);
		} catch(Exception e) {
			log.debug("Handled exception", e);
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
			log.debug("Handled exception", e);
		}
	}
	

	@Override
	public void completedMqttDelivery(int messageID) {
		// TODO Auto-generated method stub
		System.out.println("deliveryComplete : " + messageID);
	}


	@Override
	public boolean sendMqttMessage(OneM2mResponse resMessage) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void receiveMqttMessage(OneM2mResponse resMessage) {
		// TODO Auto-generated method stub
		
	}	
}
