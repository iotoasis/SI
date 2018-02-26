package com.canonical.democlient;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by 문선호 on 2016-10-07.
 */
public class PageBuzzer extends PageHandler implements android.view.View.OnClickListener {

    private final static String TAG = MainActivity.class.getSimpleName();
    private HttpOneM2MOperation op = new HttpOneM2MOperation();
    private Button button_set, button_cancel;

    private int tone;

    /* OIC Module */
    private FoundItem foundItem;

    private void initData(){
        Intent intent = getIntent();
        foundItem = (FoundItem)intent.getSerializableExtra("foundItem");
    }

    private void sendData(){
        new Thread(){
            @Override
            public void run() {
                try {
                    initData();
                    op.init(Util.makeUrl(foundItem.getHost(), foundItem.getDeviceId(), Constants.EXECUTE), foundItem);
                    foundItem.setContent(tone);
                    setCmdList();
                    JSONObject result = op.createContentInstance(getCmdList(), foundItem);
                    Log.e(TAG, result.toString());
                } catch (Exception je) {
                    Log.e(TAG, je.toString());
                }
            }
        }.start();
        button_set.setEnabled(true);
        button_set.setText("SET");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.control_buzzer);

        Spinner spinner = (Spinner) findViewById(R.id.spinner_buzzer_a);

        button_set = (Button) findViewById(R.id.button_buzzer_a_set);
        button_set.setOnClickListener(this);
        button_cancel = (Button) findViewById(R.id.button_buzzer_a_cancel);
        button_cancel.setOnClickListener(this);

        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
                Log.e("BuzzerControl", "Position: " + String.valueOf(position));
                switch(position) {
                    case 0:
                        tone = 1915;
                        break;
                    case 1:
                        tone = 1700;
                        break;
                    case 2:
                        tone = 1519;
                        break;
                    case 3:
                        tone = 1432;
                        break;
                    case 4:
                        tone = 1275;
                        break;
                    case 5:
                        tone = 1136;
                        break;
                    case 6:
                        tone = 1014;
                        break;
                    case 7:
                        tone = 956;
                        break;
                }
            }

            public void onNothingSelected(AdapterView arg0) {
                Log.e("BuzzerControl", "No selection");
                tone = 1915;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_buzzer_a_set:
                button_set.setText("Putting ...");
                button_set.setEnabled(false);
                sendData();
                break;
            case R.id.button_buzzer_a_cancel:
                finish();
                break;
            default:
                break;
        }
    }
}
