package net.herit.iot.onem2m.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.herit.iot.message.onem2m.OneM2mResponse.RESPONSE_STATUS;

public class OneM2MException extends Exception {
	
	private Logger log = LoggerFactory.getLogger(OneM2MException.class);
	
	public OneM2MException(RESPONSE_STATUS statusCode, String msg) {
		super(msg);
		setResponseStatusCode(statusCode);
	}
	
	/**
	 * @return the errorCode
	 */
	public RESPONSE_STATUS getResponseStatusCode() {
		log.error("OneM2MException: " + responseStatus, this);
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
