package org.eclipse.leshan.server.onem2m.resources;

import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.leshan.server.Constants;
import org.eclipse.leshan.server.api.Util;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AE extends Resource{
	
	public AE(){
		setShortName("ae");
		setRn(Constants.BASIC_AE_NAME);
		setApi(Constants.BASIC_AE_NAME);
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
            body.put("apn", getApn());
            body.put("api", getApi());
            body.put("rr", isRr());
            body.put("et", getEt());

            body_wrap.put(getNameSpace(this),body);

        } catch(JSONException jsone) {
            jsone.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        return body_wrap;
    }
    
    public AE makes(){
    	setResToJson(setForCreate());
    	return this;
    }
    
    /*
    
    // retrieve
    public JSONObject retrieveAE(FoundItem oicDevice) {

        JSONObject jsonResponse = new JSONObject();

        try {
            String strOrigin = "OIC-" + this.deviceId;

            client.openConnection();
            client.setRequestHeader(this.host, this.deviceId, this.deviceId, strOrigin, MemberType.AE, Constants.REQUEST_METHOD_TYPE.GET.Value());
            jsonResponse = client.getResponse();

        }catch(IOException ioe) {
            ioe.printStackTrace();
        }catch(JSONException jsone) {
            jsone.printStackTrace();
        }catch(Exception e) {
            e.printStackTrace();
        }

        return jsonResponse;
    }
    // delete
    public JSONObject deleteAE(FoundItem oicDevice) {

        JSONObject jsonResponse = new JSONObject();

        try {
            client.openConnection();
            client.setRequestHeader(this.host, this.deviceId, this.deviceId, oicDevice.getResourceUri(), MemberType.AE, Constants.REQUEST_METHOD_TYPE.DELETE.Value());

            jsonResponse = client.getResponse();

        }catch(IOException ioe) {
            ioe.printStackTrace();
        }catch(JSONException jsone) {
            jsone.printStackTrace();
        }catch(Exception e) {
            e.printStackTrace();
        }

        return jsonResponse;
    }
    */
}
