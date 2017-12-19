package net.herit.iot.onem2m.bind.coap.server;



import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.List;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.network.CoapEndpoint;
import org.eclipse.californium.core.network.EndpointManager;
import org.eclipse.californium.core.network.config.NetworkConfig;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.eclipse.californium.scandium.DTLSConnector;
import org.eclipse.californium.scandium.config.DtlsConnectorConfig;
import org.eclipse.californium.scandium.dtls.pskstore.InMemoryPskStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.herit.iot.onem2m.bind.coap.api.CoapServerListener;


public class HCoapServer extends CoapServer {

	private int port;
	private int sec_port = -1;
	private CoapServerListener listener = null;
	
	private Logger log = LoggerFactory.getLogger(HCoapServer.class);

//	private static final int DEFAULT_DTLS_PORT = 4433;
	private static final String TRUST_STORE_PASSWORD = "rootPass";
	private static final String KEY_STORE_PASSWORD = "endPass";
	private static final String KEY_STORE_LOCATION = "./certs/keyStore.jks";
	private static final String TRUST_STORE_LOCATION = "./certs/trustStore.jks";

	private void addEndpoints() {
		log.debug("addEndpoints");
//		for (InetAddress addr : EndpointManager.getEndpointManager().getNetworkInterfaces()) {
			// only binds to IPv4 addresses and localhost
//			log.debug(addr.getHostAddress());
//			if(!addr.getHostAddress().equals("127.0.0.1")) continue;
			InputStream in = null;
			InputStream inTrust = null;
			
			InetSocketAddress bindToAddress = new InetSocketAddress(this.port);
			addEndpoint(new CoapEndpoint(bindToAddress));
			log.info("Coap port: {}", this.port);
			
			if(this.sec_port <= 0) return;
			
			try {

				InMemoryPskStore pskStore = new InMemoryPskStore();
				// put in the PSK store the default identity/psk for tinydtls tests
				pskStore.setKey("Client_identity", "secretPSK".getBytes());
				KeyStore keyStore = KeyStore.getInstance("JKS");
				in = new FileInputStream(KEY_STORE_LOCATION);
				keyStore.load(in, KEY_STORE_PASSWORD.toCharArray());
	
				// load the trust store
				KeyStore trustStore = KeyStore.getInstance("JKS");
				inTrust = new FileInputStream(TRUST_STORE_LOCATION);
				trustStore.load(inTrust, TRUST_STORE_PASSWORD.toCharArray());
	
				// You can load multiple certificates if needed
				Certificate[] trustedCertificates = new Certificate[1];
				trustedCertificates[0] = trustStore.getCertificate("root");
	
				DtlsConnectorConfig.Builder builder = new DtlsConnectorConfig.Builder(new InetSocketAddress(this.sec_port));
				builder.setPskStore(pskStore);
				builder.setIdentity((PrivateKey)keyStore.getKey("server", KEY_STORE_PASSWORD.toCharArray()),
						keyStore.getCertificateChain("server"), true);
				builder.setTrustStore(trustedCertificates);

//				if (addr instanceof Inet4Address || addr.isLoopbackAddress()) {
//					InetSocketAddress bindToAddress = new InetSocketAddress(addr, COAP_PORT);
					
					addEndpoint(new CoapEndpoint(new DTLSConnector(builder.build()), NetworkConfig.getStandard()));//	
					
					log.info("Coaps port: {}", this.sec_port);
//				}
			} catch(Exception e) {
				log.error("addEndpoints exception:", e);
			} finally {
				try {
					if (inTrust != null) {
						inTrust.close();
					}
					if (in != null) {
						in.close();
					}
				} catch (IOException e) {
					log.error("Cannot close key store file", e);
				}
			}
//		}
	}

	public HCoapServer(String resourceId, String resourceName) throws SocketException {
		this(resourceId, resourceName, NetworkConfig.getStandard().getInt(NetworkConfig.Keys.COAP_PORT), -1);
	}
	
	public HCoapServer(String resourceId, String resourceName, CoapServerListener listener) 
			throws SocketException {
		this(resourceId, resourceName, listener, NetworkConfig.getStandard().getInt(NetworkConfig.Keys.COAP_PORT), -1);
	}
	
