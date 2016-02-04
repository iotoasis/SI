package net.herit.iot.onem2m.resource;

import java.sql.Timestamp;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlAccessType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "uri",
    "notificationUri",
    "commandId",
    "command",
    "contentInfo",
    "content",
    "createTime"
})
public class RestCommand {
	
	@XmlElement(name = "_uri")
	private String uri;
	
	@XmlElement(name = "_notificationUri")
	private String notificationUri;
	
	@XmlElement(name = "_commandId")
	private String commandId;
	
	@XmlElement(name = "_command")
	private String command;
	
	@XmlElement(name = "cnf")
	private String contentInfo;
	
	@XmlElement(name = "con")
	private String content;

	
	@XmlElement(name = "createTime")
	private Timestamp createTime;

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
	 * @return the notificationUri
	 */
	public String getNotificationUri() {
		return notificationUri;
	}

	/**
	 * @param notificationUri the notificationUri to set
	 */
	public void setNotificationUri(String notificationUri) {
		this.notificationUri = notificationUri;
	}

	/**
	 * @return the commandId
	 */
	public String getCommandId() {
		return commandId;
	}

	/**
	 * @param commandId the commandId to set
	 */
	public void setCommandId(String commandId) {
		this.commandId = commandId;
	}

	/**
	 * @return the command
	 */
	public String getCommand() {
		return command;
	}

	/**
	 * @param command the command to set
	 */
	public void setCommand(String command) {
		this.command = command;
	}

	/**
	 * @return the contentInfo
	 */
	public String getContentInfo() {
		return contentInfo;
	}

	/**
	 * @param contentInfo the ContentInfo to set
	 */
	public void setContentInfo(String contentInfo) {
		this.contentInfo = contentInfo;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param contenet the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the createTime
	 */
	public Timestamp getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}	
	
	protected static final String NL = "\r\n";
	public String toString() {
		
		StringBuilder sbld = new StringBuilder();
	
		sbld.append("[SSCommand]").append(NL);
		sbld.append("command:").append(command).append(NL);
		sbld.append("commandId:").append(commandId).append(NL);
		sbld.append("content:").append(content).append(NL);
		sbld.append("contentInfo:").append(contentInfo).append(NL);
		sbld.append("notificationUri:").append(notificationUri).append(NL);
		sbld.append("uri:").append(uri).append(NL);
		sbld.append(NL).append(super.toString());
		
		return sbld.toString();
	}
	
}
