package com.example.buiderdream.apehire.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.buiderdream.apehire.R;
import com.example.buiderdream.apehire.adapter.TeachListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2017/2/24.
 */

public class TechListFragment extends Fragment {
    private  View techlistFragView;
    private ListView lv;
    private TeachListAdapter adapter;
    private  String type;
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
            type = getArguments().getString("type");
            initView();
            addData();

        }
    }

    private void addData() {
        List<String> list = new ArrayList<String>();
       for (int i=0;i< 20;i++){
           list.add("wenz"+i+type);
       }
        adapter.addList(list);
        adapter.notifyDataSetChanged();



    }

    private void initView() {

        lv = (ListView) techlistFragView.findViewById(R.id.frag_list_lv);
        adapter = new TeachListAdapter(getActivity());
        lv.setAdapter(adapter);
    }
}
