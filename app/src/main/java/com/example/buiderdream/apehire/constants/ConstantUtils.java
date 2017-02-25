package com.example.buiderdream.apehire.constants;

import android.content.SharedPreferences;

import com.example.buiderdream.apehire.utils.SharePreferencesUtil;

import java.util.StringTokenizer;

/**
 * Created by Administrator on 2016/12/4.
 */

public class ConstantUtils {

    //public static final int BOSSMINEFRAGMENT_GET_BEFORE_DATA = ;

    //-------------------handler------------------
    //闪屏页
    public static final int GUIDE_START_ACTIVITY = 0x00001000;
    //用户登录成功
    public static final int LOGIN_GET_DATA = 0x00001001;
    public static final int LOGIN_GET_DATA_FAILURE = 0x00001002;
    public static final int REGISTER_GET_DATA = 0x00001003;
    //--------------------startActivityForResultCode-------------------------
    //编辑用户信息
    public static final int EDIT_USER_INFO_ACTIVITY_CODE = 1000;
    public static final int EDIT_USER_INFO_ACTIVITY_RESULT_CODE = 1001;


    //主机地址
    public static final String USER_ADDRESS = "http://192.168.191.2:8080/ApeHire/";
    //用户表：
    public static final String USER_SERVLET = "userInfoServlet";
    //公司
    public static final String COMPANY_SERVLET = "companyServlet";
    //文章
    public static final String ARTICLE_SERVLET = "articleServlet";
    //公司图片
    public static final String COMPANY_PIC_SERVLET = "companyPicServlet";
    //收藏工作
    public static final String JOB_COLLECTION_SERVLET = "jobCollectionServlet";
    //收藏文章
    public static final String ARTICLE_COLLECTION_SERVLET = "articleCollectionServlet";


//    SharedPreferences
    //登录用户信息
    public static final String USER_LOGIN_INFO = "UserInfo";
    public static final String LOCATION_USER_TYPE = "Location_user_type";

    //七牛
    public static final String QN_ACCESSKEY = "1lz3oyLnZAMG3r0o6hsRUY_U45E58nb9-Q2mCzp8";
    public static final String QN_SECRETKEY = "1no9Tx1bAHSOC0g3xABHhsYXPbRKX_v3o_uGI0Nv";
    public static final String QN_IMG_ADDRESS = "http://ojterpx44.bkt.clouddn.com/";
    //用户头像存放文件名
    public static final String AVATAR_FILE_PATH = "/userHeadImg.jpg";


}
