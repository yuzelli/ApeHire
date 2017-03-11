package com.example.buiderdream.apehire.view.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.buiderdream.apehire.R;
import com.example.buiderdream.apehire.base.BaseFragment;
import com.example.buiderdream.apehire.bean.JobAndCompany;
import com.example.buiderdream.apehire.bean.UserCompJob;
import com.example.buiderdream.apehire.bean.UserInfo;
import com.example.buiderdream.apehire.constants.ConstantUtils;
import com.example.buiderdream.apehire.bean.JobInfo;
import com.example.buiderdream.apehire.https.OkHttpClientManager;
import com.example.buiderdream.apehire.utils.ACache;
import com.example.buiderdream.apehire.utils.CommonAdapter;
import com.example.buiderdream.apehire.utils.GsonUtils;
import com.example.buiderdream.apehire.utils.SharePreferencesUtil;
import com.example.buiderdream.apehire.utils.ViewHolder;
import com.example.buiderdream.apehire.view.activitys.JobActivity;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

/**
 * Created by 51644 on 2017/2/28.
 * 投递的职位
 */

public class UserJobFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{
    private View userJobFragmentView;
    private ListView lv_jobInfo;
    private CommonAdapter<UserCompJob> adapter;
    private List<UserCompJob> jobInfoList;
    private UserJobFragmentHandler handler;
    private UserInfo userInfo;
    private Context context;
    private SwipeRefreshLayout srl_fresh;
    private ProgressBar pb_loading;
    private final List<String> scaleList = new ArrayList<>(Arrays.asList("0-20", "20-99", "100-499", "500-999", "1000+"));
    private final List<String> jobTypes = new ArrayList<>(Arrays.asList("不限", "软件研发工程师", "java研发工程师", "嵌入式研发工程师", "Unity3D工程师", "Linux工程师"));
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (userJobFragmentView == null) {
            userJobFragmentView = inflater.inflate(R.layout.fragment_userjob, container, false);
        }
        if (userJobFragmentView != null) {
            return userJobFragmentView;
        }
        return inflater.inflate(R.layout.fragment_userjob, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

            handler = new UserJobFragmentHandler();
            context = getActivity();
            userInfo = (UserInfo) SharePreferencesUtil.readObject(context, ConstantUtils.USER_LOGIN_INFO);
            String result = ACache.get(context).getAsString(userInfo.getUserPhoneNum()+ConstantUtils.USER_JOB_FRAGMENT_ACACHE);
            if (result!=null&&!result.equals("")) {
                jobInfoList = GsonUtils.jsonToArrayList(result,UserCompJob.class);
            }else {
                jobInfoList = new ArrayList<>();
            }
            initView();

        updataListView();
        getUserJobData();
    }

