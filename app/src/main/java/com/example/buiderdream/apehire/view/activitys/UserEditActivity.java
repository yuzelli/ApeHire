package com.example.buiderdream.apehire.view.activitys;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.buiderdream.apehire.R;
import com.example.buiderdream.apehire.base.BaseActivity;

public class UserEditActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit);
    }

    public static void actionStart(Context context){
        Intent intent = new Intent(context,UserEditActivity.class);
        context.startActivity(intent);
    }
}
