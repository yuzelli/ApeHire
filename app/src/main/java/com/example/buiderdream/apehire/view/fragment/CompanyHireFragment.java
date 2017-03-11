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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.example.buiderdream.apehire.R;
import com.example.buiderdream.apehire.base.BaseFragment;
import com.example.buiderdream.apehire.bean.CompanyInfo;
import com.example.buiderdream.apehire.bean.JobAndCompany;
import com.example.buiderdream.apehire.constants.ConstantUtils;
import com.example.buiderdream.apehire.https.OkHttpClientManager;
import com.example.buiderdream.apehire.utils.ACache;
import com.example.buiderdream.apehire.utils.CommonAdapter;
import com.example.buiderdream.apehire.utils.GsonUtils;
import com.example.buiderdream.apehire.utils.NetworkUtils;
import com.example.buiderdream.apehire.utils.SharePreferencesUtil;
import com.example.buiderdream.apehire.utils.ViewHolder;
import com.example.buiderdream.apehire.view.activitys.JobActivity;
import com.example.buiderdream.apehire.view.activitys.MainActivity;
import com.example.buiderdream.apehire.view.activitys.ReleaseJobActivity;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

/**
 * Created by 51644 on 2017/2/13.
 * 公司发布的职位
 */

public class CompanyHireFragment extends BaseFragment implements View.OnClickListener ,SwipeRefreshLayout.OnRefreshListener{
    private View compangyHireFragmentView;
    private ListView lv_hire;
    private Button btn_releaseHire;
    private List<JobAndCompany> jobList;
    private CommonAdapter<JobAndCompany> adapter;
    private Context context;
    private CompanyInfo company;
    private CompanyHireFragHandler handler;
    private SwipeRefreshLayout srl_fresh;
    private ProgressBar pb_loading;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (compangyHireFragmentView == null) {
            compangyHireFragmentView = inflater.inflate(R.layout.fragment_company_hire, container, false);
        }
        if (compangyHireFragmentView != null) {
            return compangyHireFragmentView;
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
        company = (CompanyInfo) SharePreferencesUtil.readObject(context,ConstantUtils.USER_LOGIN_INFO);
        handler = new CompanyHireFragHandler();
        initView();
        updataListView();

    }



    private void initView() {
        lv_hire = (ListView) compangyHireFragmentView.findViewById(R.id.lv_hire);
        btn_releaseHire = (Button) compangyHireFragmentView.findViewById(R.id.btn_releaseHire);
        btn_releaseHire.setOnClickListener(this);
        String result = ACache.get(context).getAsString(company.getCompanyNum()+ConstantUtils.COMPANY_HIRE_FRAGMENT_ACACHE);
        if (result!=null&&!result.equals("")) {
            jobList = GsonUtils.jsonToArrayList(result,JobAndCompany.class);
        }else {
            jobList = new ArrayList<>();
        }
        srl_fresh = (SwipeRefreshLayout) compangyHireFragmentView.findViewById(R.id.srl_fresh);
        pb_loading = (ProgressBar) compangyHireFragmentView.findViewById(R.id.pb_loading);
        srl_fresh.setColorSchemeColors(  getResources().getColor(android.R.color.holo_red_light),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_blue_bright)
        );
        srl_fresh.setOnRefreshListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (NetworkUtils.isNetAvailable(context)) {
            getCompanyJob();
        }

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
                if (item.getCompany()!=null&&item.getCompany().getCompanyHeadImg()!=null&&!item.getCompany().getCompanyHeadImg().equals("")) {
                    helper.setImageByUrl2(R.id.job_item_img, item.getCompany().getCompanyHeadImg());
                }
                helper.setText(R.id.job_item_jobName, item.getJobName());
                helper.setText(R.id.job_item_companyAddress, citys.get(item.getJobCity()));
                helper.setText(R.id.job_item_companyName, item.getCompany().getCompanyName());
                helper.setText(R.id.job_item_jobCharge, jobCharges.get(item.getJobCharge()));
            }
        };
        lv_hire.setAdapter(adapter);
        lv_hire.setEmptyView(compangyHireFragmentView.findViewById(R.id.img_emptyView));
        lv_hire.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ReleaseJobActivity.actionStart(getActivity(),jobList.get(position));
            }
        });
        lv_hire.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ShowDeleteDialog(jobList.get(position).getJobId());

                return true;
            }
        });
    }

    /**
     * show Delete Dialog
     */
    private void ShowDeleteDialog(final int jobID) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);// 构建
        builder.setTitle("提示框");
        builder.setMessage("你确定要删除么");
        // 添加确定按钮 listener事件是继承与DialogInerface的
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                // 完成业务逻辑代码
                DeleteCollectionHire(jobID);
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

    private void DeleteCollectionHire(int jobID) {
        OkHttpClientManager manager = OkHttpClientManager.getInstance();
        Map<String, String> map = new HashMap<>();
        map.put("type", "delJob");
        map.put("ids",jobID+"");

        String url = OkHttpClientManager.attachHttpGetParams(ConstantUtils.USER_ADDRESS + ConstantUtils.JOB_SERVLET, map);
        manager.getAsync(url, new OkHttpClientManager.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                Toast.makeText(context, "请求失败！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void requestSuccess(String result) throws Exception {
                JSONObject object = new JSONObject(result);
                boolean flag = object.getBoolean("flag");
                if (flag==true) {
                    handler.sendEmptyMessage(ConstantUtils.COMPANY_HIRE_DELET_DATA);
                }
            }
        });
    }
    /**
     * 获取发布的职位
     */
    private void getCompanyJob() {

        OkHttpClientManager manager = OkHttpClientManager.getInstance();
        Map<String, String> map = new HashMap<>();
        map.put("type", "selSomeJob");
        map.put("JobType", "");
        map.put("JobCharge", "");
        map.put("JobCity", "");
        map.put("CompanyId",company.getCompanyId()+"" );

        String url = OkHttpClientManager.attachHttpGetParams(ConstantUtils.USER_ADDRESS + ConstantUtils.JOB_SERVLET, map);
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
                    jobList = GsonUtils.jsonToArrayList(object.getString("object"),JobAndCompany.class);
                    ACache aCache = ACache.get(context);
                    aCache.put(company.getCompanyNum()+ConstantUtils.COMPANY_HIRE_FRAGMENT_ACACHE,object.getString("object"));

                    handler.sendEmptyMessage(ConstantUtils.COMPANYHIRE_FRAGMENT_GET_DATA);
                }else {
                    Toast.makeText(context, "请求数据失败！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void onDestroyView() {
        ViewGroup parent = (ViewGroup) compangyHireFragmentView.getParent();
        if (parent != null) {
            parent.removeView(compangyHireFragmentView);
        }
        super.onDestroyView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_releaseHire:
                ReleaseJobActivity.actionStart(getActivity(),null);
                break;

            default:
                break;
        }
    }

    @Override
    public void onRefresh() {
        getCompanyJob();
    }

    class CompanyHireFragHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ConstantUtils.COMPANYHIRE_FRAGMENT_GET_DATA:
                    updataListView();
                    break;
                case ConstantUtils.COMPANY_HIRE_DELET_DATA:
                    getCompanyJob();
                    break;
                default:
                    break;
            }
        }
    }
}
