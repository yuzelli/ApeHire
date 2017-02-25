package com.example.buiderdream.apehire.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.buiderdream.apehire.constants.ConstantUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by 51644 on 2017/2/9.
 * @author 李秉龙
 */

public class JudgeUtils {
    /**
     * 验证电话号码是否符合格式
     * @return true or false
     */
    public static boolean isPhoneEnable(String strPhone,String passWord) {
        boolean b = false;
        if (strPhone.length() == 11) {
            Pattern pattern = null;
            Matcher matcher = null;
            pattern = Pattern.compile("^[1][3,4,5,8][0-9]{9}$"); // 验证手机号
            matcher = pattern.matcher(strPhone);
            b = matcher.matches();
        }
        if (passWord.length()>16){
            b=false;
        }
        return b;
    }

    public static boolean getUserType(Context context){
        SharedPreferences pref = context.getSharedPreferences("date",MODE_PRIVATE);
        return  pref.getBoolean(ConstantUtils.LOCATION_USER_TYPE,true);
    }
    public static void saveUserType(Context context,boolean flag){
        SharedPreferences.Editor editor = context.getSharedPreferences("date",MODE_PRIVATE).edit();
        editor.putBoolean(ConstantUtils.LOCATION_USER_TYPE, flag);
        editor.commit();
    }
}
