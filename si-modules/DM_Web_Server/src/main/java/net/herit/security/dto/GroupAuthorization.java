package net.herit.security.dto;

import java.io.Serializable;

public class GroupAuthorization implements Serializable{

	private int groupId;
	private String resource;
	private String authorizationDBCreate;
	private String authorizationDBRead;
	private String authorizationDBUpdate;
	private String authorizationDBDelete;
	
	/*	boolean을 데이터베이스가 지원할 경우 사용
 	private boolean authorizationDBCreate;
	private boolean authorizationDBRead;
	private boolean authorizationDBUpdate;
	private boolean authorizationDBDelete; 
	*/
	
	public GroupAuthorization() {
	
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public String getAuthorizationDBCreate() {
		return authorizationDBCreate;
	}

	public void setAuthorizationDBCreate(String authorizationDBCreate) {
		this.authorizationDBCreate = authorizationDBCreate;
	}

	public String getAuthorizationDBRead() {
		return authorizationDBRead;
	}

	public void setAuthorizationDBRead(String authorizationDBRead) {
		this.authorizationDBRead = authorizationDBRead;
	}

	public String getAuthorizationDBUpdate() {
		return authorizationDBUpdate;
	}

	public void setAuthorizationDBUpdate(String authorizationDBUpdate) {
		this.authorizationDBUpdate = authorizationDBUpdate;
	}

	public String getAuthorizationDBDelete() {
		return authorizationDBDelete;
	}

	public void setAuthorizationDBDelete(String authorizationDBDelete) {
		this.authorizationDBDelete = authorizationDBDelete;
	}
	
	/* 데이터 베이스가 boolean 을 지원할 경우 사용
	 
 	public boolean isAuthorizationDBCreate() {
		return authorizationDBCreate;
	}

	public void setAuthorizationDBCreate(boolean authorizationDBCreate) {
		this.authorizationDBCreate = authorizationDBCreate;
	}

	public boolean isAuthorizationDBRead() {
		return authorizationDBRead;
	}

	public void setAuthorizationDBRead(boolean authorizationDBRead) {
		this.authorizationDBRead = authorizationDBRead;
	}

	public boolean isAuthorizationDBUpdate() {
		return authorizationDBUpdate;
	}

	public void setAuthorizationDBUpdate(boolean authorizationDBUpdate) {
		this.authorizationDBUpdate = authorizationDBUpdate;
	}

	public boolean isAuthorizationDBDelete() {
		return authorizationDBDelete;
	}

	public void setAuthorizationDBDelete(boolean authorizationDBDelete) {
		this.authorizationDBDelete = authorizationDBDelete;
	}
	*/
}
