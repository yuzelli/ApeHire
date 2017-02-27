package com.example.buiderdream.apehire.view.activitys;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.support.v7.widget.AppCompatSpinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buiderdream.apehire.R;
import com.example.buiderdream.apehire.base.BaseActivity;
import com.example.buiderdream.apehire.bean.UserInfo;
import com.example.buiderdream.apehire.constants.ConstantUtils;
import com.example.buiderdream.apehire.https.OkHttpClientManager;
import com.example.buiderdream.apehire.utils.ActivityCollectorUtil;
import com.example.buiderdream.apehire.utils.ImageUtils;
import com.example.buiderdream.apehire.utils.LxQiniuUploadUtils;
import com.example.buiderdream.apehire.utils.SharePreferencesUtil;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qiniu.android.http.ResponseInfo;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

/**
 * 编辑用户信息
 *
 * @author 李秉龙
 */
public class UserEditActivity extends BaseActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private ImageView img_headImg;  //头像
    private ImageView img_back;  //后退建
    private TextView tv_trueName;   //真实姓名
    private RadioGroup rg_sex;  //性别单选组
    private RadioButton radio_man;  //男
    private RadioButton radio_woman;  //女
    private TextView tv_age;   //年龄
    private TextView tv_phoneNumber;   //手机号
    private AppCompatSpinner spinner_education;  //学历
    private AppCompatSpinner spinner_salary;  //薪资
    private TextView tv_graduate;   //毕业学校
    private TextView tv_experience;   //项目经验
    private TextView tv_superiority;   //我的优势
    private TextView tv_exit;   //退出
    private Button btn_upload;   //上传


    private RelativeLayout rl_headImg;   //头像布局模块
    private RelativeLayout rl_realName;   //头真实姓名布局模块
    private RelativeLayout rl_age;   //年龄布局模块
    private RelativeLayout rl_graduate;   //毕业学校布局模块
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


    private Context context;
    private UserInfo userInfo;
    private UserEditHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit);
        context = this;
        handler = new UserEditHandler();
        userInfo = (UserInfo) SharePreferencesUtil.readObject(context, ConstantUtils.USER_LOGIN_INFO);
        initView();
        updataView();
    }


    private void initView() {
        img_headImg = (ImageView) this.findViewById(R.id.img_headImg);
        img_back = (ImageView) this.findViewById(R.id.img_back);
        tv_trueName = (TextView) this.findViewById(R.id.tv_trueName);
        tv_exit = (TextView) this.findViewById(R.id.tv_exit);
        rg_sex = (RadioGroup) this.findViewById(R.id.rg_sex);
        radio_man = (RadioButton) this.findViewById(R.id.radio_man);
        radio_woman = (RadioButton) this.findViewById(R.id.radio_woman);
        spinner_education = (AppCompatSpinner) this.findViewById(R.id.spinner_education);
        spinner_salary = (AppCompatSpinner) this.findViewById(R.id.spinner_salary);
        tv_age = (TextView) this.findViewById(R.id.tv_age);
        tv_graduate = (TextView) this.findViewById(R.id.tv_graduate);
        tv_phoneNumber = (TextView) this.findViewById(R.id.tv_phoneNumber);
        tv_experience = (TextView) this.findViewById(R.id.tv_experience);
        tv_superiority = (TextView) this.findViewById(R.id.tv_superiority);
        btn_upload = (Button) this.findViewById(R.id.btn_upload);

        rl_headImg = (RelativeLayout) this.findViewById(R.id.rl_headImg);
        rl_realName = (RelativeLayout) this.findViewById(R.id.rl_realName);
        rl_age = (RelativeLayout) this.findViewById(R.id.rl_age);
        rl_graduate = (RelativeLayout) this.findViewById(R.id.rl_graduate);
        rl_experience = (RelativeLayout) this.findViewById(R.id.rl_experience);
        rl_superiority = (RelativeLayout) this.findViewById(R.id.rl_superiority);


        tv_exit.setOnClickListener(this);
        rl_headImg.setOnClickListener(this);
        img_back.setOnClickListener(this);
        rl_realName.setOnClickListener(this);
        rg_sex.setOnCheckedChangeListener(this);
        rl_age.setOnClickListener(this);
        rl_graduate.setOnClickListener(this);
        rl_experience.setOnClickListener(this);
        rl_superiority.setOnClickListener(this);
        btn_upload.setOnClickListener(this);
    }

    /**
     *
     */
    private void updataView() {
        if (userInfo.getUserGender().equals("男")) {
            rg_sex.check(radio_man.getId());
        } else {
            rg_sex.check(radio_woman.getId());
        }
        final List<String> educationList = new ArrayList<>();
        educationList.add("大专");
        educationList.add("本科");
        educationList.add("硕士");
        educationList.add("博士");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(UserEditActivity.this, android.R.layout.simple_spinner_dropdown_item, educationList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_education.setAdapter(adapter);
        spinner_education.setSelection(userInfo.getUserDegree());
        spinner_education.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userInfo.setUserDegree(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        final List<String> salaryList = new ArrayList<>();
        salaryList.add("不限");
        salaryList.add("3k-5k");
        salaryList.add("5k-10k");
        salaryList.add("10k-15k");
        salaryList.add("15k-20k");
        salaryList.add("20k-30k");
        salaryList.add("30k-50k");
        ArrayAdapter<String> salaryAdapter = new ArrayAdapter<String>(UserEditActivity.this, android.R.layout.simple_spinner_dropdown_item, salaryList);
        salaryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_salary.setAdapter(salaryAdapter);
        spinner_salary.setSelection(userInfo.getUserExpactMonney());
        spinner_salary.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userInfo.setUserExpactMonney(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_loading)
                .showImageOnFail(R.mipmap.ic_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoader.getInstance().displayImage(userInfo.getUserHeadImg(), img_headImg, options);
        tv_trueName.setText(userInfo.getUserTrueName());
        tv_phoneNumber.setText(userInfo.getUserPhoneNum());
        tv_age.setText(userInfo.getUserAge() + "");
        tv_graduate.setText(userInfo.getUserSchool());
        tv_experience.setText(userInfo.getUserExperence());
        tv_superiority.setText(userInfo.getUserAdvantage());
    }


    /**
     * 显示选择头像的对话框
     */
    private void showHeadImgDialog() {
        View contentView = LayoutInflater.from(UserEditActivity.this).inflate(R.layout.popup_headimg, null);
        final PopupWindow mPopWindow = new PopupWindow(contentView);
        mPopWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopWindow.setContentView(contentView);//设置包含视图
        // 控制popupwindow点击屏幕其他地方消失
        RelativeLayout rl_photograph = (RelativeLayout) contentView.findViewById(R.id.rl_photograph);
        RelativeLayout rl_album = (RelativeLayout) contentView.findViewById(R.id.rl_album);
        RelativeLayout rl_default_avatar = (RelativeLayout) contentView.findViewById(R.id.rl_default_avatar);
        TextView tv_cancel = (TextView) contentView.findViewById(R.id.tv_cancel);
        rl_photograph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPhotoGraph();
            }
        });
        rl_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPhotoAlbum();
            }
        });
        rl_default_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opendefaultList();
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopWindow.dismiss();
            }
        });
        View rootview = LayoutInflater.from(UserEditActivity.this).inflate(R.layout.activity_user_edit, null);
        mPopWindow.setBackgroundDrawable(this.getResources().getDrawable(
                R.drawable.bg_popupwindow));// 设置背景图片，不能在布局中设置，要通过代码来设置
        mPopWindow.setOutsideTouchable(true);// 触摸popupwindow外部，popupwindow消失。这个要求你的popupwindow要有背景图片才可以成功，如上
        mPopWindow.setAnimationStyle(R.style.contextPopupAnim);//设置动画
        mPopWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);//设置模式，和Activity的一样，覆盖，调整大小。
        mPopWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
    }


    //打开相册方法
    private void openPhotoAlbum() {
        Intent picIntent = new Intent(Intent.ACTION_PICK, null);
        picIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(picIntent, HEAD_PORTRAIT_PIC);
    }

    //打开相机方法
    private void openPhotoGraph() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if (!file.exists()) {
                file.mkdirs();
            }
            photoFile = new File(file, System.currentTimeMillis() + "");

            Uri photoUri = Uri.fromFile(photoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            startActivityForResult(intent, HEAD_PORTRAIT_CAM);
        } else {

            Toast.makeText(this, "请确认已经插入SD卡", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 打开默认头像
     */
    private void opendefaultList() {
    }

    /**
     * 打开系统图片裁剪功能
     *
     * @param uri
     */
    private void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("scale", true); //黑边
        intent.putExtra("scaleUpIfNeeded", true); //黑边
        intent.putExtra("return-data", true);
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, HEAD_PORTRAIT_CUT);
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, UserEditActivity.class);
        context.startActivity(intent);
    }

    /**
     * 在InputInfoAC中修改个人信息
     *
     * @param i 0：真实姓名；1 年龄； 2 毕业学校 ；3 薪资水平 ；4 项目经验  ； 5 我的优势
     */
    private void updateUserInfo(int i) {
        Intent intent = new Intent(UserEditActivity.this, InputInfoActivity.class);
        intent.putExtra("editType", i);
        startActivityForResult(intent, ConstantUtils.EDIT_USER_INFO_ACTIVITY_CODE);
    }
    /**
     *退出操作
     */
    private void exitUser() {
        LoginActivity.actionStart(context);
        SharePreferencesUtil.saveObject(context,ConstantUtils.USER_LOGIN_INFO,null);
        ActivityCollectorUtil.removeOtherForLogin();
    }

    //-----------------------------接口回调---------------------------------
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tv_exit:
                exitUser();
                break;
            case R.id.img_back:
                finish();
                break;
            case R.id.rl_headImg:
                showHeadImgDialog();
                break;
            case R.id.rl_realName:
                updateUserInfo(0);
                break;
            case R.id.rl_age:
                updateUserInfo(1);
                break;
            case R.id.rl_graduate:
                updateUserInfo(2);
                break;
            case R.id.rl_experience:
                updateUserInfo(7);
                break;
            case R.id.rl_superiority:
                updateUserInfo(8);
                break;
            case R.id.btn_upload:
                uploadUserInfo();
                break;
            default:
                break;
        }

    }

    /**
     * 提交数据
     */
    private void uploadUserInfo() {
        OkHttpClientManager manager = OkHttpClientManager.getInstance();
        Map<String, String> map = new HashMap<>();
        map.put("type", "updateUser");
        map.put("userID", userInfo.getUserId() + "");
        map.put("UserPhoneNum", userInfo.getUserPhoneNum());
        map.put("UserPassword", userInfo.getUserPassword());
        map.put("UserHeadImg", userInfo.getUserHeadImg());
        map.put("UserTrueName", tv_trueName.getText().toString().trim());
        map.put("UserGender", userInfo.getUserGender());
        map.put("UserAge", tv_age.getText().toString().trim());
        map.put("UserDegree", userInfo.getUserDegree() + "");
        map.put("UserSchool", tv_graduate.getText().toString().trim());
        map.put("UserExpactMonney", userInfo.getUserExpactMonney() + "");
        map.put("UserExperence", tv_experience.getText().toString().trim());
        map.put("UserAdvantage", tv_superiority.getText().toString().trim());
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
                    handler.sendEmptyMessage(ConstantUtils.USEREDIT__GET_DATA);
                }
            }
        });
    }


    //回调函数
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case HEAD_PORTRAIT_CAM:
                    startPhotoZoom(Uri.fromFile(photoFile));
                    break;
                case HEAD_PORTRAIT_PIC:
                    if (data == null || data.getData() == null) {
                        return;
                    }
                    startPhotoZoom(data.getData());
                    break;
                case HEAD_PORTRAIT_CUT:
                    if (data != null) {
                        photoBitmap = data.getParcelableExtra("data");
                        img_headImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        img_headImg.setImageBitmap(photoBitmap);
                        try {
                            File SDCardRoot = Environment.getExternalStorageDirectory();
                            if (ImageUtils.saveBitmap2file(photoBitmap)) {

                                String photoPath = SDCardRoot + ConstantUtils.AVATAR_FILE_PATH;
                                //doUploadPicture(photoPath);
                                //  final String StouserHeadImgName =  userInfo.getU_phone() +"_"+ System.currentTimeMillis();
                                final String StouserHeadImgName = "13133443006" + "_" + System.currentTimeMillis();

                                LxQiniuUploadUtils.uploadPic("yuzelloroom", photoPath, StouserHeadImgName, new LxQiniuUploadUtils.UploadCallBack() {
                                    @Override
                                    public void sucess(String url) {
                                        Toast.makeText(context, url, Toast.LENGTH_SHORT).show();
                                        userInfo.setUserHeadImg(ConstantUtils.QN_IMG_ADDRESS + StouserHeadImgName);
                                    }

                                    @Override
                                    public void fail(String key, ResponseInfo info) {
                                        Toast.makeText(context, "shibeile", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
        if (requestCode == ConstantUtils.EDIT_USER_INFO_ACTIVITY_CODE && resultCode == ConstantUtils.EDIT_USER_INFO_ACTIVITY_RESULT_CODE) {
            String result = data.getStringExtra("result");
            int editType = data.getIntExtra("editType", -1);
            if (editType != -1) {
                switch (editType) {
                    case 0:
                        tv_trueName.setText(result);
                        break;
                    case 1:
                        tv_age.setText(result);
                        break;
                    case 2:
                        tv_graduate.setText(result);
                        break;
                    case 7:
                        tv_experience.setText(result);
                        break;
                    case 8:
                        tv_superiority.setText(result);
                        break;
                    default:
                        break;
                }
            } else {
                Toast.makeText(context, "修改失败！", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == radio_man.getId()) {
            userInfo.setUserGender(radio_man.getText().toString());
        }
        if (checkedId == radio_woman.getId()) {
            userInfo.setUserGender(radio_woman.getText().toString());
        }
    }


    class UserEditHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ConstantUtils.USEREDIT__GET_DATA:
                    SharePreferencesUtil.saveObject(context, ConstantUtils.USER_LOGIN_INFO, userInfo);
                    finish();
                    break;
                default:
                    break;
            }
        }
    }
}
