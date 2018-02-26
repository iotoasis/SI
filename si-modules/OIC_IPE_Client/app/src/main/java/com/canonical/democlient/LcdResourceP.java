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
public class LcdResourceP extends DemoResource implements
        OcPlatform.OnResourceFoundListener,
        OcResource.OnGetListener,
        OcResource.OnPutListener,
        OcResource.OnPostListener,
        OcResource.OnObserveListener {

    private String mLcd;
    private int mLcdListIndex;
    private static final String LCD_STRING_KEY = "lcd";
    public final static String lcd_display = "(RaspberryPi2) LCD: ";
    public final static String msg_content_set = "lcd_string";

    public LcdResourceP(Activity main, Context c, ArrayList<String> list_item,
                        ArrayAdapter<String> list_adapter) {

        super(main, c, list_item, list_adapter);

        resource_type = "gateway.lcdp";
        resource_uri = "/gateway/lcdp";

        mLcd = "";
        mLcdListIndex = -1;

        msg_content_found = "msg_found_lcd_p";

        msg_type_set = "msg_type_lcd_p_set";

        LocalBroadcastManager.getInstance(main_context).registerReceiver(setReceiver,
                new IntentFilter(msg_type_set));
    }

    public void setLcdIndex(int index) { mLcdListIndex = index; }
    public int getLcdIndex() { return mLcdListIndex; }

    protected void update_list() {
        main_activity.runOnUiThread(new Runnable() {
            public synchronized void run() {
                main_list_item.set(mLcdListIndex, lcd_display + mLcd);
                main_list_adapter.notifyDataSetChanged();
                Log.e(TAG, "RaspberryPi2 LCD:");
                Log.e(TAG, mLcd);
            }
        });
    }

    protected void setFromIntent(Intent intent) {
        String str = intent.getStringExtra(msg_content_set);
        Log.e(TAG, "LCD string: " + str);
        putRep(str);
    }

    public void setOcRepresentation(OcRepresentation rep) throws OcException {
        mLcd = rep.getValue(LCD_STRING_KEY);
    }

    public OcRepresentation getOcRepresentation() throws OcException {
        OcRepresentation rep = new OcRepresentation();
        rep.setValue(LCD_STRING_KEY, mLcd);
        return rep;
    }

    public void putRep(String str) {
        mLcd = str;
        putResourceRepresentation();
    }

    protected void post_reset() {
        LocalBroadcastManager.getInstance(main_context).unregisterReceiver(setReceiver);
    }
}
