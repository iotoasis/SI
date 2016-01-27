package net.herit.iot.onem2m.ae.lib.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * LGU+ OneM2M MEF 연동을 위한 인증 요청 정보 클래스
 * @author eastflag
 *
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "auth")
public class AuthReqDTO {
	@XmlElement(name = "deviceModel")
	private String deviceModel;
	
	@XmlElement(name = "deviceSerialNo")
	private String deviceSerialNo;
	
	@XmlElement(name = "serviceCode")
	private String serviceCode;
	
	@XmlElement(name = "deviceType")
	private String deviceType;
	
	@XmlElement(name = "ctn")
	private String ctn;
	
	@XmlElement(name = "mac")
	private String mac;
	
	@XmlElement(name = "iccId")
	private String iccId;
	
	public String getDeviceModel() {
		return deviceModel;
	}
	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}
	
	public String getDeviceSerialNo() {
		return deviceSerialNo;
	}
	public void setDeviceSerialNo(String deviceSerialNo) {
		this.deviceSerialNo = deviceSerialNo;
	}
	
	public String getServiceCode() {
		return serviceCode;
	}
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	
	public String getCtn() {
		return ctn;
	}
	public void setCtn(String ctn) {
		this.ctn = ctn;
	}
	
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	
	public String getIccId() {
		return iccId;
	}
	public void setIccId(String iccId) {
		this.iccId = iccId;
	}
}
