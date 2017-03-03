package com.example.buiderdream.apehire.view.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buiderdream.apehire.R;
import com.example.buiderdream.apehire.base.BaseActivity;
import com.example.buiderdream.apehire.bean.JobAndCompany;
import com.example.buiderdream.apehire.bean.UserInfo;
import com.example.buiderdream.apehire.constants.ConstantUtils;
import com.example.buiderdream.apehire.https.OkHttpClientManager;
import com.example.buiderdream.apehire.utils.CommonAdapter;
import com.example.buiderdream.apehire.utils.ImageLoader;
import com.example.buiderdream.apehire.utils.SharePreferencesUtil;
import com.example.buiderdream.apehire.utils.ViewHolder;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

/**
 * 公司的详细信息
 * @李文捷
 */
public class CompanyInfoActivity extends BaseActivity {
    TextView jd_company_name2,jd_company_address2,jd_company_scale,jd_company_detail;
    ImageView job_company_img;
    JobAndCompany jobInfo;//职位详情
    ListView allJobOfCompLV;
    List<JobAndCompany> jobInfolist;
    CommonAdapter<JobAndCompany> jobInfoAdapter;
    ImageLoader imageLoader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_info);
        imageLoader = ImageLoader.getInstance();
        Intent intent = getIntent();
        jobInfo = (JobAndCompany) intent.getSerializableExtra("jobInfo");
        initView();
        initAllJobData();
    }

    private void initAllJobData() {
        jobInfolist = new ArrayList<>();

        JobAndCompany j1 = new JobAndCompany();
        j1.setJobName("假数据1");
        j1.setJobCharge(3);
        j1.setJobCity(2);
        JobAndCompany.CompanyBean comp = new JobAndCompany.CompanyBean();
        comp.setCompanyName("连不上服务器啊");
        comp.setCompanyAddress("又想看界面啊");
        comp.setCompanyIntroduce("好可怜啊啊啊啊啊啊啊好可怜啊啊啊啊啊啊啊好可怜啊啊啊啊啊啊啊好可怜啊啊啊啊啊啊啊好可怜啊啊啊啊啊啊啊好可怜啊啊啊啊啊啊啊好可怜啊啊啊啊啊啊啊好可怜啊啊啊啊啊啊啊");
        comp.setCompanyScale(2);
        j1.setCompany(comp);
        jobInfolist.add(j1);

        getAllJobByComp();
        initAdapter();
    }

    private void getAllJobByComp() {
        OkHttpClientManager manager = OkHttpClientManager.getInstance();
        Map<String,String> map = new HashMap<>();
        map.put("type", "selJob");
        map.put("JobCity","");
        map.put("JobType","");
        map.put("JobCharge","");
        map.put("CompanyId", jobInfo.getCompany().getCompanyId()+"");

        String url = manager.attachHttpGetParams(ConstantUtils.USER_ADDRESS+ConstantUtils.JOB_SERVLET,map);
        manager.getAsync(url, new OkHttpClientManager.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                Toast.makeText(CompanyInfoActivity.this, "获取公司发布职位失败", Toast.LENGTH_SHORT).show();
                Log.i("获取公司发布职位失败",e.getMessage());
            }

            @Override
            public void requestSuccess(String result) throws Exception {
                Gson gson = new Gson();
                JSONObject object = new JSONObject(result);
                String flag = object.getString("error");
                if (flag.equals("ok")) {
                    JSONArray array = object.getJSONArray("object");
                    for (int i = 0;i<array.length();i++){
                        JobAndCompany jac = gson.fromJson(array.getJSONObject(i).toString(),JobAndCompany.class);
                        jobInfolist.add(jac);
                    }

                }
            }
        });
    }

    private void initAdapter() {
        jobInfoAdapter = new CommonAdapter<JobAndCompany>(CompanyInfoActivity.this,jobInfolist, R.layout.fragment_hire_item) {
            @Override
            public void convert(ViewHolder helper, JobAndCompany item) {
                helper.setText(R.id.job_item_jobName,item.getJobName());
                helper.setText(R.id.job_item_jobCharge,item.getJobCharge()>2?(item.getJobCharge()>3?(item.getJobCharge()>4?"15K+":"8K~15K"):"5K~8k"):"5K以下");
                helper.setText(R.id.job_item_companyName,item.getCompany().getCompanyName());
                helper.setText(R.id.job_item_companyAddress,item.getCompany().getCompanyAddress());
                helper.setImageByUrl(R.id.job_item_img,item.getCompany().getCompanyHeadImg());
            }
        };
        allJobOfCompLV.setAdapter(jobInfoAdapter);
        jobInfoAdapter.notifyDataSetChanged();
    }

    private void initView() {
        jd_company_name2 = (TextView) findViewById(R.id.jd_company_name2);
        jd_company_address2 = (TextView) findViewById(R.id.jd_company_address2);
        jd_company_scale = (TextView) findViewById(R.id.jd_company_scale);
        jd_company_detail = (TextView) findViewById(R.id.jd_company_detail);
        job_company_img = (ImageView) findViewById(R.id.jd_company_img);
        allJobOfCompLV = (ListView) findViewById(R.id.allJobOfComp);

        //公司详情赋值
        jd_company_name2.setText(jobInfo.getCompany().getCompanyName());
        jd_company_address2.setText(jobInfo.getCompany().getCompanyAddress());
        jd_company_detail.setText(jobInfo.getCompany().getCompanyIntroduce());
//        if(jobInfo.getCompany().getCompanyHeadImg()!=null||jobInfo.getCompany().getCompanyHeadImg()!=""){
//            imageLoader.loadImage(jobInfo.getCompany().getCompanyHeadImg(),job_company_img);
//        }
        int scale = jobInfo.getCompany().getCompanyScale();
        jd_company_scale.setText(scale>2?(scale>3?(scale>4?(scale>5?(scale>6?"上市公司 1000人以上":"C轮 100~1000人"):"B轮 20~99人"):"A轮  10~50人"):"天使轮  0~20人"):"未融资  0~20人");

        allJobOfCompLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(CompanyInfoActivity.this, JobActivity.class);
                intent.putExtra("jobInfo",jobInfolist.get(i));
                startActivity(intent);

            }
        });
    }

    public void backClick(View v){
        finish();
    }
}
