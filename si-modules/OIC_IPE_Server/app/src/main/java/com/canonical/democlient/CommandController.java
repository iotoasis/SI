package com.canonical.democlient;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Base64;

import org.json.JSONObject;

/**
 * Created by 문선호 on 2017-03-10.
 */
public class CommandController {

    private PageHandler ph = new PageHandler();

    private JSONObject con;
    private String response;
    public CommandController(String response){
        this.response = response;
    }

    public JSONObject handle(){

        // con 내용 JSON 형태로 변경
        // null인지 확인
        if( Util.isNoE(response) ){
            System.err.println("response is null");
        } else if( response.indexOf("<con>") > -1 && response.indexOf("</con>") > -1 ) {	// con 태그가 존재하는지 확인
            try{
                String strCon = new String(Base64.decode(Util.getValue(response, "con"), 0));
                JSONObject jCon = new JSONObject(strCon);
                if( jCon.getString("ord").equals("exec") ){
                    switch( jCon.getString("type") ){
                        case "/led":
                            msg_type_set = "msg_type_led_a_set";
                            msg_content_set = "progress";
                            break;
                        case "/buzzer":
                            msg_type_set = "msg_type_buzzer_a_set";
                            msg_content_set = "tone";
                            break;
                    }
                } else if( jCon.getString("ord").equals("read") ) {
                    switch( jCon.getString("type") ){
                        case "/temperature":
                            msg_type_set = "msg_type_led_a_set";
                            msg_content_set = "progress";
                            break;
                        case "/":
                            msg_type_set = "msg_type_buzzer_a_set";
                            msg_content_set = "tone";
                            break;
                    }
                }


            } catch(Exception e) {
                e.printStackTrace();
            }
        }


            JSONObject resp = null;
            OrderExecutor oe = new OrderExecutor(item);
            oe.execute();

            System.out.println("[   resp   ]   "+resp);
        } catch(Exception e) {}

        return result;
    }

}
