package com.canonical.democlient;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class LcdControl extends Dialog implements
        android.view.View.OnClickListener {

    private Activity c;
    private String msg_type_set;

    private Button button_set, button_cancel;
    private EditText editText;
    private String str;

    public String msg_content_set;

    public LcdControl(Activity a, String msg_type, String content_type) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        msg_type_set = msg_type;
        msg_content_set = content_type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.lcd_control);

        str = "";

        button_set = (Button) findViewById(R.id.button_lcd_set);
        button_set.setOnClickListener(this);
        button_cancel = (Button) findViewById(R.id.button_lcd_cancel);
        button_cancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_lcd_set:
                editText = (EditText) findViewById(R.id.editText_lcd);
                String temp = editText.getText().toString();
                if(!temp.isEmpty())
                    str = temp;

                sendMessage();
                dismiss();
                break;
            case R.id.button_lcd_cancel:
                dismiss();
                break;
            default:
                break;
        }
    }

    private void sendMessage() {
        Intent intent = new Intent(msg_type_set);
        // You can also include some extra data.
        intent.putExtra(msg_content_set, str);
        LocalBroadcastManager.getInstance(this.c).sendBroadcast(intent);
    }
}
