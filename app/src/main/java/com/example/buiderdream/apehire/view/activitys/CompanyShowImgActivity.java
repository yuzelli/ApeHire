package com.example.buiderdream.apehire.view.activitys;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.buiderdream.apehire.R;
import com.example.buiderdream.apehire.base.BaseActivity;
import com.example.buiderdream.apehire.bean.CompanyInfo;
import com.example.buiderdream.apehire.constants.ConstantUtils;
import com.example.buiderdream.apehire.https.OkHttpClientManager;
import com.example.buiderdream.apehire.utils.LxQiniuUploadUtils;
import com.example.buiderdream.apehire.utils.SharePreferencesUtil;
import com.google.gson.Gson;
import com.qiniu.android.http.ResponseInfo;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;

public class CompanyShowImgActivity extends BaseActivity {

    private GridView gridView1;                 //网格显示缩略图
    private Button buttonPublish;              //发布按钮
    private final int IMAGE_OPEN = 1;      //打开图片标记
    private final int GET_DATA = 2;           //获取处理后图片标记
    private final int TAKE_PHOTO = 3;       //拍照标记
    private String pathImage;                     //选择图片路径
    private Bitmap bmp;                             //导入临时图片
    private Uri imageUri;                            //拍照Uri
    private String pathTakePhoto;              //拍照路径
    private ProgressDialog mpDialog;         //进度对话框
    private int count = 0;                           //计算上传图片个数 线程调用

    private int flagThread = 0;                    //线程循环标记变量 否则会上个线程没执行完就进行下面的
    private int flagThreadUpload = 0;         //上传图片控制变量
    private int flagThreadDialog = 0;          //对话框标记变量

    //获取图片上传URL路径 文件夹名+时间命名图片
    private String[] urlPicture;
    //存储Bmp图像
    private ArrayList<HashMap<String, Object>> imageItem;
    //适配器
    private SimpleAdapter simpleAdapter;
    //插入PublishId通过Json解析
    private String publishIdByJson;

    private ArrayList<String> picPaths;   // 图片本机地址
    private ArrayList<String> qiniuPicPaths;   // 图片七牛地址
    private int finishUpload = 0;

