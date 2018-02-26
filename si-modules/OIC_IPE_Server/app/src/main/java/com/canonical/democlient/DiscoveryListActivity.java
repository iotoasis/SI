package com.canonical.democlient;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by seung-wanmun on 2016. 8. 11..
 */
public class DiscoveryListActivity extends AppCompatActivity {

    /**
     * Called when the activity is first created.
     */

    //private final static String INCSE_ADDR = "http://10.10.224.240:8080/nimbus-cse";
    private final static String INCSE_ADDR = "http://10.10.0.76:8080/herit-cse";
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

        setCustomActionbar();

        /*
        foundDeviceInfo = (FoundDeviceInfo)getApplicationContext();

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
        }
        */

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
                Log.i(TAG, "|||||||||||||||||||||| Device Info rgistered |||||||||||||");


                /*
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
                            for (FoundDeviceInfo.ResourceInfo resinfo : resourceInfos) {
                                labels.add(resinfo.resourceUri.substring(1));
                            }
                            Log.i(TAG, "==========================>trace-1");
                            op.init(Constants.INCSE_ADDR, item);
                            Log.i(TAG, "==========================>trace-2");

                            // AE생성
                            final JSONObject ae_result = op.createAE(item, labels);
                            Log.i(TAG, ">>>>>>>>>>>>>>>>>>>>>>>> RESPONSE_CODE ::: " + (ae_result == null));
                            Log.i(TAG, ">>>>>>>>>>>>>>>>>>>>>>>> RESPONSE_CODE ::: " + ae_result);

                            try {
                                // Polling channel 생성
                                Log.i(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>      =-=-=-=   >> Polling 생성 전");
                                op.init(Util.makeUrl(Constants.INCSE_ADDR, ae_result.getJSONObject("ae")), item);
                                JSONObject pch_result = op.createPollingChannel(item, labels);
                                Log.i(TAG, ">>>>>>>>>>>>>>>>>>>>>>>> RESPONSE_CODE ::: " + (pch_result == null));
                                Log.i(TAG, ">>>>>>>>>>>>>>>>>>>>>>>> RESPONSE_CODE ::: " + pch_result);
                                Log.i(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>      =-=-=-=   >> Polling 생성 후");

                                // RESPONSE-CODE가 존재하지 않을 때 : 정상적으로 AE 생성 했을 때
                                if (ae_result.getJSONObject("ae") != null) {

                                    op.init(Util.makeUrl(Constants.INCSE_ADDR, ae_result.getJSONObject("ae")), item);


                                    // 각각의 AE 밑에 플랫폼 Container 생성
                                    ArrayList<JSONObject> device_name_list = new ArrayList<JSONObject>();
                                    for (FoundDeviceInfo.ResourceInfo resinfo : resourceInfos) {

                                        JSONObject platform_result = op.createContainer(labels, resinfo);
                                        boolean isPassed = true;
                                        for( int i=0; i<Constants.PASS_URL.length; i++ ){
                                            if( platform_result.getJSONObject("cnt").getString("rn").indexOf(Constants.PASS_URL[i]) > -1 ) {
                                                isPassed = false;
                                                break;
                                            }
                                        }
                                        if( isPassed ){
                                            device_name_list.add(platform_result);
                                            Log.i(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>      =-=-=-=   >> 생성 >> " + platform_result.getJSONObject("cnt").getString("rn"));
                                        }
                                    }


                                    for ( int i=0; i<device_name_list.size(); i++ ){
                                        op.init(Util.makeUrl(Constants.INCSE_ADDR, ae_result.getJSONObject("ae"), device_name_list.get(i).getJSONObject("cnt")), item);
                                        FoundDeviceInfo.ResourceInfo fd = new FoundDeviceInfo.ResourceInfo();
                                        for (String baseContainerName : Constants.BASE_CONTAINER_URL) {
                                            fd.resourceUri = baseContainerName;
                                            JSONObject function_result = op.createContainer(labels,fd);
                                        }
                                    }

                                    /*temp
                                    ArrayList<OcRepresentation> oc_list = foundDeviceInfo.getCtrlResourceList();
                                    try {
                                        for (int i = 0; i < oc_list.size(); i++) {
                                            for (String unuse : oc_list.get(i).getValues().keySet()) {
                                                for (int j = 0; j < device_name_list.size(); j++) {
                                                    String url = Util.makeUrl(Constants.INCSE_ADDR, ae_result.getJSONObject("ae"), device_name_list.get(j).getJSONObject("cnt"));
                                                    if (url.indexOf("oic/d") > -1 || url.indexOf("oic/p") > -1) {
                                                        Log.i(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>      =-=-=-=  condition pass ::::  >> " + url);
                                                    } else {
                                                        String param = url.substring(url.lastIndexOf("/") + 1).toLowerCase();
                                                        String key = oc_list.get(i).getValues().get("n").toString();
                                                        if (param.equals(key.toLowerCase())) {
                                                            Log.i(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>      =-=-=-=  result comp::::  >> " + param + "   [" + key + "]");
                                                            op.init(url, item);
                                                        } else {
                                                            Log.i(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>      =-=-=-=  result diff :::: " + param + "   [" + key + "]");
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    } catch(Exception e) {
                                        Log.i(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+e.toString() );
                                    }

                                        //op.init(Util.makeUrl(Constants.INCSE_ADDR, ae_result.getJSONObject("ae"), action_list.get(i).getJSONObject("cnt")), item);



                                } else {
                                    // RESPONSE-CODE가 존재할 때 : AE 생성 실패
                                    Log.i(TAG, ">>>>>>>>>>>>>>>>>>>>>>>> RESPONSE_CODE ::: " + ae_result.get("RESPONSE-CODE"));

                                    /*
                                    final Handler handler = new Handler();
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            handler.post(new Runnable() {
                                                public void run() {
                                                    try {
                                                        //Toast.makeText(DiscoveryListActivity.this, String.valueOf(HttpStatusCode.RESPONSE_STATUS.get(Integer.parseInt((String) result.get("RESPONSE-CODE")))), Toast.LENGTH_SHORT).show();
                                                    } catch(Exception e){

                                                    }
                                                }
                                            });
                                            SystemClock.sleep(1 * 60 * 1000);
                                        }
                                    }).start();



                                }
                            } catch(JSONException je) {
                                Log.i(TAG, "==========================> !!!!!  JSONException  !!!!!");
                                Log.i(TAG, "==========================> "+je.toString());
                            }

                            // 각각의 컨테이너에 execute, result, data 세 개의 컨테이너 생성



                            op.closeConnecton();
                            Log.i(TAG, "==========================>trace-3");
                        }
                    }
                }.start();
                */

            }
        });


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
