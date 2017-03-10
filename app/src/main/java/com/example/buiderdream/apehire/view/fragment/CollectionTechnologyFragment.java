package com.example.buiderdream.apehire.view.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.buiderdream.apehire.R;
import com.example.buiderdream.apehire.base.BaseFragment;
import com.example.buiderdream.apehire.bean.CollectionArticle;
import com.example.buiderdream.apehire.bean.CompanyInfo;
import com.example.buiderdream.apehire.bean.JobAndCompany;
import com.example.buiderdream.apehire.bean.UserInfo;
import com.example.buiderdream.apehire.constants.ConstantUtils;
import com.example.buiderdream.apehire.entity.Technology;
import com.example.buiderdream.apehire.https.OkHttpClientManager;
import com.example.buiderdream.apehire.utils.ACache;
import com.example.buiderdream.apehire.utils.CommonAdapter;
import com.example.buiderdream.apehire.utils.GsonUtils;
import com.example.buiderdream.apehire.utils.SharePreferencesUtil;
import com.example.buiderdream.apehire.utils.ViewHolder;
import com.example.buiderdream.apehire.view.activitys.MainActivity;
import com.example.buiderdream.apehire.view.activitys.TechnologyActivity;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

/**
 * Created by binglong_li on 2017/2/12.
 * 收藏的技术
 */

public class CollectionTechnologyFragment extends BaseFragment{
    private View collectionTechnologyFragmentView;
    private ListView frag_list_lv;
    private CommonAdapter<CollectionArticle> adapter;
    private List<CollectionArticle> articleList;
    private CollectionTechnologyFragmentHandler handler;
    private UserInfo userInfo;
    private Context context;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (collectionTechnologyFragmentView==null){
            collectionTechnologyFragmentView = inflater.inflate(R.layout.fragment_coll_technology,container,false);
        }
        if (collectionTechnologyFragmentView!=null){
            return collectionTechnologyFragmentView;
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        handler = new CollectionTechnologyFragmentHandler();
        context = getActivity();
        userInfo = (UserInfo) SharePreferencesUtil.readObject(context,ConstantUtils.USER_LOGIN_INFO);
        String result = ACache.get(context).getAsString(userInfo.getUserPhoneNum()+ConstantUtils.COLLECTION_ARTICLE_FRAGMENT_ACACHE);
        if (result!=null&&!result.equals("")) {
            articleList = GsonUtils.jsonToArrayList(result,CollectionArticle.class);
        }else {
            articleList = new ArrayList<>();
        }
        initView();
        updataListView();
        getUserCollArticle();
        updataListView();
    }

    /**
     * 获取数据
     */
    private void getUserCollArticle() {
        OkHttpClientManager manager = OkHttpClientManager.getInstance();
        Map<String, String> map = new HashMap<>();
        map.put("type", "findArtCollByCUserID");
        map.put("UserInfoId",userInfo.getUserId()+"");

        String url = OkHttpClientManager.attachHttpGetParams(ConstantUtils.USER_ADDRESS + ConstantUtils.ARTICLE_COLLECTION_SERVLET, map);
        manager.getAsync(url, new OkHttpClientManager.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                Toast.makeText(context, "请求失败！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void requestSuccess(String result) throws Exception {
                Gson gson = new Gson();
                JSONObject object = new JSONObject(result);
                String flag = object.getString("error");
                if (flag.equals("ok")) {
                    ACache achace = ACache.get(context);
                    articleList = new ArrayList<CollectionArticle>();
                    achace.put(userInfo.getUserPhoneNum()+ConstantUtils.COLLECTION_ARTICLE_FRAGMENT_ACACHE,object.getString("object"));
                    articleList = GsonUtils.jsonToArrayList(object.getString("object"), CollectionArticle.class);
                    handler.sendEmptyMessage(ConstantUtils.COLLECTION_ARTICLE_GET_DATA);
                }
            }
        });
    }

    /**
     * 更新listView
     */
    private void updataListView() {
        adapter = new CommonAdapter<CollectionArticle>(context,articleList,R.layout.item_tech_arti) {
            @Override
            public void convert(ViewHolder helper, CollectionArticle item) {
                if (item.getImgUrl()!=null){
                    helper.setImageByUrl2(R.id.img_userHeader,"http:"+item.getImgUrl());
                }
                helper.setText(R.id.tv_userName,item.getUserName());
                helper.setText(R.id.tv_title,item.getTitle());
                helper.setText(R.id.tv_content,item.getContent());
                helper.setText(R.id.tv_replies,item.getReplies()+"");
            }
        };
        frag_list_lv.setAdapter(adapter);
        frag_list_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CollectionArticle art = articleList.get(position);
                Technology tech = new Technology();
                tech.setId(art.getVexID());
                Technology.MemberBean member = new Technology.MemberBean();
                member.setUsername(art.getUserName());
                member.setAvatar_large(art.getImgUrl());
                member.setAvatar_mini(art.getImgUrl());
                member.setAvatar_normal(art.getImgUrl());
                tech.setMember(member);
                tech.setReplies(Integer.valueOf(art.getReplies()));
                tech.setContent(art.getContent());
              //  tech.setCreated(Integer.valueOf(art.getCreateTime()));
                tech.setTitle(art.getTitle());
                TechnologyActivity.actionStart(context,tech);
            }
        });

        frag_list_lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ShowDeleteDialog(articleList.get(position).getCollectionId());
                return true;
            }
        });

    }

    /**
     * show Delete Dialog
     */
    private void ShowDeleteDialog(final int artCollectionID) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);// 构建
        builder.setTitle("提示框");
        builder.setMessage("你确定要删除么");
        // 添加确定按钮 listener事件是继承与DialogInerface的
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                // 完成业务逻辑代码
                DeleteCollectionTech(artCollectionID);
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
    private void DeleteCollectionTech(int artCollectionID) {
        OkHttpClientManager manager = OkHttpClientManager.getInstance();
        Map<String, String> map = new HashMap<>();
        map.put("type", "deleteArtColl");
        map.put("CollectionId",artCollectionID+"");

        String url = OkHttpClientManager.attachHttpGetParams(ConstantUtils.USER_ADDRESS + ConstantUtils.ARTICLE_COLLECTION_SERVLET, map);
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
                     handler.sendEmptyMessage(ConstantUtils.COLLECTION_ARTICLE_DELET_DATA);
                }
            }
        });
    }

    private void initView() {
        frag_list_lv = (ListView) collectionTechnologyFragmentView.findViewById(R.id.frag_list_lv);

    }

    @Override
    public void onDestroyView() {
        ViewGroup parent = (ViewGroup) collectionTechnologyFragmentView.getParent();
        if (parent!=null){
            parent.removeView(collectionTechnologyFragmentView);
        }
        super.onDestroyView();
    }

    class CollectionTechnologyFragmentHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ConstantUtils.COLLECTION_ARTICLE_GET_DATA:
                    updataListView();
                    break;
                case ConstantUtils.COLLECTION_ARTICLE_DELET_DATA:
                    getUserCollArticle();
                    break;
                default:
                    break;
            }
        }
    }
}
