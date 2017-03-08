package com.example.buiderdream.apehire.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buiderdream.apehire.R;
import com.example.buiderdream.apehire.base.BaseFragment;
import com.example.buiderdream.apehire.bean.JobAndCompany;
import com.example.buiderdream.apehire.bean.JobInfo;
import com.example.buiderdream.apehire.bean.UserInfo;
import com.example.buiderdream.apehire.constants.ConstantUtils;
import com.example.buiderdream.apehire.https.OkHttpClientManager;
import com.example.buiderdream.apehire.utils.CommonAdapter;
import com.example.buiderdream.apehire.utils.ViewHolder;
import com.example.buiderdream.apehire.view.activitys.JobActivity;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

/**
 * Created by Administrator on 2016/12/4.
 */

public class HireFragment extends BaseFragment implements View.OnClickListener {
    private View hireFragmentView;
    TextView job_city, job_type, job_charge, cityMark;
    LinearLayout jobSelectLayout;
    ListView jobListView;
    List<JobAndCompany> jobInfolist;
    CommonAdapter<JobAndCompany> jobInfoAdapter;
    PopupWindow jobCityWindow, jobTypeWindow, jobChargeWindow;
    View jobCityView, jobTypeView, jobChargeView;
    String type_city = "", type_job = "", type_charge = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (hireFragmentView == null) {
            hireFragmentView = inflater.inflate(R.layout.fragment_hire, container, false);
        }
        if (hireFragmentView != null) {
            return hireFragmentView;
        }
        return inflater.inflate(R.layout.fragment_hire, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (view != null) {
            initView();
        }
    }

