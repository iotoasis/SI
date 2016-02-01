package net.herit.iot.onem2m.incse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.message.onem2m.OneM2mResponse.RESPONSE_STATUS;
import net.herit.iot.message.onem2m.format.Enums.CONTENT_TYPE;
import net.herit.iot.onem2m.core.util.LogManager;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.incse.context.OneM2mContext;
import net.herit.iot.onem2m.incse.facility.NseManager;
import net.herit.iot.onem2m.incse.manager.dao.PollingChannelDAO;
import net.herit.iot.onem2m.resource.Naming;
import net.herit.iot.onem2m.resource.PollingChannel;
import net.herit.iot.onem2m.resource.Request;
import net.herit.iot.onem2m.resource.RequestPrimitive;

public class AccessPointManager {

//	private static final String DATE_FORMAT = "yyyyMMdd'T'HHmmss";
	private static final long REQUEST_EXPIRATION_DURATION_TIME = 180000;	// 180 seconds
	
	private static AccessPointManager INSTANCE = new AccessPointManager();
	
	private Logger log = LoggerFactory.getLogger(AccessPointManager.class);
	
	private AccessPointManager() {}
	
	public static AccessPointManager getInstance() {
		return INSTANCE;
	}
	
	public void initialize(OneM2mContext ctx) {
		this.ctx = ctx;
		
		//logManager = ctx.getLogManager();
		//dbManager = ctx.getDatabaseManager();
		nseManager = ctx.getNseManager();
		
		//log = logManager.getLogger();
	}
	
	private Object syncObject = new Object();
	private HashMap<String, String> mappingMap = new HashMap<String, String>();	// requestId and pollingChannel_Uri mapping.
	private HashMap<String, AccessPoint> accessPointMap = new HashMap<String, AccessPoint>(); // pollingChannel_Uri, AccessPoint mapping.
	
	private Object syncQueue = new Object();
	private HashMap<String, List<RequestPrimitive>> requestQueue = new HashMap<String, List<RequestPrimitive>>();
	
	private OneM2mContext ctx;
	//private LogManager logManager;
	//private DatabaseManager dbManager;
	private NseManager nseManager;
	
	/*** PollingChannel Resource Management ***/
	private HashMap<String, Timer> pollingChannelExpireMap = new HashMap<String, Timer>(); // pollingChannel _uri, Timer mapping.
	
	
	private void addRequestQueue(String pollingChannelUri, RequestPrimitive request) {
		log.debug("addRequestQueue");
		synchronized(syncQueue) {
			List<RequestPrimitive> requests = requestQueue.get(pollingChannelUri);
			if(requests == null) {
				requests = new ArrayList<RequestPrimitive>();
				
				requests.add(request);
				requestQueue.put(pollingChannelUri, requests);
			} else {
				requests.add(request);
			}
		}
	}
	
	private RequestPrimitive getRequestQueue(String pollingChannelUri) {
		RequestPrimitive request = null;
		synchronized(syncQueue) {
			List<RequestPrimitive> requests = requestQueue.get(pollingChannelUri);
			if(requests != null) {
				request = requests.remove(0);
				
				if(requests.size() <= 0) {
					requestQueue.remove(pollingChannelUri);
				}
			}
		}
		
		return request;
	}
	
	public void sendRequest(String pollingChannelUri, RequestPrimitive request) throws OneM2MException {
		AccessPoint accessPoint = getAccessPoint(pollingChannelUri);
		if(accessPoint == null) {
			log.debug("accessPoint is null");
			addRequestQueue(pollingChannelUri, request);
			
			return;
		}

		sendRequest(pollingChannelUri, accessPoint.getOneM2mReqeuest(), request);
		
	}
	
	private void sendRequest(String pollingChannelUri, OneM2mRequest reqMessage, RequestPrimitive request) throws OneM2MException {
		
		try {
			log.debug("AccessPoint sendRequest] pollingChannelUri: {}", pollingChannelUri);
			
			OneM2mResponse resMessage = new OneM2mResponse(RESPONSE_STATUS.OK, reqMessage);
			resMessage.setContentType(CONTENT_TYPE.XML);
			resMessage.setContentObject(request);
			nseManager.sendResponseMessage(resMessage	);
			
			removeAccessPoint(pollingChannelUri);
		} catch(Exception e) {
			throw new OneM2MException(RESPONSE_STATUS.INTERNAL_SERVER_ERROR, "sendRequest failed through pollingChannel");
		}
	}
	
	public void newAccessPoint(OneM2mRequest reqMessage) throws OneM2MException {
		String pollingChannelUri = reqMessage.getTo();
		String originator = reqMessage.getFrom();
		
		String uri = pollingChannelUri.substring(0, pollingChannelUri.length()-Naming.POLLINGCHANNELURI_SN.length()-1);
		log.debug("_uri: {}", uri);
		
		//if(!pollingChannelUri.startsWith(originator)) {
		//	throw new OneM2MException(RESPONSE_STATUS.ACCESS_DENIED, "Origiantor is not the creator of the parent <pollingChannel> resource");
		//}
		
		PollingChannel pollingChann = (PollingChannel)new PollingChannelDAO(this.ctx).retrieve(uri, null);
		if(pollingChann == null) {
			throw new OneM2MException(RESPONSE_STATUS.ACCESS_DENIED, "PollingChannel resource not exist");
		}
			
		RequestPrimitive request = getRequestQueue(pollingChannelUri);
		if(request != null) {
			sendRequest(pollingChannelUri, reqMessage, request);
			
			return;
		}
		
		try {
			long expireTime = 0L;
			String strExpirationTime = reqMessage.getRequestExpirationTimestamp();
			long currentTime = System.currentTimeMillis();
			if(strExpirationTime == null) {
				expireTime = currentTime + REQUEST_EXPIRATION_DURATION_TIME;
			} else {
				expireTime = new SimpleDateFormat(Naming.DATE_FORMAT).parse(strExpirationTime).getTime();
			}
			
			log.debug("expirationTime: {} {}", expireTime, new Date(expireTime));
			if((currentTime > expireTime)) {
				throw new OneM2MException(RESPONSE_STATUS.INVALID_ARGUMENTS, "ExpirationTime is invalid");
			}

			addAccessPoint(reqMessage, expireTime); //pollChann.getExpirationTime());
		} catch(Exception e) {
			throw new OneM2MException(RESPONSE_STATUS.INTERNAL_SERVER_ERROR, "New AccessPoint creation failed");
		}
	
	}
	
