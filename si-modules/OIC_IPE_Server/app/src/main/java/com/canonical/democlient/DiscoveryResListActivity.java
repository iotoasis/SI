package com.canonical.democlient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import java.util.ArrayList;

/**
 * Created by seung-wanmun on 2016. 8. 18..
 */
public class DiscoveryResListActivity extends AppCompatActivity {

    private final static String INCSE_ADDR = "http://10.10.224.240:8080/nimbus-cse";
    private final static String TAG = DiscoveryResListActivity.class.getSimpleName();

    private FoundDeviceInfo foundDeviceInfo;
    private ArrayList<FoundDeviceInfo.ResourceInfo> image_details;
    private CustomResListAdapter customListAdapter;
    private String hostUri;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.discovery_res_result);

        final Button button = (Button)findViewById(R.id.resource_reg_btn);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setText("Registering....");
                button.setEnabled(false);
                new Thread(new Runnable() {
                    public void run() {
                        AlertDialog.Builder alert = new AlertDialog.Builder(DiscoveryResListActivity.this);
                        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();     //닫기
                            }
                        });
                        alert.setMessage("Registering Resource......");
                        alert.show();


                    }
                }).start();
            }
        });


        foundDeviceInfo = (FoundDeviceInfo)getApplicationContext();

        setCustomActionbar();

        Intent intent = getIntent();
        hostUri = intent.getExtras().getString("host");
        ArrayList<FoundDeviceInfo.ResourceInfo> resourceList = foundDeviceInfo.getResource(hostUri);

        image_details = new ArrayList<FoundDeviceInfo.ResourceInfo>();

        for(FoundDeviceInfo.ResourceInfo resourceInfo : resourceList) {
            image_details.add(resourceInfo);
        }

        final ListView lv1 = (ListView) findViewById(R.id.discovery_res_list);
        customListAdapter = new CustomResListAdapter(this, image_details);

        lv1.setAdapter(customListAdapter);

    }

    private void setCustomActionbar() {
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        View mCustomView = LayoutInflater.from(this).inflate(R.layout.discovery_res_ab, null);
        actionBar.setCustomView(mCustomView);

        ImageButton imgButton = (ImageButton)findViewById(R.id.device_reg_btn);
        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "|||||||||||||||||||||| Device Info rgistered |||||||||||||||||");

                /*
                new Thread() {
                    public void run() {
                        HttpOneM2MOperation op = new HttpOneM2MOperation();
                        ArrayList<String> labels = null;
                        String host;

                        FoundPlatform foundPlatform = new FoundPlatform();
                        FoundItem foundDevice = new FoundItem();
                        OcResource foundResource = null;
                        OcRepresentation foundCtrlResource = null;
                        OcResource foundDefaultResource = null;

                        for(FoundPlatform item : foundDeviceInfo.getPlatformList()) {
                            if(item.getHost().equalsIgnoreCase(hostUri)) {
                                foundPlatform = item;
                            }
                        }
                        for(FoundItem item : foundDeviceInfo.getDeviceList()) {
                            if(item.getHost().equalsIgnoreCase(hostUri)) {
                                foundDevice = item;
                            }
                        }
                        for(OcResource item : foundDeviceInfo.getDefaultResourceList()) {
                            if(item.getHost().equalsIgnoreCase(hostUri)) {
                                foundDefaultResource = item;
                            }
                        }
                        for(OcRepresentation item : foundDeviceInfo.getCtrlResourceList()) {
                            if(item.getHost().equalsIgnoreCase(hostUri)) {
                                foundCtrlResource = item;
                            }
                        }

                        for(FoundDeviceInfo.ResourceInfo item :  foundDeviceInfo.getResource(hostUri)) {

                            labels = new ArrayList<String>();

                            Log.i(TAG, "==========================>trace-1");
                            op.init(INCSE_ADDR, foundDevice);

                            switch(item.resourceUri) {
                                case "/oic/p":
                                    String systemTime = foundPlatform.getSystemTime();
                                    if(systemTime != null && !systemTime.equals(""))  {
                                        labels.add("SystemTime");
                                    }
                                    String firmwareVersion = foundPlatform.getFirmwareVersion();
                                    if(firmwareVersion != null && !firmwareVersion.equals(""))  {
                                        labels.add("firmwareVersion");
                                    }
                                    String hardwareVersion = foundPlatform.getHardwareVersion();
                                    if(hardwareVersion != null && !hardwareVersion.equals(""))  {
                                        labels.add("hardwareVersion");
                                    }
                                    String osVersion = foundPlatform.getOsVersion();
                                    if(osVersion != null && !osVersion.equals(""))  {
                                        labels.add("osVersion");
                                    }
                                    String platformVersion = foundPlatform.getPlatformVersion();
                                    if(platformVersion != null && !platformVersion.equals(""))  {
                                        labels.add("platformVersion");
                                    }
                                    String platformId = foundPlatform.getPlatformId();
                                    if(platformId != null && !platformId.equals(""))  {
                                        labels.add("platformId");
                                    }
                                    String manufactureDate = foundPlatform.getManufactureDate();
                                    if(manufactureDate != null && !manufactureDate.equals(""))  {
                                        labels.add("manufactureDate");
                                    }
                                    String manufactureName = foundPlatform.getManufactureName();
                                    if(manufactureName != null && !manufactureName.equals(""))  {
                                        labels.add("manufactureName");
                                    }
                                    String manufactureUrl = foundPlatform.getManufactureUrl();
                                    if(manufactureUrl != null && !manufactureUrl.equals(""))  {
                                        labels.add("manufactureUrl");
                                    }
                                    String modelNo = foundPlatform.getModelNo();
                                    if(modelNo != null && !modelNo.equals(""))  {
                                        labels.add("modelNumber");
                                    }

                                    break;
                                case "/oic/d":
                                    String deviceId = foundDevice.getDeviceId();
                                    if(deviceId != null && !deviceId.equals(""))  {
                                        labels.add("deviceId");
                                    }
                                    String deviceName = foundDevice.getDeviceName();
                                    if(deviceName != null && !deviceName.equals(""))  {
                                        labels.add("deviceName");
                                    }
                                    labels.add("host");

                                    break;
                                default:
                                    String defaultUri = foundDefaultResource.getUri();
                                    if(defaultUri != null && !defaultUri.equals("")) {
                                        labels.add("Resource URI");
                                    }
                                    boolean connType = foundDefaultResource.getConnectivityTypeSet().isEmpty();
                                    if(!connType) {
                                        labels.add("Connectivity Type");
                                    }
                                    boolean observable = foundDefaultResource.isObservable();
                                    if(observable) {
                                        labels.add("observable");
                                    }
                                    boolean resType = foundDefaultResource.getResourceTypes().isEmpty();
                                    if(!resType) {
                                        labels.add("Resource Types");
                                    }
                                    int nResIF = foundDefaultResource.getResourceInterfaces().size();
                                    if(nResIF > 0) {
                                        labels.add("Resource Interface");
                                    }
                                    //
                                    String attribute = "attribute[";
                                    for(String key : foundCtrlResource.getValues().keySet()) {
                                        attribute += key + ",";
                                    }
                                    attribute = attribute.substring(0, attribute.length()-1);
                                    attribute += "]";
                                    labels.add(attribute);
                            }

                            Log.i(TAG, "==========================>trace-2");
                            op.createContainer(labels, item);
                            op.closeConnecton();
                            Log.i(TAG, "==========================>trace-3");

                        }
                    }
                }.start();

                */
            }
        });


    }

}
