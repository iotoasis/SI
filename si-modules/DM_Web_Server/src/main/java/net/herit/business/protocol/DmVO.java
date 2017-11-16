package net.herit.business.protocol;

import org.json.JSONObject;

import net.herit.business.protocol.constant.KeyName;

public class DmVO {
	private String deviceId = null;
	private String oui = null;
	private String modelName = null;
	private String serialNumber = null;
	private JSONObject inform = null;
	
	
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
}
