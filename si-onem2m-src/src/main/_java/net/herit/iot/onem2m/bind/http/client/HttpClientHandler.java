package net.herit.iot.onem2m.bind.http.client;


import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import net.herit.iot.onem2m.bind.http.api.HttpClientListener;
import net.herit.iot.onem2m.core.util.Utils;

public class HttpClientHandler extends ChannelInboundHandlerAdapter {
	
	HttpClientListener		responseListener	= null;
	DefaultFullHttpRequest	request			= null;
	
	private Logger log = LoggerFactory.getLogger(HttpClientHandler.class);
	
	public HttpClientHandler(DefaultFullHttpRequest request, HttpClientListener listener) {
		this.request = request;
		this.responseListener = listener;
	}
	
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Channel channel = ctx.channel();
		log.debug("channelActive={}", channel);

/*		if (responseListener != null) {
			responseListener.channelConnected(ctx);
		}
*/
		sendHttpMessage(ctx, request, responseListener);

	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object message) throws Exception {
		try
		{
			Channel channel = ctx.channel();
			log.debug("channelRead={}", channel);
			
			if (message instanceof FullHttpResponse) {
				FullHttpResponse response = (FullHttpResponse) message;

				StringBuilder strBld = new StringBuilder();
				
				strBld.append("\n");
				strBld.append("######################## Client Response Log  ################################\n");
				strBld.append("---------------------------------------------------------------------\n");
				strBld.append(">> RECV HTTP RESPONSE: ").append(ctx.channel()).append("\n");
				strBld.append("STATUS: ").append(response.getStatus()).append("\n");
				strBld.append("VERSION: ").append(response.getProtocolVersion()).append("\n");

				for (Entry<String, String> entry : response.headers()) {
					strBld.append("HEADER: ").append(entry.getKey()).append(" = ").append(entry.getValue()).append("\n");
				}

				if (response.content().isReadable()) {
					String contentType = response.headers().get(HttpHeaders.Names.CONTENT_TYPE);

					/* 암호화에 따른 바이너리 Contents 출력 */
					ByteBuf copyBuf = response.content().copy();
					String content = null;
					//xml pretty print
					content = "CONTENTS: \n" + Utils.format(new String(copyBuf.array(), CharsetUtil.UTF_8));

					strBld.append(content).append("\n");
					copyBuf.release();
					strBld.append("---------------------------------------------------------------------\n");
				}
				log.debug(strBld.toString());

				if (responseListener != null) {
					responseListener.handleHttpResponse(ctx, response);
				} else {
					log.error("channelRead_failure=Not Found Listener");
				}
			} else {
				log.error("channelRead_failure=It's Not Supported Message Type. : {}", message);
			}
		} catch (Exception exception) {
			 log.error("channelRead_error=", exception);
		} finally {
			/* ByteBuf 해제 */
			ReferenceCountUtil.release(message);
			ctx.close();
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		Channel channel = ctx.channel();
		log.debug("channelInactive={}", channel);

		// if (triggerResource != null)
		// {
		// triggerResource.channelDisconnected(ctx);
		// }
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		Channel channel = ctx.channel();
		log.debug("channelUnregistered={}", channel);

		if (responseListener != null) {
			responseListener.channelDisconnected(ctx);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		Channel channel = ctx.channel();
		log.error("exceptionCaught={}, cause={}", channel, cause);
		
		/* Close */
		channel.disconnect().addListener(ChannelFutureListener.CLOSE);
	}

	/**
	 * Http message를 전송한다.
	 * 
	 * @param ctx
	 * @param request
	 * @param listener
	 * @return
	 */
	public ChannelFuture sendHttpMessage(ChannelHandlerContext ctx, DefaultFullHttpRequest request, HttpClientListener listener) {
		ChannelFuture future = null;
		String contentString = null;
		try {


			//ctx.channel().attr(AttributeKey.valueOf("TEST")).set(listener);

			if (request.content() != null) {
				if (request.content().isReadable()) {				
					ByteBuf copyBuf = request.content().copy();
					contentString = "CONTENTS: \n" + new String(copyBuf.array(), CharsetUtil.UTF_8);

					copyBuf.release();
				}
			}


//			 SENDING 
			future = ctx.channel().writeAndFlush(request);
			future.addListener(new EventListener(ctx, request, listener, contentString));

			StringBuilder strBld = new StringBuilder();
			
			strBld.append("\n");
			strBld.append("######################## Client Response Log  ###########################\n");
			strBld.append("<< SEND HTTP Request: ").append(future.channel()).append("\n");
			strBld.append("VERSION: ").append(request.getProtocolVersion()).append("\n");
			strBld.append("METHOD: ").append(request.getMethod()).append("\n");
			strBld.append("URI: ").append(request.getUri()).append("\n");
			
			for (Entry<String, String> entry : request.headers()) {
				strBld.append("HEADER: ").append(entry.getKey()).append(" = ").append(entry.getValue()).append("\n");
			}

			if (contentString != null) strBld.append(contentString).append("\n");
			strBld.append("\n");
			log.debug(strBld.toString());
			
			
			return future;
		} catch (Exception exception) {
			log.error("sendHttpMessage_error : {}", exception.getMessage());
		}
		return null;
	}

	public class EventListener implements ChannelFutureListener {

		private ChannelHandlerContext	ctx			= null;
		private HttpClientListener		listener	= null;
		private String					contents	= null;
		private DefaultFullHttpRequest	request		= null;

		public EventListener(ChannelHandlerContext ctx, DefaultFullHttpRequest request, HttpClientListener listener, String contents) {
			this.ctx = ctx;
			this.listener = listener;
			this.contents = contents;
			this.request = request;
		}

		/**
		 * SENT
		 */
		@Override
		public void operationComplete(ChannelFuture future) throws Exception {
			try {
				if (future.isDone() && future.isSuccess()) {
					StringBuilder strBld = new StringBuilder();
					strBld.append("\n");
					strBld.append("######################## Client Log operationComplete  ################################\n");
					strBld.append("---------------------------------------------------------------------\n");
					strBld.append("SEND HTTP Request: " + future.channel()).append("\n");

					//log.debug(strBld.toString());
					
//					 COMPLETE API 
					if(listener != null) {
						listener.channelRequested(ctx);
					}
				} else {
					log.error("operationComplete_failure channel=" + future.channel() + "\n" + request + " " + future.cause());
				}
			} catch (Exception e) {
				log.error("operationComplete_error : ", e);
				
				throw e;
			}
			finally {
//				 ByteBuf 해제 
//				ReferenceCountUtil.release(request);
			}
		}
	}
}
