package com.example.buiderdream.apehire.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.buiderdream.apehire.R;
import com.example.buiderdream.apehire.base.BaseFragment;

/**
 * Created by 51644 on 2017/2/28.
 */

public class BossGetResumeFragment extends BaseFragment {
    private View bossGetResumeView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (bossGetResumeView == null) {
            bossGetResumeView = inflater.inflate(R.layout.fragment_userjob, container, false);
        }
        if (bossGetResumeView != null) {
            return bossGetResumeView;
        }
        return inflater.inflate(R.layout.fragment_userjob, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (view != null) {
            initView();
        }
    }

    private void initView() {
    }

    @Override
    public void onDestroyView() {
        ViewGroup parent = (ViewGroup) bossGetResumeView.getParent();
        if (parent != null) {
            parent.removeView(bossGetResumeView);
        }
        super.onDestroyView();
    }
}
