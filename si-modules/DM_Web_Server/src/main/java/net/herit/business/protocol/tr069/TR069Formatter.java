package net.herit.business.protocol.tr069;

import net.herit.business.protocol.constant.Target;

public class TR069Formatter {
	
	// singleton
	private static TR069Formatter instance = null;
	public static TR069Formatter getInstance(){
		if(instance == null){
			instance = new TR069Formatter();
		}
		return instance;
	}
	
	// resource uri
	public String getResourceUri(String resourceUri, Target to){
		String result = null;
		switch(to){
		case DM: 
			result = resourceUri.replace(".", "/");
			break;
		case ACS:
			result = resourceUri.replace("/", ".");
			break;
		}
		return result;
	}
	
	// device id
	public String getDeviceId(String deviceId, Target to){
		String result = null;
		switch(to){
		case DM: 
			result = deviceId.replace("-", "_");
			break;
		case ACS:
			result = deviceId.replace("_", "-");
			break;
		}
		return result;
	}
}
