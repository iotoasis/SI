package net.herit.business.protocol.lwm2m;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.herit.business.api.service.ApiHdhDAO;
import net.herit.business.api.service.ApiHdmDAO;
import net.herit.business.api.service.ApiHdpDAO;
import net.herit.business.api.service.Formatter;
import net.herit.business.api.service.LWM2MApiService;
import net.herit.business.device.service.DeviceModelVO;
import net.herit.business.device.service.DeviceService;
import net.herit.business.device.service.MoProfileVO;
import net.herit.business.protocol.DmVO;
import net.herit.business.protocol.HttpOperator;
import net.herit.business.protocol.constant.Errors;
import net.herit.business.protocol.constant.KeyName;
import net.herit.business.protocol.constant.Target;
import net.herit.business.protocol.lwm2m.exception.JsonFormatException;
import net.herit.business.protocol.lwm2m.resource.ResourceVO;
import net.herit.common.exception.UserSysException;
import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.message.onem2m.OneM2mRequest.OPERATION;
import net.herit.iot.message.onem2m.OneM2mRequest.RESOURCE_TYPE;
import net.herit.iot.message.onem2m.format.Enums.CONTENT_TYPE;
import net.herit.iot.onem2m.bind.http.client.HttpClient;

@Controller
@RequestMapping("/lwm2m")
public class LWM2MController {

	@Resource(name = "DeviceService")
	private DeviceService deviceService;
	
	@Resource(name="ApiHdhDAO")
	private ApiHdhDAO hdhDAO;
	@Resource(name="ApiHdmDAO")
	private ApiHdmDAO hdmDAO;
	@Resource(name="ApiHdpDAO")
	private ApiHdpDAO hdpDAO;

	
	private HttpOperator httpOperator = new HttpOperator();
	private LWM2MKeyExtractor keyExtractor = new LWM2MKeyExtractor();

	private DmVO initialize(JSONObject token){
		DmVO vo = new DmVO();
		vo.setDeviceId(keyExtractor.getDeviceId(token));
		vo.setModelName(keyExtractor.getKeyFromId(vo.getDeviceId(), KeyName.MODEL_NAME));
		vo.setOui(keyExtractor.getKeyFromId(vo.getDeviceId(), KeyName.MANUFACTURER_OUI));
		vo.setSerialNumber(keyExtractor.getKeyFromId(vo.getDeviceId(), KeyName.SERIAL_NUMBER));
		vo.setAuthId(keyExtractor.getAuthId(token));
		vo.setAuthPwd(keyExtractor.getAuthPwd(token));
		return vo;
	}
	
	
	// CONNECT
	@ResponseBody
	@RequestMapping(value="/connect.do", produces="application/json; charset=utf8")
	public String connect(HttpServletRequest request){
		String response = null;
		try{
			System.out.println("----------------------------------- LWM2M Connect Start!!");
			JSONObject token = httpOperator.getParamFromRequest(request);
			
			// 초기화
			DmVO vo = initialize(token);
			
			// 등록 조회
			DeviceModelVO deviceModel = checkDeviceModelRegist(vo.getModelName());		// 등록 조회 : hdp_device_model
			checkDeviceModelProfileRegist(deviceModel);									// 등록 조회 : hdp_mo_profile
			checkDeviceRegist(vo);														// 등록 조회 : hdm_device 
			System.out.println("----------------------------------- LWM2M Device has connected.");
			response = "200 OK";
			
		} catch(Exception e) {
			response = e.getMessage();
			e.printStackTrace();
		}
		return response;
	}
	
