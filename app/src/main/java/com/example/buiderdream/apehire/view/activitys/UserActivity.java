package com.example.buiderdream.apehire.view.activitys;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.buiderdream.apehire.R;
import com.example.buiderdream.apehire.base.BaseActivity;
import com.example.buiderdream.apehire.bean.UserCompJob;
import com.example.buiderdream.apehire.bean.JobInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserActivity extends BaseActivity implements View.OnClickListener {
    private ImageView img_back;  //后退
    private RelativeLayout rl_job;  //职位
    private TextView tv_jobName;  //职位名称
    private TextView tv_monney;   // 职位待遇
    private TextView tv_jobType;   //职位类别
    private TextView tv_jobAddress;   //职位地址

    private ImageView img_userHeader;  //求职者头像
    private TextView tv_userName;  //求职者姓名
    private TextView tv_sex;  //求职者性别
    private TextView tv_school;  //求职者学校
    private TextView tv_degree;  //求职者学历
    private TextView tv_expactMoney;  //求职者期望薪资
    private TextView tv_userExperence;  //求职者期望经验
    private TextView tv_userAdvantage;  //求职者期望优势

    private TextView tv_chat;  // 聊天
    private Context context;

    private UserCompJob userCompJob;
    private final List<String> jobCharges = new ArrayList<>(Arrays.asList("不限", "3k-5k", "5k-10k", "10k-15k", "15k-20k", "20k-30k", "30k-50k"));
    private final   List<String> jobTypes = new ArrayList<>(Arrays.asList("不限", "软件研发工程师", "java研发工程师", "嵌入式研发工程师", "Unity3D工程师", "Linux工程师"));
    private final  List<String> citys = new ArrayList<>(Arrays.asList("不限", "北京", "上海", "广州", "深圳", "武汉", "杭州", "成都", "西安"));
    private final List<String> educationList = new ArrayList<>(Arrays.asList("大专","本科","硕士","博士"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        context= this;
        Intent intent = getIntent();
        userCompJob = (UserCompJob) intent.getSerializableExtra("userCompJob");
        initView();
        updataView();
    }


    private void initView() {
        rl_job = (RelativeLayout) this.findViewById(R.id.rl_job);
        tv_jobName = (TextView) this.findViewById(R.id.tv_jobName);
        tv_monney = (TextView) this.findViewById(R.id.tv_monney);
        tv_jobType = (TextView) this.findViewById(R.id.tv_jobType);
        tv_jobAddress = (TextView) this.findViewById(R.id.tv_jobAddress);
        tv_chat = (TextView) this.findViewById(R.id.tv_chat);

        img_userHeader = (ImageView) this.findViewById(R.id.img_userHeader);
        img_back = (ImageView) this.findViewById(R.id.img_back);
        tv_userName = (TextView) this.findViewById(R.id.tv_userName);
        tv_sex = (TextView) this.findViewById(R.id.tv_sex);
        tv_school = (TextView) this.findViewById(R.id.tv_school);
        tv_degree = (TextView) this.findViewById(R.id.tv_degree);
        tv_expactMoney = (TextView) this.findViewById(R.id.tv_expactMoney);
        tv_userExperence = (TextView) this.findViewById(R.id.tv_userExperence);
        tv_userAdvantage = (TextView) this.findViewById(R.id.tv_userAdvantage);
        tv_chat.setOnClickListener(this);
        rl_job.setOnClickListener(this);
        img_back.setOnClickListener(this);
    }

    /**
     *更新视图
     */
    private void updataView() {
        tv_jobName.setText(userCompJob.getJob().getJobName());
        tv_monney.setText(jobCharges.get(userCompJob.getJob().getJobCharge()));
        tv_jobType.setText(jobTypes.get(userCompJob.getJob().getJobType()));
        tv_jobAddress.setText(citys.get(userCompJob.getJob().getJobCity()));

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_loading)
                .showImageOnFail(R.mipmap.ic_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoader.getInstance().displayImage(userCompJob.getUserInfo().getUserHeadImg(), img_userHeader, options);
        tv_userName.setText(userCompJob.getUserInfo().getUserTrueName());
        tv_sex.setText(userCompJob.getUserInfo().getUserGender());
        tv_degree.setText(educationList.get(userCompJob.getUserInfo().getUserDegree()));
        tv_expactMoney.setText(jobCharges.get(userCompJob.getUserInfo().getUserExpactMonney()));
        tv_userExperence.setText(userCompJob.getUserInfo().getUserExperence());
        tv_userAdvantage.setText(userCompJob.getUserInfo().getUserAdvantage());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_chat:
                ChatActivity.actionStart(UserActivity.this,userCompJob.getUserInfo().getUserPhoneNum());
                break;
            case R.id.rl_job:
                Intent intent = new Intent(context, JobActivity.class);
                UserCompJob.JobBean jobBean = userCompJob.getJob();
                JobInfo job = new JobInfo(jobBean.getJobId(),jobBean.getJobName(),jobBean.getJobDetail(),jobBean.getJobType(),jobBean.getJobCity(),jobBean.getJobCharge(),jobBean.getCompanyId(),jobBean.getJobTechnology());
                intent.putExtra("jobInfo",job);
                startActivity(intent);
                break;
            default:
                break;

        }
    }

    public static void actionStart(Context context,UserCompJob ucj) {
        Intent intent = new Intent(context, UserActivity.class);
        intent.putExtra("userCompJob",ucj);
        context.startActivity(intent);
    }

}
