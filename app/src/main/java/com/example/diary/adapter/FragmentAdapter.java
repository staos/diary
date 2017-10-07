package com.example.diary.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by 周要明 on 2017/8/16.
 */

public class FragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragment;
    public FragmentAdapter(FragmentManager fm,List<Fragment> fragments) {
        super(fm);
        mFragment = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragment.get(position);
    }

    @Override
    public int getCount() {
        return mFragment.size();
    }
}
