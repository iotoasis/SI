/*
*/

package net.herit.iot.onem2m.bind.http.client;


import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.message.onem2m.OneM2mRequest.OPERATION;
import net.herit.iot.onem2m.bind.api.ResponseListener;
import net.herit.iot.onem2m.bind.http.api.HttpClientListener;
import net.herit.iot.onem2m.bind.http.codec.HttpRequestCodec;
import net.herit.iot.onem2m.bind.http.codec.HttpResponseCodec;


/**
 * A simple HTTP client that prints out the content of the HTTP response to
 * {@link System#out} to test {@link HttpSnoopServer}.
 */
public final class HttpClient {

	private static HttpClient INSTANCE = new HttpClient();
	
	private int CONNECTION_TIMEOUT_MILLIS = 180 * 1000; // 180 seconds.
	
	private ResponseListener listener = null;

	private Logger log = LoggerFactory.getLogger(HttpClient.class);
	
	private boolean isSync = false;
	
	ChannelFuture channelFuture = null;
//	OneM2mResponse resMessage = null;
	//Channel channel = null;
	
	Bootstrap bootstrap = null;
	EventLoopGroup group = null;
	
	
	
	private HttpClient() {
		// Configure the client.
		group = new NioEventLoopGroup();
		bootstrap = new Bootstrap();
		bootstrap.group(group)
		.channel(NioSocketChannel.class)
		.handler(new HttpClientInitializer(mHttpClientListener));

		bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, CONNECTION_TIMEOUT_MILLIS);
		bootstrap.option(ChannelOption.TCP_NODELAY, true);
	}
	
	public static HttpClient getInstance() {
		return INSTANCE;
	}
	
	public void terminate() {
		if (group != null) {
			group.shutdownGracefully();
			
			log.debug("========== shutdownGracefully =============");
		} else {
			log.error("group is null");
		}
	}
	
	
	private DefaultFullHttpRequest makeHttpMessage(String host, OneM2mRequest reqMessage) throws Exception {
		DefaultFullHttpRequest request = HttpRequestCodec.encode(reqMessage, HttpVersion.HTTP_1_1);
		request.headers().add(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.CLOSE);
		request.headers().add(HttpHeaders.Names.HOST, host);
		
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
		
		return request;
	}
	
	public OneM2mResponse sendRequest(String url, OneM2mRequest reqMessage) {
		
		try {
			URI uri = new URI(url);
			String host = uri.getHost() == null? "127.0.0.1" : uri.getHost();
			int port = uri.getPort();
			
			DefaultFullHttpRequest request = makeHttpMessage(url, reqMessage);
			
			Channel channel = bootstrap.connect(host, port).sync().channel();
			channel.writeAndFlush(request);
			
			channel.closeFuture().sync(); //.addListener(new ConnectListner(request, mHttpClientListener));

			OneM2mResponse resMessage = resMessageMap.get(channel);
			return resMessage;

		} catch (Exception e) {
			
		}
		
		
		return null;
	}
	
	
	public boolean sendAsyncRequest(ResponseListener listener, String url, OneM2mRequest reqMessage) {
		
		try {
			URI uri = new URI(url);
			String host = uri.getHost() == null? "127.0.0.1" : uri.getHost();
			int port = uri.getPort();
		
			DefaultFullHttpRequest request = makeHttpMessage(url, reqMessage);

			bootstrap.connect(host, port).addListener(new ConnectListner(request, mHttpClientListener, listener));
			
			return true;
			
		} catch (Exception e) {
			
		}
		
		return false;
	}
	
	
	private class ConnectListner implements ChannelFutureListener
	{
		private DefaultFullHttpRequest	request		= null;
		private HttpClientListener		listener	= null;
		private ResponseListener svcListener = null;

		public ConnectListner(DefaultFullHttpRequest request, HttpClientListener listener, ResponseListener svcListener)
		{
			this.request = request;
			this.listener = listener;
			this.svcListener = svcListener;
		}

		@Override
		public void operationComplete(ChannelFuture future) throws Exception
		{
			addListener(future.channel(), this.svcListener);
			future.channel().pipeline().addLast("handler", new HttpClientHandler(request, listener));
		}
	}
	
	public OneM2mResponse process_back(String url, OneM2mRequest reqMessage) {
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
				log.error("Only HTTP(S) is supported.");
				return null;
			}
	
			// Configure SSL context if necessary.
	//		final boolean ssl = "https".equalsIgnoreCase(scheme);
	//		final SslContext sslCtx;
	//		if (ssl) {
	//			sslCtx = SslContextBuilder.forClient()
	//					.trustManager(InsecureTrustManagerFactory.INSTANCE).build();
	//          } else {
	//        	  sslCtx = null;
	//          }
	
			// Configure the client.
			group = new NioEventLoopGroup();
		
			// Prepare the HTTP request.
