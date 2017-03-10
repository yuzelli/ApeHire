package com.example.buiderdream.apehire.view.activitys;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buiderdream.apehire.R;
import com.example.buiderdream.apehire.base.BaseActivity;
import com.example.buiderdream.apehire.bean.JobAndCompany;
import com.example.buiderdream.apehire.bean.UserInfo;
import com.example.buiderdream.apehire.constants.ConstantUtils;
import com.example.buiderdream.apehire.https.OkHttpClientManager;
import com.example.buiderdream.apehire.utils.ImageLoader;
import com.example.buiderdream.apehire.utils.JudgeUtils;
import com.example.buiderdream.apehire.utils.SharePreferencesUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import android.os.Handler;

import okhttp3.Request;

/**
 * 职位详情页面
 * @李文捷
 */
public class JobActivity extends BaseActivity implements View.OnClickListener{
    private TextView jd_title,jd_company_name,jd_company_name2,jd_company_address,jd_company_address2,jd_company_scale,jd_company_detail,jd_detail,jd_charge,jd_technology;
    private ImageView job_company_img;
    private  JobAndCompany jobInfo;//职位详情
    private TextView sendJobReq;//职位投递
    private TextView job_like_click;//职位收藏
    private UserInfo userInfo;
    private  ImageLoader imageLoader;
    private RelativeLayout companyPart;
    private boolean userType;   //用户类型
    private boolean isSend;
    private final String[] citys = {"不限", "北京", "上海", "广州", "深圳", "武汉", "杭州", "成都", "西安"};
    private final  String[] chargeLists = {"不限", "3k-5k", "5k-10k", "10k-15k", "15k-20k", "20k-30k", "30k-50k"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job);
        initView();
        imageLoader = ImageLoader.getInstance();
        userType =JudgeUtils.getUserType(getApplication());
        if (userType) {
            userInfo = (UserInfo) SharePreferencesUtil.readObject(JobActivity.this, ConstantUtils.USER_LOGIN_INFO);
            job_like_click.setVisibility(View.VISIBLE);
            sendJobReq.setVisibility(View.VISIBLE);
        }else {
            job_like_click.setVisibility(View.GONE);
            sendJobReq.setVisibility(View.GONE);
        }
        Intent intent = getIntent();
        jobInfo = (JobAndCompany) intent.getSerializableExtra("jobInfo");
        isSend  =  intent.getBooleanExtra("isSend",false);
        if (isSend){
            sendJobReq.setVisibility(View.GONE);
        }else {
            sendJobReq.setVisibility(View.VISIBLE);
        }
        initData();
    }

    private void initData() {
        //职位详情赋值
        jd_title.setText(jobInfo.getJobName());
        jd_detail.setText(jobInfo.getJobDetail());
        jd_technology.setText(jobInfo.getJobTechnology());
        jd_charge.setText(jobInfo.getJobCharge()>2?(jobInfo.getJobCharge()>3?(jobInfo.getJobCharge()>4?"15K+":"8K~15K"):"5K~8k"):"5K以下");
        //公司详情赋值
        jd_company_name.setText(jobInfo.getCompany().getCompanyName());
        jd_company_name2.setText(jobInfo.getCompany().getCompanyName());
        jd_company_address.setText(citys[jobInfo.getJobCity()]);
        jd_company_address2.setText(jobInfo.getCompany().getCompanyAddress());
        jd_company_detail.setText(jobInfo.getCompany().getCompanyIntroduce());

        if(jobInfo.getCompany().getCompanyHeadImg()!=null||jobInfo.getCompany().getCompanyHeadImg()!=""){
            imageLoader.loadImage(jobInfo.getCompany().getCompanyHeadImg(),job_company_img);
        }
        int scale = jobInfo.getCompany().getCompanyScale();
        jd_company_scale.setText(chargeLists[scale]);

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_loading)
                .showImageOnFail(R.mipmap.ic_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(jobInfo.getCompany().getCompanyHeadImg(), job_company_img, options);
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
        job_company_img = (ImageView) findViewById(R.id.jd_company_img);
        sendJobReq = (TextView) findViewById(R.id.sendJobReq);
        sendJobReq.setOnClickListener(this);
        job_like_click = (TextView) findViewById(R.id.job_like_click);
        job_like_click.setOnClickListener(this);
        companyPart = (RelativeLayout) findViewById(R.id.job_detail_part4);
        companyPart.setOnClickListener(this);
    }
    public void backClick(View v){
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.sendJobReq:
                doSendJobReq();
                break;
            case R.id.job_like_click:
                doLikeJobReq();
                break;
            case R.id.job_detail_part4:
                if (!userType){
                    break;
                }
                Intent intent = new Intent(JobActivity.this, CompanyInfoActivity.class);
                intent.putExtra("jobInfo",jobInfo);
                startActivity(intent);
            default:
                break;
        }
    }



    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                {
                    sendJobReq.setText("已投递");
                    sendJobReq.setClickable(false);
                }
                    break;
                case 2:
                {

                    job_like_click.setClickable(false);
                }
                    break;
                default:
                    break;
            }

        }
    };
    /*
        收藏职位
     */
    private void doLikeJobReq() {
        OkHttpClientManager manager = OkHttpClientManager.getInstance();
        Map<String,String> map = new HashMap<>();
        map.put("type", "addJobColl");
        map.put("UserInfoId",userInfo.getUserId()+"");
        map.put("jobID",jobInfo.getJobId()+"");

        String url = manager.attachHttpGetParams(ConstantUtils.USER_ADDRESS+ConstantUtils.JOB_COLLECTION_SERVLET,map);
        manager.getAsync(url, new OkHttpClientManager.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                Toast.makeText(JobActivity.this, "收藏职位失败", Toast.LENGTH_SHORT).show();
                Log.i("收藏职位失败",e.getMessage());
            }

            @Override
            public void requestSuccess(String result) throws Exception {
                handler.sendEmptyMessage(2);
            }
        });
    }
    /*
        投递职位
     */
    private void doSendJobReq() {
        OkHttpClientManager manager = OkHttpClientManager.getInstance();
        Map<String,String> map = new HashMap<>();
        map.put("type", "addCompanyResume");
        map.put("jobId",jobInfo.getJobId()+"");
        map.put("userId",userInfo.getUserId()+"");
        map.put("companyId",jobInfo.getCompany().getCompanyId()+"");

        String url = manager.attachHttpGetParams(ConstantUtils.USER_ADDRESS+ConstantUtils.COMPANY_RESUM_SERVLET,map);
        manager.getAsync(url, new OkHttpClientManager.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                Toast.makeText(JobActivity.this, "投递职位失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void requestSuccess(String result) throws Exception {
                handler.sendEmptyMessage(1);
            }
        });
    }
}
