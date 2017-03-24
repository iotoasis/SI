package com.canonical.democlient;

import android.app.Activity;
import android.content.Context;
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
public class ButtonResourceA extends  DemoResource implements
        OcPlatform.OnResourceFoundListener,
        OcResource.OnGetListener,
        OcResource.OnPutListener,
        OcResource.OnPostListener,
        OcResource.OnObserveListener {

    private int mButton;
    private int mTouch;
    private int mButtonListIndex;
    private int mTouchListIndex;
    private static final String BUTTON_KEY = "button";
    private static final String BUTTON_TOUCH_KEY = "touch";
    public final static String button_display = "Button : ";
    public final static String button_touch_display = "Touch : ";

    public ButtonResourceA(Activity main, Context c, ArrayList<String> list_item,
                           ArrayAdapter<String> list_adapter) {

        super(main, c, list_item, list_adapter);

        resource_type = "grove.button";
        resource_uri = "/grove/button";

        mButton = 0;
        mTouch = 0;
        mButtonListIndex = -1;
        mTouchListIndex = -1;

        msg_content_found = "msg_found_button_a";
    }

    public void setButtonIndex(int index) { mButtonListIndex = index; }
    public void setTouchIndex(int index) { mTouchListIndex = index; }
    public int getButtonIndex() { return mButtonListIndex; }
    public int getTouchIndex() { return mTouchListIndex; }

    public int getButtonValue() { return mButton; }
    public int getTouchValue() { return mTouch; }

    protected void update_list() {
        main_activity.runOnUiThread(new Runnable() {
            public synchronized void run() {
                main_list_item.set(mButtonListIndex, button_display + String.valueOf(mButton));
                main_list_item.set(mTouchListIndex, button_touch_display + String.valueOf(mTouch));
                main_list_adapter.notifyDataSetChanged();

                Log.e(TAG, "Arduino Button:");
                Log.e(TAG, String.valueOf(mButton));
                Log.e(TAG, String.valueOf(mTouch));
            }
        });
    }

    public void setOcRepresentation(OcRepresentation rep) throws OcException {
        mButton = rep.getValue(BUTTON_KEY);
        mTouch = rep.getValue(BUTTON_TOUCH_KEY);
    }

    public OcRepresentation getOcRepresentation() throws OcException {
        OcRepresentation rep = new OcRepresentation();
        rep.setValue(BUTTON_KEY, mButton);
        rep.setValue(BUTTON_TOUCH_KEY, mTouch);
        return rep;
    }
}
