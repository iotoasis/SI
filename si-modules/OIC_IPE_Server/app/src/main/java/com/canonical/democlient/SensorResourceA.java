package com.canonical.democlient;

import android.app.Activity;
import android.content.BroadcastReceiver;
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
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by gerald on 2015/11/10.
 */
public class SensorResourceA extends DemoResource implements
        OcPlatform.OnResourceFoundListener,
        OcResource.OnGetListener,
        OcResource.OnPutListener,
        OcResource.OnPostListener,
        OcResource.OnObserveListener {

    private double mTemp;
    private int mLight;
    private int mSound;
    private int mTempListIndex;
    private int mLightListIndex;
    private int mSoundListIndex;
    private static final String SENSOR_TEMPERATURE_KEY = "temperature";
    private static final String SENSOR_LIGHT_KEY = "light";
    private static final String SENSOR_SOUND_KEY = "sound";
    private final static String sensor_temp_display = "Temperature sensor : ";
    private final static String sensor_light_display = "Light sensor : ";
    private final static String sensor_sound_display = "Sound sensor : ";

    private boolean update_thread_done;

    public SensorResourceA(Activity main, Context c, ArrayList<String> list_item,
                           ArrayAdapter<String> list_adapter) {

        super(main, c, list_item, list_adapter);

        resource_type = "grove.sensor";
        resource_uri = "/grove/sensor";

        mTemp = 0.0;
        mLight = 0;
        mSound = 0;

        mTempListIndex = -1;
        mLightListIndex = -1;
        mSoundListIndex = -1;

        msg_content_found = "msg_found_sensor_a";

        msg_type_get_done = "msg_get_done_sensor_a";
        update_thread_done = true;

        LocalBroadcastManager.getInstance(main_activity).registerReceiver(mSensorReadReceiver,
                new IntentFilter(msg_type_get_done));
    }

    protected void update_thread() {
        Log.e(TAG, "Start update thread");
        while(update_thread_running) {
            Log.e(TAG, "Start update sensors: " + String.valueOf(update_thread_done));
            if(update_thread_done) {
                update_thread_done = false;
                getResourceRepresentation();
                try {
                    update_thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.e(TAG, e.toString());
                }
            }

            try {
                update_thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.e(TAG, e.toString());
            }
        }
    }

    public void setTempIndex(int index) { mTempListIndex = index; }
    public void setLightIndex(int index) { mLightListIndex = index; }
    public void setSoundIndex(int index) { mSoundListIndex = index; }

    public int getTempIndex() { return mTempListIndex; }
    public int getLightIndex() { return mLightListIndex; }
    public int getSoundIndex() { return mSoundListIndex; }

    public double getTempValue() { return mTemp; }
    public double getLightValue() { return mLight; }
    public double getSoundValue() { return mSound; }

    /* OIC Module */
    private HttpOneM2MOperation op = new HttpOneM2MOperation();
    private Context context;
    private FoundItem foundItem;
    private JSONObject discovery;

    private void initData(){
        foundItem = new FoundItem();
        foundItem.setHost(Constants.HOST_IP);
        foundItem.setDeviceId(Constants.BASE_AE);
        foundItem.setDeviceName(foundItem.getDeviceId());
    }

    protected void update_list() {
        /*
        new Thread(new Runnable() {
            @Override
            public void run() {
                initData();
                foundItem.setResourceName("temperature");
                foundItem.setResourceUri("temperature");
                Log.e(TAG, Util.makeUrl(Constants.INCSE_ADDR, Constants.BASE_AE, "temperature", "result"));
                op.init(Util.makeUrl(Constants.INCSE_ADDR, Constants.BASE_AE, "temperature", "result"), foundItem);
                ArrayList<String> lbl = new ArrayList<String>();
                JSONObject cin_result = op.createContentInstance(foundItem, lbl, String.format("%.2f", mTemp));
                Log.e(TAG, cin_result.toString());

                foundItem.setResourceName("light");
                foundItem.setResourceUri("light");
                op.init(Util.makeUrl(Constants.INCSE_ADDR, Constants.BASE_AE, "light", "result"), foundItem);
                JSONObject cin_result2 = op.createContentInstance(foundItem, lbl, String.valueOf(mLight));
                Log.e(TAG, cin_result2.toString());

                foundItem.setResourceName("sound");
                foundItem.setResourceUri("sound");
                op.init(Util.makeUrl(Constants.INCSE_ADDR, Constants.BASE_AE, "sound", "result"), foundItem);
                JSONObject cin_result3 = op.createContentInstance(foundItem, lbl, String.valueOf(mSound));
                Log.e(TAG, cin_result3.toString());
            }
        }).start();
        //*/

        main_activity.runOnUiThread(new Runnable() {
            public synchronized void run() {
                main_list_item.set(mTempListIndex, sensor_temp_display + String.valueOf( Double.parseDouble(String.format("%.2f", mTemp)) ));
                main_list_item.set(mLightListIndex, sensor_light_display + String.valueOf(mLight));
                main_list_item.set(mSoundListIndex, sensor_sound_display + String.valueOf(mSound));

                main_list_adapter.notifyDataSetChanged();

                Log.e(TAG, "Arduino Sensors:");
                Log.e(TAG, String.valueOf(mTemp));
                Log.e(TAG, String.valueOf(mLight));
                Log.e(TAG, String.valueOf(mSound));
            }
        });
    }

    public void setOcRepresentation(OcRepresentation rep) throws OcException {
        mTemp = rep.getValue(SENSOR_TEMPERATURE_KEY);
        mLight = rep.getValue(SENSOR_LIGHT_KEY);
        mSound = rep.getValue(SENSOR_SOUND_KEY);
    }

    public OcRepresentation getOcRepresentation() throws OcException {
        OcRepresentation rep = new OcRepresentation();
        rep.setValue(SENSOR_TEMPERATURE_KEY, mTemp);
        rep.setValue(SENSOR_LIGHT_KEY, mLight);
        rep.setValue(SENSOR_SOUND_KEY, mSound);
        return rep;
    }

    protected void sendGetCompleteMessage() {
        sendBroadcastMessage(msg_type_get_done, msg_content_get_done, true);
    }

    private BroadcastReceiver mSensorReadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            update_thread_done = intent.getBooleanExtra(msg_content_get_done, false);
        }
    };
}
