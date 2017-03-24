package net.herit.business.device.service;

import java.io.Serializable;

import net.herit.common.util.StringUtil;


public class DeviceModelVO implements Serializable{

	/** ID */
	private String id;
	/** OUI */
	private String oui;
	/** MANUFACTURER */
	private String manufacturer;
	/** MODEL_NAME */
	private String modelName;
	/** DEVICE_TYPE */
	private String deviceType;
	/** PROFILE_VER */
	private String profileVer;
	/** ICON_URL */
	private String iconUrl;
	/** DESCRIPTION */
	private String description;
	/** APPLY_YN */
	private String applyYn;	
	/** CREATE_TIME */
	private String createTime;
	/** UPDATE_TIME */
	private String updateTime;


	public String getId() {
		return StringUtil.nvl(id);
	}
	public void setId(String id) {
		this.id = id;
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
	public String getModelName() {
		return StringUtil.nvl(modelName);
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public String getDeviceType() {
		return StringUtil.nvl(deviceType);
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public String getIconUrl() {
		return StringUtil.nvl(iconUrl);
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	public String getDescription() {
		return StringUtil.nvl(description);
	}
	public void setDescription(String description) {
		this.description = description;
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
	public String getProfileVer() {
		return profileVer;
	}
	public void setProfileVer(String profileVer) {
		this.profileVer = profileVer;
	}
	public String getApplyYn() {
		return applyYn;
	}
	public void setApplyYn(String applyYn) {
		this.applyYn = applyYn;
	}

}
