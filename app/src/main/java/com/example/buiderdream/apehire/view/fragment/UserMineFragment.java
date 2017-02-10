package com.example.buiderdream.apehire.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.buiderdream.apehire.R;
import com.example.buiderdream.apehire.base.BaseFragment;

/**
 * Created by Administrator on 2016/12/4.
 */

public class UserMineFragment extends BaseFragment {
    private View userMineFragmentView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        if (userMineFragmentView==null){
            userMineFragmentView = inflater.inflate(R.layout.fragment_hire, container,false);
        }
        if (userMineFragmentView!=null){
            return userMineFragmentView;
        }
        return inflater.inflate(R.layout.fragment_hire, null);
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
}
