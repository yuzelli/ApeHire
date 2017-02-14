package com.example.buiderdream.apehire.view.activitys;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
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
import com.example.buiderdream.apehire.constants.ConstantUtils;
import com.example.buiderdream.apehire.utils.ImageUtils;
import com.example.buiderdream.apehire.utils.LxQiniuUploadUtils;
import com.qiniu.android.http.ResponseInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 编辑用户信息
 *
 * @author 李秉龙
 */
public class UserEditActivity extends BaseActivity implements View.OnClickListener,RadioGroup.OnCheckedChangeListener{
    private ImageView img_headImg;  //头像
    private TextView tv_trueName;   //真实姓名
    private RadioGroup rg_sex;  //性别单选组
    private RadioButton radio_man;  //男
    private RadioButton radio_woman;  //女
    private TextView tv_age;   //年龄
    private AppCompatSpinner spinner_education;  //学历
    private TextView tv_graduate;   //毕业学校
    private TextView tv_salary;   //薪资
    private TextView tv_experience;   //项目经验
    private TextView tv_superiority;   //我的优势
    private Button btn_upload;   //上传


    private RelativeLayout rl_headImg;   //头像布局模块
    private RelativeLayout rl_realName;   //头真实姓名布局模块

    private RelativeLayout rl_age;   //年龄布局模块

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

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit);
        context = this;
        initView();
        updataView();
    }



    private void initView() {
        img_headImg = (ImageView) this.findViewById(R.id.img_headImg);
        tv_trueName = (TextView) this.findViewById(R.id.tv_trueName);
        rg_sex = (RadioGroup) this.findViewById(R.id.rg_sex);
        radio_man = (RadioButton) this.findViewById(R.id.radio_man);
        radio_woman = (RadioButton) this.findViewById(R.id.radio_woman);
        spinner_education  = (AppCompatSpinner) this.findViewById(R.id.spinner_education);
        tv_age = (TextView) this.findViewById(R.id.tv_age);
        tv_graduate = (TextView) this.findViewById(R.id.tv_graduate);
        tv_salary = (TextView) this.findViewById(R.id.tv_salary);
        tv_experience = (TextView) this.findViewById(R.id.tv_experience);
        tv_salary = (TextView) this.findViewById(R.id.tv_salary);
        tv_superiority = (TextView) this.findViewById(R.id.tv_superiority);
        btn_upload = (Button) this.findViewById(R.id.btn_upload);

        rl_headImg = (RelativeLayout) this.findViewById(R.id.rl_headImg);
        rl_realName = (RelativeLayout) this.findViewById(R.id.rl_realName);
        rl_age = (RelativeLayout) this.findViewById(R.id.rl_age);
        rl_graduate = (RelativeLayout) this.findViewById(R.id.rl_graduate);
        rl_salary = (RelativeLayout) this.findViewById(R.id.rl_salary);
        rl_experience = (RelativeLayout) this.findViewById(R.id.rl_experience);
        rl_superiority = (RelativeLayout) this.findViewById(R.id.rl_superiority);


        rl_headImg.setOnClickListener(this);
        rl_realName.setOnClickListener(this);
        rg_sex.setOnCheckedChangeListener(this);
        rl_age.setOnClickListener(this);
        rl_graduate.setOnClickListener(this);
        rl_salary.setOnClickListener(this);
        rl_experience.setOnClickListener(this);
        rl_superiority.setOnClickListener(this);
    }

    /**
     *
     */
    private void updataView() {
        if (false){
            rg_sex.check(radio_man.getId());
        }else {
            rg_sex.check(radio_woman.getId());
        }
        final List<String> educationList = new ArrayList<>();

        educationList.add("大专");
        educationList.add("本科");
        educationList.add("硕士");
        educationList.add("博士");

        ArrayAdapter<String> adapter =new ArrayAdapter<String>(UserEditActivity.this, android.R.layout.simple_spinner_dropdown_item, educationList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_education.setAdapter(adapter);
        spinner_education.setSelection(3);
        spinner_education.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(context,educationList.get(position),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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

    //-----------------------------接口回调---------------------------------
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_upload:
                break;
            case R.id.rl_headImg:
                showHeadImgDialog();
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
                                        userHeadImgUrl = ConstantUtils.QN_IMG_ADDRESS + StouserHeadImgName;
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

    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId==radio_man.getId()){
            Toast.makeText(context,"man",Toast.LENGTH_SHORT).show();
        }
        if (checkedId==radio_woman.getId()){
            Toast.makeText(context,"woman",Toast.LENGTH_SHORT).show();
        }
    }
}
