package com.canonical.democlient;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by seung-wanmun on 2016. 8. 11..
 */
public class DiscoveryListActivity extends AppCompatActivity {

    /**
     * Called when the activity is first created.
     */

    private final static String INCSE_ADDR = "http://10.10.224.240:8080/nimbus-cse";
    private final static String TAG = DiscoveryListActivity.class.getSimpleName();

    private ArrayList<FoundItem> image_details;
    private CustomListAdapter customListAdapter;

    private ResponseSynchThread synchronizerThread;

    private FoundDeviceInfo foundDeviceInfo;

    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
        customListAdapter.notifyDataSetChanged();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.discovery_result);

        Intent intent = getIntent();
        foundDeviceInfo = (FoundDeviceInfo)intent.getSerializableExtra("foundDeviceInfo");

        setCustomActionbar();
        image_details = new ArrayList<FoundItem>();

        for(FoundItem foundItem : foundDeviceInfo.getDeviceList()) {
            image_details.add(foundItem);
        }

        final View empty = findViewById(R.id.list_empty_1);
        final ListView lv1 = (ListView) findViewById(R.id.discovery_list);
        customListAdapter = new CustomListAdapter(this, image_details);

        if(image_details.isEmpty()) {
            lv1.setEmptyView(empty);
        } else {
            lv1.setAdapter(customListAdapter);
            empty.setVisibility(View.GONE);
        }

    }

    private void setCustomActionbar() {
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        View mCustomView = LayoutInflater.from(this).inflate(R.layout.discovery_res_ab, null);
        actionBar.setCustomView(mCustomView);

        /*
        ImageButton imgButton = (ImageButton)findViewById(R.id.device_reg_btn);
        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "|||||||||||||||||||||| Device Info rgistered |||||||||||||||||");

                new Thread() {
                    public void run() {
                        HttpOneM2MOperation op = new HttpOneM2MOperation();
                        ArrayList<String> labels;
                        String host;
                        ArrayList<FoundDeviceInfo.ResourceInfo> resourceInfos;

                        for(FoundItem item :  foundDeviceInfo.getDeviceList()) {
                            host = item.getHost();
                            resourceInfos = foundDeviceInfo.getResource(host);
                            labels = new ArrayList<String>();
                            labels.add("oic.d");
                            labels.add("oic.p");
                            for(FoundDeviceInfo.ResourceInfo resinfo : resourceInfos) {
                                labels.add(resinfo.resourceUri.substring(1));
                            }
                            Log.i(TAG, "==========================>trace-1");
                            op.init(INCSE_ADDR, item);
                            Log.i(TAG, "==========================>trace-2");
                            op.createAE(item, labels);
                            op.closeConnecton();
                            Log.i(TAG, "==========================>trace-3");
                        }
                    }
                }.start();


            }
        });
        */


        //actionBar.setBackgroundDrawable(new ColorDrawable(Color.DKGRAY));


    }

    private static class ResponseSynchThread implements Runnable {

        LinkedList<Runnable> responseQueue = new LinkedList<Runnable>();

        @Override
        public void run() {
            while(!Thread.interrupted()) {
                synchronized (this) {
                    try {
                        while (responseQueue.isEmpty()) {
                            this.wait();
                            break;
                        }
                    } catch (InterruptedException e) {
                        return;
                    }
                }

                Runnable thread;
                synchronized (this) {
                    thread = responseQueue.pop();
                }

                try {
                    thread.run();
                } catch (Exception e) {
                    if(e instanceof InterruptedException) {
                        return;
                    }
                    e.printStackTrace();
                }
            }
        }

        public void addToQueue(Runnable event) {
            synchronized (this) {
                responseQueue.add(event);
                this.notify();
            }
        }
    }

}
