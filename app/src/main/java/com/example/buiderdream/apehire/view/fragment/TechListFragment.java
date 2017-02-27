package com.example.buiderdream.apehire.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.buiderdream.apehire.R;
import com.example.buiderdream.apehire.adapter.TeachListAdapter;
import com.example.buiderdream.apehire.constants.ConstantUtils;
import com.example.buiderdream.apehire.entity.Teachnology;
import com.example.buiderdream.apehire.https.OkHttpClientManager;
import com.example.buiderdream.apehire.utils.GsonUtils;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

/**
 * Created by Admin on 2017/2/24.
 */

public class TechListFragment extends Fragment {
    private  View techlistFragView;
    private ListView lv;
    private TeachListAdapter adapter;
    private  String type;
    private Context context;
    private List<Teachnology> list;
    private  TeachListFragmentHandler handler;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (techlistFragView == null){
            techlistFragView = inflater.inflate(R.layout.fragment_tech_list, container,false);
        }
        if (techlistFragView != null){
            return techlistFragView;
        }
        return  inflater.inflate(R.layout.fragment_tech_list,null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (view !=null){
            context = getActivity();
            type = getArguments().getString("type");
            handler = new TeachListFragmentHandler();
            initView();
            addData();
            upDataList();


        }
    }

    private void upDataList() {

        adapter.addList(list);
//        adapter.notifyDataSetChanged();
        lv.setAdapter(adapter);
    }

    private void addData() {

        OkHttpClientManager manager = OkHttpClientManager.getInstance();
        Map<String, String> map = new HashMap<>();
        map.put("node_name",type);
        String url = OkHttpClientManager.attachHttpGetParams(ConstantUtils.V2EX_ARTICLE+ConstantUtils.V2EX_NODE,map);
        manager.getAsync(url,new OkHttpClientManager.DataCallBack(){

            @Override
            public void requestFailure(Request request, IOException e) {
                Toast.makeText(context, "请求失败！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void requestSuccess(String result) throws Exception {

                list = GsonUtils.jsonToArrayList(result,Teachnology.class);
                handler.sendEmptyMessage(ConstantUtils.TEACHLOGY_GET_DATA);
            }
        });


    }

    private void initView() {

        lv = (ListView) techlistFragView.findViewById(R.id.frag_list_lv);
        adapter = new TeachListAdapter(getActivity());
        lv.setAdapter(adapter);
        list = new ArrayList<Teachnology>();

    }

    class  TeachListFragmentHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ConstantUtils.TEACHLOGY_GET_DATA:
                    upDataList();
                    break;
                default:
                    break;
            }
        }
    }

}
