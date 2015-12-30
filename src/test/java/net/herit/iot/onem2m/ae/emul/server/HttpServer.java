package net.herit.iot.onem2m.ae.emul.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import net.herit.iot.onem2m.ae.emul.server.api.HttpServerListener;

//import io.netty.handler.ssl.SslContext;
//import io.netty.handler.ssl.SslContextBuilder;
//import io.netty.handler.ssl.util.SelfSignedCertificate;
/**
 * 
 */
public final class HttpServer {

	static final boolean SSL = System.getProperty("ssl") != null;
	//static final int PORT = Integer.parseInt(System.getProperty("port", SSL? "8443" : "8080"));
	static int PORT = 8080;

	private Logger log = LoggerFactory.getLogger(HttpServer.class);
	
	private HttpServerListener listener = null;
	
	// member for runAsync
	private Channel bindChannel = null;
	EventLoopGroup bossGroup = null;
	EventLoopGroup workerGroup = null;
	
	public HttpServer(HttpServerListener listener, int port) {
		this.listener = listener;
		this.PORT = port;
	}


	public void run() throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
			.channel(NioServerSocketChannel.class)
			.handler(new LoggingHandler(LogLevel.INFO))
			.childHandler(new HttpServerInitializer(this.listener));

			b.option(ChannelOption.SO_REUSEADDR, true);
			Channel ch = b.bind(PORT).sync().channel();

			log.debug("Open your web browser and navigate to {}://127.0.0.1:{}/", (SSL? "https" : "http"), PORT);

			ch.closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

	public void runAsync() throws Exception {
		bossGroup = new NioEventLoopGroup(1);
		workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
			.channel(NioServerSocketChannel.class)
			.handler(new LoggingHandler(LogLevel.INFO))
			.childHandler(new HttpServerInitializer(this.listener));

			b.option(ChannelOption.SO_REUSEADDR, true);
			bindChannel = b.bind(PORT).sync().channel();

			log.debug("Open your web browser and navigate to {}://127.0.0.1:{}/", (SSL? "https" : "http"), PORT);

			//ch.closeFuture().sync();
		} finally {
			//bossGroup.shutdownGracefully();
			//workerGroup.shutdownGracefully();
		}
	}
	
	public void stop() {
		try {
			bindChannel.close();
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();	
			bindChannel.closeFuture().sync();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {		
			
		}
	}
	
	public ChannelFuture sendHttpResponse(ChannelHandlerContext ctx, DefaultFullHttpResponse response)	{

		return HttpServerHandler.sendHttpMessage(response, ctx.channel());
	}

	public static void main(String[] args) throws Exception {
		
		HttpServer server = new HttpServer(null, 8080);
		server.run();
		
		// Configure SSL.
//		final SslContext sslCtx;
//		if (SSL) {
//			SelfSignedCertificate ssc = new SelfSignedCertificate();
//			sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
//		} else {
//			sslCtx = null;
//		}

		// Configure the server.
		
	}
}
