package com.example.buiderdream.apehire.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.example.buiderdream.apehire.R;
import com.example.buiderdream.apehire.adapter.TeachFragAdapter;
import com.example.buiderdream.apehire.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/4.
 */

public class TechnologyFragment extends BaseFragment {
    private View technologyFragmentView;
    private PagerSlidingTabStrip pst;
    private ViewPager vp;


    TeachFragAdapter adapter;
    TechListFragment techListFragment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        if (technologyFragmentView==null){
            technologyFragmentView = inflater.inflate(R.layout.fragment_technology, container,false);
        }
        if (technologyFragmentView!=null){
            return technologyFragmentView;

         }
        return inflater.inflate(R.layout.fragment_technology, null);
    }
     

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (view!=null){
            initView();
            vp.setAdapter(adapter);

            addData();
            pst.setViewPager(vp);
        }
    }

    private void addData() {
        List<String> title_list =  new ArrayList<String>();
        title_list.add("android");
        title_list.add("js");
        title_list.add("c");
        title_list.add("html");
        title_list.add("mysql");
        title_list.add("php");
        title_list.add("java");
        title_list.add("git");
        List<Fragment> fragment_list = new ArrayList<Fragment>();
        for (int i=0;i<title_list.size();i++)
        {
            TechListFragment fragment = new TechListFragment();
            Bundle bundle = new Bundle();
            bundle.putString("type",title_list.get(i));
            fragment.setArguments(bundle);
            fragment_list.add(fragment);
        }
        adapter.addTitle(title_list);
        adapter.addFragment(fragment_list);
        adapter.notifyDataSetChanged();
        vp.setOffscreenPageLimit(3);

    }

    /**
     *   初始化布局
     */
    private void initView() {
        pst = (PagerSlidingTabStrip) technologyFragmentView.findViewById(R.id.frag_tech_strip);
        vp = (ViewPager) technologyFragmentView.findViewById(R.id.frag_tech_vpss);
        adapter = new TeachFragAdapter(getChildFragmentManager());

    }

    @Override
    public void onDestroy() {
        ViewGroup parent = (ViewGroup) technologyFragmentView.getParent();
        if (parent!=null){
            parent.removeView(technologyFragmentView);
        }
        super.onDestroy();
    }
}
