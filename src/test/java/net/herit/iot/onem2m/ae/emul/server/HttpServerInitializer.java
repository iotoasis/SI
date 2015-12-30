package net.herit.iot.onem2m.ae.emul.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import net.herit.iot.onem2m.ae.emul.server.api.HttpServerListener;

//import io.netty.handler.ssl.SslContext;

public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {

	HttpServerListener listener = null;
//	private final SslContext sslCtx;

	public HttpServerInitializer(HttpServerListener listener) {
//		this.sslCtx = sslCtx;
		this.listener = listener;
	}

	@Override
	public void initChannel(SocketChannel ch) {
		ChannelPipeline pipeline = ch.pipeline();
//		if (sslCtx != null) {
//			p.addLast(sslCtx.newHandler(ch.alloc()));
//		}
		pipeline.addLast(new HttpResponseEncoder());
		pipeline.addLast(new HttpRequestDecoder());
		// Uncomment the following line if you don't want to handle HttpChunks.
		//pipeline.addLast("chunkedWriter", new ChunkedWriteHandler());
		//p.addLast(new HttpObjectAggregator(1048576));
		// Remove the following line if you don't want automatic content compression.
		//pipeline.addLast(new HttpContentCompressor());
		
		// Uncomment the following line if you don't want to handle HttpContents.
		pipeline.addLast(new HttpObjectAggregator(65536));
		
		pipeline.addLast("handler", new HttpServerHandler(listener));
	}
}