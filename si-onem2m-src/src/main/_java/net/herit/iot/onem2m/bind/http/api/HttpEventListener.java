package net.herit.iot.onem2m.bind.http.api;

import io.netty.channel.ChannelHandlerContext;

public interface HttpEventListener {
	/**
	 * 
	 * @param ctx
	 *            the ChannelHandlerContext
	 * @param e
	 *            the ChannelStateEvent
	 */
	public void channelDisconnected(ChannelHandlerContext ctx);

	/**
	 * 
	 * @param ctx
	 */
	public void channelConnected(ChannelHandlerContext ctx);

	/**
	 * 
	 * @param ctx
	 */
	public void channelRequested(ChannelHandlerContext ctx);
}
