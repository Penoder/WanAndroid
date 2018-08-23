package com.penoder.mylibrary.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Penoder
 * @date 2017/11/16
 */
public class CommonFragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments = new ArrayList<>();

    private List<String> mTitles = null;

    public CommonFragmentAdapter(FragmentManager fm, Fragment... fragments) {
        super(fm);
        Collections.addAll(this.fragments, fragments);
    }

    public CommonFragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    public void setTitles(List<String> titles) {
        mTitles = titles;
    }

    public void setTitles(String[] titles) {
        if (mTitles == null) {
            mTitles = new ArrayList<>();
        }
        mTitles.clear();
        Collections.addAll(mTitles, titles);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments != null ? fragments.size() : 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (mTitles != null && mTitles.size() > 0) {
            return mTitles.get(position);
        } else {
            return super.getPageTitle(position);
        }
    }
}