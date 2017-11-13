package org.eclipse.leshan.server.extension.onem2m.resources;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.leshan.server.extension.Constants;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ContentInstance extends Resource{
	public ContentInstance(){
        setShortName("cin");
        setRn(getShortName()+"_"+getEt());
        setCnf("application/json:1");
        resetLbl();
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
            body.put("cnf", getCnf());
            body.put("con", getCon());
            body.put("et", getEt());

            body_wrap.put(getNameSpace(this),body);

        } catch(JSONException jsone) {
            jsone.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        }

        return body_wrap;
    }

    public ContentInstance makes(){
        setResToJson(setForCreate());
        return this;
    }
}
