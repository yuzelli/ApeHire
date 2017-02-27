package com.example.buiderdream.apehire.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2017/2/24.
 */

public class TeachFragAdapter extends FragmentPagerAdapter {

    //标签
    private List<String> title_list = new ArrayList<String>();
    //页面
    private  List<Fragment> fragment_list = new ArrayList<Fragment>();

    public void addTitle(List<String> title_list){
        this.title_list =title_list;
    }
    public void addFragment(List<Fragment> fragment_list){
        this.fragment_list = fragment_list;
    }

    public TeachFragAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragment_list.get(position);
    }

    @Override
    public int getCount() {
        return fragment_list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title_list.get(position);
    }
}
