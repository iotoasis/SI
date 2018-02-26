package com.canonical.democlient;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * Created by seung-wanmun on 2016. 8. 19..
 */
public class TabFragPlatform extends Fragment {

    private final static String TAG = TabFragPlatform.class.getSimpleName();
    private FoundDeviceInfo foundDeviceInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_fragment_3, container, false);

        Intent intent = getActivity().getIntent();
        final String hosturi = intent.getStringExtra("host");

        foundDeviceInfo = (FoundDeviceInfo) inflater.getContext().getApplicationContext();

        TableLayout ll = (TableLayout)view.findViewById(R.id.platform_table_layout);

        Context context = inflater.getContext();
        Log.i(TAG, "***********************************>>>>> platform size=" + foundDeviceInfo.getPlatformList().size());
        for(FoundPlatform foundPlatform : foundDeviceInfo.getPlatformList()) {
            Log.i(TAG, "***********************************>>>>> platform ID=" + foundPlatform.getPlatformId());
            if(foundPlatform.getHost().equalsIgnoreCase(hosturi)) {
                TableRow row = new TableRow(context);
                row.setBackgroundColor(Color.rgb(237, 239, 241));
                row.setPadding(5, 5, 5, 5);

                TextView tv1 = new TextView(context);
                tv1.setText("ID");
                tv1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                row.addView(tv1);

                TextView tv2 = new TextView(context);
                tv2.setText(foundPlatform.getPlatformId());
                tv2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                row.addView(tv2);

                ll.addView(row);

                row = new TableRow(context);
                row.setBackgroundColor(Color.rgb(237, 239, 241));
                row.setPadding(5, 5, 5, 5);
                tv1 = new TextView(context);
                tv1.setText("Manufacturer Name");
                tv1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                row.addView(tv1);

                tv2 = new TextView(context);
                tv2.setText(foundPlatform.getManufactureName());
                tv2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                row.addView(tv2);

                ll.addView(row);

                row = new TableRow(context);
                row.setBackgroundColor(Color.rgb(237, 239, 241));
                row.setPadding(5, 5, 5, 5);
                tv1 = new TextView(context);
                tv1.setText("Manufacturer URL");
                tv1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                row.addView(tv1);

                tv2 = new TextView(context);
                tv2.setText(foundPlatform.getManufactureUrl());
                tv2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                row.addView(tv2);

                ll.addView(row);

                row = new TableRow(context);
                row.setBackgroundColor(Color.rgb(237, 239, 241));
                row.setPadding(5, 5, 5, 5);
                tv1 = new TextView(context);
                tv1.setText("Model No");
                tv1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                row.addView(tv1);

                tv2 = new TextView(context);
                tv2.setText(foundPlatform.getModelNo());
                tv2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                row.addView(tv2);

                ll.addView(row);

                row = new TableRow(context);
                row.setBackgroundColor(Color.rgb(237, 239, 241));
                row.setPadding(5, 5, 5, 5);
                tv1 = new TextView(context);
                tv1.setText("Date of Manufacture");
                tv1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                row.addView(tv1);

                tv2 = new TextView(context);
                tv2.setText(foundPlatform.getManufactureDate());
                tv2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                row.addView(tv2);

                ll.addView(row);

                row = new TableRow(context);
                row.setBackgroundColor(Color.rgb(237, 239, 241));
                row.setPadding(5, 5, 5, 5);
                tv1 = new TextView(context);
                tv1.setText("Version");
                tv1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                row.addView(tv1);

                tv2 = new TextView(context);
                tv2.setText(foundPlatform.getPlatformVersion());
                tv2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                row.addView(tv2);

                ll.addView(row);

                row = new TableRow(context);
                row.setBackgroundColor(Color.rgb(237, 239, 241));
                row.setPadding(5, 5, 5, 5);
                tv1 = new TextView(context);
                tv1.setText("OS Version");
                tv1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                row.addView(tv1);

                tv2 = new TextView(context);
                tv2.setText(foundPlatform.getOsVersion());
                tv2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                row.addView(tv2);

                ll.addView(row);

                row = new TableRow(context);
                row.setBackgroundColor(Color.rgb(237, 239, 241));
                row.setPadding(5, 5, 5, 5);
                tv1 = new TextView(context);
                tv1.setText("Hardware Version");
                tv1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                row.addView(tv1);

                tv2 = new TextView(context);
                tv2.setText(foundPlatform.getHardwareVersion());
                tv2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                row.addView(tv2);

                ll.addView(row);

                row = new TableRow(context);
                row.setBackgroundColor(Color.rgb(237, 239, 241));
                row.setPadding(5, 5, 5, 5);
                tv1 = new TextView(context);
                tv1.setText("Firmware Version");
                tv1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                row.addView(tv1);

                tv2 = new TextView(context);
                tv2.setText(foundPlatform.getFirmwareVersion());
                tv2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                row.addView(tv2);

                ll.addView(row);

                row = new TableRow(context);
                row.setBackgroundColor(Color.rgb(237, 239, 241));
                row.setPadding(5, 5, 5, 5);
                tv1 = new TextView(context);
                tv1.setText("Support URL");
                tv1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                row.addView(tv1);

                tv2 = new TextView(context);
                tv2.setText(foundPlatform.getSupportUrl());
                tv2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                row.addView(tv2);

                ll.addView(row);

                row = new TableRow(context);
                row.setBackgroundColor(Color.rgb(237, 239, 241));
                row.setPadding(5, 5, 5, 5);
                tv1 = new TextView(context);
                tv1.setText("System Time");
                tv1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                row.addView(tv1);

                tv2 = new TextView(context);
                tv2.setText(foundPlatform.getSystemTime());
                tv2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                row.addView(tv2);

                ll.addView(row);

            }
        }

        return view;
    }
}
