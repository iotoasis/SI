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
public class LedResourceP extends DemoResource implements
        OcPlatform.OnResourceFoundListener,
        OcResource.OnGetListener,
        OcResource.OnPutListener,
        OcResource.OnPostListener,
        OcResource.OnObserveListener {

    private int mLedRed;
    private int mLedGreen;
    private int mLedBlue;
    private int mLedRedListIndex;
    private int mLedGreenListIndex;
    private int mLedBlueListIndex;
    private final static String LED_RED_KEY = "red";
    private final static String LED_GREEN_KEY = "green";
    private final static String LED_BLUE_KEY = "blue";

    public final static String led_red_display = "(RaspberryPi2) LED red: ";
    public final static String led_green_display = "(RaspberryPi2) LED green: ";
    public final static String led_blue_display = "(RaspberryPi2) LED blue: ";

    public final static String msg_content_set_red = "red";
    public final static String msg_content_set_green = "green";
    public final static String msg_content_set_blue = "blue";

    public LedResourceP(Activity main, Context c, ArrayList<String> list_item,
                        ArrayAdapter<String> list_adapter) {
        super(main, c, list_item, list_adapter);

        resource_type = "gateway.ledp";
        resource_uri = "/gateway/ledp";

        mLedRed = 0;
        mLedGreen = 0;
        mLedBlue = 0;
        mLedRedListIndex = -1;
        mLedGreenListIndex = -1;
        mLedBlueListIndex = -1;

        msg_content_found = "msg_found_led_p";

        msg_type_put_done = "msg_put_done_led_p";

        msg_type_set = "msg_type_led_p_set";

        LocalBroadcastManager.getInstance(main_context).registerReceiver(setReceiver,
                new IntentFilter(msg_type_set));
    }

    public int getmLedRed() { return mLedRed; };
    public int getmLedGreen() { return mLedGreen; };
    public int getmLedBlue() { return mLedBlue; };

    public void setLedRedIndex(int index) { mLedRedListIndex = index; }
    public void setLedGreenIndex(int index) { mLedGreenListIndex = index; }
    public void setLedBlueIndex(int index) { mLedBlueListIndex = index; }

    public int getLedRedIndex() { return mLedRedListIndex; }
    public int getLedGreenIndex() { return mLedGreenListIndex; }
    public int getLedBlueIndex() { return mLedBlueListIndex; }

    protected void update_list() {
        main_activity.runOnUiThread(new Runnable() {
            public synchronized void run() {
                main_list_item.set(mLedRedListIndex, led_red_display + String.valueOf(mLedRed));
                main_list_item.set(mLedGreenListIndex, led_green_display + String.valueOf(mLedGreen));
                main_list_item.set(mLedBlueListIndex, led_blue_display + String.valueOf(mLedBlue));
                main_list_adapter.notifyDataSetChanged();

                Log.e(TAG, "Raspberry Pi 2 LED status:");
                Log.e(TAG, String.valueOf(mLedRed));
                Log.e(TAG, String.valueOf(mLedGreen));
                Log.e(TAG, String.valueOf(mLedBlue));
            }
        });
    }

    protected void setFromIntent(Intent intent) {
        int r = intent.getIntExtra(msg_content_set_red, 0);
        int g = intent.getIntExtra(msg_content_set_green, 0);
        int b = intent.getIntExtra(msg_content_set_blue, 0);
        Log.e(TAG, "LED status: ");
        Log.e(TAG, "Red: " + String.valueOf(r));
        Log.e(TAG, "Green: " + String.valueOf(g));
        Log.e(TAG, "Blue: " + String.valueOf(b));
        putRep(r, g, b);
    }

    public void setOcRepresentation(OcRepresentation rep) throws OcException {
        mLedRed = rep.getValue(LED_RED_KEY);
        mLedGreen = rep.getValue(LED_GREEN_KEY);
        mLedBlue = rep.getValue(LED_BLUE_KEY);
    }

    public OcRepresentation getOcRepresentation() throws OcException {
        OcRepresentation rep = new OcRepresentation();
        rep.setValue(LED_RED_KEY, mLedRed);
        rep.setValue(LED_GREEN_KEY, mLedGreen);
        rep.setValue(LED_BLUE_KEY, mLedBlue);
        return rep;
    }

    public void putRep(int r, int g, int b) {
        mLedRed = r;
        mLedGreen = g;
        mLedBlue = b;

        putResourceRepresentation();
    }

    protected void sendPutCompleteMessage() {
        sendBroadcastMessage(msg_type_put_done, msg_content_put_done, true);
    }

    protected void post_reset() {
        LocalBroadcastManager.getInstance(main_context).unregisterReceiver(setReceiver);
    }
}
