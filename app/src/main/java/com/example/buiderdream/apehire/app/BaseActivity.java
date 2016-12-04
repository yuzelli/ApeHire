package com.example.buiderdream.apehire.app;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.buiderdream.apehire.R;

/**
 *Created by Administrator on 2016/12/3.
 * 配置baseActivity
 * @author 李秉龙
 */
public class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }
}
