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
public class ButtonResourceP extends DemoResource implements
        OcPlatform.OnResourceFoundListener,
        OcResource.OnGetListener,
        OcResource.OnPutListener,
        OcResource.OnPostListener,
        OcResource.OnObserveListener {

    private int mButton;
    private int mButtonListIndex;
    private static final String BUTTON_KEY = "button";
    public final static String button_display = "(RaspberryPi2) Button: ";

    public ButtonResourceP(Activity main, Context c, ArrayList<String> list_item,
                           ArrayAdapter<String> list_adapter) {

        super(main, c, list_item, list_adapter);

        resource_type = "gateway.buttonp";
        resource_uri = "/gateway/buttonp";

        mButton = 0;
        mButtonListIndex = -1;

        msg_content_found = "msg_found_button_p";
    }

    public void setButtonIndex(int index) { mButtonListIndex = index; }
    public int getButtonIndex() { return mButtonListIndex; }

    protected void update_list() {
        main_activity.runOnUiThread(new Runnable() {
            public synchronized void run() {
                main_list_item.set(mButtonListIndex, button_display + String.valueOf(mButton));
                main_list_adapter.notifyDataSetChanged();

                Log.e(TAG, "RaspberryPi2 Button:");
                Log.e(TAG, String.valueOf(mButton));
            }
        });
    }

    public void setOcRepresentation(OcRepresentation rep) throws OcException {
        mButton = rep.getValue(BUTTON_KEY);
    }

    public OcRepresentation getOcRepresentation() throws OcException {
        OcRepresentation rep = new OcRepresentation();
        rep.setValue(BUTTON_KEY, mButton);
        return rep;
    }
}
