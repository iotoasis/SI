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
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import org.iotivity.base.OcResource;

/**
 * Created by seung-wanmun on 2016. 8. 19..
 */
public class TabFragDefault extends Fragment {

    private final static String TAG = TabFragDefault.class.getSimpleName();
    private FoundDeviceInfo foundDeviceInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment_1, container, false);
        View detail = inflater.inflate(R.layout.activity_detail, container, false);

        Intent intent = getActivity().getIntent();
        final String resuri = intent.getStringExtra("resuri");

        foundDeviceInfo = (FoundDeviceInfo) inflater.getContext().getApplicationContext();

        TableLayout ll = (TableLayout)view.findViewById(R.id.default_table_layout);

        Context context = inflater.getContext();

        String addr, connectType, resType, resIf;
        boolean observable;

        for(OcResource ocResource : foundDeviceInfo.getDefaultResourceList()) {
            Log.i(TAG, ocResource.toString());
            if(ocResource.getUri().equalsIgnoreCase(resuri)) {

                addr = ocResource.getHost();
                connectType = ocResource.getConnectivityTypeSet().toString();
                observable = ocResource.isObservable();
                resType = ocResource.getResourceTypes().toString();
                resIf = ocResource.getResourceInterfaces().toString();


                TableRow row = new TableRow(context);
                row.setBackgroundColor(Color.rgb(237, 239, 241));
                row.setPadding(5, 5, 5, 5);

                TextView tv1 = new TextView(context);
                tv1.setText("Resource URI");
                tv1.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f));
                row.addView(tv1);

                TextView tv2 = new TextView(context);
                tv2.setText(resuri);
                tv2.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f));
                row.addView(tv2);

                Log.i(TAG, "###########################>" + resuri);

                ll.addView(row);

                row = new TableRow(context);
                row.setBackgroundColor(Color.rgb(237, 239, 241));
                row.setPadding(5, 5, 5, 5);
                tv1 = new TextView(context);
                tv1.setText("Address");
                tv1.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f));
                row.addView(tv1);

                tv2 = new TextView(context);
                tv2.setText(addr);
                tv2.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f));
                row.addView(tv2);

                ll.addView(row);

                row = new TableRow(context);
                row.setBackgroundColor(Color.rgb(237, 239, 241));
                row.setPadding(5, 5, 5, 5);
                tv1 = new TextView(context);
                tv1.setText("Connectvity Type");
                tv1.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f));
                row.addView(tv1);

                tv2 = new TextView(context);
                tv2.setText(connectType);

                tv2.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f));
                row.addView(tv2);

                ll.addView(row);

                row = new TableRow(context);
                row.setBackgroundColor(Color.rgb(237, 239, 241));
                row.setPadding(5, 5, 5, 5);
                tv1 = new TextView(context);
                tv1.setText("Observable");
                tv1.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f));
                row.addView(tv1);

                tv2 = new TextView(context);
                tv2.setText(observable == true ? "true" : "false");
                tv2.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f));
                row.addView(tv2);

                ll.addView(row);

                row = new TableRow(context);
                row.setBackgroundColor(Color.rgb(237, 239, 241));
                row.setPadding(5, 5, 5, 5);
                tv1 = new TextView(context);
                tv1.setText("Resource Types");
                tv1.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f));
                row.addView(tv1);

                tv2 = new TextView(context);
                tv2.setText(resType);
                tv2.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f));
                row.addView(tv2);

                ll.addView(row);

                row = new TableRow(context);
                row.setBackgroundColor(Color.rgb(237, 239, 241));
                row.setPadding(5, 5, 5, 5);
                tv1 = new TextView(context);
                tv1.setText("Resource IF");
                tv1.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f));
                row.addView(tv1);

                tv2 = new TextView(context);
                tv2.setText(resIf);
                tv2.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f));
                row.addView(tv2);

                ll.addView(row);

            }

        }

        return view;
    }
}
