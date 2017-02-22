package com.example.buiderdream.apehire.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.buiderdream.apehire.R;
import com.example.buiderdream.apehire.base.BaseFragment;

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
        tv_describe.setText("神马是专注移动互联网的搜索引擎，致力于为用户创造方便、快捷、开放的移动搜索新体验。神马是一支创业团队，由全球用户量最大的移动浏览器UC优视与中国互联网行业领军企业阿里巴巴共同发起组建，并由来自微软、谷歌、百度、360等国内外IT公司的资深员工所组成，我们坚信移动互联网一定能够让搜索更智慧，让生活更美好。");
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
