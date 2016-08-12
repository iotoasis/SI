package net.herit.iot.onem2m.incse;

import java.net.InetSocketAddress;
import java.net.URI;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import net.herit.iot.db.mongo.MongoPool;
import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.onem2m.bind.http.api.HttpServerListener;
import net.herit.iot.onem2m.bind.http.codec.HttpRequestCodec;
import net.herit.iot.onem2m.bind.http.codec.HttpResponseCodec;
import net.herit.iot.onem2m.bind.http.server.HttpServer;
import net.herit.iot.onem2m.bind.http.server.HttpServerHandler;
import net.herit.iot.onem2m.core.util.LogManager;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.incse.context.OneM2mContext;
import net.herit.iot.onem2m.incse.controller.InterworkingController;
import net.herit.iot.onem2m.incse.controller.NotificationController;
import net.herit.iot.onem2m.incse.facility.CfgManager;
import net.herit.iot.onem2m.incse.facility.DatabaseManager;
import net.herit.iot.onem2m.incse.facility.NseManager;
import net.herit.iot.onem2m.incse.facility.SeqNumManager;

public class HttpHandler implements HttpServerListener  {
	
	private final static HttpVersion		httpVersion 		= HttpVersion.HTTP_1_1;
	private CfgManager cfgManager = CfgManager.getInstance();
	private HashMap<Integer, String> channelMap = new HashMap<Integer, String>();
	private HashMap<String, ChannelHandlerContext> sessionMap = new HashMap<String, ChannelHandlerContext>();
	private Object syncObject = new Object();

		
	private Logger log = LoggerFactory.getLogger(HttpHandler.class);

	public HttpHandler() {
		try {
				
		} catch (Exception e) {
			log.debug("Handled exception", e);
		}
		
	}
	

	
	@Override
	public void channelDisconnected(ChannelHandlerContext ctx) {
		log.debug("channelDisconnected");
	
		String requestId = channelMap.get(ctx.channel().hashCode());
		
		LongPollingManager.getInstance().disconnectedAccessPoint(requestId);
		
		removeSession(requestId);
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx) {
		log.debug("channelConnected");
		
	}

	@Override
	public void channelRequested(ChannelHandlerContext ctx) {
		log.debug("channelRequested");
		
	}

	private OneM2mContext createContext() {
		//return new OneM2mContext(new NseManager( this, NseManager.INCOME_INTERFACE.IN_HTTP));
		return null;
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
			reqMessage = HttpRequestCodec.decode(request, ((InetSocketAddress)ctx.channel().remoteAddress()).getHostString());
			
			addSession(reqMessage.getRequestIdentifier(), ctx);
			log.debug(reqMessage.toString());
			
			OneM2mContext context = createContext();
			
			new OperationProcessor(context).processRequest(reqMessage);
			
		} catch (Throwable th) {
			log.debug("RequestMessage decode failed", th);

			if(reqMessage != null) {
				removeSession(reqMessage.getRequestIdentifier());
			}
			
			sendError(ctx);
		}
	}
	
	@Override
	public boolean sendHttpResponse(OneM2mResponse resMessage) {
		ChannelHandlerContext ctx = sessionMap.get(resMessage.getRequestIdentifier());
		DefaultFullHttpResponse response = null;
		
		if(ctx == null) return false;
		
		removeSession(resMessage.getRequestIdentifier());
		
		try {
			response = HttpResponseCodec.encode(resMessage, httpVersion);
			response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.CLOSE);
			
			HttpServerHandler.sendHttpMessage(response, ctx.channel()).
									addListener(ChannelFutureListener.CLOSE).
									addListener(new FilnalEventListener(ctx, true));
		} catch (Exception e) {
			log.debug("Handled exception", e);

			sendError(ctx);
		}
		
		return true;
		
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

	
}
