package net.herit.business.onem2m.service;

import java.io.Serializable;

import org.springframework.data.annotation.Id;

@SuppressWarnings("serial")
public class PlatformVO implements Serializable {
	
	@Id
	private String id;

	private String spId;
	private String serverName;
	private String serverHost;
	private String serverPort;
	private String protocol;
	private String cseId;
	private String cseName;
	private String maxTps;
	private String createTime;
	private String updateTime;
	
	/** 선택삭제리스트 */
	private String[] checkList = null;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSpId() {
		return spId;
	}
	public void setSpId(String spId) {
		this.spId = spId;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public String getServerHost() {
		return serverHost;
	}
	public void setServerHost(String serverHost) {
		this.serverHost = serverHost;
	}
	public String getServerPort() {
		return serverPort;
	}
	public void setServerPort(String serverPort) {
		this.serverPort = serverPort;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public String getCseId() {
		return cseId;
	}
	public void setCseId(String cseId) {
		this.cseId = cseId;
	}
	public String getCseName() {
		return cseName;
	}
	public void setCseName(String cseName) {
		this.cseName = cseName;
	}
	public String getMaxTps() {
		return maxTps;
	}
	public void setMaxTps(String maxTps) {
		this.maxTps = maxTps;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String[] getCheckList() {
		return checkList;
	}
	public void setCheckList(String[] checkList) {
		this.checkList = checkList;
	}

}
