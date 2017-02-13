package com.example.buiderdream.apehire.view.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.example.buiderdream.apehire.R;
import com.example.buiderdream.apehire.adapter.BannerAdapter;
import com.example.buiderdream.apehire.adapter.MineFragmentAdapter;
import com.example.buiderdream.apehire.base.BaseFragment;
import com.example.buiderdream.apehire.utils.DensityUtils;
import com.example.buiderdream.apehire.view.activitys.BossEditActivity;
import com.example.buiderdream.apehire.view.activitys.UserEditActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bingling_li on 2016/12/4.
 * 公司的个人中心
 */

public class BossMineFragment extends BaseFragment implements View.OnClickListener,View.OnTouchListener, ViewPager.OnPageChangeListener {
    private View bossMineFragmentView;
    private ViewPager vp_picture;   //图片轮播
    private LinearLayout ll_Point;   //图片轮播下标点
    private TextView tv_edit;
    private BannerAdapter adapter;   //图片轮播adapter
    private ArrayList<ImageView> bannerImageDates;   //图片轮播的图片
    private int currentIndex = 300;   //图片下标
    private long lastTime;           //上一次图片滚动时间
    private Activity activity;
    private BossMineHandler handler;
    private PagerSlidingTabStrip psts_tab;   //选项卡
    private ViewPager vp_fragment;           //选项卡对应的Fragment

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
        tv_edit = (TextView) bossMineFragmentView.findViewById(R.id.tv_edit);
        ll_Point = (LinearLayout) bossMineFragmentView.findViewById(R.id.ll_Point);
        psts_tab = (PagerSlidingTabStrip) bossMineFragmentView.findViewById(R.id.psts_tab);
        vp_fragment  = (ViewPager) bossMineFragmentView.findViewById(R.id.vp_fragment);
        tv_edit.setOnClickListener(this);
        initBossFragment();
    }

    /**
     * 初始化页面布局
     */
    private void initBossFragment() {
        List<String> titleList = new ArrayList();
        titleList.add(getResources().getString(R.string.companyDescribe));
        titleList.add(getResources().getString(R.string.companyHire));
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new CompanyDescribeFragment());
        fragmentList.add(new CompanyHireFragment());
        MineFragmentAdapter adapter = new MineFragmentAdapter(getChildFragmentManager(),titleList,fragmentList);
        vp_fragment.setAdapter(adapter);
        psts_tab.setViewPager(vp_fragment);
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
                .showImageOnLoading(R.mipmap.ic_loading)
                .showImageOnFail(R.mipmap.ic_loading)
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
                    DensityUtils.dp2px(activity,10), DensityUtils.dp2px(activity,10));
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
            case R.id.tv_edit:
                BossEditActivity.actionStart(activity);
                break;
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
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}
