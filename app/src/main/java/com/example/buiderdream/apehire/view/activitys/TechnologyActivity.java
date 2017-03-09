package com.example.buiderdream.apehire.view.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buiderdream.apehire.R;
import com.example.buiderdream.apehire.bean.CompanyInfo;
import com.example.buiderdream.apehire.bean.UserInfo;
import com.example.buiderdream.apehire.constants.ConstantUtils;
import com.example.buiderdream.apehire.entity.Technology;
import com.example.buiderdream.apehire.https.OkHttpClientManager;
import com.example.buiderdream.apehire.utils.DateUtils;
import com.example.buiderdream.apehire.utils.JudgeUtils;
import com.example.buiderdream.apehire.utils.SharePreferencesUtil;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;

/**
 * Created by Admin on 2017/2/28.
 */

public class TechnologyActivity extends Activity {

    private TextView title_tv, content_tv, user_name_tv, time_tv, collection_tv;
    private ImageView iv;
    private Technology technology;
    private int tec_id, collection_id;
    private TechnologyHandler handler;
    private UserInfo userInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachnology);
        Intent intent = getIntent();

        technology = (Technology) intent.getSerializableExtra("technology");
        handler = new TechnologyHandler();
        InitView();
        title_tv.setText(technology.getTitle().toString());

        content_tv.setText(technology.getContent().toString());
        user_name_tv.setText(technology.getMember().getUsername().toString());
        String time = DateUtils.converTime(technology.getCreated());
        time_tv.setText("发布于" + time);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_loading)
                .showImageOnFail(R.mipmap.ic_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoader.getInstance().displayImage("http:" + technology.getMember().getAvatar_normal(), iv, options);

        if (!JudgeUtils.getUserType(getApplication())) {
            collection_tv.setVisibility(View.GONE);
        } else {
            userInfo = (UserInfo) SharePreferencesUtil.readObject(getApplicationContext(), ConstantUtils.USER_LOGIN_INFO);
            collection_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    addArticle();
                }
            });
        }

    }


    private void addArticle() {
        OkHttpClientManager manager = OkHttpClientManager.getInstance();
        Map<String, String> map = new HashMap<>();
        map.put("type", "addArticle");
        map.put("VexID", technology.getId() + "");
        map.put("Title", technology.getTitle());
        map.put("Type", technology.getNode().getName());
        map.put("ImgUrl", technology.getNode().getAvatar_normal());
        map.put("UserName", technology.getMember().getUsername());
        map.put("CreateTime", technology.getCreated() + "");
        map.put("Content", technology.getContent());
        map.put("Replies", technology.getReplies() + "");
        String url = OkHttpClientManager.attachHttpGetParams(ConstantUtils.USER_ADDRESS + ConstantUtils.ARTICLE_SERVLET, map);
        manager.getAsync(url, new OkHttpClientManager.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                Toast.makeText(getApplicationContext(), "添加文章失败！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void requestSuccess(String result) throws Exception {

                Toast.makeText(getApplicationContext(), "已经收藏", Toast.LENGTH_LONG).show();
                JSONObject object = new JSONObject(result);
                String flag = object.getString("error");
                if (flag.equals("ok")) {
                    JSONObject object1 = new JSONObject(object.getString("object"));
                    tec_id = object1.getInt("articleID");
                    handler.sendEmptyMessage(ConstantUtils.ARTI_ADD_DATA);
                }

            }
        });


    }

    private void InitView() {
        title_tv = (TextView) findViewById(R.id.tech_title_name);
        content_tv = (TextView) findViewById(R.id.tech_content_tv);
        user_name_tv = (TextView) findViewById(R.id.tech_username_tv);
        time_tv = (TextView) findViewById(R.id.tech_usertime_tv);
        iv = (ImageView) findViewById(R.id.tech_user_iv);
        collection_tv = (TextView) findViewById(R.id.tech_collection_tv);
    }

    public void UserCollectonArti() {
        OkHttpClientManager manager = OkHttpClientManager.getInstance();
        Map<String, String> map = new HashMap<>();
        map.put("type", "addArtColl");
        map.put("UserInfoId", userInfo.getUserId() + "");
        map.put("ArticalId", tec_id + "");
        String url = OkHttpClientManager.attachHttpGetParams(ConstantUtils.USER_ADDRESS + ConstantUtils.ARTICLE_COLLECTION_SERVLET, map);
        manager.getAsync(url, new OkHttpClientManager.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                Toast.makeText(getApplicationContext(), "收藏文章失败！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void requestSuccess(String result) throws Exception {
                Gson gson = new Gson();
                JSONObject object = new JSONObject(result);
                String flag = object.getString("error");
                if (flag.equals("ok")) {
                    JSONObject object1 = new JSONObject(object.getString("object"));
                    collection_id = object1.getInt("ColletionID");
                    handler.sendEmptyMessage(ConstantUtils.COLLECTION_ARTI_ADD_DATA);
                }


            }
        });


    }

    public static void actionStart(Context context, Technology technology) {
        Intent intent = new Intent(context, TechnologyActivity.class);
        intent.putExtra("technology", technology);
        context.startActivity(intent);
    }

    public void back(View v) {

        finish();
    }


    class TechnologyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ConstantUtils.ARTI_ADD_DATA:
                    UserCollectonArti();
                    break;
                case ConstantUtils.COLLECTION_ARTI_ADD_DATA:
                    Toast.makeText(getApplicationContext(), collection_id + "收藏文章ID", Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    }


}
