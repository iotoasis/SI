package net.herit.iot.onem2m.ae.emul.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.Map.Entry;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import net.herit.iot.onem2m.ae.emul.server.api.HttpServerListener;

public class HttpServerHandler extends ChannelInboundHandlerAdapter {

	private HttpVersion httpVersion = HttpVersion.HTTP_1_1;
	private boolean			isReadBytes	= false;
	private HttpServerListener requestListener;
	
	private static Logger log = LoggerFactory.getLogger(HttpServerHandler.class);
	
	public HttpServerHandler(HttpServerListener listener) {
		this.requestListener = listener;
	}
	

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Channel channel = ctx.channel();
		log.debug("channelActive={}", channel);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		Channel channel = ctx.channel();
		log.debug("channelInactive={}", channel);
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		Channel channel = ctx.channel();
		log.debug("channelUnregistered={}", channel);

		/*
		 * else { InetSocketAddress address = (InetSocketAddress) channel.remoteAddress(); String addr =
		 * address.getHostName();
		 * 
		 * if (logger.isErrorEnabled()) { if (addr.equalsIgnoreCase("10.101.101.98") == false)
		 * logger.error("channelUnregistered=ctx.attr(LISTENER_ATTR_KEY) Not Found." ); } }
		 */

		if (requestListener != null) {
			requestListener.channelDisconnected(ctx);
		}
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object message) throws Exception {
		FullHttpRequest httpRequest = null;
		try
		{
			Channel channel = ctx.channel();
			log.debug("channelRead=", channel);

			/* timer 해제 */
			isReadBytes = true;

			/* HTTP 요청 메시지만 처리 */
			if (message instanceof FullHttpRequest) {
				httpRequest = (FullHttpRequest) message;

				StringBuilder strBld = new StringBuilder();
				strBld.append("\n");
				strBld.append("######################## Server Log  ################################\n");
				strBld.append("---------------------------------------------------------------------\n");
				strBld.append(">> RECV HTTP REQUEST: ").append(ctx.channel()).append("\n");
				strBld.append("VERSION: ").append(httpRequest.getProtocolVersion()).append("\n");
				strBld.append("METHOD: ").append(httpRequest.getMethod()).append("\n");
				strBld.append("URI: ").append(httpRequest.getUri()).append("\n");
					
				for (Entry<String, String> entry : httpRequest.headers()) {
					strBld.append("HEADER: ").append(entry.getKey()).append(" = ").append(entry.getValue()).append("\n");
				}

				if (httpRequest.content().isReadable()) {
					String contentType = httpRequest.headers().get(HttpHeaders.Names.CONTENT_TYPE);

					ByteBuf copyBuf = httpRequest.content().copy();
					String content = null;
					content = "CONTENTS: \n" + new String(copyBuf.array(), CharsetUtil.UTF_8);

					strBld.append(content).append("\n");
					copyBuf.release();
					strBld.append("---------------------------------------------------------------------\n");
				}
				log.debug(strBld.toString());

				/* WARNNING - URL 기본규칙을 사용하지 않음 */
				/*
				 * URI uri = new URI(URLDecoder.decode(httpRequest.getUri(), ServerProperty.getInst().getEncodeChar()));
				 */
				URI uri = null;
				try {
					uri = new URI(httpRequest.getUri());
				} catch (Exception e) {
					log.error("channelRead_error=", e);

					sendHttpMessage(new DefaultFullHttpResponse(httpVersion, HttpResponseStatus.BAD_REQUEST),
							ctx.channel()).addListener(ChannelFutureListener.CLOSE);

					return;
				}

				/**
				 * Resource 위치가 명시되지 않으면 에러 응답.
				 */
				if (httpRequest.getUri() == null || uri.getPath() == null) {
					log.error("channelRead_failure=Resource Not Found from URI(URI is Not Exist)");
				
					sendHttpMessage(new DefaultFullHttpResponse(httpVersion, HttpResponseStatus.NOT_FOUND),
							ctx.channel()).addListener(ChannelFutureListener.CLOSE);

					return;
				}

				if (httpRequest.getUri().equalsIgnoreCase("/bad-request")) {
					log.error("channelRead_failure=Resource Not Found from URI(URI is Unavailable)");

					sendHttpMessage(new DefaultFullHttpResponse(httpVersion, HttpResponseStatus.BAD_REQUEST),
							ctx.channel()).addListener(ChannelFutureListener.CLOSE);

					return;
				}

				if (uri.getPath().equalsIgnoreCase("/monitor.do")) {
					String hello = "Hello";
					ByteBuf buff = ctx.alloc().directBuffer().writeBytes(hello.getBytes(CharsetUtil.UTF_8));
					DefaultFullHttpResponse httpMessage = new DefaultFullHttpResponse(httpVersion, HttpResponseStatus.OK, buff);
					httpMessage.headers().add(HttpHeaders.Names.CONTENT_LENGTH, buff.readableBytes());
					sendHttpMessage(httpMessage, ctx.channel());
				} else {
					StringBuilder strBld2 = new StringBuilder();
					strBld2.append("\n");
					strBld2.append("######################## Trigger Log  ###########################\n");
					strBld2.append("---------------------------------------------------------------------\n");
					strBld2.append("URL : ").append(uri.getPath()).append("\n");
					strBld2.append("TRIGGER TARGET: ").append(requestListener).append("\n");
					strBld2.append("---------------------------------------------------------------------\n");
					log.debug(strBld2.toString());
					
					if (requestListener != null) {
//						ctx.attr(HttpServerImpl.LISTENER_ATTR_KEY).set(triggerResource);
						requestListener.handleHttpRequest(ctx, (FullHttpRequest) message);
					} else {
						log.error("channelRead_failure=Not Found Listener");
						
						sendHttpMessage(new DefaultFullHttpResponse(httpVersion, HttpResponseStatus.NOT_FOUND),
								ctx.channel()).addListener(ChannelFutureListener.CLOSE);
					}

				}

			} else  {
				log.error("channelRead_failure=It's Not Supported Message Type. : " + message);

				ctx.close();
			}
		} catch (Exception exception) {
			log.error("channelRead_error=" + exception.toString());

			if (httpRequest != null) {
				sendHttpMessage(new DefaultFullHttpResponse(httpVersion, HttpResponseStatus.INTERNAL_SERVER_ERROR),
						ctx.channel()).addListener(	ChannelFutureListener.CLOSE);
			} else {
				ctx.close();
			}
		} finally {
			/* ByteBuf 해제 */
			ReferenceCountUtil.release(message);
		}
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

		Channel channel = ctx.channel();
		log.error("userEventTriggered={}, object={}", channel, evt);

		if (!(evt instanceof IdleStateEvent)) { return; }

		if (evt instanceof IdleStateEvent)  {
			IdleStateEvent e = (IdleStateEvent) evt;

			if (e.state() == IdleState.READER_IDLE) {
				log.debug("userEventTriggered(readIdle)={}", ctx.channel());

				if (isReadBytes == false) ctx.close();
			} else if (e.state() == IdleState.WRITER_IDLE) {
				log.debug("userEventTriggered(writeIdle)={}", ctx.channel());
				
				if (isReadBytes == false) ctx.close();
			} else {
				log.debug("userEventTriggered(allIdle)={}", ctx.channel());
				
				if (isReadBytes == false) ctx.close();
			}
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		Channel channel = ctx.channel();
		log.error("exceptionCaught={}, cause={}", channel, cause);

		/* Content Length의 크기가 너무 큰 경우 */
		if (cause instanceof TooLongFrameException) {
			sendHttpMessage(new DefaultFullHttpResponse(httpVersion, HttpResponseStatus.REQUEST_ENTITY_TOO_LARGE), ctx.channel())
					.addListener(ChannelFutureListener.CLOSE);
		} else {
			ctx.close();
		}

	}

	/**
	 * Http message를 전송한다.
	 * 
	 * @param response
	 *            the http message
	 * @param channel
	 *            the channel
	 * @return the channel future
	 */
	public static ChannelFuture sendHttpMessage(final DefaultFullHttpResponse response, Channel channel) {

		try {
			if (channel != null && channel.isActive() && channel.isWritable()) {
				String contentString = null;

				if (response.content() != null) {
					if (response.content().isReadable()) {
						String contentType = response.headers().get(HttpHeaders.Names.CONTENT_TYPE);

						contentString = "CONTENTS: \n" + response.content().toString(CharsetUtil.UTF_8);

					}
				}

				/* SENDING */
				ChannelFuture future = channel.writeAndFlush(response);

				final String contents = contentString;
				future.addListener(new ChannelFutureListener() {

					/**
					 * SENT
					 */
					@Override
					public void operationComplete(ChannelFuture future) throws Exception {
						try {
							if (future.isDone() && future.isSuccess()) {
								
								StringBuilder strBld = new StringBuilder();
								strBld.append("\n");
								strBld.append("######################## Server Log  ################################\n");
								strBld.append("---------------------------------------------------------------------\n");
								strBld.append("<< SEND HTTP Response: ").append(future.channel()).append("\n");
								for (Entry<String, String> entry : response.headers()) {
									strBld.append("HEADER: ").append(entry.getKey()).append(" = ").append(entry.getValue()).append("\n");
								}
								strBld.append("VERSION: ").append(response.getProtocolVersion()).append("\n");
								strBld.append("STATUS: ").append(response.getStatus()).append("\n");


								if (contents != null) strBld.append(contents).append("\n");
								strBld.append("---------------------------------------------------------------------\n");
								strBld.append("\n");
								log.debug(strBld.toString());
							} else {
								log.error("operationComplete_failure channel=" + future.channel() + "\n" + response + ", " + future.cause());
							}
						} catch (Exception e) {
							log.error("operationComplete_error=", e);
							
							throw e;
						} finally {
							
							/* ByteBuf 해제 */
							try {
								ReferenceCountUtil.release(response);
							}
							catch (Exception e) {}
						}
					}
				});

				return future;
			} else {
				log.error("sendHttpMessage_failure=Channel not connected, channel={}", channel);
			}
		} catch (Exception exception) {
			log.error("sendHTTPMessage_error=", exception);
			exception.printStackTrace();
			
			response.setStatus(HttpResponseStatus.INTERNAL_SERVER_ERROR);
			channel.writeAndFlush(response);
			channel.close();
		}

		return null;
	}
}