package com.canonical.democlient;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * Created by seung-wanmun on 2016. 8. 19..
 */
public class TabFragDevice extends Fragment {

    private final static String TAG = TabFragDevice.class.getSimpleName();
    private FoundDeviceInfo foundDeviceInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_fragment_2, container, false);

        Intent intent = getActivity().getIntent();
        final String hosturi = intent.getStringExtra("host");

        foundDeviceInfo = (FoundDeviceInfo) inflater.getContext().getApplicationContext();

        TableLayout ll = (TableLayout)view.findViewById(R.id.device_table_layout);

        Context context = inflater.getContext();



        for(FoundItem foundItem : foundDeviceInfo.getDeviceList()) {
            if(foundItem.getHost().equalsIgnoreCase(hosturi)) {
                TableRow row = new TableRow(context);
                row.setBackgroundColor(Color.rgb(237, 239, 241));
                row.setPadding(5, 5, 5, 5);

                TextView tv1 = new TextView(context);
                tv1.setText("Device ID");
                tv1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                row.addView(tv1);

                TextView tv2 = new TextView(context);
                tv2.setText(foundItem.getDeviceId());
                tv2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                row.addView(tv2);

                ll.addView(row);

                row = new TableRow(context);
                row.setBackgroundColor(Color.rgb(237, 239, 241));
                row.setPadding(5, 5, 5, 5);
                tv1 = new TextView(context);
                tv1.setText("Device Name");
                tv1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                row.addView(tv1);

                tv2 = new TextView(context);
                tv2.setText(foundItem.getDeviceName());
                tv2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                row.addView(tv2);

                ll.addView(row);

                row = new TableRow(context);
                row.setBackgroundColor(Color.rgb(237, 239, 241));
                row.setPadding(5, 5, 5, 5);
                tv1 = new TextView(context);
                tv1.setText("Address");
                tv1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                row.addView(tv1);

                tv2 = new TextView(context);
                tv2.setText(foundItem.getHost());
                tv2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                row.addView(tv2);

                ll.addView(row);


            }
        }

        return view;

    }
}
