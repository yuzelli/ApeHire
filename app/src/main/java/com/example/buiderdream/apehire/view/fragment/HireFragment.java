package com.example.buiderdream.apehire.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.example.buiderdream.apehire.bean.JobInfo;
import com.example.buiderdream.apehire.utils.CommonAdapter;
import com.example.buiderdream.apehire.utils.ViewHolder;
import com.example.buiderdream.apehire.view.activitys.JobActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/4.
 */

public class HireFragment extends BaseFragment implements View.OnClickListener{
    private View hireFragmentView;
    TextView job_city,job_type,job_charge,cityMark;
    LinearLayout jobSelectLayout;
    ListView jobListView;
    List<JobInfo> jobInfolist;
    CommonAdapter<JobInfo> jobInfoAdapter;
    PopupWindow jobCityWindow,jobTypeWindow,jobChargeWindow;
    View jobCityView,jobTypeView,jobChargeView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        if (hireFragmentView==null){
            hireFragmentView = inflater.inflate(R.layout.fragment_hire, container,false);
        }
        if (hireFragmentView!=null){
           return hireFragmentView;
        }
        return inflater.inflate(R.layout.fragment_hire, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (view!=null){
            initView();
        }
    }
    /**
     *   初始化布局
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
                intent.putExtra("jobInfo",jobInfolist.get(i));
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
        jobCityView = LayoutInflater.from(getActivity()).inflate(R.layout.popup_job_city,null);
        jobTypeView = LayoutInflater.from(getActivity()).inflate(R.layout.popup_job_type,null);
        jobChargeView = LayoutInflater.from(getActivity()).inflate(R.layout.popup_job_charge,null);
        jobCityWindow = new PopupWindow(jobCityView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        jobTypeWindow = new PopupWindow(jobTypeView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        jobChargeWindow = new PopupWindow(jobChargeView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        //城市筛选
        GridView cityGridView = (GridView) jobCityView.findViewById(R.id.job_city_grid);
        String[] citys = {"不限","北京","上海","广州","深圳","武汉","杭州","成都","西安"};
        final List<String> list = new ArrayList<String>();
        for (String s:citys) {
            list.add(s);
        }
        CommonAdapter<String> adapter = new CommonAdapter<String>(getActivity(),list,R.layout.pop_item) {
            @Override
            public void convert(ViewHolder helper, String item) {
                helper.setText(R.id.pop_item,item);
            }
        };
        cityGridView.setAdapter(adapter);
        cityGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //doSearchJobBy(1,i);
                if (i!=0){
                    cityMark.setVisibility(View.VISIBLE);
                    cityMark.setText(list.get(i));
                }else{
                    cityMark.setVisibility(View.GONE);
                }
                Toast.makeText(getActivity(), i+"", Toast.LENGTH_SHORT).show();
                if (jobCityWindow.isShowing()) {
                    jobCityWindow.dismiss();
                }
            }
        });
        //职位类别筛选
        ListView type_listView = (ListView) jobTypeView.findViewById(R.id.jd_type_list);
        String[] typeLists = {"不限","软件研发工程师","java研发工程师","嵌入式研发工程师","Unity3D工程师","Linux工程师"};
        List<String> typeList = new ArrayList<String>();
        for (String s:typeLists) {
            typeList.add(s);
        }
        CommonAdapter<String> type_adapter = new CommonAdapter<String>(getActivity(),typeList,R.layout.pop_item) {
            @Override
            public void convert(ViewHolder helper, String item) {
                helper.setText(R.id.pop_item,item);
            }
        };
        type_listView.setAdapter(type_adapter);
        type_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //doSearchJobBy(2,i);
                Toast.makeText(getActivity(), i+"", Toast.LENGTH_SHORT).show();
                if (jobTypeWindow.isShowing()) {
                    jobTypeWindow.dismiss();
                }
            }
        });
        //薪资筛选
        ListView charge_listView = (ListView) jobChargeView.findViewById(R.id.jd_charge_list);
        String[] chargeLists = {"不限","3k-5k","5k-10k","10k-15k","15k-20k","20k-30k","30k-50k"};
        List<String> chargeList = new ArrayList<String>();
        for (String s:chargeLists) {
            chargeList.add(s);
        }
        CommonAdapter<String> charge_adapter = new CommonAdapter<String>(getActivity(),chargeList,R.layout.pop_item) {
            @Override
            public void convert(ViewHolder helper, String item) {
                helper.setText(R.id.pop_item,item);
            }
        };
        charge_listView.setAdapter(charge_adapter);
        charge_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //doSearchJobBy(3,i);
                Toast.makeText(getActivity(), i+"", Toast.LENGTH_SHORT).show();
                if (jobChargeWindow.isShowing()) {
                    jobChargeWindow.dismiss();
                }
            }
        });
    }
    //按照筛选结果查找职位
    private void doSearchJobBy(int num,int i) {
        switch (num){
            case 1://
                break;
            case 2:
                break;
            case 3:
                break;
            default:
                break;
        }
    }

    private void initData() {
        jobInfolist = new ArrayList<>();

        jobInfolist.add(new JobInfo(1,"Android工程师","负责公司已有Android项目的开发和维护",1,1,2,1,"Android常用框架，熟练使用Git等版本控制工具"));
        jobInfolist.add(new JobInfo(2,"iOS工程师","负责公司已有iOS项目的开发和维护",1,2,3,1,"Android常用框架，熟练使用Git等版本控制工具"));
        jobInfolist.add(new JobInfo(2,"iOS工程师","负责公司已有iOS项目的开发和维护",1,2,4,1,"Android常用框架，熟练使用Git等版本控制工具"));
        jobInfolist.add(new JobInfo(2,"iOS工程师","负责公司已有iOS项目的开发和维护",1,2,1,1,"Android常用框架，熟练使用Git等版本控制工具"));
        jobInfolist.add(new JobInfo(1,"Android工程师","负责公司已有Android项目的开发和维护",1,1,2,1,"Android常用框架，熟练使用Git等版本控制工具"));
        jobInfolist.add(new JobInfo(2,"iOS工程师","负责公司已有iOS项目的开发和维护",1,2,3,1,"Android常用框架，熟练使用Git等版本控制工具"));
        jobInfolist.add(new JobInfo(2,"iOS工程师","负责公司已有iOS项目的开发和维护",1,2,4,1,"Android常用框架，熟练使用Git等版本控制工具"));
        jobInfolist.add(new JobInfo(2,"iOS工程师","负责公司已有iOS项目的开发和维护",1,2,1,1,"Android常用框架，熟练使用Git等版本控制工具"));
        jobInfolist.add(new JobInfo(1,"Android工程师","负责公司已有Android项目的开发和维护",1,1,2,1,"Android常用框架，熟练使用Git等版本控制工具"));
        jobInfolist.add(new JobInfo(2,"iOS工程师","负责公司已有iOS项目的开发和维护",1,2,3,1,"Android常用框架，熟练使用Git等版本控制工具"));
        jobInfolist.add(new JobInfo(2,"iOS工程师","负责公司已有iOS项目的开发和维护",1,2,4,1,"Android常用框架，熟练使用Git等版本控制工具"));
        jobInfolist.add(new JobInfo(2,"iOS工程师","负责公司已有iOS项目的开发和维护",1,2,1,1,"Android常用框架，熟练使用Git等版本控制工具"));

        jobInfoAdapter = new CommonAdapter<JobInfo>(getActivity(),jobInfolist,R.layout.fragment_hire_item) {
            @Override
            public void convert(ViewHolder helper, JobInfo item) {
                helper.setText(R.id.job_item_jobName,item.getJobName());
                helper.setText(R.id.job_item_jobCharge,item.getJobCharge()>2?(item.getJobCharge()>3?(item.getJobCharge()>4?"15K+":"8K~15K"):"5K~8k"):"5K以下");
            }
        };
        jobListView.setAdapter(jobInfoAdapter);
    }

    @Override
    public void onDestroy() {
        ViewGroup parent = (ViewGroup) hireFragmentView.getParent();
        if (parent!=null){
            parent.removeView(hireFragmentView);
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
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

        if (!popWindow.isShowing()){
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
        }else{
            popWindow.dismiss();
        }
    }
}


