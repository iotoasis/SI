package net.herit.iot.onem2m.bind.http.api;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpResponse;

/**
 * 
 * @author 
 * 
 */
public interface HttpClientListener extends HttpEventListener {

	/**
	 * 
	 * @param ctx
	 *            the ChannelHandlerContext
	 * @param response
	 *            the RmResponse
	 */
	public void handleHttpResponse(ChannelHandlerContext ctx, FullHttpResponse response);

}