/*
*/

package net.herit.iot.onem2m.bind.http.client;


import java.net.URI;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;
//import io.netty.handler.ssl.SslContext;
//import io.netty.handler.ssl.SslContextBuilder;
//import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import net.herit.iot.onem2m.bind.http.api.HttpClientListener;

/**
 * A simple HTTP client that prints out the content of the HTTP response to
 * {@link System#out} to test {@link HttpSnoopServer}.
 */
public final class HttpClientBasic {

	private Logger log = LoggerFactory.getLogger(HttpClientBasic.class);
	ChannelFuture channelFuture = null;
	FullHttpResponse resMessage = null;
	
	EventLoopGroup group = null;
	
	
	private HttpClientListener mHttpClientListener = new HttpClientListener() {
		@Override
		public void channelRequested(ChannelHandlerContext ctx) {
			
		}
		
		@Override
		public void channelDisconnected(ChannelHandlerContext ctx) {
			
		}
		
		@Override
		public void channelConnected(ChannelHandlerContext ctx) {
			
		}
		
		@Override
		public void handleHttpResponse(ChannelHandlerContext ctx, FullHttpResponse response) {
			resMessage = response.copy();
		}
	};


	public FullHttpResponse process(String url, DefaultFullHttpRequest request) {
		try {
			URI uri = new URI(url);
			String scheme = uri.getScheme() == null? "http" : uri.getScheme();
			String host = uri.getHost() == null? "127.0.0.1" : uri.getHost();
			int port = uri.getPort();
			if (port == -1) {
				if ("http".equalsIgnoreCase(scheme)) {
					port = 80;
				} else if ("https".equalsIgnoreCase(scheme)) {
					port = 443;
				}
			}
	
			if (!"http".equalsIgnoreCase(scheme) && !"https".equalsIgnoreCase(scheme)) {
				System.err.println("Only HTTP(S) is supported.");
				return null;
			}
			
			//print log
			StringBuilder strBld = new StringBuilder();
			
			strBld.append("\n");
			strBld.append("######################## Client Request log  ###########################\n");
			strBld.append("<< SEND HTTP Request: ").append("\n");
			strBld.append("VERSION: ").append(request.getProtocolVersion()).append("\n");
			strBld.append("METHOD: ").append(request.getMethod()).append("\n");
			strBld.append("URI: ").append(request.getUri()).append("\n");
			
			for (Entry<String, String> entry : request.headers()) {
				strBld.append("HEADER: ").append(entry.getKey()).append(" = ").append(entry.getValue()).append("\n");
			}
			
			if (request.content() != null) {
				if (request.content().isReadable()) {
					ByteBuf copyBuf = request.content().copy();
					String contentString = "CONTENTS: \n" + new String(copyBuf.array(), CharsetUtil.UTF_8);
					strBld.append(contentString).append("\n");
					copyBuf.release();
				}
			}

			log.debug(strBld.toString());

			
			// Configure the client.
			group = new NioEventLoopGroup();

			Bootstrap b = new Bootstrap();
			b.group(group)
			.channel(NioSocketChannel.class)
			.handler(new HttpClientInitializer(mHttpClientListener));
			

			b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);
			// Make the connection attempt.
			//channelFuture = b.connect(host, port).addListener(new ConnectListner(httpRequest, this)); //sync().channel();
			Channel channel = b.connect(host, port).sync().channel();

			channel.writeAndFlush(request);

			// Wait for the server to close the connection.
			channel.closeFuture().sync();
	
		} catch (Exception e) {
			log.debug("Handled exception", e);
		} finally {
			// Shut down executor threads to exit.
			group.shutdownGracefully();
		}

		return resMessage;
	}
	
	

	
	public static void main(String[] args) throws Exception {
		
		HttpClientBasic client = new HttpClientBasic();
		
		String deviceModel = "kidswatch1";
		String serviceCode = "0079";
		String m2mmType = "LTE_DEVICE";
		String deviceSn = "00000000000000000301l"; 
		String mac = ""; 
		String ctn = "01012345674";
		String deviceType = "adn";

		String mefAddr = "http://106.103.234.198/mef";

		String body = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n<auth>\r\n"+
					"    <deviceModel>"+deviceModel+"</deviceModel>\r\n"+
				    "    <serviceCode>"+serviceCode+"</serviceCode>\r\n"+
				    "    <m2mmType>"+m2mmType+"</ m2mmType >\r\n"+
					"    <deviceSerialNo>"+deviceSn+"</ deviceSerialNo >\r\n"+
				    "    <mac>"+mac+"</mac>\r\n"+
					"    <ctn>"+ctn+"</ctn>\r\n"+
				    "    <deviceType>"+deviceType+"</deviceType>\r\n"+
				    "<auth >\r\n";

		DefaultFullHttpRequest httpRequest = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, 
												mefAddr, Unpooled.copiedBuffer(body.getBytes()));
		
		FullHttpResponse httpResponse = client.process(mefAddr, httpRequest);
		
		System.out.println(httpResponse.toString());
		
	}
}