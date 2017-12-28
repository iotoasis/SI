package net.herit.business.onem2m.resource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AE extends OneM2MResource{
	
	// resource short name
	private String shortName = "ae";
	
	// init
	public AE(){
		resetLabel();
		setEt(getExpiredTime());
	}

	// pc
	private String rn = null;
	private String apn = null;
	private String api = null;
	private boolean rr = false;
	private String et = null;

	// getter and setter
	public String getRn() {
		return rn;
	}

	public void setRn(String rn) {
		this.rn = rn;
	}

	public String getApn() {
		return apn;
	}

	public void setApn(String apn) {
		this.apn = apn;
	}

	public String getApi() {
		return api;
	}

	public void setApi(String api) {
		this.api = api;
	}

	public boolean isRr() {
		return rr;
	}

	public void setRr(boolean rr) {
		this.rr = rr;
	}

	public String getEt() {
		return et;
	}

	public void setEt(String et) {
		this.et = et;
	}

	public String getShortName() {
		return shortName;
	}

}
