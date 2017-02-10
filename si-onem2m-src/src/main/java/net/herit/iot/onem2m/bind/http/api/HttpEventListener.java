package net.herit.iot.onem2m.bind.http.api;

import io.netty.channel.ChannelHandlerContext;

public interface HttpEventListener {
	/**
	 * Channel 연결이 해제되면 Invoke된다.
	 * 
	 * @param ctx
	 *            the ChannelHandlerContext
	 * @param e
	 *            the ChannelStateEvent
	 */
	public void channelDisconnected(ChannelHandlerContext ctx);

	/**
	 * Channel 연결이 완료되면 Invoke된다.
	 * 
	 * @param ctx
	 */
	public void channelConnected(ChannelHandlerContext ctx);

	/**
	 * 요청메시지 전송이 완료되면 Invoke된다.
	 * 
	 * @param ctx
	 */
	public void channelRequested(ChannelHandlerContext ctx);
}
