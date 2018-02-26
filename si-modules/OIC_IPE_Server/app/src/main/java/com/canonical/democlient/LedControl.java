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
import android.widget.SeekBar;

import org.iotivity.base.OcException;
import org.iotivity.base.OcRepresentation;

public class LedControl extends Dialog implements
        android.view.View.OnClickListener {

    private Activity c;
    private Button button_set;
    private Button button_cancel;

    private boolean put_done = true;

    private SeekBar seekBar;
    private int progress = 0;

    private String msg_type_set;
    public String msg_content_set;

    private String msg_type_put_done;
    private String msg_content_put_done;

    public LedControl(Activity a, String type_set, String content_set,
                      String type_done, String content_done, int progress_curr) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        progress = progress_curr;
        msg_type_set = type_set;
        msg_content_set = content_set;
        msg_type_put_done = type_done;
        msg_content_put_done = content_done;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.led_control);

        seekBar = (SeekBar) findViewById(R.id.seekBar_led_a);
        seekBar.setProgress(progress);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Do something here,
                //if you want to do anything at the start of
                // touching the seekbar
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Display the value in textview

            }
        });

        button_set = (Button) findViewById(R.id.button_led_a_set);
        button_set.setOnClickListener(this);

        button_cancel = (Button) findViewById(R.id.button_led_a_cancel);
        button_cancel.setOnClickListener(this);

        LocalBroadcastManager.getInstance(this.c).registerReceiver(mLedPutDoneReceiver,
                new IntentFilter(msg_type_put_done));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_led_a_set:
                put_done = false;
                button_set.setText("Putting ...");
                button_set.setEnabled(false);
                sendMessage();
                break;
            case R.id.button_led_a_cancel:
                dismiss();
                break;
            default:
                break;
        }
    }

    private void sendMessage() {
        Intent intent = new Intent(msg_type_set);
        // You can also include some extra data.
        intent.putExtra(msg_content_set, progress);
        LocalBroadcastManager.getInstance(this.c).sendBroadcast(intent);
    }

    private BroadcastReceiver mLedPutDoneReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            put_done = intent.getBooleanExtra(msg_content_put_done, false);
            if(put_done) {
                button_set.setEnabled(true);
                button_set.setText("SET");
            }
        }
    };
}
