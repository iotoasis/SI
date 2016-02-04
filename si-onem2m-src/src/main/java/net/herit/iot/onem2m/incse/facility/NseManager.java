package net.herit.iot.onem2m.incse.facility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.onem2m.bind.http.client.AsyncResponseListener;
import net.herit.iot.onem2m.bind.http.client.HttpClient;
import net.herit.iot.onem2m.incse.InCse;

public class NseManager {

	private Logger log = LoggerFactory.getLogger(NseManager.class);
	
	public enum BINDING_TYPE {
		BIND_NONE, BIND_HTTP, BIND_COAP, BIND_MQTT;
	}
	
	private AsyncResponseListener listener = null;

	private InCse inCSE = null;
	private BINDING_TYPE binding = BINDING_TYPE.BIND_NONE;

	public NseManager(InCse inCse, BINDING_TYPE income) {
		this.inCSE = inCse;
		this.binding = income;
	}

	public OneM2mResponse sendRequestMessage(String url, OneM2mRequest reqMessage) {
		log.debug("url: {} \nmessage: {}", url, reqMessage);
		if (!url.startsWith("http:")) url = "http:"+url;
		return new HttpClient().process(url, reqMessage);
	}

	public void setAsyncResponseListener(AsyncResponseListener listener) {
		this.listener = listener;
	}
	
	public boolean sendAsyncRequestMessage(String url, OneM2mRequest reqMessage) {
		return new HttpClient().processAsyncRequest(url, reqMessage, listener);
	}
	
	public boolean sendAsyncRequestMessage(String url, OneM2mRequest reqMessage, AsyncResponseListener listener) {
		return new HttpClient().processAsyncRequest(url, reqMessage, listener);
	}
	
//	public interface AsyncResponseListener {
//		public void asyncResponse(OneM2mResponse resMessage);
//	}
	
	public boolean sendResponseMessage(OneM2mResponse resMessage) {
		
		switch (binding) {
		case BIND_HTTP:
			return inCSE.sendHttpResponse(resMessage);
		case BIND_MQTT:
			return inCSE.sendMqttMessage(resMessage);
		case BIND_COAP:
		default:
			log.error("Not supported binding protocol({})", binding);
			return false;
		}
		
	}
}
