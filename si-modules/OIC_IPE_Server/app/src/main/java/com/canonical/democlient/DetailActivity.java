package com.canonical.democlient;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by seung-wanmun on 2016. 8. 19..
 */
public class DetailActivity extends AppCompatActivity {

    private final static String TAG = DetailActivity.class.getSimpleName();
    private FoundDeviceInfo foundDeviceInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

    /*    foundDeviceInfo = (FoundDeviceInfo) getApplicationContext();

        Intent intent = getIntent();
        String resuri = intent.getExtras().getString("resuri");

        TableLayout ll = (TableLayout) findViewById(R.id.default_table_layout);
        String addr, connectType, resType, resIf;
        boolean observable;

        for(OcResource ocResource : foundDeviceInfo.getCtrlResourceList()) {
            Log.i(TAG, ocResource.toString());
            if(ocResource.getUri().equalsIgnoreCase(resuri)) {

                addr = ocResource.getHost();
                connectType = ocResource.getConnectivityTypeSet().toString();
                observable = ocResource.isObservable();
                resType = ocResource.getResourceTypes().toString();
                resIf = ocResource.getResourceInterfaces().toString();

                TableRow row = new TableRow(this);
                TextView tv1 = new TextView(this);
                tv1.setText("Resource URI");
                tv1.setTextColor(Color.WHITE);
                tv1.setBackgroundColor(Color.BLACK);
                tv1.setPadding(3, 3, 3, 3);
                row.addView(tv1);

                TextView tv2 = new TextView(this);
                tv2.setText(resuri);
                tv2.setTextColor(Color.WHITE);
                tv2.setBackgroundColor(Color.BLACK);
                tv2.setPadding(3, 3, 3, 3);
                row.addView(tv2);

                Log.i(TAG, "###########################>" + resuri);

                ll.addView(row, 1);

                row = new TableRow(this);
                tv1 = new TextView(this);
                tv1.setText("Address");
                tv1.setTextColor(Color.WHITE);
                tv1.setBackgroundColor(Color.BLACK);
                tv1.setPadding(3, 3, 3, 3);
                row.addView(tv1);

                tv2 = new TextView(this);
                tv2.setText(addr);
                tv2.setTextColor(Color.WHITE);
                tv2.setBackgroundColor(Color.BLACK);
                tv2.setPadding(3, 3, 3, 3);
                row.addView(tv2);

                ll.addView(row);

                row = new TableRow(this);
                tv1 = new TextView(this);
                tv1.setText("Connectvity Type");
                tv1.setTextColor(Color.WHITE);
                tv1.setBackgroundColor(Color.BLACK);
                tv1.setPadding(3, 3, 3, 3);
                row.addView(tv1);

                tv2 = new TextView(this);
                tv2.setText(connectType);
                tv2.setTextColor(Color.WHITE);
                tv2.setBackgroundColor(Color.BLACK);
                tv2.setPadding(3, 3, 3, 3);
                row.addView(tv2);

                ll.addView(row);

                row = new TableRow(this);
                tv1 = new TextView(this);
                tv1.setText("Observable");
                tv1.setTextColor(Color.WHITE);
                tv1.setBackgroundColor(Color.BLACK);
                tv1.setPadding(3, 3, 3, 3);
                row.addView(tv1);

                tv2 = new TextView(this);
                tv2.setText(observable == true ? "true" : "false");
                tv2.setTextColor(Color.WHITE);
                tv2.setBackgroundColor(Color.BLACK);
                tv2.setPadding(3, 3, 3, 3);
                row.addView(tv2);

                ll.addView(row);

                row = new TableRow(this);
                tv1 = new TextView(this);
                tv1.setText("Resource Types");
                tv1.setTextColor(Color.WHITE);
                tv1.setBackgroundColor(Color.BLACK);
                tv1.setPadding(3, 3, 3, 3);
                row.addView(tv1);

                tv2 = new TextView(this);
                tv2.setText(resType);
                tv2.setTextColor(Color.WHITE);
                tv2.setBackgroundColor(Color.BLACK);
                tv2.setPadding(3, 3, 3, 3);
                row.addView(tv2);

                ll.addView(row);

                row = new TableRow(this);
                tv1 = new TextView(this);
                tv1.setText("Resource IF");
                tv1.setTextColor(Color.WHITE);
                tv1.setBackgroundColor(Color.BLACK);
                tv1.setPadding(3, 3, 3, 3);
                row.addView(tv1);

                tv2 = new TextView(this);
                tv2.setText(resIf);
                tv2.setTextColor(Color.WHITE);
                tv2.setBackgroundColor(Color.BLACK);
                tv2.setPadding(3, 3, 3, 3);
                row.addView(tv2);

                ll.addView(row);

            }

        } */

    //    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    //    setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Default"));
        tabLayout.addTab(tabLayout.newTab().setText("Device"));
        tabLayout.addTab(tabLayout.newTab().setText("Platform"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

 */
}