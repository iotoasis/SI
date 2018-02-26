package com.canonical.democlient;

import android.util.Log;

/**
 * Created by 문선호 on 2016-10-06.
 */
public class CheckDiscoveryComplete extends Thread{
    private String TAG = CheckDiscoveryComplete.class.getSimpleName();
    public void run(){
        while( true ){
            try {
                Log.i(TAG,"                        SEARCHING....       ");
                if (Constants.deviceInfo != null) {
                    Log.i(TAG,"                        FOUND IT! SO BREAK OUT!!       ");
                    break;
                }
                sleep(1500);
            } catch(Exception e) {
                Log.i(TAG, e.toString() );
            }
        }
        Log.i(TAG,"                        CHECK DISCOVERY OUT....       ");
    }
}
