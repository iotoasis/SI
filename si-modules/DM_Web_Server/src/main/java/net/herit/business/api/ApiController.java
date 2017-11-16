package net.herit.business.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.herit.business.api.service.ApiHdmDAO;
import net.herit.business.api.service.ApiHdpDAO;
import net.herit.business.api.service.DatabaseService;
import net.herit.business.api.service.FileUploadVO;
import net.herit.business.api.service.LWM2MApiService;
import net.herit.business.api.service.OneM2MApiService;
import net.herit.business.api.service.OpenApiService;
import net.herit.business.api.service.TR069ApiService;
import net.herit.business.device.service.DeviceModelVO;
import net.herit.business.device.service.DeviceVO;
import net.herit.business.device.service.ExtMoProfileVO;
import net.herit.business.device.service.MoProfileVO;
import net.herit.business.device.service.ParameterVO;
import net.herit.business.firmware.service.FirmwareDAO;
import net.herit.business.firmware.service.FirmwareService;
import net.herit.business.lwm2m.Util;
import net.herit.business.protocol.HttpConnector;
import net.herit.business.protocol.tr069.CurlOperation;
import net.herit.common.conf.HeritProperties;
import net.herit.common.exception.UserSysException;
import net.herit.common.model.ErrorVO;
import net.herit.common.model.HeritFormBasedFileVO;
import net.herit.common.util.HeritFileUploadUtil;
import net.herit.common.util.PagingUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.oreilly.servlet.MultipartRequest;

@Controller
@RequestMapping(value = "/api")
public class ApiController {

	@Resource(name = "DatabaseService")
	private DatabaseService databaseService;
	@Resource(name = "OpenApiService")
	private OpenApiService openApiService;
	@Resource(name = "LWM2MApiService")
	private LWM2MApiService lwm2mApiService;
	@Resource(name = "TR069ApiService")
	private TR069ApiService tr069ApiService;
	@Resource(name = "ApiHdmDAO")
	private ApiHdmDAO hdmDAO;
	@Resource(name="ApiHdpDAO")
	private ApiHdpDAO hdpDAO;
	@Resource(name = "FirmwareService")
	private FirmwareService firmwareService;
	@Resource(name = "FirmwareDAO")
	private FirmwareDAO fdao;

	/** 이미지경로를 위한 URL */
	private final String hostUrl = HeritProperties
			.getProperty("Globals.hostUrl");
	/** 톰캣 위치 지정 */
	private final String tomcatDir = HeritProperties
			.getProperty("Globals.tomcatDir");
	/** 첨부파일 위치 지정 */
	private final String uploadDir = HeritProperties
			.getProperty("Globals.uploadDir");

	private final String firmwareDir = HeritProperties
			.getProperty("Globals.firmwareDir");

	private final long maxFileSize = 1024 * 1024 * 100;

	public final static String INVALID_PARAM = "101";
	public final static String DUPLICATED_DEVICE_ID = "102";

