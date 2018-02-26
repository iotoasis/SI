package com.canonical.democlient;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import org.iotivity.base.OcException;
import org.iotivity.base.OcRepresentation;

public class LedControlP extends Dialog implements
        android.view.View.OnClickListener {

    private Activity c;
    private Button button_back;
    private Switch switch_red;
    private Switch switch_green;
    private Switch switch_blue;

    private int status_red;
    private int status_green;
    private int status_blue;

    private boolean put_done = true;

    private String msg_type_set;
    public String msg_content_set_red;
    public String msg_content_set_green;
    public String msg_content_set_blue;

    private String msg_type_put_done;
    private String msg_content_put_done;

    public LedControlP(Activity a, String type_set, String content_set_red,
                       String content_set_green, String content_set_blue,
                       String type_done, String content_done,
                       int r, int g, int b) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        status_red = r;
        status_green = g;
        status_blue = b;

        msg_type_set = type_set;
        msg_content_set_red = content_set_red;
        msg_content_set_green = content_set_green;
        msg_content_set_blue = content_set_blue;
        msg_type_put_done = type_done;
        msg_content_put_done = content_done;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.led_control_p);

        switch_red = (Switch) findViewById(R.id.switch_led_p_red);
        switch_green = (Switch) findViewById(R.id.switch_led_p_green);
        switch_blue = (Switch) findViewById(R.id.switch_led_p_blue);

        switch_red.setChecked(status_red != 0);
        switch_green.setChecked(status_green != 0);
        switch_blue.setChecked(status_blue != 0);

        switch_red.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if (isChecked) {
                    Log.e("LED Control P", "Red led on");
                    status_red = 1;
                } else {
                    Log.e("LED Control P", "Red led off");
                    status_red = 0;
                }
                disable_switch();
                sendMessage();
            }
        });

        switch_green.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if (isChecked) {
                    Log.e("LED Control P", "Green led on");
                    status_green = 1;
                } else {
                    Log.e("LED Control P", "Green led off");
                    status_green = 0;
                }
                disable_switch();
                sendMessage();
            }
        });

        switch_blue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if (isChecked) {
                    Log.e("LED Control P", "Blue led on");
                    status_blue = 1;
                } else {
                    Log.e("LED Control P", "Blue led off");
                    status_blue = 0;
                }
                disable_switch();
                sendMessage();
            }
        });


        button_back = (Button) findViewById(R.id.button_led_p_back);
        button_back.setOnClickListener(this);

        LocalBroadcastManager.getInstance(this.c).registerReceiver(mLedPutDoneReceiver,
                new IntentFilter(msg_type_put_done));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_led_p_back:
                dismiss();
                break;
            default:
                break;
        }
    }

    private void disable_switch() {
        switch_red.setEnabled(false);
        switch_green.setEnabled(false);
        switch_blue.setEnabled(false);
    }

    private void enable_switch() {
        switch_red.setEnabled(true);
        switch_green.setEnabled(true);
        switch_blue.setEnabled(true);
    }

    private void sendMessage() {
        Intent intent = new Intent(msg_type_set);
        // You can also include some extra data.
        intent.putExtra(msg_content_set_red, status_red);
        intent.putExtra(msg_content_set_green, status_green);
        intent.putExtra(msg_content_set_blue, status_blue);
        LocalBroadcastManager.getInstance(this.c).sendBroadcast(intent);
    }

    private BroadcastReceiver mLedPutDoneReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            put_done = intent.getBooleanExtra(msg_content_put_done, false);
            if(put_done) {
                enable_switch();
            }
        }
    };
}
