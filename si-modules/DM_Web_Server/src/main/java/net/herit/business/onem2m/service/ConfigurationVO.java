package net.herit.business.onem2m.service;

import java.io.Serializable;

import org.springframework.data.annotation.Id;

@SuppressWarnings("serial")
public class ConfigurationVO implements Serializable {
	
	@Id
	private String id;
	
	private String CONFIGURATION_NAME;
	private String maxPollingSessionNo;
	private String maxAENo;
	private String maxCSENo;
	private String maxTps;
	private String updateTime;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCONFIGURATION_NAME() {
		return CONFIGURATION_NAME;
	}
	public void setCONFIGURATION_NAME(String cONFIGURATION_NAME) {
		CONFIGURATION_NAME = cONFIGURATION_NAME;
	}
	public String getMaxPollingSessionNo() {
		return maxPollingSessionNo;
	}
	public void setMaxPollingSessionNo(String maxPollingSessionNo) {
		this.maxPollingSessionNo = maxPollingSessionNo;
	}
	public String getMaxAENo() {
		return maxAENo;
	}
	public void setMaxAENo(String maxAENo) {
		this.maxAENo = maxAENo;
	}
	public String getMaxCSENo() {
		return maxCSENo;
	}
	public void setMaxCSENo(String maxCSENo) {
		this.maxCSENo = maxCSENo;
	}
	public String getMaxTps() {
		return maxTps;
	}
	public void setMaxTps(String maxTps) {
		this.maxTps = maxTps;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

}
