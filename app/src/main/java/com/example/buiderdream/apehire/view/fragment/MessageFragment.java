package com.example.buiderdream.apehire.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.example.buiderdream.apehire.R;
import com.example.buiderdream.apehire.adapter.MineFragmentAdapter;
import com.example.buiderdream.apehire.base.BaseFragment;
import com.example.buiderdream.apehire.utils.JudgeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/4.
 */

public class MessageFragment extends BaseFragment {
    private View messageFragmentView;
    private PagerSlidingTabStrip psts_tab;   //选项卡
    private ViewPager vp_fragment;           //选项卡对应的Fragment
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (messageFragmentView == null) {
            messageFragmentView = inflater.inflate(R.layout.fragment_message, container, false);
        }
        if (messageFragmentView != null) {
            return messageFragmentView;
        }
        return inflater.inflate(R.layout.fragment_message, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (view != null) {
            context = getActivity();
            initView();
        }
    }

    /**
     * 初始化布局
     */
    private void initView() {
        psts_tab = (PagerSlidingTabStrip) messageFragmentView.findViewById(R.id.psts_tab);
        vp_fragment = (ViewPager) messageFragmentView.findViewById(R.id.vp_fragment);
        if (!JudgeUtils.getUserType(context)) {
            initUserFragment();
        } else {
            initBossFragment();
        }

    }

    private void initBossFragment() {
        List<String> titleList = new ArrayList();
        titleList.add(getResources().getString(R.string.push));
        titleList.add(getResources().getString(R.string.userjob));
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new PushFragment());
        fragmentList.add(new UserJobFragment());
        MineFragmentAdapter adapter = new MineFragmentAdapter(getChildFragmentManager(), titleList, fragmentList);
        vp_fragment.setAdapter(adapter);
        psts_tab.setViewPager(vp_fragment);
    }

    private void initUserFragment() {
        List<String> titleList = new ArrayList();
        titleList.add(getResources().getString(R.string.push));
        titleList.add(getResources().getString(R.string.getResume));
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new PushFragment());
        fragmentList.add(new BossGetResumeFragment());
        MineFragmentAdapter adapter = new MineFragmentAdapter(getChildFragmentManager(), titleList, fragmentList);
        vp_fragment.setAdapter(adapter);
        psts_tab.setViewPager(vp_fragment);
    }

    @Override
    public void onDestroy() {
        ViewGroup parent = (ViewGroup) messageFragmentView.getParent();
        if (parent != null) {
            parent.removeView(messageFragmentView);
        }
        super.onDestroy();
    }
}
