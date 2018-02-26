package com.canonical.democlient;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * Created by 문선호 on 2016-10-07.
 */
public class PageTouch extends PageHandler implements android.view.View.OnClickListener {

    private final static String TAG = MainActivity.class.getSimpleName();
    private HttpOneM2MOperation op = new HttpOneM2MOperation();
    private Button button_cancel;

    private Thread observe_result;
    private boolean isObserving = false;

    /* OIC Module */
    private FoundItem foundItem;

    private void initData(){
        Intent intent = getIntent();
        foundItem = (FoundItem)intent.getSerializableExtra("foundItem");
    }

    private void receiveData(){
        isObserving = true;
        int i=0;
        while( !Thread.interrupted() || isObserving ) {
            try {
                Log.i("WHILE >>>  >>>>  >>", "                [" + ++i + "]");
                initData();
                op.init(Util.makeUrl(foundItem.getHost(), Constants.POLLING_CHANNEL_S, "pcu"), foundItem);
                setObsList(foundItem.getDeviceId());
                String xml = op.retrievePollingChannelPCU();
                final String value = super.getValue(xml,"con");
                if( super.getValue(xml,"cr").equals("touch") ){
                    Log.e(TAG, value);
                    runOnUiThread(new Runnable(){
                        @Override
                        public void run() {
                            appendRow( value );
                        }
                    });
                }
            } catch (Exception je) {
                Log.e(TAG, je.toString());
            }
        }
        Log.e(TAG, "           BREAK  !! ");
    }

    public void start_observe(){
        observe_result = new Thread(new Runnable() {
            @Override
            public void run() {
                receiveData();
            }
        });
        observe_result.start();
    }

    public void stop_observe(){
        isObserving = false;
        observe_result.interrupt();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.control_touch);

        button_cancel = (Button) findViewById(R.id.button_touch_a_cancel);
        button_cancel.setOnClickListener(this);

        start_observe();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_touch_a_cancel:
                stop_observe();
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed(){
        Log.e(TAG, "           Back  !! ");
        stop_observe();
        finish();
    }
}
