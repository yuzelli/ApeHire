package com.example.buiderdream.apehire.view.activitys;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.buiderdream.apehire.R;
import com.example.buiderdream.apehire.base.BaseActivity;
import com.example.buiderdream.apehire.bean.PushMessage;
import com.example.buiderdream.apehire.utils.DateUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class PushMessageActivity extends BaseActivity {
    private ImageView img_back;
    private ImageView img_pic;
    private TextView tv_title;
    private TextView tv_time;
    private TextView tv_content;
    private PushMessage message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_message);
        Intent intent = getIntent();
        message = (PushMessage) intent.getSerializableExtra("message");

        initView();

        updataView();

    }

    private void initView() {
        img_back = (ImageView) this.findViewById(R.id.img_back);
        img_pic = (ImageView) this.findViewById(R.id.img_pic);
        tv_title = (TextView) this.findViewById(R.id.tv_title);
        tv_time = (TextView) this.findViewById(R.id.tv_time);
        tv_content = (TextView) this.findViewById(R.id.tv_content);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void updataView() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_loading)
                .showImageOnFail(R.mipmap.ic_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoader.getInstance().displayImage(message.getImg(), img_pic, options);
        tv_title.setText(message.getTitle());
        tv_time.setText(DateUtils.converTime(message.getTime()));
        tv_content.setText(message.getContent());
    }

    public static void actionStart(Context context, PushMessage message){
        Intent intent = new Intent(context,PushMessageActivity.class);
        intent.putExtra("message",message);
        context.startActivity(intent);
    }
}
