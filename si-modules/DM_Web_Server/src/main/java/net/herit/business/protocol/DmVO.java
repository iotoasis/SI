package net.herit.business.protocol;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import net.herit.business.device.service.MoProfileVO;

public class DmVO {
	
	// DM Base
	private String deviceId = null;
	private String oui = null;
	private String modelName = null;
	private String serialNumber = null;
	private String authId = null;
	private String authPwd = null;
	
	// TR-069
	private String uriString = null;
	private List<MoProfileVO> uriList = null;
	private JSONObject inform = null;
	
	// LWM2M
	private JSONArray objectModels = null; 
	private List<String[]> objModels = null;
	
	
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getOui() {
		return oui;
	}
	public void setOui(String oui) {
		this.oui = oui;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public JSONObject getInform() {
		return inform;
	}
	public void setInform(JSONObject inform) {
		this.inform = inform;
	}
	public String getAuthId() {
		return authId;
	}
	public void setAuthId(String authId) {
		this.authId = authId;
	}
	public String getAuthPwd() {
		return authPwd;
	}
	public void setAuthPwd(String authPwd) {
		this.authPwd = authPwd;
	}
	public String getUriString() {
		return uriString;
	}
	public void setUriString(String uriString) {
		this.uriString = uriString;
	}
	public JSONArray getObjectModels() {
		return objectModels;
	}
	public void setObjectModels(JSONArray objectModels) {
		this.objectModels = objectModels;
	}
	public List<String[]> getObjModels() {
		return objModels;
	}
	public void setObjModels(List<String[]> objModels) {
		this.objModels = objModels;
	}
	public List<MoProfileVO> getUriList() {
		return uriList;
	}
	public void setUriList(List<MoProfileVO> uriList) {
		this.uriList = uriList;
	}
	
	
}
