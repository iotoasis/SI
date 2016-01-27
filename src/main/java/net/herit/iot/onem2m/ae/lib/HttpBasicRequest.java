package net.herit.iot.onem2m.ae.lib;

import java.util.HashMap;

public class HttpBasicRequest {

	private String method;
	private String uri;
	private String host;
	private byte[] content;
	private HashMap<String, String> headers;
		
	public HttpBasicRequest(String method, String uri, String host, byte[] content2) {
		this.method = method;
		this.uri = uri;
		this.host = host;
		this.content = content2;
	}
	
	//reqMessage = RequestCodec.decode(method, uri, headerMap, host, content);
	/**
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}
	/**
	 * @param method the method to set
	 */
	public void setMethod(String method) {
		this.method = method;
	}
	/**
	 * @return the uri
	 */
	public String getUri() {
		return uri;
	}
	/**
	 * @param uri the uri to set
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}
	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}
	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
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
}
