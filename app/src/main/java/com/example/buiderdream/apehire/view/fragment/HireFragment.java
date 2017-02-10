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

public class HireFragment extends BaseFragment {
    private View hireFragmentView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        if (hireFragmentView==null){
            hireFragmentView = inflater.inflate(R.layout.fragment_hire, container,false);
        }
        if (hireFragmentView!=null){
           return hireFragmentView;
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
        ViewGroup parent = (ViewGroup) hireFragmentView.getParent();
        if (parent!=null){
            parent.removeView(hireFragmentView);
        }
        super.onDestroy();
    }
}


