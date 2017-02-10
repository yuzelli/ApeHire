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

public class MessageFragment extends Fragment {
    private View messageFragmentView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        if (messageFragmentView==null){
            messageFragmentView = inflater.inflate(R.layout.fragment_hire, container,false);
        }
        if (messageFragmentView!=null){
            return messageFragmentView;
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
        ViewGroup parent = (ViewGroup) messageFragmentView.getParent();
        if (parent!=null){
            parent.removeView(messageFragmentView);
        }
        super.onDestroy();
    }
}
