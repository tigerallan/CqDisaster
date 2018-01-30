package com.nandi.cqdisaster.securitydao.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by qingsong on 2017/11/25.
 */

public class MessageAdapter extends FragmentPagerAdapter {
    private String[] mTitles = new String[]{"基本信息", "图片","视频","人员信息"};
    private List<Fragment> list;

    public MessageAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return mTitles.length>0 ? mTitles.length : 0;
    }

    //用来设置tab的标题
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}

