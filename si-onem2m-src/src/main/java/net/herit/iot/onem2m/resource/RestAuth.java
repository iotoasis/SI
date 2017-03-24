package net.herit.iot.onem2m.resource;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "devId",
    "pwd",
    "key"
})
public class RestAuth {
	@XmlElement(name = "_devId")
	private String devId;
	
	@XmlElement(name = "_pwd")
	private String pwd;
	
	@XmlElement(name = "_key")
	private String key;

	public String getDevId() {
		return devId;
	}

	public void setDevId(String devId) {
		this.devId = devId;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	protected static final String NL = "\r\n";
	public String toString() {
		
		StringBuilder sbld = new StringBuilder();
	
		sbld.append("[ResAuth]").append(NL);
		sbld.append("devId:").append(devId).append(NL);
		sbld.append("pwd:").append(pwd).append(NL);
		sbld.append("key:").append(key).append(NL);
		sbld.append(NL).append(super.toString());
		
		return sbld.toString();
	}
}
