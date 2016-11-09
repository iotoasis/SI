package net.herit.iot.onem2m.incse;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.coap.Response;
import org.eclipse.californium.core.server.resources.CoapExchange;
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
import net.herit.iot.message.onem2m.OneM2mRequest.OPERATION;
import net.herit.iot.message.onem2m.OneM2mRequest.RESOURCE_TYPE;
import net.herit.iot.message.onem2m.OneM2mResponse.RESPONSE_STATUS;
import net.herit.iot.onem2m.bind.coap.api.CoapServerListener;
import net.herit.iot.onem2m.bind.coap.codec.CoapRequestCodec;
import net.herit.iot.onem2m.bind.coap.codec.CoapResponseCodec;
import net.herit.iot.onem2m.bind.coap.server.HCoapServer;
import net.herit.iot.onem2m.bind.http.api.HttpServerListener;
import net.herit.iot.onem2m.bind.http.codec.HttpRequestCodec;
import net.herit.iot.onem2m.bind.http.codec.HttpResponseCodec;
import net.herit.iot.onem2m.bind.http.server.HttpServer;
import net.herit.iot.onem2m.bind.http.server.HttpServerHandler;
import net.herit.iot.onem2m.bind.mqtt.api.MqttClientListener;
import net.herit.iot.onem2m.bind.mqtt.api.MqttServerListener;
import net.herit.iot.onem2m.bind.mqtt.client.MqttClientHandler;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.incse.context.OneM2mContext;
import net.herit.iot.onem2m.incse.controller.InterworkingController;
import net.herit.iot.onem2m.incse.controller.NotificationController;
import net.herit.iot.onem2m.incse.controller.RestCommandController;
import net.herit.iot.onem2m.incse.controller.RestNotificationController;
import net.herit.iot.onem2m.incse.facility.CfgManager;
import net.herit.iot.onem2m.incse.facility.CfgManager.RemoteCSEInfo;
import net.herit.iot.onem2m.incse.facility.DatabaseManager;
import net.herit.iot.onem2m.incse.facility.NseManager;
import net.herit.iot.onem2m.incse.facility.NseManager.BINDING_TYPE;
import net.herit.iot.onem2m.incse.facility.QosManager;
import net.herit.iot.onem2m.incse.facility.SeqNumManager;
import net.herit.iot.onem2m.incse.manager.CSEBaseManager;
import net.herit.iot.onem2m.incse.manager.ManagerFactory;

public class InCse implements HttpServerListener, MqttServerListener, CoapServerListener  {
	
	private HttpServer		httpServers;
//	private HttpServer 		httpServer;
//	private HttpServer		httpsServer;
//	private HttpServer 		restServer;
	private RestHandler		restHandler;
	
	private MqttClientHandler	mqttClient;
	
	private HCoapServer		coapServer;
	
	private DatabaseManager	dbManager = DatabaseManager.getInstance();
	private LongPollingManager accessPointManager = LongPollingManager.getInstance();
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
			

			httpServers = new HttpServer(cfgManager.getNettyBossThreadPoolSize(), cfgManager.getNettyWorkerThreadPoolSize());
			httpServers.addServer(this, cfgManager.getHttpServerPort(), false);
			if(cfgManager.getHttpsServerPort() > 0) {
				httpServers.addServer(this, cfgManager.getHttpsServerPort(), true);
			}
			httpServers.addServer(restHandler, cfgManager.getRestServerPort(), false);
			
