package com.example.buiderdream.apehire.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.buiderdream.apehire.R;
import com.example.buiderdream.apehire.base.BaseFragment;
import com.example.buiderdream.apehire.entity.JobInfo;
import com.example.buiderdream.apehire.utils.CommonAdapter;
import com.example.buiderdream.apehire.utils.ViewHolder;
import com.example.buiderdream.apehire.view.activitys.JobActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/4.
 */

public class HireFragment extends BaseFragment implements View.OnClickListener{
    private View hireFragmentView;
    TextView job_city,job_type,job_charge;
    ListView jobListView;
    List<JobInfo> jobInfolist;
    CommonAdapter<JobInfo> jobInfoAdapter;
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
        job_city = (TextView) hireFragmentView.findViewById(R.id.job_city);
        job_type = (TextView) hireFragmentView.findViewById(R.id.job_type);
        job_charge = (TextView) hireFragmentView.findViewById(R.id.job_charge);
        jobListView = (ListView) hireFragmentView.findViewById(R.id.jobListView);
        job_city.setOnClickListener(this);
        job_type.setOnClickListener(this);
        job_charge.setOnClickListener(this);
        jobListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), JobActivity.class);
                intent.putExtra("jobInfo",jobInfolist.get(i));
                startActivity(intent);

            }
        });
        //加载职位数据
        initData();
    }

    private void initData() {
        jobInfolist = new ArrayList<>();

        jobInfolist.add(new JobInfo(1,"Android工程师","负责公司已有Android项目的开发和维护",1,1,2,1,"Android常用框架，熟练使用Git等版本控制工具"));
        jobInfolist.add(new JobInfo(2,"iOS工程师","负责公司已有iOS项目的开发和维护",1,2,3,1,"Android常用框架，熟练使用Git等版本控制工具"));
        jobInfolist.add(new JobInfo(2,"iOS工程师","负责公司已有iOS项目的开发和维护",1,2,4,1,"Android常用框架，熟练使用Git等版本控制工具"));
        jobInfolist.add(new JobInfo(2,"iOS工程师","负责公司已有iOS项目的开发和维护",1,2,1,1,"Android常用框架，熟练使用Git等版本控制工具"));

        jobInfoAdapter = new CommonAdapter<JobInfo>(getActivity(),jobInfolist,R.layout.fragment_hire_item) {
            @Override
            public void convert(ViewHolder helper, JobInfo item) {
                helper.setText(R.id.job_item_jobName,item.getJobName());
                helper.setText(R.id.job_item_jobCharge,item.getJobCharge()>2?(item.getJobCharge()>3?(item.getJobCharge()>4?"15K+":"8K~15K"):"5K~8k"):"5K以下");
            }
        };
        jobListView.setAdapter(jobInfoAdapter);
    }

    @Override
    public void onDestroy() {
        ViewGroup parent = (ViewGroup) hireFragmentView.getParent();
        if (parent!=null){
            parent.removeView(hireFragmentView);
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.job_city:
                break;
            case R.id.job_type:
                break;
            case R.id.job_charge:
                break;
            default:
                break;
        }
    }
}


