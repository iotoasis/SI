package net.herit.business.onem2m;

import java.util.HashMap;

public class OneM2MInitData {

	private static OneM2MInitData INSTANCE;
	
	public OneM2MInitData() {
		
	}
	
	public static OneM2MInitData getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new OneM2MInitData();
			return INSTANCE;
		}
		else return INSTANCE;
	}
	
	public HashMap<String, Object> data = new HashMap<String, Object>();
}
