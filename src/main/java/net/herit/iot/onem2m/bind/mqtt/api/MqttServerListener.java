package net.herit.iot.onem2m.bind.mqtt.api;

import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mResponse;

public interface MqttServerListener {
	public void receiveMqttMessage(OneM2mRequest reqMessage);
	public void receiveMqttMessage(OneM2mResponse resMessage);
	public boolean sendMqttMessage(OneM2mResponse resMessage);
	
	public void completedMqttDelivery(int messageID);
}
