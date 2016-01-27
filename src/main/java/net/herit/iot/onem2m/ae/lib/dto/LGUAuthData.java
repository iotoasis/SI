package net.herit.iot.onem2m.ae.lib.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * LGU+ OneM2M MES서버 연동을 위한 인증정보 클래스
 * 수신 메시지에 포함된 XML과 매핑됨
 *
 * @version : 1.0
 * @author  : Lee inseuk
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "authdata")
public class LGUAuthData {
	private static final String NL = "\r\n";

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlRootElement(name = "http")
	public static class Http {
		@XmlElement(name = "entityId")
		private String entityId;

		@XmlElement(name = "enrmtKey")
		private String enrmtKey;

		@XmlElement(name = "token")
		private String token;

		public String toString() {
			StringBuilder sbd = new StringBuilder();
			sbd.append("http] entityId=").append(entityId).append(NL);
			sbd.append("\tenrmtKey=").append(enrmtKey).append(NL);
			sbd.append("\ttoken=").append(token).append(NL);

			return sbd.toString();
		}

		public String getEntityId() {
			return entityId;
		}
		public void setEntityId(String entityId) {
			this.entityId = entityId;
		}
		public String getEnrmtKey() {
			return enrmtKey;
		}
		public void setEnrmtKey(String enrmtKey) {
			this.enrmtKey = enrmtKey;
		}
		public String getToken() {
			return token;
		}
		public void setToken(String token) {
			this.token = token;
		}

	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlRootElement(name = "coap")
	public static class Coap {
		@XmlElement(name = "entityId")
		private String entityId;

		@XmlElement(name = "enrmtKey")
		private String enrmtKey;

		@XmlElement(name = "token")
		private String token;

		@XmlElement(name = "encryptionMethod")
		private String encryptionMethod;

		public String toString() {
			StringBuilder sbd = new StringBuilder();
			sbd.append("coap] entityId=").append(entityId).append(NL);
			sbd.append("\tenrmtKey=").append(enrmtKey).append(NL);
			sbd.append("\ttoken=").append(token).append(NL);
			sbd.append("\tencryptionMethod=").append(encryptionMethod).append(NL);

			return sbd.toString();
		}

		public String getEntityId() {
			return entityId;
		}
		public void setEntityId(String entityId) {
			this.entityId = entityId;
		}
		public String getEnrmtKey() {
			return enrmtKey;
		}
		public void setEnrmtKey(String enrmtKey) {
			this.enrmtKey = enrmtKey;
		}
		public String getToken() {
			return token;
		}
		public void setToken(String token) {
			this.token = token;
		}
		public String getEncryptionMethod() {
			return encryptionMethod;
		}
		public void setEncryptionMethod(String encryptionMethod) {
			this.encryptionMethod = encryptionMethod;
		}

	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlRootElement(name = "mqtt")
	public static class Mqtt {
		@XmlElement(name = "entityId")
		private String entityId;

		@XmlElement(name = "enrmtKey")
		private String enrmtKey;

		@XmlElement(name = "token")
		private String token;

		@XmlElement(name = "certUrl")
		private String certUrl;

		public String toString() {
			StringBuilder sbd = new StringBuilder();
			sbd.append("mqtt] entityId=").append(entityId).append(NL);
			sbd.append("\tenrmtKey=").append(enrmtKey).append(NL);
			sbd.append("\ttoken=").append(token).append(NL);
			sbd.append("\tcertUrl=").append(certUrl).append(NL);

			return sbd.toString();
		}

		public String getEntityId() {
			return entityId;
		}
		public void setEntityId(String entityId) {
			this.entityId = entityId;
		}
		public String getEnrmtKey() {
			return enrmtKey;
		}
		public void setEnrmtKey(String enrmtKey) {
			this.enrmtKey = enrmtKey;
		}
		public String getToken() {
			return token;
		}
		public void setToken(String token) {
			this.token = token;
		}
		public String getCertUrl() {
			return certUrl;
		}
		public void setCertUrl(String certUrl) {
			this.certUrl = certUrl;
		}
	}

	private Http http;
	private Coap coap;
	private Mqtt mqtt;

	private String keId;

	private String deviceModel;
	private String networkInfo;

	public String toString() {
		StringBuilder sbd = new StringBuilder();

		sbd.append("AuthData] ").append(http).append(coap).append(mqtt);
		sbd.append("\tkeId=").append(keId).append(NL);
		sbd.append("\tdeviceModel=").append(deviceModel).append(NL);
		sbd.append("\tnetworkInfo=").append(networkInfo).append(NL);

		return sbd.toString();
	}

	public Http getHttp() {
		return http;
	}
	public void setHttp(Http http) {
		this.http = http;
	}
	public Coap getCoap() {
		return coap;
	}
	public void setCoap(Coap coap) {
		this.coap = coap;
	}
	public Mqtt getMqtt() {
		return mqtt;
	}
	public void setMqtt(Mqtt mqtt) {
		this.mqtt = mqtt;
	}
	public String getDeviceModel() {
		return deviceModel;
	}
	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}
	public String getNetworkInfo() {
		return networkInfo;
	}
	public void setNetworkInfo(String networkInfo) {
		this.networkInfo = networkInfo;
	}
	public String getKeId() {
		return keId;
	}
	public void setKeId(String keId) {
		this.keId = keId;
	}

}