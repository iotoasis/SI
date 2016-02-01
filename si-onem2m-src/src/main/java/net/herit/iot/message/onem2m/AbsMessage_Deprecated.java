package net.herit.iot.message.onem2m;

import java.util.HashMap;

import net.herit.iot.message.onem2m.format.Enums.CONTENT_TYPE;

public abstract class AbsMessage_Deprecated {
	public final static int NOT_SET = -1;
	
	public final static String NOTIFY_RESOURCE = "notify";
	
	protected String unvalidReason;
	
	protected String	requestId = null;
	protected String	to = null;
	protected String	from = null;
	protected String	originTime = null;
	protected String	resultExpireTime = null;
	protected int		eventCategory = NOT_SET;		// 100 ~ 999: User defined.
	protected byte[]	content = null;
	protected Object	contentObject = null;
	protected CONTENT_TYPE		contentType;
	protected String remoteHost = null;
	
	protected HashMap<String, String> userDefinedHeaders = new HashMap<String, String>();

	// Abstract method.
	public abstract boolean isValid();
	public abstract String getUnvalidReason();
	
	
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getOriginTime() {
		return originTime;
	}
	public void setOriginTime(String originTime) {
		this.originTime = originTime;
	}
	public String getResultExpireTime() {
		return resultExpireTime;
	}
	public void setResultExpireTime(String resultExpireTime) {
		this.resultExpireTime = resultExpireTime;
	}
	public int getEventCategory() {
		return eventCategory;
	}
	public void setEventCategory(int eventCategory) {
		this.eventCategory = eventCategory;
	}
	public byte[] getContent() throws Exception {
		
		return content;
	}
	public void setContent(byte[] content) {
		this.content = content;
	}
	
	public CONTENT_TYPE getContentType() {
		return contentType;
	}
	
	public void setContentType(CONTENT_TYPE contentType) {
		this.contentType = contentType;
	}
	
//	public void setContentType(String contentType) {
//		
//		if(contentType == null) return;
//		
//		String[] split = contentType.split(";");
//		for(String type : split) {
//			CONTENT_TYPE cont_type = CONTENT_TYPE.get(type);
//			if(cont_type != null) {PrimitiveContent
//				this.contentType = cont_type;
//				break;
//			}
//		}
//	}
	/**
	 * @return the contentObject
	 */
	public Object getContentObject() {
		return contentObject;
	}
	/**
	 * @param contentObject the contentObject to set
	 */
	public void setContentObject(Object contentObject) {
		this.contentObject = contentObject;
	}
	
	public void setRemoteHost(String host) {
		this.remoteHost = host;
	}
	
	public String getRemoteHost() {
		return this.remoteHost;
	}
	
	public HashMap<String, String> getUserDefinedHeaders() {
		return this.userDefinedHeaders;
	}
	
	public void addUserDefinedHeaders(String name, String value) {
		if (this.userDefinedHeaders == null) {
			this.userDefinedHeaders = new HashMap<String, String>();
		}
		this.userDefinedHeaders.put(name, value);
	}
}
