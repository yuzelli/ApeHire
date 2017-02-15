package com.example.buiderdream.apehire.view.activitys;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.buiderdream.apehire.R;
import com.example.buiderdream.apehire.base.BaseActivity;

/**
 * 编辑公司信息
 * @author 李秉龙
 */
public class BossEditActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boss_edit);
    }
    public static void actionStart(Context context){
        Intent intent = new Intent(context,BossEditActivity.class);
        context.startActivity(intent);
    }
}

