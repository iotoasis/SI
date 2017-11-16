package net.herit.business.etcprotocol.tr069;

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
import net.herit.business.etcprotocol.Connector;
import net.herit.business.etcprotocol.MessageHandler;
import net.herit.business.etcprotocol.constant.Errors;
import net.herit.business.etcprotocol.constant.KeyName;
import net.herit.business.etcprotocol.constant.Type;
import net.herit.business.etcprotocol.model.EtcProtocol;

@Controller
@RequestMapping("/tr069")
public class TR069 implements EtcProtocol{

	@Resource(name = "Connector")
	private Connector connector;
	
	@Resource(name="ApiHdhDAO")
	private ApiHdhDAO hdhDAO;
	@Resource(name="ApiHdmDAO")
	private ApiHdmDAO hdmDAO;
	@Resource(name="ApiHdpDAO")
	private ApiHdpDAO hdpDAO;

	// key value
	private JSONObject token;
	
	private MessageHandler msgHandler = MessageHandler.getInstance();
	private Extractor ext = new Extractor();
	private String deviceId = null;
	private String oui = null;
	private String modelName = null;
	private String serialNumber = null;
	private JSONObject inform = null;
	
	// handler
	@RequestMapping(value="/connect.do", produces="application/json; charset=utf8")
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
			/*
			else if(command.equals("controlHistory")){
				hdhDAO.insertControlHistory(UtilT.jsonToMap(token));
			}*/
			
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
	@RequestMapping(value="/report.do", produces="application/json; charset=utf8")
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
		inform = ext.getInform(token);
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
