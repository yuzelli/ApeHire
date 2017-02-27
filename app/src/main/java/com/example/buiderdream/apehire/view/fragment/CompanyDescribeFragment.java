package com.example.buiderdream.apehire.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.buiderdream.apehire.R;
import com.example.buiderdream.apehire.base.BaseFragment;
import com.example.buiderdream.apehire.bean.CompanyInfo;
import com.example.buiderdream.apehire.constants.ConstantUtils;
import com.example.buiderdream.apehire.utils.SharePreferencesUtil;

/**
 * Created by 51644 on 2017/2/13.
 * 公司介绍
 */

public class CompanyDescribeFragment extends BaseFragment {
    private View companyDescribeFragmentView;
    private TextView tv_describe;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (companyDescribeFragmentView==null){
            companyDescribeFragmentView = inflater.inflate(R.layout.fragment_company_describe,container,false);
        }
        if (companyDescribeFragmentView!=null){

            return companyDescribeFragmentView;
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initView();
        updateView();
    }

    /**
     * 初始化布局
     */
    private void initView() {
        tv_describe = (TextView) companyDescribeFragmentView.findViewById(R.id.tv_describe);

    }

    private void updateView() {
        CompanyInfo companyInfo = (CompanyInfo) SharePreferencesUtil.readObject(getActivity(), ConstantUtils.USER_LOGIN_INFO);
        tv_describe.setText(companyInfo.getCompanyIntroduce());
    }

    @Override
    public void onDestroyView() {
        ViewGroup parent = (ViewGroup) companyDescribeFragmentView.getParent();
        if (parent!=null){
            parent.removeView(companyDescribeFragmentView);
        }
        super.onDestroyView();
    }
}
