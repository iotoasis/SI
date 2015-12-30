package net.herit.iot.onem2m.incse;

import java.net.InetSocketAddress;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.onem2m.bind.http.api.HttpServerListener;
import net.herit.iot.onem2m.bind.http.codec.RequestCodec;
import net.herit.iot.onem2m.bind.http.codec.ResponseCodec;
import net.herit.iot.onem2m.bind.http.server.HttpServerHandler;
import net.herit.iot.onem2m.core.util.Utils;
import net.herit.iot.onem2m.incse.context.RestContext;
import net.herit.iot.onem2m.incse.facility.CfgManager;

public class RestHandler implements HttpServerListener  {

	private final static HttpVersion		httpVersion 		= HttpVersion.HTTP_1_1;
	private CfgManager cfgManager = CfgManager.getInstance();
	private HashMap<Integer, String> channelMap = new HashMap<Integer, String>();
	private HashMap<String, ChannelHandlerContext> sessionMap = new HashMap<String, ChannelHandlerContext>();
	private Object syncObject = new Object();

	private Logger log = LoggerFactory.getLogger(RestHandler.class);

	public RestHandler() {
				
	}
	

	
	@Override
	public void channelDisconnected(ChannelHandlerContext ctx) {
		log.debug("channelDisconnected");
	
		String requestId = channelMap.get(ctx.channel().hashCode());
		
		AccessPointManager.getInstance().disconnectedAccessPoint(requestId);
		
		removeSession(requestId);
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx) {
		log.debug("Rest channelConnected");
		
	}

	@Override
	public void channelRequested(ChannelHandlerContext ctx) {
		log.debug("Rest channelRequested");
		
	}

	private RestContext createContext() {
		return new RestContext(this);
	}
	
	private void addSession(String requestId, ChannelHandlerContext ctx) {
		synchronized(syncObject) {
			sessionMap.put(requestId, ctx);
			channelMap.put(ctx.channel().hashCode(), requestId);
		}
	}
	
	private void removeSession(String requestId) {
		synchronized(syncObject) {
			ChannelHandlerContext channContext =	sessionMap.remove(requestId);
			if(channContext != null) {
				channelMap.remove(channContext.channel().hashCode());
			} else {
				log.debug("removeSession] ChannelHandlerContext is null. can't remove sessionMap");
			}
		}
	}
	@Override
//	public void handleHttpRequest(ChannelHandlerContext ctx,DefaultFullHttpRequest request) {
	public void receiveHttpRequest(ChannelHandlerContext ctx, FullHttpRequest request) {
		log.debug("handleHttpRequest from " + ctx.channel().remoteAddress().toString());
		OneM2mRequest reqMessage = null;
		try {
			reqMessage = RequestCodec.decode(request, ((InetSocketAddress)ctx.channel().remoteAddress()).getHostString());
			
			String reqId = Utils.createRequestId();
			reqMessage.setRequestIdentifier(reqId);
			
			addSession(reqId, ctx);
			
			log.debug(reqMessage.toString());
			RestContext context = createContext();
			
			new RestProcessor(context).processRequest(reqMessage);
			
		} catch (Throwable th) {
			th.printStackTrace();
			log.error("RequestMessage decode failed.", th);
			if(reqMessage != null) {
				removeSession(reqMessage.getRequestIdentifier());
			}
			
			sendError(ctx);
		}
	}
	
	public boolean sendResponseMessage(String requestId, String statusCode, String body) {
		ChannelHandlerContext ctx = sessionMap.get(requestId);
		
		if(ctx == null) return false;
		
		removeSession(requestId);
		
		try {
			DefaultFullHttpResponse response = this.makeHttpResponse(HttpResponseStatus.valueOf(Integer.parseInt(statusCode)), body != null ? body.getBytes() : null);
			response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.CLOSE);
			
			HttpServerHandler.sendHttpMessage(response, ctx.channel()).
									addListener(ChannelFutureListener.CLOSE).
									addListener(new FilnalEventListener(ctx, true));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			sendError(ctx);
		}
		
		return true;
		
	}
	
	private DefaultFullHttpResponse makeHttpResponse(HttpResponseStatus status, byte[] content) {
		
		DefaultFullHttpResponse response = null;
		if(content != null) {
			response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, Unpooled.copiedBuffer(content));
			response.headers().set("Content-Length", content.length);
			response.headers().set("Content-Type", "application/json");
		} else {
			response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status);
		}
		
		return response;
	}
	
	private void sendError(ChannelHandlerContext ctx) {
		DefaultFullHttpResponse response = 
				new DefaultFullHttpResponse(httpVersion, HttpResponseStatus.INTERNAL_SERVER_ERROR);
		HttpServerHandler.sendHttpMessage(response, ctx.channel()).
								addListener(ChannelFutureListener.CLOSE).
								addListener(new FilnalEventListener(ctx, true));
	}
	
	private class FilnalEventListener implements ChannelFutureListener {

		private ChannelHandlerContext channelContext = null;
		private boolean isClose = false;
		
		public FilnalEventListener(ChannelHandlerContext channelContext, boolean isClose) {
			this.channelContext = channelContext;
			this.isClose = isClose;
		}
		
		@Override
		public void operationComplete(ChannelFuture arg0) throws Exception {
			log.debug("FinalEventListener.operationComplete..");
		}
		
	}

	@Override
	public boolean sendHttpResponse(OneM2mResponse resMessage) {
		ChannelHandlerContext ctx = sessionMap.get(resMessage.getRequestIdentifier());
		DefaultFullHttpResponse response = null;
		
		if(ctx == null) return false;
		
		removeSession(resMessage.getRequestIdentifier());
		
		try {
			response = ResponseCodec.encode(resMessage, httpVersion);
			response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.CLOSE);
			
			HttpServerHandler.sendHttpMessage(response, ctx.channel()).
									addListener(ChannelFutureListener.CLOSE).
									addListener(new FilnalEventListener(ctx, true));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			sendError(ctx);
		}
		
		return true;
	}
}
