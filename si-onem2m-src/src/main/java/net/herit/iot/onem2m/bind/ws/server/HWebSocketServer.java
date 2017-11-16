package net.herit.iot.onem2m.bind.ws.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Collection;

import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.onem2m.bind.coap.api.CoapServerListener;
import net.herit.iot.onem2m.bind.coap.server.HCoapServer;
import net.herit.iot.onem2m.bind.ws.api.WebSocketServerListener;
import net.herit.iot.onem2m.bind.ws.codec.WSRequestCodec;
import net.herit.iot.onem2m.bind.ws.codec.WSResponseCodec;

public class HWebSocketServer extends WebSocketServer {
	
	private WebSocketServerListener listener = null;
	
	private Logger log = LoggerFactory.getLogger(HWebSocketServer.class);
	
	public HWebSocketServer( int port ) throws UnknownHostException {
		super( new InetSocketAddress( port ) );
	}
	
	public HWebSocketServer( InetSocketAddress address ) {
		super( address );
	}
	
	public HWebSocketServer(int port, WebSocketServerListener listener) throws UnknownHostException {
		super( new InetSocketAddress( port ) );
		this.listener = listener;
	}
	
	public HWebSocketServer( InetSocketAddress address, WebSocketServerListener listener ) {
		super( address );
		this.listener = listener;
	}
	
	public void setListener(WebSocketServerListener listener) {
		this.listener = listener;
	}
	
	@Override
	public void onOpen( WebSocket conn, ClientHandshake handshake ) {
		//System.out.println("========================>" + handshake.getFieldValue("Sec-WebSocket-Protocol"));
		
		//this.sendToAll( "new connection: " + handshake.getResourceDescriptor() );
		conn.send("new connection: " + handshake.getResourceDescriptor());
		
	}
	
	@Override
	public void onClose( WebSocket conn, int code, String reason, boolean remote ) {
		//this.sendToAll( conn + " has left the room!" );
		//conn.send("connection[" + conn + "] is closed.");
		
	}
	
	@Override
	public void onMessage( WebSocket conn, String message ) {
		//System.out.println("onMessage==================>" + conn + ": " + message );
		//this.sendToAll( message );
		if(listener != null) {
			listener.receiveWebSocketRequest(message.getBytes(), conn);
		} else {
			conn.send("Not found listener");
		}
		//System.out.println( conn + ": " + message );
	}
	
	@Override
	public void onMessage( WebSocket conn, ByteBuffer message ) {
		byte[] msg = message.array();
		try {
			//System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!![" + new String(msg, "UTF-8"));
			//String recvMessage = new String(msg, "UTF-8").trim();
			if(listener != null) {
				//System.out.println("##################### listener ##################");
				listener.receiveWebSocketRequest(msg, conn);
			} else {
				conn.send("Not found listener");
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		//ByteArrayInputStream bis = new ByteArrayInputStream(msg);
		
		super.onMessage(conn, message);
	}

	

	public void onFragment( WebSocket conn, Framedata fragment ) {
		System.out.println( "received fragment: " + fragment );
	}
	
	@Override
	public void onError( WebSocket conn, Exception ex ) {
		ex.printStackTrace();
		if( conn != null ) {
			// some errors like port binding failed may not be assignable to a specific websocket
		}
	}
	
	/**
	 * Sends <var>text</var> to all currently connected WebSocket clients.
	 * 
	 * @param text
	 *            The String to send across the network.
	 * @throws InterruptedException
	 *             When socket related I/O errors occur.
	 */
	public void sendToAll( String text ) {
		Collection<WebSocket> con = connections();
		synchronized ( con ) {
			for( WebSocket c : con ) {
				
				//System.out.println("%%%%%%%%%% WebSocket %%%%%%%%%%%% " + c);
				c.send( text );
			}
		}
	}
	
	public static void main( String[] args ) throws InterruptedException , IOException {
		WebSocketImpl.DEBUG = true;
		int port = 8887; // 843 flash policy port
		try {
			port = Integer.parseInt( args[ 0 ] );
		} catch ( Exception ex ) {
		}
		HWebSocketServer s = new HWebSocketServer( port );
		
		s.start();
		
		
		
		System.out.println( "demoServer started on port: " + s.getPort() );

		BufferedReader sysin = new BufferedReader( new InputStreamReader( System.in ) );
		while ( true ) {
			String in = sysin.readLine();
			s.sendToAll( in );
			if( in.equals( "exit" ) ) {
				s.stop();
				break;
			} else if( in.equals( "restart" ) ) {
				s.stop();
				s.start();
				break;
			}
		}
	}
	

}
