package com.canonical.democlient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import org.iotivity.base.ModeType;
import org.iotivity.base.OcPlatform;
import org.iotivity.base.PlatformConfig;
import org.iotivity.base.QualityOfService;
import org.iotivity.base.ServiceType;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity{
    private final static String TAG = MainActivity.class.getSimpleName();

    private HttpOneM2MOperation op = new HttpOneM2MOperation();

    /* All resource classes */
    private SensorResourceA sensor_a = null;
    private LedResourceA led_a = null;
    private LcdResourceA lcd_a = null;
    private BuzzerResourceA buzzer_a = null;
    private ButtonResourceA button_a = null;

    private SensorResourceP sensor_p = null;
    private LedResourceP led_p = null;
    private LcdResourceP lcd_p = null;
    private BuzzerResourceP buzzer_p = null;
    private ButtonResourceP button_p = null;

    /* Resource items in ListView */
    private ArrayList<String> list_item;
    private ArrayAdapter<String> list_adapter;
    private int found_devices = 0;

    /* Debug output view */
    private TextView mConsoleTextView;
    private ScrollView mScrollView;

    /* OIC Module */
    private FoundDeviceInfo foundDeviceInfo;
    private Context context;
    private FoundItem foundItem;
    private JSONArray discovery;

    private void initData(){
        foundItem = new FoundItem();
        foundItem.setHost(Constants.HOST_IP);
        foundItem.setDeviceId("oic_ipe");
        foundItem.setDeviceName(foundItem.getDeviceId());
    }

    /* search 'AE' on CSE */
    private void searchDevice(){
        try {
            initData();
            op.init(Util.combineString(Constants.INCSE_ADDR, "?fu=1&lbl=", Constants.BASE_CONTAINER_LBL), foundItem);
            discovery = op.discoveryOfAllResource().getJSONObject("uril").getJSONArray("URIList");
            foundDeviceInfo = new FoundDeviceInfo();
            for(int i=0; i<discovery.length(); i++){
                String url = discovery.get(i).toString();
                int end_index = url.lastIndexOf("/");
                int start_index = (url.substring(0, end_index)).lastIndexOf("/");
                String ae = url.substring(start_index, end_index);
                String uri = url.substring(end_index);
                String resId = uri.substring(1);

                foundItem = new FoundItem();
                foundItem.setHost(Util.combineString(Constants.INCSE_ADDR,ae));
                foundItem.setDeviceId(resId);
                foundItem.setDeviceName(uri);

                List<String> resourceType = new ArrayList<String>();
                resourceType.add(resId);
                foundDeviceInfo.addResource(Constants.INCSE_ADDR, uri, resourceType);
                foundDeviceInfo.addFoundItem(foundItem);
                Log.e(TAG, " result : " + uri);
            }

            final Button button = (Button)findViewById(R.id.main_btn_1);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    button.setText("Device Discovery");
                    button.setEnabled(true);
                }
            });

            Intent intent = new Intent(getApplicationContext(), DiscoveryListActivity.class);
            intent.putExtra("foundDeviceInfo", foundDeviceInfo);
            startActivity(intent);

        } catch( JSONException je) {
            Log.e(TAG, je.toString());
            Log.e(TAG, discovery.toString());
        }
    }

    /* Configure and then find all the resource by resource classes */
    private void startDemoClient() {
        Context context = this;

        PlatformConfig platformConfig = new PlatformConfig(
                context,
                ServiceType.IN_PROC,
                ModeType.CLIENT,
                "0.0.0.0", // By setting to "0.0.0.0", it binds to all available interfaces
                0,         // Uses randomly available port
                QualityOfService.LOW
        );

        msg("Configuring platform.");
        OcPlatform.Configure(platformConfig);

        msg("Finding all resources");
        /* Arduino resources */
        sensor_a = new SensorResourceA(MainActivity.this, this, list_item, list_adapter);
        sensor_a.find_resource();

        led_a = new LedResourceA(MainActivity.this, this, list_item, list_adapter);
        led_a.find_resource();

        lcd_a = new LcdResourceA(MainActivity.this, this, list_item, list_adapter);
        lcd_a.find_resource();

        buzzer_a = new BuzzerResourceA(MainActivity.this, this, list_item, list_adapter);
        buzzer_a.find_resource();

        button_a = new ButtonResourceA(MainActivity.this, this, list_item, list_adapter);
        button_a.find_resource();

        /* Raspberry Pi 2 resources */
        sensor_p = new SensorResourceP(MainActivity.this, this, list_item, list_adapter);
        sensor_p.find_resource();

        led_p = new LedResourceP(MainActivity.this, this, list_item, list_adapter);
        led_p.find_resource();

        lcd_p = new LcdResourceP(MainActivity.this, this, list_item, list_adapter);
        lcd_p.find_resource();

        buzzer_p = new BuzzerResourceP(MainActivity.this, this, list_item, list_adapter);
        buzzer_p.find_resource();

        button_p = new ButtonResourceP(MainActivity.this, this, list_item, list_adapter);
        button_p.find_resource();

    }

    /* when a resource is found, add a empty item to ListView first
     * then the GET thread will update it's content
     */
    private void add_device() {
        if(found_devices != 0) {
            list_item.add("");
        }
    }

    /* Each resource has various sensors or devices, add them to the ListView */
    private void create_list(final String device) {
        runOnUiThread(new Runnable() {
            public synchronized void run() {
                if(device.equals(sensor_a.msg_content_found)){
                    add_device();
                    sensor_a.setTempIndex(found_devices++);
                    add_device();
                    sensor_a.setLightIndex(found_devices++);
                    add_device();
                    sensor_a.setSoundIndex(found_devices++);
                    list_adapter.notifyDataSetChanged();
                } else if(device.equals(led_a.msg_content_found)) {
                    add_device();
                    led_a.setLedIndex(found_devices++);
                    list_item.set(led_a.getLedIndex(), led_a.led_display);
                    list_adapter.notifyDataSetChanged();
                } else if(device.equals(lcd_a.msg_content_found)) {
                    add_device();
                    lcd_a.setLcdIndex(found_devices++);
                    list_item.set(lcd_a.getLcdIndex(), lcd_a.lcd_display);
                    list_adapter.notifyDataSetChanged();
                } else if(device.equals(buzzer_a.msg_content_found)) {
                    add_device();
                    buzzer_a.setBuzzerIndex(found_devices++);
                    list_item.set(buzzer_a.getBuzzerIndex(), buzzer_a.buzzer_display);
                    list_adapter.notifyDataSetChanged();
                } else if(device.equals(button_a.msg_content_found)) {
                    add_device();
                    button_a.setButtonIndex(found_devices++);
                    list_item.set(button_a.getButtonIndex(), button_a.button_display);
                    add_device();
                    button_a.setTouchIndex(found_devices++);
                    list_item.set(button_a.getTouchIndex(), button_a.button_touch_display);
                    list_adapter.notifyDataSetChanged();
                } else if(device.equals(sensor_p.msg_content_found)) {
                    add_device();
                    sensor_p.setTempIndex(found_devices++);
                    add_device();
                    sensor_p.setHumidityIndex(found_devices++);
                    add_device();
                    sensor_p.setLightIndex(found_devices++);
                    add_device();
                    sensor_p.setSoundIndex(found_devices++);
                    list_adapter.notifyDataSetChanged();
                } else if(device.equals(led_p.msg_content_found)) {
                    add_device();
                    led_p.setLedRedIndex(found_devices++);
                    list_item.set(led_p.getLedRedIndex(), led_p.led_red_display);
                    add_device();
                    led_p.setLedGreenIndex(found_devices++);
                    list_item.set(led_p.getLedGreenIndex(), led_p.led_green_display);
                    add_device();
                    led_p.setLedBlueIndex(found_devices++);
                    list_item.set(led_p.getLedBlueIndex(), led_p.led_blue_display);
                    list_adapter.notifyDataSetChanged();
                } else if(device.equals(lcd_p.msg_content_found)) {
                    add_device();
                    lcd_p.setLcdIndex(found_devices++);
                    list_item.set(lcd_p.getLcdIndex(), lcd_p.lcd_display);
                    list_adapter.notifyDataSetChanged();
                } else if(device.equals(buzzer_p.msg_content_found)) {
                    add_device();
                    buzzer_p.setBuzzerIndex(found_devices++);
                    list_item.set(buzzer_p.getBuzzerIndex(), buzzer_p.buzzer_display);
                    list_adapter.notifyDataSetChanged();
                } else if(device.equals(button_p.msg_content_found)) {
                    add_device();
                    button_p.setButtonIndex(found_devices++);
                    list_item.set(button_p.getButtonIndex(), button_p.button_display);
                    list_adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.e(TAG, e.toString());
        }
    }

    private void msg(final String text) {
        Log.i(TAG, text);
    }

    private void printLine() {
        msg("------------------------------------------------------------------------");
    }

    /* Call reset in onDestroy */
    private synchronized void resetGlobals() {
        if(sensor_a != null) { sensor_a.reset(); }
        if(sensor_a != null) { led_a.reset(); }
        if(sensor_a != null) { lcd_a.reset(); }
        if(sensor_a != null) { buzzer_a.reset(); }
        if(sensor_a != null) { button_a.reset(); }

        if(sensor_p != null) { sensor_p.reset(); }
        if(sensor_p != null) { led_p.reset(); }
        if(sensor_p != null) {  lcd_p.reset(); }
        if(sensor_p != null) { buzzer_p.reset(); }
        if(sensor_p != null) { button_p.reset(); }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        final Button button = (Button)findViewById(R.id.main_btn_1);

        if (null == savedInstanceState) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    button.setText("Discovering...");
                    button.setEnabled(false);
                    new Thread(new Runnable() {
                        public void run() {
                            searchDevice();
                        }
                    }).start();
                }
            });
        } else {
            String consoleOutput = savedInstanceState.getString("consoleOutputString");
            //mConsoleTextView.setText(consoleOutput);
        }

    }

    private void show_ip(final String ipaddr) {
        runOnUiThread(new Runnable() {
            public synchronized void run() {
                TextView tv = (TextView)findViewById(R.id.textView2);
                String str = tv.getText().toString();
                str += "\n";
                String ip = ipaddr.replace("coap://", "");
                str += ip;
                tv.setText(str);
            }
        });
    }

    private BroadcastReceiver mFoundResourceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean found_resource;

            found_resource = intent.getBooleanExtra(sensor_a.msg_content_found, false);
            if(found_resource) {
                msg("Message: found Arduino sensor resource");
                create_list(sensor_a.msg_content_found);
                //sensor_a.getResourceRepresentation();
                sensor_a.start_update_thread();
                return;
            }

            found_resource = intent.getBooleanExtra(led_a.msg_content_found, false);
            if(found_resource) {
                msg("Message: found Arduino LED resource");
                create_list(led_a.msg_content_found);
                led_a.getResourceRepresentation();
                return;
            }

            found_resource = intent.getBooleanExtra(lcd_a.msg_content_found, false);
            if(found_resource) {
                msg("Message: found Arduino LCD resource");
                create_list(lcd_a.msg_content_found);
                lcd_a.getResourceRepresentation();
                return;
            }

            found_resource = intent.getBooleanExtra(buzzer_a.msg_content_found, false);
            if(found_resource) {
                msg("Message: found Arduino buzzer resource");
                create_list(buzzer_a.msg_content_found);
                buzzer_a.getResourceRepresentation();
                return;
            }

            found_resource = intent.getBooleanExtra(button_a.msg_content_found, false);
            if(found_resource) {
                msg("Message: found Arduino button resource");
                create_list(button_a.msg_content_found);
                //button_a.getResourceRepresentation();
                button_a.observeFoundResource();
                return;
            }


            found_resource = intent.getBooleanExtra(sensor_p.msg_content_found, false);
            if(found_resource) {
                msg("Message: found RaspberryPi2 sensor resource");
                create_list(sensor_p.msg_content_found);
                //sensor_p.getResourceRepresentation();
                sensor_p.start_update_thread();
                return;
            }

            found_resource = intent.getBooleanExtra(led_p.msg_content_found, false);
            if(found_resource) {
                msg("Message: found RaspberryPi2 LED resource");
                create_list(led_p.msg_content_found);
                led_p.getResourceRepresentation();
                return;
            }

            found_resource = intent.getBooleanExtra(lcd_p.msg_content_found, false);
            if(found_resource) {
                msg("Message: found RaspberryPi2 LCD resource");
                create_list(lcd_p.msg_content_found);
                lcd_p.getResourceRepresentation();
                show_ip(lcd_p.server_addr);
                return;
            }

            found_resource = intent.getBooleanExtra(buzzer_p.msg_content_found, false);
            if(found_resource) {
                msg("Message: found RaspberryPi2 buzzer resource");
                create_list(buzzer_p.msg_content_found);
                buzzer_p.getResourceRepresentation();
                return;
            }

            found_resource = intent.getBooleanExtra(button_p.msg_content_found, false);
            if(found_resource) {
                msg("Message: found RaspberryPi2 button resource");
                create_list(button_p.msg_content_found);
                //button_p.getResourceRepresentation();
                button_p.observeFoundResource();
                return;
            }
        }
    };

    @Override
    public void onDestroy() {
        Log.e(TAG, "on Destroy");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mFoundResourceReceiver);

        resetGlobals();

        super.onDestroy();
    }
}
