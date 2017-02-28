package com.example.buiderdream.apehire.view.activitys;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buiderdream.apehire.R;
import com.example.buiderdream.apehire.base.BaseActivity;
import com.example.buiderdream.apehire.bean.CompanyInfo;
import com.example.buiderdream.apehire.constants.ConstantUtils;
import com.example.buiderdream.apehire.https.OkHttpClientManager;
import com.example.buiderdream.apehire.utils.ActivityCollectorUtil;
import com.example.buiderdream.apehire.utils.ImageUtils;
import com.example.buiderdream.apehire.utils.LxQiniuUploadUtils;
import com.example.buiderdream.apehire.utils.NetworkUtils;
import com.example.buiderdream.apehire.utils.SharePreferencesUtil;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qiniu.android.http.ResponseInfo;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

/**
 * 编辑公司信息
 *
 * @author 李秉龙
 */
public class BossEditActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout rl_companyHeadImg;   //头像
    private RelativeLayout rl_companyName;   //公司名称
    private RelativeLayout rl_phoneNumber;   //注册电话
    private RelativeLayout rl_address;   //公司地址
    private RelativeLayout rl_describe;   //公司介绍
    private RelativeLayout rl_CompanyShow;   //公司轮播图片设置

    private ImageView img_companyHeadImg;  //头像
    private TextView tv_exit;  //头像
    private ImageView img_back;  //后退
    private TextView tv_companyName;  //公司名称
    private TextView tv_phoneNumber;  //电话
    private TextView tv_address;  //地址
    private TextView tv_describe;  //介绍
    private Button btn_upload;  //上传
    private AppCompatSpinner spinner_scale;  // 公司规模   0：1-20 , 1 :20-99  , 2 : 100-499;  3 : 500-999 4 :1000+


    /**
     * 定义三种状态
     */
    private static final int HEAD_PORTRAIT_PIC = 1;//相册
    private static final int HEAD_PORTRAIT_CAM = 2;//相机
    private static final int HEAD_PORTRAIT_CUT = 3;//图片裁剪
    private File photoFile;
    private Bitmap photoBitmap;
    private String userHeadImgUrl;   // 图片地址

    private Context context;
    private CompanyInfo company;
    private BossEditHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boss_edit);
        context = this;
        handler = new BossEditHandler();
        company = (CompanyInfo) SharePreferencesUtil.readObject(context, ConstantUtils.USER_LOGIN_INFO);
        if (company==null){
            company = new CompanyInfo();
        }
        initView();
        updataView();
    }

    private void initView() {
        rl_companyHeadImg = (RelativeLayout) this.findViewById(R.id.rl_companyHeadImg);
        rl_companyName = (RelativeLayout) this.findViewById(R.id.rl_companyName);
        rl_phoneNumber = (RelativeLayout) this.findViewById(R.id.rl_phoneNumber);
        rl_address = (RelativeLayout) this.findViewById(R.id.rl_address);
        rl_describe = (RelativeLayout) this.findViewById(R.id.rl_describe);
        rl_CompanyShow = (RelativeLayout) this.findViewById(R.id.rl_CompanyShow);

        img_back = (ImageView) this.findViewById(R.id.img_back);
        tv_exit = (TextView) this.findViewById(R.id.tv_exit);
        img_companyHeadImg = (ImageView) this.findViewById(R.id.img_companyHeadImg);
        tv_companyName = (TextView) this.findViewById(R.id.tv_companyName);
        tv_phoneNumber = (TextView) this.findViewById(R.id.tv_phoneNumber);
        tv_address = (TextView) this.findViewById(R.id.tv_address);
        tv_describe = (TextView) this.findViewById(R.id.tv_describe);
        btn_upload = (Button) this.findViewById(R.id.btn_upload);
        spinner_scale = (AppCompatSpinner) this.findViewById(R.id.spinner_scale);

        img_back.setOnClickListener(this);
        rl_companyHeadImg.setOnClickListener(this);
        rl_companyName.setOnClickListener(this);
        rl_phoneNumber.setOnClickListener(this);
        rl_address.setOnClickListener(this);
        rl_describe.setOnClickListener(this);
        rl_CompanyShow.setOnClickListener(this);
        btn_upload.setOnClickListener(this);
        tv_exit.setOnClickListener(this);

        spinner_scale.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                company.setCompanyScale(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void updataView() {
        final List<String> educationList = new ArrayList<>(Arrays.asList("0-20", "20-99", "100-499", "500-999", "1000+"));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(BossEditActivity.this, android.R.layout.simple_spinner_dropdown_item, educationList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_scale.setAdapter(adapter);
        spinner_scale.setSelection(company.getCompanyScale());
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_loading)
                .showImageOnFail(R.mipmap.ic_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoader.getInstance().displayImage(company.getCompanyHeadImg(), img_companyHeadImg, options);
        tv_companyName.setText(company.getCompanyName());
        tv_phoneNumber.setText(company.getCompanyNum());
        tv_address.setText(company.getCompanyAddress());
        tv_describe.setText(company.getCompanyIntroduce());

    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, BossEditActivity.class);
        context.startActivity(intent);
    }

    /**
     * 显示选择头像的对话框
     */
    private void showHeadImgDialog() {
        View contentView = LayoutInflater.from(BossEditActivity.this).inflate(R.layout.popup_headimg, null);
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
        View rootview = LayoutInflater.from(BossEditActivity.this).inflate(R.layout.activity_user_edit, null);
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

    //---------------------------------接口方法------------------------------------------------
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_exit:
                exitUser();
                break;
            case R.id.rl_companyHeadImg:
                showHeadImgDialog();
                break;
            case R.id.img_back:
                finish();
                break;
            case R.id.rl_companyName:
                updateUserInfo(3);
                break;
            case R.id.rl_address:
                updateUserInfo(4);
                break;
            case R.id.rl_describe:
                updateUserInfo(6);
                break;
            case R.id.rl_CompanyShow:
                CompanyShowImgActivity.actionStart(context);
                break;
            case R.id.btn_upload:
                if (!NetworkUtils.isNetAvailable(context)) {
                    Toast.makeText(this, "五网络链接，请检查网络设置！", Toast.LENGTH_SHORT).show();
                    break;
                }
                updateCompanyInfo();
                break;
            default:
                break;
        }
    }

    /**
     *退出操作
     */
    private void exitUser() {
        LoginActivity.actionStart(context);
        SharePreferencesUtil.saveObject(context,ConstantUtils.USER_LOGIN_INFO,null);
        ActivityCollectorUtil.removeOtherForLogin();
    }

    /**
     * 在InputInfoAC中修改个人信息
     *
     * @param i 3：公司名称；4地址； 6公司介绍
     */
    private void updateUserInfo(int i) {
        Intent intent = new Intent(BossEditActivity.this, InputInfoActivity.class);
        intent.putExtra("editType", i);
        startActivityForResult(intent, ConstantUtils.EDIT_USER_INFO_ACTIVITY_CODE);
    }

    /**
     * 更新公司信息
     */
    private void updateCompanyInfo() {
        OkHttpClientManager manager = OkHttpClientManager.getInstance();
        Map<String, String> map = new HashMap<>();
        map.put("type", "updateCompany");
        map.put("CompanyId", company.getCompanyId() + "");
        map.put("CompanyNum", company.getCompanyNum());
        map.put("CompanyPassword", company.getCompanyPassword());
        map.put("CompanyHeadImg", company.getCompanyHeadImg());
        map.put("CompanyName", tv_companyName.getText().toString().trim());
        map.put("CompanyAddress", tv_address.getText().toString().trim());
        map.put("CompanyIntroduce", tv_describe.getText().toString().trim());
        map.put("CompanyScale", company.getCompanyScale() + "");
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
                    company = gson.fromJson(object.getString("object"), CompanyInfo.class);
                    handler.sendEmptyMessage(ConstantUtils.COMPANYS_UPDATE_GET_DATA);
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
                        img_companyHeadImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        img_companyHeadImg.setImageBitmap(photoBitmap);
                        try {
                            File SDCardRoot = Environment.getExternalStorageDirectory();
                            if (ImageUtils.saveBitmap2file(photoBitmap)) {

                                String photoPath = SDCardRoot + ConstantUtils.AVATAR_FILE_PATH;
                                //doUploadPicture(photoPath);
                                //  final String StouserHeadImgName =  userInfo.getU_phone() +"_"+ System.currentTimeMillis();
                                final String StouserHeadImgName = company.getCompanyNum() + "_" + System.currentTimeMillis();

                                LxQiniuUploadUtils.uploadPic("yuzelloroom", photoPath, StouserHeadImgName, new LxQiniuUploadUtils.UploadCallBack() {
                                    @Override
                                    public void sucess(String url) {
                                        Toast.makeText(context, url, Toast.LENGTH_SHORT).show();
                                        company.setCompanyHeadImg(ConstantUtils.QN_IMG_ADDRESS + StouserHeadImgName);
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
                    case 3:
                        tv_companyName.setText(result);
                        break;
                    case 4:
                        tv_address.setText(result);
                        break;
                    case 6:
                        tv_describe.setText(result);
                        break;
                    default:
                        break;
                }
            } else {
                Toast.makeText(context, "修改失败！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class BossEditHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ConstantUtils.COMPANYS_UPDATE_GET_DATA:
                    SharePreferencesUtil.saveObject(context, ConstantUtils.USER_LOGIN_INFO, company);
                    Toast.makeText(context, "公司信息已经更新", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                default:
                    break;
            }
        }
    }
}

