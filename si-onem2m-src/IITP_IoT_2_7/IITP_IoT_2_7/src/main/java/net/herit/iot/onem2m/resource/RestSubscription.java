package net.herit.iot.onem2m.resource;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlAccessType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "uri",
    "notificationUri",
    "commandId"
})
public class RestSubscription {
	public final static String SCHEMA_LOCATION = "CDT-software-v1_2_0.xsd";
	
	@XmlElement(name = "_uri")
	private String uri;
	
	@XmlElement(name = "_notificationUri")
	private String notificationUri;
	
	@XmlElement(name = "_commandId")
	private String commandId;		// added in 2016-12-07

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
	
	public String getCommandId() {
		return commandId;
	}

	public void setCommandId(String commandId) {
		this.commandId = commandId;
	}

	protected static final String NL = "\r\n";
	public String toString() {
		
		StringBuilder sbld = new StringBuilder();
	
		sbld.append("[SSCommand]").append(NL);
		sbld.append("notificationUri:").append(notificationUri).append(NL);
		sbld.append("uri:").append(uri).append(NL);
		sbld.append("commandId:").append(commandId).append(NL);
		sbld.append(NL).append(super.toString());
		
		return sbld.toString();
	}
}
