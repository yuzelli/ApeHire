package com.example.buiderdream.apehire.view.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
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
import com.example.buiderdream.apehire.utils.NetworkUtils;
import com.example.buiderdream.apehire.utils.SharePreferencesUtil;
import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;

/**
 * 登陆界面
 *
 * @author 李秉龙
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private TextView tv_register;     //注册按钮
    private EditText et_userPhone;    //手机输入框
    private EditText et_passWord;    //密码输入框
    private RadioGroup rg_userType;  //性别单选组
    private RadioButton radio_worker;  //求职者
    private RadioButton radio_boss;  //boss
    private TextView tv_login;       //登陆按钮
    private UserInfo userInfo;
    private CompanyInfo companyInfo;


    private Context context;
    private boolean userTypeFlag; // 用户判断用户是否boss,还是worker ；worker==true,默认
    private LoginHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        handler = new LoginHandler();
        userTypeFlag = JudgeUtils.getUserType(context);
        initView();
        updataView();
    }


    private void initView() {
        tv_register = (TextView) this.findViewById(R.id.tv_register);
        et_userPhone = (EditText) this.findViewById(R.id.et_userPhone);
        et_passWord = (EditText) this.findViewById(R.id.et_passWord);
        rg_userType = (RadioGroup) this.findViewById(R.id.rg_userType);
        radio_worker = (RadioButton) this.findViewById(R.id.radio_worker);
        radio_boss = (RadioButton) this.findViewById(R.id.radio_boss);
        tv_login = (TextView) this.findViewById(R.id.tv_login);
        tv_login.setOnClickListener(this);
        tv_register.setOnClickListener(this);

        rg_userType.setOnCheckedChangeListener(this);
        if (!NetworkUtils.isNetAvailable(this)) {
            Toast.makeText(this, "五网络链接，请检查网络设置！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 更新视图
     */
    private void updataView() {
        tv_register.setText(Html.fromHtml("<U>Sign Up<U>"));
        if (userTypeFlag) {
            rg_userType.check(radio_worker.getId());
        } else {
            rg_userType.check(radio_boss.getId());
        }
    }

    /**
     * 登陆用户
     */
    private void doUserLogin() {
        OkHttpClientManager manager = OkHttpClientManager.getInstance();
        Map<String, String> map = new HashMap<>();
        map.put("type", "login");
        map.put("phone", et_userPhone.getText().toString().trim());
        map.put("passWord", et_passWord.getText().toString().trim());
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
                    handler.sendEmptyMessage(ConstantUtils.LOGIN_GET_DATA);
                } else {
                    handler.sendEmptyMessage(ConstantUtils.LOGIN_GET_DATA_FAILURE);
                }
            }
        });

    }

    private void doCompanyLogin() {
        OkHttpClientManager manager = OkHttpClientManager.getInstance();
        Map<String, String> map = new HashMap<>();
        map.put("type", "login");
        map.put("CompanyNum", et_userPhone.getText().toString().trim());
        map.put("CompanyPassword", et_passWord.getText().toString().trim());
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
                    handler.sendEmptyMessage(ConstantUtils.LOGIN_GET_DATA);
                } else {
                    handler.sendEmptyMessage(ConstantUtils.LOGIN_GET_DATA_FAILURE);
                }
            }
        });
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login:
                if (!JudgeUtils.isPhoneEnable(et_userPhone.getText().toString().trim(), et_passWord.getText().toString().trim())) {
                    handler.sendEmptyMessage(ConstantUtils.LOGIN_GET_DATA_FAILURE);
                    break;
                }
                if (!NetworkUtils.isNetAvailable(context)) {
                    Toast.makeText(this, "五网络链接，请检查网络设置！", Toast.LENGTH_SHORT).show();
                    break;
                }
//                if (userTypeFlag) {
//                    doUserLogin();
//                } else {
//                    doCompanyLogin();
//                }
                loginHuanxing(et_userPhone.getText().toString().trim(), et_passWord.getText().toString().trim());
                break;
            case R.id.tv_register:
                RegisterActivity.actionStart(context);
                break;
            default:
                break;
        }
    }

    /**
     * 登陆环信
     *
     * @param phone
     * @param pass
     */
    private void loginHuanxing(String phone, String pass) {
        EMClient.getInstance().login(phone, pass, new EMCallBack() {
            @Override
            public void onSuccess() {

                handler.sendEmptyMessage(ConstantUtils.HUANXING_LOGIN);
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(context,"登陆失败",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
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

    class LoginHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ConstantUtils.HUANXING_LOGIN:
                    if (userTypeFlag) {
                        doUserLogin();
                    } else {
                        doCompanyLogin();
                    }
                    break;
                case ConstantUtils.LOGIN_GET_DATA:
                    if (userTypeFlag) {
                        SharePreferencesUtil.saveObject(context, ConstantUtils.USER_LOGIN_INFO, userInfo);
                    } else {
                        SharePreferencesUtil.saveObject(context, ConstantUtils.USER_LOGIN_INFO, companyInfo);
                    }
                    MainActivity.actionStart(context);
                    finish();
                    break;
                case ConstantUtils.LOGIN_GET_DATA_FAILURE:
                    Toast.makeText(context, "验证用户失败！请核对您的用户名、密码。", Toast.LENGTH_SHORT).show();
                    et_userPhone.setText("");
                    et_passWord.setText("");
                    break;
                default:
                    break;
            }
        }
    }
}
