package com.example.buiderdream.apehire.view.activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.buiderdream.apehire.R;
import com.example.buiderdream.apehire.entity.JobInfo;

/**
 * 职位详情页面
 * @author
 */
public class JobActivity extends AppCompatActivity {
    JobInfo jobInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job);
        Intent intent = getIntent();
        jobInfo = (JobInfo) intent.getSerializableExtra("jobInfo");
    }
}
