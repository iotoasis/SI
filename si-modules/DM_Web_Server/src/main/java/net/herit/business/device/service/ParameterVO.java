package net.herit.business.device.service;

import java.io.Serializable;

import net.herit.common.util.StringUtil;


public class ParameterVO implements Serializable{

	/** page */
	private int page;
	/** SN */
	private String sn;
	/** DEVICE_MODEL */
	private String deviceModel;
	/** MODEL_NAME */
	private String modelName;
	/** OUI */
	private String oui;
	/** PACKAGE */
	private String packageName;

	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public String getSn() {
		return StringUtil.nvl(sn);
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public void setDeviceModel(String deviceModel) {
		this.deviceModel= deviceModel;
	}
	public String getDeviceModel() {
		return StringUtil.nvl(deviceModel);
	}
	public void setModelName(String modelName) {
		this.modelName= modelName;
	}
	public String getModelName() {
		return StringUtil.nvl(modelName);
	}
	public void setOui(String oui) {
		this.oui= oui;
	}
	public String getOui() {
		return StringUtil.nvl(oui);
	}
	public void setPackageName(String packageName) {
		this.packageName= packageName;
	}
	public String getPackageName() {
		return StringUtil.nvl(packageName);
	}

}
