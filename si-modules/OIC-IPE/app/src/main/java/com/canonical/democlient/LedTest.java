package com.canonical.democlient;

import android.app.Activity;
import android.app.Dialog;
import android.widget.Button;
import android.widget.SeekBar;

/**
 * Created by 문선호 on 2016-10-10.
 */
public class LedTest extends Dialog {
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

    public LedTest(Activity a, String type_set, String content_set,
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
}
