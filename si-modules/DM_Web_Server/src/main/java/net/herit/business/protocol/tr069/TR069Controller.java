package net.herit.business.protocol.tr069;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.eclipse.persistence.sessions.Session;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import net.herit.business.api.service.ApiHdhDAO;
import net.herit.business.api.service.ApiHdmDAO;
import net.herit.business.api.service.ApiHdpDAO;
import net.herit.business.api.service.Formatter;
import net.herit.business.device.service.DeviceModelVO;
import net.herit.business.device.service.MoProfileVO;
import net.herit.business.protocol.DmVO;
import net.herit.business.protocol.HttpOperator;
import net.herit.business.protocol.MessageHandler;
import net.herit.business.protocol.constant.Errors;
import net.herit.business.protocol.constant.KeyName;
import net.herit.business.protocol.constant.Target;
import net.herit.business.protocol.constant.Type;
import net.herit.business.protocol.model.EtcProtocol;
import net.herit.common.exception.UserSysException;

@Controller
@RequestMapping("/tr069")
public class TR069Controller implements EtcProtocol{

	@Resource(name = "Connector")
	private TR069ConnectOperator connector;
	
	@Resource(name="ApiHdhDAO")
	private ApiHdhDAO hdhDAO;
	@Resource(name="ApiHdmDAO")
	private ApiHdmDAO hdmDAO;
	@Resource(name="ApiHdpDAO")
	private ApiHdpDAO hdpDAO;

	// key value
	private JSONObject token;
	
	private MessageHandler msgHandler = MessageHandler.getInstance();
	private TR069KeyExtractor ext = new TR069KeyExtractor();
	private String deviceId = null;
	private String oui = null;
	private String modelName = null;
	private String serialNumber = null;
	private JSONObject inform = null;
	
	
	
	
	
	private HttpOperator httpOperator = new HttpOperator();
	private TR069KeyExtractor keyExtractor = new TR069KeyExtractor();
	
	private DmVO connectInitialize(JSONObject token){
		DmVO vo = new DmVO();
		vo.setDeviceId(keyExtractor.getDeviceId(token));
		vo.setModelName(keyExtractor.getKeyFromId(vo.getDeviceId(), KeyName.MODEL_NAME));
		vo.setOui(keyExtractor.getKeyFromId(vo.getDeviceId(), KeyName.MANUFACTURER_OUI));
		vo.setSerialNumber(keyExtractor.getKeyFromId(vo.getDeviceId(), KeyName.SERIAL_NUMBER));
		vo.setInform(keyExtractor.getConnectInform(token));
		vo.setAuthId(keyExtractor.getAuthId(vo.getInform()));
		vo.setAuthPwd(keyExtractor.getAuthPwd(vo.getInform()));
		vo.setUriString(token.getString("uriString"));
		vo.setUriList(keyExtractor.getUriList(vo));
		return vo;
	}
	private DmVO reportInitialize(JSONObject token){
		DmVO vo = new DmVO();
		vo.setDeviceId(keyExtractor.getDeviceId(token));
		vo.setModelName(keyExtractor.getKeyFromId(vo.getDeviceId(), KeyName.MODEL_NAME));
		vo.setOui(keyExtractor.getKeyFromId(vo.getDeviceId(), KeyName.MANUFACTURER_OUI));
		vo.setSerialNumber(keyExtractor.getKeyFromId(vo.getDeviceId(), KeyName.SERIAL_NUMBER));
		vo.setInform(keyExtractor.getReportInform(token));
		return vo;
	}
	