	private AccessPoint getAccessPoint(String pollingChannelUri) {
		AccessPoint accessPoint = null;
		synchronized(syncObject) {
			accessPoint = accessPointMap.get(pollingChannelUri);
		}
		
		return accessPoint;
	}
	
	private boolean addAccessPoint(OneM2mRequest reqMessage, long expireTime) throws Exception {
		log.debug("addAccessPoint");
		synchronized(syncObject) {
			String pollchann_uri = reqMessage.getTo();
			String reqId = reqMessage.getRequestIdentifier();
			Timer timer = new Timer(pollchann_uri);
			timer.schedule(new TimeoutTask(pollchann_uri, pollchann_uri), new Date(expireTime));
			
			accessPointMap.put(pollchann_uri, new AccessPoint(reqId, pollchann_uri, timer, reqMessage));
			mappingMap.put(reqId, pollchann_uri);
		}
		
		return true;
	}
	
	private void removeAccessPoint(String pollingChannelUri) {
		log.debug("removeAccessPoint");
		synchronized(syncObject) {
			AccessPoint accPoint = accessPointMap.remove(pollingChannelUri);
			if(accPoint != null) {
				accPoint.cancelSchedule();
				mappingMap.remove(accPoint.getRequestId());
			} else {
				log.debug("AccessPoint is null. can't remove mappingMap.");
			}
		}
	}
	
	public void disconnectedAccessPoint(String requestId) {
		String pollingChannelUri = mappingMap.get(requestId);
		removeAccessPoint(pollingChannelUri);
	}
	
	private class AccessPoint {
		private String pollingChannelUri;
		private String RequestId;
		private Timer timer;
		private OneM2mRequest reqMessage;
		
		public  AccessPoint(String requestId, String pollingChannelUri, Timer timer, OneM2mRequest reqMessage) {
			this.RequestId = requestId;
			this.pollingChannelUri = pollingChannelUri;
			this.timer = timer;
			this.reqMessage = reqMessage;
		}
		
		public String getRequestId() {
			return this.RequestId;
		}
		
		public String getPollingChannelUri() {
			return this.pollingChannelUri;
		}
		
		public OneM2mRequest getOneM2mReqeuest() {
			return this.reqMessage;
		}
		
		public void cancelSchedule() {
			timer.cancel();
		}
	}
	
	
	private class TimeoutTask extends TimerTask {

		private String requestId;
		private String pollingChannelUri;
		
		public TimeoutTask(String requestId, String pollingChannelUri) {
			this.requestId = requestId;
			this.pollingChannelUri = pollingChannelUri;
		}
		
		@Override
		public void run() {
			log.debug("TimeoutTask] map key: " + requestId + ", pollingChannelUri: " + pollingChannelUri);
			AccessPoint accPoint = accessPointMap.get(pollingChannelUri);
			removeAccessPoint(pollingChannelUri);
			
			OneM2mResponse resMessage = new OneM2mResponse(RESPONSE_STATUS.REQUEST_TIMEOUT, accPoint.getOneM2mReqeuest());
			resMessage.setContent("TIME OUT".getBytes());
			
			nseManager.sendResponseMessage(resMessage	);
		}
	}
	
	private class PollingChannelExpireTask extends TimerTask {

		private String uri;
		OneM2mContext ctx;
		
		public PollingChannelExpireTask(OneM2mContext ctx, String uri) {
			this.ctx = ctx;
			this.uri = uri;
		}
		
		@Override
		public void run() {
			try {
				new PollingChannelDAO(this.ctx).delete(uri);
			} catch(OneM2MException e) {
				log.error(e.getMessage());
			}
		}
	}
	
	public void addPollingChannelExpireTask(String uri, String expirationDate) throws OneM2MException {
		if(expirationDate == null) return;
		
		try {
			Timer timer = new Timer(uri);
			timer.schedule(new PollingChannelExpireTask(this.ctx, uri),
					new SimpleDateFormat(Naming.DATE_FORMAT).parse(expirationDate).getTime());
			
			pollingChannelExpireMap.put(uri, timer);
		} catch(Exception e) {
			throw new OneM2MException(RESPONSE_STATUS.INTERNAL_SERVER_ERROR, "Internal Server error");
		}
		
		log.debug("addPollingChannelExpireTask] uri={}, expirationDate={}", uri, expirationDate);
	}
	
	public void removePollingChannelExpireTask(String uri) {
		Timer timer = pollingChannelExpireMap.remove(uri);
		if(timer != null) {
			int cnt = timer.purge();
			
			log.debug("removePollingChannelExpireTask] remove task count is {}", cnt);
		}
	}
	
	
	public static void main(String[] args) throws Exception {
		String strDate = "20150603T122321";
		long time = new SimpleDateFormat("yyyyMMdd'T'HHmmss").parse(strDate).getTime();

		Date date = new Date(time);
		System.out.println(date.toString());
	}
}
