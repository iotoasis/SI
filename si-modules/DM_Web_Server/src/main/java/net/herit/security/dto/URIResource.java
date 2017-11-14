package net.herit.security.dto;

import java.io.Serializable;

public class URIResource implements Serializable{
	
	private int uriResourceId;
	private String uri;
	private String description;
	private String isExcludeAuthentication;
	
	/* 데이터베이스가 boolean 을 지원할 경우 사용
	private boolean isExcludeAuthentication;
	*/
	
	public URIResource() {
	
	}

	public int getUriResourceId() {
		return uriResourceId;
	}

	public void setUriResourceId(int uriResourceId) {
		this.uriResourceId = uriResourceId;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIsExcludeAuthentication() {
		return isExcludeAuthentication;
	}

	public void setIsExcludeAuthentication(String isExcludeAuthentication) {
		this.isExcludeAuthentication = isExcludeAuthentication;
	}

	/* 데이터베이스가 boolean 을 지원할 경우 사용	
 	public boolean isExcludeAuthentication() {
		return isExcludeAuthentication;
	}

	public void setExcludeAuthentication(boolean isExcludeAuthentication) {
		this.isExcludeAuthentication = isExcludeAuthentication;
	}
	*/
	
}
