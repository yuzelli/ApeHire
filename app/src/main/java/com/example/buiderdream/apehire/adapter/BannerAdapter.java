package com.example.buiderdream.apehire.adapter;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/12/30.
 */

public class BannerAdapter extends PagerAdapter {
    private ArrayList<ImageView> datas;
    private Context context;

    public BannerAdapter(Context context, ArrayList<ImageView> bannerImageDates) {
        this.context = context;
        this.datas = bannerImageDates;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int index = position%datas.size();
        ViewParent parent = datas.get(index).getParent();
        if (parent!=null){
            container.removeView(datas.get(index));
        }
        ((ViewPager)container).addView(datas.get(index));



        return datas.get(index);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager)container).removeView(datas.get(position%datas.size()));
    }
}
