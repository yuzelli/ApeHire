package com.example.buiderdream.apehire.view.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.buiderdream.apehire.R;
import com.example.buiderdream.apehire.base.BaseFragment;
import com.example.buiderdream.apehire.bean.CompanyInfo;
import com.example.buiderdream.apehire.bean.UserCompJob;
import com.example.buiderdream.apehire.constants.ConstantUtils;
import com.example.buiderdream.apehire.https.OkHttpClientManager;
import com.example.buiderdream.apehire.utils.ACache;
import com.example.buiderdream.apehire.utils.CommonAdapter;
import com.example.buiderdream.apehire.utils.GsonUtils;
import com.example.buiderdream.apehire.utils.SharePreferencesUtil;
import com.example.buiderdream.apehire.utils.ViewHolder;
import com.example.buiderdream.apehire.view.activitys.UserActivity;

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

public class BossGetResumeFragment extends BaseFragment {
    private View bossGetResumeView;
    private ListView lv_userInfo;
    private CommonAdapter<UserCompJob> adapter;
    private List<UserCompJob> userInfoList;
    private BossGetResumeFragmentHandler handler;
    private CompanyInfo company;
    private Context context;
    private final List<String> moneyList = new ArrayList<>(Arrays.asList("不限", "3k-5k", "5k-10k", "10k-15k", "15k-20k", "20k-30k", "30k-50k"));
    private final List<String> educationList = new ArrayList<>(Arrays.asList("大专", "本科", "硕士", "博士"));
    private final List<String> jobTypes = new ArrayList<>(Arrays.asList("不限", "软件研发工程师", "java研发工程师", "嵌入式研发工程师", "Unity3D工程师", "Linux工程师"));

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (bossGetResumeView == null) {
            bossGetResumeView = inflater.inflate(R.layout.fragment_bossgetresume, container, false);
        }
        if (bossGetResumeView != null) {
            return bossGetResumeView;
        }
        return inflater.inflate(R.layout.fragment_bossgetresume, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        handler = new BossGetResumeFragmentHandler();
        context = getActivity();
        company = (CompanyInfo) SharePreferencesUtil.readObject(context, ConstantUtils.USER_LOGIN_INFO);
        String result = ACache.get(context).getAsString(ConstantUtils.BOSS_GET_RESUME_ACACHE);
        if (result != null && !result.equals("")) {
            userInfoList = GsonUtils.jsonToArrayList(result, UserCompJob.class);
        } else {
            userInfoList = new ArrayList<>();
        }
        initView();
        updataListView();
        getBossGetResumeData();
    }


    private void initView() {
        lv_userInfo = (ListView) bossGetResumeView.findViewById(R.id.lv_userInfo);

    }

    /**
     * 更新listView视图
     */
    private void updataListView() {
        adapter = new CommonAdapter<UserCompJob>(context, userInfoList, R.layout.item_boss_get_list) {
            @Override
            public void convert(ViewHolder helper, UserCompJob item) {
                helper.setImageByUrl2(R.id.img_userHeader, item.getUserInfo().getUserHeadImg());
                helper.setText(R.id.tv_userName, item.getUserInfo().getUserTrueName());
                helper.setText(R.id.tv_degree, educationList.get(item.getUserInfo().getUserDegree()));
                helper.setText(R.id.tv_expactMoney, moneyList.get(item.getUserInfo().getUserExpactMonney()));
                helper.setText(R.id.tv_jobName, item.getJob().getJobName());
                helper.setText(R.id.tv_jobType, jobTypes.get(item.getJob().getJobType()));
            }
        };
        lv_userInfo.setAdapter(adapter);
        lv_userInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserActivity.actionStart(context,userInfoList.get(position));
            }
        });
        lv_userInfo.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                ShowDeleteDialog(userInfoList.get(position).getResume_id());

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
                Log.d("-----请求失败----->","请求失败");
                Toast.makeText(context, "请求失败！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void requestSuccess(String result) throws Exception {
                Log.d("-----chenggonh----->",result);
                handler.sendEmptyMessage(ConstantUtils.BOSS_GET_DELECT);

            }
        });
    }

    /**
     * 得到数据
     */
    private void getBossGetResumeData() {
        OkHttpClientManager manager = OkHttpClientManager.getInstance();
        Map<String, String> map = new HashMap<>();
        map.put("type", "findCompanyResumeByCompID");
        map.put("companyId", company.getCompanyId() + "");

        String url = OkHttpClientManager.attachHttpGetParams(ConstantUtils.USER_ADDRESS + ConstantUtils.COMPANY_RESUM_SERVLET, map);
        manager.getAsync(url, new OkHttpClientManager.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                Toast.makeText(context, "请求失败！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void requestSuccess(String result) throws Exception {
                Log.d("-----请求----->",result);
                JSONObject object = new JSONObject(result);
                String flag = object.getString("error");
                if (flag.equals("ok")) {
                    userInfoList = GsonUtils.jsonToArrayList(object.getString("object"), UserCompJob.class);
                    ACache aCache = ACache.get(context);
                    aCache.put(ConstantUtils.BOSS_GET_RESUME_ACACHE, object.getString("object"));
                    handler.sendEmptyMessage(ConstantUtils.BOSS_GET_RESUME_GET_DATA);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        ViewGroup parent = (ViewGroup) bossGetResumeView.getParent();
        if (parent != null) {
            parent.removeView(bossGetResumeView);
        }
        super.onDestroyView();
    }


    class BossGetResumeFragmentHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ConstantUtils.BOSS_GET_RESUME_GET_DATA:
                    updataListView();
                    break;
                case ConstantUtils.BOSS_GET_DELECT:
                    getBossGetResumeData();
                    break;
                default:
                    break;
            }
        }
    }
}
