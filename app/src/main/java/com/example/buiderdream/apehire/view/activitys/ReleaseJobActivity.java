package com.example.buiderdream.apehire.view.activitys;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buiderdream.apehire.R;
import com.example.buiderdream.apehire.base.BaseActivity;
import com.example.buiderdream.apehire.bean.CompanyInfo;
import com.example.buiderdream.apehire.bean.JobAndCompany;
import com.example.buiderdream.apehire.constants.ConstantUtils;
import com.example.buiderdream.apehire.bean.JobInfo;
import com.example.buiderdream.apehire.https.OkHttpClientManager;
import com.example.buiderdream.apehire.utils.SharePreferencesUtil;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

/**
 * 发布公司职位信息
 *
 * @author
 */
public class ReleaseJobActivity extends BaseActivity implements View.OnClickListener {


    private RelativeLayout rl_jobName;   //工作名称
    private RelativeLayout rl_jobDetail;   //工作描述
    private RelativeLayout rl_jobTechnology;  //技能要求

    private ImageView img_back;  //后退
    private TextView tv_companyName;   //公司名称
    private TextView tv_jobName;  //工作名
    private TextView tv_jobDetail;    //工作描述
    private TextView tv_jobTechnology;//技能要求
    private Button btn_upload;//发布职位
    private AppCompatSpinner spinner_city;  //城市
    private AppCompatSpinner spinner_jobType;  //工作类型
    private AppCompatSpinner spinner_jobCharge;  // 薪资水平

