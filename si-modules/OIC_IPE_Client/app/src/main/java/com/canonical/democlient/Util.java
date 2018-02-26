package com.canonical.democlient;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 문선호 on 2016-09-28.
 */
public class Util {

    public static String makeUrl(String... str){
        StringBuffer sb = new StringBuffer();
        for( int i=0; i<str.length; i++ ) {
            sb.append(str[i]);
            if( i<str.length-1 ) {
                sb.append("/");
            }
        }
        return sb.toString();
    }

    public static String makeUrl(String baseUrl, Object... objs){
        StringBuffer sb = new StringBuffer(baseUrl);
        sb.append("/");
        try{
            for( int i=0; i<objs.length; i++ ) {
                sb.append( ((JSONObject)objs[i]).getString("rn") );
                if( i<objs.length-1 ) {
                    sb.append("/");
                }
            }
        } catch(JSONException je) {
            Log.i("UTIL",">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> makeUrl error");
        }
        return sb.toString();
    }

    public static String combineString(String... str){
        StringBuffer sb = new StringBuffer();
        for( int i=0; i<str.length; i++ ) {
            sb.append(str[i]);
        }
        return sb.toString();
    }

    public static List<String> getDeviceList(){
        List<String> deviceList = new ArrayList<String>();
        return deviceList;
    }
}
