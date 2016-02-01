package net.herit.iot.onem2m.ae.emul.server;

import java.net.InetSocketAddress;
import java.util.Iterator;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.AttributeKey;
import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.onem2m.ae.emul.server.api.HttpServerListener;
import net.herit.iot.onem2m.ae.emul.server.api.IHttpListener;
import net.herit.iot.onem2m.ae.emul.server.api.IHttpRequestListener;
import net.herit.iot.onem2m.ae.lib.HttpBasicRequest;
import net.herit.iot.onem2m.ae.lib.HttpBasicResponse;
import net.herit.iot.onem2m.core.util.LogManager;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.bind.http.codec.ResponseCodec;

public class OneM2mHttpListener implements HttpServerListener, IHttpListener {

	private final HttpVersion httpVersion 		= HttpVersion.HTTP_1_1;

	public static AttributeKey<FullHttpRequest> ATTR_KEY_REQUEST = AttributeKey.valueOf("httpRequest");

	private HttpServer 		httpServer;
	private LogManager logManager = LogManager.getInstacne();
	private Logger log = LoggerFactory.getLogger(OneM2mHttpListener.class);

	IHttpRequestListener mListener;
//	private AEController aeController;
//	private NotiHandlerInterface notiHandler;

	public OneM2mHttpListener(String ip, int port, IHttpRequestListener listener) {
		try {
			httpServer = new HttpServer(this, port);
			logManager.initialize(LoggerFactory.getLogger("IITP-IOT-APP-AE"), null);
			mListener = listener;

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	@Override
	public void start() throws Exception {

		httpServer.runAsync();

	}

	@Override
	public void stop() {

		httpServer.stop();

	}



	@Override
	public void channelDisconnected(ChannelHandlerContext ctx) {
		log.debug("channelDisconnected");
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx) {
		log.debug("channelConnected");

	}

	@Override
	public void channelRequested(ChannelHandlerContext ctx) {
		log.debug("channelRequested");

	}

	@Override
//	public void handleHttpRequest(ChannelHandlerContext ctx,DefaultFullHttpRequest request) {
	public void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest request) {
		log.debug("handleHttpRequest from " + ctx.channel().remoteAddress().toString());

		OneM2mRequest reqMessage = null;
		try {

//			HashMap<String, String> headerMap = new HashMap<String, String>();
//			HttpHeaders headers = request.headers();
//			Iterator<Entry<String, String>> it = headers.iterator();
//			while (it.hasNext()) {
//				Entry<String, String> header = it.next();
//				headerMap.put(header.getKey(), header.getValue());
//			}

			InetSocketAddress inetSocketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
			String host = inetSocketAddress.getHostString();
			
			String method = request.getMethod().name();
			String uri = request.getUri();
			byte[] content = request.content().isReadable() ? request.content().copy().array() : null;

			HttpBasicRequest basicRequest = new HttpBasicRequest(method, uri, host, content);

			log.debug("created basicRequest");

			HttpHeaders headers = request.headers();
			Iterator<Entry<String, String>> it = headers.iterator();
			while (it.hasNext()) {
				Entry<String, String> header = it.next();
				basicRequest.addHeader(header.getKey(), header.getValue());
			}

			log.debug("added headers");

			//reqMessage = RequestCodec.decode(method, uri, headerMap, host, content);


			HttpBasicResponse response = mListener.handleHttpRequest(basicRequest);//this.aeController.doProcessRequest(basicRequest, this.notiHandler);

			log.debug("created response");

			DefaultFullHttpResponse dfResponse = null;
			if (response.getContent() != null) {
				dfResponse = new DefaultFullHttpResponse(httpVersion, HttpResponseStatus.OK,
													Unpooled.copiedBuffer(response.getContent()), false);
			} else {
				dfResponse = new DefaultFullHttpResponse(httpVersion, HttpResponseStatus.OK);
			}

			dfResponse.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.CLOSE);
			dfResponse.headers().set("X-M2M-RSC", response.getHeaders().get("X-M2M-RSC"));
			dfResponse.headers().set("X-M2M-RI", response.getHeaders().get("X-M2M-RI"));

			log.debug("start send response");
			HttpServerHandler.sendHttpMessage(dfResponse, ctx.channel()).
									addListener(ChannelFutureListener.CLOSE).
									addListener(new FilnalEventListener(ctx, true));
			log.debug("completed to send response");

		} catch (OneM2MException ex) {
			OneM2mResponse resMessage = new OneM2mResponse(ex.getResponseStatusCode(), reqMessage);
			resMessage.setContent(new String(ex.getMessage()).getBytes());
			try {

				sendResponse(ctx, ResponseCodec.encode(resMessage, httpVersion));

			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Throwable th) {
			th.printStackTrace();
			log.error("RequestMessage decode failed.", th);

			sendError(ctx);
		}
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

	protected ChannelFuture sendResponse(ChannelHandlerContext ctx, final FullHttpResponse response)
	{

		if (ctx.attr(ATTR_KEY_REQUEST).get() != null)
		{
			FullHttpRequest request = ctx.attr(ATTR_KEY_REQUEST).get();

			if (HttpHeaders.isKeepAlive(request))
			{
				response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
			}
		}

		if (response.headers().get(HttpHeaders.Names.CONNECTION) == null)
		{
			response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.CLOSE);
		}

		ChannelFuture future = ctx.writeAndFlush(response);
		future.addListener(new ChannelFutureListener()
		{
			@Override
			public void operationComplete(ChannelFuture channelFuture) throws Exception
			{
				if (channelFuture.isDone() && channelFuture.isSuccess())
				{
					if (log.isDebugEnabled())
					{
						log.debug("");
						log.debug("############################## Send Log  ############################");
						log.debug("---------------------------------------------------------------------");
						log.debug("SEND HTTP Response: " + channelFuture.channel());
						for (Entry<String, String> entry : response.headers())
						{
							log.debug("HEADER: " + entry.getKey() + " = " + entry.getValue());
						}
						log.debug("VERSION: " + response.getProtocolVersion());
						log.debug("STATUS: " + response.getStatus());

						log.debug("---------------------------------------------------------------------");
						log.debug("");
					}
				}
				else
				{
					if (log.isErrorEnabled())
					{
						log.error(
								"operationComplete_failure channel=" + channelFuture.channel() + ", isDone=" + channelFuture.isDone() + ", isSuccess=" + channelFuture.isSuccess()
										+ ", isCancelled=" + channelFuture.isCancelled() + ", isCancellable" + channelFuture.isCancellable() + "\n" + response,
								channelFuture.cause());
					}
				}

			}
		});

		log.debug("response.headers().get(CONNECTION)=" + response.headers().get(HttpHeaders.Names.CONNECTION));
		if (!response.headers().get(HttpHeaders.Names.CONNECTION).equals(HttpHeaders.Values.KEEP_ALIVE))
		{
			log.debug("connection close");
			future.addListener(ChannelFutureListener.CLOSE);
		}

		return future;
	}
	

}

