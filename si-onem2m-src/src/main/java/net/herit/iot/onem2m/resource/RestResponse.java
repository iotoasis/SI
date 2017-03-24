package net.herit.iot.onem2m.resource;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlAccessType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "code",
    "message",
    "content",
    "commandId"
})
public class RestResponse {
	
	@XmlElement(name = "code")
	private String code;
	
	@XmlElement(name = "message")
	private String message;
	
	@XmlElement(name = "content")
	private String content;
	
	@XmlElement(name = "_commandId")
	private String commandId;
	
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param commandId the commandId to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the commandId
	 */
	public String getCommandId() {
		return commandId;
	}

	/**
	 * @param command the command to set
	 */
	public void setCommandId(String commandId) {
		this.commandId = commandId;
	}

	
	protected static final String NL = "\r\n";
	public String toString() {
		
		StringBuilder sbld = new StringBuilder();
	
		sbld.append("[SSCommand]").append(NL);
		sbld.append("code:").append(code).append(NL);
		sbld.append("message:").append(message).append(NL);
		sbld.append("content:").append(content).append(NL);
		sbld.append("commandId:").append(commandId).append(NL);
		sbld.append(NL).append(super.toString());
		
		return sbld.toString();
	}
	
}