	// CONNECT
	@ResponseBody
	@RequestMapping(value="/connect.do", produces="application/json; charset=utf8")
	public String connect(HttpServletRequest request){
		String response = null;
		try{
			System.out.println("----------------------------------- TR-069 Connect Start!!");
			JSONObject token = httpOperator.getParamFromRequest(request);
			System.out.println(token);
			
			// 초기화
			DmVO vo = connectInitialize(token);
			
			// 등록 조회
			DeviceModelVO deviceModel = checkDeviceModelRegist(vo.getModelName());			// 등록 조회 : hdp_device_model
			checkDeviceModelProfileRegist(deviceModel);									// 등록 조회 : hdp_mo_profile
			checkDeviceRegist(vo);															// 등록 조회 : hdm_device 
			System.out.println("----------------------------------- TR-069 Device has connected.");
			response = "200 OK";
			
			// DB에 데이터 업데이트
			hdmDAO.updateDeviceResourcesData(vo);
			
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
			System.out.println("----------------------------------- TR-069 Report Start!!");
			JSONObject token = httpOperator.getParamFromRequest(request);
			System.out.println(token);
			
			// 초기화
			DmVO vo = reportInitialize(token);
			
			// DB에 데이터 업데이트
			hdmDAO.updateDeviceResourcesData(vo);
			response = "200 OK";
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
		int deviceModelCount = hdpDAO.getDeviceModelCountByModelName(modelName);
		if(deviceModelCount != 1){
			// 기대값이 아니므로 exception 발생
			throw new Exception(Errors.ERR_500.getMsg());
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
			throw new Exception(Errors.ERR_500.getMsg());
		}
	}
	
	/** 등록 조회 : hdm_device 
	 * @throws Exception **/
	public void checkDeviceRegist(DmVO vo) throws Exception{
		int deviceCount = hdmDAO.getCountByAuthAccount(TR069Formatter.getInstance().getDeviceId(vo.getDeviceId(), Target.DM), vo.getInform(), Type.TR_069);
		if(deviceCount == 0){
			throw new Exception(Errors.ERR_500.getMsg());
		} else if (deviceCount == 1) {
			registResourceModel(vo);
		} else {
			throw new Exception(Errors.ERR_500.getMsg());
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
			System.out.println("----------------------------------- Process done.");
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
			// resource 등록
			hdmDAO.insertDeviceResources(vo.getDeviceId(), vo.getUriList());
			System.out.println("Resource has registered.");
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// handler
	@RequestMapping(value="/connect2.do", produces="application/json; charset=utf8")
	@ResponseBody
	public String connectHandler(HttpServletRequest request){
		System.out.println("----------------------------------- connect");
		JSONObject response = null;
		try{
			
			token = msgHandler.jsonReceiver(request);
			init(token);
			
			String command = token.getString("command");
			if(command.equals("connect")){
				HttpSession session = request.getSession();
				session.setAttribute("acsInfo", token.getString("address"));
				response = connect();
				hdmDAO.updateDeviceResourcesData(Formatter.getInstance().getTR069DeviceIdToDm(deviceId), inform);
			} else if(command.equals("disconnect")){
				response = disconnect();
			} else if(command.equals("control")){
				
			} else if(command.equals("observe")){
				
			}
			
			//else if(command.equals("controlHistory")){
			//	hdhDAO.insertControlHistory(UtilT.jsonToMap(token));
			//}
			
			System.out.println("CONNECT !!!!!!!!!!!!!!!!!!!!!!!!");
			System.out.println(token);
			System.out.println("CONNECT2 !!!!!!!!!!!!!!!!!!!!!!!!");
			System.out.println(inform);
			
			
			
		} catch(Exception e) {
			response = msgHandler.setResponse(Errors.ERR_500, e.getMessage());
			e.printStackTrace();
		}
		return response.toString();
	}
	
	// handler
	@RequestMapping(value="/report2.do", produces="application/json; charset=utf8")
	@ResponseBody
	public String reportHandler(HttpServletRequest request){
		System.out.println("----------------------------------- report");
		JSONObject response = null;
		try{
			token = msgHandler.jsonReceiver(request);
			
			String api = token.getString("api");
			String headUri = "/dm/tr69";
			if(api.startsWith(headUri)){
				System.out.println(api);
				api = api.substring(headUri.length());
				hdmDAO.updateDeviceResourcesData(Formatter.getInstance().getTR069DeviceIdToDm(token.getString("deviceId")), token.getJSONObject("param"));
				
				
				System.out.println("REPORT !!!!!!!!!!!!!!!!!!!!!!!!");
				System.out.println(token);
			}
		} catch(Exception e) {
			response = msgHandler.setResponse(Errors.ERR_500, e.getMessage());
			e.printStackTrace();
		}
		return response.toString();
	}
	
	
	private void init(JSONObject token){		
		deviceId = ext.getDeviceId(token);
		modelName = ext.getKeyFromId(deviceId, KeyName.MODEL_NAME);
		oui = ext.getKeyFromId(deviceId, KeyName.MANUFACTURER_OUI);
		serialNumber = ext.getKeyFromId(deviceId, KeyName.SERIAL_NUMBER);
		inform = ext.getConnectInform(token);
	}
	
	// CONNECT
	public JSONObject connect(){
		JSONObject result = null;
		try{
			connector.connect(token, inform, deviceId, modelName, Type.TR_069);
		} catch(Exception e){
			// 예외 발생시 client에 통보
			e.printStackTrace();
			result = msgHandler.setResponse(Errors.ERR_500, e.getMessage());
		}	
		return result;
	}

	// DISCONNECT
	public JSONObject disconnect(){
		boolean hasDisconnected = false;
		JSONObject result = null;
		try {
			hasDisconnected = hdmDAO.updateDeviceConnStatus(deviceId, "0");
			result = msgHandler.setResponse(Errors.ERR_000, null);
		} catch (Exception e) {
			e.printStackTrace();
			result = msgHandler.setResponse(Errors.ERR_500, e.getMessage());
		} finally {	
			System.out.println("disconnected : " + hasDisconnected);
		}
		return result;
	}

	
	
}
