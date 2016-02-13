package net.herit.iot.onem2m.bind.coap.client;

import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.Utils;
import org.eclipse.californium.core.coap.Request;

import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mRequest.OPERATION;
import net.herit.iot.message.onem2m.OneM2mRequest.RESOURCE_TYPE;
import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.message.onem2m.format.Enums.CONTENT_TYPE;
import net.herit.iot.onem2m.bind.api.ResponseListener;
import net.herit.iot.onem2m.bind.coap.api.CoapClientListener;
import net.herit.iot.onem2m.bind.coap.codec.CoapRequestCodec;
import net.herit.iot.onem2m.bind.coap.codec.CoapResponseCodec;
import net.herit.iot.onem2m.resource.AE;
import net.herit.iot.onem2m.resource.PrimitiveContent;

public class HCoapClient implements CoapHandler {

	private static final Long	COAP_TIMEOUT = 5000L;
	
	private ResponseListener listener;
	private CoapClient client = null;
	
	public HCoapClient()	{}
	
	public HCoapClient(String host, int port, String url) throws Exception {
		String path = "coap://" + host + ":" + port + url;
		URI uri = null;
		
		try {
			uri = new URI(path);
		} catch (URISyntaxException e) {
			throw e;
		}
		
		client = new CoapClient(uri);
		client.setTimeout(COAP_TIMEOUT);
	}
	
	
//	public HCoapClient(String host, int port, String url, CoapClientListener listener) throws Exception {
//		this(host, port, url);
//		this.listener = listener;
//		
//	}
	
	public OneM2mResponse process(String url, OneM2mRequest reqMessage) {
		URI uri = null;
		
		try {
			uri = new URI(url);
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return null;
		}
		
		client = new CoapClient(uri);
		client.setTimeout(COAP_TIMEOUT);
		
		return process(reqMessage);
	}
	
	public OneM2mResponse process(OneM2mRequest reqMessage) {
		OneM2mResponse resMessage = null;
		try {
			Request request = CoapRequestCodec.encode(reqMessage);
			System.out.println(request.getPayloadString());
			resMessage = CoapResponseCodec.decode(client.advanced(request));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resMessage;
	}
	
	public boolean processAsyncRequest(ResponseListener listener, OneM2mRequest reqMessage) {
		try {
			this.listener = listener;
			Request request = CoapRequestCodec.encode(reqMessage);
			client.advanced(this, request);
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	

	@Override
	public void onLoad(CoapResponse response) {
		if (listener != null) {
			OneM2mResponse resMessage;
			try {
				resMessage = CoapResponseCodec.decode(response);
			
				listener.receiveResponse(resMessage);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}


	@Override
	public void onError() {
		if (listener != null) {
			listener.onError();
		}
		
	}
	
	public static void main(String args[]) {

		URI uri = null; // URI parameter of the request
//		String path = "coap://10.101.107.51:5683/herit-in/herit-cse/abc/dad";
		String path = "coap://10.101.107.51:5683/herit-in/herit-cse";

		try {
			HCoapClient client = new HCoapClient("10.101.107.51", 5683, "/herit-in/herit-cse");
			
			OneM2mRequest req = new OneM2mRequest();
			req.setRequestIdentifier("req_222222");
			req.setFrom("ae_0001");
			req.setTo("/herit-cse");
			req.setContentType(CONTENT_TYPE.JSON);
//			req.setOperation(OPERATION.RETRIEVE);
			req.setOperation(OPERATION.CREATE);
			req.setResourceType(RESOURCE_TYPE.AE);
			
			AE ae = new AE();
			ae.addLabels("home");
			ae.setExpirationTime("20161231T235555");
			ae.setAppName("TEST");
			ae.setAppID("testApp");
			
			PrimitiveContent pCont = new PrimitiveContent();
			pCont.addAny(ae);
			
			req.setPrimitiveContent(pCont);
			
			OneM2mResponse resMessage = client.process(req);
			System.out.println("ResponseCode=" + resMessage.getResponseStatusCode());
			System.out.println(resMessage.toString());
			
		} catch (Exception e) {
			System.out.println(e);
		}
		
	}
}
