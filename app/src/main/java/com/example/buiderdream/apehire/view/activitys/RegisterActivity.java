package com.example.buiderdream.apehire.view.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.example.buiderdream.apehire.R;
import com.example.buiderdream.apehire.base.BaseActivity;
import com.example.buiderdream.apehire.bean.CompanyInfo;
import com.example.buiderdream.apehire.bean.UserInfo;
import com.example.buiderdream.apehire.constants.ConstantUtils;
import com.example.buiderdream.apehire.https.OkHttpClientManager;
import com.example.buiderdream.apehire.utils.JudgeUtils;
import com.example.buiderdream.apehire.utils.SharePreferencesUtil;
import com.google.gson.Gson;
import org.json.JSONObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;

/**
 * 注册界面
 *
 * @author yuzelli
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private EditText et_userPhone;   //用户手机号输入框
    private EditText et_passWord;  //用户密码输入框
    private EditText et_certainPassWord;  //用户密码确认输入框
    private TextView tv_register;   //注册按钮
    private RadioGroup rg_userType;  //性别单选组
    private RadioButton radio_worker;  //求职者
    private RadioButton radio_boss;  //boss

    private UserInfo userInfo;
    private CompanyInfo companyInfo;


    private Context context;
    private boolean userTypeFlag; // 用户判断用户是否boss,还是worker ；worker==true,默认
    private RegisterHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        context = this;
        handler = new RegisterHandler();
        userTypeFlag = JudgeUtils.getUserType(context);
        initView();
        updataView();
    }

    private void updataView() {
        if (userTypeFlag) {
            rg_userType.check(radio_worker.getId());
        } else {
            rg_userType.check(radio_boss.getId());
        }
    }

    private void initView() {
        et_userPhone = (EditText) this.findViewById(R.id.et_userPhone);
        et_passWord = (EditText) this.findViewById(R.id.et_passWord);
        et_certainPassWord = (EditText) this.findViewById(R.id.et_certainPassWord);
        tv_register = (TextView) this.findViewById(R.id.tv_register);
        rg_userType = (RadioGroup) this.findViewById(R.id.rg_userType);
        radio_worker = (RadioButton) this.findViewById(R.id.radio_worker);
        radio_boss = (RadioButton) this.findViewById(R.id.radio_boss);
        tv_register.setOnClickListener(this);
        rg_userType.setOnCheckedChangeListener(this);
    }

    /**
     * 验证失败
     */
    private void errorEdit() {
        et_userPhone.setText("");
        et_userPhone.setText("");
        et_userPhone.setText("");
    }

    private void doRegisterBoss() {
        OkHttpClientManager manager = OkHttpClientManager.getInstance();
        Map<String, String> map = new HashMap<>();
        map.put("type", "register");
        map.put("CompanyNum", et_userPhone.getText().toString().trim());
        map.put("CompanyPassword", et_passWord.getText().toString().trim());
        map.put("CompanyHeadImg", "");
        map.put("CompanyName", "");
        map.put("CompanyAddress", "");
        map.put("CompanyIntroduce", "");
        map.put("CompanyScale", "0");
        String url = OkHttpClientManager.attachHttpGetParams(ConstantUtils.USER_ADDRESS + ConstantUtils.COMPANY_SERVLET, map);
        manager.getAsync(url, new OkHttpClientManager.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                Toast.makeText(context, "请求失败！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void requestSuccess(String result) throws Exception {
                Gson gson = new Gson();
                JSONObject object = new JSONObject(result);
                String flag = object.getString("error");
                if (flag.equals("ok")) {
                    companyInfo = gson.fromJson(object.getString("object"), CompanyInfo.class);
                    handler.sendEmptyMessage(ConstantUtils.REGISTER_GET_DATA);
                }
            }
        });
    }

    /**
     * 注册用户
     */
    private void doRegisterUser() {
        OkHttpClientManager manager = OkHttpClientManager.getInstance();
        Map<String, String> map = new HashMap<>();
        map.put("type", "register");
        map.put("UserPhoneNum", et_userPhone.getText().toString().trim());
        map.put("UserPassword", et_passWord.getText().toString().trim());
        map.put("UserHeadImg", "");
        map.put("UserTrueName", "");
        //map.put("UserTrueName", URLEncoder.encode("李秉龙", "utf-8"));
        map.put("UserGender", "");
        map.put("UserAge", "0");
        map.put("UserDegree", "0");
        map.put("UserSchool", "");
        map.put("UserExpactMonney", "0");
        map.put("UserExperence", "");
        map.put("UserAdvantage", "");
        String url = OkHttpClientManager.attachHttpGetParams(ConstantUtils.USER_ADDRESS + ConstantUtils.USER_SERVLET, map);
        manager.getAsync(url, new OkHttpClientManager.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                Toast.makeText(context, "请求失败！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void requestSuccess(String result) throws Exception {
                Gson gson = new Gson();
                JSONObject object = new JSONObject(result);
                String flag = object.getString("error");
                if (flag.equals("ok")) {
                    userInfo = gson.fromJson(object.getString("object"), UserInfo.class);
                    handler.sendEmptyMessage(ConstantUtils.REGISTER_GET_DATA);
                }
            }
        });
    }
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_register:
                if (!JudgeUtils.isPhoneEnable(et_userPhone.getText().toString().trim(),et_passWord.getText().toString().trim())
                        &&!et_passWord.getText().toString().trim().equals(et_certainPassWord.getText().toString().trim())){
                    errorEdit();
                    break;
                }
                if (userTypeFlag) {
                    doRegisterUser();
                } else {
                    doRegisterBoss();
                }
                break;
            default:
                break;
        }
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == radio_boss.getId()) {
            Toast.makeText(context, "radio_boss", Toast.LENGTH_SHORT).show();
            userTypeFlag = false;
            JudgeUtils.saveUserType(context, userTypeFlag);
        }
        if (checkedId == radio_worker.getId()) {
            Toast.makeText(context, "radio_worker", Toast.LENGTH_SHORT).show();
            userTypeFlag = true;
            JudgeUtils.saveUserType(context, userTypeFlag);
        }
    }


    class RegisterHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ConstantUtils.REGISTER_GET_DATA:
                    if (userTypeFlag) {
                        SharePreferencesUtil.saveObject(context, ConstantUtils.USER_LOGIN_INFO, userInfo);
                    } else {
                        SharePreferencesUtil.saveObject(context, ConstantUtils.USER_LOGIN_INFO, companyInfo);
                    }
                    MainActivity.actionStart(context);
                    finish();
                    break;
                default:
                    break;
            }
        }
    }
}
