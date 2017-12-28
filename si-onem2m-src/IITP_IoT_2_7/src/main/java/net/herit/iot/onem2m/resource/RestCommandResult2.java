package net.herit.iot.onem2m.resource;

import java.sql.Timestamp;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlAccessType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "commandId",
    "uri",
    "resultCode",
    "deviceStatus",
    "timestamp"
})
public class RestCommandResult2 {
	
	@XmlElement(name = "_uri")
	private String uri;
	
	@XmlElement(name = "_resultCode")
	private String resultCode;
	
	@XmlElement(name = "deviceStatus")
	private String deviceStatus;
	
	@XmlElement(name = "timestamp")
	private String timestamp;
	
	@XmlElement(name = "_commandId")
	private String commandId;
	
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
	
	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
	
	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getDeviceStatus() {
		return deviceStatus;
	}

	public void setDeviceStatus(String deviceStatus) {
		this.deviceStatus = deviceStatus;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
	protected static final String NL = "\r\n";
	public String toString() {
		
		StringBuilder sbld = new StringBuilder();
	
		sbld.append("[RestCommandResult2]").append(NL);
		sbld.append("commandId:").append(commandId).append(NL);
		sbld.append("uri:").append(uri).append(NL);
		sbld.append("deviceStatus:").append(deviceStatus).append(NL);
		sbld.append("timestamp:").append(timestamp).append(NL);
		sbld.append(NL).append(super.toString());
		
		return sbld.toString();
	}
	
}