	@ResponseBody
	@RequestMapping(value = "/{system}/{domain}/{data}/{operation}")
	public Map<String, Object> get(@PathVariable("system") String system,
			@PathVariable("domain") String domain,
			@PathVariable("data") String data,
			@PathVariable("operation") String operation,
			HttpServletRequest request) throws Exception {

		System.out.println("##### get in! "+system+"/"+domain+"/"+data+"/"+operation);
		Map<String, Object> response = new HashMap<String, Object>();

		HashMap<String, Object> param = new HashMap<String, Object>();

		// URL Sample: /hdp/deviceModel/info/get
		// URL Sample: /hdm/device/info/list
		/*
		 * HttpSession session = request.getSession(false); if(session != null){
		 * //페이지 권한 확인 GroupAuthorization requestAuth = (GroupAuthorization)
		 * session.getAttribute("requestAuth");
		 * if(!requestAuth.getAuthorizationDBRead().equals("1")){
		 * 
		 * response.put("result", 505); response.put("errorCode", 505);
		 * response.put("content", "Permission denied");
		 * response.put("parameter", param); return response; } }
		 */
		Enumeration names = request.getParameterNames();

		while (names.hasMoreElements()) {
			String name = (String) names.nextElement();
			String value = request.getParameter(name);
			param.put(name, value);
		}
		System.out.println(param);

		try {

			Map<String, Object> content = databaseService.execute(system,
					domain, data, operation, param);
			System.out.println(content);

			response.put("result", 0);
			response.put("errorCode", 0);
			response.put("content", content);
			response.put("parameter", param);

		} catch (UserSysException ex) {
			ex.printStackTrace();
			ErrorVO err = ex.getErrorVO();
			response.put("result", 1);
			response.put("errorCode", err.getErrorCode());
			response.put("content", err.getErrorMessage());
			response.put("parameter", param);

		} catch (Exception ex) {
			ex.printStackTrace();
			response.put("result", 1);
			response.put("errorCode", -1);
			response.put("content", ex.toString());
			response.put("exception", ex);
			response.put("parameter", param);
		}

		return response;
	}

