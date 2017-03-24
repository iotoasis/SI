package net.herit.business.device.service;

import java.io.Serializable;

public class ExtMoProfileVO implements Serializable {

	/** MO_PROFILE_ID */
	private String moProfileId;
	/** DM_TYPE */
	private String dmType;
	/** EXT_RESOURCE_URI */
	private String extResourceUri;
	
	
	public String getMoProfileId() {
		return moProfileId;
	}
	public void setMoProfileId(String moProfileId) {
		this.moProfileId = moProfileId;
	}
	public String getDmType() {
		return dmType;
	}
	public void setDmType(String dmType) {
		this.dmType = dmType;
	}
	public String getExtResourceUri() {
		return extResourceUri;
	}
	public void setExtResourceUri(String extResourceUri) {
		this.extResourceUri = extResourceUri;
	}
}
