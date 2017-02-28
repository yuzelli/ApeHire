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

public class PushFragment extends BaseFragment {
    private View pushFragmentView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (pushFragmentView == null) {
            pushFragmentView = inflater.inflate(R.layout.fragment_push, container, false);
        }
        if (pushFragmentView != null) {
            return pushFragmentView;
        }
        return inflater.inflate(R.layout.fragment_push, null);
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
        ViewGroup parent = (ViewGroup) pushFragmentView.getParent();
        if (parent != null) {
            parent.removeView(pushFragmentView);
        }
        super.onDestroyView();
    }

}
