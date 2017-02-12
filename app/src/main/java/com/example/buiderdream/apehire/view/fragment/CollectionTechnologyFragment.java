package com.example.buiderdream.apehire.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.buiderdream.apehire.R;
import com.example.buiderdream.apehire.base.BaseFragment;

/**
 * Created by binglong_li on 2017/2/12.
 */

public class CollectionTechnologyFragment extends BaseFragment{
    private View collectionTechnologyFragmentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (collectionTechnologyFragmentView==null){
            collectionTechnologyFragmentView = inflater.inflate(R.layout.fragment_coll_technology,container,false);
        }
        if (collectionTechnologyFragmentView!=null){
            return collectionTechnologyFragmentView;
        }
        return super.onCreateView(inflater, container, savedInstanceState);
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
        ViewGroup parent = (ViewGroup) collectionTechnologyFragmentView.getParent();
        if (parent!=null){
            parent.removeView(collectionTechnologyFragmentView);
        }
        super.onDestroyView();
    }
}
