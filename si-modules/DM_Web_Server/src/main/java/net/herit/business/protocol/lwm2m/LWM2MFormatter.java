package net.herit.business.protocol.lwm2m;

import net.herit.business.protocol.constant.Target;

public class LWM2MFormatter {
	// singleton
	private static LWM2MFormatter instance = null;
	public static LWM2MFormatter getInstance(){
		if(instance == null){
			instance = new LWM2MFormatter();
		}
		return instance;
	}
	
	// resource uri
	public String getResourceUri(String resourceUri, Target to){
		String result = null;
		switch(to){
		case DM: 
			result = resourceUri.replace("-", "0");
			break;
		case IPE:
			result = resourceUri.replace("0", "-");
			break;
		}
		return result;
	}
	
}
