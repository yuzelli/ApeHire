package com.example.buiderdream.apehire.view.activitys;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.buiderdream.apehire.R;
import com.example.buiderdream.apehire.base.BaseActivity;

import java.io.File;

public class UserEditActivity extends BaseActivity  implements View.OnClickListener{
    private ImageView img_headImg;  //头像
    private TextView tv_trueName;   //真实姓名
    private TextView tv_sex;   //性别
    private TextView tv_age;   //年龄
    private TextView tv_education;   //学历
    private TextView tv_graduate;   //毕业学校
    private TextView tv_salary;   //薪资
    private TextView tv_experience;   //项目经验
    private TextView tv_superiority;   //我的优势
    private Button btn_upload;   //上传

    private RelativeLayout rl_headImg;   //头像布局模块
    private RelativeLayout rl_realName;   //头真实姓名布局模块
    private RelativeLayout rl_sex;   //性别布局模块
    private RelativeLayout rl_age;   //年龄布局模块
    private RelativeLayout rl_education;   //学历布局模块
    private RelativeLayout rl_graduate;   //毕业学校布局模块
    private RelativeLayout rl_salary;   //薪资布局模块
    private RelativeLayout rl_experience;   //项目经验布局模块
    private RelativeLayout rl_superiority;   //我的优势布局模块

    /**
     * 定义三种状态
     */
    private static final int HEAD_PORTRAIT_PIC = 1;//相册
    private static final int HEAD_PORTRAIT_CAM = 2;//相机
    private static final int HEAD_PORTRAIT_CUT = 3;//图片裁剪
    private File photoFile;
    private Bitmap photoBitmap;
    private String userHeadImgUrl;   // 图片地址

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit);
        initView();
    }

    private void initView() {
        img_headImg = (ImageView) this.findViewById(R.id.img_headImg);
        tv_trueName = (TextView) this.findViewById(R.id.tv_trueName);
        tv_sex = (TextView) this.findViewById(R.id.tv_sex);
        tv_age = (TextView) this.findViewById(R.id.tv_age);
        tv_education = (TextView) this.findViewById(R.id.tv_education);
        tv_graduate = (TextView) this.findViewById(R.id.tv_graduate);
        tv_salary = (TextView) this.findViewById(R.id.tv_salary);
        tv_experience = (TextView) this.findViewById(R.id.tv_experience);
        tv_salary = (TextView) this.findViewById(R.id.tv_salary);
        tv_superiority = (TextView) this.findViewById(R.id.tv_superiority);
        btn_upload = (Button) this.findViewById(R.id.btn_upload );

        rl_headImg = (RelativeLayout) this.findViewById(R.id.rl_headImg);
        rl_realName = (RelativeLayout) this.findViewById(R.id.rl_realName);
        rl_sex = (RelativeLayout) this.findViewById(R.id.rl_sex);
        rl_age = (RelativeLayout) this.findViewById(R.id.rl_age);
        rl_education = (RelativeLayout) this.findViewById(R.id.rl_education);
        rl_graduate = (RelativeLayout) this.findViewById(R.id.rl_graduate);
        rl_salary = (RelativeLayout) this.findViewById(R.id.rl_salary);
        rl_experience = (RelativeLayout) this.findViewById(R.id.rl_experience);
        rl_superiority = (RelativeLayout) this.findViewById(R.id.rl_superiority);

        rl_headImg.setOnClickListener(this);
        rl_realName.setOnClickListener(this);
        rl_sex.setOnClickListener(this);
        rl_age.setOnClickListener(this);
        rl_education.setOnClickListener(this);
        rl_graduate.setOnClickListener(this);
        rl_salary.setOnClickListener(this);
        rl_experience.setOnClickListener(this);
        rl_superiority.setOnClickListener(this);
    }

    public static void actionStart(Context context){
        Intent intent = new Intent(context,UserEditActivity.class);
        context.startActivity(intent);
    }

    //-----------------------------接口回调---------------------------------
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_upload:
                break;
            case R.id.rl_headImg:
                break;
            case R.id.rl_realName:
                break;
            case R.id.rl_sex:
                break;
            case R.id.rl_age:
                break;
            case R.id.rl_education:
                break;
            case R.id.rl_graduate:
                break;
            case R.id.rl_salary:
                break;
            case R.id.rl_experience:
                break;
            case R.id.rl_superiority:
                break;
            default:
                break;
        }

    }
}
