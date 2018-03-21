package com.nandi.cqdisaster.examine.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import java.util.List;

/**
 * Created by ChenPeng on 2017/3/6.
 */

public class FillInfoPagerAdapter extends FragmentPagerAdapter {
    private String[] titles={"基础信息","采集信息","媒体信息"};
    private List<Fragment> fragments;
    public FillInfoPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments=fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
