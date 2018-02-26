package com.canonical.democlient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.iotivity.base.ModeType;
import org.iotivity.base.OcPlatform;
import org.iotivity.base.PlatformConfig;
import org.iotivity.base.QualityOfService;
import org.iotivity.base.ServiceType;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

/**
 * Created by seung-wanmun on 2016. 10. 7..
 */
public class DiscoveryResultActivity extends PageHandler {
    private final static String TAG = DiscoveryResultActivity.class.getSimpleName();

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
    private Thread thread_check;

    /* OIC Module */
    private HttpOneM2MOperation op = new HttpOneM2MOperation();
    private Context context;
    private FoundItem foundItem;
    private JSONObject discovery;

    private boolean isSearching = false;
    private boolean isReadyForStart = false;

    private void initData(){
        foundItem = new FoundItem();
        foundItem.setHost(Constants.HOST_IP);
        foundItem.setDeviceId(Constants.BASE_AE);
        foundItem.setDeviceName(foundItem.getDeviceId());
    }

    protected String getValue( String xml, String path ) {
        Node node = null;
        String make_xml = Util.combineString(xml.substring(0,xml.indexOf(">")+1),"<root>",xml.substring(xml.indexOf(">")+1),"</root>");
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(make_xml.getBytes()));
            doc.getDocumentElement().normalize();
            XPath xpath = XPathFactory.newInstance().newXPath();
            node = (Node) xpath.evaluate("//pc/sgn/nev/rep/cin/"+path, doc, XPathConstants.NODE);
        } catch( Exception e ) {
            Log.e("GET VALUE >> ", e.toString());
        }
        return node.getTextContent();
    }

    private Thread ipe;
    private Thread cmd;

    private Button regResourceBtn = null;

    private void sendMessage( int value, String msg_type_set, String msg_content_set ) {
        Intent intent = new Intent(msg_type_set);
        // You can also include some extra data.
        intent.putExtra(msg_content_set, value);
        LocalBroadcastManager.getInstance(DiscoveryResultActivity.this).sendBroadcast(intent);
    }

    private synchronized boolean isRepositoryExist(){
        boolean result = false;

        if(true){
            result = true;
        }
        return result;
    }

    private synchronized void createRepository(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                if( createRootAE() ){
                    Log.i(TAG,"createRootAE");
                    if( createPollingChannelUnderAE() && createContainerUnderAE() ){
                        Log.i(TAG,"createPollingChannelUnderAE/createContainerUnderAE");
                        if( createContainerUnderContainer() ){
                            Log.i(TAG,"createContainerUnderContainer");
                            if( createSubscription() ){
                                Log.i(TAG,"createSubscription");
                                Log.i(TAG,"Repository has created.");
                                Toast.makeText(context, "Repository has created.", Toast.LENGTH_SHORT);
                            } else {
                                Log.i(TAG,"FAIL  :: createSubscription");
                            }
                        } else {
                            Log.i(TAG,"FAIL  :: createContainerUnderContainer");
                        }
                    } else {
                        Log.i(TAG,"FAIL  :: createPollingChannelUnderAE/createContainerUnderAE");
                    }
                } else {
                    Log.i(TAG,"FAIL  :: createRootAE");
                }

                regResourceBtn.setText("Register Resources");
                regResourceBtn.setEnabled(true);
                if( !isSearching ){

                    start();
                    search();
                }
            }
        }).start();
    }

    private boolean hasBeenMadeSuccessfully( JSONObject jobj ){
        boolean result = false;
        if( jobj != null && jobj.isNull("RESPONSE-CODE") ){
            // nothing
        }
        return result;
    }

    private synchronized boolean createRootAE(){
        op.init(Constants.INCSE_ADDR, foundItem);
        setLblList("ae");
        foundItem.setResourceName(Constants.BASE_AE);
        foundItem.setResourceUri(Constants.INCSE_ADDR);
        JSONObject ae = op.createAE(getLblList(), foundItem);
        return hasBeenMadeSuccessfully( ae );
    }

    private synchronized boolean createContainerUnderAE(){
        boolean result = true;
        ArrayList<Boolean> result_list = new ArrayList<Boolean>();
        String url = Util.makeUrl(Constants.INCSE_ADDR, Constants.BASE_AE);
        op.init(url ,foundItem);
        for( String str : Constants.COMMAND_CONTAINER_URL ){
            setLblList("cnt_h","cmd_h", str );
            foundItem.setResourceName(str);
            foundItem.setResourceUri(url);
            result_list.add( hasBeenMadeSuccessfully(op.createContainer(getLblList(), foundItem)) );
        }
        for( String str : Constants.OBSERVE_CONTAINER_URL ){
            setLblList("cnt_h","obs_h", str );
            foundItem.setResourceName(str);
            foundItem.setResourceUri(url);
            result_list.add( hasBeenMadeSuccessfully(op.createContainer(getLblList(), foundItem)) );
        }
        for( boolean res : result_list ){
            result &= res;
        }
        return result;
    }

    private synchronized boolean createPollingChannelUnderAE(){
        boolean result = true;
        ArrayList<Boolean> result_list = new ArrayList<Boolean>();
        String url = Util.makeUrl(Constants.INCSE_ADDR, Constants.BASE_AE);
        op.init(url ,foundItem);
        for( String str : Constants.POLLING_CHANNEL_URL ){
            setLblList( str );
            foundItem.setResourceName(str);
            foundItem.setResourceUri(url);
            result_list.add( hasBeenMadeSuccessfully(op.createPollingChannel(getLblList(), foundItem)) );
        }
        for( boolean res : result_list ){
            result &= res;
        }
        return result;
    }

    private synchronized boolean createContainerUnderContainer(){
        boolean result = true;
        ArrayList<Boolean> result_list = new ArrayList<Boolean>();
        for( String str_b : Constants.BASE_CONTAINER_URL ) {
            for (String str_c : Constants.COMMAND_CONTAINER_URL) {
                String url = Util.makeUrl(Constants.INCSE_ADDR, Constants.BASE_AE, str_c);
                op.init(url, foundItem);
                setLblList("cnt_l", "cmd_l", str_c);
                foundItem.setResourceName(str_b);
                foundItem.setResourceUri(url);
                result_list.add(hasBeenMadeSuccessfully(op.createContainer(getLblList(), foundItem)));
            }
            for (String str_c : Constants.OBSERVE_CONTAINER_URL) {
                String url = Util.makeUrl(Constants.INCSE_ADDR, Constants.BASE_AE, str_c);
                op.init(url, foundItem);
                setLblList("cnt_l", "obs_l", str_c);
                foundItem.setResourceName(str_b);
                foundItem.setResourceUri(url);
                result_list.add(hasBeenMadeSuccessfully(op.createContainer(getLblList(), foundItem)));
            }
        }
        for( boolean res : result_list ){
            result &= res;
        }
        return result;
    }

    private synchronized boolean createSubscription(){
        boolean result = true;
        ArrayList<Boolean> result_list = new ArrayList<Boolean>();
        for( String str_b : Constants.BASE_CONTAINER_URL ) {
            for (String str_c : Constants.COMMAND_CONTAINER_URL) {
                String url = Util.makeUrl(Constants.INCSE_ADDR, Constants.BASE_AE, str_c, str_b);
                op.init(url, foundItem);
                String rn = Util.combineString("sub_",str_b.substring(0,1));
                setLblList(rn, str_c);
                foundItem.setResourceName(rn);
                foundItem.setResourceUri(url);
                foundItem.setPcuUri(Util.makeUrl(Constants.INCSE_ADDR,Constants.BASE_AE,Constants.POLLING_CHANNEL_URL[1],"pcu"));
                result_list.add(hasBeenMadeSuccessfully(op.createSubscription(getLblList(), foundItem)));
            }
            for (String str_c : Constants.OBSERVE_CONTAINER_URL) {
                String url = Util.makeUrl(Constants.INCSE_ADDR, Constants.BASE_AE, str_c, str_b);
                op.init(url, foundItem);
                String rn = Util.combineString("sub_",str_b.substring(0,1));
                setLblList(rn, str_c);
                foundItem.setResourceName(rn);
                foundItem.setResourceUri(url);
                foundItem.setPcuUri(Util.makeUrl(Constants.INCSE_ADDR,Constants.BASE_AE,Constants.POLLING_CHANNEL_URL[0],"pcu"));
                result_list.add(hasBeenMadeSuccessfully(op.createSubscription(getLblList(), foundItem)));
            }
        }
        for( boolean res : result_list ){
            result &= res;
        }
        return result;
    }

    /* Configure and then find all the resource by resource classes */
    private void startDemoClient() {
        Context context = this;

        initData();
        Log.e(TAG, Util.makeUrl(Constants.INCSE_ADDR, Constants.BASE_AE));
        op.init(Util.makeUrl(Constants.INCSE_ADDR, Constants.BASE_AE), foundItem);
        discovery = op.retrieveAE(foundItem);

        if (discovery != null && !discovery.isNull("RESPONSE-CODE")) {
            isReadyForStart = true;
            start();
            search();
        } else {
            Toast.makeText(context,"There is no repository. Please press the button 'Register Resources'.",Toast.LENGTH_SHORT);
        }
    }

    private void start() {
        while(true){
            try{
                CommandReceiver cr = new CommandReceiver(sensor_a);
                result = cr.receive();
            } catch(Exception e) {
                e.printStackTrace();
                try {
                    System.out.println("Failed to connect to server. It will try again in 5 seconds.");
                    Thread.sleep(5000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    private void search(){
        isSearching = true;
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
        sensor_a = new SensorResourceA(DiscoveryResultActivity.this, this, list_item, list_adapter);
        sensor_a.find_resource();

        led_a = new LedResourceA(DiscoveryResultActivity.this, this, list_item, list_adapter);
        led_a.find_resource();

        lcd_a = new LcdResourceA(DiscoveryResultActivity.this, this, list_item, list_adapter);
        lcd_a.find_resource();

        buzzer_a = new BuzzerResourceA(DiscoveryResultActivity.this, this, list_item, list_adapter);
        buzzer_a.find_resource();

        button_a = new ButtonResourceA(DiscoveryResultActivity.this, this, list_item, list_adapter);
        button_a.find_resource();

        /* Raspberry Pi 2 resources */
        sensor_p = new SensorResourceP(DiscoveryResultActivity.this, this, list_item, list_adapter);
        sensor_p.find_resource();

        led_p = new LedResourceP(DiscoveryResultActivity.this, this, list_item, list_adapter);
        led_p.find_resource();

        lcd_p = new LcdResourceP(DiscoveryResultActivity.this, this, list_item, list_adapter);
        lcd_p.find_resource();

        buzzer_p = new BuzzerResourceP(DiscoveryResultActivity.this, this, list_item, list_adapter);
        buzzer_p.find_resource();

        button_p = new ButtonResourceP(DiscoveryResultActivity.this, this, list_item, list_adapter);
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

    private void msg(final String text) {
        runOnUiThread(new Runnable() {
            public void run() {
                mConsoleTextView.append("\n");
                mConsoleTextView.append(text);
                mScrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
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
        setContentView(R.layout.activity_sub);

        context = this;
        mConsoleTextView = (TextView) findViewById(R.id.consoleTextView);
        mConsoleTextView.setMovementMethod(new ScrollingMovementMethod());
        mScrollView = (ScrollView) findViewById(R.id.scrollView);
        mScrollView.fullScroll(View.FOCUS_DOWN);


        ListView listview = (ListView) findViewById(R.id.listView);

        LocalBroadcastManager.getInstance(this).registerReceiver(mFoundResourceReceiver,
                new IntentFilter(DemoResource.msg_type_found));

        list_item = new ArrayList<String>();
        list_item.add("");

        list_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                list_item);
        listview.setAdapter(list_adapter);

        if (null == savedInstanceState) {
            ipe = new Thread(new Runnable() {
                public void run() {
                    startDemoClient();
                }
            });
            ipe.start();
        } else {
            String consoleOutput = savedInstanceState.getString("consoleOutputString");
            mConsoleTextView.setText(consoleOutput);
        }

        regResourceBtn = (Button)findViewById(R.id.resource_reg_btn);

        regResourceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regResourceBtn.setText("Registering....");
                regResourceBtn.setEnabled(false);

                if(isRepositoryExist()){
                    regResourceBtn.setText("Register Resources");
                    regResourceBtn.setEnabled(true);
                    Toast.makeText(context, "Repository already has exists.", Toast.LENGTH_SHORT);
                } else {
                    createRepository();
                }
            }
        });

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