			mqttClient = MqttClientHandler.getInstance(cfgManager.getCSEBaseCid(), cfgManager.getKeepaliveInterval());
			mqttClient.setListener(this);
			
			
			///////// CoAP 
			coapServer = new HCoapServer(cfgManager.getCSEBaseRid(), cfgManager.getCSEBaseName(),
					this, cfgManager.getCoapServerPort(), cfgManager.getCoapsServerPort());

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
				log.debug("Handled exception", e);
				
			}
			

		} catch (Exception e) {
			log.debug("Handled exception", e);
		}
		
	}
	
	public void start() throws Exception {
		
		if(cfgManager.isSupportMqtt()) {
			mqttClient.connect(cfgManager.getMqttBrokerAddress(), false);
		}
		
		if (cfgManager.isSupportCoap()) {
			coapServer.start();
		}
		
		iwController.run();
		
		httpServers.runAll();
		
	}
	

	/*****************************************************************************
	 *   HTTP BINDING
	 *****************************************************************************/
	
	@Override
	public void channelDisconnected(ChannelHandlerContext ctx) {
		log.debug("channelDisconnected");
	
		String requestId = channelMap.get(ctx.channel().hashCode());
		
		LongPollingManager.getInstance().disconnectedAccessPoint(requestId);
		
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

			reqMessage = HttpRequestCodec.decode(request, ((InetSocketAddress)ctx.channel().remoteAddress()).getHostString());

			//================== INFO log write. ===================
			StringBuilder strbld = new StringBuilder();
			strbld.append(">> RECV REQ ");
			strbld.append("remote:").append(ctx.channel().remoteAddress().toString()).append(" ");
			strbld.append("OP:" ).append(reqMessage.getOperationEnum().Name()).append(" ");
			//* Check Request Type
			if(reqMessage.getOperationEnum().equals(OPERATION.CREATE)) {
				// just add a line at the log
				strbld.append("TY:").append(reqMessage.getResourceTypeEnum().Name()).append(" ");
				
				// bypass authentication
				if( "AE".equals(reqMessage.getResourceTypeEnum().Name()) ){					
					String[] bypassList = cfgManager.getBypass();
					Utils.authenticate( bypassList, request );
				}
			}
			//*/
			strbld.append("TO:").append(reqMessage.getTo()).append(" ");
			strbld.append("RI:").append(reqMessage.getRequestIdentifier());
	
			log.info(strbld.toString());
			//===============================================================
			
			addSession(reqMessage.getRequestIdentifier(), ctx);
			log.debug(reqMessage.toString());
			
			OneM2mContext context = createContext(BINDING_TYPE.BIND_HTTP);

			new OperationProcessor(context).processRequest(reqMessage);

		} catch (OneM2MException e) {
			log.debug("Handled exception", e);

			OneM2mResponse resMessage = new OneM2mResponse(e.getResponseStatusCode(), reqMessage);
			resMessage.setContent(new String(e.getMessage()).getBytes());
			sendHttpResponseMessage(resMessage, ctx);
			
		} catch (Throwable th) {
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
		boolean result = false;
		if(ctx != null) {
			//================== INFO log write. ===================
			StringBuilder strbld = new StringBuilder();
			strbld.append("<< SEND RES ");

			strbld.append("remote:").append(ctx.channel().remoteAddress().toString()).append(" ");
			strbld.append("STATUS:").append(resMessage.getResponseStatusCodeEnum().Value()).append(" ");
			//*
			strbld.append("OP:" ).append(reqMessage.getOperationEnum().Name()).append(" ");
			if(reqMessage.getOperationEnum().equals(OPERATION.CREATE)) {
				strbld.append("TY:").append(reqMessage.getResourceTypeEnum().Name()).append(" ");
			}
			strbld.append("TO:").append(resMessage.getTo()).append(" ");//*/
			strbld.append("RI:").append(resMessage.getRequestIdentifier());

			log.info(strbld.toString());
			//===============================================================

			DefaultFullHttpResponse response = null;

			removeSession(resMessage.getRequestIdentifier());

			try {
				response = HttpResponseCodec.encode(resMessage, CfgManager.getInstance().getHttpVersion());
				response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.CLOSE);

				HttpServerHandler.sendHttpMessage(response, ctx.channel()).
										addListener(ChannelFutureListener.CLOSE).
										addListener(new FilnalEventListener(ctx, true));
				result = true;
			} catch (Exception e) {
				log.debug("Handled exception", e);
				sendError(ctx);
			}
		}	
		return result;
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
			log.debug("Handled exception", e);
			OneM2mResponse resMessage = new OneM2mResponse(RESPONSE_STATUS.INTERNAL_SERVER_ERROR, reqMessage);
			resMessage.setContent("Internal server".getBytes());
			sendMqttMessage(resMessage);
			
		} catch (Throwable th) {
			log.error("RequestMessage decode failed.", th);
		}
	}

	@Override
	public void receiveMqttMessage(OneM2mResponse resMessage) {
		
		log.debug(resMessage.toString());
		
		
	}
	

	/*
	 * (non-Javadoc)
	 * @see net.herit.iot.onem2m.bind.mqtt.api.MqttClientListener#sendMqttMessage(net.herit.iot.message.onem2m.OneM2mRequest)
	 */
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
	
	
	/*****************************************************************************
	 *   COAP BINDING
	 *****************************************************************************/
	private HashMap<String, CoapExchange> coapMap = new HashMap<String, CoapExchange>();
	
	@Override
	public void receiveCoapRequest(CoapExchange exchange) {
		
		log.debug(">> RECVD CoAP MESSAGE:");
		log.debug("Options format=" + exchange.getRequestOptions().getContentFormat());
		
		log.debug("RequestCode=" + exchange.getRequestCode());
		log.debug("RequestPayload=" +exchange.getRequestPayload());
		log.debug("Options=" + exchange.getRequestOptions());
		log.debug("UriPath=" + exchange.getRequestOptions().getUriPathString());
		if (exchange.getRequestPayload() != null) {
			log.debug(exchange.getRequestText());
		}
		
		OneM2mRequest reqMessage = null;
		try {
			reqMessage = CoapRequestCodec.decode(exchange);
			
			coapMap.put(reqMessage.getRequestIdentifier(), exchange);
			log.debug(reqMessage.toString());
			
			OneM2mContext context = createContext(BINDING_TYPE.BIND_COAP);
			
			new OperationProcessor(context).processRequest(reqMessage);

		} catch (OneM2MException e) {
			
			OneM2mResponse resMessage = new OneM2mResponse(e.getResponseStatusCode(), reqMessage);
			resMessage.setContent(new String(e.getMessage()).getBytes());
			sendCoapResponse(resMessage, exchange);
			
		} catch (Throwable th) {
			th.printStackTrace();
			log.error("COAP] RequestMessage decode failed.", th);
			if(reqMessage != null) {
				coapMap.remove(reqMessage.getRequestIdentifier());
			}
			
//			sendError(ctx);
		}
		
		
	}

	@Override
	public boolean sendCoapResponse(OneM2mResponse resMessage) {
		if (resMessage != null) 
			return sendCoapResponse(resMessage, coapMap.get(resMessage.getRequestIdentifier()));
		else return false;
	}
	
	private boolean sendCoapResponse(OneM2mResponse resMessage, CoapExchange exchange) {
		
		if (exchange == null) {
			return false;
		}
		
		coapMap.remove(resMessage.getRequestIdentifier());
		
		try {
			Response response = CoapResponseCodec.encode(resMessage, exchange);
			
			log.debug("<< SEND CoAP MESSAGE:");
			log.debug(response.toString());
			log.debug(response.getPayloadString());
			exchange.respond(response);
			
			return true;
		} catch (Exception e) {
			
			e.printStackTrace();
			
			exchange.respond(ResponseCode.INTERNAL_SERVER_ERROR, "Respose encoding failed");
		}
		
		return false;
	}
	
	/*****************************************************************************
	 *   END COAP BINDING
	 *****************************************************************************/
	
	// start from here
	public static void main(String[] args) throws Exception {
		try {
			new InCse().start();
		} catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

}
