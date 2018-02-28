package org.eclipse.leshan.server.extension.onem2m.resources;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Subscription extends Resource {
	public Subscription(){
		setShortName("sub");
		resetLbl();
		resetNu();
		setExpired();
	}
	
    // for create
    public JSONObject setForCreate() {

    	JSONObject body_wrap = new JSONObject();
    	JSONObject body = new JSONObject();
    	JSONArray lbl = new JSONArray();
    	JSONArray nu = new JSONArray();
    	
        try {
        	if( getLbl() != null ){
	            for(String label : getLbl()) {
	                lbl.put(label);
	            }
	            body.put("lbl", lbl);
        	}
        	
        	if( getNu() != null ){
	            for(String url : getNu()) {
	                nu.put(url);
	            }
	            body.put("nu", nu);
        	}
        	body.put("enc", getEnc());
        	body.put("rn", getRn());
            body.put("et", getEt());

            body_wrap.put(getNameSpace(this),body);

        } catch(JSONException jsone) {
            jsone.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        return body_wrap;
    }
    
    public Subscription makes(){
    	setResToJson(setForCreate());
    	return this;
    }
    
    public JSONObject getJSON(){
    	makes();
		return getResToJson();
	}
}
