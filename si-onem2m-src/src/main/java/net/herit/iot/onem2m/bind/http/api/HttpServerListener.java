package net.herit.iot.onem2m.bind.http.api;

import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.onem2m.core.util.OneM2MException;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

public interface HttpServerListener extends HttpEventListener {
	/**
	 * HTTP Request가 들어오면 Invoke된다.
	 * 
	 * @param ctx
	 *            the ChannelHandlerContext
	 * @param request
	 *            the HttpRequest
	 */
//	public void handleHttpRequest(ChannelHandlerContext ctx, DefaultFullHttpRequest request);
	public void receiveHttpRequest(ChannelHandlerContext ctx, FullHttpRequest request);

	public boolean sendHttpResponse(OneM2mResponse responseMessage) throws OneM2MException;
}
