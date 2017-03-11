package com.example.buiderdream.apehire.view.activitys;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buiderdream.apehire.R;
import com.example.buiderdream.apehire.base.BaseActivity;
import com.example.buiderdream.apehire.bean.JobAndCompany;

import com.example.buiderdream.apehire.constants.ConstantUtils;
import com.example.buiderdream.apehire.https.OkHttpClientManager;

import com.example.buiderdream.apehire.utils.CommonAdapter;
import com.example.buiderdream.apehire.utils.GsonUtils;

import com.example.buiderdream.apehire.utils.ViewHolder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

/**
 * 公司的详细信息
 * @李文捷
 */
public class CompanyInfoActivity extends BaseActivity {
    TextView jd_company_name2,jd_company_address2,jd_company_scale,jd_company_detail,tv_chat;
    ImageView job_company_img;
    ImageView imageView;
    JobAndCompany jobInfo;//职位详情
    ListView allJobOfCompLV;
    List<JobAndCompany> jobInfolist;
    CommonAdapter<JobAndCompany> jobInfoAdapter;

    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_info);

        Intent intent = getIntent();
        jobInfo = (JobAndCompany) intent.getSerializableExtra("jobInfo");
        initView();
        jobInfolist = new ArrayList<>();
        getAllJobByComp();
        context =this;
    }



    private void getAllJobByComp() {
        OkHttpClientManager manager = OkHttpClientManager.getInstance();
        Map<String, String> map = new HashMap<>();
        map.put("type", "selSomeJob");
        map.put("JobType", "");
        map.put("JobCharge", "");
        map.put("JobCity", "");

        map.put("CompanyId", jobInfo.getCompany().getCompanyId()+"");
        String url = OkHttpClientManager.attachHttpGetParams(ConstantUtils.USER_ADDRESS + ConstantUtils.JOB_SERVLET, map);
        manager.getAsync(url, new OkHttpClientManager.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                Toast.makeText(CompanyInfoActivity.this, "获取公司发布职位失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void requestSuccess(String result) throws Exception {
                JSONObject object = new JSONObject(result);
                String flag = object.getString("error");
                if (flag.equals("ok")) {
                    jobInfolist = GsonUtils.jsonToArrayList(object.getString("object"), JobAndCompany.class);

                  initAdapter();
                } else {
                    Toast.makeText(context, "请求数据失败！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initAdapter() {
        final  List<String> jobCharges = new ArrayList<>(Arrays.asList("不限", "3k-5k", "5k-10k", "10k-15k", "15k-20k", "20k-30k", "30k-50k"));

        jobInfoAdapter = new CommonAdapter<JobAndCompany>(CompanyInfoActivity.this,jobInfolist, R.layout.fragment_hire_item) {
            @Override
            public void convert(ViewHolder helper, JobAndCompany item) {
                helper.setText(R.id.job_item_jobName,item.getJobName());
                helper.setText(R.id.job_item_jobCharge,jobCharges.get(item.getJobCharge()));
                helper.setText(R.id.job_item_companyName,item.getCompany().getCompanyName());
                helper.setText(R.id.job_item_companyAddress,item.getCompany().getCompanyAddress());
                helper.setImageByUrl2(R.id.job_item_img,item.getCompany().getCompanyHeadImg());
            }
        };
        allJobOfCompLV.setAdapter(jobInfoAdapter);
        jobInfoAdapter.notifyDataSetChanged();
        allJobOfCompLV.setFocusable(false);
        setListViewHeightBasedOnChildren(allJobOfCompLV);
    }
    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1))+50;
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }
    private void initView() {
        jd_company_name2 = (TextView) findViewById(R.id.jd_company_name2);
        jd_company_address2 = (TextView) findViewById(R.id.jd_company_address2);
        jd_company_scale = (TextView) findViewById(R.id.jd_company_scale);
        jd_company_detail = (TextView) findViewById(R.id.jd_company_detail);
        tv_chat = (TextView) findViewById(R.id.tv_chat);
        job_company_img = (ImageView) findViewById(R.id.jd_company_img);
        imageView = (ImageView) findViewById(R.id.imageView);
        allJobOfCompLV = (ListView) findViewById(R.id.allJobOfComp);

        //公司详情赋值
        jd_company_name2.setText(jobInfo.getCompany().getCompanyName());
        jd_company_address2.setText(jobInfo.getCompany().getCompanyAddress());
        jd_company_detail.setText(jobInfo.getCompany().getCompanyIntroduce());
        if(jobInfo.getCompany().getCompanyHeadImg()!=null||jobInfo.getCompany().getCompanyHeadImg()!=""){
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.mipmap.ic_loading)
                    .showImageOnFail(R.mipmap.ic_error)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
            ImageLoader.getInstance().displayImage(jobInfo.getCompany().getCompanyHeadImg(), job_company_img, options);
        }
        int scale = jobInfo.getCompany().getCompanyScale();
        final List<String> scaleList = new ArrayList<>(Arrays.asList("0-20", "20-99", "100-499", "500-999", "1000+"));
        jd_company_scale.setText(scaleList.get(scale)+"人");

        allJobOfCompLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(CompanyInfoActivity.this, JobActivity.class);
                intent.putExtra("jobInfo",jobInfolist.get(i));
                intent.putExtra("isSend",false);
                startActivity(intent);

            }
        });
        tv_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("--->",jobInfo.getCompany().getCompanyNum());
                ChatActivity.actionStart(CompanyInfoActivity.this,jobInfo.getCompany().getCompanyNum());
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void backClick(View v){
        finish();
    }
}
