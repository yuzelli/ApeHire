package com.example.buiderdream.apehire.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.buiderdream.apehire.R;
import com.example.buiderdream.apehire.base.BaseFragment;
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
 */

public class UserJobFragment extends BaseFragment {
    private View userJobFragmentView;
    private ListView lv_jobInfo;
    private CommonAdapter<UserCompJob> adapter;
    private List<UserCompJob> jobInfoList;
    private UserJobFragmentHandler handler;
    private UserInfo userInfo;
    private Context context;
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
            String result = ACache.get(context).getAsString(ConstantUtils.USER_JOB_FRAGMENT_ACACHE);
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
        lv_jobInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, JobActivity.class);
                UserCompJob.JobBean jobBean = jobInfoList.get(position).getJob();
                JobInfo job = new JobInfo(jobBean.getJobId(),jobBean.getJobName(),jobBean.getJobDetail(),jobBean.getJobType(),jobBean.getJobCity(),jobBean.getJobCharge(),jobBean.getCompanyId(),jobBean.getJobTechnology());
                intent.putExtra("jobInfo",job);
                startActivity(intent);
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
            }

            @Override
            public void requestSuccess(String result) throws Exception {

                JSONObject object = new JSONObject(result);
                String flag = object.getString("error");
                if (flag.equals("ok")) {
                    jobInfoList = GsonUtils.jsonToArrayList(object.getString("object"), UserCompJob.class);
                    ACache aCache = ACache.get(context);
                    aCache.put(ConstantUtils.USER_JOB_FRAGMENT_ACACHE, object.getString("object"));
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

    class UserJobFragmentHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ConstantUtils.USER_JOB_GET_DATA:
                    updataListView();
                    break;
                default:
                    break;
            }
        }
    }
}
