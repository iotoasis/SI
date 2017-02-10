package net.herit.iot.onem2m.bind.http.server;


import java.util.ArrayList;
import java.util.List;

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



/**
 * 
 */
public final class HttpServer {

	private Logger log = LoggerFactory.getLogger(HttpServer.class);
	
	class BindInfo {
		private HttpServerListener listener;
		private int port;
		private boolean ssl;
		
		public BindInfo(HttpServerListener listener, int port, boolean isSSL) {
			this.listener = listener;
			this.port = port;
			this.ssl = isSSL;
		}
		public HttpServerListener getListener() {
			return this.listener;
		}
		public int getPort() {
			return this.port;
		}
		public boolean isSSL() {
			return this.ssl;
		}
	}

	
	private boolean ssl = false;
	private int port = 8080;
	private SslContext sslCtx = null; 
	private HttpServerListener listener = null;
	
	private int bossThreadPoolSize;
	private int workerThreadPoolSize;
	
	// member for runAsync
	private Channel bindChannel = null;
	private List<BindInfo> bindInfo = new ArrayList<BindInfo>();
	private EventLoopGroup bossGroup = null;
	private EventLoopGroup workerGroup = null;
	
	public HttpServer() {
		this(0, 30);
	}
	
	public HttpServer(int bossThreadPoolSize, int workerThreadPoolSize) {
		this.bossThreadPoolSize = bossThreadPoolSize;
		this.workerThreadPoolSize = workerThreadPoolSize;

		log.debug("Netty bossThreadPoolSize: {}, workerThreadPoolSize: {}", 
				this.bossThreadPoolSize, this.workerThreadPoolSize);
		
		if(this.bossThreadPoolSize > 0) {
			bossGroup = new NioEventLoopGroup(this.bossThreadPoolSize);
		} else {
			bossGroup = new NioEventLoopGroup();
		}
		workerGroup = new NioEventLoopGroup(this.workerThreadPoolSize);
	}
	
	public HttpServer(HttpServerListener listener, int port) throws Exception {
		this.listener = listener;
		this.port = port;
		bindInfo.add(new BindInfo(listener, port, false));
	}
	
	public HttpServer(HttpServerListener listener, int port, boolean ssl) throws Exception {
		this.listener = listener;
		this.port = port;
		bindInfo.add(new BindInfo(listener, port, ssl));
		
		this.ssl = ssl;
		if(this.ssl) {
			SelfSignedCertificate ssc = new SelfSignedCertificate();
			this.sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
//			this.sslCtx = SslContextBuilder.forServer(
//					new File("/home/hubiss/project/2015/IITP-IoT/target/root.crt"), 
//					new File("/home/hubiss/project/2015/IITP-IoT/target/root.key")).build();
		}
	}

	public void addServer(HttpServerListener listener, int port, boolean isSSL) {
		bindInfo.add(new BindInfo(listener, port, isSSL));
	}

	private SslContext getSslContext() throws Exception {
		SelfSignedCertificate ssc = new SelfSignedCertificate();
		return SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
	}
	
	public void runAll() throws Exception {

		List<ServerBootstrap> bootstrapList = new ArrayList<ServerBootstrap>();
		List<ChannelFuture> channelList = new ArrayList<ChannelFuture>();
		try {
			for(int i=0; i<bindInfo.size(); i++) {
				BindInfo bind = bindInfo.get(i);
				ServerBootstrap bootstrap = new ServerBootstrap();
				
				SslContext sslCtx = null;
				if(bind.isSSL()) {
					sslCtx = getSslContext();
				}
				bootstrap.group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class)
				.handler(new LoggingHandler(LogLevel.INFO))
				.childHandler(new HttpServerInitializer(bind.getListener(), sslCtx));

				bootstrap.option(ChannelOption.SO_REUSEADDR, true);
				// 2016.02.05 added..
				bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
				
				ChannelFuture channelFuture = bootstrap.bind(bind.getPort());

				bootstrapList.add(bootstrap);
				channelList.add(channelFuture);
				
				log.debug("Open your web browser and navigate to {}://127.0.0.1:{}/", 
						(bind.isSSL()? "https" : "http"), bind.getPort());
			}
			
		} finally {
			for(int i=0; i<channelList.size(); i++) {
				ChannelFuture channelFuture = channelList.get(i);
				channelFuture.sync().channel().closeFuture().sync();
			}

			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

	public void run() throws Exception {

		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workerGroup)
			.channel(NioServerSocketChannel.class)
			.handler(new LoggingHandler(LogLevel.INFO))
			.childHandler(new HttpServerInitializer(this.listener, this.sslCtx));

			bootstrap.option(ChannelOption.SO_REUSEADDR, true);
			// 2016.02.05 added..
			bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
			
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

		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workerGroup)
			.channel(NioServerSocketChannel.class)
			.handler(new LoggingHandler(LogLevel.INFO))
			.childHandler(new HttpServerInitializer(this.listener, this.sslCtx));

			bootstrap.option(ChannelOption.SO_REUSEADDR, true);
		// 2016.02.05 added..
			bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
			bindChannel = bootstrap.bind(this.port).sync().channel();

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
