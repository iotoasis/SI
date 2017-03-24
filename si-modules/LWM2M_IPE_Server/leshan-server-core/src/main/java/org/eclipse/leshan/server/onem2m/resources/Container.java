package org.eclipse.leshan.server.onem2m.resources;

import org.eclipse.leshan.server.Constants;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Container extends Resource{
	
	public Container(){
		setShortName("cnt");
		setMni(100);
		setMbs(1000 * 1024);
		setMia(36000);
		resetLbl();
		setExpired();
	}
	
    // for create
    public JSONObject setForCreate() {

    	JSONObject body_wrap = new JSONObject();
    	JSONObject body = new JSONObject();
    	JSONArray lbl = new JSONArray();
    	
        try {
        	if( getLbl() != null ){
	            for(String label : getLbl()) {
	                lbl.put(label);
	            }
	            body.put("lbl", lbl);
        	}
        	body.put("rn", getRn());
            body.put("mni", getMni());
            body.put("mbs", getMbs());
            body.put("mia", getMia());
            
            body.put("et", getEt());

            body_wrap.put(getNameSpace(this),body);

        } catch(JSONException jsone) {
            jsone.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        return body_wrap;
    }
    
    public Container makes(){
    	setResToJson(setForCreate());
    	return this;
    }
}
