package org.eclipse.leshan.server.extension.onem2m;

import java.util.HashMap;

import org.eclipse.leshan.server.extension.Constants;

public class Onem2mHeaderMaker {
	
	// PCU header
	public HashMap<String, String> getBasicHeader(){
		return getBasicHeader("10.10.0.23","RES_"+Long.toHexString(System.currentTimeMillis()),"SAE");
	}
	public HashMap<String, String> getBasicHeader(String host, String ri, String origin){
		HashMap<String, String> header = new HashMap<String, String>();
		header.put("Accept", "application/json");
		header.put("HOST", host);
		header.put("X-M2M-RI", ri);
		header.put("X-M2M-Origin", origin);
		return header;
	}
	
	
	public HashMap<String, String> getResourceCreationHeader(int type){
		
		HashMap<String, String> header = null;
		
		switch(type){
		case Constants.AE :
			header = getBasicHeader("10.10.0.23","RES_"+Long.toHexString(System.currentTimeMillis()),"S");
			break;
		default :
			header = getBasicHeader();
			break;
		}
		
		header.put("Content-Type","application/vnd.onem2m-res+json; ty="+type);
		return header;
	}
	
}
