package net.herit.iot.onem2m.bind.mqtt.client;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Timer;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.onem2m.bind.api.ResponseListener;
import net.herit.iot.onem2m.bind.mqtt.api.MqttServerListener;
import net.herit.iot.onem2m.bind.mqtt.codec.MqttRequestCodec;
import net.herit.iot.onem2m.bind.mqtt.codec.MqttResponseCodec;
import net.herit.iot.onem2m.bind.mqtt.util.Utils;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.TimerPingSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MqttClientHandler implements MqttCallback {
	private static MqttClientHandler instance = null;

	private Logger log = LoggerFactory.getLogger(MqttClientHandler.class);
	
	private TimerPingSender pingSender;
	
	public final static String ONEM2M_TOPIC_ID = "/oneM2M";
	public final static String REQ_TOPIC_ID = "/req";
	public final static String RES_TOPIC_ID = "/resp";
	public final static String REQUEST_TOPIC_BASE = ONEM2M_TOPIC_ID + REQ_TOPIC_ID;
	public final static String RESPONSE_TOPIC_BASE = ONEM2M_TOPIC_ID + RES_TOPIC_ID;
	//
	public final static String REG_REQ_TOPIC_ID = "/reg_req";
	public final static String REG_RES_TOPIC_ID = "/reg_resp";
	public final static String REG_REQUEST_TOPIC_BASE = ONEM2M_TOPIC_ID + REG_REQ_TOPIC_ID;
	public final static String REG_RESPONSE_TOPIC_BASE = ONEM2M_TOPIC_ID + REG_RES_TOPIC_ID;
	private String subscribeTopic4Req;
	private String subscribeTopic4Res;
	private String subscribeTopic4RegReq;
	private String subscribeTopic4RegRes;
	private String clientID;

//	private MqttAsyncClient mqttClient;
	private MqttClient mqttClient;
	private MqttConnectOptions connOpt;
	private MqttServerListener serverListener;
	private ResponseListener responseListener;

	private static int keepAliveInterval = 30;
	private int subQoS = 1;
	private int pubQoS = 1;
	
	private String brokerURL;
	private String userName;
	private String password;
	private boolean isSSL;
	
	private boolean RUNNING = false;

	public static MqttClientHandler getInstance(String CES_ID, int keepalive) {
		if (instance == null) {
			instance = new MqttClientHandler(CES_ID);
		}
		keepAliveInterval = keepalive;		
		return instance;
	}

	public static MqttClientHandler getInstance() {
		return instance;
	}

	private MqttClientHandler(String clientId) {
		this.clientID = Utils.getTopicID(clientId);
	}
	
	public void setListener(MqttServerListener listener) {
		this.serverListener = listener;
	}
	
	public void setListener(ResponseListener listener) {
		this.responseListener = listener;
	}

	public void connect(String brokerURL, boolean ssl) throws Exception {
		connect(brokerURL, clientID, null, ssl);
	}

	public void connect(String brokerURL, String userName, String password, boolean ssl) throws Exception {
		this.brokerURL = brokerURL;
		this.isSSL = ssl;
		this.userName = userName;
		this.password = password;
		
		this.connect(brokerURL, userName, password, ssl, false);
		
		log.debug("[MqttClientHandler.connect] connected! CES_ID : "+clientID+", BrokerURL : " + brokerURL);

		setSubscribe();
	}

	//keytool -import -alias mqtt-broker -file cacert.pem  -keypass mykeystorepassword -keystore raw_key_file
	// 		-storetype BKS -storepass mykeystorepassword 
	//		-providerClass org.bouncycastle.jce.provider.BouncyCastleProvider
	//		-providerpath bcprov-ext-jdk15on-1.46.jar
	
	private static final String TRUST_STORE_PASSWORD = "rootPass";
	private static final String KEY_STORE_PASSWORD = "endPass";
	private static final String KEY_STORE_LOCATION = "./certs/keyStore.jks";
	private static final String TRUST_STORE_LOCATION = "./certs/trustStore.jks";
	public SSLSocketFactory getSocketFactory() {
		 
		SSLSocketFactory result = null;
 
		try {
//			KeyStore keystoreTrust = KeyStore.getInstance("JKS");		// Bouncy Castle
//			keystoreTrust.load(mContext.getResources().openRawResource(certificateId),
//					certificatePassword.toCharArray());
			
			KeyStore keystoreTrust = KeyStore.getInstance("JKS");
			InputStream in = new FileInputStream(TRUST_STORE_LOCATION);
			keystoreTrust.load(in, TRUST_STORE_PASSWORD.toCharArray());
 
			TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			trustManagerFactory.init(keystoreTrust);
			SSLContext sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
			result = sslContext.getSocketFactory();
		}
		catch ( Exception ex ) {
			// log exception
		}
 
		return result;
	}
	
	
	private void connect(String brokerURL, String userName, String password, 
			boolean ssl, boolean cleanSession) throws Exception {
		MqttConnectOptions connOpt = new MqttConnectOptions();

		connOpt.setCleanSession(cleanSession);
		connOpt.setKeepAliveInterval(keepAliveInterval);

		if (userName != null) {
			connOpt.setUserName(userName);
		}
		if  (password != null) {
			connOpt.setPassword(password.toCharArray());
		}

		if(ssl) {
//			SSLContext sslContext = SSLContext.getInstance("TLS");
//			TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
//			KeyStore keyStore = KeyStore.getInstance("JKS");
//			InputStream in = new FileInputStream(KEY_STORE_LOCATION);
//			keyStore.load(in, KEY_STORE_PASSWORD.toCharArray());
//			
//			trustManagerFactory.init(keyStore);
//			sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
//			 
//			connOpt.setSocketFactory(sslContext.getSocketFactory());
			
			
//			connOpt.setSocketFactory(SSLSocketFactory.getDefault());
		}
		
		
//		mqttClient = new MqttAsyncClient(brokerURL, clientID);
		mqttClient = new MqttClient(brokerURL, clientID);
		mqttClient.setCallback(this);
		mqttClient.connect(connOpt);
//		mqttClient.connect(connOpt, new IMqttActionListener() {
//
//			@Override
//			public void onSuccess(IMqttToken asyncActionToken) {
//				// TODO Auto-generated method stub
//				try {
//					setSubscribe();
//				} catch (Exception e) {
//					log.error("Subscription exception: ", e);
//				}
//			}
//
//			@Override
//			public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//		});
		log.debug("MQTT] connected to mqtt borker.");
	
//		pingSender = new TimerPingSender();
//		pingSender.schedule((keepAliveInterval-5)*1000);
//		pingSender.start();
		
//		MqttDeliveryToken token = new MqttDeliveryToken(getClientId());
//		MqttPingReq pingMsg = new MqttPingReq();
//		mqttClient. sendNoWait(pingMsg, token);
		
	}
	
	public void connect2(String brokerURL, boolean ssl) throws Exception {
		connect2(brokerURL, clientID, null, ssl);
	}
	
	public void connect2(String brokerURL, String userName, String password, boolean ssl) throws Exception {
		
		connect(brokerURL, userName, password, ssl, true);
		disconnect();
		connect(brokerURL, userName, password, ssl, false);
		
		setSubscribe();
	}
	
	private void disconnect() throws Exception {
		
		
		if(mqttClient != null) {
			mqttClient.disconnect();
		} else {
			log.error("mqttClient is null");
		}
	}

	public void terminator() throws Exception {
		disconnect();
//		connect(this.brokerURL, this.userName, this.password, this.isSSL, true);
//		disconnect();
	}
	
	public String getClientId() {
		return this.clientID;
	}

	@Override
	public void connectionLost(Throwable arg0) {
		log.debug("MQTT >>>> CONNECTION LOST..");
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		// TODO Auto-generated method stub
		log.debug("[MqttClientHandler.deliveryComplete] deliveryComplete, topic : " +  Arrays.toString(token.getTopics()));

		if (serverListener != null)
		{
			serverListener.completedMqttDelivery(token.getMessageId());
		}
	}

	@Override
	public void messageArrived(final String topic, final MqttMessage message) throws Exception {
		// TODO Auto-generated method stub
		log.debug("[MqttClientHandler.messageArrived] messageArrived, topic : " + topic);

		try {

			byte[] payloadMessage = message.getPayload();
			log.debug("RCVD({}): {}", message.isRetained(), new String(payloadMessage, "UTF-8"));
			
			if(topic.startsWith(REQUEST_TOPIC_BASE)) {
				if (serverListener != null) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							String[] IDs = Utils.getOneM2mIDsFromTopic(topic, REQUEST_TOPIC_BASE);

							try {
								OneM2mRequest reqMessage = MqttRequestCodec.decode(message.getPayload());
								reqMessage.setCredentialID(IDs[0]);
								serverListener.receiveMqttMessage(reqMessage);
							} catch (Exception e) {
								// TODO: send error Response message.
								log.error("request message decode exception: ", e);
							}
						}
					}).start();
					
				}
			} else if(topic.startsWith(RESPONSE_TOPIC_BASE))  {
				if (responseListener != null) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								OneM2mResponse resMessage = MqttResponseCodec.decode(message.getPayload());
	
								responseListener.receiveResponse(resMessage);
							} catch(Exception e) {
								log.error("response message decode exception: ", e);
							}
						}
						
					}).start();
				}
				if (serverListener != null) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								OneM2mResponse resMessage = MqttResponseCodec.decode(message.getPayload());
	
								serverListener.receiveMqttMessage(resMessage);
							} catch(Exception e) {
								log.error("response message decode exception: ", e);
							}
						}
						
					}).start();
				}
			}
