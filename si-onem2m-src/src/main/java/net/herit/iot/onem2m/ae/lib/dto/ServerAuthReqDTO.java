package net.herit.iot.onem2m.ae.lib.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "auth")
public class ServerAuthReqDTO {

	@XmlElement(name = "svcServerCdSeq")
	private String svcServerCdSeq;
	
	@XmlElement(name = "svcCdSeq")
	private String svcCdSeq;
	
	@XmlElement(name = "svcCdNum")
	private String svcCdNum;

	public String getSvcServerCdSeq() {
		return svcServerCdSeq;
	}

	public void setSvcServerCdSeq(String svcServerCdSeq) {
		this.svcServerCdSeq = svcServerCdSeq;
	}

	public String getSvcCdSeq() {
		return svcCdSeq;
	}

	public void setSvcCdSeq(String svcCdSeq) {
		this.svcCdSeq = svcCdSeq;
	}

	public String getSvcCdNum() {
		return svcCdNum;
	}

	public void setSvcCdNum(String svcCdNum) {
		this.svcCdNum = svcCdNum;
	}

}
