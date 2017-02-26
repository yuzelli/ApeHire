package com.example.buiderdream.apehire.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/4.
 * activity管理器
 * @author 李秉龙
 */
public class ActivityCollectorUtil {
    public static List<Activity> activities = new ArrayList<Activity>();


    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    public static void removeOtherForLogin(){

       for(int i = activities.size()-1;i>=0;i--){
           if (!activities.get(i).isFinishing()) {
               activities.get(i).finish();
           }
       }
    }

}