	@SuppressWarnings("unchecked")
	@ResponseBody
    @RequestMapping(value = "/{system}/{domain}/{operation}")
	public Map<String, Object> execute(
			@RequestBody String bodyString,
			@PathVariable("system") String system,
			@PathVariable("domain") String domain,
			@PathVariable("operation") String operation,
			HttpServletRequest request) throws Exception {
		
		System.out.println("##### get in222! "+system+"/"+domain+"/"+operation);

		Map<String, Object> response = new HashMap<String, Object>();
		HashMap<String, String> param = new HashMap<String, String>();
		/*
		 * HttpSession session = request.getSession(false); if(session != null){
		 * //페이지 권한 확인 GroupAuthorization requestAuth = (GroupAuthorization)
		 * session.getAttribute("requestAuth");
		 * if(!requestAuth.getAuthorizationDBRead().equals("1")){
		 * 
		 * response.put("result", 505); response.put("errorCode", 505);
		 * response.put("content", "Permission denied");
		 * response.put("parameter", param); return response; } }
		 */

		Enumeration names = request.getParameterNames();

		while (names.hasMoreElements()) {
			String name = (String) names.nextElement();
			String value = request.getParameter(name);
			param.put(name, value);
		}

		//TODO:body 안에 있는 정보를 가져와야 햄.
		HashMap<String, Object> contentMap = new ObjectMapper().readValue(bodyString, HashMap.class);

		String deviceId = contentMap.containsKey("d") ? (String) contentMap.get("d") : null;
		if (deviceId == null || "".equals(deviceId)) {
			/** oneM2M 서버에서 검색 */
			if (operation.equals("discovery")) {
				String sn = (String) contentMap.get("sn");
				
				OneM2MApiService oneM2MSvc = OneM2MApiService.getInstance();
				HashMap<String, Object> content = oneM2MSvc.discovery(operation, sn);
				
				ArrayList<HashMap<String, String>> eList = new ArrayList<HashMap<String,String>>();
				LinkedHashMap<String, Object> snContent = new LinkedHashMap<String, Object>();

				snContent.put("o", "d");
				snContent.put("r", "200");//XXX
				snContent.put("devId", content);
				
				response.put("result", 0);
				response.put("content", snContent);
				System.out.println("response (discovery) : " + response.toString());
				return response;
			}
		}
		
		System.out.println("deviceId:" + deviceId);
		System.out.println("WHY");
		DeviceVO deviceInfo = hdmDAO.getDeviceInfo(deviceId);
		
		System.out.println("deviceInfo is null? : " + deviceInfo == null);

		int dmType = deviceInfo.getDmType();
		String extDeviceId = deviceInfo.getExtDeviceId();
		String modelName = deviceInfo.getModelName();
		DeviceModelVO deviceModelInfo = hdpDAO.getDeviceModelId(modelName);
		String deviceModelId = deviceModelInfo.getId();
		System.out.println("dmType : " + dmType);
		System.out.println("extDeviceId : " + extDeviceId);
		System.out.println("modelName : " + modelName);
		System.out.println("deviceModelId : " + deviceModelId);
		
		System.err.println("#########################################");
		System.err.println("   TEST :: dmType :: "+dmType);
		System.err.println("#########################################");
		
		String body = null;
		if (dmType == 0) {
			try {

				// body = getBody(request);
				HashMap<String, Object> content = openApiService.execute(operation, bodyString);

				System.err.println(content.toString());
				if (content.get("exception") == null
						&& (Integer) content.get("status") == 200) {
					response.put("result", 0);
					response.put("errorCode", content.get("status"));
					response.put("content", content.get("json"));
				} else if (content.get("exception") == null
						&& (Integer) content.get("status") != 200) {
					response.put("result", 1);
					response.put("errorCode", content.get("status"));
					response.put("content", content.get("json"));
					response.put("requestBody", body);
					response.put("responseBody", content.get("body"));
				} else {
					response.put("result", 0);
					response.put("errorCode", content.get("status"));
					response.put("content", content.get("exception").toString());
					// response.put("exception", content.get("exception"));
					response.put("requestBody", body);
					response.put("responseBody", content.get("body"));
					// response.put("json", content.get("json"));
				}

			} catch (UserSysException ex) {
				ErrorVO err = ex.getErrorVO();
				response.put("result", 1);
				response.put("errorCode", err.getErrorCode());
				response.put("content", err.getErrorMessage());
				response.put("parameter", param);

			} catch (Exception ex) {
				response.put("result", 1);
				response.put("errorCode", -1);
				response.put("content", ex.toString());
				response.put("exception", ex);
				response.put("requestBody", body);
			}
		} else if (dmType == 1) {//oneM2M서버
			try {
				//body data
				ArrayList<HashMap<String, String>> resourceList = contentMap.containsKey("e") ? (ArrayList<HashMap<String,String>>) contentMap.get("e") : null;
				if (resourceList == null || resourceList.isEmpty()) {
					// 예외처리
				}
				System.out.println("e:" + resourceList.toString());
				
				ArrayList<String> resUris = new ArrayList<String>();
				String executeCont = null;
				for (int i=0; i < resourceList.size(); i++) {
					System.out.println("contentList:" + resourceList.get(i));
					
					HashMap<String, String> resUri = resourceList.get(i);
					String resource = resUri.get("n");
					System.out.println("resource:" + resource);
					
					//execute 명령에 대한 웹 제어 데이터
					executeCont = resUri.get("sv");
					System.out.println("executeCont:" + executeCont);
					
					MoProfileVO moProfileInfo = hdpDAO.getMoProfileId(deviceModelId, resource);
					String moProfileId = moProfileInfo.getId();
					System.out.println("moProfileId : " + moProfileId);
					
					ExtMoProfileVO extInfo = hdpDAO.getResUriNameInfo(moProfileId);
					System.out.println("extInfo:" + extInfo.getExtResourceUri());
					
					String resourceUri = extDeviceId +"/"+ extInfo.getExtResourceUri();
					System.out.println("resourceUri:" + resourceUri);
					
					resUris.add(resourceUri);
				}
				
				System.out.println("resUris:" + resUris.toString());
				
				//oneM2M 서버
				OneM2MApiService oneM2MSvc = OneM2MApiService.getInstance();
				HashMap<String, Object> content = oneM2MSvc.execute(operation, resUris, extDeviceId, executeCont);
				
				//dm서버와 같은 데이터 형식으로 만들어서 구현
				ArrayList<String> to = new ArrayList<String>(); 
				ArrayList<String> ct = new ArrayList<String>(); 
 				for(int j=0; j < resUris.size(); j++) {
					to.add((String) content.get(resUris.get(j)));
					ct.add((String) content.get(resUris.get(j)+"_"+j));
					System.out.println(resUris.get(j) + content.get(resUris.get(j)));
					System.out.println(resUris.get(j)+"_"+j + content.get(resUris.get(j)+"_"+j));
				}
				
				ArrayList<HashMap<String, String>> eList = new ArrayList<HashMap<String,String>>();
				
				LinkedHashMap<String, Object> resContent = new LinkedHashMap<String, Object>();
				HashMap<String, String> resDatas = new HashMap<String, String>();

				resContent.put("o", (String)content.get("o"));
				resContent.put("r", "200");//XXX
				for(int k=0; k < resourceList.size(); k++) {
					resDatas = resourceList.get(k);
					String resData = resDatas.get("n");
					HashMap<String, String> dataMap = new HashMap<String, String>();
					dataMap.put("n", resData);
					dataMap.put("sv", to.get(k));
					dataMap.put("ti", ct.get(k));
					eList.add(dataMap);
				}
				resContent.put("e", eList);
				/*ObjectMapper mapper = new ObjectMapper();
				Object json = mapper.writeValueAsString(resContent);*/
				response.put("result", 0);
				response.put("errorCode", "200");
				response.put("content", resContent);
				
				
				//System.out.println("=================" + response.toString());

			} catch (Exception ex) {
				System.out.println("ex :" + ex);
			}
		} else if (dmType == 2) { // LWM2M Server
			try {
				
				String op = "r";
				if(operation.equals("write")){
					op = "e";
				} else if(operation.equals("reboot")) {
					op = "rb";
				}
				contentMap.put("o", op);
				HashMap<String, Object> content = lwm2mApiService.execute(operation, contentMap, deviceInfo.getAuthId());
				
				
				//* 
				if (content.get("exception") == null
						&& (Integer) content.get("status") == 200) {
					response.put("result", 0);
					response.put("errorCode", content.get("status"));
					response.put("content", content.get("json"));
				} else if (content.get("exception") == null
						&& (Integer) content.get("status") != 200) {
					response.put("result", 1);
					response.put("errorCode", content.get("status"));
					response.put("content", content.get("json"));
					response.put("requestBody", body);
					response.put("responseBody", content.get("body"));
				} else {
					response.put("result", 0);
					response.put("errorCode", content.get("status"));
					response.put("content", content.get("exception").toString());
					response.put("requestBody", body);
					response.put("responseBody", content.get("body"));
				}//*/
				
				System.out.println("CONTENT : " + content.toString());
				System.out.println("RESPONSE : " + response);

			} catch (UserSysException ex) {
				ex.printStackTrace();
				
				ErrorVO err = ex.getErrorVO();
				response.put("result", 1);
				response.put("errorCode", err.getErrorCode());
				response.put("content", err.getErrorMessage());
				response.put("parameter", param);

			} catch (Exception ex) {
				ex.printStackTrace();
				
				response.put("result", 1);
				response.put("errorCode", -1);
				response.put("content", ex.toString());
				response.put("exception", ex);
				response.put("requestBody", body);
			}
			
		} else if (dmType == 3) { // TR-069 Server
			
			System.out.println("##### operation : "+operation);
			System.out.println("##### bodyString : "+bodyString);
			System.out.println("##### contentMap : "+contentMap);
			
			HashMap<String, Object> content = null;
			
			try{
				content = tr069ApiService.execute2(operation, new JSONObject(bodyString));
				System.out.println("::::::::::::::::::::::::::::::::::");
				System.out.println(content);
				if (content.get("exception") == null && (Integer) content.get("status") == 200) {
					
					response.put("result", 0);
					response.put("errorCode", content.get("status"));
					response.put("content", content.get("json"));
				} else if (content.get("exception") == null && (Integer) content.get("status") != 200) {
					response.put("result", 1);
					response.put("errorCode", content.get("status"));
					response.put("content", content.get("json"));
					response.put("requestBody", body);
					response.put("responseBody", content.get("body"));
				} else {
					response.put("result", 0);
					response.put("errorCode", content.get("status"));
					response.put("content", content.get("exception").toString());
					response.put("requestBody", body);
					response.put("responseBody", content.get("body"));
				}
			} /*catch (UserSysException ex) {
				ex.printStackTrace();
				System.out.println(ex);
				ErrorVO err = ex.getErrorVO();
				response.put("result", 1);
				response.put("errorCode", err.getErrorCode());
				response.put("content", err.getErrorMessage());
				response.put("parameter", param);

			}*/ catch (Exception ex) {
				ex.printStackTrace();
				System.out.println(ex);
				response.put("result", 1);
				response.put("errorCode", -1);
				response.put("content", ex.toString());
				response.put("exception", ex);
				response.put("requestBody", body);
			}
			
			System.out.println("[ CONTENT_OUT ] : "+content);
			System.out.println("[ RESPONSE_OUT ] : "+response);
			
		}
		
		return response;
	}
	
