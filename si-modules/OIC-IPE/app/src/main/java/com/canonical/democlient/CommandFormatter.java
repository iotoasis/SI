package com.canonical.democlient;

import org.json.JSONObject;

/**
 * Created by 문선호 on 2017-03-13.
 */
public class CommandFormatter {

    private static CommandFormatter instance = null;
    public static CommandFormatter getInstance(){
        if( instance == null ){
            instance = new CommandFormatter();
        }
        return instance;
    }

    public JSONObject getOrderForm(String type, String val){
        JSONObject result = new JSONObject();
        try {
            result.put("type", type);
            result.put("ord", "exec");
            result.put("val", Integer.parseInt(val));
        } catch(Exception e) {
            e.printStackTrace();
        }

        return result;
    }


    public JSONObject getReceiveForm(String type){
        JSONObject result = new JSONObject();
        try {
            result.put("type", type);
            result.put("ord", "read");
        } catch(Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
