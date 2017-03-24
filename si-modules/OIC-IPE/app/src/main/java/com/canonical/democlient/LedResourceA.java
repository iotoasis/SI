package com.canonical.democlient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.iotivity.base.OcException;
import org.iotivity.base.OcPlatform;
import org.iotivity.base.OcRepresentation;
import org.iotivity.base.OcResource;

import java.util.ArrayList;

/**
 * Created by gerald on 2015/11/10.
 */
public class LedResourceA extends DemoResource implements
        OcPlatform.OnResourceFoundListener,
        OcResource.OnGetListener,
        OcResource.OnPutListener,
        OcResource.OnPostListener,
        OcResource.OnObserveListener {

    private int mLed;
    private int mLedListIndex;
    private final static String LED_STATUS_KEY = "status";
    public final static String led_display = "LED : ";
    public final static String msg_content_set = "progress";

    public LedResourceA(Activity main, Context c, ArrayList<String> list_item,
                        ArrayAdapter<String> list_adapter) {

        super(main, c, list_item, list_adapter);

        resource_type = "grove.led";
        resource_uri = "/grove/led";

        mLed = 0;
        mLedListIndex = -1;

        msg_content_found = "msg_found_led_a";

        msg_type_put_done = "msg_put_done_led_a";

        msg_type_set = "msg_type_led_a_set";

        LocalBroadcastManager.getInstance(main_context).registerReceiver(setReceiver, new IntentFilter(msg_type_set));
    }

    public int getLed() { return mLed; }
    public void setLedIndex(int index) { mLedListIndex = index; }
    public int getLedIndex() { return mLedListIndex; }

    protected void update_list() {
        main_activity.runOnUiThread(new Runnable() {
            public synchronized void run() {
                main_list_item.set(mLedListIndex, led_display + String.valueOf(mLed));
                main_list_adapter.notifyDataSetChanged();

                Log.e(TAG, "Arduino LED status:");
                Log.e(TAG, String.valueOf(mLed));
            }
        });
    }

    protected void setFromIntent(Intent intent) {
        int progress = intent.getIntExtra(msg_content_set, 0);
        Log.e(TAG, "LED status: " + String.valueOf(progress));
        putRep(progress);
    }

    public void setOcRepresentation(OcRepresentation rep) throws OcException {
        mLed = rep.getValue(LED_STATUS_KEY);
    }

    public OcRepresentation getOcRepresentation() throws OcException {
        OcRepresentation rep = new OcRepresentation();
        rep.setValue(LED_STATUS_KEY, mLed);
        return rep;
    }

    public void putRep(int status) {
        mLed = status;
        putResourceRepresentation();
    }

    protected void sendPutCompleteMessage() {
        sendBroadcastMessage(msg_type_put_done, msg_content_put_done, true);
    }

    protected void post_reset() {
        LocalBroadcastManager.getInstance(main_context).unregisterReceiver(setReceiver);
    }
}