	// HERE FIRMWARE VERSION INSERT, UPDATE
	@ResponseBody
	@RequestMapping(value = "/{system}/{domain}/upload")
	public String uploadFirmware(
		@PathVariable("system") String system,
		@PathVariable("domain") String domain,
		HttpServletRequest request) {
		//throws Exception {

		String returnMsg = "success";
		Map<String, Object> response = new HashMap<String, Object>();
		
		try{
			System.out.println("##### get in444! "+system+"/"+domain);
			
			String savePath = "";
			System.out.println(firmwareDir);
			System.out.println(tomcatDir+uploadDir);
			System.out.println("ddddddddd3333");
			
			MultipartHttpServletRequest mptRequest = (MultipartHttpServletRequest)request;
			String originalFileName = mptRequest.getFile("packageName").getOriginalFilename();
			System.out.println(originalFileName);
			File dir = new File(originalFileName);
			
			if (!dir.exists()) {
				dir.mkdir();
			}
			
			String directory = null;
			int fileType = Integer.parseInt(mptRequest.getParameter("fileType").substring(0, mptRequest.getParameter("fileType").indexOf(" ")));
			if( fileType == 1 ){
				directory = firmwareDir;
			} else {
				directory = tomcatDir+uploadDir;
			}
				
			System.out.println("middle test : " +dir);
			List<HeritFormBasedFileVO> result = HeritFileUploadUtil.filesUpload(request, directory, maxFileSize);
			System.out.println(result.size());
			
			String res = null;
			HttpConnector hc = new HttpConnector();
			res = hc.sendFile(mptRequest);
			System.out.println("---- res : "+res);
			/*
			hc.sendFileWithCurl();
	
			
			if( result.size() > 0 ){
				
				CurlOperation co = new CurlOperation();
				co.setHeader("fileType", mptRequest.getParameter("fileType"));
				co.setHeader("oui", mptRequest.getParameter("oui"));
				co.setHeader("productClass", mptRequest.getParameter("productClass"));
				co.setHeader("version", mptRequest.getParameter("version"));
				
				Object[] params = new Object[3];
				params[0] = "http://"+HeritProperties.getProperty("Globals.tr069ServerHost")+":"+HeritProperties.getProperty("Globals.tr069ServerPort");
				params[1] = co.getHeader();
				params[2] = firmwareDir+dir.getName();
				
				res = co.send("file upload", params);
				System.out.println(res);				
			}*/
			
			if(res != null && res.indexOf("201") > -1){
				int deviceModelId = Integer.parseInt(mptRequest.getParameter("deviceModel"));
				//long fileSize = dir.length();
				long fileSize = result.get(0).getSize();
				System.out.println("######################## file size ");
				System.out.println(fileSize);
				int fwId = 0;
				String fileName = originalFileName;
				String version = mptRequest.getParameter("version");
				String description = mptRequest.getParameter("description");
				
				fwId = fdao.getFirmwareId(deviceModelId, fileType);
				int fvCount = 0;
				if(fwId == 0){
					fwId = fdao.addFirmwareInfo(deviceModelId, fileName, fileType, description);
					fdao.addFirmwareVersion(fwId, version, fileSize);
				} else {
					fvCount = fdao.getFirmwareVersionCount(fwId, mptRequest.getParameter("version"));
					if(fvCount == 0){
						fdao.addFirmwareVersion(fwId, version, fileSize);
					}
				}
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
			returnMsg = ex.getMessage();
		}

		return returnMsg;
	}

	// HERE FIRMWARE VERSION INSERT, UPDATE
	@ResponseBody
	@RequestMapping(value = "/{system}/{domain}/{data}/upload")
	public Map<String, Object> upload(
			@PathVariable("system") String system,
			@PathVariable("domain") String domain,
			@PathVariable("data") String data, HttpServletRequest request)
			throws Exception {

		System.out.println("##### get in333! "+system+"/"+domain+"/"+data);
		
		Map<String, Object> response = new HashMap<String, Object>();
		HashMap<String, Object> param = new HashMap<String, Object>();

		Enumeration names = request.getParameterNames();

		while (names.hasMoreElements()) {
			String name = (String) names.nextElement();
			String value = request.getParameter(name);
			System.out.println("name value : " + name + " , " + value);
			param.put(name, value);
		}

		FileUploadVO fileUploadVO = new FileUploadVO();

		List<FileUploadVO> fileUploadList = new ArrayList();

		// List<HeritFormBasedFileVO> list =
		// HeritFileUploadUtil.filesUpload(request, fileDir, maxFileSize);
		String fileUploadDir = "";

		List<HeritFormBasedFileVO> list = null;
		if (domain.equalsIgnoreCase("firmware")) {

			String dirPackageName = (String) param.get("packageName");
			System.out.println("dirPackageName : " + dirPackageName);
			File dir = new File(dirPackageName);

			if (!dir.exists()) {
				dir.mkdir();
			}

			String fileDir = firmwareDir + dir;
			System.out.println("fileDir " + fileDir);

			fileUploadDir = firmwareDir;
			list = HeritFileUploadUtil.filesUpload(request, fileDir,
					maxFileSize);
		} else {
			fileUploadDir = tomcatDir + uploadDir;
			list = HeritFileUploadUtil.uploadFiles(request, fileUploadDir,
					maxFileSize);
		}

		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				HeritFormBasedFileVO vo = list.get(i);

				fileUploadVO.setFileRealName(vo.getFileName());
				fileUploadVO.setFilePhysName(vo.getPhysicalName());
				fileUploadVO.setFileSize(String.valueOf(vo.getSize()));
				fileUploadList.add(fileUploadVO);
			}
		}
		// js에 보내질 url 경로
		// param.put("fileUploadDir", fileUploadDir);
		param.put("hostUrl", hostUrl);
		param.put("uploadDir", uploadDir);

