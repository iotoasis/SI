package net.herit.iot.onem2m.bind.ws.client;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.message.onem2m.OneM2mRequest.OPERATION;
import net.herit.iot.message.onem2m.OneM2mRequest.RESOURCE_TYPE;
import net.herit.iot.message.onem2m.format.Enums.CONTENT_TYPE;
import net.herit.iot.onem2m.bind.coap.client.HCoapClient;
import net.herit.iot.onem2m.bind.coap.codec.CoapRequestCodec;
import net.herit.iot.onem2m.bind.coap.codec.CoapResponseCodec;
import net.herit.iot.onem2m.resource.AE;
import net.herit.iot.onem2m.resource.PrimitiveContent;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.Request;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HWebSocketClient extends WebSocketClient {
	
	private static final int	WS_TIMEOUT = 5000;
	
	private Logger log = LoggerFactory.getLogger(HWebSocketClient.class);
	
	public HWebSocketClient( URI serverUri , Draft draft ) {
		super( serverUri, draft );
	}

	public HWebSocketClient( URI serverURI ) {
		super( serverURI );
	}
	
	public HWebSocketClient(URI serverUri, Draft draft, Map<String, String> httpHeaders, int connectionTimeout) {
		super(serverUri, draft, httpHeaders, WS_TIMEOUT);
	}
	
	@Override
	public void onOpen( ServerHandshake handshakedata ) {

		//System.out.println( "opened connection" );
		log.info("opened connection");
		// if you plan to refuse connection based on ip or httpfields overload: onWebsocketHandshakeReceivedAsClient
	}

	@Override
	public void onMessage( String message ) {
		//System.out.println( "received: " + message );
		log.info("received: " + message);
	}

	
	public void onFragment( Framedata fragment ) {
		//System.out.println( "received fragment: " + new String( fragment.getPayloadData().array() ) );		
		log.info( "received fragment: " + new String( fragment.getPayloadData().array() ) );
	}

	@Override
	public void onClose( int code, String reason, boolean remote ) {
		// The codecodes are documented in class org.java_websocket.framing.CloseFrame
		//System.out.println( "Connection closed by " + ( remote ? "remote peer" : "us" ) );
		log.info("Connection closed by " + ( remote ? "remote peer" : "us" ) );
	}
	
	
	@Override
	public void onError( Exception ex ) {
		ex.printStackTrace();
		// if the error is fatal then onClose will be called additionally
	}
/*	
	public OneM2mResponse process(String url, OneM2mRequest reqMessage) throws Exception {
		URI uri = null;
		
		log.debug("url: {}", url);
		log.debug(reqMessage.toString());
		
		Map<String, String> headers = new HashMap();
		
		headers.put("Sec-WebSockdet-Protocol", "oneM2M.R2.0.JSON");
		
		try {
			uri = new URI(url);
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return null;
		}
		
		HWebSocketClient c = new HWebSocketClient( uri, new Draft_17(), headers, WS_TIMEOUT );
		
	}
*/	
	public static void main( String[] args ) throws URISyntaxException {
Map<String, String> headers = new HashMap();
		
		headers.put("Sec-WebSockdet-Protocol", "oneM2M.R2.0.JSON");
		//headers.put("Sec-WebSockdet-Protocol", "oneM2M.R2.0.XML");
		
		HWebSocketClient c = new HWebSocketClient( new URI( "ws://10.10.0.22:8887" ), new Draft_17(), headers, 0 ); // more about drafts here: http://github.com/TooTallNate/Java-WebSocket/wiki/Drafts
		//c.connect();
		
		OneM2mRequest req = new OneM2mRequest();
		req.setRequestIdentifier("req_222222");
		req.setFrom("ae_001");
		req.setTo("/herit-cse");
		req.setContentType(CONTENT_TYPE.JSON);
//		req.setOperation(OPERATION.RETRIEVE);
		req.setOperation(OPERATION.CREATE);
		req.setResourceType(RESOURCE_TYPE.AE);
		
		AE ae = new AE();
		ae.addLabels("home");
		ae.setExpirationTime("20161231T235555");
		ae.setAppName("TEST");
		ae.setAppID("testApp");
		
		PrimitiveContent pCont = new PrimitiveContent();
		pCont.addAnyOrAny(pCont);
		
		req.setPrimitiveContent(pCont);
		
		String msg = "{\"m2m:rqp\":{\"op\":1,\"to\":\"/herit-cse\",\"rqi\":\"A1000\", \"rcn\":1,\"pc\":{\"m2m:ae\":{\"rn\":\"SmartHomeApplication6\", \"api\":\"Na561\", \"apn\":\"app123456\", \"rr\": false}},\"ty\":2, \"fr\" : \"//iot.herit.net/csebase/AE_00001123456\"}}";
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				       + "<m2m:rqp xmlns:m2m=\"http://www.onem2m.org/xml/protocols\">"
				       +     "<op>1</op>"
				       +     "<to>/herit-cse</to>"
				       +     "<fr>/cse1234/app5</fr>"
				       +     "<rqi>0002bf63</rqi>"
				       +     "<ty>2</ty>"
				       +     "<pc>"
				       +         "<m2m:ae>"
				       +             "<rn>SmartHomeApp4</rn>"
				       +             "<api>Na5666</api>"
				       +             "<apn>app12345</apn>"
				       +             "<rr>false</rr>"
				       +         "</m2m:ae>" 
				       +     "</pc>"
				       +  "</m2m:rqp>";
		
		try {
			if(c.connectBlocking()) {
				c.send(xml);
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
}
