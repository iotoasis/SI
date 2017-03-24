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
public class BuzzerResourceP extends DemoResource implements
        OcPlatform.OnResourceFoundListener,
        OcResource.OnGetListener,
        OcResource.OnPutListener,
        OcResource.OnPostListener,
        OcResource.OnObserveListener {

    private double mBuzzer;
    private int mBuzzerListIndex;
    private static final String BUZZER_KEY = "buzzer";
    public final static String buzzer_display = "(RaspberryPi2) Buzzer: ";

    public final static String msg_content_set = "second";

    public BuzzerResourceP(Activity main, Context c, ArrayList<String> list_item,
                           ArrayAdapter<String> list_adapter) {

        super(main, c, list_item, list_adapter);

        resource_type = "gateway.buzzerp";
        resource_uri = "/gateway/buzzerp";

        mBuzzer = 0.0;
        mBuzzerListIndex = -1;

        msg_content_found = "msg_found_buzzer_p";
        msg_type_put_done = "msg_put_done_buzzer_p";

        msg_type_set = "msg_type_buzzer_p_set";

        LocalBroadcastManager.getInstance(main_context).registerReceiver(setReceiver,
                new IntentFilter(msg_type_set));
    }

    public void setBuzzerIndex(int index) { mBuzzerListIndex = index; }
    public int getBuzzerIndex() { return mBuzzerListIndex; }

    protected void update_list() {
        main_activity.runOnUiThread(new Runnable() {
            public synchronized void run() {
                main_list_item.set(mBuzzerListIndex, buzzer_display);
                main_list_adapter.notifyDataSetChanged();
            }
        });
    }

    protected void setFromIntent(Intent intent) {
        double beep = intent.getDoubleExtra(msg_content_set, 0);
        Log.e(TAG, "Buzzer beep: " + String.valueOf(beep));
        putRep(beep);
    }

    public void setOcRepresentation(OcRepresentation rep) throws OcException {
        mBuzzer = rep.getValue(BUZZER_KEY);
    }

    public OcRepresentation getOcRepresentation() throws OcException {
        OcRepresentation rep = new OcRepresentation();
        rep.setValue(BUZZER_KEY, mBuzzer);
        return rep;
    }

    public void putRep(double buzzer) {
        mBuzzer = buzzer;
        putResourceRepresentation();
    }

    protected void sendPutCompleteMessage() {
        sendBroadcastMessage(msg_type_put_done, msg_content_put_done, true);
    }

    protected void post_reset() {
        LocalBroadcastManager.getInstance(main_context).unregisterReceiver(setReceiver);
    }
}
