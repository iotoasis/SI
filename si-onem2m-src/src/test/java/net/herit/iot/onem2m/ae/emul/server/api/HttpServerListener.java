package net.herit.iot.onem2m.ae.emul.server.api;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import net.herit.iot.onem2m.bind.http.api.HttpEventListener;

public interface HttpServerListener extends HttpEventListener {
	/**
	 * HTTP Request가 들어오면 Invoke된다.
	 * 
	 * @param ctx
	 *            the ChannelHandlerContext
	 * @param request
	 *            the HttpRequest
	 */
	public void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest request);
}
