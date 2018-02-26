package com.canonical.democlient;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {
    private final static String TAG = MainActivity.class.getSimpleName();


    /* Configure and then find all the resource by resource classes */
    private void startDemoClient() {
        final Context context = this;

        new Thread(){
            public void run(){

                runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        try {

                            Button button = (Button)findViewById(R.id.main_btn_1);
                            button.setText("Device Discovery");
                            button.setEnabled(true);

                            Intent intent_ = new Intent(context, DiscoveryResultActivity.class);
                            startActivity(intent_);

                        } catch( Exception e ) {
                            msg( e.toString() );
                        }
                    }
                });


            }
        }.start();
    }

    private void msg(final String text) {
        Log.i(TAG, text);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button button = (Button)findViewById(R.id.main_btn_1);

        if (null == savedInstanceState) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    button.setText("Discovering....");
                    button.setEnabled(false);
                    new Thread(new Runnable() {
                        public void run() {
                            startDemoClient();
                        }
                    }).start();
                }
            });
        } else {
            String consoleOutput = savedInstanceState.getString("consoleOutputString");
            msg("                                  console output    " + consoleOutput);
            //mConsoleTextView.setText(consoleOutput);
        }
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "on Destroy");

        super.onDestroy();
    }
}