    private void initView() {
        lv_jobInfo = (ListView) userJobFragmentView.findViewById(R.id.lv_jobInfo);

        srl_fresh = (SwipeRefreshLayout) userJobFragmentView.findViewById(R.id.srl_fresh);
        pb_loading = (ProgressBar) userJobFragmentView.findViewById(R.id.pb_loading);
        srl_fresh.setColorSchemeColors(  getResources().getColor(android.R.color.holo_red_light),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_blue_bright)
        );
        srl_fresh.setOnRefreshListener(this);
    }
    /**
     * 更新listView视图
     */
    private void updataListView() {
        adapter = new CommonAdapter<UserCompJob>(context, jobInfoList, R.layout.item_user_job_list) {
            @Override
            public void convert(ViewHolder helper, UserCompJob item) {
                helper.setImageByUrl2(R.id.img_companyHeader,item.getCompany().getCompanyHeadImg());
                helper.setText(R.id.tv_companyName,item.getCompany().getCompanyName());
                helper.setText(R.id.tv_address,item.getCompany().getCompanyAddress());
                helper.setText(R.id.tv_scale,scaleList.get(item.getCompany().getCompanyScale()));
                helper.setText(R.id.tv_jobName,item.getJob().getJobName());
                helper.setText(R.id.tv_jobType,jobTypes.get(item.getJob().getJobType()));
            }
        };
        lv_jobInfo.setAdapter(adapter);
        lv_jobInfo.setEmptyView(userJobFragmentView.findViewById(R.id.img_emptyView));
        lv_jobInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, JobActivity.class);
                UserCompJob.JobBean jobBean = jobInfoList.get(position).getJob();
                UserCompJob.CompanyBean companyBean = jobInfoList.get(position).getCompany();
                JobAndCompany job = new JobAndCompany();
                JobAndCompany.CompanyBean comp = new JobAndCompany.CompanyBean();
                comp.setCompanyHeadImg(companyBean.getCompanyHeadImg());
                comp.setCompanyAddress(companyBean.getCompanyAddress());
                comp.setCompanyId(companyBean.getCompanyId());
                comp.setCompanyIntroduce(companyBean.getCompanyIntroduce());
                comp.setCompanyName(companyBean.getCompanyName());
                comp.setCompanyNum(companyBean.getCompanyNum());
                comp.setCompanyPassword(companyBean.getCompanyPassword());
                comp.setCompanyScale(companyBean.getCompanyScale());
                job.setCompany(comp);
                job.setJobCharge(jobBean.getJobCharge());
                job.setJobCity(jobBean.getJobCity());
                job.setJobDetail(jobBean.getJobDetail());
                job.setJobId(jobBean.getJobId());
                job.setJobName(jobBean.getJobName());
                job.setJobTechnology(jobBean.getJobTechnology());
                job.setJobType(jobBean.getJobType());
                intent.putExtra("jobInfo",job);
                intent.putExtra("isSend",true);
                startActivity(intent);
            }
        });

        lv_jobInfo.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ShowDeleteDialog(jobInfoList.get(position).getResume_id());
                return true;
            }
        });
    }
    /**
     * show Delete Dialog
     */
    private void ShowDeleteDialog(final int resumeID) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);// 构建
        builder.setTitle("提示框");
        builder.setMessage("你确定要删除么");
        // 添加确定按钮 listener事件是继承与DialogInerface的
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                // 完成业务逻辑代码
                DeleteResume(resumeID);
            }
        });



        // 添加取消按钮
        builder.setNegativeButton("取消删除",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // TODO Auto-generated method stub
                    }
                });
        builder.show();
    }

    /**
     * 删除
     */
    private void DeleteResume(int resumeID) {
        OkHttpClientManager manager = OkHttpClientManager.getInstance();
        Map<String, String> map = new HashMap<>();
        map.put("type", "deleteCompanyResume");
        map.put("resume_id", resumeID + "");

        String url = OkHttpClientManager.attachHttpGetParams(ConstantUtils.USER_ADDRESS + ConstantUtils.COMPANY_RESUM_SERVLET, map);
        Log.d("-----url----->",url);
        manager.getAsync(url, new OkHttpClientManager.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {

                Toast.makeText(context, "请求失败！", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void requestSuccess(String result) throws Exception {

                handler.sendEmptyMessage(ConstantUtils.BOSS_GET_DELECT);

            }
        });
    }

    /**
     * 得到数据
     */
    private void getUserJobData() {
        OkHttpClientManager manager = OkHttpClientManager.getInstance();
        Map<String, String> map = new HashMap<>();
        map.put("type", "findCompanyResumeByUserID");
        map.put("userId", userInfo.getUserId() + "");

        String url = OkHttpClientManager.attachHttpGetParams(ConstantUtils.USER_ADDRESS + ConstantUtils.COMPANY_RESUM_SERVLET, map);
        manager.getAsync(url, new OkHttpClientManager.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                Toast.makeText(context, "请求失败！", Toast.LENGTH_SHORT).show();
                srl_fresh.setRefreshing(false);
                pb_loading.setVisibility(View.GONE);
            }

            @Override
            public void requestSuccess(String result) throws Exception {
                srl_fresh.setRefreshing(false);
                pb_loading.setVisibility(View.GONE);
                JSONObject object = new JSONObject(result);
                String flag = object.getString("error");
                if (flag.equals("ok")) {
                    jobInfoList = GsonUtils.jsonToArrayList(object.getString("object"), UserCompJob.class);
                    ACache aCache = ACache.get(context);
                    aCache.put(userInfo.getUserPhoneNum()+ConstantUtils.USER_JOB_FRAGMENT_ACACHE, object.getString("object"));
                    handler.sendEmptyMessage(ConstantUtils.USER_JOB_GET_DATA);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        ViewGroup parent = (ViewGroup) userJobFragmentView.getParent();
        if (parent != null) {
            parent.removeView(userJobFragmentView);
        }
        super.onDestroyView();
    }

    @Override
    public void onRefresh() {
        getUserJobData();
    }

    class UserJobFragmentHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ConstantUtils.USER_JOB_GET_DATA:
                    updataListView();
                    break;
                case ConstantUtils.BOSS_GET_DELECT:
                    getUserJobData();
                    break;
                default:
                    break;
            }
        }
    }
}
