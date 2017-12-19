package net.herit.iot.onem2m.bind.http.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentDecompressor;
//import io.netty.handler.ssl.SslContext;
import io.netty.handler.codec.http.HttpObjectAggregator;
import net.herit.iot.onem2m.bind.http.api.HttpClientListener;

public class HttpClientInitializer extends ChannelInitializer<SocketChannel> {

//	private final SslContext sslCtx;
	private DefaultFullHttpRequest mRequest;
	private HttpClientListener mHttpClientListener;

//	public HttpClientInitializer(SslContext sslCtx) {
	public HttpClientInitializer(HttpClientListener listener) {
//		this.sslCtx = sslCtx;
		mHttpClientListener = listener;
	}

	@Override
	public void initChannel(SocketChannel ch) {
		ChannelPipeline pipeline = ch.pipeline();

		// Enable HTTPS if necessary.
//		if (sslCtx != null) {
//			pipeline.addLast(sslCtx.newHandler(ch.alloc()));
//		}

		pipeline.addLast(new HttpClientCodec());
		// Remove the following line if you don't want automatic content decompression.
		pipeline.addLast(new HttpContentDecompressor());

		// Uncomment the following line if you don't want to handle HttpContents.
		//pipeline.addLast(new HttpObjectAggregator(65536));
		pipeline.addLast(new HttpObjectAggregator(65536 * 3));

		
//		pipeline.addLast(new HttpClientHandler(null, mHttpClientListener));
	}
}