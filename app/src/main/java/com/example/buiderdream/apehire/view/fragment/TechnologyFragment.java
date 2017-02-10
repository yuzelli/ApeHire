package com.example.buiderdream.apehire.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.buiderdream.apehire.R;

/**
 * Created by Administrator on 2016/12/4.
 */

public class TechnologyFragment extends Fragment {
    private View technologyFragmentView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        if (technologyFragmentView==null){
            technologyFragmentView = inflater.inflate(R.layout.fragment_hire, container,false);
        }
        if (technologyFragmentView!=null){
            return technologyFragmentView;
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
        ViewGroup parent = (ViewGroup) technologyFragmentView.getParent();
        if (parent!=null){
            parent.removeView(technologyFragmentView);
        }
        super.onDestroy();
    }
}
