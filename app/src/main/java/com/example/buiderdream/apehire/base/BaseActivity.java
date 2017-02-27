package com.example.buiderdream.apehire.base;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.buiderdream.apehire.R;
import com.example.buiderdream.apehire.utils.ActivityCollectorUtil;

/**
 *Created by Administrator on 2016/12/3.
 * 配置baseActivity
 * @author 李秉龙
 */
public class BaseActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        ActivityCollectorUtil.addActivity(this);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollectorUtil.removeActivity(this);
    }
}
