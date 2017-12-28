package net.herit.iot.onem2m.bind.http.server;

import net.herit.iot.onem2m.bind.http.api.HttpServerListener;
import net.herit.iot.onem2m.bind.http.server.HttpServerHandler;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;
//import io.netty.handler.ssl.SslContext;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {
	private static final int READ_TIMEOUT = 240; // 180 seconds.
	private HttpServerListener listener = null;
	private final SslContext sslCtx;

	public HttpServerInitializer(HttpServerListener listener2, SslContext sslCtx) {
		this.sslCtx = sslCtx;
		this.listener = listener2;
	}

	@Override
	public void initChannel(SocketChannel ch) {
		ChannelPipeline pipeline = ch.pipeline();
		if (sslCtx != null) {
			pipeline.addLast(sslCtx.newHandler(ch.alloc()));
		}
		pipeline.addLast(new HttpResponseEncoder());
		pipeline.addLast(new HttpRequestDecoder());
		// Uncomment the following line if you don't want to handle HttpChunks.
		//pipeline.addLast("chunkedWriter", new ChunkedWriteHandler());
		//p.addLast(new HttpObjectAggregator(1048576));
		// Remove the following line if you don't want automatic content compression.
		//pipeline.addLast(new HttpContentCompressor());
		
		// Uncomment the following line if you don't want to handle HttpContents.
		pipeline.addLast(new HttpObjectAggregator(65536));
		pipeline.addLast("readTimeoutHandler", new ReadTimeoutHandler(READ_TIMEOUT));
		pipeline.addLast("myHandler", new MyHandler());
		
		pipeline.addLast("handler", new HttpServerHandler(listener));
	}
	
	class MyHandler extends ChannelDuplexHandler {
		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
			if(cause instanceof ReadTimeoutException) {
				System.out.println("## Read Timeout. ## [Message : " + cause.getMessage() + "]");
			} else {
				super.exceptionCaught(ctx, cause);
			}

		}
	}
}