    private JobInfo job;
    private CompanyInfo company;
    private ReleaseJobHandler handler;
    private Context context;
    private boolean isUpDataJob = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_job);
        context = this;
        handler = new ReleaseJobHandler();
        company = (CompanyInfo) SharePreferencesUtil.readObject(this, ConstantUtils.USER_LOGIN_INFO);
        initView();
        updataView();

        judgeType();
    }

    /**
     * 判断是发布还是修改
     */
    private void judgeType() {
        Intent intent = getIntent();
        JobAndCompany jobInfo = (JobAndCompany) intent.getSerializableExtra("jobInfo");
        if (jobInfo!=null){
            job.setJobId(jobInfo.getJobId());
            job.setCompanyId(jobInfo.getCompany().getCompanyId());
            job.setJobCharge(jobInfo.getJobCharge());
            job.setJobCity(jobInfo.getJobCity())
            ;
            job.setJobDetail(jobInfo.getJobDetail());
            job.setJobName(jobInfo.getJobName());
            job.setJobTechnology(jobInfo.getJobTechnology());
            job.setJobType(jobInfo.getJobType());
            isUpDataJob = false;
            initDataView();
        }else {
            isUpDataJob = true;
            btn_upload.setText("发布职位");
        }
    }

    /**
     * 修改job信息，需加载job数据
     */
    private void initDataView() {
        tv_companyName.setText(company.getCompanyName());
        tv_jobName.setText(job.getJobName());
        spinner_city.setSelection(job.getJobCity());
        spinner_jobType.setSelection(job.getJobType());
        spinner_jobCharge.setSelection(job.getJobCharge());
        tv_jobDetail.setText(job.getJobDetail());
        tv_jobTechnology.setText(job.getJobTechnology());
        btn_upload.setText("修改职位");
    }

    /**
     * 初始化
     */
    private void initView() {
        rl_jobName = (RelativeLayout) this.findViewById(R.id.rl_jobName);
        rl_jobDetail = (RelativeLayout) this.findViewById(R.id.rl_jobDetail);
        rl_jobTechnology = (RelativeLayout) this.findViewById(R.id.rl_jobTechnology);
        img_back = (ImageView) this.findViewById(R.id.img_back);
        tv_companyName = (TextView) this.findViewById(R.id.tv_companyName);
        tv_jobName = (TextView) this.findViewById(R.id.tv_jobName);
        tv_jobDetail = (TextView) this.findViewById(R.id.tv_jobDetail);
        tv_jobTechnology = (TextView) this.findViewById(R.id.tv_jobTechnology);
        spinner_city = (AppCompatSpinner) this.findViewById(R.id.spinner_city);
        spinner_jobType = (AppCompatSpinner) this.findViewById(R.id.spinner_jobType);
        spinner_jobCharge = (AppCompatSpinner) this.findViewById(R.id.spinner_jobCharge);
        btn_upload = (Button) this.findViewById(R.id.btn_upload);

        rl_jobName.setOnClickListener(this);
        rl_jobDetail.setOnClickListener(this);
        rl_jobTechnology.setOnClickListener(this);
        img_back.setOnClickListener(this);
        btn_upload.setOnClickListener(this);

       job = new JobInfo();
    }

    /**
     * 更新视图
     */
    private void updataView() {
        tv_companyName.setText(company.getCompanyName());
        List<String> citys = new ArrayList<>(Arrays.asList("不限", "北京", "上海", "广州", "深圳", "武汉", "杭州", "成都", "西安"));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, citys);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_city.setAdapter(adapter);
        spinner_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                job.setJobCity(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        List<String> jobTypes = new ArrayList<>(Arrays.asList("不限", "软件研发工程师", "java研发工程师", "嵌入式研发工程师", "Unity3D工程师", "Linux工程师"));
        ArrayAdapter<String> jobTypesAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, jobTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_jobType.setAdapter(jobTypesAdapter);

        spinner_jobType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                job.setJobType(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        List<String> jobCharges = new ArrayList<>(Arrays.asList("不限", "3k-5k", "5k-10k", "10k-15k", "15k-20k", "20k-30k", "30k-50k"));
        ArrayAdapter<String> jobChargeAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, jobCharges);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_jobCharge.setAdapter(jobChargeAdapter);

        spinner_jobCharge.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                job.setJobCharge(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    public static void actionStart(Context context,JobAndCompany job) {
        Intent intent = new Intent(context, ReleaseJobActivity.class);
        intent.putExtra("jobInfo",job);
        context.startActivity(intent);
    }

    /**
     * 在InputInfoAC中修改职位信息
     *
     * @param i 5：职位名称；9职位介绍；10技能要求
     */
    private void updateJobInfo(int i) {
        Intent intent = new Intent(ReleaseJobActivity.this, InputInfoActivity.class);
        intent.putExtra("editType", i);
        startActivityForResult(intent, ConstantUtils.EDIT_USER_INFO_ACTIVITY_CODE);
    }

    /**
     * 发布职位
     */
    private void releaseJob() {
        OkHttpClientManager manager = OkHttpClientManager.getInstance();
        Map<String, String> map = new HashMap<>();
        map.put("type", "addJob");
        map.put("jobrname", tv_jobName.getText().toString().trim());
        map.put("jobdetails", tv_jobDetail.getText().toString().trim());
        map.put("jobtype", job.getJobType() + "");
        map.put("city", job.getJobCity() + "");
        map.put("jobsalary", job.getJobCharge() + "");
        map.put("companyname", company.getCompanyId() + "");
        map.put("jobskill", tv_jobTechnology.getText().toString().trim());
        String url = OkHttpClientManager.attachHttpGetParams(ConstantUtils.USER_ADDRESS + ConstantUtils.JOB_SERVLET, map);
        manager.getAsync(url, new OkHttpClientManager.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                Toast.makeText(context, "请求失败！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void requestSuccess(String result) throws Exception {
                JSONObject object = new JSONObject(result);
                String flag = object.getString("error");
                if (flag.equals("ok")) {
                    handler.sendEmptyMessage(ConstantUtils.RELEASE_JOB__GET_DATA);
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.rl_jobName:
                updateJobInfo(5);
                break;
            case R.id.rl_jobDetail:
                updateJobInfo(9);
                break;
            case R.id.rl_jobTechnology:
                updateJobInfo(10);
                break;
            case R.id.btn_upload:
                if(isUpDataJob) {
                    releaseJob();
                }else {
                    updataJob();
                }
                break;

        }
    }

    /**
     * 修改职位信息
     */
    private void updataJob() {
        OkHttpClientManager manager = OkHttpClientManager.getInstance();
        Map<String, String> map = new HashMap<>();
        map.put("type", "jobUpdate");
        map.put("jobid", job.getJobId()+"");
        map.put("jobrname", tv_jobName.getText().toString().trim());
        map.put("jobdetails", tv_jobDetail.getText().toString().trim());
        map.put("jobtype", job.getJobType() + "");
        map.put("city", job.getJobCity() + "");
        map.put("jobsalary", job.getJobCharge() + "");
        map.put("companyname", company.getCompanyId() + "");
        map.put("jobskill", tv_jobTechnology.getText().toString().trim());
        String url = OkHttpClientManager.attachHttpGetParams(ConstantUtils.USER_ADDRESS + ConstantUtils.JOB_SERVLET, map);
        manager.getAsync(url, new OkHttpClientManager.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                Toast.makeText(context, "请求失败！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void requestSuccess(String result) throws Exception {
                JSONObject object = new JSONObject(result);
                String flag = object.getString("error");
                if (flag.equals("ok")) {
                    handler.sendEmptyMessage(ConstantUtils.RELEASE_JOB__GET_DATA);
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ConstantUtils.EDIT_USER_INFO_ACTIVITY_CODE && resultCode == ConstantUtils.EDIT_USER_INFO_ACTIVITY_RESULT_CODE) {
            String result = data.getStringExtra("result");
            int editType = data.getIntExtra("editType", -1);
            if (editType != -1) {
                switch (editType) {
                    case 5:
                        tv_jobName.setText(result);
                        break;
                    case 9:
                        tv_jobDetail.setText(result);
                        break;
                    case 10:
                        tv_jobTechnology.setText(result);
                        break;
                    default:
                        break;
                }
            } else {
                Toast.makeText(context, "修改失败！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class ReleaseJobHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ConstantUtils.RELEASE_JOB__GET_DATA:
                    finish();
                    break;
                default:
                    break;
            }
        }
    }
}