	// REPORT
	@ResponseBody
	@RequestMapping(value="/report.do", produces="application/json; charset=utf8")
	public String report(HttpServletRequest request){
		String response = null;
		try{
			System.out.println("----------------------------------- LWM2M Report Start!!");
			JSONObject token = httpOperator.getParamFromRequest(request);
			System.out.println(token);
			response = "200 OK";
			
			// DB에 데이터 업데이트
			hdmDAO.updateDeviceResourcesData(Formatter.getInstance().getTR069DeviceIdToDm(token.getString("deviceId")), token.getJSONObject("param"));
			
		} catch(Exception e) {
			response = e.getMessage();
			e.printStackTrace();
		}
		return response;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/** 등록 조회 : hdp_device_model 
	 * @throws Exception **/
	public DeviceModelVO checkDeviceModelRegist(String modelName) throws Exception{
		DeviceModelVO deviceModel = null;
		
		System.out.println(hdpDAO==null);
		int deviceModelCount = hdpDAO.getDeviceModelCountByModelName(modelName);
		if(deviceModelCount != 1){
			throw new Exception(Response.ERR101.getMsg());
		} else {
			deviceModel = hdpDAO.getDeviceModelId(modelName);
		}
		return deviceModel;
	}
	
	/** 등록 조회 : hdp_mo_profile 
	 * @throws Exception **/
	public void checkDeviceModelProfileRegist(DeviceModelVO deviceModel) throws Exception{
		int profileCount = hdpDAO.getMoProfileCountByDeviceModelId(deviceModel.getId());
		if(profileCount < 1){
			// 최소 1개 이상의 resource가 등록되어야 함
			throw new Exception(Response.ERR102.getMsg());
		}
	}
	
	/** 등록 조회 : hdm_device 
	 * @throws Exception **/
	public void checkDeviceRegist(DmVO vo) throws Exception{
		int deviceCount = hdmDAO.getCountByAuthAccount(vo);
		if(deviceCount == 0){
			throw new Exception(Errors.ERR_500.getMsg());
		} else if (deviceCount == 1) {
			registResourceModel(vo);
		} else {
			throw new Exception(Errors.ERR_500.getMsg());
		}
	}
	
	/** 리소스 등록
	 * @throws UserSysException
	 */
	public void registResources(DmVO vo) throws UserSysException{
		// resource 개수 파악
		int resourceCount = hdmDAO.getResourceCountByDeviceId(vo.getDeviceId());
		if(resourceCount == 0){
			// resource 등록 : hdm_device_mo_data : resource 추출
			List<MoProfileVO> profileList = hdpDAO.getResourceUriByDeviceModelId(vo.getDeviceId());
			List<MoProfileVO> uri_list = new ArrayList<MoProfileVO>();
			
			JSONArray om = vo.getObjectModels();
			for(int i=0; i<om.length(); i++){
				for(int j=0; j<profileList.size(); j++){
					if(LWM2MFormatter.getInstance().getResourceUri(profileList.get(j).getResourceUri(), Target.DM).indexOf(om.getJSONObject(i).getString("uri")) > -1){
						uri_list.add(profileList.get(j));
					}
				}
			}
			hdmDAO.insertDeviceResources(vo.getDeviceId(), uri_list);
			System.out.println("Resources have registered.");
		}
	}
	
	/** resource 등록 : hdm_device_mo_data : resource 추출 
	 * @throws Exception **/
	public void registResourceModel(DmVO vo) throws Exception{
		registResources(vo);
		
		boolean isConnected = false;
		int connCount = hdmDAO.getDeviceConnStatusCount(vo.getDeviceId());
		switch(connCount){
		case 0:
			// 연결 정보가 없기 때문에 추가
			isConnected = hdmDAO.insertDeviceConnStatus(vo.getDeviceId(), "DGP2");
			break;
		case 1:
			// 연결 정보가 있기 때문에 상태값만 업데이트
			isConnected = hdmDAO.updateDeviceConnStatus(vo.getDeviceId(), "1");
			break;
		default :
			// 기대값 아니므로 exception 발생
			throw new Exception(Errors.ERR_500.getMsg());
		}
		
		if(isConnected){
			System.out.println("----------------------------------- Process done");
		} else {
			throw new Exception(Errors.ERR_500.getMsg());
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// key value
	private JSONObject token;
	private String deviceId;
	
	// response msg
	public enum Response{
		
		// ERROR
		ERR000("The error has occured."),
		
		ERR101("Data Error"),	// =1
		ERR102("At least a resource or more than must be registered."),	// >=1
		ERR103("The device hasn't registered or incorrect information call."),	// =1
		ERR104("Data Error"),
		
		ERR900("The server doesn't support completely this device."),
		ERR901("JSON Format Exception"),
		
		// SUCCESS
		SUC_CONN("Connected"),
		SUC_DISC("Disconnected ");
		
		private String msg;
		Response(String msg){
			this.msg = msg;
		}
		public String getMsg(){
			return this.msg;
		}
	}
	
	// handler
	@RequestMapping(value="/conn.do", produces="application/json; charset=utf8")
	@ResponseBody
	public String connectHandler(HttpServletRequest request){
		
		JSONObject response = null;
		
		try{
			token = requestMessageFromLwmw2mServer(request);
			deviceId = Util.makeDeviceId(token);
			
			System.out.println(token);
			
			String connectMethod = token.get("conn").toString();
			if(connectMethod.equals("connect")){
				response = connect2();
			} else if(connectMethod.equals("disconnect")){
				response = disconnect();
			} else if(connectMethod.equals("controlHistory")){
				hdhDAO.insertControlHistory(Util.jsonToMap(token));
			}
			/*
			 else if(connectMethod.equals("res")){
				resourceDataToDatabase(token);
			} else if(connectMethod.equals("oneM2M_write")){
				//write(token);
			} else if(connectMethod.equals("oneM2M_read")){
				//read(token);
			} else if(connectMethod.equals("setStatus")){
				// history 등록 process
			} 
			//*/
		} catch(JsonFormatException e) {
			response = setResponse(Response.ERR901,e.getMessage());
			e.printStackTrace();
		} catch(Exception e) {
			response = setResponse(Response.ERR900,e.getMessage());
			e.printStackTrace();
		}
		
		return response.toString();
	}
	
	// handler
	@RequestMapping(value="/test.do", produces="application/json; charset=utf8")
	@ResponseBody
	public String test(HttpServletRequest request){
		
		OneM2mRequest req = new OneM2mRequest();
		req.setRequestIdentifier("req");
		req.setFrom("S");
		req.setTo("/herit-cse");
		req.setContentType(CONTENT_TYPE.JSON);
		req.setOperation(OPERATION.RETRIEVE);
		req.setResourceType(RESOURCE_TYPE.CSE_BASE);
		
		OneM2mResponse res = HttpClient.getInstance().sendRequest("http://10.10.224.240:8080/herit-in/herit-cse", req);
		return res.toString();
	}
	
	// set response
	public JSONObject setResponse(Response caseOfResult, String msg) {
		JSONObject response = new JSONObject();

		try{
			response.put("RESPONSE-CODE", caseOfResult.name());
			
			switch(caseOfResult){
			// error
			case ERR101: case ERR102: case ERR103:
				response.put("result", "Failed");
				break;
			case ERR900:
				response.put("result", "Failed");
				break;
			
			// success
			case SUC_CONN:
				response.put("result", "Connected");
				break;
			case SUC_DISC:
				response.put("result", "Disconnected");
				break;
				
			// default
			default :
				response.put("result", "Failed");
				break;
			}
			
			response.put("reason", caseOfResult.getMsg());
			
			if(!Util.isNOE(msg)){
				response.put("msg", msg);
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return response;
	}
	
	
	// 메시지 받기
	public JSONObject requestMessageFromLwmw2mServer(HttpServletRequest request) throws JsonFormatException{
		
		InputStream is;
		String token;
		JSONObject result = null;
		
		try{
			is = request.getInputStream();
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] byteBuffer = new byte[1024];
			byte[] byteData;
			int nLength = 0;
			while((nLength=is.read(byteBuffer,0,byteBuffer.length)) != -1){
				baos.write(byteBuffer,0,nLength);
			}
			byteData = baos.toByteArray();
			token = new String(byteData);
			result = new JSONObject(token);
			
		} catch(Exception e) {
			result = setResponse(Response.ERR901, null);
			throw new JsonFormatException();
		}
		
		return result;
	}
	
	
	
	public JSONObject connect2(){
		
		JSONObject result = null;
		boolean hasConnected = false;
		
		//*
		try{			
			// 등록 조회 : hdp_device_model, hdp_mo_profile, hdm_device
			// hdp_device_model
			int deviceModelCount = hdpDAO.getDeviceModelCountByModelName(token.get("modelName").toString());
			DeviceModelVO deviceModel;
			if(deviceModelCount != 1){
				// 기대값이 아니므로 exception 발생
				result = setResponse(Response.ERR101, null);
				throw new Exception(Response.ERR101.getMsg());
			} else {
				deviceModel = hdpDAO.getDeviceModelId(token.get("modelName").toString());
			}
			
			// hdp_mo_profile
			int profileCount = hdpDAO.getMoProfileCountByDeviceModelId(deviceModel.getId());
			if(profileCount < 1){
				// 최소 1개 이상의 resource가 등록되어야 함
				result = setResponse(Response.ERR102, null);
				throw new Exception(Response.ERR102.getMsg());
			}
			
			// hdm_device
			int deviceCount = hdmDAO.getCountByAuthAccount(deviceId, token.getString("authId"), token.getString("authPwd"));
			switch(deviceCount){
			case 0:
				// 정보 불일치 혹은 등록되지 않았으므로 exception 발생
				result = setResponse(Response.ERR103, null);
				throw new Exception(Response.ERR103.getMsg());
			case 1:
				// 조건 통과
				// resource 등록 : hdm_device_mo_data : resource 추출
				List<MoProfileVO> profileList = hdpDAO.getResourceUriByDeviceModelId(deviceModel.getId());
				List<MoProfileVO> uri_list = new ArrayList<MoProfileVO>();
				
				JSONArray om = token.getJSONArray("objectModels");
				for(int i=0; i<om.length(); i++){
					for(int j=0; j<profileList.size(); j++){
						if(profileList.get(j).getResourceUri().replace("/-/", "/0/").indexOf(om.getJSONObject(i).getString("uri")) > -1){
							uri_list.add(profileList.get(j));
						}
					}
				}
			
				// resource 개수 파악  
				int resourceCount = hdmDAO.getResourceCountByDeviceId(deviceId);
				if(resourceCount != uri_list.size()){
					// db에서의 resource 개수와 client에서 보내온 resource 개수가 다를 경우 
					/*
					if(resourceCount != 0){
						// 기존 resource가 0개가 아닐 때, 기존 resource 삭제 후 재등록
						System.out.println("delete old resource");
						hdmDAO.removeResourceByDeviceId(deviceId);
					}
					*/
					// resource 등록
					hdmDAO.insertDeviceResources(deviceId, uri_list);
					System.out.println("Resources have registered.");
				}
				
				// 연결 성공
				int connCount = hdmDAO.getDeviceConnStatusCount(deviceId);
				switch(connCount){
				case 0:
					// 연결 정보가 없기 때문에 추가
					hasConnected = hdmDAO.insertDeviceConnStatus(deviceId, "DGP2");
					result = setResponse(Response.SUC_CONN, null);
					break;
				case 1:
					// 연결 정보가 있기 때문에 상태값만 업데이트
					hasConnected = hdmDAO.updateDeviceConnStatus(deviceId, "1");
					result = setResponse(Response.SUC_CONN, null);
					break;
				default :
					// 기대값 아니므로 exception 발생
					result = setResponse(Response.ERR000, null);
					throw new Exception(Response.ERR000.getMsg());
				}
				
				
				break;
			default :
				// 기대값이 아니므로 exception 발생
				result = setResponse(Response.ERR104, null);
				throw new Exception(Response.ERR104.getMsg());
			}
			
		} catch(Exception e){
			// 예외 발생시 client에 통보
			e.printStackTrace();
			result = setResponse(Response.ERR900,e.getMessage());
		} finally {
			System.out.println("connected : "+hasConnected);
			
			//*
			Timer timer = new Timer();
			timer.schedule(new TimerTask(){
				public void run() {
					try{
						
						// rx, tx start
						LWM2MApiService ls = new LWM2MApiService();
						
						String order = "{\"d\":\""+deviceId+"\", \"e\":[{\"n\":\"/7/-/6\",\"sv\":\"ON\"}]}";
						HashMap<String, Object> contentMap = new ObjectMapper().readValue(order, HashMap.class);
						contentMap.put("o", "e");
						
						ls.execute("write", contentMap, token.getString("authId"));
						
						order = "{\"d\":\""+deviceId+"\",\"e\":[{\"n\":\"/6/-/0\"},{\"n\":\"/6/-/1\"}]}";
						contentMap = new ObjectMapper().readValue(order, HashMap.class);
						contentMap.put("o", "r");
						HashMap<String, Object> content = ls.execute("read", contentMap, token.getString("authId"));
						
						hdmDAO.updateDeviceResourcesData(deviceId, content);
					} catch(Exception e){
						e.printStackTrace();
					}
				}
			}, 5000);
			//*/
		}
		
		//*/	
		return result ;
	}
	
	public JSONObject disconnect(){
		
		boolean hasDisconnected = false;
		JSONObject result = null;
		
		try {
			hasDisconnected = hdmDAO.updateDeviceConnStatus(deviceId, "0");
			result = setResponse(Response.SUC_DISC, null);
		} catch (Exception e) {
			e.printStackTrace();
			result = setResponse(Response.ERR900,e.getMessage());
			// TODO: handle exception
		} finally {	
			System.out.println("disconnected : " + hasDisconnected);
		}
		return result;
	}
	
	
	//////////////////////////////////////////////////////////
	
	// resource json 파일 일괄 db에 저장하기
	public void resourceDataToDatabase(JSONObject token) throws JSONException{
		
		List<ResourceVO> list = new ArrayList<ResourceVO>();
		
		JSONArray wrap = token.getJSONArray("contents");
		for (int i = 0; i < wrap.length(); i++) {
			StringBuffer uri_base = new StringBuffer("/");
			JSONObject obj = wrap.getJSONObject(i);
			uri_base.append(obj.get("id")).append("/-/");
			
			JSONArray resourcedefs = obj.getJSONArray("resourcedefs");
			for (int j = 0; j < resourcedefs.length(); j++) {
				ResourceVO res = new ResourceVO();
				StringBuffer uri = new StringBuffer(uri_base);
				
				JSONObject resourcedef = resourcedefs.getJSONObject(j);
				uri.append(resourcedef.get("id"));
				res.setResource_uri(uri.toString());
				res.setData_type(Util.getDataType(resourcedef.getString("type")));
				res.setUnit(resourcedef.getString("units").length()>3?"ref":resourcedef.getString("units"));
				res.setOperation(resourcedef.getString("operations").equals("NONE")?"":resourcedef.getString("operations"));
				res.setIs_mandatory(resourcedef.getBoolean("mandatory")?"Y":"N");
				res.setDisplay_name(resourcedef.getString("name"));
				res.setIs_multiple(resourcedef.getString("instancetype").equals("single")?"N":"Y");
				res.setDescription(resourcedef.getString("description"));
				
				list.add(res);
			}
		}
		
		try {
			int result = hdpDAO.insertsProfile(list);
		} catch (UserSysException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/*
	public void write(JSONObject token){
		LWM2MApiService ls = new LWM2MApiService();
		String order = "{\"d\":\""+deviceId+"\" , \"n\":\""+token.getString("resourceUri")+"\", \"o\":\"e\", \"sv\":\""+token.getInt("sv")+"\"}";
		HashMap<String, Object> result = ls.startObserveRxTxData("write", order, token.getString("authId"));
		System.out.println(result.toString());
	}
	public void read(JSONObject token){
		LWM2MApiService ls = new LWM2MApiService();
		String order = "{\"d\":\""+deviceId+"\" , \"n\":\""+token.getString("resourceUri")+"\", \"o\":\"o\"}";
		HashMap<String, Object> result = ls.startObserveRxTxData("read", order, token.getString("authId"));
		System.out.println(result.toString());
	}
	//*/
}
