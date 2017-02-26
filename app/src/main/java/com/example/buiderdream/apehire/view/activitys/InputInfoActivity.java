package com.example.buiderdream.apehire.view.activitys;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.buiderdream.apehire.R;
import com.example.buiderdream.apehire.base.BaseActivity;
import com.example.buiderdream.apehire.constants.ConstantUtils;

public class InputInfoActivity extends BaseActivity implements View.OnClickListener{
    private TextView tv_title;  //标题
    private TextView tv_confirm;  //确认
    private EditText et_userInput;  //用户短输入
    private EditText et_userLongInput;  //用户短输入
    private ImageView img_back;  //后退
    private int editType;   //传过来的对应标记0：真实姓名；1 年龄； 2 毕业学校 ；3：公司名称；4 公司地址；、
    // 传过来的对应标记 5 公司介绍,6 项目经验  ； 7我的优势；

    private String[] editTypeTitle = {"真实姓名","年龄","毕业学校","薪资水平","公司名称","公司地址","公司介绍","项目经验","我的优势"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_info);
        initView();
        updateView();
    }

    private void initView() {
        tv_title = (TextView) this.findViewById(R.id.tv_title);
        tv_confirm = (TextView) this.findViewById(R.id.tv_confirm);
        et_userInput = (EditText) this.findViewById(R.id.et_userInput);
        et_userLongInput = (EditText) this.findViewById(R.id.et_userLongInput);
        img_back = (ImageView) this.findViewById(R.id.img_back);
        tv_confirm.setOnClickListener(this);
        img_back.setOnClickListener(this);
    }

    /**
     * 更新视图
     */
    private void updateView() {
        Intent intent = getIntent();
        editType = intent.getIntExtra("editType",-1);
        if (editType!=-1){
            tv_title.setText(editTypeTitle[editType]);
            if(editType>4){
                et_userLongInput.setVisibility(View.VISIBLE);
                et_userInput.setVisibility(View.GONE);
            }else {
                et_userLongInput.setVisibility(View.GONE);
                et_userInput.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 返回值
     */
    private void returnResult() {
        String value = null;
        if (editType>4) {
            value  = et_userLongInput.getText().toString().trim();
        }else {
            value  = et_userInput.getText().toString().trim();
        }
        Intent intent = new Intent();
        intent.putExtra("editType",editType);
        intent.putExtra("result", value);
        //设置回传的意图p
        setResult(ConstantUtils.EDIT_USER_INFO_ACTIVITY_RESULT_CODE, intent);

    }
//-------------------------------回调接口-----------------------------------------
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.img_back:
                finish();
                break;
            case  R.id.tv_confirm:
                returnResult();
                finish();
                break;
            default:
                break;
        }
    }


}
