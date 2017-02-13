package com.example.buiderdream.apehire.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.astuetz.PagerSlidingTabStrip;
import com.example.buiderdream.apehire.R;
import com.example.buiderdream.apehire.adapter.MineFragmentAdapter;
import com.example.buiderdream.apehire.base.BaseFragment;
import com.example.buiderdream.apehire.view.activitys.UserEditActivity;
import com.example.buiderdream.apehire.widgets.RoundImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by binglong_li on 2016/12/4.
 * 用户个人中心
 */

public class UserMineFragment extends BaseFragment implements View.OnClickListener {
    private View userMineFragmentView;
    private RoundImageView img_userHeader;   //用户头像
    private TextView tv_userName;   //用户名
    private TextView tv_education;   //用户学历
    private TextView tv_edit;   //编辑
    private PagerSlidingTabStrip psts_tab;
    private ViewPager vp_fragment;

    private Activity activity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        if (userMineFragmentView==null){
            userMineFragmentView = inflater.inflate(R.layout.fragment_user_mine, container,false);
        }

        if (userMineFragmentView!=null){
            activity =getActivity();
            return userMineFragmentView;
        }

        return inflater.inflate(R.layout.fragment_user_mine, null);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (view!=null){
            initView();
        }
    }
    /**
     *   初始化布局
     */
    private void initView() {
        img_userHeader = (RoundImageView) userMineFragmentView.findViewById(R.id.img_userHeader);
        tv_userName  = (TextView) userMineFragmentView.findViewById(R.id.tv_userName);
        tv_education  = (TextView) userMineFragmentView.findViewById(R.id.tv_education);
        tv_edit  = (TextView) userMineFragmentView.findViewById(R.id.tv_edit);
        psts_tab = (PagerSlidingTabStrip) userMineFragmentView.findViewById(R.id.psts_tab);
        vp_fragment = (ViewPager) userMineFragmentView.findViewById(R.id.vp_fragment);
        tv_edit.setOnClickListener(this);
        initCollFragment();
    }

    /**
     * 出事化收藏fragmnet
     */
    private void initCollFragment() {
        List<String> titleList = new ArrayList();
        titleList.add(getResources().getString(R.string.userCollTitle_Hire));
        titleList.add(getResources().getString(R.string.userCollTitle_Technology));
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new CollectionHireFragment());
        fragmentList.add(new CollectionTechnologyFragment());
        MineFragmentAdapter adapter = new MineFragmentAdapter(getChildFragmentManager(),titleList,fragmentList);
        vp_fragment.setAdapter(adapter);
        psts_tab.setViewPager(vp_fragment);
    }

    @Override
    public void onDestroy() {
        ViewGroup parent = (ViewGroup) userMineFragmentView.getParent();
        if (parent!=null){
            //移除当前视图，防止重复加载相同视图使得程序闪退
            parent.removeView(userMineFragmentView);
        }
        super.onDestroy();
    }

    //----------------------------接口回掉-------------------------------------------

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_edit:
                UserEditActivity.actionStart(activity);
                break;
        }
    }
}