		try {

			Map<String, Object> content = databaseService.upload(system,
					domain, data, param, fileUploadList);

			response.put("result", 0);
			response.put("errorCode", 0);
			response.put("content", content);
			response.put("parameter", param);

		} catch (UserSysException ex) {
			ErrorVO err = ex.getErrorVO();
			response.put("result", 1);
			response.put("errorCode", err.getErrorCode());
			response.put("content", err.getErrorMessage());
			response.put("parameter", param);

		} catch (Exception ex) {
			response.put("result", 1);
			response.put("errorCode", -1);
			response.put("content", ex.toString());
			response.put("exception", ex);
			response.put("parameter", param);
		}
		return response;
	}

	@ResponseBody
	@RequestMapping(value = "/{system}/{domain}/{data}/{operation}/json")
	public Map<String, Object> getJson(
			@RequestBody Map<String, Object> paramMap,
			@PathVariable("system") String system,
			@PathVariable("domain") String domain,
			@PathVariable("data") String data,
			@PathVariable("operation") String operation,
			HttpServletRequest request) throws Exception {

		Map<String, Object> response = new HashMap<String, Object>();
		HashMap<String, Object> param = new HashMap<String, Object>();

		try {

			Map<String, Object> content = databaseService.execute(system,
					domain, data, operation, paramMap);

			response.put("result", 0);
			response.put("errorCode", 0);
			response.put("content", content);
			response.put("parameter", paramMap);

		} catch (UserSysException ex) {
			ErrorVO err = ex.getErrorVO();
			response.put("result", 1);
			response.put("errorCode", err.getErrorCode());
			response.put("content", err.getErrorMessage());
			response.put("parameter", paramMap);

		} catch (Exception ex) {
			response.put("result", 1);
			response.put("errorCode", -1);
			response.put("content", ex.toString());
			response.put("exception", ex);
			response.put("parameter", paramMap);
		}

		return response;
	}

	/*public static void main(String[] a) throws JsonGenerationException, JsonMappingException, IOException {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("a", "aaa");
		map.put("b", "bbb");

		ObjectMapper mapper = new ObjectMapper();
		Object json = mapper.writeValueAsString(map);
		
		System.out.println("json: " + json.toString());
	}*/
	
	/*
	@SuppressWarnings("unchecked")
	@ResponseBody
    @RequestMapping(value = "/{system}/{domain}/{operation}/json")
	public Map<String, Object> dbHandler(
			@RequestBody String bodyString,
			@PathVariable("system") String system,
			@PathVariable("domain") String domain,
			@PathVariable("operation") String operation,
			HttpServletRequest request) throws Exception {
		
		HashMap<String, Object> param = new HashMap<String, Object>();

		Enumeration names = request.getParameterNames();

		while (names.hasMoreElements()) {
			String name = (String) names.nextElement();
			String value = request.getParameter(name);
			param.put(name, value);
		}
		
		
		return null;
	}
	*/
	
	@ResponseBody
	@RequestMapping(value = "/firmware/list.do")
	//public Map<String, Object> getFirmwareList(ParameterVO po, HttpServletRequest request) throws Exception {
	public Map<String, Object> getFirmwareList(@RequestBody String bodyString, HttpServletRequest request) throws Exception {
		System.out.println("값 체크");
		Map<String, Object> response = new HashMap<String, Object>();

		

		//*
		JSONObject param = new JSONObject(bodyString);
		System.out.println(bodyString);
		System.out.println(param);
		System.out.println("@@@@@@@@@@@@@@@@@@@");
		ParameterVO po = new ParameterVO();
		try{
			po.setSn(param.getString("sn"));
			po.setDeviceModel(String.valueOf(param.getInt("deviceModel")));
			po.setOui(param.getString("oui"));
			po.setModelName(param.getString("modelName"));//*/
			
			Method[] methods = po.getClass().getMethods();
			for(int i=0; i<methods.length; i++){
				if( methods[i].getName().startsWith("get") && methods[i].invoke(po) != null ){
					System.out.println("[PO - "+methods[i].getName()+"] : "+methods[i].invoke(po));
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		PagingUtil pagingUtil = null;
		try {
			pagingUtil = firmwareService.getFirmwareListPaging(1, 10, po);
			
			response.put("result", 0);
			response.put("errorCode", 0);
			response.put("pagingUtil", pagingUtil);

		} catch (Exception ex) {
			ex.printStackTrace();
			response.put("result", 1);
			response.put("errorCode", -1);
			response.put("content", ex.toString());
			response.put("exception", ex);
		}
		
		return response;
	}
	
}
