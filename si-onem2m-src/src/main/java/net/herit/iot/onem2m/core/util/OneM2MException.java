package net.herit.iot.onem2m.core.util;

import net.herit.iot.message.onem2m.OneM2mResponse.RESPONSE_STATUS;

public class OneM2MException extends Exception {
	
	public OneM2MException(RESPONSE_STATUS statusCode, String msg) {
		super(msg);
		setResponseStatusCode(statusCode);
	}
	
	/**
	 * @return the errorCode
	 */
	public RESPONSE_STATUS getResponseStatusCode() {
		return responseStatus;
	}

	/**
	 * @param errorCode the errorCode to set
	 */
	public void setResponseStatusCode(RESPONSE_STATUS status) {
		this.responseStatus = status;
	}

	protected RESPONSE_STATUS responseStatus;
	
}
