package net.herit.iot.onem2m.ae.lib;

import java.util.HashMap;

public class HttpBasicResponse {

	private String version;
	private String statusCode;
	private String statusMessage;
	private byte content[];
	private HashMap<String, String> headers;
		
	public HttpBasicResponse(String version, String statusCode, String statusMessage, byte[] content) {
		this.version = version;
		this.statusCode = statusCode;
		this.statusMessage = statusMessage;
		this.content = content;
	}
	
	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return the statusCode
	 */
	public String getStatusCode() {
		return statusCode;
	}

	/**
	 * @param statusCode the statusCode to set
	 */
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	/**
	 * @return the statusMessage
	 */
	public String getStatusMessage() {
		return statusMessage;
	}

	/**
	 * @param statusMessage the statusMessage to set
	 */
	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	/**
	 * @return the content
	 */
	public byte[] getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(byte[] content) {
		this.content = content;
	}

	/**
	 * @param headers the headers to set
	 */
	public void setHeaders(HashMap<String, String> headers) {
		this.headers = headers;
	}
		
	/**
	 * @return the headers
	 */
	public HashMap<String, String> getHeaders() {
		return headers;
	}
	/**
	 * @param headers the headers to set
	 */
	public void addHeader(String key, String value) {
		if (this.headers == null) {
			this.headers = new HashMap<String, String>();
		}
		this.headers.put(key,  value);
	}
	/**
	 * @param headers the headers to set
	 */
	public void addHeader(String key, int value) {
		if (this.headers == null) {
			this.headers = new HashMap<String, String>();
		}
		this.headers.put(key,  Integer.toString(value));
	}
}
