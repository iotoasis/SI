package net.herit.iot.onem2m.incse;

import java.net.InetSocketAddress;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import net.herit.iot.db.mongo.MongoPool;
import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.message.onem2m.OneM2mRequest.RESOURCE_TYPE;
import net.herit.iot.message.onem2m.OneM2mResponse.RESPONSE_STATUS;
import net.herit.iot.onem2m.bind.http.api.HttpServerListener;
import net.herit.iot.onem2m.bind.http.codec.RequestCodec;
import net.herit.iot.onem2m.bind.http.codec.ResponseCodec;
import net.herit.iot.onem2m.bind.http.server.HttpServer;
import net.herit.iot.onem2m.bind.http.server.HttpServerHandler;
import net.herit.iot.onem2m.bind.mqtt.api.MqttClientListener;
import net.herit.iot.onem2m.bind.mqtt.client.MqttClientHandler;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.incse.context.OneM2mContext;
import net.herit.iot.onem2m.incse.controller.InterworkingController;
import net.herit.iot.onem2m.incse.controller.NotificationController;
import net.herit.iot.onem2m.incse.controller.RestCommandController;
import net.herit.iot.onem2m.incse.controller.RestNotificationController;
import net.herit.iot.onem2m.incse.facility.CfgManager;
import net.herit.iot.onem2m.incse.facility.DatabaseManager;
import net.herit.iot.onem2m.incse.facility.NseManager;
import net.herit.iot.onem2m.incse.facility.NseManager.BINDING_TYPE;
import net.herit.iot.onem2m.incse.facility.QosManager;
import net.herit.iot.onem2m.incse.facility.SeqNumManager;
import net.herit.iot.onem2m.incse.manager.CSEBaseManager;
import net.herit.iot.onem2m.incse.manager.ManagerFactory;

public class InCse implements HttpServerListener, MqttClientListener  {
	
	private HttpServer 		httpServer;
	private HttpServer 		restServer;
	private RestHandler		restHandler;
	
	private MqttClientHandler	mqttClient;
	
	private DatabaseManager	dbManager = DatabaseManager.getInstance();
	private AccessPointManager accessPointManager = AccessPointManager.getInstance();
	private NotificationController notificationController = NotificationController.getInstance();
	private RestNotificationController restNotificationController = RestNotificationController.getInstance();
	private RestCommandController restCommandController = RestCommandController.getInstance();
	private CfgManager cfgManager = CfgManager.getInstance();
	private QosManager qosManager = QosManager.getInstance();
	private SeqNumManager seqNumManager = SeqNumManager.getInstance(); 
	
	private InterworkingController iwController = null; 
	
	private Object syncObject = new Object();
	//<channelId, RequestId> mapping
	private HashMap<Integer, String> channelMap = new HashMap<Integer, String>();
	//<RequestId, ChannelHandlerContext> mapping
	private HashMap<String, ChannelHandlerContext> sessionMap = new HashMap<String, ChannelHandlerContext>();
	
	private Logger log = LoggerFactory.getLogger(InCse.class);

