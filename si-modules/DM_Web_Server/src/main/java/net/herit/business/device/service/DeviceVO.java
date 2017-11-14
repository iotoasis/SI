package net.herit.business.device.service;

import java.io.Serializable;

import net.herit.common.util.StringUtil;


public class DeviceVO implements Serializable{

	/** DEVICE_ID */
	private String deviceId;
	/** PARENT_ID */
	private String parentId;
	/** DEVICE_TYPE */
	private String deviceType;
	/** MODEL_NAME */
	private String modelName;
	/** OUI */
	private String oui;
	/** MANUFACTURER */
	private String manufacturer;
	/** EMBEDED_URI */
	private String embededUri;
	/** SN */
	private String sn;
	/** AUTH_ID */
	private String authId;
	/** AUTH_PWD */
	private String authPwd;
	/** BS_STATUS */
	private String bsStatus;
	/** REG_TIME */
	private String regTime;
	/** REG_UPDATE_TIME */
	private String regUpdateTime;
	/** CREATE_TIME */
	private String createTime;
	/** UPDATE_TIME */
	private String updateTime;
	/** DM_TYPE */
	private int dmType;
	/** EXT_DEVICE_ID */
	private String extDeviceId;

	public String getDeviceId() {
		return StringUtil.nvl(deviceId);
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getParentId() {
		return StringUtil.nvl(parentId);
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getDeviceType() {
		return StringUtil.nvl(deviceType);
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public String getModelName() {
		return StringUtil.nvl(modelName);
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public String getOui() {
		return StringUtil.nvl(oui);
	}
	public void setOui(String oui) {
		this.oui = oui;
	}
	public String getManufacturer() {
		return StringUtil.nvl(manufacturer);
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getEmbededUri() {
		return StringUtil.nvl(embededUri);
	}
	public void setEmbededUri(String embededUri) {
		this.embededUri = embededUri;
	}
	public String getSn() {
		return StringUtil.nvl(sn);
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public String getAuthId() {
		return StringUtil.nvl(authId);
	}
	public void setAuthId(String authId) {
		this.authId = authId;
	}
	public String getAuthPwd() {
		return StringUtil.nvl(authPwd);
	}
	public void setAuthPwd(String authPwd) {
		this.authPwd = authPwd;
	}
	public String getBsStatus() {
		return StringUtil.nvl(bsStatus);
	}
	public void setBsStatus(String bsStatus) {
		this.bsStatus = bsStatus;
	}
	
	public String getCreateTime() {
		return StringUtil.nvl(createTime);
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpdateTime() {
		return StringUtil.nvl(updateTime);
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public int getDmType() {
		return dmType;
	}
	public void setDmType(int dmType) {
		this.dmType = dmType;
	}
	public String getExtDeviceId() {
		return extDeviceId;
	}
	public void setExtDeviceId(String extDeviceId) {
		this.extDeviceId = extDeviceId;
	}	
	
}
