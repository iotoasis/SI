package net.herit.iot.onem2m.bind.coap.server;



import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.network.CoapEndpoint;
import org.eclipse.californium.core.network.EndpointManager;
import org.eclipse.californium.core.network.config.NetworkConfig;
import org.eclipse.californium.core.server.resources.CoapExchange;

import net.herit.iot.onem2m.bind.coap.api.CoapServerListener;

public class HCoapServer extends CoapServer {

	private int COAP_PORT;
	private CoapServerListener listener = null;

	/**
	 * Add individual endpoints listening on default CoAP port on all IPv4 addresses of all network interfaces.
	 */
	private void addEndpoints() {
		for (InetAddress addr : EndpointManager.getEndpointManager().getNetworkInterfaces()) {
			// only binds to IPv4 addresses and localhost
			if(addr.getHostAddress() != "127.0.0.1") continue;
			
			if (addr instanceof Inet4Address || addr.isLoopbackAddress()) {
				InetSocketAddress bindToAddress = new InetSocketAddress(addr, COAP_PORT);
				addEndpoint(new CoapEndpoint(bindToAddress));
				System.out.println("addEndpoint: " + addr.getHostAddress() + ":" + COAP_PORT);
			}
		}
	}

	public HCoapServer(String resourceId, String resourceName) throws SocketException {
		this(NetworkConfig.getStandard().getInt(NetworkConfig.Keys.COAP_PORT), resourceId, resourceName);
	}
	
	public HCoapServer(String resourceId, String resourceName, CoapServerListener listener) 
			throws SocketException {
		this(NetworkConfig.getStandard().getInt(NetworkConfig.Keys.COAP_PORT), 
				resourceId, resourceName, listener);
	}
	
	public HCoapServer(int port, String resourceId, String resourceName) throws SocketException {
		COAP_PORT = port;

		add(new HCoapResource(resourceName));
		add(new CoapResource(resourceId).add(new HCoapResource(resourceName)));
		add(new HCoapResource("~"));	// for SP-relative Resource ID
		add(new HCoapResource("_"));	// Absolute Resource ID
		
		addEndpoints();
	}
	
	public HCoapServer(int port, String resourceId, String resourceName, CoapServerListener listener)
			throws SocketException {
		
		this(port, resourceId, resourceName);
		
		this.listener = listener;
	}
	
	public void setListener(CoapServerListener listener) {
		this.listener = listener;
	}

	/*
	 * Definition of the Hello-World Resource
	 */
	class HCoapResource extends CoapResource {

		public HCoapResource(String resourceName) {

			// set resource identifier
			super(resourceName);
			this.setAllChildFilter(true);

		}

		private void receiveRequest(CoapExchange exchange) {
			if(listener != null) {
				listener.receiveCoapRequest(exchange);
			} else {
				exchange.respond(ResponseCode.INTERNAL_SERVER_ERROR, "Not found listener");
			}
		}
		
		@Override
		public void handleGET(CoapExchange exchange) {
			receiveRequest(exchange);
		}
		
		@Override
		public void handlePOST(CoapExchange exchange) {
			receiveRequest(exchange);
		}
		
		@Override
		public void handlePUT(CoapExchange exchange) {
			receiveRequest(exchange);
		}
		
		@Override
		public void handleDELETE(CoapExchange exchange) {
			receiveRequest(exchange);
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
