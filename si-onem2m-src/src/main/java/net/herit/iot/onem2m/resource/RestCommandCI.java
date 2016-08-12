package net.herit.iot.onem2m.resource;

import java.sql.Timestamp;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlAccessType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
	    "execId",
	    "execResult",
	    "data"
})
public class RestCommandCI {
	
	@XmlElement(name = "exec_id")
	private String execId;

	
	@XmlElement(name = "exec_result")
	private String execResult;

	
	@XmlElement(name = "data")
	private String data;

	/**
	 * @return the execId
	 */
	public String getExecId() {
		return execId;
	}

	/**
	 * @param uri the uri to set
	 */
	public void setExecId(String execId) {
		this.execId = execId;
	}

	/**
	 * @return the execResult
	 */
	public String getExecResult() {
		return execResult;
	}

	/**
	 * @param uri the execResult to set
	 */
	public void setExecResult(String execResult) {
		this.execResult = execResult;
	}

	/**
	 * @return the data
	 */
	public String getData() {
		return data;
	}

	/**
	 * @param uri the data to set
	 */
	public void setData(String data) {
		this.data = data;
	}
}
