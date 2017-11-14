package net.herit.business.etcprotocol.model;

import org.json.JSONObject;

public interface EtcProtocol {
	
	public JSONObject connect();
	
	public JSONObject disconnect();
	
}