//			else {
//					log.warn("Not subscribed topic.(" + topic + ")");
//				}
//			}
		}
		catch (Exception e) {
			log.debug("Handled exception", e);
		}
	}

	private void setSubscribe() throws Exception {
		// blocked in 2017-12-04
		//subscribeTopic4Req = REQUEST_TOPIC_BASE + "/+/" + clientID;
		//subscribeTopic4Res = RESPONSE_TOPIC_BASE + "/" + clientID + "/+";
		subscribeTopic4Req = REQUEST_TOPIC_BASE + "/+/" + clientID + "/+";
		subscribeTopic4Res = RESPONSE_TOPIC_BASE + "/" + clientID + "/+/+";
		
		subscribeTopic4RegReq = REG_REQUEST_TOPIC_BASE + "/+/" + clientID + "/+";
		subscribeTopic4RegRes = REG_RESPONSE_TOPIC_BASE + "/" + clientID + "/+/+";
		
		mqttClient.subscribe(subscribeTopic4Req, subQoS);
		log.debug("SUBSCRIBED. TOPIC = " + subscribeTopic4Req);
		Thread.sleep(50);
		mqttClient.subscribe(subscribeTopic4Res, subQoS);
		log.debug("SUBSCRIBED. TOPIC = " + subscribeTopic4Res);
		//
		Thread.sleep(50);
		mqttClient.subscribe(subscribeTopic4RegReq, subQoS);
		log.debug("SUBSCRIBED. TOPIC = " + subscribeTopic4RegReq);
		Thread.sleep(50);
		mqttClient.subscribe(subscribeTopic4RegRes, subQoS);
		log.debug("SUBSCRIBED. TOPIC = " + subscribeTopic4RegRes);
	}
	
	private void unsetSubscribe() throws Exception {
		mqttClient.unsubscribe(subscribeTopic4Res);
		//
		mqttClient.unsubscribe(subscribeTopic4RegRes);
	}
	
	public int sendReqMessage(OneM2mRequest reqMessage) throws Exception {
		return sendReqMessage(reqMessage.getTo(), reqMessage);
	}
	
	public int sendResMessage(OneM2mResponse resMessage) throws Exception {
		return sendResMessage(resMessage.getTo(), resMessage);
	}
	
	/*
	 * targetID: 
	 */
	public int sendReqMessage(String targetID, OneM2mRequest reqMessage) throws Exception {
		
		targetID = Utils.getTopicID(targetID);
		if(targetID == null) throw new Exception("Invalid targetID");

		String strTopic = REQUEST_TOPIC_BASE + "/" + clientID + "/" + targetID + "/json";
		
		byte[] byteContents = MqttRequestCodec.encode(reqMessage);
		
		return publish(strTopic, byteContents);
		
	}
	
	public int sendResMessage(String targetID, OneM2mResponse resMessage) throws Exception {
		targetID = Utils.getTopicID(targetID);
		if(targetID == null) throw new Exception("Invalid targetID");
		
		//String strTopic = RESPONSE_TOPIC_BASE + "/" + targetID + "/" + clientID;
		String strTopic = RESPONSE_TOPIC_BASE + "/" + targetID + "/" + clientID + "/json";
		
		byte[] byteContents = MqttResponseCodec.encode(resMessage);

		return publish(strTopic, byteContents);
	}
	
	public int sendReqMessage(String targetID, String message) throws Exception {
		targetID = Utils.getTopicID(targetID);
		if(targetID == null) throw new Exception("Invalid targetID");
		
		String strTopic = REQUEST_TOPIC_BASE + "/" + clientID + "/" + targetID + "/json";

		return publish(strTopic, message.getBytes());

	}
	
	public int sendResMessage(String targetID, String message) throws Exception {
		targetID = Utils.getTopicID(targetID);
		if(targetID == null) throw new Exception("Invalid targetID");
		
		String strTopic = RESPONSE_TOPIC_BASE + "/" + targetID + "/" + clientID + "/json";
		
		return publish(strTopic, message.getBytes());
		
	}
	
 
	private int publish(String strTopic, byte[] contents) throws Exception {
		
		log.debug("PUBLISH] topic=" + strTopic + ", contents=" + new String(contents, "UTF-8"));
		
//		MqttTopic mqttTopic = mqttClient.getTopic(strTopic);

		MqttMessage message = new MqttMessage(contents);
		message.setQos(pubQoS);
		message.setRetained(false);

		mqttClient.publish(strTopic, message);
		
		return 1;
		
//		MqttDeliveryToken token = null;
//		token = mqttTopic.publish(message);
//		token.waitForCompletion();
//
//		log.debug("[MqttClientHandler.publish] publish. topic : " + strTopic);
//		
//		return token.getMessageId();
	}
	
}
