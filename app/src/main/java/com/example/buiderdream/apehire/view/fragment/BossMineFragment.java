package com.example.buiderdream.apehire.view.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buiderdream.apehire.R;
import com.example.buiderdream.apehire.adapter.BannerAdapter;
import com.example.buiderdream.apehire.base.BaseFragment;
import com.example.buiderdream.apehire.constants.ConstantUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by bingling_li on 2016/12/4.
 * 公司的个人中心
 */

public class BossMineFragment extends BaseFragment implements View.OnClickListener,View.OnTouchListener, ViewPager.OnPageChangeListener {
    private View bossMineFragmentView;
    private ViewPager vp_picture;   //图片轮播
    private TextView tv_vp_title;   //图片轮播的简介
    private LinearLayout ll_Point;
    private BannerAdapter adapter;   //图片轮播adapter
    private ArrayList<ImageView> bannerImageDates;   //图片轮播的图片
    private int currentIndex = 300;   //图片下标
    private long lastTime;           //上一次图片滚动时间
    private Activity activity;
    private BossMineHandler handler;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (bossMineFragmentView == null) {
            bossMineFragmentView = inflater.inflate(R.layout.fragment_boss_mine, container, false);
        }
        if (bossMineFragmentView != null) {
            activity = getActivity();
            handler = new BossMineHandler();
            return bossMineFragmentView;
        }
        return inflater.inflate(R.layout.fragment_boss_mine, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        updataBanner();
    }

    private void initView() {
        vp_picture = (ViewPager) bossMineFragmentView.findViewById(R.id.vp_picture);
        tv_vp_title = (TextView) bossMineFragmentView.findViewById(R.id.tv_vp_title);
        ll_Point = (LinearLayout) bossMineFragmentView.findViewById(R.id.ll_Point);
    }

    @Override
    public void onDestroyView() {
        ViewGroup parent = (ViewGroup) bossMineFragmentView.getParent();
        if (parent != null) {
            parent.removeView(bossMineFragmentView);
        }
        super.onDestroyView();
    }


    /**
     * 更新图片轮播
     */
    private void updataBanner() {
        bannerImageDates = new ArrayList<>();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.icon_loading_64px)
                .showImageOnFail(R.mipmap.icon_error_64px)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        for (int i = 0; i < 5; i++) {
            ImageView img = new ImageView(activity);
            img.setImageResource(R.drawable.splash0);
            img.setScaleType(ImageView.ScaleType.FIT_XY);
            bannerImageDates.add(img);
        }
        adapter = new BannerAdapter(activity, bannerImageDates);
        vp_picture.setOnTouchListener(this);
        vp_picture.setAdapter(adapter);
        vp_picture.setCurrentItem(300);
        vp_picture.addOnPageChangeListener(this);
        handler.postDelayed(runnableForBanner, 2000);
        addPoint();
        vp_picture.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                monitorPoint(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    // 设置轮播时间间隔
    private Runnable runnableForBanner = new Runnable() {
        @Override
        public void run() {
            if (System.currentTimeMillis() - lastTime >= 3000) {
                currentIndex++;
                vp_picture.setCurrentItem(currentIndex);
                lastTime = System.currentTimeMillis();
            }
            handler.postDelayed(runnableForBanner, 3000);
        }
    };

    /**
     * 添加小圆点
     */
    private void addPoint() {
        // 1.根据图片多少，添加多少小圆点
        ll_Point.removeAllViews();

        for (int i = 0; i < 5; i++) {
            LinearLayout.LayoutParams pointParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if (i < 1) {
                pointParams.setMargins(0, 0, 0, 0);
            } else {
                pointParams.setMargins(10, 0, 0, 0);
            }
            ImageView iv = new ImageView(activity);
            iv.setLayoutParams(pointParams);
            iv.setBackgroundResource(R.drawable.point_normal);
            ll_Point.addView(iv);
        }
        ll_Point.getChildAt(0).setBackgroundResource(R.drawable.point_select);
    }

    /**
     * 判断小圆点
     *
     * @param position
     */
    private void monitorPoint(int position) {
        int current = (position - 300) % 5;
        for (int i = 0; i < 5; i++) {
            if (i == current) {
                ll_Point.getChildAt(current).setBackgroundResource(
                        R.drawable.point_select);
            } else {
                ll_Point.getChildAt(i).setBackgroundResource(
                        R.drawable.point_normal);
            }
        }
    }

    class BossMineHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                default:
                    break;
            }
        }
    }
    //------------------------------接口回调--------------------------------------------
    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(final int position) {
        currentIndex = position;
        lastTime = System.currentTimeMillis();
        //设置轮播文字改变
        final int index = position % bannerImageDates.size();
        bannerImageDates.get(index).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity,position+"",Toast.LENGTH_SHORT).show();

            }
        });
        tv_vp_title.setText("ssssssssssssssssssss");
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}
