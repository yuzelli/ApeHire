package com.example.buiderdream.apehire.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.buiderdream.apehire.R;
import com.example.buiderdream.apehire.base.BaseFragment;
import com.example.buiderdream.apehire.bean.CompanyInfo;
import com.example.buiderdream.apehire.bean.JobAndCompany;
import com.example.buiderdream.apehire.bean.UserInfo;
import com.example.buiderdream.apehire.utils.CommonAdapter;
import com.example.buiderdream.apehire.utils.ViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by binglong_li on 2017/2/12.
 * 收藏的公司
 */

public class CollectionHireFragment extends BaseFragment{
    private View collectionHireFragmentView;
    private ListView lv_hire;
    private List<JobAndCompany> jobList;
    private CommonAdapter<JobAndCompany> adapter;
    private Context context;
    private UserInfo userInfo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (collectionHireFragmentView==null){
            collectionHireFragmentView = inflater.inflate(R.layout.fragment_coll_hire,container,false);
        }
        if (collectionHireFragmentView!=null){
            return collectionHireFragmentView;
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        jobList = new ArrayList<>();
        initView();

    }

    private void initView() {
        lv_hire = (ListView) collectionHireFragmentView.findViewById(R.id.lv_hire);
    }
    /**
     * 更新listView
     */
    private void updataListView() {
        final List<String> citys = new ArrayList<>(Arrays.asList("不限", "北京", "上海", "广州", "深圳", "武汉", "杭州", "成都", "西安"));
        final List<String> jobCharges = new ArrayList<>(Arrays.asList("不限", "3k-5k", "5k-10k", "10k-15k", "15k-20k", "20k-30k", "30k-50k"));
        adapter = new CommonAdapter<JobAndCompany>(context, jobList, R.layout.fragment_hire_item) {
            @Override
            public void convert(ViewHolder helper, JobAndCompany item) {
                helper.setImageByUrl2(R.id.job_item_img, item.getCompany().getCompanyHeadImg());
                helper.setText(R.id.job_item_jobName, item.getJobName());
                helper.setText(R.id.job_item_companyAddress, citys.get(item.getJobCity()));
                helper.setText(R.id.job_item_companyName, item.getCompany().getCompanyName());
                helper.setText(R.id.job_item_jobCharge, jobCharges.get(item.getJobCharge()));
            }
        };
        lv_hire.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        ViewGroup parent = (ViewGroup) collectionHireFragmentView.getParent();
        if (parent!=null){
            parent.removeView(collectionHireFragmentView);
        }
        super.onDestroyView();
    }
}
