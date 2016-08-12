package net.herit.iot.onem2m.bind.http.server;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.herit.iot.onem2m.bind.http.api.HttpServerListener;
import net.herit.iot.onem2m.bind.http.server.HttpServer;
import net.herit.iot.onem2m.bind.http.server.HttpServerHandler;
import net.herit.iot.onem2m.bind.http.server.HttpServerInitializer;
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
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
//import io.netty.handler.ssl.SslContext;
//import io.netty.handler.ssl.SslContextBuilder;
//import io.netty.handler.ssl.util.SelfSignedCertificate;

/**
 * 
 */
public final class HttpServer {

	private Logger log = LoggerFactory.getLogger(HttpServer.class);
	
	private boolean ssl = false;
	private int port = 8080;
	private SslContext sslCtx = null; 
	private HttpServerListener listener = null;
	
	// member for runAsync
	private Channel bindChannel = null;
	EventLoopGroup bossGroup = null;
	EventLoopGroup workerGroup = null;
	
	public HttpServer(HttpServerListener listener, int port) throws Exception {
		this.listener = listener;
		this.port = port;
	}
	
	public HttpServer(HttpServerListener listener, int port, boolean ssl) throws Exception {
		this(listener, port);
		
		this.ssl = ssl;
		if(this.ssl) {
			SelfSignedCertificate ssc = new SelfSignedCertificate();
			this.sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
//			this.sslCtx = SslContextBuilder.forServer(
//					new File("/home/hubiss/project/2015/IITP-IoT/target/root.crt"), 
//					new File("/home/hubiss/project/2015/IITP-IoT/target/root.key")).build();
		}
	}


	public void run() throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workerGroup)
			.channel(NioServerSocketChannel.class)
			.handler(new LoggingHandler(LogLevel.INFO))
			.childHandler(new HttpServerInitializer(this.listener, this.sslCtx));

			bootstrap.option(ChannelOption.SO_REUSEADDR, true);
			
			Channel ch = bootstrap.bind(this.port).sync().channel();
			

			log.debug("Open your web browser and navigate to {}://127.0.0.1:{}/", 
					(this.ssl? "https" : "http"), this.port);

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
			.childHandler(new HttpServerInitializer(this.listener, this.sslCtx));

			b.option(ChannelOption.SO_REUSEADDR, true);
			bindChannel = b.bind(this.port).sync().channel();

			log.debug("Open your web browser and navigate to {}://127.0.0.1:{}/", 
					(this.ssl? "https" : "http"), this.port);

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
			log.debug("Handled exception",e);
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
