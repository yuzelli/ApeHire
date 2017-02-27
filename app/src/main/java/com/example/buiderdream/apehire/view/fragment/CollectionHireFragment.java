package com.example.buiderdream.apehire.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.buiderdream.apehire.R;
import com.example.buiderdream.apehire.base.BaseFragment;
import com.example.buiderdream.apehire.bean.CompanyInfo;
import com.example.buiderdream.apehire.bean.JobAndCompany;
import com.example.buiderdream.apehire.bean.UserInfo;
import com.example.buiderdream.apehire.constants.ConstantUtils;
import com.example.buiderdream.apehire.https.OkHttpClientManager;
import com.example.buiderdream.apehire.utils.CommonAdapter;
import com.example.buiderdream.apehire.utils.GsonUtils;
import com.example.buiderdream.apehire.utils.SharePreferencesUtil;
import com.example.buiderdream.apehire.utils.ViewHolder;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

/**
 * Created by binglong_li on 2017/2/12.
 * 收藏的公司
 */

public class CollectionHireFragment extends BaseFragment{
    private View collectionHireFragmentView;
    private ListView lv_hire;
    private List<JobAndCompany> jobList;
    private CommonAdapter<JobAndCompany> adapter;
    private Context context;
    private UserInfo userInfo;
    private CollectionHireFragHandler handler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (collectionHireFragmentView==null){
            collectionHireFragmentView = inflater.inflate(R.layout.fragment_coll_hire,container,false);
        }
        if (collectionHireFragmentView!=null){
            return collectionHireFragmentView;
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        jobList = new ArrayList<>();
        handler = new CollectionHireFragHandler();
        context = getActivity();
        userInfo = (UserInfo) SharePreferencesUtil.readObject(getActivity(),ConstantUtils.USER_LOGIN_INFO);
        initView();
        getUserCollectionHire();
    }

    /**
     * 获取职位信息
     */
    private void getUserCollectionHire() {
        OkHttpClientManager manager = OkHttpClientManager.getInstance();
        Map<String, String> map = new HashMap<>();
        map.put("type", "findJobCollByUserID");
        map.put("UserInfoId", userInfo.getUserId()+"");


        String url = OkHttpClientManager.attachHttpGetParams(ConstantUtils.USER_ADDRESS + ConstantUtils.JOB_COLLECTION_SERVLET, map);
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
                    jobList = GsonUtils.jsonToArrayList(object.getString("object"),JobAndCompany.class);
                    handler.sendEmptyMessage(ConstantUtils.COLLECTION_HIRE_GET_DATA);
                }else {
                    Toast.makeText(context, "请求数据失败！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initView() {
        lv_hire = (ListView) collectionHireFragmentView.findViewById(R.id.lv_hire);
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
                if (item.getCompany().getCompanyHeadImg()!=null&&!item.getCompany().getCompanyHeadImg().equals("")) {
                    helper.setImageByUrl2(R.id.job_item_img, item.getCompany().getCompanyHeadImg());
                }
                helper.setText(R.id.job_item_jobName, item.getJobName());
                helper.setText(R.id.job_item_companyAddress, citys.get(item.getJobCity()));
                helper.setText(R.id.job_item_companyName, item.getCompany().getCompanyName());
                helper.setText(R.id.job_item_jobCharge, jobCharges.get(item.getJobCharge()));
            }
        };
        lv_hire.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        ViewGroup parent = (ViewGroup) collectionHireFragmentView.getParent();
        if (parent!=null){
            parent.removeView(collectionHireFragmentView);
        }
        super.onDestroyView();
    }

    class CollectionHireFragHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ConstantUtils.COLLECTION_HIRE_GET_DATA:
                    updataListView();
                    break;
                default:
                    break;
            }
        }
    }
}