    private Context context;
    private CompanyShowImgHandler handler;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
         * 防止键盘挡住输入框
         * 不希望遮挡设置activity属性 android:windowSoftInputMode="adjustPan"
         * 希望动态调整高度 android:windowSoftInputMode="adjustResize"
         */
        getWindow().setSoftInputMode(WindowManager.LayoutParams.
                SOFT_INPUT_ADJUST_PAN);
        //锁定屏幕
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_company_show_img);
        //获取控件对象
        gridView1 = (GridView) findViewById(R.id.gridView1);
        buttonPublish = (Button) findViewById(R.id.btn_upload);

        context = this;
        handler = new CompanyShowImgHandler();
        //发布内容
        buttonPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

        		/*
        		 * 上传图片 进度条显示
        		 * String path = "/storage/emulated/0/DCIM/Camera/lennaFromSystem.jpg";
        		 * upload_SSP_Pic(path,"ranmei");
        		 * Toast.makeText(MainActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
        		 */
                //判断是否添加图片
                if(imageItem.size()==1) {
                    Toast.makeText(CompanyShowImgActivity.this, "没有图片需要上传", Toast.LENGTH_SHORT).show();
                    return;
                }
                uploadPics();
                //消息提示
              //  Toast.makeText(CompanyShowImgActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
            }
        });

        /*
         * 载入默认图片添加图片加号
         * 通过适配器实现
         * SimpleAdapter参数imageItem为数据源 R.layout.griditem_addpic为布局
         */
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.gridview_addpic); //加号
        imageItem = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("itemImage", bmp);
        map.put("pathImage", "add_pic");
        imageItem.add(map);
        simpleAdapter = new SimpleAdapter(this,
                imageItem, R.layout.griditem_addpic,
                new String[] { "itemImage"}, new int[] { R.id.imageView1});
        /*
         * HashMap载入bmp图片在GridView中不显示,但是如果载入资源ID能显示 如
         * map.put("itemImage", R.drawable.img);
         * 解决方法:
         *              1.自定义继承BaseAdapter实现
         *              2.ViewBinder()接口实现
         *  参考 http://blog.csdn.net/admin_/article/details/7257901
         */
        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data,
                                        String textRepresentation) {
                // TODO Auto-generated method stub
                if(view instanceof ImageView && data instanceof Bitmap){
                    ImageView i = (ImageView)view;
                    i.setImageBitmap((Bitmap) data);
                    return true;
                }
                return false;
            }
        });
        gridView1.setAdapter(simpleAdapter);

        /*
         * 监听GridView点击事件
         * 报错:该函数必须抽象方法 故需要手动导入import android.view.View;
         */
        gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                if( imageItem.size() == 10) { //第一张为默认图片
                    Toast.makeText(CompanyShowImgActivity.this, "图片数9张已满", Toast.LENGTH_SHORT).show();
                }
                else if(position == 0) { //点击图片位置为+ 0对应0张图片
                    //Toast.makeText(MainActivity.this, "添加图片", Toast.LENGTH_SHORT).show();
                    AddImageDialog();
                }
                else {
                    DeleteDialog(position);
                    //Toast.makeText(MainActivity.this, "点击第" + (position + 1) + " 号图片",
                    //		Toast.LENGTH_SHORT).show();
                }

            }
        });
        picPaths = new ArrayList<>();
        qiniuPicPaths = new ArrayList<>();
    }

    /**
     * 往七牛上传图片
     */
    private void uploadPics() {
        finishUpload = picPaths.size();
        for (int i = 0 ; i < picPaths.size();i++){
            final String picName = picPaths.get(i);
            LxQiniuUploadUtils.uploadPic("yuzelloroom", picPaths.get(i), picPaths.get(i), new LxQiniuUploadUtils.UploadCallBack() {
                @Override
                public void sucess(String url) {
                    Toast.makeText(context, url, Toast.LENGTH_SHORT).show();
                    String userHeadImgUrl = ConstantUtils.QN_IMG_ADDRESS + picName;
                    qiniuPicPaths.add(userHeadImgUrl);

                    handler.sendEmptyMessage(ConstantUtils.COMPANYSHOWIMG_GET_DATA);
                }

                @Override
                public void fail(String key, ResponseInfo info) {
                    Toast.makeText(context, "失败了", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    //获取图片路径 响应startActivityForResult
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //打开图片
        if(resultCode==RESULT_OK && requestCode==IMAGE_OPEN) {
            Uri uri = data.getData();
            if (!TextUtils.isEmpty(uri.getAuthority())) {
                //查询选择图片
                Cursor cursor = getContentResolver().query(
                        uri,
                        new String[] { MediaStore.Images.Media.DATA },
                        null,
                        null,
                        null);
                //返回 没找到选择图片
                if (null == cursor) {
                    return;
                }
                //光标移动至开头 获取图片路径
                cursor.moveToFirst();
                String path = cursor.getString(cursor
                        .getColumnIndex(MediaStore.Images.Media.DATA));
                //向处理活动传递数据
                //Toast.makeText(this, path, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, ProcessActivity.class); //主活动->处理活动
                intent.putExtra("path", path);
                //startActivity(intent);
                startActivityForResult(intent, GET_DATA);
            } else {
                Intent intent = new Intent(this, ProcessActivity.class); //主活动->处理活动
                intent.putExtra("path", uri.getPath());
                //startActivity(intent);
                startActivityForResult(intent, GET_DATA);
            }
        }  //end if 打开图片
        //获取图片
        if(resultCode==RESULT_OK && requestCode==GET_DATA) {
            //获取传递的处理图片在onResume中显示
            pathImage = data.getStringExtra("pathProcess");
        }
        //拍照
        if(resultCode==RESULT_OK && requestCode==TAKE_PHOTO) {
            Intent intent = new Intent("com.android.camera.action.CROP"); //剪裁
            intent.setDataAndType(imageUri, "image/*");
            intent.putExtra("scale", true);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            //广播刷新相册
            Intent intentBc = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intentBc.setData(imageUri);
            this.sendBroadcast(intentBc);
            //向处理活动传递数据
            Intent intentPut = new Intent(this, ProcessActivity.class); //主活动->处理活动
            intentPut.putExtra("path", pathTakePhoto);
            //startActivity(intent);
            startActivityForResult(intentPut, GET_DATA);
        }
    }

    //刷新图片
    @Override
    protected void onResume() {
        super.onResume();
        //获取传递的处理图片在onResume中显示
        //Intent intent = getIntent();
        //pathImage = intent.getStringExtra("pathProcess");
        //适配器动态显示图片
        if(!TextUtils.isEmpty(pathImage)){
            picPaths.add(pathImage);
            Bitmap addbmp=BitmapFactory.decodeFile(pathImage);
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("itemImage", addbmp);
            map.put("pathImage", pathImage);
            imageItem.add(map);
            simpleAdapter = new SimpleAdapter(this,
                    imageItem, R.layout.griditem_addpic,
                    new String[] { "itemImage"}, new int[] { R.id.imageView1});
            //接口载入图片
            simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                @Override
                public boolean setViewValue(View view, Object data,
                                            String textRepresentation) {
                    // TODO Auto-generated method stub
                    if(view instanceof ImageView && data instanceof Bitmap){
                        ImageView i = (ImageView)view;
                        i.setImageBitmap((Bitmap) data);
                        return true;
                    }
                    return false;
                }
            });
            gridView1.setAdapter(simpleAdapter);
            simpleAdapter.notifyDataSetChanged();
            //刷新后释放防止手机休眠后自动添加
            pathImage = null;
        }
    }

    /*
     * Dialog对话框提示用户删除操作
     * position为删除图片位置
     */
    protected void DeleteDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CompanyShowImgActivity.this);
        builder.setMessage("确认移除已添加图片吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                imageItem.remove(position);
                picPaths.remove(position-1);
                simpleAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /*
     * 添加图片 可通过本地添加、拍照添加
     */
    protected void AddImageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CompanyShowImgActivity.this);
        builder.setTitle("添加图片");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setCancelable(false); //不响应back按钮
        builder.setItems(new String[] {"本地相册选择","手机相机添加","取消选择图片"},
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        switch(which) {
                            case 0: //本地相册
                                dialog.dismiss();
                                Intent intent = new Intent(Intent.ACTION_PICK,
                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(intent, IMAGE_OPEN);
                                //通过onResume()刷新数据
                                break;
                            case 1: //手机相机
                                dialog.dismiss();
                                File outputImage = new File(Environment.getExternalStorageDirectory(), "suishoupai_image.jpg");
                                pathTakePhoto = outputImage.toString();
                                try {
                                    if(outputImage.exists()) {
                                        outputImage.delete();
                                    }
                                    outputImage.createNewFile();
                                } catch(Exception e) {
                                    e.printStackTrace();
                                }
                                imageUri = Uri.fromFile(outputImage);
                                Intent intentPhoto = new Intent("android.media.action.IMAGE_CAPTURE"); //拍照
                                intentPhoto.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                startActivityForResult(intentPhoto, TAKE_PHOTO);
                                break;
                            case 2: //取消添加
                                dialog.dismiss();
                                break;
                            default:
                                break;
                        }
                    }
                });
        //显示对话框
        builder.create().show();
    }

    class CompanyShowImgHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ConstantUtils.COMPANYSHOWIMG_GET_DATA:
                    finishUpload --;
                    Log.d("qiniu add ","--->qiniu"+finishUpload);
                    if (finishUpload==0){
//                        提交后台
                        uploadServlet();
                    }
                    break;
                case ConstantUtils.COMPANYSHOWIMGSERVLET_GET_DATA:
                    finishUpload --;
                    Log.d("houtai add ","--->houtai"+finishUpload);
                    if (finishUpload==0){
                        Toast.makeText(context,"上传成功！",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    break;
                default:
                    break;
            }
        }
    }
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, CompanyShowImgActivity.class);
        context.startActivity(intent);
    }
    /**
     *上传图片到后台
     */
    private void uploadServlet() {
        finishUpload = qiniuPicPaths.size();
        CompanyInfo company = (CompanyInfo) SharePreferencesUtil.readObject(context,ConstantUtils.USER_LOGIN_INFO);
        for (int i = 0 ; i < qiniuPicPaths.size();i++){
            OkHttpClientManager manager = OkHttpClientManager.getInstance();
            Map<String, String> map = new HashMap<>();
            map.put("type", "addPic");
            map.put("PictureURL", qiniuPicPaths.get(i));
            map.put("CompanyId",company.getCompanyId()+"" );
            String url = OkHttpClientManager.attachHttpGetParams(ConstantUtils.USER_ADDRESS+ConstantUtils.COMPANY_PIC_SERVLET, map);
            manager.getAsync(url, new OkHttpClientManager.DataCallBack() {
                @Override
                public void requestFailure(Request request, IOException e) {
                    Toast.makeText(context,"请求失败！",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void requestSuccess(String result) throws Exception {
                    JSONObject object = new JSONObject(result);
                    String flag = object.getString("error");
                    if (flag.equals("ok")){
                        handler.sendEmptyMessage(ConstantUtils.COMPANYSHOWIMGSERVLET_GET_DATA);
                    }
                }
            });
        }
    }

}
