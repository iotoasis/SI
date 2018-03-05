package net.herit.iot.onem2m.ae.emul;

import java.net.URI;

import org.eclipse.californium.core.CoapResponse;

import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.message.onem2m.OneM2mRequest.OPERATION;
import net.herit.iot.message.onem2m.OneM2mRequest.RESOURCE_TYPE;
import net.herit.iot.message.onem2m.format.Enums.CONTENT_TYPE;
import net.herit.iot.onem2m.bind.coap.client.HCoapClient;
import net.herit.iot.onem2m.resource.AE;
import net.herit.iot.onem2m.resource.PrimitiveContent;

public class CoapAeEmulator {

	private static final int COAP_DEFAULT_PORT = 5683;
	private static final int COAP_DEFAULT_DTLS_PORT = 4433; //5684;
	
	public static void main(String args[]) {

		URI uri = null; // URI parameter of the request
//		String path = "coap://10.101.107.51:5683/herit-in/herit-cse/abc/dad";
		String path = "coap://10.101.107.10:5683/herit-in/herit-cse";
		
		try {
			//HCoapClient client = new HCoapClient("10.101.107.10", COAP_DEFAULT_DTLS_PORT, "/~/herit-cse");
			HCoapClient client = new HCoapClient("10.10.0.18", COAP_DEFAULT_PORT, "");
			//client.setDTLS();
			
			OneM2mRequest req = new OneM2mRequest();
			req.setRequestIdentifier("req_222222");
			req.setFrom("ae_0001");
			req.setTo("/~/herit-in");
			req.setContentType(CONTENT_TYPE.JSON);
			req.setOperation(OPERATION.RETRIEVE);
/*			req.setOperation(OPERATION.CREATE);
			req.setResourceType(RESOURCE_TYPE.AE);
			
			AE ae = new AE();
			ae.addLabels("home");
			ae.setExpirationTime("20161231T235555");
			ae.setAppName("TEST_COAP_20161101");
			ae.setAppID("testApp");
			
			PrimitiveContent pCont = new PrimitiveContent();
			//pCont.addAny(ae);
			pCont.addAnyOrAny(ae);
			
			req.setPrimitiveContent(pCont);
*/			
			OneM2mResponse resMessage = client.process(req);
			System.out.println("ResponseCode=" + resMessage.getResponseStatusCode());
			System.out.println(resMessage.toString());
			
		} catch (Exception e) {
			System.out.println(e);
		}
		
	}
}
