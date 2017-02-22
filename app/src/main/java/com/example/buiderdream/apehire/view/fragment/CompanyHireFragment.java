package com.example.buiderdream.apehire.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.buiderdream.apehire.R;
import com.example.buiderdream.apehire.base.BaseFragment;

/**
 * Created by 51644 on 2017/2/13.
 * 公司发布的职位
 */

public class CompanyHireFragment extends BaseFragment {
    private View compangyHireFragmentView;
    private ListView lv_hire;
    private Button btn_releaseHire;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (compangyHireFragmentView == null) {
            compangyHireFragmentView = inflater.inflate(R.layout.fragment_company_hire, container, false);
        }
        if (compangyHireFragmentView != null) {
            return compangyHireFragmentView;
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        lv_hire= (ListView) compangyHireFragmentView.findViewById(R.id.lv_hire);
        btn_releaseHire = (Button) compangyHireFragmentView.findViewById(R.id.btn_releaseHire);
    }

    @Override
    public void onDestroyView() {
        ViewGroup parent = (ViewGroup) compangyHireFragmentView.getParent();
        if (parent!=null){
            parent.removeView(compangyHireFragmentView);
        }
        super.onDestroyView();
    }
}