	public HCoapServer(String resourceId, String resourceName, int port, int sec_port) throws SocketException {
		this.port = port;
		this.sec_port = sec_port;

		log.debug("######### HCoapServer RESOURCE_ID: {}, resourceName: {}", resourceId, resourceName);
		add(new HCoapResource(resourceId)); //resourceName));  // 2016. 05. 13
		add(new HCoapResource(resourceName));
//		add(new CoapResource("~"+resourceId).add(new HCoapResource(resourceName)));
//		add(new CoapResource(resourceId).add(new HCoapResource(resourceName)));
		add(new HCoapResource("~"));	// for SP-relative Resource ID
		add(new HCoapResource("_"));	// Absolute Resource ID
		
		addEndpoints();
	}
	
	public HCoapServer(List<String> resourceList, int port, int sec_port) throws SocketException {
		this.port = port;
		this.sec_port = sec_port;

		for(String res : resourceList) {
			
			String[] split = res.split("\\^");
			if (split == null) continue;
			
			log.debug("resource[0]: {}", split[0] );
			add(new HCoapResource(split[0]));
			if(split.length >= 2) {
				log.debug("resource[1]: {}", split[1] );
				add(new CoapResource(split[0]).add(new HCoapResource(split[1])));
			}
		}
		add(new HCoapResource("~"));	// for SP-relative Resource ID
		add(new HCoapResource("_"));	// Absolute Resource ID
		
		addEndpoints();
	}
	
	public HCoapServer(String resourceId, String resourceName, CoapServerListener listener, int port, int sec_port)
			throws SocketException {
		
		this(resourceId, resourceName, port, sec_port);
		
		this.listener = listener;
	}
	
	public HCoapServer(List<String> resourceList, CoapServerListener listener, int port, int sec_port)
			throws SocketException {
		
		this(resourceList, port, sec_port);
		
		this.listener = listener;
	}
	
	public void setListener(CoapServerListener listener) {
		this.listener = listener;
	}

	/*
	 * Definition of the Hello-World Resource
	 */
	public class HCoapResource extends CoapResource {

		public HCoapResource(String resourceName) {

			// set resource identifier
			super(resourceName);
			this.setAllChildFilter(true);

			// set display name
//			getAttributes().setTitle("Herit Coap Resource");
		}

//		@Override
//		public void handleRequest(final Exchange exchange) {
//			Code code = exchange.getRequest().getCode();
//			switch (code) {
//				case GET:	handleGET(new CoapExchange(exchange, this)); break;
//				case POST:	handlePOST(new CoapExchange(exchange, this)); break;
//				case PUT:	handlePUT(new CoapExchange(exchange, this)); break;
//				case DELETE: handleDELETE(new CoapExchange(exchange, this)); break;
//			}
//		}
		
		private void receiveRequest(CoapExchange exchange) {
			if(listener != null) {
				listener.receiveCoapRequest(exchange);
			} else {
				exchange.respond(ResponseCode.INTERNAL_SERVER_ERROR, "Not found listener");
			}
		}
		
		
		@Override
		public void handleGET(CoapExchange exchange) {	
			// respond to the request
			//try { Thread.sleep(2000); } catch(Exception e) {} 
			
//			System.out.println("RequestCode=" + exchange.getRequestCode());
//			System.out.println("RequestPayload=" +exchange.getRequestPayload());
//			System.out.println("Options=" + exchange.getRequestOptions());
//			System.out.println("UriPath=" + exchange.getRequestOptions().getUriPathString());
			
//			exchange.respond("Hello World!");
			
			receiveRequest(exchange);
		}
		
		@Override
		public void handlePOST(CoapExchange exchange) {
			
			receiveRequest(exchange);
//			exchange.respond(ResponseCode.METHOD_NOT_ALLOWED);
		}
		
		@Override
		public void handlePUT(CoapExchange exchange) {
			
			receiveRequest(exchange);
//			exchange.respond(ResponseCode.METHOD_NOT_ALLOWED);
		}
		
		@Override
		public void handleDELETE(CoapExchange exchange) {
			receiveRequest(exchange);
//			exchange.respond(ResponseCode.METHOD_NOT_ALLOWED);
		}
		
	}
	

	public static void main(String[] args) {

		try {

			// create server
			HCoapServer server = new HCoapServer("herit-in", "herit-cse");
			// add endpoints on all IP addresses
			server.start();

		} catch (SocketException e) {
			System.err.println("Failed to initialize server: " + e.getMessage());
		}
	}
	
}
