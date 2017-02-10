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

public class BossMineFragment extends BaseFragment {
    private View bossMineFragmentView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        if (bossMineFragmentView==null){
            bossMineFragmentView = inflater.inflate(R.layout.fragment_boss_mine, container,false);
        }
        if (bossMineFragmentView!=null){
            return bossMineFragmentView;
        }
        return inflater.inflate(R.layout.fragment_boss_mine, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {

    }

    @Override
    public void onDestroyView() {
        ViewGroup parent = (ViewGroup) bossMineFragmentView.getParent();
        if (parent!=null){
            parent.removeView(bossMineFragmentView);
        }
        super.onDestroyView();
    }
}
