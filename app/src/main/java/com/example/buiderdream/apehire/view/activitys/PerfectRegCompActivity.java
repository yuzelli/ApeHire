package com.example.buiderdream.apehire.view.activitys;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.buiderdream.apehire.R;
import com.example.buiderdream.apehire.base.BaseActivity;


/**
 * 注册时完善公司信息
 * @author yuzelli
 */
public class PerfectRegCompActivity extends BaseActivity  {


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfect_reg_comp);

    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, PerfectRegCompActivity.class);
        context.startActivity(intent);
    }
}
