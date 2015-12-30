package net.herit.iot.onem2m.resource;

import java.sql.Timestamp;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlAccessType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "result",
    "resultCode",
    "commandId"
})
public class RestCommandResult {
	
	@XmlElement(name = "_result")
	private String result;
	
	@XmlElement(name = "_resultCode")
	private String resultCode;
	
	@XmlElement(name = "_commandId")
	private String commandId;
	
	/**
	 * @return the result
	 */
	public String getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(String result) {
		this.result = result;
	}

	/**
	 * @return the resultCode
	 */
	public String getResultCode() {
		return resultCode;
	}

	/**
	 * @param resultCode the resultCode to set
	 */
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
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
	
	protected static final String NL = "\r\n";
	public String toString() {
		
		StringBuilder sbld = new StringBuilder();
	
		sbld.append("[RestCommandResult]").append(NL);
		sbld.append("commandId:").append(commandId).append(NL);
		sbld.append("resultCode:").append(resultCode).append(NL);
		sbld.append("result:").append(result).append(NL);
		sbld.append(NL).append(super.toString());
		
		return sbld.toString();
	}
	
}
