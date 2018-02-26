package com.canonical.democlient;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import org.json.JSONObject;

/**
 * Created by 문선호 on 2016-10-07.
 */
public class PageLed extends PageHandler implements android.view.View.OnClickListener  {

    private final static String TAG = MainActivity.class.getSimpleName();
    private HttpOneM2MOperation op = new HttpOneM2MOperation();
    private Button button_set, button_cancel;
    private SeekBar seekBar;
    private TextView led_brightness;
    private int progress = 0;

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
                    foundItem.setContent(progress);
                    setCmdList(foundItem.getDeviceId());
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
        setContentView(R.layout.control_led);

        seekBar = (SeekBar) findViewById(R.id.seekBar_led_a);
        led_brightness = (TextView) findViewById(R.id.led_brightness);
        seekBar.setProgress(progress);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;
                led_brightness.setText("Brightness : "+(progress/255)*100);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                led_brightness.setText("Brightness : "+seekBar.getProgress());
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                led_brightness.setText("Brightness : "+seekBar.getProgress());
            }
        });

        button_set = (Button) findViewById(R.id.button_led_a_set);
        button_set.setOnClickListener(this);

        button_cancel = (Button) findViewById(R.id.button_led_a_cancel);
        button_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_led_a_set:
                button_set.setText("Putting ...");
                button_set.setEnabled(false);
                sendData();
                break;
            case R.id.button_led_a_cancel:
                finish();
                break;
            default:
                break;
        }
    }
}
