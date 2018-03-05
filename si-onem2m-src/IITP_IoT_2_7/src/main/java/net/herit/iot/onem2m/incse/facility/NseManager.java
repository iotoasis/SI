package net.herit.iot.onem2m.incse.facility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.message.onem2m.OneM2mRequest.OPERATION;
import net.herit.iot.onem2m.bind.api.ResponseListener;
import net.herit.iot.onem2m.bind.coap.client.HCoapClient;
import net.herit.iot.onem2m.bind.http.api.HttpClientListener;
import net.herit.iot.onem2m.bind.http.client.HttpClient;
import net.herit.iot.onem2m.incse.InCse;

public class NseManager {

	private Logger log = LoggerFactory.getLogger(NseManager.class);
	
	public enum BINDING_TYPE {
		BIND_NONE, BIND_HTTP, BIND_COAP, BIND_MQTT,	BIND_WEBSOCKET;
	}
	
	private ResponseListener listener = null;

	private InCse inCSE = null;
	private BINDING_TYPE binding = BINDING_TYPE.BIND_NONE;

	public NseManager(InCse inCse, BINDING_TYPE income) {
		this.inCSE = inCse;
		this.binding = income;
	}

	public String getBindingName() {
		return binding.name();
	}
	
	public OneM2mResponse sendRequestMessage(String url, OneM2mRequest reqMessage) {
		log.debug("url: {} \nmessage: {}", url, reqMessage);
		
//		if (!url.startsWith("http:")) url = "http:"+url;
		
		if (url.startsWith("http")) {
//			return new HttpClient().process(url, reqMessage);
			return HttpClient.getInstance().sendRequest(url, reqMessage);
		} else if (url.startsWith("coap")) {
			try {
				return new HCoapClient().process(url, reqMessage);
			} catch (Exception e) {
				return null;
			}
		} else if (url.startsWith("mqtt")) {
			inCSE.sendMqttMessage(reqMessage);
	
				// Mqtt binding은 Blocking mode로 동작 할 수 없음.
			return null;
		} else {
			log.warn("Don't send oneM2mRequest Message. Unknown Binding protocol.");
			return null;
		}
		
	}

//	public void setAsyncResponseListener(ResponseListener listener) {
//		this.listener = listener;
//	}
//	
//	public boolean sendAsyncRequestMessage(String url, OneM2mRequest reqMessage) {
//		return new HttpClient().processAsyncRequest(url, reqMessage, listener);
//	}
	
	public boolean sendAsyncRequestMessage(ResponseListener listener, String url, OneM2mRequest reqMessage) {
//		return new HttpClient().processAsyncRequest(listener, url, reqMessage);
		return HttpClient.getInstance().sendAsyncRequest(listener, url, reqMessage);
	}
	
	public boolean sendResponseMessage(OneM2mResponse resMessage) {
		
		//================== INFO log write. ===================
//		StringBuilder strbld = new StringBuilder();
//		strbld.append("<< SEND RES ");
//
//		strbld.append("PRTO:").append(getBindingName()).append(" ");
//		strbld.append("STATUS:").append(resMessage.getResponseStatusCodeEnum().Value()).append(" ");
////		strbld.append("OP:" ).append(reqMessage.getOperationEnum().Name()).append(" ");
////		if(reqMessage.getOperationEnum().equals(OPERATION.CREATE)) {
////			strbld.append("TY:").append(reqMessage.getResourceTypeEnum().Name()).append(" ");
////		}
////		strbld.append("TO:").append(resMessage.getTo()).append(" ");
//		strbld.append("RI:").append(resMessage.getRequestIdentifier());
//
//		log.info(strbld.toString());
		//===============================================================
		
		if(inCSE == null) {
			log.warn("MessageHandler(inCSE) is null");
			return false;
		}
				
		switch (binding) {
		case BIND_HTTP:
			return inCSE.sendHttpResponse(resMessage);
		case BIND_MQTT:
			return inCSE.sendMqttMessage(resMessage);
		case BIND_COAP:
			return inCSE.sendCoapResponse(resMessage);
		case BIND_WEBSOCKET:
			return inCSE.sendWebSocketResponse(resMessage);
		default:
			log.error("Not supported binding protocol({})", binding);
			return false;
		}
		
	}
}
