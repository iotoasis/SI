package com.canonical.democlient;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by seung-wanmun on 2016. 8. 19..
 */
public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumberOfTabs;

    public PagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.mNumberOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                TabFragDefault tabFragDefault = new TabFragDefault();
                return tabFragDefault;
            case 1:
                TabFragDevice tabFragDevice = new TabFragDevice();
                return tabFragDevice;
            case 2:
                TabFragPlatform tabFragPlatform = new TabFragPlatform();
                return tabFragPlatform;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumberOfTabs;
    }
}