	public InCse() {
		try {
			cfgManager.initialize();
			
			MongoPool.getInstance().initialize(cfgManager.getDatabaseHost(), cfgManager.getDatabasePort(), cfgManager.getDatabaseName(), 
												cfgManager.getDatabaseUser(), cfgManager.getDatabasePassword());
			
			restHandler = new RestHandler();
			
			httpServer = new HttpServer(this, cfgManager.getHttpServerPort());	
			restServer = new HttpServer(restHandler, cfgManager.getRestServerPort()); 
			
			mqttClient = MqttClientHandler.getInstance(cfgManager.getCSEBaseCid());
			mqttClient.connect(cfgManager.getMqttBrokerAddress());
			mqttClient.setListener(this);
			
			//logManager.initialize(LoggerFactory.getLogger("IITP-IOT"), null);
			dbManager.initialize(MongoPool.getInstance());
			
			accessPointManager.initialize(createContext(BINDING_TYPE.BIND_HTTP));
			notificationController.initialize(createContext(BINDING_TYPE.BIND_NONE));
			restNotificationController.initialize(createContext(BINDING_TYPE.BIND_NONE));
			restCommandController.initialize(createContext(BINDING_TYPE.BIND_NONE));
			iwController = new InterworkingController(createContext(BINDING_TYPE.BIND_NONE));
			
			seqNumManager.initialize(createContext(BINDING_TYPE.BIND_NONE));
			qosManager.initialize(createContext(BINDING_TYPE.BIND_NONE));

			// create csebase resource if not exist		
			try {
				CSEBaseManager manager = (CSEBaseManager)ManagerFactory.create(RESOURCE_TYPE.CSE_BASE, createContext(BINDING_TYPE.BIND_NONE));
				manager.createIfNotExist();
			} catch (OneM2MException e) {
				e.printStackTrace();
				
			}
			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void start() throws Exception {
				
		iwController.run();
		
		restServer.runAsync();
		
		
		httpServer.run();
		
		// register remoteCSE for interworking with other SP IN-CSE
		
	}
	

	/*****************************************************************************
	 *   HTTP BINDING
	 *****************************************************************************/
	
	@Override
	public void channelDisconnected(ChannelHandlerContext ctx) {
		log.debug("channelDisconnected");
	
		String requestId = channelMap.get(ctx.channel().hashCode());
		
		AccessPointManager.getInstance().disconnectedAccessPoint(requestId);
		
		removeSession(requestId);
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx) {
		log.debug("channelConnected");
		
	}

	@Override
	public void channelRequested(ChannelHandlerContext ctx) {
		log.debug("channelRequested");
		
	}

	private OneM2mContext createContext(BINDING_TYPE bind) {
		return new OneM2mContext(new NseManager( this, bind));
	}
	
	private void addSession(String requestId, ChannelHandlerContext ctx) {
		synchronized(syncObject) {
			sessionMap.put(requestId, ctx);
			channelMap.put(ctx.channel().hashCode(), requestId);
		}
	}
	
	private void removeSession(String requestId) {
		synchronized(syncObject) {
			ChannelHandlerContext channContext =	sessionMap.remove(requestId);
			if(channContext != null) {
				channelMap.remove(channContext.channel().hashCode());
			} else {
				log.debug("removeSession] ChannelHandlerContext is null. can't remove sessionMap");
			}
		}
	}

	@Override
//	public void handleHttpRequest(ChannelHandlerContext ctx,DefaultFullHttpRequest request) {
	public void receiveHttpRequest(ChannelHandlerContext ctx, FullHttpRequest request) {
		log.debug("handleHttpRequest from " + ctx.channel().remoteAddress().toString());
		OneM2mRequest reqMessage = null;
		try {
			reqMessage = RequestCodec.decode(request, ((InetSocketAddress)ctx.channel().remoteAddress()).getHostString());
			
			addSession(reqMessage.getRequestIdentifier(), ctx);
			log.debug(reqMessage.toString());
			
			OneM2mContext context = createContext(BINDING_TYPE.BIND_HTTP);
			
			new OperationProcessor(context).processRequest(reqMessage);

		} catch (OneM2MException e) {
			e.printStackTrace();
			OneM2mResponse resMessage = new OneM2mResponse(e.getResponseStatusCode(), reqMessage);
			resMessage.setContent(new String(e.getMessage()).getBytes());
			sendHttpResponseMessage(resMessage, ctx);
			
		} catch (Throwable th) {
			th.printStackTrace();
			log.error("RequestMessage decode failed.", th);
			if(reqMessage != null) {
				removeSession(reqMessage.getRequestIdentifier());
			}
			
			sendError(ctx);
		}
	}
	
	@Override
	public boolean sendHttpResponse(OneM2mResponse resMessage) {
		ChannelHandlerContext ctx = sessionMap.get(resMessage.getRequestIdentifier());
		return sendHttpResponseMessage(resMessage, ctx);
	}
	
	private boolean sendHttpResponseMessage(OneM2mResponse resMessage, ChannelHandlerContext ctx) {
		
		if(ctx == null) {
			return false;
		}	
		
		DefaultFullHttpResponse response = null;
		
		removeSession(resMessage.getRequestIdentifier());
		
		try {
			response = ResponseCodec.encode(resMessage, CfgManager.getInstance().getHttpVersion());
			response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.CLOSE);
			
			HttpServerHandler.sendHttpMessage(response, ctx.channel()).
									addListener(ChannelFutureListener.CLOSE).
									addListener(new FilnalEventListener(ctx, true));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			sendError(ctx);
		}
		
		return true;
		
	}
	
	private void sendError(ChannelHandlerContext ctx) {
		DefaultFullHttpResponse response = 
				new DefaultFullHttpResponse(CfgManager.getInstance().getHttpVersion(), HttpResponseStatus.INTERNAL_SERVER_ERROR);
		HttpServerHandler.sendHttpMessage(response, ctx.channel()).
								addListener(ChannelFutureListener.CLOSE).
								addListener(new FilnalEventListener(ctx, true));
	}
	
	private class FilnalEventListener implements ChannelFutureListener {

		private ChannelHandlerContext channelContext = null;
		private boolean isClose = false;
		
		public FilnalEventListener(ChannelHandlerContext channelContext, boolean isClose) {
			this.channelContext = channelContext;
			this.isClose = isClose;
		}
		
		@Override
		public void operationComplete(ChannelFuture arg0) throws Exception {
			log.debug("FinalEventListener.operationComplete..");
		}
		
	}
	/*****************************************************************************
	 *   END HTTP BINDING
	 *****************************************************************************/
	

	/*****************************************************************************
	 *   MQTT BINDING
	 *****************************************************************************/
	/*
	 * (non-Javadoc)
	 * @see net.herit.iot.onem2m.bind.mqtt.api.MqttClientListener#messageArrived(net.herit.iot.message.onem2m.OneM2mRequest)
	 */
	@Override
	public void receiveMqttMessage(OneM2mRequest reqMessage) {
		
		log.debug(reqMessage.toString());
		
		try {
			OneM2mContext context = createContext(BINDING_TYPE.BIND_MQTT);
			new OperationProcessor(context).processRequest(reqMessage);
		} catch (Exception e) {
			e.printStackTrace();
			OneM2mResponse resMessage = new OneM2mResponse(RESPONSE_STATUS.INTERNAL_SERVER_ERROR, reqMessage);
			resMessage.setContent("Internal server".getBytes());
			sendMqttMessage(resMessage);
			
		} catch (Throwable th) {
			th.printStackTrace();
			log.error("RequestMessage decode failed.", th);
		}
		
		
	}

	/*
	 * (non-Javadoc)
	 * @see net.herit.iot.onem2m.bind.mqtt.api.MqttClientListener#messageArrived(net.herit.iot.message.onem2m.OneM2mResponse)
	 */
	public void receiveMqttMessage(OneM2mResponse response) {
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.herit.iot.onem2m.bind.mqtt.api.MqttClientListener#sendMqttMessage(net.herit.iot.message.onem2m.OneM2mRequest)
	 */
	@Override
	public boolean sendMqttMessage(OneM2mRequest reqMessage) {
		try {
			log.debug("MQTT BINDING] sendMqttMessage. {}", reqMessage.toString());
			int messageID = mqttClient.sendReqMessage(reqMessage.getCredentialID(), reqMessage);
			return true;
		} catch (Exception e) {
			log.error("MQTT BINDING] sendMqttMessage failed.", e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see net.herit.iot.onem2m.bind.mqtt.api.MqttClientListener#sendMqttMessage(net.herit.iot.message.onem2m.OneM2mResponse)
	 */
	@Override
	public boolean sendMqttMessage(OneM2mResponse resMessage) {
		try {
			log.debug("MQTT BINDING] sendMqttMessage. {}", resMessage.toString());
			int messageID = mqttClient.sendResMessage(resMessage.getRequest().getCredentialID(), resMessage);
			return true;
		} catch (Exception e) {
			log.error("MQTT BINDING] sendMqttMessage failed.", e);
		}
		
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.herit.iot.onem2m.bind.mqtt.api.MqttClientListener#deliveryComplete(int)
	 */
	@Override
	public void completedMqttDelivery(int messageID) {
		log.debug("MQTT BINDING] completed Mqtt delivery. messageID={}", messageID);
	}
	
	/*****************************************************************************
	 *   END MQTT BINDING
	 *****************************************************************************/
	
	
	public static void main(String[] args) throws Exception {
		new InCse().start();
//		String host = "//127.0.0.1:8080";
//		URI uri = new URI(host);
//		System.out.println(uri.toString());
//		System.out.println(uri.getHost());
		
		
	}



}
