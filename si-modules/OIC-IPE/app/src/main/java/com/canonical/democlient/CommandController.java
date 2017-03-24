package com.canonical.democlient;

import android.util.Base64;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by 문선호 on 2017-03-10.
 */
public class CommandController {

    private String msg_type_set = null;
    private String msg_content_set = null;
    private double readValue = 0.0;
    private HashMap<String, DemoResource> resource = null;
    JSONObject jCon = null;

    private String response;
    public CommandController(String response, HashMap<String, DemoResource> resource){
        this.response = response;
        this.resource = resource;
    }

    public JSONObject handle(){

        // con 내용 JSON 형태로 변경
        // null인지 확인
        if( Util.isNoE(response) ){
            System.err.println("response is null");
        } else if( response.indexOf("<con>") > -1 && response.indexOf("</con>") > -1 ) {	// con 태그가 존재하는지 확인
            try{
                String strCon = new String(Base64.decode(Util.getValue(response, "con"), 0));
                jCon = new JSONObject(strCon);
                System.out.println(jCon.toString());
                if( jCon.getString("ord").equals("exec") ){
                    switch( jCon.getString("type") ){
                        case "led":
                            msg_type_set = "msg_type_led_a_set";
                            msg_content_set = "progress";
                            break;
                        case "buzzer":
                            msg_type_set = "msg_type_buzzer_a_set";
                            msg_content_set = "tone";
                            break;
                    }

                    jCon.put("msg_type_set",msg_type_set);
                    jCon.put("msg_content_set",msg_content_set);

                } else if( jCon.getString("ord").equals("read") ) {
                    switch( jCon.getString("type") ){
                        case "temperature":
                            readValue = ((SensorResourceA)resource.get("sensor_a")).getTempValue();
                            break;
                        case "light":
                            readValue = ((SensorResourceA)resource.get("sensor_a")).getLightValue();
                            break;
                        case "sound":
                            readValue = ((SensorResourceA)resource.get("sensor_a")).getSoundValue();
                            break;
                        case "button":
                            readValue = ((ButtonResourceA)resource.get("button_a")).getButtonValue();
                            break;
                        case "touch":
                            readValue = ((ButtonResourceA)resource.get("button_a")).getTouchValue();
                            break;
                    }

                    jCon.put("value",readValue);
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        return jCon;
    }

}
