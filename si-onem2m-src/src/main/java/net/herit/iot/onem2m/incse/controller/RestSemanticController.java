package net.herit.iot.onem2m.incse.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mRequest.OPERATION;
import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.message.onem2m.OneM2mResponse.RESPONSE_STATUS;
import net.herit.iot.message.onem2m.format.Enums.CONTENT_TYPE;
import net.herit.iot.onem2m.bind.http.client.HttpClient;
import net.herit.iot.onem2m.core.convertor.ConvertorFactory;
import net.herit.iot.onem2m.core.convertor.JSONConvertor;
import net.herit.iot.onem2m.incse.context.OneM2mContext;
import net.herit.iot.onem2m.incse.controller.RestCommandController.ExpiredCmdTimer;
import net.herit.iot.onem2m.incse.facility.CfgManager;
import net.herit.iot.onem2m.resource.ContentInstance;
import net.herit.iot.onem2m.resource.FilterCriteria;
import net.herit.iot.onem2m.resource.Resource;
import net.herit.iot.onem2m.resource.RestCommand;
import net.herit.iot.onem2m.resource.RestSemanticCI;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestSemanticController {

	private final static RestSemanticController INSTANCE = new RestSemanticController();
	
	private Logger log = LoggerFactory.getLogger(RestSemanticController.class);
	
	private String semanticRestURL;
	private OneM2mContext context;
	private HashMap<String, RestCommand> requestMap;
	private Timer expireTimer;
	
	public static RestSemanticController getInstance() {
		return INSTANCE;
	}
	
	public void initialize(OneM2mContext context) {
		
		this.context = context;
		this.requestMap = new HashMap<String, RestCommand>();

		expireTimer = new Timer();
		expireTimer.schedule(new ExpiredCmdTimer(), CfgManager.getInstance().getCommandExpireTimerInterval()*1000);
		
		CfgManager cfgManager = CfgManager.getInstance();
		
		semanticRestURL = cfgManager.getSemanticEngineProtocol() + "://" + cfgManager.getSemanticEngineHost() + ":" + cfgManager.getSemanticEnginePort() + "/";
		
	}
	
	public boolean validate(String strRdf) {
		boolean isValidate = false;
		
		try {
			
			OneM2mResponse resMessage = null;
			
			String semanticEngineUri = this.semanticRestURL + "validate";
			RestSemanticCI param = new RestSemanticCI();
			param.setParamDescriptor(strRdf);
			
			JSONConvertor<RestSemanticCI> cvtRcr = (JSONConvertor<RestSemanticCI>) ConvertorFactory.getJSONConvertor(RestSemanticCI.class, null);
			
			String jsonParam = cvtRcr.marshal(param);
			OneM2mRequest reqMessage = new OneM2mRequest();
			
			reqMessage.setContent(jsonParam.getBytes());
			
			reqMessage.setTo(extractToFromFullUri(semanticEngineUri));
			reqMessage.setOperation(OPERATION.CREATE);
			reqMessage.setContentType(CONTENT_TYPE.JSON);
			
			resMessage = HttpClient.getInstance().sendRequest(semanticEngineUri, reqMessage);
			
			byte[] body = resMessage.getContent();
			String resJson = new String(resMessage.getContent());
			Document resDoc = Document.parse(resJson);
			
			isValidate = resDoc.getBoolean("ret");
			
		} catch (Exception e) {
			log.error("processSemanticOperation", e);
		}
		
		return isValidate;
		
	}
	
	public boolean addDescriptor(String uri, String descriptor, String contType) {
		boolean isSuccess = false;
		
		try {
			
			OneM2mResponse resMessage = null;
			
			if(uri == null || uri.equals("") || descriptor == null || descriptor.equals("") || contType == null || contType.equals("") ) {
				return false;
			}
			
			String semanticEngineUri = this.semanticRestURL + "add";
			RestSemanticCI param = new RestSemanticCI();
			param.setParamUri(uri);
			param.setParamDescriptor(descriptor);
			param.setParamContentType(contType);
			
			JSONConvertor<RestSemanticCI> cvtRcr = (JSONConvertor<RestSemanticCI>) ConvertorFactory.getJSONConvertor(RestSemanticCI.class, null);
			
			String jsonParam = cvtRcr.marshal(param);
			OneM2mRequest reqMessage = new OneM2mRequest();
			
			reqMessage.setContent(jsonParam.getBytes());
			
			reqMessage.setTo(extractToFromFullUri(semanticEngineUri));
			reqMessage.setOperation(OPERATION.CREATE);
			reqMessage.setContentType(CONTENT_TYPE.JSON);
			
			resMessage = HttpClient.getInstance().sendRequest(semanticEngineUri, reqMessage);
			
			byte[] body = resMessage.getContent();
			String resJson = new String(resMessage.getContent());
			Document resDoc = Document.parse(resJson);
			
			isSuccess = resDoc.getBoolean("ret");
			
		} catch (Exception e) {
			log.error("addDescriptor", e);
		}
		
		return isSuccess;
	}
	
	public boolean deleteDescriptor(String uri, String descriptor, String contType) {
		boolean isSuccess = false;
		
		try {
			
			OneM2mResponse resMessage = null;
			
			if(uri == null || uri.equals("") || descriptor == null || descriptor.equals("") || contType == null || contType.equals("") ) {
				return false;
			}
			
			String semanticEngineUri = this.semanticRestURL + "delete";
			RestSemanticCI param = new RestSemanticCI();
			param.setParamUri(uri);
			param.setParamDescriptor(descriptor);
			param.setParamContentType(contType);
			
			JSONConvertor<RestSemanticCI> cvtRcr = (JSONConvertor<RestSemanticCI>) ConvertorFactory.getJSONConvertor(RestSemanticCI.class, null);
			
			String jsonParam = cvtRcr.marshal(param);
			OneM2mRequest reqMessage = new OneM2mRequest();
			
			reqMessage.setContent(jsonParam.getBytes());
			
			reqMessage.setTo(extractToFromFullUri(semanticEngineUri));
			reqMessage.setOperation(OPERATION.CREATE);
			reqMessage.setContentType(CONTENT_TYPE.JSON);
			
			resMessage = HttpClient.getInstance().sendRequest(semanticEngineUri, reqMessage);
			
			byte[] body = resMessage.getContent();
			String resJson = new String(resMessage.getContent());
			Document resDoc = Document.parse(resJson);
			
			isSuccess = resDoc.getBoolean("ret");
			
		} catch (Exception e) {
			log.error("deleteDescriptor", e);
		}
		
		return isSuccess;
	}
	
	public List<Resource> processSemanticOperation(List<Resource> resources, FilterCriteria filter) {
	//public String processSemanticOperation(List<Resource> resources, FilterCriteria filter) {
		
		List<Resource> retResources = new ArrayList<Resource>();
		//String retResult = "";
		
		try {
			OneM2mResponse resMessage = null;
			
			List<String> semanticFilters = filter.getSemanticsFilter();
			List<String> resourceUris = new ArrayList<String>();
			
			for(Resource res : resources) {
				resourceUris.add(res.getUri());
			}
			
			String semanticEngineUri = this.semanticRestURL + "discover";
			
			RestSemanticCI param = new RestSemanticCI();
			param.setFilterList(semanticFilters);
			param.setResourceUris(resourceUris);
			
			JSONConvertor<RestSemanticCI> cvtRcr = (JSONConvertor<RestSemanticCI>) ConvertorFactory.getJSONConvertor(RestSemanticCI.class, null);
			String jsonParam = cvtRcr.marshal(param);
			OneM2mRequest reqMessage = new OneM2mRequest();
			
			reqMessage.setContent(jsonParam.getBytes());
			
			reqMessage.setTo(extractToFromFullUri(semanticEngineUri));
			reqMessage.setOperation(OPERATION.CREATE);
			reqMessage.setContentType(CONTENT_TYPE.JSON);
			
			resMessage = HttpClient.getInstance().sendRequest(semanticEngineUri, reqMessage);
			
			byte[] body = resMessage.getContent();
			String resJson = new String(resMessage.getContent());
			Document resDoc = Document.parse(resJson);
			
			List<String> resUris = (List<String>)resDoc.get("uri");
			Resource r = null;
			for(String uri : resUris) {
				r = new Resource();
				r.setResourceID(uri);
				retResources.add(r);
				r = null;
			}
			
			
		} catch (Exception e) {
			log.error("processSemanticOperation", e);
		}
		
		return retResources;
	}
	
	public String test() {
		String result = null;
		try {
			
			OneM2mResponse resMessage = null;
			
			String semanticEngineUri = this.semanticRestURL + "api.do?main=getGWInfos";
			RestSemanticCI param = new RestSemanticCI();
			param.setParamUri("XXXXXXXXXXXXXXXXXXXXXXXXXX");
			
			JSONConvertor<RestSemanticCI> cvtRcr = (JSONConvertor<RestSemanticCI>) ConvertorFactory.getJSONConvertor(RestSemanticCI.class, null);
			
			String jsonParam = cvtRcr.marshal(param);
			OneM2mRequest reqMessage = new OneM2mRequest();
			
			reqMessage.setContent(jsonParam.getBytes());
			
			reqMessage.setTo(extractToFromFullUri(semanticEngineUri));
			reqMessage.setOperation(OPERATION.CREATE);
			reqMessage.setContentType(CONTENT_TYPE.JSON);
			
			resMessage = HttpClient.getInstance().sendRequest(semanticEngineUri, reqMessage);
			
			byte[] body = resMessage.getContent();
			result = new String(resMessage.getContent());
			
			
		} catch (Exception e) {
			log.error("processSemanticOperation", e);
		}
		
		return result;
		
	}
	
	private void putCommandToMap(String reqId, RestCommand cmd) {
		
		if (cmd.getCreateTime() == null) {
			cmd.setCreateTime(new Timestamp(new Date().getTime()));
		}
		synchronized(this) {
			this.requestMap.put(reqId, cmd);
		}
				
	}
	
	private RestCommand getCommandFromMap(String reqId, boolean delete) {
		
		synchronized(this) {
			RestCommand cmd = this.requestMap.get(reqId);
			if (cmd != null && delete) {
				requestMap.remove(reqId);
			}
			return cmd;
		}
				
	}
	
	private String extractToFromFullUri(String fullUri) {
		
		String to = "/";
		URI url;
		try {
			url = new URI(fullUri);

			String path = url.getPath();
			if (path.length() > 1) {
				int i = fullUri.indexOf(path);
				to = fullUri.substring(i);
			}
			
		} catch (URISyntaxException e) {
			
			log.debug("Handled exception", e);
		}
		return to;
		
	}
	
	private HashMap<String, RestCommand> getRequestMap() {
		
		return this.requestMap;
		
	}
	
	class ExpiredCmdTimer extends TimerTask {
		
		public void run() {
			
			//log.debug("ExpiredCmdTimer run!!!!");
			int expireSecond = CfgManager.getInstance().getCommandTimeout();
			Timestamp currentTime = new Timestamp(new Date().getTime());
			ArrayList<RestCommand> expiredCmd = new ArrayList<RestCommand>();
			
			synchronized(RestCommandController.getInstance()) {
				
				Set<String> keySet = requestMap.keySet();
				Iterator<String> it = keySet.iterator();
				
				List<String> expiredReqIds = new ArrayList<String>();
				
				while (it.hasNext()) {
					String reqId = it.next();
					RestCommand cmd = requestMap.get(reqId);
					Timestamp expireTime = new Timestamp(cmd.getCreateTime().getTime() + expireSecond*1000L);
					
					if (currentTime.after(expireTime)) {
						expiredCmd.add(cmd);
					}
				}			
				
				Iterator<RestCommand> itCmd = expiredCmd.iterator();
				while (itCmd.hasNext()) {
					requestMap.remove(itCmd.next().getCommandId());					
				}

			}
			
			Iterator<RestCommand> it = expiredCmd.iterator();
			while (it.hasNext()) {
				RestCommand cmd = it.next();
				
				try {

					log.debug("##########################################");
					log.debug("Processing expired command: {}", cmd.toString());
					
					Document doc = new Document();
					doc.put("_commandId", cmd.getCommand());
					doc.put("_resultCode", RESPONSE_STATUS.COMMAND_TIMEOUT.Value());
					doc.put("_result", RESPONSE_STATUS.COMMAND_TIMEOUT.toString());
					
					String notiUri = cmd.getNotificationUri();
					OneM2mRequest reqMessage = new OneM2mRequest();
					
					reqMessage.setTo(extractToFromFullUri(notiUri));
					reqMessage.setOperation(OPERATION.CREATE);
					reqMessage.setContentType(CONTENT_TYPE.JSON);
					reqMessage.setContent(doc.toJson().getBytes());
					
//					new HttpClient().process(notiUri, reqMessage);
					HttpClient.getInstance().sendRequest(notiUri, reqMessage);
					
				} catch (Exception e) {
					
					log.debug("Handled exception", e);
					
				}
			}

			expireTimer.schedule(new ExpiredCmdTimer(), CfgManager.getInstance().getCommandExpireTimerInterval()*1000);
		}	
	}

}