    /**
     * 初始化布局
     */
    private void initView() {
        job_city = (TextView) hireFragmentView.findViewById(R.id.job_city);
        job_type = (TextView) hireFragmentView.findViewById(R.id.job_type);
        job_charge = (TextView) hireFragmentView.findViewById(R.id.job_charge);
        cityMark = (TextView) hireFragmentView.findViewById(R.id.cityMark);
        jobListView = (ListView) hireFragmentView.findViewById(R.id.jobListView);
        job_city.setOnClickListener(this);
        job_type.setOnClickListener(this);
        job_charge.setOnClickListener(this);
        jobSelectLayout = (LinearLayout) hireFragmentView.findViewById(R.id.jobSelectLayout);
        jobListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), JobActivity.class);
                intent.putExtra("jobInfo", jobInfolist.get(i));
                intent.putExtra("isSend",false);
                startActivity(intent);

            }
        });
        //加载职位数据
        initData();
        initPopWindow();
    }

    /*
        初始化popWindow
     */
    private void initPopWindow() {
        jobCityView = LayoutInflater.from(getActivity()).inflate(R.layout.popup_job_city, null);
        jobTypeView = LayoutInflater.from(getActivity()).inflate(R.layout.popup_job_type, null);
        jobChargeView = LayoutInflater.from(getActivity()).inflate(R.layout.popup_job_charge, null);
        jobCityWindow = new PopupWindow(jobCityView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        jobTypeWindow = new PopupWindow(jobTypeView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        jobChargeWindow = new PopupWindow(jobChargeView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        //城市筛选
        GridView cityGridView = (GridView) jobCityView.findViewById(R.id.job_city_grid);
        String[] citys = {"不限", "北京", "上海", "广州", "深圳", "武汉", "杭州", "成都", "西安"};
        final List<String> list = new ArrayList<String>();
        for (String s : citys) {
            list.add(s);
        }
        CommonAdapter<String> adapter = new CommonAdapter<String>(getActivity(), list, R.layout.pop_item) {
            @Override
            public void convert(ViewHolder helper, String item) {
                helper.setText(R.id.pop_item, item);
            }
        };
        cityGridView.setAdapter(adapter);
        cityGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                type_city ="";
                type_job="";
                type_charge="";
                if (i == 0) {
                    type_city = "";
                } else {
                    type_city = i + "";
                }
                doSearchJobBy();
                if (i != 0) {
                    cityMark.setVisibility(View.VISIBLE);
                    cityMark.setText(list.get(i));
                } else {
                    cityMark.setVisibility(View.GONE);
                }
                Toast.makeText(getActivity(), i + "", Toast.LENGTH_SHORT).show();
                if (jobCityWindow.isShowing()) {
                    jobCityWindow.dismiss();
                }
            }
        });
        //职位类别筛选
        ListView type_listView = (ListView) jobTypeView.findViewById(R.id.jd_type_list);
        String[] typeLists = {"不限", "软件研发工程师", "java研发工程师", "嵌入式研发工程师", "Unity3D工程师", "Linux工程师"};
        List<String> typeList = new ArrayList<String>();
        for (String s : typeLists) {
            typeList.add(s);
        }
        CommonAdapter<String> type_adapter = new CommonAdapter<String>(getActivity(), typeList, R.layout.pop_item) {
            @Override
            public void convert(ViewHolder helper, String item) {
                helper.setText(R.id.pop_item, item);
            }
        };
        type_listView.setAdapter(type_adapter);
        type_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                type_city ="";
                type_job="";
                type_charge="";
                if (i == 0) {
                    type_job = "";
                } else {
                    type_job = i + "";
                }
                doSearchJobBy();
                Toast.makeText(getActivity(), i + "", Toast.LENGTH_SHORT).show();
                if (jobTypeWindow.isShowing()) {
                    jobTypeWindow.dismiss();
                }
            }
        });
        //薪资筛选
        ListView charge_listView = (ListView) jobChargeView.findViewById(R.id.jd_charge_list);
        String[] chargeLists = {"不限", "3k-5k", "5k-10k", "10k-15k", "15k-20k", "20k-30k", "30k-50k"};
        List<String> chargeList = new ArrayList<String>();
        for (String s : chargeLists) {
            chargeList.add(s);
        }
        CommonAdapter<String> charge_adapter = new CommonAdapter<String>(getActivity(), chargeList, R.layout.pop_item) {
            @Override
            public void convert(ViewHolder helper, String item) {
                helper.setText(R.id.pop_item, item);
            }
        };
        charge_listView.setAdapter(charge_adapter);
        charge_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                type_city ="";
                type_job="";
                type_charge="";
                if (i == 0) {
                    type_charge = "";
                } else {
                    type_charge = i + "";
                }
                doSearchJobBy();
                Toast.makeText(getActivity(), i + "", Toast.LENGTH_SHORT).show();
                if (jobChargeWindow.isShowing()) {
                    jobChargeWindow.dismiss();
                }
            }
        });
    }

    //按照筛选结果查找职位
    private void doSearchJobBy() {
        jobInfolist = new ArrayList<JobAndCompany>();
        OkHttpClientManager manager = OkHttpClientManager.getInstance();
        Map<String, String> map = new HashMap<>();
        map.put("type", "selSomeJob");
//        if (type_city!=""||type_city.length()!=0)
        map.put("JobCity", type_city);
//        if (type_job!=""||type_job.length()!=0)
        map.put("JobType", type_job);
//        if (type_charge!=""||type_charge.length()!=0)
        map.put("JobCharge", type_charge);
        map.put("CompanyId", "");
//        String urls = "http://172.20.10.2:8080/ApeHire/jobServlet";
        String url = manager.attachHttpGetParams(ConstantUtils.USER_ADDRESS + ConstantUtils.JOB_SERVLET, map);
        manager.getAsync(url, new OkHttpClientManager.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                Toast.makeText(getActivity(), "失败", Toast.LENGTH_SHORT).show();
                // Log.i("asdasdasdadsas",e.getMessage());
            }

            @Override
            public void requestSuccess(String result) throws Exception {
                Gson gson = new Gson();
                JSONObject object = new JSONObject(result);
                String flag = object.getString("error");
                if (flag.equals("ok")) {
                    JSONArray array = object.getJSONArray("object");
                    Log.d("----->",result);

                    for (int i = 0; i < array.length(); i++) {
                        JobAndCompany jac = gson.fromJson(array.getJSONObject(i).toString(), JobAndCompany.class);
                        jobInfolist.add(jac);

                    }
                    initAdapter();
                }
            }
        });

    }

    private void initData() {
        jobInfolist = new ArrayList<>();

//        JobAndCompany j1 = new JobAndCompany();
//        j1.setJobName("假数据1");
//        j1.setJobCharge(3);
//        j1.setJobCity(2);
//        JobAndCompany.CompanyBean comp = new JobAndCompany.CompanyBean();
//        comp.setCompanyName("连不上服务器啊");
//        comp.setCompanyAddress("又想看界面啊");
//        comp.setCompanyIntroduce("好可怜啊啊啊啊啊啊啊好可怜啊啊啊啊啊啊啊好可怜啊啊啊啊啊啊啊好可怜啊啊啊啊啊啊啊好可怜啊啊啊啊啊啊啊好可怜啊啊啊啊啊啊啊好可怜啊啊啊啊啊啊啊好可怜啊啊啊啊啊啊啊");
//        comp.setCompanyScale(2);
//        j1.setCompany(comp);
//        jobInfolist.add(j1);
//        jobInfolist.add(j1);


        doSearchJobBy();
        initAdapter();
    }

    private void initAdapter() {
        jobInfoAdapter = new CommonAdapter<JobAndCompany>(getActivity(), jobInfolist, R.layout.fragment_hire_item) {
            @Override
            public void convert(ViewHolder helper, JobAndCompany item) {
                helper.setText(R.id.job_item_jobName, item.getJobName());
                helper.setText(R.id.job_item_jobCharge, item.getJobCharge() > 2 ? (item.getJobCharge() > 3 ? (item.getJobCharge() > 4 ? "15K+" : "8K~15K") : "5K~8k") : "5K以下");
                helper.setText(R.id.job_item_companyName, item.getCompany().getCompanyName());
                helper.setText(R.id.job_item_companyAddress, item.getCompany().getCompanyAddress());
                if (item.getCompany().getCompanyHeadImg() != null || item.getCompany().getCompanyHeadImg() != "")
                    helper.setImageByUrl(R.id.job_item_img, item.getCompany().getCompanyHeadImg());
            }
        };
        jobListView.setAdapter(jobInfoAdapter);
        // jobInfoAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        ViewGroup parent = (ViewGroup) hireFragmentView.getParent();
        if (parent != null) {
            parent.removeView(hireFragmentView);
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.job_city:
                showJobPop(jobCityWindow);
                break;
            case R.id.job_type:
                showJobPop(jobTypeWindow);
                break;
            case R.id.job_charge:
                showJobPop(jobChargeWindow);
                break;
            default:
                break;
        }
    }


    private void showJobPop(PopupWindow popWindow) {

        if (!popWindow.isShowing()) {
            //在底部显示
            popWindow.showAsDropDown(jobSelectLayout);
            //popupWindow.showAtLocation(titleLayout, Gravity.BOTTOM, 0, 0);
            //popupWindow.setAnimationStyle(R.style.mySesrchPopAnim);
            popWindow.setOutsideTouchable(true);
            popWindow.setTouchable(true);
            popWindow.setFocusable(true);

            jobCityView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (jobCityWindow.isShowing()) {
                        jobCityWindow.dismiss();
                    }
                    return false;
                }
            });
            jobTypeView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (jobTypeWindow.isShowing()) {
                        jobTypeWindow.dismiss();
                    }
                    return false;
                }
            });
            jobChargeView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (jobChargeWindow.isShowing()) {
                        jobChargeWindow.dismiss();
                    }
                    return false;
                }
            });
        } else {
            popWindow.dismiss();
        }
    }
}


