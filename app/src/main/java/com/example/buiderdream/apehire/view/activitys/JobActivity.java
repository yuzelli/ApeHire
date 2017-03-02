package com.example.buiderdream.apehire.view.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.buiderdream.apehire.R;
import com.example.buiderdream.apehire.base.BaseActivity;
import com.example.buiderdream.apehire.bean.JobInfo;

/**
 * 职位详情页面
 * @author
 */
public class JobActivity extends BaseActivity {
    TextView jd_title,jd_company_name,jd_company_name2,jd_company_address,jd_company_address2,jd_company_scale,jd_company_detail,jd_detail,jd_charge,jd_technology;
    JobInfo jobInfo;//职位详情
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job);
        initView();
        Intent intent = getIntent();
        jobInfo = (JobInfo) intent.getSerializableExtra("jobInfo");
        initData();
    }

    private void initData() {
        //职位详情赋值
        jd_title.setText(jobInfo.getJobName());
        jd_detail.setText(jobInfo.getJobDetail());
        jd_technology.setText(jobInfo.getJobTechnology());
        jd_charge.setText(jobInfo.getJobCharge()>2?(jobInfo.getJobCharge()>3?(jobInfo.getJobCharge()>4?"15K+":"8K~15K"):"5K~8k"):"5K以下");
        //公司详情赋值
//        jd_company_name.setText();
//        jd_company_name2.setText();
//        jd_company_address.setText();
//        jd_company_address2.setText();
//        jd_company_detail.setText();
//        jd_company_scale.setText();


    }

    private void initView() {
        jd_title = (TextView) findViewById(R.id.jd_title);
        jd_charge = (TextView) findViewById(R.id.jd_charge);
        jd_detail = (TextView) findViewById(R.id.jd_detail);
        jd_technology = (TextView) findViewById(R.id.jd_technology);
        jd_company_name = (TextView) findViewById(R.id.jd_company_name);
        jd_company_name2 = (TextView) findViewById(R.id.jd_company_name2);
        jd_company_address = (TextView) findViewById(R.id.jd_company_address);
        jd_company_address2 = (TextView) findViewById(R.id.jd_company_address2);
        jd_company_scale = (TextView) findViewById(R.id.jd_company_scale);
        jd_company_detail = (TextView) findViewById(R.id.jd_company_detail);



    }
    public void backClick(View v){
        finish();
    }

}
