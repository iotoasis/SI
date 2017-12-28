package net.herit.iot.onem2m.incse.controller.dm;


public class HitDMException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public HitDMException(int statusCode, String msg) {
		super(msg);
		setStatusCode(statusCode);
	}
	
	/**
	 * @return the errorCode
	 */
	public int getStatusCode() {
		return status;
	}

	/**
	 * @param errorCode the errorCode to set
	 */
	public void setStatusCode(int status) {
		this.status = status;
	}

	protected int status;
	
}
