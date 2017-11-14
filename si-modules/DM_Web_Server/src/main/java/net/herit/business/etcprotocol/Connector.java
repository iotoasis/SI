package net.herit.business.etcprotocol;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import net.herit.business.api.service.ApiHdhDAO;
import net.herit.business.api.service.ApiHdmDAO;
import net.herit.business.api.service.ApiHdpDAO;
import net.herit.business.device.service.DeviceModelVO;
import net.herit.business.device.service.DeviceService;
import net.herit.business.device.service.MoProfileVO;
import net.herit.business.etcprotocol.constant.Errors;
import net.herit.business.etcprotocol.constant.Type;
import net.herit.common.exception.UserSysException;

@Service("Connector")
public class Connector {

	@Resource(name = "DeviceService")
	private DeviceService deviceService;
	
	@Resource(name="ApiHdhDAO")
	private ApiHdhDAO hdhDAO;
	@Resource(name="ApiHdmDAO")
	private ApiHdmDAO hdmDAO;
	@Resource(name="ApiHdpDAO")
	private ApiHdpDAO hdpDAO;
	
	private String acsIp = null;
	private int acsPort = 0;
	
	private static Connector instance;
	public static Connector getInstance(){
		if(instance == null){
			instance = new Connector();
		}
		return instance;
	}
	
	private DeviceModelVO deviceModel;
	public DeviceModelVO getDeviceModelVO(){
		return deviceModel;
	}
	
	private JSONObject token;
	public JSONObject getToken(){
		return token;
	}
	private JSONObject inform;
	public JSONObject getInform(){
		return inform;
	}
	private String deviceId;
	public String getDeivceId(){
		return deviceId;
	}
	
	/** 최초 token과 deviceId 저장 **/
	public void regist(JSONObject token, JSONObject inform, String deviceId){
		this.token = token;
		this.inform = inform;
		this.deviceId = deviceId;
	}
	
	/** 등록 조회 : hdp_device_model 
	 * @throws Exception **/
	public void checkDeviceModelRegistered(String modelName) throws Exception{
		System.out.println(modelName);
		int deviceModelCount = hdpDAO.getDeviceModelCountByModelName(modelName);
		if(deviceModelCount != 1){
			// 기대값이 아니므로 exception 발생
			throw new Exception(Errors.ERR_500.getMsg());
		} else {
			deviceModel = hdpDAO.getDeviceModelId(modelName);
		}
	}
	
	/** 등록 조회 : hdp_mo_profile 
	 * @throws Exception **/
	public void checkDeviceModelProfileRegistered() throws Exception{
		int profileCount = hdpDAO.getMoProfileCountByDeviceModelId(deviceModel.getId());
		if(profileCount < 1){
			// 최소 1개 이상의 resource가 등록되어야 함
			throw new Exception(Errors.ERR_500.getMsg());
		}
	}
	
	/** 등록 조회 : hdm_device 
	 * @throws Exception **/
	public void checkDeviceRegistered(Type protocol) throws Exception{
		int deviceCount = hdmDAO.getCountByAuthAccount(deviceId, inform, protocol);
		if(deviceCount == 0){
			throw new Exception(Errors.ERR_500.getMsg());
		} else if (deviceCount == 1) {
			registResourceModel(protocol);
		} else {
			throw new Exception(Errors.ERR_500.getMsg());
		}
	}
	
	/** 리소스 등록
	 * @throws UserSysException
	 */
	public void registResources() throws UserSysException{
		// resource 개수 파악  
		int resourceCount = hdmDAO.getResourceCountByDeviceId(deviceId);
		
		List<MoProfileVO> uriList = new ArrayList<MoProfileVO>();
		String uriString = token.getString("uriString");
		uriString = uriString.replace(",", "/");
		String[] uriSplit = uriString.split("@@");
		for(int i=0; i<uriSplit.length; i++){
			MoProfileVO uriObj = new MoProfileVO();
			uriObj.setResourceUri(uriSplit[i]);
			uriObj.setDisplayName(uriSplit[i]);
			try{
				uriObj.setData(inform.getString(uriSplit[i].replace("/", ".")));
			} catch(Exception e) {}
			uriList.add(uriObj);
		}
		
		if(resourceCount == 0){
			// resource 등록
			hdmDAO.insertDeviceResources(deviceId, uriList);
			System.out.println("Resource has registered.");
		}
	}
	
	
	/** resource 등록 : hdm_device_mo_data : resource 추출 
	 * @throws Exception **/
	public void registResourceModel(Type protocol) throws Exception{
		switch(protocol){
		case LWM2M :
			break;
		case TR_069 :
			registResources();
			break;
		}
		
		boolean isConnected = false;
		int connCount = hdmDAO.getDeviceConnStatusCount(deviceId);
		switch(connCount){
		case 0:
			// 연결 정보가 없기 때문에 추가
			isConnected = hdmDAO.insertDeviceConnStatus(deviceId, "DGP2");
			break;
		case 1:
			// 연결 정보가 있기 때문에 상태값만 업데이트
			isConnected = hdmDAO.updateDeviceConnStatus(deviceId, "1");
			break;
		default :
			// 기대값 아니므로 exception 발생
			throw new Exception(Errors.ERR_500.getMsg());
		}
		
		if(isConnected){
			System.out.println("!!!    The device has connected!");
		} else {
			throw new Exception(Errors.ERR_500.getMsg());
		}
	}

	public void connect(JSONObject token, JSONObject inform, String deviceId, String modelName, Type type) throws Exception {
		regist(token, inform, deviceId);              
		checkDeviceModelRegistered(modelName);
		checkDeviceModelProfileRegistered();
		checkDeviceRegistered(type); 
	}
	
	
}
