package net.herit.business.protocol.model;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;

public interface EtcProtocol {
	
	public JSONObject connect();
	
	public JSONObject disconnect();
	
	
}
