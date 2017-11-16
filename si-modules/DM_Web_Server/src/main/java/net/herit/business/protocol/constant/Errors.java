package net.herit.business.protocol.constant;

public enum Errors {
	// Success
	ERR_000("The device has disconnected without any of error."),
	ERR_001("The device has connected without any of error."),
	
	// Error - Format Error

	// Error - DM Error
	ERR_500("A value not expected has returned.");
	
	
	
	

	
	private String msg;
	Errors(String msg){
		this.msg = msg;
	}
	public String getMsg(){
		return this.msg;
	}
}
