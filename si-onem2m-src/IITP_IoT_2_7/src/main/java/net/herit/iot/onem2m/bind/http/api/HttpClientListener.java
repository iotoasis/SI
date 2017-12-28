package net.herit.iot.onem2m.bind.http.api;

import io.netty.channel.ChannelHandlerContext;

import io.netty.handler.codec.http.FullHttpResponse;

/**
 * HttpEventListener Interface 클래스. 이벤트 발생시, 해당 메서드가 Invoke됨.
 * 
 * @author 
 * 
 */
public interface HttpClientListener extends HttpEventListener {

	/**
	 * HTTP Response가 들어오면 Invoke된다.
	 * 
	 * @param ctx
	 *            the ChannelHandlerContext
	 * @param response
	 *            the RmResponse
	 */
	public void handleHttpResponse(ChannelHandlerContext ctx, FullHttpResponse response);

}