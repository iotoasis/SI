package org.eclipse.leshan.server.extension;

import org.eclipse.leshan.server.client.Client;

public class Lwm2mVO {
		
	private String authId = null;
	private String authPwd = null;
	private Client client = null;
	
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
	public Client getClient() {
		return client;
	}
	public void setClient(Client client) {
		this.client = client;
	}
	
	private int currCount = 0;

	public int getCurrCount() {
		return currCount;
	}
	public void setCurrCount(int currCount) {
		this.currCount = currCount;
	}
	
}
