package com.example.buiderdream.apehire.view.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buiderdream.apehire.R;
import com.example.buiderdream.apehire.entity.Technology;
import com.example.buiderdream.apehire.https.OkHttpClientManager;
import com.example.buiderdream.apehire.utils.DateUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Admin on 2017/2/28.
 */

public class TechnologyActivity extends Activity{

    private TextView title_tv,content_tv,user_name_tv,time_tv,collection_tv;
    private ImageView iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachnology);
        Intent intent = getIntent();
        Technology technology = (Technology) intent.getSerializableExtra("technology");

        InitView();
         title_tv.setText(technology.getTitle().toString());
        content_tv.setText(technology.getContent().toString());
        user_name_tv.setText(technology.getMember().getUsername().toString());
        String time = DateUtils.converTime(technology.getCreated());
        time_tv.setText("发布于"+time);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading (R.mipmap.ic_loading)
                .showImageOnFail(R.mipmap.ic_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoader.getInstance().displayImage("http:"+technology.getMember().getAvatar_normal(), iv, options);

        collection_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"已经收藏",Toast.LENGTH_LONG).show();
                 addArticle();
            }
        });
    }


    private  void addArticle(){
        OkHttpClientManager manager = OkHttpClientManager.getInstance();
        Map<String, String> map = new HashMap<>();
        map.put("type","addArticle");


    }
    private void InitView() {
        title_tv = (TextView) findViewById(R.id.tech_title_name);
        content_tv = (TextView) findViewById(R.id.tech_content_tv);
        user_name_tv = (TextView) findViewById(R.id.tech_username_tv);
        time_tv = (TextView) findViewById(R.id.tech_usertime_tv);
        iv = (ImageView) findViewById(R.id.tech_user_iv);
        collection_tv = (TextView) findViewById(R.id.tech_collection_tv);
    }

    public static void actionStart(Context context, Technology technology) {
        Intent intent = new Intent(context,TechnologyActivity.class);
        intent.putExtra("technology",technology);
        context.startActivity(intent);
    }
    public void back(View v){

        finish();
    }


}
