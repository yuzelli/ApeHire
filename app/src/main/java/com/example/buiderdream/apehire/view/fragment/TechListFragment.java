package com.example.buiderdream.apehire.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.buiderdream.apehire.R;
import com.example.buiderdream.apehire.constants.ConstantUtils;
import com.example.buiderdream.apehire.entity.Technology;
import com.example.buiderdream.apehire.https.OkHttpClientManager;
import com.example.buiderdream.apehire.utils.CommonAdapter;
import com.example.buiderdream.apehire.utils.GsonUtils;
import com.example.buiderdream.apehire.utils.ViewHolder;
import com.example.buiderdream.apehire.view.activitys.TechnologyActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

/**
 * Created by Admin on 2017/2/24.
 */

public class TechListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private  View techlistFragView;
    private SwipeRefreshLayout srl_fresh;
    private ListView lv;
    private CommonAdapter<Technology> adapter;
    private  String type;
    private Context context;
    private List<Technology> list;
    private  TeachListFragmentHandler handler;
    private ProgressBar pb;
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
            list = new ArrayList<Technology>();
        }
    }

    private void upDataList() {
        lv.setAdapter(adapter);

        adapter = new CommonAdapter<Technology>(context,list,R.layout.item_tech_arti) {
            @Override
            public void convert(ViewHolder helper, Technology item) {
                if (item.getMember().getAvatar_large()!=null){
                    helper.setImageByUrl2(R.id.img_userHeader,"http:"+item.getMember().getAvatar_large());
                }
                helper.setText(R.id.tv_userName,item.getMember().getUsername());
                helper.setText(R.id.tv_title,item.getTitle());
                helper.setText(R.id.tv_content,item.getContent());
                helper.setText(R.id.tv_replies,item.getReplies()+"");
            }
        };
        lv.setAdapter(adapter);
        lv.setEmptyView(techlistFragView.findViewById(R.id.img_emptyView));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TechnologyActivity.actionStart(getActivity(),list.get(position));
            }
        });
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
                srl_fresh.setRefreshing(false);
            }

            @Override
            public void requestSuccess(String result) throws Exception {
                pb.setVisibility(View.GONE);
                list = GsonUtils.jsonToArrayList(result,Technology.class);
                handler.sendEmptyMessage(ConstantUtils.TEACHLOGY_GET_DATA);
                srl_fresh.setRefreshing(false);
            }
        });


    }

    private void initView() {
        pb = (ProgressBar) techlistFragView.findViewById(R.id.frag_tech_pb);
        lv = (ListView) techlistFragView.findViewById(R.id.frag_list_lv);
        srl_fresh = (SwipeRefreshLayout) techlistFragView.findViewById(R.id.srl_fresh);
        srl_fresh.setOnRefreshListener(this);
        srl_fresh.setColorSchemeColors(  getResources().getColor(android.R.color.holo_red_light),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_blue_bright)

        );
    }

    @Override
    public void onRefresh() {
        addData();
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