//			DefaultFullHttpRequest request = HttpRequestCodec.encode(url, reqMessage, HttpVersion.HTTP_1_1);
			DefaultFullHttpRequest request = HttpRequestCodec.encode(reqMessage, HttpVersion.HTTP_1_1);
			request.headers().add(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.CLOSE);
			request.headers().add(HttpHeaders.Names.HOST, host);
			
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
			
			Bootstrap b = new Bootstrap();
			b.group(group)
			.channel(NioSocketChannel.class)
			.handler(new HttpClientInitializer(mHttpClientListener));

			b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);
			// Make the connection attempt.
			//channelFuture = b.connect(host, port).addListener(new ConnectListner(request, mHttpClientListener)); //sync().channel();
			Channel channel = b.connect(host, port).sync().channel();

			//if (channelFuture != null) log.debug("channelFuture is not null.");
			
			// Send the HTTP request.
			channel.writeAndFlush(request);

			// Wait for the server to close the connection.
			channel.closeFuture().sync();
		} catch (URISyntaxException e) {
			log.error("exception: ", e);
		} catch (Exception e) {
			log.error("exception: ", e);
		} finally {
			// Shut down executor threads to exit.
			// group.shutdownGracefully();
		}

		return null; //resMessage;
	}
	
	public boolean processAsyncRequest_back(ResponseListener listener, String url, OneM2mRequest reqMessage) {
		isSync = false;
		boolean res = false;
		this.listener = listener;
		
		try {
			if(process_back(url, reqMessage) != null) {
				res = true;
			}

		} catch (Exception e) {
			log.debug("Handled exception",e);
		}
		
		return res;
	}
	
/*	public OneM2mResponse processRequest(String url, OneM2mRequest reqMessage) {
		isSync = true;
		
		try {
			ChannelFuture channelFuture = process(url, reqMessage);
			
			if (channelFuture == null) System.out.println("dddchannelFuture is null..");
			
			synchronized(channelFuture) {
				log.debug("START: wait for channelFuture notification!!! {}", channelFuture);
				channelFuture.wait();
				log.debug("END: wait for channelFuture notification!!! {}", channelFuture);
			}

			log.debug("========== Channel notified");
			if (resMessage == null) {
				log.debug(channelFuture.toString());
				//log.debug(channelFuture.cause().toString());
			}
		} catch (Exception e) {
			log.error("execption=", e);			
			log.debug("Handled exception",e);
			resMessage = null;
		}
		
		if (resMessage != null) {
			resMessage.setRequest(reqMessage);
		}
		return resMessage;
	}*/
	
	private ConcurrentHashMap<Channel, ResponseListener> listenerMap = 
			new ConcurrentHashMap<Channel, ResponseListener>();
	
	private ConcurrentHashMap<Channel, OneM2mResponse> resMessageMap = 
			new ConcurrentHashMap<Channel, OneM2mResponse>();
	
	public void addResMessage(Channel channel, OneM2mResponse resMessage) {
		resMessageMap.put(channel, resMessage);
	}
	
	public void addListener(Channel channel, ResponseListener listener) {
		listenerMap.put(channel, listener);
	}

	
	private HttpClientListener mHttpClientListener = new HttpClientListener() {
				
		@Override
		public void channelRequested(ChannelHandlerContext ctx) {
			log.debug("channelRequested");
		}
		
		@Override
		public void channelDisconnected(ChannelHandlerContext ctx) {
			log.debug("channelDisconnected");

			ResponseListener listener = listenerMap.remove(ctx.channel());
			if(listener != null) {
				listener.onError();
			}
			
			if(isSync)
				synchronized(channelFuture) {
					channelFuture.notify();
				}
			
//			if (group != null) {
//				group.shutdownGracefully();
//			}
//
//			group = null;
		}
		
		@Override
		public void channelConnected(ChannelHandlerContext ctx) {
			log.debug("channelConnected");
			
		}
		
		@Override
		public void handleHttpResponse(ChannelHandlerContext ctx, FullHttpResponse response) {
			try {
				log.debug("HttpClient: handleHttpResponse");
				OneM2mResponse resMessage = HttpResponseCodec.decode(response, ((InetSocketAddress)ctx.channel().remoteAddress()).getHostString());
				
				ResponseListener listener = listenerMap.remove(ctx.channel());
				if(listener != null) {
					listener.receiveResponse(resMessage);
				} else {
					addResMessage(ctx.channel(), resMessage);
				}
			} catch (Exception e) {
				log.debug("Handled exception", e);
//				resMessage = null;
			}
		}
	};
	
	
	public static void main(String[] args) throws Exception {
		HttpClient client = HttpClient.getInstance();
		OneM2mRequest reqMessage = new OneM2mRequest();
		reqMessage.setOperation(OPERATION.CREATE);
		reqMessage.setTo("monitor.do");
		reqMessage.setFrom("test");
		reqMessage.setRequestIdentifier("233322323");

		client.sendAsyncRequest(null, "http://10.101.101.107:8088/monitor.do", reqMessage);
//		OneM2mResponse response = client.sendRequest("http://10.101.101.107:8088/monitor.do", reqMessage);
//		System.out.println(response.toString());
		
//		OneM2mResponse resMessage = client.process("http://10.101.101.107:8088/monitor.do", reqMessage);

//		OneM2mResponse resMessage = client.processRequest(reqMessage);
//		System.out.println(resMessage.toString());
	}
}